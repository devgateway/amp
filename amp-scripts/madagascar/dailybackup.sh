#!/bin/bash
PATH=$PATH:$HOME/bin
export PATH
PATH=$PATH:$HOME/bin
export PATH
export ORACLE_BASE=/oradata
export ORACLE_HOME=/oracle/app/oracle
export ORACLE_SID=ampmada
export PATH=.:${PATH}:$HOME/bin:$ORACLE_HOME/bin
export PATH=${PATH}:/usr/bin:/bin:/usr/bin/X11:/usr/local/bin
export ORACLE_TERM=xterm
export TNS_ADMIN=$ORACLE_HOME/network/admin
export ORA_NLS33=$ORACLE_HOME/ocommon/nls/admin/data
export LD_LIBRARY_PATH=$ORACLE_HOME/lib
export CLASSPATH=$ORACLE_HOME/JRE
export CLASSPATH=${CLASSPATH}:$ORACLE_HOME/jlib
export CLASSPATH=${CLASSPATH}:$ORACLE_HOME/rdbms/jlib
export CLASSPATH=${CLASSPATH}:$ORACLE_HOME/network/jlib
export THREADS_FLAG=native
export LD_ASSUME_KERNEL=2.6.9

BACKUPS_HOME="/home/amp/backups"

cdate=`date +%d%b%Y`
me=`whoami`
cdir=`pwd`
exp amp/amp GRANTS=Y ROWS=Y FILE=$BACKUPS_HOME/amp-madagascar-daily.dump LOG=$BACKUPS_HOME/daily_$cdate.log
tar -cvzf $BACKUPS_HOME/amp-madagascar-daily.dump.tar.gz $BACKUPS_HOME/amp-madagascar-daily.dump 
rm -f $BACKUPS_HOME/amp-madagascar-daily.dump 



