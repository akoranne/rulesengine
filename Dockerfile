FROM java

ADD ./build/libs/rulesengine-0.0.1-SNAPSHOT.jar /rulesengine.jar
EXPOSE 8080
CMD ["java", "-jar", "rulesengine.jar"]
