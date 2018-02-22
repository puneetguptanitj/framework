# Mesos Framework using Spring Boot

- Mesos Framework for ephemeral tasks, built using Spring boot. 
- Postgres is used to persist task state. 
- Supports failover. 

## Build
Docker build can be generated using
```
mvn package docker:build
```
## Run
```
docker run registry.ac.uda.io/mesos-framework -p <port mapping>
```

## APIs
Documentation of API is available via swagger. Supported APIs are:
- /tasks POST
- /tasks/{task_id} GET
- /tasks/{task_id} DELETE
