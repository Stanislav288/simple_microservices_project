FROM eclipse-temurin:17-jdk-jammy
RUN mkdir /opt/app
COPY /target/product*.jar /opt/app
CMD ["/bin/sh", "-c", "java -jar /opt/app/product*.jar"]