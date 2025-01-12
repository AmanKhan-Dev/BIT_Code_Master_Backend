# Use a lightweight Java image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /target

# Copy the JAR file from the local build (assumes it's in the target directory)
COPY target/spring-bcm-docker.jar spring-bcm-docker.jar

# Install gcc and g++ (if necessary for your application)
RUN apt-get update && \
    apt-get install -y --no-install-recommends gcc g++ && \
    rm -rf /var/lib/apt/lists/*

# Expose the application port (Render will map it automatically)
EXPOSE 8080

# Set the default command to run the application
ENTRYPOINT ["java", "-jar", "spring-bcm-docker.jar"]


