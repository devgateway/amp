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
define('PROJECT_ENVIRONMENT', 'production');

// Allow developers to debug environments.
$hide_errors = TRUE;
if ($hide_errors && PROJECT_ENVIRONMENT == 'production') {
  ini_set('error_reporting', E_ALL & ~E_DEPRECATED & ~E_STRICT);
  ini_set('display_errors', 'Off');
}
else {
  ini_set('error_reporting', E_ALL);
  ini_set('display_errors', 'On');
}

// Setup the database configuration.
$databases = array();

// Main database settings.
$databases['default']['default'] = array(
  'driver'   => 'pgsql',
  'database' => 'amp_cms_local',
  'username' => 'amp_cms_user',
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
 * Deny access to unwanted visitors.
 */
$deny_access = FALSE;
if ($deny_access && php_sapi_name() != 'cli') {
  if (empty($_COOKIE['KnockKnock']) || $_COOKIE['KnockKnock'] != 'Nobel') {
    // - Knock, Knock. - Who’s there? - Nobel. - Nobel who? - No bell, that’s why I knocked!
    print 'You are not welcome here!';
    exit();
  }
}

