#include <WiFi.h>
#include <PubSubClient.h>
#include <time.h>
#include <WiFiManager.h>

// Variáveis de configuração do broker MQTT
char mqttServer[40] = "192.168.1.10"; // Endereço do broker MQTT
char mqttPort[6] = "1883";             // Porta do broker MQTT como string
char mqttID[20] = "ESP32Client";       // ID do cliente MQTT

const char* mqttUser = "USUARIO_MQTT"; // Nome de usuário MQTT, se necessário
const char* mqttPassword = "SENHA_MQTT"; // Senha MQTT, se necessário 

// Pinos dos sensores e relé
const int pirPin = 2;
const int relayPin = 5;
const int voltageSensorPin = 34;
const int currentSensorPin = 35;

float voltage = 0;
float current = 0;
float power = 0;
float energyWh = 0;
unsigned long lastTime = 0;
unsigned long lastMovementTime = 0;
const float voltageCalibration = 220.0;
const float currentCalibration = 10.0;
bool lightsOn = false;

int pirState = LOW;
int val = 0;

WiFiClient espClient;
PubSubClient client(espClient);

// Função para reconectar ao broker MQTT
void reconnect() {
  while (!client.connected()) {
    Serial.print("Tentando conexão MQTT...");
    if (client.connect(mqttID, mqttUser, mqttPassword)) {
      Serial.println("Conectado ao broker MQTT");
      client.subscribe("casa/luzes/controle");
      client.subscribe("casa/luzes/status");
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

float readVoltage() {
  int sensorValue = analogRead(voltageSensorPin);
  return (sensorValue / 4096.0) * 3.3 * voltageCalibration;
}

float readCurrent() {
  int sensorValue = analogRead(currentSensorPin);
  return (sensorValue / 4096.0) * 3.3 * currentCalibration;
}

void calculatePower() {
  voltage = readVoltage();
  current = readCurrent();
  power = voltage * current;
  
  unsigned long currentTime = millis();
  unsigned long timeElapsed = currentTime - lastTime;
  
  energyWh += (power * timeElapsed) / (1000.0 * 3600.0);
  lastTime = currentTime;
}

void turnOnLights() {
  digitalWrite(relayPin, LOW);
  lightsOn = true;
  Serial.println("Luzes acesas");
  client.publish("casa/luzes/status", "Luzes acesas");
}

void turnOffLights() {
  digitalWrite(relayPin, HIGH);
  lightsOn = false;
  Serial.println("Luzes apagadas");
  client.publish("casa/luzes/status", "Luzes apagadas");
}

void callback(char* topic, byte* payload, unsigned int length) {
  String message;
  for (int i = 0; i < length; i++) {
    message += (char)payload[i];
  }
  Serial.print("Mensagem recebida: ");
  Serial.println(message);

  if (String(topic) == "casa/luzes/controle") {
    if (message == "acender") {
      turnOnLights();
    } else if (message == "apagar") {
      turnOffLights();
    }
  }

  if (String(topic) == "casa/luzes/status") {
    if (lightsOn) {
      client.publish("casa/luzes/status", "Luzes acesas");
    } else {
      client.publish("casa/luzes/status", "Luzes apagadas");
    }
  }
}

void setup() {
  Serial.begin(115200);
  pinMode(pirPin, INPUT);
  pinMode(relayPin, OUTPUT);
  digitalWrite(relayPin, HIGH);

  /********** Configuração do Wi-Fi com WiFiManager ***********/
  WiFiManager wifiManager;
  //wifiManager.resetSettings();  // Limpa as redes salvas
  // Parâmetros personalizados para configuração do broker MQTT
  WiFiManagerParameter custom_mqtt_server("server", "Endereço broker MQTT", mqttServer, 40);
  WiFiManagerParameter custom_mqtt_port("port", "Porta do broker MQTT", mqttPort, 6);
  WiFiManagerParameter custom_mqtt_id("id", "ID", mqttID, 20);

  // Adiciona os parâmetros personalizados ao WiFiManager
  wifiManager.addParameter(&custom_mqtt_server);
  wifiManager.addParameter(&custom_mqtt_port);
  wifiManager.addParameter(&custom_mqtt_id);
  wifiManager.autoConnect("ESP32"); // Nome do AP configurado como "ESP32"

  /********** Configuração do MQTT Server ***********/
  // Atualiza as variáveis com os valores configurados
  strcpy(mqttServer, custom_mqtt_server.getValue());
  strcpy(mqttPort, custom_mqtt_port.getValue());
  strcpy(mqttID, custom_mqtt_id.getValue());

  // Configura o cliente MQTT com os parâmetros configurados
  client.setServer(mqttServer, atoi(mqttPort)); // Converte o valor da porta para inteiro
  client.setCallback(callback);

  // Sincronização de tempo via NTP
  configTime(0, 0, "pool.ntp.org", "time.nist.gov");
  lastTime = millis();
}

void loop() {
  // Conexão ao Wi-Fi e ao broker MQTT
  while (WiFi.status() != WL_CONNECTED) {
    Serial.println("Conexão Wi-Fi perdida. Tentando reconectar...");
    WiFiManager wifiManager;
    wifiManager.autoConnect("ESP32");
  }

  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  val = digitalRead(pirPin);

  if (val == HIGH) {
    lastMovementTime = millis();
    if (!lightsOn) {
      turnOnLights();
    }
  }

  if (lightsOn && (millis() - lastMovementTime) > 30 * 60 * 1000) {
    turnOffLights();
  }

  // Envio de mensagem MQTT com valor e timestamp
  int randomNumber = random(1, 100);
  String powerMessage = String(randomNumber) + " | " + getTime();
  Serial.println(powerMessage);
  client.publish("3/medidas", powerMessage.c_str());

  delay(1000);
}
