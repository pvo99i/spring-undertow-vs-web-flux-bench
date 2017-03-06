#!/usr/bin/env bash
set -x

docker-compose -f docker/docker-compose-dev.yml down
./start-docker.sh

