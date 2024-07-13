# Используем официальный образ Maven для сборки проекта
FROM maven:3.8.6-eclipse-temurin-17 AS build
# Устанавливаем рабочую директорию
WORKDIR /app
# Копируем файлы проекта в рабочую директорию
COPY pom.xml .
COPY src ./src
# Собираем проект и создаем fat JAR
RUN mvn clean package
# Используем официальный образ JDK для запуска приложения
FROM eclipse-temurin:17-jdk-jammy
# Устанавливаем рабочую директорию
WORKDIR /app
# Копируем собранный JAR файл из предыдущего этапа
COPY --from=build /app/target/TelegramBot-1.0-SNAPSHOT.jar /app/TelegramBot-1.0-SNAPSHOT.jar
# Определяем команду для запуска приложения
CMD ["java", "-jar", "/app/TelegramBot-1.0-SNAPSHOT.jar"]