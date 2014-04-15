<?php

/**
 * @file
 * Environment specific configuration overrides sample file.
 * Copy this file to custom.settings.php and update the information.
 *
 * NOTE: Do not commit 'custom.settings.php' to version control!
 */

/**
 * The current project environment.
 * NOTE: The variable can be used inside update scripts, only use one of the
 *       following values: local, staging, preprod, production, other.
 */
define('PROJECT_ENVIRONMENT', 'production');

// Setup the database configuration.
$databases = array();

// Database settings for the Public Portal.
$databases['default']['default'] = array (
  'database' => 'databasename',
  'username' => 'username',
  'password' => 'password',
  'host'     => 'localhost',
  'driver'   => 'mysql',
);

// Database settings for the AMP database.
$databases['amp_projects']['default'] = array (
  'database' => 'databasename',
  'username' => 'username',
  'password' => 'password',
  'host'     => 'localhost',
  'driver' => 'pgsql',
  // Throw an error if the AMP database didn't respond in less then a minute
  // 'pdo' => array(PDO::ATTR_TIMEOUT => 60),
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
 * Drupal automatically generates a unique session cookie name for each site
 * based on its full domain name. If you have multiple domains pointing at the
 * same Drupal site, you can either redirect them all to a single domain (see
 * comment in .htaccess), or uncomment the line below and specify their shared
 * base domain. Doing so assures that users remain logged in as they cross
 * between your various domains. Make sure to always start the $cookie_domain
 * with a leading dot, as per RFC 2109.
 */
// $cookie_domain = '.example.com';

/**
 * Override the managed files directory path.
 */
// $conf['file_public_path'] = 'sites/default/files';

/**
 * "Stage File Proxy" is recommended for development environments to easily
 * retrieve files from the origin environment of the DB.
 */
// $conf['stage_file_proxy_origin'] = "http://example.org"; // no trailing slash!
// $conf['stage_file_proxy_origin_dir'] = 'sites/default/files';

/**
 * Setup memcache as the default cache backend.
 */
// $conf['cache_backends'][] = 'sites/all/modules/contrib/memcache/memcache.inc';
// $conf['cache_default_class'] = 'MemCacheDrupal';

/**
 * If memcache is used ensure that the cache_form bin is assigned to
 * non-volatile storage.
 */
// $conf['cache_class_cache_form'] = 'DrupalDatabaseCache';

/**
 * Override the PHP environment error settings:
 *
 * @see http://www.php.net/manual/en/errorfunc.configuration.php
 */
// Development specific values:
// ini_set('error_reporting', E_ALL | E_STRICT);
// ini_set('display_errors', TRUE);
// ini_set('display_startup_errors', TRUE);

// Production specific values:
// ini_set('error_reporting', E_ALL & ~E_DEPRECATED);
// ini_set('display_errors', FALSE);
// ini_set('display_startup_errors', FALSE);
