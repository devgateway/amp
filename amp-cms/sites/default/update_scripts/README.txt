
It is recommended to organize update scripts into subdirectories, such as
"tasks" for update scripts that perform various tasks, and "environment" for
environment setup scripts and for large projects it's recommended to group
update scripts in sub-folders like:
  PHASE/update.XXXX.php
Where PHASE is the name of the phase and XXXX is an incremental number:
  p1.0/update.0103.php
  p2.0/update.0001.php
  p2.1/update.0074.php

For projects with multiple environments it's recommended to setup an environment
specific script that runs during each build for that environment. Or when
databases are copied from one environment to another; from production to a
development environment:
  environment/setup.ENV.php
Where ENV matches one of the possible values for PROJECT_ENVIRONMENT constant in
the settings file:
  environment/setup.staging.php
  environment/setup.production.php

Task specific scripts should be named using a verb and a noun:
  task/cleanup.users.php
  task/disable.solr.php
  task/enable.memcache.php

Doc style comments can be added at the beginning at each file. The following
tags will be displayed in the UI: @file and @desc.

/**
 * @file: Cleanup user accounts
 * @desc: Deletes oldest 500 users that are blocked, have never logged in and were created more than two weeks ago.
 */
