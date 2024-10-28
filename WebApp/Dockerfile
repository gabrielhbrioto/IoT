# Use a imagem oficial do Nginx como base
FROM nginx:alpine

WORKDIR /app
COPY ./img /app/img

# Copie o conteúdo da pasta atual para a pasta padrão de HTML do Nginx
COPY . /usr/share/nginx/html

# Exponha a porta padrão do Nginx
EXPOSE 80
