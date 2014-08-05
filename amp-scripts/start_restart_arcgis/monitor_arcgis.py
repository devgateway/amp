#!/usr/bin/python

import os
import time
import datetime
import sys
import subprocess
import os.path
import socket;

'''
ArcGIS periodically dies because it is a PoS unstable software
this script should be called in a periodical cronjob (like once in 5 minutes)
What it does:
    1. checks that ArcGIS is still alive
    2. if not, kills it
    3. also, kills it every 5th day of the month before 3 a.m., because ArcGIS leaks memory and dies by itself approx once in 2 weeks
'''

OUTPUT_FILE = "/tools/arcgis/arcgis_monitoring.csv"
FILE_WITH_LAST_RESTART_TIME = "/tools/arcgis/arcgis_restarts.csv"

ARCGIS_INSTALLATION_FOLDER = "/tools/arcgis"
BASE_MOMENT = datetime.datetime(year = 2014, month = 8, day = 1)

def port_is_open(port):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    result = sock.connect_ex(('127.0.0.1', port))
    if result == 0:
        print "Port is open"
        return True
    else:
        print "Port is not open"
        return False
        
def arcgis_is_running():
    port_open = port_is_open(6080)
    cmd = ARCGIS_INSTALLATION_FOLDER + "/server/framework/etc/scripts/agsserver.sh"
    print "running command: " + cmd
    #["Python","script.py","name1","name2","name3"]
    proc = subprocess.Popen([cmd, "status"], stdout = subprocess.PIPE);
    output = proc.communicate()[0];
    return port_open and (output.strip() == "ArcGIS Server: running")
    
def add_line_to_restarts_file(timestr, timenr):
    if (not os.path.exists(FILE_WITH_LAST_RESTART_TIME)):
        opf = open(FILE_WITH_LAST_RESTART_TIME, "wt")
        opf.write("%s;%s" % ('time', 'seconds since base_moment'));
        opf.write("\n")
        opf.close()
    with open(FILE_WITH_LAST_RESTART_TIME, "a") as myfile:
        outline = "%s;%s" % (timestr, timenr)
        print outline
        myfile.write(outline);
        myfile.write("\n")
     
def get_last_restarted_timestamp():
    if (not os.path.exists(FILE_WITH_LAST_RESTART_TIME)):
        return -1
    return os.path.getmtime(FILE_WITH_LAST_RESTART_TIME);
   
def add_line_to_file(timestr, timenr, running, restarting_arcgis):
    if (not os.path.exists(OUTPUT_FILE)):
        opf = open(OUTPUT_FILE, "wt")
        opf.write("%s;%s;%s;%s" % ('time', 'seconds since base_moment', 'ArcGIS running', 'Will start/restart ArcGIS'));
        opf.write("\n")
        opf.close()
    with open(OUTPUT_FILE, "a") as myfile:
        outline = "%s;%s;%s;%s" % (timestr, timenr, running, restarting_arcgis)
        print outline
        myfile.write(outline);
        myfile.write("\n")
    
def restart_arcgis():
    print "restarting ArcGIS!\n"
    os.system(ARCGIS_INSTALLATION_FOLDER + "/server/stopserver.sh");
    time.sleep(20) # wait for ArcGIS to shutdown
    os.system(ARCGIS_INSTALLATION_FOLDER + "/server/startserver.sh");

def main():    
    moment_now = datetime.datetime.fromtimestamp(time.time())
    delta_timestamp = (moment_now - BASE_MOMENT)
    delta_timestamp_seconds = delta_timestamp.seconds + delta_timestamp.days * 3600*24
    arcgis_running = arcgis_is_running()
    will_restart_arcgis = (not arcgis_running) or ((moment_now.hour < 3) and (time.time() - get_last_restarted_timestamp() > 5 * 24 * 3600))
    add_line_to_file(moment_now.isoformat(' '), delta_timestamp_seconds, arcgis_running, will_restart_arcgis)
    if will_restart_arcgis:
        restart_arcgis() # restart AMP after midnight if it is close to running out of connections
        add_line_to_restarts_file(moment_now.isoformat(' '), delta_timestamp_seconds)    
    sys.exit(0)
    
if __name__ == "__main__":
    main()
    