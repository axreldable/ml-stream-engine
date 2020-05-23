#!/usr/bin/env bash

docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)

ps aux | grep spark
docker rmi -f $(docker images -a -q)
