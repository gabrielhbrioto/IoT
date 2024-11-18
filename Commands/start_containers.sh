#!/bin/bash

echo "Iniciando mosquitto_container..."
docker start mosquitto_container

echo "Iniciando webapp_container..."
docker start webapp_container
