#!/usr/bin/python

import psycopg2
import os
import time
import datetime
import sys

OUTPUT_FILE = "/home/tomcat/postgres_monitoring.csv"
DATABASE_NAME = "amp_nepal_prod23"
DATABASE_USER_NAME = "amp"
DATABASE_USER_PASSWORD = "amp"
BASE_MOMENT = datetime.datetime(year = 2014, month = 1, day = 1)

def get_count(cursor, query):
    cursor.execute(query);
    record = cursor.fetchone();
    return record[0]
    
def add_line_to_file(timestr, timenr, nr_connections, nr_idle_connections_total, nr_idle_connections_in_transaction):
    if (not os.path.exists(OUTPUT_FILE)):
        opf = open(OUTPUT_FILE, "wt")
        opf.write("%s;%s;%s;%s;%s" % ('time', 'seconds since base_moment', 'postgres connections open', 'postgres connections idle total', 'postgres connections idle in transaction'));
        opf.write("\n")
        opf.close()
    with open(OUTPUT_FILE, "a") as myfile:
        outline = "%s;%s;%s;%s;%s" % (timestr, timenr, nr_connections, nr_idle_connections_total, nr_idle_connections_in_transaction)
        print outline
        myfile.write(outline);
        myfile.write("\n")
    
def restart_amp():
    print "restarting AMP!\n"
    os.system("/home/tomcat/tomcat/bin/shutdown.sh")
    time.sleep(20) # wait for Tomcat to shutdown
    os.system("/home/tomcat/tomcat/bin/startup.sh")

def main():
    conn_string = "host='localhost' port='5433' dbname='%s' user='%s' password='%s'" % (DATABASE_NAME, DATABASE_USER_NAME, DATABASE_USER_PASSWORD);
    
    conn = psycopg2.connect(conn_string)
    cursor = conn.cursor()
    print "Connected!\n"
    nr_idle_connections_total = get_count(cursor, "SELECT count(*) FROM pg_stat_activity where current_query like '<IDLE>%'");
    nr_idle_connections_in_transaction = get_count(cursor, "SELECT count(*) FROM pg_stat_activity where current_query like '<IDLE> in transaction'");
    nr_connections = get_count(cursor, "SELECT count(*) FROM pg_stat_activity");
    conn.close()
    moment_now = datetime.datetime.fromtimestamp(time.time());
    delta_timestamp = (moment_now - BASE_MOMENT);
    delta_timestamp_seconds = delta_timestamp.seconds + delta_timestamp.days * 3600*24

    add_line_to_file(moment_now.isoformat(' '), delta_timestamp_seconds, nr_connections, nr_idle_connections_total, nr_idle_connections_in_transaction)
    if (moment_now.hour < 3) and (nr_idle_connections_total > 75):
	restart_amp(); # restart AMP after midnight if it is close to running out of connections
    sys.exit(0)
    
if __name__ == "__main__":
    main()
    