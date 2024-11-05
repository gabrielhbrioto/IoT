Pare o serviço Mosquitto padrão
sudo systemctl stop mosquitto

Inicie o Mosquitto usando o arquivo de configuração da pasta criada
mosquitto -c IoT/mosquitto_config/mosquitto.conf

Escuta a um contexto do broker
mosquitto_sub -h (IP) -t (Topico)

Envia uma mensagem a um contexto do broker
mosquitto_pub -h (IP) -t (Topico) -m (Menssagem )

Pega o IPV4
hostname -I

Auto-Start
sudo systemctl "disable/enable" mosquitto



