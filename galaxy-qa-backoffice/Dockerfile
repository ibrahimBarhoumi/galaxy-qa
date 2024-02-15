FROM openjdk:11-slim
WORKDIR /app
ADD /target/galaxy-qa-backoffice-*.jar /app/galaxy-qa-backoffice.jar
CMD ["java", "-Dspring.profiles.active = dev", "-jar", "galaxy-qa-backoffice.jar"]
