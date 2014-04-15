<?php

/**
 * Implementation of hook_ctools_plugin_directory().
 */
function ampp_projects_ctools_plugin_directory($owner, $plugin_type) {
  if ($owner == 'ctools' && $plugin_type == 'content_types') {
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

  $items['ampp_search/results_total'] = array(
    'page callback' => 'ampp_search_get_results_total',
    'page arguments' => array(),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );

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
 * Get the total amount for a given transaction type.
 *
 * @param
 *   A transaction type ID:
 *    - AMP_TRANSACTION_TYPE_COMMITMENT for commitments
 *    - AMP_TRANSACTION_TYPE_DISBURSEMENT for disbursements
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

  // Load all rows, this makes the query very slow!
  $tmpview->set_items_per_page(0);

  // Prepare the view.
  $tmpview->set_arguments($args);
  $tmpview->build('panel_pane_1');

  // We don't need a count query.
  // @TODO: Make use_count_query() return FALSE in order not to execute_count_query();
  // $view->get_total_rows = FALSE;
  // $view->query->pager->use_count_query()

  $tmpview->pre_execute();

  // Remove unused fields and order by.
  $tmpview->query->orderby = array();
  foreach ($tmpview->query->fields as $key => $value) {
    if ($key != 'amp_activity_id') {
      unset($tmpview->query->fields[$key]);
    }
  }

  // Execute the view and get the list of filtered public activities.
  $tmpview->execute();
  $activities_list = array();
  foreach ($tmpview->result as $row) {
    $activities_list[] = $row->amp_activity_id;
  }

  // Only display 'Actual' commitments and disbursements.
  $adjustment_type_id = _amp_activity_get_adjustment_type_id(AMP_ADJUSTMENT_TYPE_ACTUAL);

  // If the view is filtered by Donors, only show values for the selected Donors.
  $donors_list = array();
  foreach ($tmpview->argument as $argument_handler) {
    if (isset($argument_handler->role_value) && in_array('DN', $argument_handler->role_value)) {
      if ($argument_handler->argument != 'all' && !empty($argument_handler->value)) {
        $donors_list = $argument_handler->value;
      }
    }
  }

  // Get all transactions amount in the base currency.
  $all_transactions = _amp_activity_get_activity_funding_details($transaction_type, $adjustment_type_id, $activities_list, $donors_list);

  // Add all transactions.
  $total_amount = (int) array_sum($all_transactions);

  // Store the total amount in cache.
  cache_set($cache_id, $total_amount, 'cache', CACHE_TEMPORARY);

  return $total_amount;
}

/**
 * Menu callback for ampp_search/results_total.
 *
 * @TODO: Cleanup and remove old URL mapping.
 */
function ampp_search_get_results_total() {
  $values = array(
    'commitment' => 0,
    'disbursement' => 0,
  );

  $commitment_amount = amp_get_transactions_amount(AMP_TRANSACTION_TYPE_COMMITMENT);
  $disbursement_amount = amp_get_transactions_amount(AMP_TRANSACTION_TYPE_DISBURSEMENT);

  if (module_exists('format_number')) {
    $values['commitment'] = empty($commitment_amount) ? '0' : format_number($commitment_amount, 0);
    $values['disbursement'] = empty($disbursement_amount) ? '0' : format_number($disbursement_amount, 0);
  }
  else {
    $values['commitment'] = empty($commitment_amount) ? '0' : number_format(intval($commitment_amount));
    $values['disbursement'] = empty($disbursement_amount) ? '0' : number_format(intval($disbursement_amount));
  }

  drupal_json_output($values);
}
