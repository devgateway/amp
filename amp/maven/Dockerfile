FROM maven:3.8.4-jdk-8

ENTRYPOINT []

RUN adduser --disabled-password --gecos jenkins --uid `id -u` jenkins

USER jenkins

ENV HOME=/home/jenkins

WORKDIR $HOME

ADD --chown=jenkins pom.xml $HOME

RUN ["mvn", "-B", "de.qaware.maven:go-offline-maven-plugin:resolve-dependencies"]
