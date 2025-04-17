FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
CMD ["java","-jar", "-Xmx6G", "-Duser.timezone=America/Sao_Paulo", "app.jar"]
