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

echo "Verificando se as portas estão ocupadas..."

# Função para verificar portas
check_ports() {
  local PORTS=$1
  for port in $(echo $PORTS | tr ',' ' ' | sed 's/:.*//'); do
    if nc -z localhost $port; then
      echo "Porta $port está ocupada. Por favor, libere-a antes de continuar."
      exit 1
    fi
  done
}

# Verifica as portas definidas no docker.conf
check_ports $MOSQUITTO_PORTS
check_ports $WEBAPP_PORTS

echo "Todas as portas estão livres. Você pode usar o Docker Compose."
