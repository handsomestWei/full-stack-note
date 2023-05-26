#!/bin/bash
rm -rf www
mkdir www
unzip xx.war -d www
docker build -t xy .
docker push xy
