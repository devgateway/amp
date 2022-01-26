FROM maven:3.8.4-jdk-8

ARG jenkinsUserID
ARG jenkinsGroupID
RUN addgroup --gid ${jenkinsGroupID} jenkins \
  && adduser --disabled-password --gecos jenkins --uid ${jenkinsUserID} --gid ${jenkinsGroupID} jenkins
