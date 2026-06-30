#! /bin/bash

sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://mirror.baidubce.com"]
}
EOF


sudo systemctl daemon-reload
sudo systemctl restart docker

sudo docker pull mysql:8.0

mkdir mysql && cd mysql

sudo tee docker-compose.yml <<-'EOF'
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    restart: always
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: 123456
      MYSQL_DATABASE: user_db
      TZ: UTC                      # 强制时区 UTC
    command:
      --default-time-zone=UTC     # 数据库时区 UTC
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_unicode_ci
    volumes:
      - ./mysql-data:/var/lib/mysql
EOF

sudo docker compose up -d

sudo docker compose logs -f

sudo docker exec -it mysql mysql -uroot -p123456
