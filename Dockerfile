FROM maven

WORKDIR .

COPY . .
EXPOSE 8080
RUN mvn clean verify
CMD ["java", "--enable-preview", "-jar", "target/javaapp.jar"]
