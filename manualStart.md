# Iniciar mqtt
cd ./Mosquitto-MQTT/
mosquitto -c mosquitto.conf -d

# Iniciar SpringBoot
cd ./BackEnd/iot/
mvn clean install
mvn spring-boot:run

# Iniciar frontend
cd ./WebApp/
http-server -p 8081 dist/
