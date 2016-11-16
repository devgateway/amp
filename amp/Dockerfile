FROM tomcat:7-jre8
ADD docker/wait-for-it.sh bin
ADD docker/setenv.sh bin
RUN rm -r /usr/local/tomcat/webapps/ROOT && mkdir /usr/local/tomcat/webapps/ROOT
ARG AMP_EXPLODED_WAR
ADD $AMP_EXPLODED_WAR /usr/local/tomcat/webapps/ROOT
