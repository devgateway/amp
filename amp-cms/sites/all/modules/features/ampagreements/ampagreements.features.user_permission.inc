<?php
/**
 * @file
 * ampagreements.features.user_permission.inc
 */

/**
 * Implements hook_user_default_permissions().
 */
function ampagreements_user_default_permissions() {
  $permissions = array();

  // Exported permission: 'create agreement content'.
  $permissions['create agreement content'] = array(
    'name' => 'create agreement content',
    'roles' => array(
      'admin user' => 'admin user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'delete any agreement content'.
  $permissions['delete any agreement content'] = array(
    'name' => 'delete any agreement content',
    'roles' => array(
      'admin user' => 'admin user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'delete own agreement content'.
  $permissions['delete own agreement content'] = array(
    'name' => 'delete own agreement content',
    'roles' => array(
      'admin user' => 'admin user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'edit any agreement content'.
  $permissions['edit any agreement content'] = array(
    'name' => 'edit any agreement content',
    'roles' => array(
      'admin user' => 'admin user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'edit own agreement content'.
  $permissions['edit own agreement content'] = array(
    'name' => 'edit own agreement content',
    'roles' => array(
      'admin user' => 'admin user',
    ),
    'module' => 'node',
  );

  return $permissions;
}
