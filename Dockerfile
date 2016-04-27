FROM java

ADD ./build/libs/rulesengine-0.0.1-SNAPSHOT.jar /simplerules.jar
EXPOSE 8080
CMD ["java", "-jar", "simplerules.jar"]
