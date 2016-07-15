# Mesos Framework using Spring Boot

Mesos Framework for ephemeral tasks, built using Spring boot. Postgres is used to persist task state. Supports failover. 

## Building
Docker build can be generated using
```
mvn package docker:build
```
## Running 
```
docker run registry.ac.uda.io/mesos-framework -p <port mapping>
```

## Api 
Documentation of API is available via swagger. Supported APIs are:
- /tasks POST
- /tasks/{task_id} GET
- /tasks/{task_id} DELETE
