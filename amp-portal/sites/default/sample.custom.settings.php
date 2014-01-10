<?php

// Configure DB connection to your Drupal and AMP DBs as needed
$databases = array (
  'default' => array (
    'default' => array(
        'driver' => 'mysql',
        'database' => '',
        'username' => '',
        'password' => '',
        'host' => '',
        'prefix' => '',
        'collation' => 'utf8_general_ci',
    ),
  ),
  'amp_projects' => array(
    'default' => array(
        'driver' => 'pgsql',
        'database' => '',
        'username' => '',
        'password' => '',
        'host' => '',
        'prefix' => '',
        'collation' => 'utf8_general_ci',
    ),
  ),
);

// Uncomment below if you have memcache running on localhost port 11211
// If host or port are differnet see memcached configuration at http://drupal.org/node/1131468
//$conf['cache_class_cache_form'] = 'DrupalDatabaseCache';
//$conf['cache_backends'][] = 'sites\all\modules\contrib\memcache\memcache.inc';
//$conf['cache_default_class'] = 'MemCacheDrupal';
//$conf['memcache_key_prefix'] = 'ampp_';

// Uncomment one that corresponds to your environment
//define('PROJECT_ENVIRONMENT','LOCAL');
//define('PROJECT_ENVIRONMENT','STAGING');
//define('PROJECT_ENVIRONMENT','PREPROD');
//define('PROJECT_ENVIRONMENT','PRODUCTION');

$conf['maintenance_theme'] = 'ampp';
$drupal_hash_salt = 'xwWKUhKqgKWyfZmxM3wDaZiAfTxw62E7p1Ti5oW6B28';

error_reporting(E_ERROR);
//error_reporting(E_ALL);
//ini_set('display_errors', TRUE);
//ini_set('display_startup_errors', TRUE);