# Etapa de build
FROM maven:3.8.6-amazoncorretto-17 as build
WORKDIR /app

# Copia o código fonte para o container
COPY pom.xml .
COPY src ./src

# Realiza o build do projeto, ignorando os testes
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR gerado na etapa de build para o container final
COPY --from=build /app/target/*.jar app.jar

# Define o comando para iniciar o aplicativo
CMD ["java", "-jar", "app.jar"]
