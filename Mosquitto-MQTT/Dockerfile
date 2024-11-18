# Use a imagem oficial do Mosquitto como base
FROM eclipse-mosquitto:2.0

# Define o diretório de trabalho dentro do container
WORKDIR /mosquitto

# Copia o arquivo de configuração para o container
COPY mosquitto.conf /mosquitto/config/mosquitto.conf

# Expõe a porta padrão do Mosquitto (1883) e a porta de WebSocket (9001)
EXPOSE 1883
EXPOSE 9001

# Define o comando de inicialização
CMD ["mosquitto", "-c", "/mosquitto/config/mosquitto.conf"]
