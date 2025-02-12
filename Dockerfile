FROM harbor.fiskeridirektoratet.no/dockerhub-proxy/library/eclipse-temurin:21-jre as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract


FROM eclipse-temurin:21-jre
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/company-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT exec java org.springframework.boot.loader.launch.JarLauncher $JETTY_DEBUG -Xmx512M -Doracle.jdbc.J2EE13Compliant=true -Djava.security.egd=file:/dev/./urandom