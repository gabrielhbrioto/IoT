#!/bin/bash

echo "Parando webapp_container..."
docker stop webapp_container

echo "Parando mosquitto_container..."
docker stop mosquitto_container
