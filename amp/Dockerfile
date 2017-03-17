FROM tomcat:7.0.67-jre8

ARG AMP_EXPLODED_WAR
ARG AMP_PULL_REQUEST
ARG AMP_BRANCH

LABEL "pull-request"=$AMP_PULL_REQUEST
LABEL "branch"=$AMP_BRANCH

ADD docker/wait-for-it.sh bin
ADD docker/setenv.sh bin

RUN rm -r /usr/local/tomcat/webapps/ROOT && mkdir /usr/local/tomcat/webapps/ROOT
ADD $AMP_EXPLODED_WAR /usr/local/tomcat/webapps/ROOT
ADD META-INF/context.xml /usr/local/tomcat/webapps/ROOT/META-INF/

RUN sed -i 's/port="8080"/port="8080" URIEncoding="UTF-8"/g' conf/server.xml
