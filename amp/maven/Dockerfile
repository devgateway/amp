FROM maven:3.8.4-jdk-8

ARG jenkinsUserID
RUN adduser --disabled-password --gecos jenkins --uid ${jenkinsUserID} jenkins

USER jenkins

ENV HOME=/home/jenkins

WORKDIR $HOME

ADD --chown=jenkins pom.xml $HOME

RUN ["mvn", "-B", "dependency:resolve", "dependency:resolve-plugins", "dependency:go-offline"]
