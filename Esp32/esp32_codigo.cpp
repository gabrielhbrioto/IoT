#include <WiFi.h>
#include <PubSubClient.h>
#include <time.h>

// Definir as credenciais Wi-Fi
const char* ssid = "SEU_SSID";
const char* password = "SUA_SENHA";

// Definir o endereço e porta do broker MQTT
const char* mqttServer = "SEU_BROKER_MQTT";
const int mqttPort = 1883;  // Porta padrão MQTT
const char* mqttUser = "USUARIO_MQTT";  // Se necessário
const char* mqttPassword = "SENHA_MQTT";  // Se necessário

// Definir pinos dos sensores e relé
const int pirPin = 2;           // Pino do sensor PIR
const int relayPin = 5;         // Pino do relé (para controlar as luzes)
const int voltageSensorPin = 34; // Pino do sensor de tensão (ADC1_6)
const int currentSensorPin = 35; // Pino do sensor de corrente (ADC1_7)

float voltage = 0;
float current = 0;
float power = 0;
float energyWh = 0;
unsigned long lastTime = 0;
unsigned long lastMovementTime = 0;
const float voltageCalibration = 220.0; // Fator de calibração da tensão
const float currentCalibration = 10.0;  // Fator de calibração da corrente
bool lightsOn = false;  // Estado atual das luzes (acesas ou apagadas)

int pirState = LOW;
int val = 0;

WiFiClient espClient;
PubSubClient client(espClient);

// Função para conectar ao Wi-Fi
void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Conectando-se a ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi conectado");
  Serial.println("Endereço IP: ");
  Serial.println(WiFi.localIP());
}

// Função para conectar ao broker MQTT
void reconnect() {
  while (!client.connected()) {
    Serial.print("Tentando conexão MQTT...");
    // Tenta se conectar
    if (client.connect("ESP32Client", mqttUser, mqttPassword)) {
      Serial.println("Conectado ao broker MQTT");
      // Inscrever-se nos tópicos de controle de luz
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

// Função para obter a hora atual
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

// Função para ler o sensor de tensão
float readVoltage() {
  int sensorValue = analogRead(voltageSensorPin);
  // Converter o valor ADC para tensão
  float voltage = (sensorValue / 4096.0) * 3.3 * voltageCalibration;
  return voltage;
}

// Função para ler o sensor de corrente
float readCurrent() {
  int sensorValue = analogRead(currentSensorPin);
  // Converter o valor ADC para corrente
  float current = (sensorValue / 4096.0) * 3.3 * currentCalibration;
  return current;
}

// Função para calcular a potência e energia
void calculatePower() {
  voltage = readVoltage();
  current = readCurrent();
  power = voltage * current;  // Potência instantânea (em Watts)
  
  unsigned long currentTime = millis();
  unsigned long timeElapsed = currentTime - lastTime;
  
  // Calcular a energia (em Wh), convertendo o tempo de milissegundos para horas
  energyWh += (power * timeElapsed) / (1000.0 * 3600.0);  // Wh = W * (ms / 3600000)
  lastTime = currentTime;
}

// Função para acender as luzes
void turnOnLights() {
  digitalWrite(relayPin, LOW);  // Aciona o relé (dependendo da lógica do relé)
  lightsOn = true;
  Serial.println("Luzes acesas");
  client.publish("casa/luzes/status", "Luzes acesas");
}

// Função para apagar as luzes
void turnOffLights() {
  digitalWrite(relayPin, HIGH);  // Desliga o relé
  lightsOn = false;
  Serial.println("Luzes apagadas");
  client.publish("casa/luzes/status", "Luzes apagadas");
}

// Função de callback para tratar mensagens MQTT recebidas
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
  digitalWrite(relayPin, HIGH);  // Garante que as luzes comecem apagadas
  setup_wifi();
  client.setServer(mqttServer, mqttPort);
  client.setCallback(callback);

  // Sincronizar tempo via NTP
  configTime(0, 0, "pool.ntp.org", "time.nist.gov");
  lastTime = millis();
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();

  val = digitalRead(pirPin);

  if (val == HIGH) {
    lastMovementTime = millis();  // Atualiza o tempo do último movimento
    if (!lightsOn) {
      turnOnLights();  // Acende as luzes ao detectar movimento
    }
  }

  // Apagar as luzes após 30 minutos sem movimento
  if (lightsOn && (millis() - lastMovementTime) > 30 * 60 * 1000) {
    turnOffLights();
  }

  // Ler os sensores de tensão e corrente, calcular a potência e energia
  calculatePower();
  
  // Publicar a potência e a energia via MQTT
  String powerMessage = "Potência: " + String(power) + " W";
  String energyMessage = "Energia consumida: " + String(energyWh) + " Wh";
  Serial.println(powerMessage);
  Serial.println(energyMessage);

  client.publish("sensor/potencia", powerMessage.c_str());
  client.publish("sensor/energia", energyMessage.c_str());

  delay(1000);  // Atualizar os dados a cada 1 segundo
}
