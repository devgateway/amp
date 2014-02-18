#!/bin/bash
#
# Simple cron script for those who cannot
# use wget, lynx or curl because the host
# closed the localhost loop.
#
# Usage:
# - put this file in the ~/scripts dir
# - add a cronjob pointing to this script
#
# Copyright OCS - 2006
# Copyright hansfn@drupal.org - 2008
# This script is provided under the GPL.
#
# v 0.1 original release
# V 0.2 added PHP parser options and verbosity
#    setting to keep it quiet so real errors can
#    be reported by cron.

#############################
# CONFIGURATION OPTIONS
#############################

# Complete local path
#
# Set the complete local path
# to where the cron.php file
# is (ie the root path)
# Default is /var/www/html/
if [ $# -eq 0 ] # should check for no arguments
then
    echo "Arg #1: Complete local path to where the cron.php is. Example: /var/www/html/"
    echo "Arg #2 (optional): complete path to the php parser. Default: /usr/bin/php"
    echo "Arg #3 (optional): Default to only report errors. Default: false"
    exit
fi
root_path=$1

# Complete php path
#
# Set the complete path
# to the php parser if
# different from standard
if [ "$2" ]
then parse=$2
else parse=/usr/bin/php
fi

# PHP parser options
#
# Defaulting to not report notices.
parse_options="-d error_reporting=2037"

# Verbosity
#
# Default to only report errors.
if [ "$3" ]
then verbose=$3
else verbose=false
fi

##############################
# END OF CONFIGURATION OPTIONS
##############################

cd $root_path

if [ -e "cron.php" ]
then
    $parse $parse_options cron.php
    if [ "$?" -ne "0" ]
    then
         echo "cron.php not parsed."
    else
         if $verbose
         then
             echo "cron.php has succesfully been parsed."
         fi
    fi
else
    echo "cron.php not found."
    exit
fi

exit