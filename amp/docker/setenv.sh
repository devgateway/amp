#!/bin/bash

JAVA_OPTS="-server -Xmx2g -Djava.awt.headless=true -XX:HeapDumpPath=/opt/heapdumps -XX:+HeapDumpOnOutOfMemoryError -DsmtpHost=sulfur -DsmtpFrom=system@digijava.org"
CATALINA_OPTS="-Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false -DAMP_DEVELOPMENT=true"
