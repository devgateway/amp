FROM tomcat:7.0.67-jre8

ARG AMP_EXPLODED_WAR
ARG AMP_PULL_REQUEST
ARG AMP_BRANCH

LABEL "pull-request"=$AMP_PULL_REQUEST
LABEL "branch"=$AMP_BRANCH

ADD docker/wait-for-it.sh bin/
ADD docker/setenv.sh bin/
ADD docker/server.xml conf/

ENV AMP_REGISTRY_SECRET_TOKEN=NEc324abe4ff32187668da8d475cb9efbedc8e35b19d681ea0d4ffcd94d898b5a7

RUN rm -r /usr/local/tomcat/webapps/ROOT && mkdir /usr/local/tomcat/webapps/ROOT
ADD $AMP_EXPLODED_WAR /usr/local/tomcat/webapps/ROOT
ADD META-INF/context.xml /usr/local/tomcat/webapps/ROOT/META-INF/

