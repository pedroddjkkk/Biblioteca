FROM gradle:6.9-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew build

FROM amazoncorretto:17
USER root
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/library-api.jar
ENTRYPOINT ["java","-jar","/app/library-api.jar"]