<?php

/**
 * @file
 * Drush Environment specific configuration sample file.
 * Copy this file to drushrc.php and update the information.
 *
 * @see https://github.com/drush-ops/drush/blob/master/examples/example.drushrc.php
 *
 * NOTE: Do not commit 'drushrc.php' to version control!
 */

// Drupal's $GLOBALS['base_url'] will be set to http://default. This may cause
// some functionality to not work as expected (e.g. inline images)
# $options['uri'] = "http://amp-cms-local.dev"; // Without trailing slash.

/**
 * List of tables whose *data* is skipped by the 'sql-dump' and 'sql-sync'
 * commands when the "--structure-tables-key=common" option is provided.
 * You may add specific tables to the existing array or add a new element.
 *
 */
$options['structure-tables']['common'] = array('cache', 'cache_*', 'history', 'search_*', 'sessions', 'watchdog');

/**
 * List of tables to be omitted entirely from SQL dumps made by the 'sql-dump'
 * and 'sql-sync' commands when the "--skip-tables-key=common" option is
 * provided on the command line. This is useful if your database contains
 * non-Drupal tables used by some other application or during a migration for
 * example. You may add new tables to the existing array or add a new element.
 */
# $options['skip-tables']['common'] = array('migration_*');

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
$options['shell-aliases']['pull'] = '!git pull'; // We've all done it.
$options['shell-aliases']['pulldb'] = '!git pull && drush updatedb';
$options['shell-aliases']['setup'] = '!drush updatedb --yes && drush usr all --yes && drush usr task/revert.features.php --yes';

$options['shell-aliases']['wipe'] = 'cache-clear all';

$options['shell-aliases']['offline'] = 'variable-set -y --always-set maintenance_mode 1';
$options['shell-aliases']['online'] = 'variable-set -y --always-set maintenance_mode 0';
// $options['shell-aliases']['self-alias'] = 'site-alias @self --with-db --alias-name=new';
// $options['shell-aliases']['site-get'] = '@none php-eval "return drush_sitealias_site_get();"';

// Create a custom sql-dump command.
$drupal_root = drush_get_context('DRUSH_SELECTED_DRUPAL_ROOT');
$file_name = preg_replace('/[^A-Za-z0-9._-]/', '', basename($drupal_root)) . '.' . date('Ymd-Hi') . '.sql';
$options['shell-aliases']['dump'] = 'sql-dump --result-file=' . $file_name;

$command_specific['sql-dump'] = array(
  'gzip' => true,
  'structure-tables-key' => 'common',
);

// Create a custom site-install command.
$options['shell-aliases']['install'] = 'site-install ampbase';
$command_specific['site-install'] = array(
  // Set a name for user one.
  'account-name' => 'oneUser',
  // 'account-pass' => 'ThiSIs4R@nd0mPa$swOrD',

  // Update the site name and mail.
  'site-name' => 'Aid Management Platform',
  'site-mail' => 'no-reply@example.org',

  // Do not install update.module.
  'install_configure_form.update_status_module' => 'array(FALSE,FALSE)',
);
