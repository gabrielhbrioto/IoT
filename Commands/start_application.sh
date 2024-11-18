#!/bin/bash

echo "Verificando se as portas estão ocupadas..."
for port in 1883 9001 8080; do
  if nc -z localhost $port; then
    echo "Porta $port está ocupada. Por favor, libere-a antes de continuar."
    exit 1
  fi
done

echo "Iniciando os serviços com Docker Compose..."
docker-compose up --build
