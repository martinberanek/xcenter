FROM maven:latest

ADD pom.xml /app/
WORKDIR /app
RUN mvn dependency:go-offline
RUN mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install-deps"
ADD . /app/
RUN mvn verify
RUN ln -s /app/target/*.jar /app.jar

CMD ["java", "-jar", "/app.jar"]