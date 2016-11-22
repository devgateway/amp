FROM tomcat:7.0.67-jre8

ARG AMP_EXPLODED_WAR
ARG AMP_PULL_REQUEST

LABEL "pull-request"=$AMP_PULL_REQUEST

ADD docker/wait-for-it.sh bin
ADD docker/setenv.sh bin

RUN rm -r /usr/local/tomcat/webapps/ROOT && mkdir /usr/local/tomcat/webapps/ROOT
ADD $AMP_EXPLODED_WAR /usr/local/tomcat/webapps/ROOT
ADD META-INF/context.xml /usr/local/tomcat/webapps/ROOT/META-INF/
