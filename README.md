# Overview

Service that's part of Service Mesh studies.

# Setup

### Jaeger

docker run -d --name jaeger --net host jaegertracing/all-in-one:1.11.0

### MongoDb

docker run --name mongo --net host -d mongo:latest

### NATS

docker run -d --name=nats-server --net=host nats:latest

# Docker

1. `mvn package docker:build`
2. `docker run --rm --name=fraud-detection --net=host larchanjo/fraud-detection:latest`

# Kubernetes

Look the file `k8s-deployment.yaml`

# TODO

- deploy new version /api
  - update postman
  - update kubernetes
- add kubernetes deployments to all
- apply virtual service
- apply gateway