FROM openjdk:11-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application jar file to the container
COPY target/consumer-1.0.0-SNAPSHOT.jar /app/consumer-1.0.0-SNAPSHOT.jar

# Set the environment variables
ENV JAVA_OPTS=""

# Expose the port 8080
EXPOSE 8080

# Define the command to run the application
CMD java $JAVA_OPTS -jar /app/consumer-1.0.0-SNAPSHOT.jar
