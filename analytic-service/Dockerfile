FROM eclipse-temurin:22-jdk

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Copy sebagai application-prod.yml agar secara otomatis menjadi konfigurasi utama
COPY src/main/resources/application-prod.yml /app/config/application.yml

# Spring Boot secara otomatis mencari konfigurasi di /config
ENTRYPOINT ["java", "-jar", "app.jar"]