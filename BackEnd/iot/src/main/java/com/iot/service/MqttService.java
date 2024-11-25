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

    /**
     * Inicializa um cliente MQTT com persistência em memória, configura opções de conexão (sessão limpa 
     * e reconexão automática), conecta ao broker MQTT e chama a função subscribeToAllSalaTopics() que 
     * se inscreve em todos os tópicos das salas.
     */
    @PostConstruct
    public void init() throws MqttException {
        // Inicializa o cliente MQTT com persistência em memória
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(brokerUrl, clientId, persistence);

        // Configurações de conexão MQTT
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setAutomaticReconnect(true);
        client.connect(connOpts);
        logger.info("Conexão com MQTT Broker estabelecida em {}", brokerUrl);
        subscribeToAllSalaTopics();
    }

    /**
     * Chamado após a construção do bean, recupera todas as salas do repositório, obtém seus IDs, 
     * adiciona o sufixo "/medidas" a cada ID para formar o nome do tópico e 
     * tenta se inscrever em cada um desses tópicos. Em caso de sucesso, 
     * um log informativo é gerado. Em caso de falha, um log de erro é gerado.
     */
    @PostConstruct
    private void subscribeToAllSalaTopics() {
        // Inscreve-se em todos os tópicos das salas
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

    /**
     * Inscreve-se em um tópico MQTT específico e define o callback para processar mensagens recebidas.
     * 
     * @param topic O tópico MQTT ao qual se inscrever.
     * @throws MqttException Se ocorrer um erro durante a inscrição no tópico.
     */
    public void subscribe(String topic) throws MqttException {
        // Inscreve-se em um tópico específico e define o callback para mensagens recebidas
        client.subscribe(topic, (receivedTopic, msg) -> {
            logger.info("Mensagem MQTT: {}", msg);

            // Processa a mensagem recebida
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

            // Registra a medida no banco de dados
            registrarMedida(medida).subscribe();
        });
    }

    /**
     * Registra uma medida no repositório.
     *
     * @param medida a medida a ser registrada
     * @return um Mono contendo a medida registrada
     */
    private Mono<Medida> registrarMedida(Medida medida) {
        // Salva a medida no repositório
        return medidaRepository.save(medida);
    }

    /**
     * Publica uma mensagem em um tópico específico do MQTT.
     *
     * @param topic o tópico no qual a mensagem será publicada
     * @param messageContent o conteúdo da mensagem a ser publicada
     * 
     * @throws MqttException se ocorrer um erro durante a publicação da mensagem
     */
    public void publish(String topic, String messageContent) {
        try {
            // Publica uma mensagem em um tópico específico
            MqttMessage message = new MqttMessage(messageContent.getBytes());
            message.setQos(0);
            client.publish(topic, message);
            logger.info("Mensagem publicada no tópico {}: {}", topic, messageContent);
        } catch (MqttException e) {
            logger.error("Erro ao publicar mensagem no tópico {}: {}", topic, e.getMessage());
        }
    }
}
