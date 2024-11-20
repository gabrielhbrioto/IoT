### Construir Tudo
chmod +x Commands/*.sh
set -a && source docker.conf && docker compose up --build -d

# Permissão de execução
chmod +x Commands/*.sh

# Criar aplicação
set -a && source docker.conf && docker compose up --build -d

# Ações
# Remover containers
set -a && source docker.conf && docker compose down

# Criar imagens
set -a && source docker.conf && docker compose build