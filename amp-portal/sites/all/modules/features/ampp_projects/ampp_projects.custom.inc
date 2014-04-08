<?php

global $amp_db_exists;
$amp_db_exists = FALSE;

/**
 * Implementation of hook_init().
 */
function ampp_projects_init() {
  //Let's check if AMP database is configured and if not throw a worning message and also do not try to connect to it later
  amp_db_exists();
}

/**
 * Checking if AMP database is configured
 *
 * @TODO: Switch to drupal_static() and check if the DB connection actually works.
 */
function amp_db_exists() {
  global $amp_db_exists;

  global $databases;
  if (isset($databases['amp_projects']) && is_array($databases['amp_projects'])) {
    $amp_db_exists = TRUE;
  }
}

/**
 * Implementation of hook_ctools_plugin_directory().
 */
function ampp_projects_ctools_plugin_directory($owner, $plugin_type) {
  if($owner == 'ctools' && $plugin_type == 'content_types') {
    return 'plugins/content_types';
  }
}

/**
 * Implementation of hook_menu().
 */
function ampp_projects_menu() {
  $items['search_form'] = array(
    'title' => 'AMPP search form',
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_form'),
    'access callback' => TRUE,
    'description' => 'Item for AMPP serach form',
    'type' => MENU_NORMAL_ITEM,
  );

  // @HACK: Get the plugin specific menu routes.
  $module_path = drupal_get_path('module', 'ampp_projects');
  $file_path = $module_path . '/plugins/content_types/projects_search_form/projects_search_form_menu.inc';
  if (file_exists($file_path)) {
    require_once $file_path;
    _projects_search_form_menu_helper($items, $file_path);
  }

  return $items;
}

/**
 * Implementation of hook_date_popup_process_alter().
 *
 * @TODO: Remove!!!
 */
function ampp_projects_date_popup_process_alter(&$element, &$form_state, $context) {
  if ($context['form']['#form_id'] != 'projects_search_form') {
    return;
  }

  switch ($element['#name']) {
    case 'period_start' :
      $element['date']['#title'] = '';
      break;
    case 'period_end' :
      $element['date']['#title'] = t('to');
      break;
  }

  unset($element['date']['#description']);
}

/**
 * Get the total amount for given type of transactions:
 *  - AMP_TRANSACTION_TYPE_COMMITMENT for commitments
 *  - AMP_TRANSACTION_TYPE_DISBURSEMENT for disbursments
 */
function amp_get_transactions_amount($transaction_type) {
  $args = _ampp_get_search_arguments(TRUE);

  // Determine if any arguments were provided.
  $arguments_count = 0;
  foreach ($args as $value) {
    if ($value != 'all') {
      $arguments_count++;
    }
  }

  // Cache total transactions amount in order to speed up the views pager.
  $cache_id = __FUNCTION__ . ":noargs:$transaction_type";
  if ($arguments_count > 0) {
    $md5sum = md5(serialize($args));
    $cache_id = __FUNCTION__ . ":$md5sum:$transaction_type";
  }

  if ($cache = cache_get($cache_id)) {
    return $cache->data;
  }

  $view = views_get_view('projects_search_result');
  $tmpview = $view->clone_view();
  $tmpview->set_display('panel_pane_1');
  $tmpview->set_arguments($args);

  // Change the views query to get the total amounts for $transaction_type.
  $tmpview->get_total_amount_type = $transaction_type;

  // Load all rows, this makes the query very slow!
  $tmpview->set_items_per_page(0);

  $tmpview->pre_execute();
  $tmpview->execute();

  $total_amount = 0;
  foreach ($tmpview->result as $row) {
    $total_amount = $total_amount + $row->amount;
  }

  // Store the total amount in cache.
  cache_set($cache_id, $total_amount, 'cache', CACHE_TEMPORARY);

  return $total_amount;
}
