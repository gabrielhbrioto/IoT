## Criar imagens e containers
# Construir docker image
docker build -t <Image_Name> .

# Criar o container
docker run -d --name <Container_Name> -p <Container_Port> <Image_Name>

## Manipulação do docker
# Listar contêineres em execução
docker ps

# Listar todos os contêineres
docker ps -a

# Listar todas as imagens
docker images

# Iniciar um contêiner
docker start <ID ou NOME_DO_CONTAINER>

# Para um contêiner
docker stop <ID ou NOME_DO_CONTAINER>

# Remover um contêiner
docker rm <ID ou NOME_DO_CONTAINER>

# Remove uma imagem
docker rmi <NOME ou ID_DA_IMAGEM>

# Abre o terminal do docker
docker exec -it mosquitto /bin/sh
