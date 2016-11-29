#!/bin/bash

JAVA_OPTS="-server -Xmx2g -Djava.awt.headless=true"
CATALINA_OPTS="-Dorg.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false -Dorg.apache.jasper.compiler.Parser.STRICT_WHITESPACE=false"
