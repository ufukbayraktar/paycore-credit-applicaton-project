FROM openjdk:11
EXPOSE 8080
ADD target/paycore-credit-application-project.jar paycore-credit-application-project.jar
ENTRYPOINT ["java","-jar","paycore-credit-application-project.jar"]