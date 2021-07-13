#!/bin/bash

psql -U postgres -c "CREATE ROLE amp ENCRYPTED PASSWORD 'md550b803cf678bf4a8b26a93f679fcdd45' SUPERUSER CREATEDB CREATEROLE INHERIT LOGIN;"
createdb -U amp amp
# using ! before pg_restore to ignore returned error status
# reason: sometimes it fails while restoring postgres-gis tables, this is not critical problem so we will ignore it
! pg_restore -U amp -O -d amp /backups/amp_database.pgsql
psql -U amp -c "UPDATE dg_site_domain SET site_domain='$AMP_SITE_DOMAIN';"
psql -U amp -c "TRUNCATE amp_email_receiver;"
psql -U amp -c "UPDATE dg_user SET password='a9993e364706816aba3e25717850c26c9cd0d89d';"
psql -U amp -c "UPDATE amp_global_settings SET settingsvalue = (SELECT CONCAT(settingsvalue, '-', now()::timestamp(0)) FROM amp_global_settings WHERE settingsname = 'Default Country') WHERE settingsname = 'AMP Server ID';"
psql -U amp -c "UPDATE amp_global_settings SET settingsvalue = 'https://amp-registry-stg.ampsite.net/' WHERE settingsname = 'AMP Registry URL';"
psql -U amp -c "UPDATE amp_global_settings SET settingsvalue = 'true' WHERE settingsname = 'AMP Offline Enabled';"
psql -U amp -c "UPDATE amp_global_settings SET settingsvalue='off' WHERE settingsname = 'Secure Server';"
psql -U amp -c "UPDATE amp_global_settings SET settingsvalue='$AMP_GEOCODER_URL' WHERE settingsname = 'Geo coder URL';"