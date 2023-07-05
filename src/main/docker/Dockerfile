####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Before building the docker image run:
#
# mvn package
#
# Then, build the image with:
#
# docker build -f src/main/docker/Dockerfile.jvm -t quarkus/getting-started-jvm .
#
# Then run the container using:
#
# docker run -i --rm -p 8080:8080 quarkus/getting-started-jvm
#
###
FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# set up deployments directory
# references: https://github.com/fabric8io-images/run-java-sh
RUN mkdir -p /deployments
ENV JAVA_APP_JAR=quarkus-run.jar \
    JAVA_APP_DIR=/deployments \
    JAVA_MAJOR_VERSION=17 \
    JAVA_OPTIONS=-Dfile.encoding=utf-8 \
    LOG4J_FORMAT_MSG_NO_LOOKUPS=true
COPY src/main/docker/run-java.sh /deployments/
RUN chmod 755 /deployments/run-java.sh

COPY target/quarkus-app/ /deployments/
COPY bots.db.json /deployments/bots.db.json
EXPOSE 8080

# run with user 1001 and be prepared for be running in OpenShift too
RUN adduser -G root --no-create-home --disabled-password 1001 \
  && chown -R 1001 /deployments \
  && chmod -R "g+rwX" /deployments \
  && chown -R 1001:root /deployments
USER 1001

ENTRYPOINT [ "/deployments/run-java.sh" ]