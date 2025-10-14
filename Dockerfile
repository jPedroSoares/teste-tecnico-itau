FROM openjdk:17-jdk-slim
WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests

RUN find /app/target -name '*.jar' -exec mv {} /app/app.jar \;

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]