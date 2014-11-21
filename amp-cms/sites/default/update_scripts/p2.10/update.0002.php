<?php

// Provide a list of modules to be installed.
$modules = array(
  // 'admin',
  'admin_menu',
    // 'admin_devel',
    'admin_menu_toolbar',
    // 'admin_views',

  'ctools',
    // 'bulk_export',
    // 'ctools_access_ruleset',
    // 'ctools_ajax_sample',
    // 'ctools_custom_content',
    // 'ctools_plugin_example',
    // 'page_manager',
    // 'stylizer',
    // 'views_content',

  'features',
    'strongarm',

  'libraries',

  'title', // Replaces entity legacy fields with regular fields.

  /*
   * Install developer modules.
   */

  // 'coder',
  //   'coder_review',
  // 'devel',
  //   'devel_generate',
  //   'devel_node_access',
  'diff',
  'module_filter',
  // 'schema',
  // 'stage_file_proxy',

);
_us_module__install($modules);

// Set administration theme.
variable_set('admin_theme', 'seven');

// Use administration theme for node edit pages.
variable_set('node_admin_theme', 1);

// Increase the maximum number of messages to keep in the database log.
variable_set('dblog_row_limit', 100000);

// Only allow administrators to register accounts.
variable_set('user_register', USER_REGISTER_ADMINISTRATORS_ONLY);

// Do not allow users to upload pictures on their accounts.
variable_set('user_pictures', 0);

// Delete user picture settings.
db_delete('variable')
  ->condition('name', db_like('user_picture_') . '%', 'LIKE')
  ->execute();

// Automatically replace legacy fields with regular fields for new bundles.
$title_settings = array(
  'auto_attach' => array(
    'filename' => 'filename',
  ),
  'hide_label' => array(
    'entity' => 'entity',
    'page' => 0,
  ),
);
variable_set('title_file', $title_settings);

// Automatically replace legacy fields with regular fields for new bundles.
$title_settings = array(
  'auto_attach' => array(
    'title' => 'title',
  ),
  'hide_label' => array(
    'entity' => 'entity',
    'page' => 0,
  ),
);
variable_set('title_node', $title_settings);

// Automatically replace legacy fields with regular fields for new bundles.
$title_settings = array(
  'auto_attach' => array(
    'name' => 'name',
    'description' => 'description',
  ),
  'hide_label' => array(
    'entity' => 'entity',
    'page' => 0,
  ),
);
variable_set('title_taxonomy_term', $title_settings);

// Create a default role for site administrators, with all available permissions assigned.
// $admin_role = new stdClass();
// $admin_role->name = 'administrator';
// $admin_role->weight = 2;
// // Check if the role exists.
// $role = user_role_load_by_name($admin_role->name);
// if (empty($role)) {
//   user_role_save($admin_role);
//   user_role_grant_permissions($admin_role->rid, array_keys(module_invoke_all('permission')));
//   $role = $admin_role;
// }

// Assign all permissionts to the "administrator" role and assign the role to user 1.
// db_delete('users_roles')->condition('uid', 1)->execute();
// db_insert('users_roles')->fields(array('uid' => 1, 'rid' => $role->rid))->execute();
// user_role_grant_permissions($role->rid, array_keys(module_invoke_all('permission')));

// Set this as the administrator role.
// variable_set('user_admin_role', $role->rid);

// Create some default users.
// $accounts = array(
//   '2' => 'admin_user',
//   '3' => 'editor_user',
//   '100' => 'system_user',
// );
// foreach ($accounts as $account_id => $account_name) {
//   $account = user_load($account_id);
//   if (empty($account)) {
//     db_insert('users')
//       ->fields(array(
//         'uid' => $account_id,
//         'name' => $account_name,
//         'mail' => $account_name . '@example.org',
//         'created' => REQUEST_TIME,
//         'status' => ($account_id == 100) ? 0 : 1,
//         'data' => NULL,
//       ))
//       ->execute();
//   }
// }

// Set default export path for new features.
variable_set('features_default_export_path', 'sites/all/modules/features');
