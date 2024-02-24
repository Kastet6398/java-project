FROM maven

WORKDIR .

COPY . .

RUN mvn clean verify
CMD ["java", "--enable-preview", "-jar", "target/javaapp.jar"]
