#!/bin/bash

#Author: Arty
# CONFIGURATION
path="/home/alexandru/Lucru/parser/awk"

# DO NOT EDIT BELOW!
#
dir=""
if [ -z "$1" ]
then
    echo "Usage: ./launch.sh <srcDir>"
else
    dir="$1"
fi

whereAmI=`pwd`
cd $dir
list=`dir -d *.java 2> /dev/null`
for file in $list ; do
    if [ -d $file ] 
    then
	i=0;
    else
	r=`$path/comments.sh "$file" | $path/hunter.sh`
	if [ "$r" != "Ok!" ]
	then
	    echo "in file: "$whereAmI"/"$file
	    $path/comments.sh "$file" | $path/hunter.sh
	fi
    fi	
done
for file in `dir -d * 2> /dev/null` ; do
    if [ -d $file ]
    then
	$path/launch.sh "$file"
    fi
done
cd $whereAmI
