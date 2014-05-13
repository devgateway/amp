<?php

/**
 * @file
 * Displays a table containing the total commitments and disbursements for the current search results.
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('Search Result Totals'),
  'description' => t('Displays a table containing the total commitments and disbursements for the current search results.'),
  'category' => array(t('AMPP Widgets')),
  'hook theme' => 'search_result_totals_content_type_theme',
);

/**
 *
 */
function ampp_projects_search_result_totals_content_type_render($subtype, $conf, $panel_args, $context) {
  $block = new stdClass();
  $block->content = '';

  if (!amp_activity_check_db()) {
    return $block;
  }

  $variables = array(
    'commitment_header' => t('Commitment amount') . ' (' . amp_activity_get_base_currency() . ")",
    'disbursement_header' => t('Disbursement amount') . ' (' . amp_activity_get_base_currency() . ")",
    'commitment_amount' => t('calculating...'),
    'disbursement_amount' => t('calculating...'),
  );
  $block->content = theme('search_result_totals', $variables);

  // Add the endpoint to JS.
  $form_js_settings = array();
  $form_js_settings['resultstotal_endpoint'] = url('ampp_search/results_total');
  drupal_add_js(array('projectsSearchForm' => $form_js_settings), 'setting');

  return $block;
}

/**
 *
 */
function search_result_totals_content_type_theme(&$theme, $plugin) {
  $theme['search_result_totals'] = array(
    'variables' => array(
      'commitment_header' => NULL,
      'disbursement_header' => NULL,
      'commitment_amount' => NULL,
      'disbursement_amount' => NULL,
    ),
    'path' => $plugin['path'],
    'template' => 'search_result_totals',
  );
}
