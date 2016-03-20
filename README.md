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

## JWT Token
### Header
```
{
  "typ": "JWT",
  "alg": "HS256"
}
```
### Payload
```
{
  "iss": "Online JWT Builder",
  "iat": 1458456961,
  "exp": 1489992960,
  "aud": "testResource",
  "sub": "nirving",
  "authorities": [
    "ROLE_Admin",
    "Customer"
  ],
  "scope": [
    "Customer.info",
    "open_id"
  ]
}
```
### Signature Verification
Secret for Signature is `Passw0rd`

```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  
Passw0rd

)
```