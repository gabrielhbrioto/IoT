package com.iot.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;
import com.iot.repository.MedidaRepository;
import com.iot.model.Medida;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;

//apagar:
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private MqttClient client;
    private String brokerUrl = "tcp://localhost:1883"; // URL do broker Mosquitto
    private String clientId = "backend-client"; // ID do cliente MQTT
    //apagar:
    private static final Logger logger = LoggerFactory.getLogger(MedidaService.class);

    @Autowired
    private MedidaRepository medidaRepository;

    public MqttService() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(brokerUrl, clientId, persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);
    }

    // public void publish(String topic, String payload) throws MqttException {
    //     MqttMessage message = new MqttMessage(payload.getBytes());
    //     message.setQos(2); // QoS nível 2 (entrega garantida)
    //     client.publish(topic, message);
    // }

    public void subscribe(String topic) throws MqttException {
        client.subscribe(topic, (receivedTopic, msg) -> {
            // Extraindo o idSala do tópico

            //apagar:
            logger.info("Mensagem MQTT: {}", msg);
            String[] topicParts = receivedTopic.split("/");
            Long idSala = Long.parseLong(topicParts[0]);

            // Processando a mensagem
            String[] messageParts = new String(msg.getPayload()).split(" \\| ");
            double valor = Double.parseDouble(messageParts[0]);
            LocalDateTime localDateTime = LocalDateTime.parse(messageParts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            ZonedDateTime horario = localDateTime.atZone(ZoneId.systemDefault());

            // Criando uma nova Medida e salvando no banco
            Medida medida = new Medida();
            medida.setIdSala(idSala);
            medida.setValor(valor);
            medida.setHorario(horario);


            registrarMedida(medida).subscribe();
        });
    }

    private Mono<Medida> registrarMedida(Medida medida) {
        return medidaRepository.save(medida);
    }

}
