<?php


/*
 * define plugin
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('Commitments Report'),
  'description' => t('Provides Commitments report'),
  'category' => array(t('AMPP Widgets')),
  'edit form' => 'ampp_projects_commitments_report_form_content_type_edit_form',
  //'render callback' => 'ampp_projects_commitments_report_render',
  'hook theme' => 'ampp_projects_commitments_report_content_type_theme',
  'defaults' => array(
    'ndays' => 180,
    'sort' => 'name',
    'sort_direction' => 'ASC',
    'currency_symbol' => '',
  ),
);

/**
 *
 */
function ampp_projects_commitments_report_content_type_render($subtype, $conf, $panel_args, $context) {
  global $amp_db_exists;
  $block = new stdClass();
  $block->content = '';
  if ($amp_db_exists) {
    $reports_info = _amp_projects_get_commitments_report($conf['ndays'], $conf['sort'], $conf['sort_direction']);
    $commitments_report = $reports_info['reports'];
    $curr_symbol = empty($conf['currency_symbol']) ? '' : ' ' . $conf['currency_symbol'];
    $block->content = theme('ampp_projects_commitments_report', array(
      'curr_symbol' => $curr_symbol,
      'commitments_report' => $commitments_report,
      'total_amount' => $reports_info['total_amount'],
      'total_org_count' => $reports_info['total_org_count']
    ));
  }

  return $block;
}

/**
 *
 */
function ampp_projects_commitments_report_content_type_theme(&$theme, $plugin) {
  $theme['ampp_projects_commitments_report'] = array(
    'arguments' => array(
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
  db_set_active("amp_projects");

  $in_actual_query = db_select('amp_category_value', 'acv')
    ->fields('acv', array('id'))
    ->condition('acv.category_value', 'Actual', '=');

  $query = db_select('amp_funding_detail', 'afd')->fields('org', array('name'));

  $date_query = db_select('amp_funding', 'a');
  $date_query->addExpression("(CURRENT_DATE - INTERVAL ' " . $number_of_days . " days')", 'ddate');
  $date_query->range(0, 1);
  $result = $date_query->execute();
  $ddate = $result->fetchAssoc();
  $d_date = $ddate['ddate'];

  $query->innerJoin('amp_funding', 'af', 'afd.amp_funding_id = af.amp_funding_id');
  $query->addJoin('LEFT', 'cached_amp_activity', 'activity', 'activity.amp_activity_id = af.amp_activity_id');
  $query->innerJoin('amp_organisation', 'org', 'af.amp_donor_org_id = org.amp_org_id');
  $query->innerJoin('amp_currency', 'cur', 'afd.amp_currency_id = cur.amp_currency_id');
  $query->condition('afd.adjustment_type', $in_actual_query, 'IN')
    ->condition('afd.transaction_type', 0, '=')
    ->condition('afd.transaction_date', $d_date, '>=')->groupBy('org.name')
    ->addExpression('SUM(afd.transaction_amount / getexchange(cur.currency_code, afd.transaction_date ))', 'transaction_amount');

  $snippet = "activity.amp_team_id is not null";
  $query->where($snippet);

  $snippet = "activity.approval_status IN ('approved', 'startedapproved')";
  $snippet .= " AND activity.draft <> true AND activity.deleted <> true AND activity.archived <> true";
  $snippet .= " AND activity.amp_team_id IN (select amp_team_id from amp_team where parent_team_id IN (select amp_team_id from amp_team where access_type = 'Management'))";
  $snippet .= " OR (";
  $snippet .= "activity.approval_status IN ('approved', 'startedapproved')";
  $snippet .= " AND activity.draft <> true";
  $snippet .= " AND activity.amp_activity_id IN (select distinct(activity) from amp_org_role where organisation IN (select amp_org_id from amp_team_orgs where amp_team_id IN (select amp_team_id from amp_team where parent_team_id in (select amp_team_id from amp_team where access_type='Management')))))";
  $query->where($snippet);

  $query->addExpression('COUNT(org.name)', 'orgname_count');
  if ($order_field == 'name') {
    $field = 'org.name';
  }
  else {
    $field = 'transaction_amount';
  }
  $query->orderBy($field, $order_direction);
  $result = $query->execute();
  $reports = array();
  $total_amount = 0.00;
  $total_records_count = 0;

  foreach ($result as $record) {
    //$record->transaction_amount = sprintf('%.2f', $record->transaction_amount);
    $total_amount += floatval($record->transaction_amount);
    $total_records_count += $record->orgname_count;
    $record->transaction_amount = format_number($record->transaction_amount, '2');
    $reports[] = array(
      'name' => $record->name,
      'count' => $record->orgname_count,
      'amount' => $record->transaction_amount
    );
  }
  $total_amount = format_number($total_amount, '2');
  db_set_active();

  return array(
    'reports' => $reports,
    'total_amount' => $total_amount,
    'total_org_count' => $total_records_count
  );
}

/**
 *
 */
function ampp_projects_commitments_report_form_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  $form['override_title_markup']['#access'] = FALSE;

  $form['ndays'] = array(
    '#type' => 'textfield',
    '#title' => t('Number of past days'),
    '#description' => t(''),
    '#size' => 5,
    '#maxlength' => 5,
    '#default_value' => isset($conf['ndays']) ? intval($conf['ndays']) : $form_state['plugin']['defaults']['ndays'],
  );

  $form['sort'] = array(
    '#type' => 'radios',
    '#title' => t('Sort output by'),
    '#options' => array('name' => 'Donor name', 'amount' => 'Amount columns'),
    '#default_value' => isset($conf['sort']) ? $conf['sort'] : $form_state['plugin']['defaults']['sort'],
  );

  $form['sort_direction'] = array(
    '#type' => 'radios',
    '#title' => t('Sorting direction'),
    '#options' => array('ASC' => 'Ascending', 'DESC' => 'Descending'),
    '#default_value' => isset($conf['sort_direction']) ? $conf['sort_direction'] : $form_state['plugin']['defaults']['sort_direction'],
  );

  $form['currency_symbol'] = array(
    '#type' => 'textfield',
    '#title' => t('Currency symbol'),
    '#size' => 3,
    '#maxlength' => 3,
    '#default_value' => isset($conf['currency_symbol']) ? $conf['currency_symbol'] : $form_state['plugin']['defaults']['currency_symbol'],
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
