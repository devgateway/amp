<?php
/**
 * @file
 * ampp_roles_and_permissions.features.user_permission.inc
 */

/**
 * Implements hook_user_default_permissions().
 */
function ampp_roles_and_permissions_user_default_permissions() {
  $permissions = array();

  // Exported permission: 'access site errors reports'.
  $permissions['access site errors reports'] = array(
    'name' => 'access site errors reports',
    'roles' => array(),
    'module' => 'ampp_roles_and_permissions',
  );

  return $permissions;
}
