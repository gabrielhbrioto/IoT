Cria um usuario com o nome postgres
sudo -i -u postgres

Abre o PostgreSQL
psql

Cria o usuario manager
CREATE USER manager WITH PASSWORD '123123';

Da todas permissoes ao manager
GRANT ALL PRIVILEGES ON DATABASE iot_database TO manager;

Remove todas permissoes ao manager
REVOKE ALL PRIVILEGES ON DATABASE iot_database FROM manager;

Remove o usuario manager
DROP ROLE manager

Utiliza o usuario
psql -U andre -d iot_database

Sai do postgres
\q

Cria o banco de dados
CREATE DATABASE iot_database;

Cria o banco de dados

Inicia o site
http-server .