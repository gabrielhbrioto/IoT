# Inicializar os containers
set -a && source docker.conf && docker compose start

# Parar os containers
set -a && source docker.conf && docker compose stop
