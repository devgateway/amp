<?php

/**
 * @file
 * Drush Environment specific configuration sample file.
 * Copy this file to drushrc.custom.php and update the information.
 *
 * @see https://github.com/drush-ops/drush/blob/master/examples/example.drushrc.php
 *
 * NOTE: Do not commit 'drushrc.custom.php' to version control!
 */

/**
 * Drupal's $GLOBALS['base_url'] will be set to http://default. This may cause
 * some functionality to not work as expected (e.g. inline images)
 */
// $options['uri'] = "http://amp-cms-local.dev"; // Without trailing slash.

/**
 * List of tables to be omitted entirely from SQL dumps made by the 'sql-dump'
 * and 'sql-sync' commands when the "--skip-tables-key=common" option is
 * provided on the command line. This is useful if your database contains
 * non-Drupal tables used by some other application or during a migration for
 * example. You may add new tables to the existing array or add a new element.
 */
// $options['skip-tables']['common'] = array('migration_*');

/**
 * Useful shell aliases:
 *
 * Drush shell aliases act similar to git aliases. For best results, define
 * aliases in one of the drushrc file locations between #3 through #6 above.
 * More information on shell aliases can be found via:
 * `drush topic docs-shell-aliases` on the command line.
 *
 * @see https://git.wiki.kernel.org/index.php/Aliases#Advanced
 */
// $options['shell-aliases']['wipe'] = 'cache-clear all';
