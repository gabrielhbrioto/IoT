package com.iot.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

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
        //apagar:
        logger.info("ok");
        client.subscribe(topic, (topic1, msg) -> {
            System.out.println("Mensagem recebida em " + topic1 + ": " + new String(msg.getPayload()));
            //apagar:
            logger.info("Mensagem recebida em {}: {} ", topic1, new String(msg.getPayload()));

        });
    }
}
