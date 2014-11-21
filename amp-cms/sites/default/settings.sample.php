<?php

/**
 * @file
 * Environment specific configuration overrides sample file.
 * Copy this file to settings.custom.php and update the information.
 *
 * Add the following lines to the settings file:
 *  - comment: Allow environment specific configuration overrides.
 *  - phpcode: include_once('settings.custom.php');
 *
 * NOTE: Do not commit 'settings.custom.php' to version control!
 */

/**
 * The current project environment.
 * NOTE: The variable can be used inside update scripts, use only the following
 *       values: local, sample, staging, preprod, production, other.
 */
define('PROJECT_ENVIRONMENT', 'local');

// Allow developers to debug production environments.
$hide_errors = TRUE;
if ($hide_errors && PROJECT_ENVIRONMENT == 'production') {
  ini_set('error_reporting', E_ALL & ~E_DEPRECATED);
  ini_set('display_errors', FALSE);
  ini_set('display_startup_errors', FALSE);
}

// Setup the database configuration.
$databases = array();

// Main database settings.
$databases['default']['default'] = array(
  'driver'   => 'pgsql',
  'database' => 'cms_local',
  'username' => 'cms_user',
  'password' => 'demo1',
  'host'     => 'localhost',
  'port'     => '',
  'prefix'   => '',
);

/**
 * Salt for one-time login links and cancel links, form tokens, etc. It should
 * be unique to each environment because of security concerns.
 *
 * NOTE: If this variable is empty, a hash of the serialized database
 * credentials will be used as a fallback salt.
 */
$drupal_hash_salt = '';

/**
 * "Stage File Proxy" is recommended for development environments to easily
 * retrieve files from the origin environment of the DB.
 */
// $conf['stage_file_proxy_origin'] = "http://example.org"; // no trailing slash!
// $conf['stage_file_proxy_origin_dir'] = 'sites/default/files';

/**
 * Sample code to deny access to unwanted visitors. Useful for DB sample environments.
 */
// if (php_sapi_name() != 'cli') {
//   if (empty($_COOKIE['KnockKnock']) || $_COOKIE['KnockKnock'] != 'Nobel') {
//     // - Knock, Knock. - Who’s there? - Nobel. - Nobel who? - No bell, that’s why I knocked!
//     $url = 'http://example.org/';
//     print 'You are not welcome here! Maybe you wanted to try: ';
//     print "<a href=\"$url\">$url</a>";
//     exit();
//   }
// }
