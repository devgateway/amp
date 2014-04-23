<?php

/**
 * Define plugin.
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('Commitments Report'),
  'description' => t('Provides Commitments report'),
  'category' => array(t('AMPP Widgets')),
  'edit form' => 'ampp_projects_commitments_report_form_content_type_edit_form',
  'hook theme' => 'ampp_projects_commitments_report_content_type_theme',
  'defaults' => array(
    'ndays' => 180,
    'sort' => 'amount',
    'sort_direction' => 'ASC',
    'currency_symbol' => '',
  ),
);

/**
 * Render callback.
 */
function ampp_projects_commitments_report_content_type_render($subtype, $conf, $panel_args, $context) {
  $block = new stdClass();
  $block->content = t('Could not connect to the activities database. Please try again later. If the problem persists contact the site administrator.');

  if (!amp_activity_check_db()) {
    return $block;
  }

  // Merge in plugin defaults, prevents not defined warnings on existing panels.
  $plugin_definition = ctools_get_plugins('ctools', 'content_types', $subtype);
  $conf = drupal_array_merge_deep($plugin_definition['defaults'], $conf);

  $reports_info = _amp_projects_get_commitments_report($conf['ndays'], $conf['sort'], $conf['sort_direction']);
  $reports = $reports_info['reports'];


  $block->content = theme('ampp_projects_commitments_report', array(
    'currency_symbol' => $conf['currency_symbol'],
    'commitments_report' => $reports,
    'total_amount' => $reports_info['total_amount'],
    'total_org_count' => $reports_info['total_org_count']
  ));

  return $block;
}

/**
 *
 */
function ampp_projects_commitments_report_content_type_theme(&$theme, $plugin) {
  $theme['ampp_projects_commitments_report'] = array(
    'arguments' => array(
      'currency_symbol' => NULL,
      'commitments_report' => NULL,
      'total_amount' => NULL,
      'total_org_count' => NULL
    ),
    'path' => $plugin['path'],
    'template' => 'commitments_report'
  );
}

/**
 *
 */
function _amp_projects_get_commitments_report($number_of_days, $order_field, $order_direction) {
  $cache_id = __FUNCTION__;
  if ($cache = cache_get($cache_id)) {
    return $cache->data;
  }

  db_set_active(AMP_DATABASE);

  $query = db_select('amp_funding', 'af');
  $query->leftJoin('cached_amp_activity', 'cached_amp_activity', 'cached_amp_activity.amp_activity_id = af.amp_activity_id');
  $query->leftJoin('amp_funding_detail', 'amp_funding_detail', 'amp_funding_detail.amp_funding_id = af.amp_funding_id');
  $query->leftJoin('amp_organisation', 'amp_organisation', 'af.amp_donor_org_id = amp_organisation.amp_org_id');
  $query->leftJoin('amp_currency', 'amp_currency', 'amp_funding_detail.amp_currency_id = amp_currency.amp_currency_id');

  /**
   * START public activities filter.
   */
  $query->where('cached_amp_activity.draft <> TRUE');
  $query->where('cached_amp_activity.amp_team_id IS NOT NULL');
  $query->where('cached_amp_activity.approval_status IN (\'approved\', \'startedapproved\')');

  $db_or = db_or();

  // Get activities for which the team has a parent of type "Management".
  $case_one_sub = "SELECT amp_team_id FROM amp_team WHERE access_type = 'Management'";
  $case_one = "SELECT amp_team_id FROM amp_team WHERE parent_team_id IN ($case_one_sub)";
  $db_or->where("cached_amp_activity.amp_team_id IN ($case_one)");

  // Get activities for which one of the organisations is in a team that has a parent of type "Management".
  $case_two_sub = "SELECT amp_org_id FROM amp_team_orgs WHERE amp_team_id IN ($case_one)";
  $case_two = "SELECT DISTINCT(activity) FROM amp_org_role WHERE organisation IN ($case_two_sub)";
  $db_or->where("cached_amp_activity.amp_activity_id IN ($case_two)");

  $query->condition($db_or);
  /**
   * END public activities filter.
   */

  // Get the translated organization name.
  $i18n_info = amp_i18n_get_translate_expression('organisation_name', NULL, 'amp_organisation', 'amp_org_id');
  $query->addExpression($i18n_info['expression'], 'organisation_name', $i18n_info['args']);
  $query->groupBy('amp_organisation.amp_org_id');

  // Get the number of organizations.
  $query->addExpression('COUNT(amp_organisation.amp_org_id)', 'organisation_count');

  // Prepare an expresion that can get the transaction_amount in the base curency.
  $transaction_amount_expression = 'SUM(amp_funding_detail.transaction_amount / getexchange(amp_currency.currency_code, amp_funding_detail.transaction_date))';
  $query->addExpression($transaction_amount_expression, 'transaction_amount_in_base_currency');

  // Only display 'Actual' commitments and disbursements.
  $adjustment_type_id = _amp_activity_get_adjustment_type_id(AMP_ADJUSTMENT_TYPE_ACTUAL);

  // Get the "timestamp without time zone" required to filter by amp_funding_detail.transaction_date.
  $timestamp = strtotime($number_of_days . ' days ago');
  $past_date = date('Y-m-d 00:00:00', $timestamp);

  $query->condition('amp_funding_detail.adjustment_type', $adjustment_type_id);
  $query->condition('amp_funding_detail.transaction_type', AMP_TRANSACTION_TYPE_COMMITMENT);
  $query->condition('amp_funding_detail.transaction_date', $past_date, '>=');

  // Order results.
  switch ($order_field) {
    case 'name':
      $query->orderBy('organisation_name', $order_direction);
      break;

    case 'amount':
    default:
      $query->orderBy('transaction_amount_in_base_currency', $order_direction);
      break;
  }

  $result = $query->execute();
  $reports = array();
  $total_amount = 0.00;
  $total_records_count = 0;

  foreach ($result as $record) {
    $total_amount += floatval($record->transaction_amount_in_base_currency);
    $total_records_count += $record->organisation_count;
    $record->transaction_amount_in_base_currency = format_number($record->transaction_amount_in_base_currency, '2');
    $reports[] = array(
      'name' => $record->organisation_name,
      'count' => $record->organisation_count,
      'amount' => $record->transaction_amount_in_base_currency,
    );
  }
  $total_amount = format_number($total_amount, '2');
  db_set_active();

  $results = array(
    'reports' => $reports,
    'total_amount' => $total_amount,
    'total_org_count' => $total_records_count,
  );

  // Store the results in cache for 12h.
  $timestamp = REQUEST_TIME + (60 * 60 * 12);
  cache_set($cache_id, $results, 'cache', $timestamp);

  return $results;
}

/**
 *
 */
function ampp_projects_commitments_report_form_content_type_edit_form($form, &$form_state) {
  // Merge in plugin defaults, prevents not defined warnings on existing panels.
  $plugin_defaults = $form_state['plugin']['defaults'];
  $conf = drupal_array_merge_deep($plugin_defaults, $form_state['conf']);

  // Hide the override_title description text.
  $form['override_title_markup']['#access'] = FALSE;

  $form['ndays'] = array(
    '#type' => 'textfield',
    '#title' => t('Number of past days'),
    '#size' => 5,
    '#maxlength' => 5,
    '#default_value' => $conf['ndays'],
  );

  $form['sort'] = array(
    '#type' => 'radios',
    '#title' => t('Sort output by'),
    '#options' => array(
      'amount' => t('Amount'),
      'name' => t('Donor name'),
    ),
    '#default_value' => $conf['sort'],
  );

  $form['sort_direction'] = array(
    '#type' => 'radios',
    '#title' => t('Sorting direction'),
    '#options' => array(
      'ASC' => t('Ascending'),
      'DESC' => t('Descending'),
    ),
    '#default_value' => $conf['sort_direction'],
  );

  $form['currency_symbol'] = array(
    '#type' => 'textfield',
    '#title' => t('Currency symbol (code)'),
    '#size' => 3,
    '#maxlength' => 3,
    '#default_value' => $conf['currency_symbol'],
  );

  return $form;
}

/*
 * handles edit for submit
 */
function ampp_projects_commitments_report_form_content_type_edit_form_submit(&$form, &$form_state) {
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}
