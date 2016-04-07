<?php
/**
 * @file
 * ampet_slide.features.user_permission.inc
 */

/**
 * Implements hook_user_default_permissions().
 */
function ampet_slide_user_default_permissions() {
  $permissions = array();

  // Exported permission: 'create slide content'.
  $permissions['create slide content'] = array(
    'name' => 'create slide content',
    'roles' => array(
      'admin user' => 'admin user',
      'editor user' => 'editor user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'delete any slide content'.
  $permissions['delete any slide content'] = array(
    'name' => 'delete any slide content',
    'roles' => array(
      'admin user' => 'admin user',
      'editor user' => 'editor user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'delete own slide content'.
  $permissions['delete own slide content'] = array(
    'name' => 'delete own slide content',
    'roles' => array(
      'admin user' => 'admin user',
      'editor user' => 'editor user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'edit any slide content'.
  $permissions['edit any slide content'] = array(
    'name' => 'edit any slide content',
    'roles' => array(
      'admin user' => 'admin user',
      'editor user' => 'editor user',
    ),
    'module' => 'node',
  );

  // Exported permission: 'edit own slide content'.
  $permissions['edit own slide content'] = array(
    'name' => 'edit own slide content',
    'roles' => array(
      'admin user' => 'admin user',
      'editor user' => 'editor user',
    ),
    'module' => 'node',
  );

  return $permissions;
}
