# Use uma imagem base do Node.js para construir o projeto
FROM node:18 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia os arquivos necessários para o diretório de trabalho
COPY package*.json ./
COPY webpack.config.js ./
COPY src/ ./src/
COPY css/ ./css/
COPY img/ ./img/

# Instala as dependências e gera o build de produção
RUN npm install
RUN npx webpack --mode production

# Usa uma imagem do Nginx para servir o conteúdo estático
FROM nginx:alpine

# Copia os arquivos gerados para o diretório padrão do Nginx
COPY --from=builder /app/dist /usr/share/nginx/html

# Expõe a porta 80 para acessar o site
EXPOSE 80

# Comando para iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]
