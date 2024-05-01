FROM amazoncorretto:21
# Start with a base image containing Java runtime

VOLUME /tmp
VOLUME /data

# Make port 9980 available to the world outside this container
EXPOSE 9009

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--fileBase=/data" ]