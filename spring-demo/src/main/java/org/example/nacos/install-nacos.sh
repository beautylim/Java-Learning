#! /bin/bash

## push nacos image
sudo docker pull nacos-registry.cn-hangzhou.cr.aliyuncs.com/nacos/nacos-server:v3.2.0

##
mkdir nacos && cd nacos

cp ../docker-compose.yml .

sudo docker compose up -docker

sudo docker compose logs -docker

# visit nacos console UI, credentials: nacos/nacos
curl http://localhost:8080/nacos