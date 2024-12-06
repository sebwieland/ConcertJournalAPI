FROM openjdk:21
VOLUME /tmp
WORKDIR /app
COPY target/ConcertJournalAPI-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
