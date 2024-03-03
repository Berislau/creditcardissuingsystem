Credit Card Issuing System
==========================


### Running application locally

Prerequisites:

* Docker, Docker Compose v3.8
* Java 17

### Starting the app

First, package the service:

    $ mvn clean package

From the project's root:

    $ docker-compose up -d

Finally, we can start the app running the main class: [CreditCardIssuingSystemApplication](src/main/java/com/bmbank/creditcardissuingsystem/CreditCardIssuingSystemApplication.java)


### Using the app

Please consider either of the following to see how to use this service and test APIs:

* http://localhost:8080/swagger-ui/index.html

* http://localhost:8080/api-docs

### Additional info

Project uses Flyway to migrate PostgreSQL up-to-date. Migration scripts can be found in [migration](src/main/resources/db/migration)

Flyway can also be accessed through maven plugin, for example for easy database reset use:

    $ mvn flyway:clean

Project uses automated formatting on every successful maven build.

There is 1 configurable application parameter creditcardissuingsystem.file-storage-location used for storing generated file purposes.
It can be modified from [properties](src/main/resources/application.properties)

CI/CD implementation can be found in [workflows](.github/workflows)

Logging configuration can be checked in [logback-spring](src/main/resources/logback-spring.xml)

For automated tests application uses H2 DB simulated as PostgreSQL.
Instead of going with Spring Profile approach tests use properties in [properties](src/test/resources/application.properties)

Application uses Swagger/OpenApi dependency in order to generate API documentation and for easy Testing.
As mentioned above this can be found in:
* http://localhost:8080/swagger-ui/index.html

* http://localhost:8080/api-docs
