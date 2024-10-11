# Use a lightweight Java image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the local build (assumes it's in the target directory)
COPY target/spring-bcm-docker.jar spring-bcm-docker.jar

# Install gcc and g++
RUN apt-get update && \
    apt-get install -y gcc g++ && \
    rm -rf /var/lib/apt/lists/*

# Expose the application port
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "spring-bcm-docker.jar"]
