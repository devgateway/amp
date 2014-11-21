
This folder should contain taks specific update scripts with the following name format:
  VERB.NOWN.php

For example:
  cleanup.users.php
  disable.solr.php


Doc style comments should be added at the beginning at each file. The following
tags will be displayed in the UI: @file and @desc.

/**
 * @file: Cleanup user accounts
 * @desc: Deletes oldest 500 users that are blocked, have never logged in and were created more than two weeks ago.
 */
