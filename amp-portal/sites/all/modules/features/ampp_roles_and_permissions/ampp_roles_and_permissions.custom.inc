<?php 
/**
 * Implementation of hook_permission()
 *
 */
function ampp_roles_and_permissions_permission() {
  return array(
    'access site errors reports' => array(
      'title' => t('Access to errors reports'),
      'description' => t('Show site errors statistics'),
    ),
  );
}


/**
 * Implements hook_menu_alter().
 */
function ampp_roles_and_permissions_menu_alter(&$items) {

  $items['admin/reports/access-denied']['access arguments'] =  array('access site errors reports');
  $items['admin/reports/page-not-found']['access arguments'] =   array('access site errors reports');
  $items['admin/reports/dblog']['access arguments'] =  array('access site errors reports');

  return $items;
}