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

CI/CD implementation can be found in [workflows](.github/workflows)
