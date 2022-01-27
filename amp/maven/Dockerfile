FROM maven:3.8.4-jdk-8

ARG jenkinsUserID
RUN adduser --disabled-password --gecos jenkins --uid ${jenkinsUserID} jenkins

USER jenkins

ENV HOME=/home/jenkins

WORKDIR $HOME

ADD --chown=jenkins pom.xml $HOME

RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "dependency:go-offline"]
