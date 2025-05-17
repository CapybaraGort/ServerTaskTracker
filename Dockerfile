FROM openjdk:21-jdk-slim AS build
WORKDIR /app

RUN apt-get update && apt-get install -y curl unzip && rm -rf /var/lib/apt/lists/*

RUN curl -sSL https://services.gradle.org/distributions/gradle-8.10-bin.zip -o gradle.zip \
    && unzip gradle.zip \
    && mv gradle-8.10 /opt/gradle \
    && rm gradle.zip

ENV PATH="/opt/gradle/bin:${PATH}"

COPY . .
RUN gradle build --no-daemon

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]