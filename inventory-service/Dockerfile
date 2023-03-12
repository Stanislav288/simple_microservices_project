FROM eclipse-temurin:17-alpine
RUN mkdir /opt/app
COPY /target/inventory*.jar /opt/app
CMD ["/bin/sh", "-c", "java -jar /opt/app/inventory*.jar"]
