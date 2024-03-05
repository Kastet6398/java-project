FROM maven AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21
COPY --from=build /target/javaapp.jar javaapp.jar
EXPOSE 8090
ENTRYPOINT ["java","--enable-preview","-jar","javaapp.jar"]
