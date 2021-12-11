# AfkoAPI

---

##Requires: 

- Database 'afkodb'
- application.properties file (not included in repo)


---
###Application.properties tempate
````
spring.datasource.url=jdbc:mysql://<DATABASE URL>
spring.datasource.username=<USERNAME>
spring.datasource.password=<LOGIN>
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql: true


#Mail
spring.mail.host=<HOST ADDRESS>
spring.mail.port=<HOST PORT>
spring.mail.username=<HOST USERNAME>
spring.mail.password=<HOST PASSWORD>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

#Security
<Redacted>

server.port=8443

#Endpoints
HOSTNAME=http://localhost:8443
DEFAULT_PATH=/api/v1
ABBREVIATIONS=/abbreviations
ADMIN=/admin
DEPARTMENT=/departments
AUTHENTICATE=/authenticate
USER=/user
GAME=/game
REPORT=/reports
````
