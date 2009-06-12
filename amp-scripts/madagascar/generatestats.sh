#!/bin/bash
date >>/home/amp/tools/stats_execution.log
perl /usr/local/awstats/tools/awstats_buildstaticpages.pl -config=localhost -update -awstatsprog=/usr/local/awstats/tools/wwwroot/cgi-bin/awstats.pl -dir=/home/amp/jboss-amp/server/default/deploy/amp.war/stats >>/home/amp/tools/stats_execution.log
mv /home/amp/jboss-amp/server/default/deploy/amp.war/stats/awstats.localhost.html /home/amp/jboss-amp/server/default/deploy/amp.war/stats/index.html