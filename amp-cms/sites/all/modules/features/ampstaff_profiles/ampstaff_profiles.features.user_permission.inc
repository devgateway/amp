<?php
/**
 * @file
 * ampstaff_profiles.features.user_permission.inc
 */

/**
 * Implements hook_user_default_permissions().
 */
function ampstaff_profiles_user_default_permissions() {
  $permissions = array();

  // Exported permission: 'create staff content'.
  $permissions['create staff content'] = array(
    'name' => 'create staff content',
    'roles' => array(),
    'module' => 'node',
  );

  // Exported permission: 'delete any staff content'.
  $permissions['delete any staff content'] = array(
    'name' => 'delete any staff content',
    'roles' => array(),
    'module' => 'node',
  );

  // Exported permission: 'delete own staff content'.
  $permissions['delete own staff content'] = array(
    'name' => 'delete own staff content',
    'roles' => array(),
    'module' => 'node',
  );

  // Exported permission: 'edit any staff content'.
  $permissions['edit any staff content'] = array(
    'name' => 'edit any staff content',
    'roles' => array(),
    'module' => 'node',
  );

  // Exported permission: 'edit own staff content'.
  $permissions['edit own staff content'] = array(
    'name' => 'edit own staff content',
    'roles' => array(),
    'module' => 'node',
  );

  return $permissions;
}
