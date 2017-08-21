#!/bin/sh

# to create backup use tar since it can be compressed better than custom format, also skip mondrian, etl & jackrabbit tables
pg_dump --host localhost --port 5432 --username "amp" --no-password  --format tar -T 'rep*' -T 'mondrian_*' -T 'etl_*' --blobs --file "amp_tests_212.backup" "amp_tests_212"

# create the zip
7za a -mx=9 amp_tests_212.backup.7z amp_tests_212.backup

# remove plain backup
rm amp_tests_212.backup
