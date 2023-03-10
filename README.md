# simple_microservices_project

```
Build Docker Image (No need for dockerfile)
https://cloud.google.com/java/getting-started/jib
```
```
Download and starts latest MariaDB server
docker run --detach --name mariadb_1 --env MARIADB_ROOT_PASSWORD=test --env MYSQL_TCP_PORT=3306 -p 3306:3306 mariadb:latest
docker run --detach --name mariadb_2 --env MARIADB_ROOT_PASSWORD=test --env MYSQL_TCP_PORT=3307 -p 3307:3307 mariadb:latest
docker run --detach --name mariadb_3 --env MARIADB_ROOT_PASSWORD=test --env MYSQL_TCP_PORT=3308 -p 3308:3308 mariadb:latest
```


| Endpoint                         | HTTP Method | Body |
|----------------------------------|:------------|------|
| http://localhost:59490/api/order | Post        |      |
