#!/bin/bash

# Caminho absoluto do diretório onde o script está localizado
SCRIPT_DIR=$(dirname "$(realpath "$0")")

# Caminho para o arquivo docker.conf relativo ao diretório do script
DOCKER_CONF_PATH="$SCRIPT_DIR/../docker.conf"

# Verifica se o arquivo docker.conf existe
if [ ! -f $DOCKER_CONF_PATH ]; then
  echo "Erro: Arquivo docker.conf não encontrado em $DOCKER_CONF_PATH."
  exit 1
fi

# Carrega as variáveis do docker.conf
source $DOCKER_CONF_PATH

# Função para verificar e liberar portas ocupadas
open_ports() {
  local PORTS=$1
  for port in $(echo $PORTS | tr ',' ' ' | sed 's/:.*//'); do
    if nc -z localhost $port; then
      echo "Porta $port está ocupada. Tentando liberar..."
      # Encontra o processo usando a porta
      PID=$(sudo lsof -t -i:$port)
      if [ -n "$PID" ]; then
        echo "Matando processo $PID que está usando a porta $port."
        sudo kill -9 $PID
        echo "Processo $PID foi encerrado. Porta $port agora está livre."
      else
        echo "Não foi possível identificar o processo usando a porta $port."
      fi
    else
      echo "Porta $port está livre. Configurando abertura no firewall..."
    fi
    # Adiciona uma regra ao iptables para abrir a porta
    sudo iptables -A INPUT -p tcp --dport $port -j ACCEPT
    sudo iptables -A INPUT -p udp --dport $port -j ACCEPT
    echo "Porta $port foi configurada para aceitar conexões."
  done
}

echo "Verificando e liberando portas para MOSQUITTO..."
open_ports $MOSQUITTO_PORTS

echo "Verificando e liberando portas para WEBAPP..."
open_ports $WEBAPP_PORTS

echo "Configuração de portas concluída. Todas as portas necessárias estão abertas."
