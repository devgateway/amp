#!/usr/bin/awk -f

#Author: Arty
{
          if ((t = index($0, "/*")) != 0) {
               # value of `tmp' will be "" if t is 1
               tmp = substr($0, 1, t - 1)
               u = index(substr($0, t + 2), "*/")
               while (u == 0) {
                    if (getline <= 0) {
                         m = "unexpected EOF or error"
                         m = (m ": " ERRNO)
                         print m > "/dev/stderr"
                         exit
                    }
                    t = -1
                    u = index($0, "*/")
               }
               # substr expression will be "" if */
               # occurred at end of line
               $0 = tmp substr($0, u + 2)
          }
	  if ($0 ~ /^[ ]*\/\//){
	  }
	  else
              print $0
     }
