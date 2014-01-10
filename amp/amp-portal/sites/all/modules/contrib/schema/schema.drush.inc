<?php
// $Id: schema.drush.inc,v 1.1 2012/03/28 03:17:24 vamirbekyan Exp $

/**
 * @file
 *   Schema drush commands.
 */

/**
 * Implementation of hook_drush_command().
 */
function schema_drush_command() {
  $items = array();

  // the key in the $items array is the name of the command.
  $items['schema-inspect'] = array(
    'description' => "Show the Drupal schema definition for table(s)",
    'arguments' => array(
      'tables' => 'A comma delimited list of table names',
    ),
    'options' => array(
      'connection' => 'An alternate connection key to inspect',
    ),
  );
  return $items;
}

/**
 * Implementation of hook_drush_help().
 */
function schema_drush_help($section) {
  switch ($section) {
    case 'drush:schema-inspect':
      return dt("Show the Drupal schema definition for table(s).");
  }
}

/**
 * A drush command callback.
 */
function drush_schema_inspect() {
  $args = func_get_args();
  $names = explode(',', $args[0]);
  $connection = drush_get_option('connection');
  if (!$connection) {
    $connection = 'default';
  }
  foreach ($names as $name) {
    if ($table = schema_dbobject()->inspect($connection, $name)) {
      $output = schema_phpprint_table($name, $table[$name]);
      drush_print($output);
    }
    else {
      drush_set_error(dt('Mising table: @table', array('@table' => $name)));
    }
  }
}