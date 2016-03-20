# OIDCMicroService

## Package
```
mvn package spring-boot:repackage
```

## Execute
```
java -jar target/bootjwt-0.0.1-SNAPSHOT.war
```

## Test
```
mvn spring-boot:run
curl -H "Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE0NTg0NTY5NjEsImV4cCI6MTQ4OTk5Mjk2MCwiYXVkIjoidGVzdFJlc291cmNlIiwic3ViIjoibmlydmluZyIsImF1dGhvcml0aWVzIjpbIlJPTEVfQWRtaW4iLCJDdXN0b21lciJdLCJzY29wZSI6WyJDdXN0b21lci5pbmZvIiwib3Blbl9pZCJdfQ.J1lUfFra3-UuRpdh9daSMpgtUx3h0o2027Qb5dKBMNw" -k -v http://localhost:8080/api/me
````

