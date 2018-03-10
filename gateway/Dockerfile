FROM openjdk:8-jre-alpine

MAINTAINER Roman Tsypuk <tsypuk.rb@gmail.com>

WORKDIR /opt/

ADD  build/libs/SmartGateway-0.0.6.jar SmartGateway.jar
RUN sh -c 'touch /opt/SmartGateway.jar'

#HEALTHCHECK --interval=5s --timeout=3s CMD curl --fail http://localhost:8080/health || exit 1

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Xmx256m -Xms256m","-jar","/opt/SmartGateway.jar"]