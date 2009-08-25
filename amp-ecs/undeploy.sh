#!/bin/bash

. ~/Lucru/tomcat/bin/axis.sh
. server.sh
java -cp /home/alexandru/Lucru/kit/axis-1_4/lib/axis.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/commons-discovery.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/commons-logging.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/jaxrpc.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/saaj.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/log4j-1.2.8.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/wsdl4j.jar:/home/alexandru/Lucru/workspace3.4/ECS2/bin org.apache.axis.client.AdminClient -lhttp://$serverName/axis/services/AdminService src/org/dgfoundation/amp/ecs/common/undeploy.wsdd
