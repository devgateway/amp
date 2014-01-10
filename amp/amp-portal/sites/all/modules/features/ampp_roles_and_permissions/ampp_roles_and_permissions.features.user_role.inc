<?php
/**
 * @file
 * ampp_roles_and_permissions.features.user_role.inc
 */

/**
 * Implements hook_user_default_roles().
 */
function ampp_roles_and_permissions_user_default_roles() {
  $roles = array();

  // Exported role: editor
  $roles['editor'] = array(
    'name' => 'editor',
    'weight' => '2',
  );

  return $roles;
}
