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

/**
 * List of tables whose *data* is skipped by the 'sql-dump' and 'sql-sync'
 * commands when the "--structure-tables-key=common" option is provided.
 * You may add specific tables to the existing array or add a new element.
 *
 */
$options['structure-tables']['common'] = array('cache', 'cache_*', 'history', 'search_*', 'sessions', 'watchdog');

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
$options['shell-aliases']['setup'] = '!drush updatedb --yes && drush usr all --yes && drush usr task/revert.features.php --yes';
$options['shell-aliases']['offline'] = 'variable-set -y --always-set maintenance_mode 1';
$options['shell-aliases']['online'] = 'variable-set -y --always-set maintenance_mode 0';
// $options['shell-aliases']['self-alias'] = 'site-alias @self --with-db --alias-name=new';
// $options['shell-aliases']['site-get'] = '@none php-eval "return drush_sitealias_site_get();"';

// Create a custom sql-dump command.
// @TODO: Make this work with Drush 7.
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

/**
 * Allow environment specific overrides.
 */
try {
  include_once(dirname(__FILE__) . '/drushrc.custom.php');
}
catch (Exception $exception) {
}
