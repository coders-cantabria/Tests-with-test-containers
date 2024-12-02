# TLDR ⏩
Spring boot application project with a REST API and a Postgres DB. This repo was made for testcontainers workshop.

## About project 📝
The project uses hexagonal architecture with Maven multi-module to separate different layers. The different layers are:
- **application**: Contains the domain and the business logic of the application.
- **driven**: Contains drive/out ports for the application. Here we have
  - **database**: Database adapter with JPA and Postgres.
- **driving**: Contains driving/in ports for the application. Here we have
  - **rest-api**: REST API adapter with Spring Boot web.

## Business 💼
The application is about managing stores, products and their stock.
We have 3 separated APIs:
- **stores**: API to read stores data. Including store data and product stock.
- **products**: API to read products data. Including product data, prices and suppliers that serves the product.
- **pallet**: API to scan cargo pallets and process them as stock for the stores.

The more interesting part here is the pallet API. The barcode of a pallet has to be decoded in order to process it.
The barcode is composed of IAs, which are a 2-3 digit indicator that tells us what kind of information comes next and how long it is. The IAs are:
- **00**: The next 18 digits are the pallet ID.
- **01**: The next 14 digits are the product ID.
- **412**: The next 13 digits are the supplier ID.
- **410**: The next 13 digits are the delivery site ID (store ID for us).
- **37**: The next 1 to 8 digits are the quantity of products (the symbol `*` is used to mark the end)
- **10**: The text 1 to 20 digits are the batch id (the symbol `*` is used to mark the end)
- **11**: The next 6 digits are the expiration date in the format `yyMMdd`.

## How to run 🚀
### Requirements 🛠
- Docker/Podman
- Java 21
- Maven 3

### Run the project 🎬
The project has a docker compose file with a postgres database container.
```shell
docker-compose up -d
```

Then you can run the project with an IDE or with maven.
```shell
mvn clean install
cd boot
mvn spring-boot:run
```

Or just run the jar file.
```shell
mvn clean package
java -jar driven/database/target/workshop-web-products-database-0.1.0.jar
```

## API 🌐
When the project is running, you can access the API documentation at:
- [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
