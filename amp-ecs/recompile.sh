#!/bin/bash

. ~/Lucru/tomcat/bin/axis.sh

ant build

cd bin
jar cvf ../ecsServerSide.jar org/dgfoundation/amp/ecs/common/*.class org/dgfoundation/amp/ecs/webservice/*.class > /dev/null
jar cvf ../ecsClientSide.jar org/dgfoundation/amp/ecs/common/*.class org/dgfoundation/amp/ecs/webservice/ECSConstants.class > /dev/null
cd ..

cp ecsServerSide.jar /home/alexandru/Lucru/jboss-amp-4.2.3/server/default/deploy/aaa-axis.war/WEB-INF/lib
cp ecsClientSide.jar /home/alexandru/Lucru/workspace3.4/amp1.14/WEB-INF/lib
 
./axisRestart.sh
