FROM openjdk:8
ADD target/deliverycostcalculator.jar deliverycostcalculator.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "deliverycostcalculator.jar"]