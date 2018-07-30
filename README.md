# crawler-service

Api rest que implementa um crawler que a partir de uma URL Ex:(http://revistaautoesporte.globo.com/rss/ultimas/feed.xml) retorne um json.

# Tecnologias Utilizadas:

Linguagem: Java 8
Gerenciadr de Dependências: Maven

# FrameWorks' e Lib's: 
spring-boot-starter 2.0.3.RELEASE
jsoup 1.8.3
lombok 1.16.22
spring-boot-starter-security 2.0.3.RELEASE
spring-boot-starter-test 2.0.3.RELEASE
spring-security-test  2.0.3.RELEASE

# Exemplo de Request Post para a chamado do serviço Rest 
 curl -X POST \
  http://localhost:8080/crawler \
  -H 'authorization: Basic Y3Jhd2xlcjpjcmF3bGVy' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: be280f2d-5a09-0353-667d-f996b19a8b75' \
  -d '{
	"url":"http://revistaautoesporte.globo.com/rss/ultimas/feed.xml"
}'
# Caso queira ultilizar o Postman para chamar a Api, configurar o Authorization com as credencias abaixo:
 type: Basic Auth \
 user:crawler \
 password:crawler 
