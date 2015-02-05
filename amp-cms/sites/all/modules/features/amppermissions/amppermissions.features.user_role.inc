<?php
/**
 * @file
 * amppermissions.features.user_role.inc
 */

/**
 * Implements hook_user_default_roles().
 */
function amppermissions_user_default_roles() {
  $roles = array();

  // Exported role: admin user.
  $roles['admin user'] = array(
    'name' => 'admin user',
    'weight' => 3,
  );

  // Exported role: editor user.
  $roles['editor user'] = array(
    'name' => 'editor user',
    'weight' => 2,
  );

  // Exported role: notification user.
  $roles['notification user'] = array(
    'name' => 'notification user',
    'weight' => 4,
  );

  return $roles;
}
