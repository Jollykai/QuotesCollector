FROM openjdk:17 as maven
WORKDIR /build
COPY mvnw .
ADD .mvn ./.mvn
COPY pom.xml .
RUN ./mvnw dependency:go-offline
COPY ./ .
RUN ./mvnw clean package spring-boot:repackage

FROM openjdk:17
ENV name=quotes-collector
WORKDIR /srv/${name}
COPY --from=maven /build/target/${name}.jar .
EXPOSE 8080
CMD java ${JAVA_OPTS} -jar ${name}.jar