#!/bin/bash

JAVA_OPTS="-server -Xmx2g -Djava.awt.headless=true"
JAVA_OPTS="$JAVA_OPTS -DsmtpHost=sulfur.migrated.devgateway.org -DsmtpFrom=system@digijava.org"
JAVA_OPTS="$JAVA_OPTS -XX:HeapDumpPath=/opt/heapdumps -XX:+HeapDumpOnOutOfMemoryError"

CATALINA_OPTS="-Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false"
CATALINA_OPTS="$CATALINA_OPTS -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false"
CATALINA_OPTS="$CATALINA_OPTS -DAMP_DEVELOPMENT=true"
CATALINA_OPTS="$CATALINA_OPTS -Djavamelody.datasources=java:comp/env/ampDS,java:comp/env/jcrDS"
CATALINA_OPTS="$CATALINA_OPTS -Djersey.client.connectTimeout=60000 -Djersey.client.readTimeout=600000"
