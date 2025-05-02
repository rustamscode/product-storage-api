FROM openjdk:21
EXPOSE 8080
COPY target/product-storage-api-0.0.1-SNAPSHOT.jar product-storage-api.jar
ENTRYPOINT ["java", "-jar", "/product-storage-api.jar"]