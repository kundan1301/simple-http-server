# simple-http-server

Implemented a naive http server using TCP socket. This project implement http protocol. For more information check inline code. 

##### How to run ?
Run server.java file using java or in any IDE. It will start listening on port 8080. 

##### Sample curl requests. 

1. ```

curl -X POST \
  http://localhost:8080/ \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -d '{
	"hi":1
}'

curl -X GET \
  http://localhost:8080/ \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
```

