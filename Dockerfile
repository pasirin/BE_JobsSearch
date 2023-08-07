FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar JobsSearch.jar
ENTRYPOINT ["java","-jar","/JobsSearch.jar"]
EXPOSE 8080

