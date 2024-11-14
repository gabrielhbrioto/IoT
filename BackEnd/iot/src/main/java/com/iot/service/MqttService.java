package com.iot.service;

import com.iot.model.Medida;
import com.iot.model.Sala;
import com.iot.repository.MedidaRepository;
import com.iot.repository.SalaRepository;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import javax.annotation.PostConstruct;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MqttService {

    private MqttClient client;
    
    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Value("${mqtt.client-id}")
    private String clientId;

    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    @Autowired
    private MedidaRepository medidaRepository;

    @Autowired
    private SalaRepository salaRepository;

    public MqttService() {
        
    }

    @PostConstruct
    public void init() throws MqttException {
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(brokerUrl, clientId, persistence);

        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        client.connect(connOpts);
        logger.info("Conexão com MQTT Broker estabelecida em {}", brokerUrl);
        subscribeToAllSalaTopics();
    }

    @PostConstruct
    private void subscribeToAllSalaTopics() {
        salaRepository.findAll()
            .map(Sala::getId)
            .map(id -> id + "/medidas")
            .doOnNext(topic -> {
                try {
                    subscribe(topic);
                    logger.info("Inscrito no tópico: {}", topic);
                } catch (MqttException e) {
                    logger.error("Erro ao se inscrever no tópico {}: {}", topic, e.getMessage());
                }
            })
            .subscribe();
    }

    public void subscribe(String topic) throws MqttException {
        client.subscribe(topic, (receivedTopic, msg) -> {
            logger.info("Mensagem MQTT: {}", msg);

            String[] topicParts = receivedTopic.split("/");
            Long idSala = Long.parseLong(topicParts[0]);
            String[] messageParts = new String(msg.getPayload()).split(" \\| ");
            double valor = Double.parseDouble(messageParts[0]);

            LocalDateTime localDateTime = LocalDateTime.parse(
                messageParts[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ZonedDateTime horario = localDateTime.atZone(ZoneId.systemDefault());

            Medida medida = new Medida();
            medida.setIdSala(idSala);
            medida.setValor(valor);
            medida.setHorario(horario);

            logger.info("Medida: {}", medida);

            registrarMedida(medida).subscribe();
        });
    }

    private Mono<Medida> registrarMedida(Medida medida) {
        return medidaRepository.save(medida);
    }

    public void publish(String topic, String messageContent) {
        try {
            MqttMessage message = new MqttMessage(messageContent.getBytes());
            message.setQos(0);
            client.publish(topic, message);
            logger.info("Mensagem publicada no tópico {}: {}", topic, messageContent);
        } catch (MqttException e) {
            logger.error("Erro ao publicar mensagem no tópico {}: {}", topic, e.getMessage());
        }
    }
}
