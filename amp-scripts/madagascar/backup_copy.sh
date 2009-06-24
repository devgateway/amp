#!/bin/bash
cdate=`date +%Y%m%d`
scp  amp@amp-madagascar.gov.mg:/home/amp/backups/amp-madagascar-daily.dump.tar.gz ./backups/$cdate-amp-madagascar-daily.dump.tar.gz
