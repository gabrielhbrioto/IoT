#include <WiFi.h>
#include <PubSubClient.h>
#include <WiFiManager.h>
#include <Preferences.h>

#define LED_PIN 2               // Pino do LED que pisca quando o reset é iniciado
#define BOOT_BUTTON_PIN 0       // Pino do botão de boot
#define BUTTON_PRESS_TIME 5000  // Tempo em milissegundos (5 segundos) para resetar
#define PIR_PIN 4               // Pino do sensor de presença
#define RELAY_PIN 5             // Pino do modulo rele onde será conectado a lampada
#define CURRENT_SENSOR_PIN 35   // Pino do sensor de corrente

// Variáveis de configuração do broker MQTT
char mqttServer[40];        // Endereço do broker MQTT
char mqttPort[6];           // Porta do broker MQTT como string
char mqttID[20];            // ID do cliente MQTT

// Variáveid dos tópicos MQTT
String topic_medidas;
String topic_luzes;

const char* mqttUser = "USUARIO_MQTT"; // Nome de usuário MQTT, se necessário
const char* mqttPassword = "SENHA_MQTT"; // Senha MQTT, se necessário 

// Variável para armazenar o fuso horário configurado pelo usuário
char timezone[10] = "BRT3"; // Fuso horário padrão (Brasil)

float voltage = 3.3;
float current = 0;
float zero_sensor_value = 0;  // Zero do sensor de corrente
float energyWh = 0;
unsigned long lastTime = 0;
unsigned long lastMovementTime = 0;
int val = 0;  // Guarda o estado de leitura do sensor de presença
bool lightsOn = false;
bool autoLight = true;  // Modo de luz automatico

// Variáveis para monitoramento do botão de reset das configurações
volatile unsigned long buttonPressStart = 0;
volatile bool buttonPressDetected = false;


WiFiClient espClient;
PubSubClient client(espClient);

Preferences preferences; // Instância da NVS

// Função de interrupção chamada ao pressionar o botão
void IRAM_ATTR onBootButtonPress() {
  if (digitalRead(BOOT_BUTTON_PIN) == LOW) { // Botão pressionado
    buttonPressStart = millis();             // Marca o tempo de início
    buttonPressDetected = true;              // Indica que o botão foi pressionado
  } else {
    buttonPressDetected = false;             // Botão solto, reseta o estado
  }
}

// Função para fazer o LED piscar várias vezes antes do reset
void blinkLED(int times, int delayMs) {
  for (int i = 0; i < times; i++) {
    digitalWrite(LED_PIN, HIGH);  // Acende o LED
    vTaskDelay(delayMs / portTICK_PERIOD_MS);
    digitalWrite(LED_PIN, LOW);   // Apaga o LED
    vTaskDelay(delayMs / portTICK_PERIOD_MS);
  }
}

// Reseta todas as configurações salvas e reseta a esp32
void resetConfigurations() {
  Serial.println("Resetando configurações...");

  // Pisca o LED 5 vezes para indicar o reset
  blinkLED(5, 200); // Pisca 5 vezes com 200ms de intervalo

  // Resetar as configurações do WiFiManager
  WiFiManager wifiManager;
  wifiManager.resetSettings();
  
  // Resetar os dados na NVS
  preferences.begin("config", false);
  preferences.clear();
  preferences.end();

  Serial.println("Configurações resetadas. Reiniciando o dispositivo...");
  ESP.restart();
}

// Task que verifica se o botão foi pressionado por tempo suficiente para resetar
void buttonLongPressTask(void* pvParameters) {
  while (true) {
    if (buttonPressDetected && (millis() - buttonPressStart >= BUTTON_PRESS_TIME)) {
      resetConfigurations(); // Chama a função para resetar configurações
    }
    vTaskDelay(100 / portTICK_PERIOD_MS); // Checa a cada 100 ms
  }
}


// Função para salvar configurações na NVS
void saveConfig() {
  preferences.begin("config", false);
  preferences.putString("mqttServer", mqttServer);
  preferences.putString("mqttPort", mqttPort);
  preferences.putString("mqttID", mqttID);
  preferences.putString("timezone", timezone);
  preferences.end();
  Serial.println("Configurações salvas na NVS.");
}

// Função para carregar configurações da NVS
void loadConfig() {
  preferences.begin("config", true); // Abre em modo de leitura
  String server = preferences.getString("mqttServer", "0.0.0.0");
  String port = preferences.getString("mqttPort", "1883");
  String id = preferences.getString("mqttID", "ESP32Client");
  String tz = preferences.getString("timezone", "BRT3");
  preferences.end();

  server.toCharArray(mqttServer, 40);
  port.toCharArray(mqttPort, 6);
  id.toCharArray(mqttID, 20);
  tz.toCharArray(timezone, 10);

  Serial.println("Configurações carregadas da NVS:");
  Serial.println("MQTT Server: " + String(mqttServer));
  Serial.println("MQTT Port: " + String(mqttPort));
  Serial.println("MQTT ID: " + String(mqttID));
  Serial.println("Timezone: " + String(timezone));
}

// Função para reconectar ao broker MQTT
void reconnect() {
  while (!client.connected()) {
    Serial.print("Tentando conexão MQTT...");
    if (client.connect(mqttID, mqttUser, mqttPassword)) {
      Serial.println("Conectado ao broker MQTT");
      client.subscribe(topic_luzes.c_str());
    } else {
      Serial.print("Falha na conexão, rc=");
      Serial.print(client.state());
      Serial.println(" Tentando novamente em 5 segundos");
      delay(5000);
    }
  }
}

String getTime() {
  time_t now;
  struct tm timeinfo;
  if (!getLocalTime(&timeinfo)) {
    Serial.println("Falha ao obter a hora");
    return "Falha ao obter a hora";
  }
  char timeStringBuff[50];
  strftime(timeStringBuff, sizeof(timeStringBuff), "%Y-%m-%d %H:%M:%S", &timeinfo);
  return String(timeStringBuff);
}

// Auto-zero do sensor
void autoZero(){
  Serial.println("Fazendo o Auto ZERO do Sensor...");
  float aux = 0;

  for(int i = 0; i < 1000 ; i++){
    aux += analogRead(CURRENT_SENSOR_PIN);
    delayMicroseconds(1);  
  }
  zero_sensor_value = (float) aux / 1000.0;
}

float readCurrent() {
 float aux = 0;
 float sensorValue = 0;

  for(int i = 0; i < 1000 ; i++){
    aux += analogRead(CURRENT_SENSOR_PIN);
    delayMicroseconds(10);  
  }
  sensorValue = (float) aux / 1000.0;
  return (zero_sensor_value - sensorValue)/66.0 * 0.805 * 10000;
}

void calculateEnergy() {
  current = readCurrent();

  unsigned long currentTime = millis();
  unsigned long timeElapsed = currentTime - lastTime;
  
  energyWh = (voltage * current * timeElapsed) / (1000.0 * 3600.0);
  lastTime = currentTime;
}

void turnOnLights() {
  digitalWrite(RELAY_PIN, HIGH);
  lightsOn = true;
  Serial.println("Luzes acesas");
}

void turnOffLights() {
  digitalWrite(RELAY_PIN, LOW);
  lightsOn = false;
  Serial.println("Luzes apagadas");
}

// Task que controla a iluminadaçao caso esteja no modo automatico
void lightsControlTask(void* pvParameters) {
  while (true) {
    if (autoLight) {
      val = digitalRead(PIR_PIN);
      if (val == HIGH) {
        lastMovementTime = millis();
        if (!lightsOn) {
          turnOnLights();
        }
      }

      if (lightsOn && (millis() - lastMovementTime) > 1 * 5 * 1000) {
        turnOffLights();
      }
    }
    vTaskDelay(100 / portTICK_PERIOD_MS); // Checa a cada 100 ms
  }
}

void callback(char* topic, byte* payload, unsigned int length) {
  String message;

  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
  Serial.print("Mensagem recebida: ");
  Serial.println(message);

  if (String(topic) == topic_luzes.c_str()) {
    if (message == "automatico"){
      lastMovementTime = millis(); // Reseta o tempo da ultima detecção
      autoLight = true;
      Serial.println("Modo automático ativado");
    } else if (message == "acender") {
      autoLight = false;
      turnOnLights();
    } else if (message == "apagar") {
      autoLight = false;
      turnOffLights();
    }
  }
}

// Tenta reconectar ao WiFi novamente
void wifiReconnectTask(void* pvParameters) {
  while (true) {
    if (WiFi.status() != WL_CONNECTED) {
      Serial.println("Conexão Wi-Fi perdida. Tentando reconectar...");
      WiFi.begin();  // Inicia uma tentativa de reconexão ao Wi-Fi
    }
    vTaskDelay(5000 / portTICK_PERIOD_MS); // Verifica a cada 5 segundos
  }
}

void setup() {
  Serial.begin(115200);
  pinMode(BOOT_BUTTON_PIN, INPUT_PULLUP);
  pinMode(PIR_PIN, INPUT);
  pinMode(RELAY_PIN, OUTPUT);
  pinMode(LED_PIN, OUTPUT);
  autoZero();


  // Configuração da interrupção para o botão de boot
  attachInterrupt(digitalPinToInterrupt(BOOT_BUTTON_PIN), onBootButtonPress, CHANGE);
  xTaskCreate(buttonLongPressTask, "Button Long Press Task", 2048, NULL, 1, NULL);

  // Task para controle das luzes
  xTaskCreate(lightsControlTask, "Lights Control Task", 2048, NULL, 1, NULL); 

  // Carrega as configurações salvas na NVS (caso existam)
  loadConfig();

  /********** Configuração do Wi-Fi com WiFiManager ***********/
  WiFiManager wifiManager;
  // Parâmetros personalizados para configuração do broker MQTT e timezone
  WiFiManagerParameter custom_mqtt_server("server", "Endereço broker MQTT", mqttServer, 40);
  WiFiManagerParameter custom_mqtt_port("port", "Porta do broker MQTT", mqttPort, 6);
  WiFiManagerParameter custom_mqtt_id("id", "ID", mqttID, 20);
  WiFiManagerParameter custom_timezone("timezone", "Fuso horário (ex: BRT3)", timezone, 10);

  // Adiciona os parâmetros personalizados ao WiFiManager
  wifiManager.addParameter(&custom_mqtt_server);
  wifiManager.addParameter(&custom_mqtt_port);
  wifiManager.addParameter(&custom_mqtt_id);
  wifiManager.addParameter(&custom_timezone);
  wifiManager.autoConnect("ESP32"); // Nome do AP configurado como "ESP32"

  /********** Configuração do MQTT Server ***********/
  // Atualiza as variáveis com os valores configurados
  strcpy(mqttServer, custom_mqtt_server.getValue());
  strcpy(mqttPort, custom_mqtt_port.getValue());
  strcpy(mqttID, custom_mqtt_id.getValue());
  strcpy(timezone, custom_timezone.getValue());

  // Salva as configurações atualizadas na NVS
  saveConfig();

  // Configura o cliente MQTT com os parâmetros configurados
  client.setServer(mqttServer, atoi(mqttPort)); // Converte o valor da porta para inteiro
  client.setCallback(callback);

  // Configura os topicos MQTT
  topic_medidas = String(mqttID) + "/medidas";
  topic_luzes = String(mqttID) + "/luzes";

  // Sincronização de tempo via NTP com fuso horário
  configTime(0, 0, "pool.ntp.org", "time.nist.gov");
  setenv("TZ", timezone, 1); // Define o fuso horário usando a variável `timezone`
  tzset();
  lastTime = millis();

  xTaskCreate(wifiReconnectTask, "WiFi Reconnect Task", 4096, NULL, 1, NULL);
}

void loop() {
  // Conexão ao broaker MQTT
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  calculateEnergy();
  // Envio de mensagem MQTT com valor e timestamp
  String powerMessage = String(energyWh) + " | " + getTime();
  Serial.println(powerMessage);
  client.publish(topic_medidas.c_str(), powerMessage.c_str());
  energyWh = 0;

  delay(1000);
}
