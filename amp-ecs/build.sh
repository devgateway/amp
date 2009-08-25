#!/bin/bash

. ~/Lucru/tomcat/bin/axis.sh
. server.sh

ant build
java -cp /home/alexandru/Lucru/kit/axis-1_4/lib/axis.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/commons-discovery.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/commons-logging.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/jaxrpc.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/saaj.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/log4j-1.2.8.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/wsdl4j.jar:/home/alexandru/Lucru/workspace3.4/ECS2/bin org.apache.axis.wsdl.Java2WSDL -o ECS.wsdl -n urn:org.dgfoundation.amp.ecs -l http://$serverName/axis/services/ECS org.dgfoundation.amp.ecs.webservice.ECS

java -cp /home/alexandru/Lucru/kit/axis-1_4/lib/axis.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/commons-discovery.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/commons-logging.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/jaxrpc.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/saaj.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/log4j-1.2.8.jar:/home/alexandru/Lucru/kit/axis-1_4/lib/wsdl4j.jar org.apache.axis.wsdl.WSDL2Java -o src -p org.dgfoundation.amp.ecs.common -s ECS.wsdl

ant build

cd bin
jar cvf ../ecsServerSide.jar org/dgfoundation/amp/ecs/common/*.class org/dgfoundation/amp/ecs/webservice/*.class > /dev/null
jar cvf ../ecsClientSide.jar org/dgfoundation/amp/ecs/common/*.class org/dgfoundation/amp/ecs/webservice/ECSConstants.class > /dev/null
cd ..

cp ecsServerSide.jar /home/alexandru/Lucru/jboss-amp-4.2.3/server/default/deploy/aaa-axis.war/WEB-INF/lib
cp ecsClientSide.jar /home/alexandru/Lucru/workspace3.4/amp1.14/WEB-INF/lib

./deploy.sh
./axisRestart.sh
