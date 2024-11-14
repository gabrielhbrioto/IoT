# Use uma imagem Maven para construir o projeto
FROM maven:3.8.4-openjdk-17 AS builder

# Cria um diretório de trabalho
WORKDIR /app

# Copia o arquivo de configuração Maven e o código do projeto para o contêiner
COPY pom.xml .
COPY src ./src

# Executa o Maven para construir o projeto, gerando um arquivo JAR na pasta target/
RUN mvn clean package -DskipTests

# Use uma imagem OpenJDK para rodar o aplicativo
FROM openjdk:17-jdk-slim

# Configura o diretório de trabalho
WORKDIR /app

# Copia o JAR gerado da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que o Spring Boot usa (8080, por padrão)
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
