# Use a base image with Java
FROM openjdk:17-alpine

# Copy the built jar file into the image
COPY target/*.jar app.jar

# Set the entry point to run your application
ENTRYPOINT ["java","-jar","/app.jar"]