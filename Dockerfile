FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cách này copy mọi file .jar NHƯNG loại trừ các file kết thúc bằng plain.jar
# Lưu ý: Gradle thường tạo ra: project-1.0.jar và project-1.0-plain.jar
COPY build/libs/kafka-tool.jar app.jar

ENV SPRING_PROFILES_ACTIVE=test
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]