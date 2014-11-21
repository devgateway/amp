<?php
/**
 * @file
 * amppermissions.strongarm.inc
 */

/**
 * Implements hook_strongarm().
 */
function amppermissions_strongarm() {
  $export = array();

  $strongarm = new stdClass();
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'user_admin_role';
  $strongarm->value = '0';
  $export['user_admin_role'] = $strongarm;

  return $export;
}
