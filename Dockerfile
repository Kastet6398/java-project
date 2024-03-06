FROM maven AS build
COPY . .
ENTRYPOINT mvn spring-boot:run
