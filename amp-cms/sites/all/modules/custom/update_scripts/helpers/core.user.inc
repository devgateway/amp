<?php

/**
 * @file
 * Update Scripts helper functions for the core User module.
 */

/**
 * Create a new user.
 */
function _us_user__create($new_user_data) {
  // Get a list of available roles.
  $roles = db_select('role', 'r')
    ->fields('r', array('rid', 'name'))
    ->condition('r.rid', array(DRUPAL_ANONYMOUS_RID, DRUPAL_AUTHENTICATED_RID), 'NOT IN')
    ->execute()
    ->fetchAllKeyed();

  // Only add roles that exist.
  $new_user_roles = array();
  foreach ($new_user_data['roles'] as $role_name) {
    if ($role_id = array_search($role_name, $roles)) {
      $new_user_roles[$role_id] = $role_name;
    }
  }
  $new_user_data['roles'] = $new_user_roles;

  // Check if the user already exists.
  $account = user_load_by_name($new_user_data['name']);
  if (empty($account)) {
    // Add some extra settings.
    $new_user_data += array(
      'pass' => user_password(),
      'status' => 1,
      'notify' => 0,
    );
    return user_save(drupal_anonymous_user(), $new_user_data);
  }
  else {
    return user_save($account, $new_user_data);
  }
}
