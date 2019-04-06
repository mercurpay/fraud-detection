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

# TODO

* Dockerfile
* Deployments Kubernetes \ Istio