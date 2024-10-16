FROM openjdk:17
ARG JAR_FILE=build/libs/shared-work.jar
COPY ${JAR_FILE} ./shared-work.jar
ENV TZ=Asia/Seoul
CMD ["java", "-jar", "shared-work.jar"]