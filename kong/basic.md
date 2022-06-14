Make service

```sh
curl -i -X POST \
  --url http://localhost:8001/services/ \
  --data 'name=spring-demo' \
  --data 'url=http://spring-demo:8080'
```

Add route to service

```sh
curl -i -X POST \
--url http://localhost:8001/services/spring-demo/routes \
--data 'paths[]=/spring-demo'
```

```sh
curl -i -x PATCH http://192.168.59.100:31181/services/demo-container-service \
--data 'connect_timeout=600000' \
--data 'read_timeout=600000`
```

curl -i -X GET --url http://192.168.59.100:31180/heavy-request \
--header 'Host: example.com'