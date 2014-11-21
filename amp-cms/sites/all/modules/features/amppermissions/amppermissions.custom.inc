<?php
/**
 * @file
 * amppermissions.custom.inc
 */

/**
 * Implements hook_modules_enabled().
 */
function amppermissions_modules_enabled($modules) {
  $admin_role = user_role_load_by_name('admin user');

  if (in_array('l10n_client', $modules)) {
    user_role_grant_permissions($admin_role->rid, array('use on-page translation'));
  }
}

/**
 * Implements hook_modules_disabled().
 */
function amppermissions_modules_disabled($modules) {
  $admin_role = user_role_load_by_name('admin user');

  if (in_array('l10n_client', $modules)) {
    user_role_revoke_permissions($admin_role->rid, array('use on-page translation'));
  }
}
