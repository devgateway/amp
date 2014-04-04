<?php
/**
 * @file
 * ampp_projects.views_default.inc
 */

/**
 * Implements hook_views_default_views().
 */
function ampp_projects_views_default_views() {
  $export = array();

  $view = new view;
  $view->name = 'projects_search_result';
  $view->description = '';
  $view->tag = 'default';
  $view->base_table = 'cached_amp_activity';
  $view->human_name = 'Projects search result';
  $view->core = 7;
  $view->api_version = '3.0-alpha1';
  $view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */

  /* Display: Master */
  $handler = $view->new_display('default', 'Master', 'default');
  $handler->display->display_options['title'] = 'Projects search';
  $handler->display->display_options['access']['type'] = 'none';
  $handler->display->display_options['cache']['type'] = 'none';
  $handler->display->display_options['query']['type'] = 'views_query';
  $handler->display->display_options['query']['options']['disable_sql_rewrite'] = TRUE;
  $handler->display->display_options['query']['options']['query_comment'] = FALSE;
  $handler->display->display_options['exposed_form']['type'] = 'basic';
  $handler->display->display_options['pager']['type'] = 'full';
  $handler->display->display_options['pager']['options']['items_per_page'] = '20';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['pager']['options']['id'] = '0';
  $handler->display->display_options['pager']['options']['expose']['items_per_page_options_all'] = 0;
  $handler->display->display_options['style_plugin'] = 'table';
  $handler->display->display_options['style_options']['columns'] = array(
    'view_link' => 'view_link',
    'name' => 'name',
    'organisation_names' => 'organisation_names',
    'organisation_names_1' => 'organisation_names_1',
    'sector_names' => 'sector_names',
    'sector_names_1' => 'sector_names_1',
    'sector_names_2' => 'sector_names_2',
    'program_names' => 'program_names',
    'location_names' => 'location_names',
    'status_name' => 'status_name',
    'actual_start_date' => 'actual_start_date',
    'actual_completion_date' => 'actual_completion_date',
    'commitment_amount' => 'commitment_amount',
    'disbursement_amount' => 'disbursement_amount',
  );
  $handler->display->display_options['style_options']['default'] = 'actual_start_date';
  $handler->display->display_options['style_options']['info'] = array(
    'view_link' => array(
      'align' => '',
      'separator' => '',
    ),
    'name' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'organisation_names' => array(
      'align' => '',
      'separator' => '',
    ),
    'organisation_names_1' => array(
      'align' => '',
      'separator' => '',
    ),
    'sector_names' => array(
      'align' => '',
      'separator' => '',
    ),
    'sector_names_1' => array(
      'align' => '',
      'separator' => '',
    ),
    'sector_names_2' => array(
      'align' => '',
      'separator' => '',
    ),
    'program_names' => array(
      'align' => '',
      'separator' => '',
    ),
    'location_names' => array(
      'align' => '',
      'separator' => '',
    ),
    'status_name' => array(
      'sortable' => 0,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'actual_start_date' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'actual_completion_date' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'commitment_amount' => array(
      'align' => '',
      'separator' => '',
    ),
    'disbursement_amount' => array(
      'align' => '',
      'separator' => '',
    ),
  );
  $handler->display->display_options['style_options']['override'] = 1;
  $handler->display->display_options['style_options']['sticky'] = 0;
  $handler->display->display_options['style_options']['empty_table'] = 0;
  /* No results behavior: Global: Text area */
  $handler->display->display_options['empty']['area']['id'] = 'area';
  $handler->display->display_options['empty']['area']['table'] = 'views';
  $handler->display->display_options['empty']['area']['field'] = 'area';
  $handler->display->display_options['empty']['area']['empty'] = FALSE;
  /* Field: Activity: View Link */
  $handler->display->display_options['fields']['view_link']['id'] = 'view_link';
  $handler->display->display_options['fields']['view_link']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['view_link']['field'] = 'view_link';
  $handler->display->display_options['fields']['view_link']['label'] = '';
  $handler->display->display_options['fields']['view_link']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['external'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['view_link']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['view_link']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['view_link']['alter']['html'] = 0;
  $handler->display->display_options['fields']['view_link']['element_label_colon'] = FALSE;
  $handler->display->display_options['fields']['view_link']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['view_link']['hide_empty'] = 0;
  $handler->display->display_options['fields']['view_link']['empty_zero'] = 0;
  $handler->display->display_options['fields']['view_link']['hide_alter_empty'] = 0;
  /* Field: Activity: Name */
  $handler->display->display_options['fields']['name']['id'] = 'name';
  $handler->display->display_options['fields']['name']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['name']['field'] = 'name';
  $handler->display->display_options['fields']['name']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['name']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['name']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['name']['alter']['external'] = 0;
  $handler->display->display_options['fields']['name']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['name']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['name']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['name']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['name']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['name']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['name']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['name']['alter']['html'] = 0;
  $handler->display->display_options['fields']['name']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['name']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['name']['hide_empty'] = 0;
  $handler->display->display_options['fields']['name']['empty_zero'] = 0;
  $handler->display->display_options['fields']['name']['hide_alter_empty'] = 0;
  /* Field: Activity: Organisations having Role */
  $handler->display->display_options['fields']['organisation_names']['id'] = 'organisation_names';
  $handler->display->display_options['fields']['organisation_names']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['organisation_names']['field'] = 'organisation_names';
  $handler->display->display_options['fields']['organisation_names']['label'] = 'Donors';
  $handler->display->display_options['fields']['organisation_names']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['external'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['organisation_names']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['organisation_names']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['organisation_names']['alter']['html'] = 0;
  $handler->display->display_options['fields']['organisation_names']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['organisation_names']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['organisation_names']['hide_empty'] = 0;
  $handler->display->display_options['fields']['organisation_names']['empty_zero'] = 0;
  $handler->display->display_options['fields']['organisation_names']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['organisation_names']['metadata_value'] = array(
    'DN' => 'DN',
  );
  /* Field: Activity: Organisations having Role */
  $handler->display->display_options['fields']['organisation_names_1']['id'] = 'organisation_names_1';
  $handler->display->display_options['fields']['organisation_names_1']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['organisation_names_1']['field'] = 'organisation_names';
  $handler->display->display_options['fields']['organisation_names_1']['label'] = 'Beneficiary Agencies';
  $handler->display->display_options['fields']['organisation_names_1']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['external'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['alter']['html'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['organisation_names_1']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['organisation_names_1']['hide_empty'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['empty_zero'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['organisation_names_1']['metadata_value'] = array(
    'BA' => 'BA',
  );
  /* Field: Activity: Sectors by classification */
  $handler->display->display_options['fields']['sector_names']['id'] = 'sector_names';
  $handler->display->display_options['fields']['sector_names']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['sector_names']['field'] = 'sector_names';
  $handler->display->display_options['fields']['sector_names']['label'] = 'Sectors';
  $handler->display->display_options['fields']['sector_names']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['external'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['sector_names']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['sector_names']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['sector_names']['alter']['html'] = 0;
  $handler->display->display_options['fields']['sector_names']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['sector_names']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['sector_names']['hide_empty'] = 0;
  $handler->display->display_options['fields']['sector_names']['empty_zero'] = 0;
  $handler->display->display_options['fields']['sector_names']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['sector_names']['metadata_operator'] = 'any';
  /* Field: Activity: Sectors by classification */
  $handler->display->display_options['fields']['sector_names_1']['id'] = 'sector_names_1';
  $handler->display->display_options['fields']['sector_names_1']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['sector_names_1']['field'] = 'sector_names';
  $handler->display->display_options['fields']['sector_names_1']['label'] = 'Primary Sectors';
  $handler->display->display_options['fields']['sector_names_1']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['external'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['sector_names_1']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['sector_names_1']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['alter']['html'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['sector_names_1']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['sector_names_1']['hide_empty'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['empty_zero'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['sector_names_1']['metadata_value'] = array(
    'Primary' => 'Primary',
  );
  /* Field: Activity: Sectors by classification */
  $handler->display->display_options['fields']['sector_names_2']['id'] = 'sector_names_2';
  $handler->display->display_options['fields']['sector_names_2']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['sector_names_2']['field'] = 'sector_names';
  $handler->display->display_options['fields']['sector_names_2']['label'] = 'Secondary Sectors';
  $handler->display->display_options['fields']['sector_names_2']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['external'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['sector_names_2']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['sector_names_2']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['alter']['html'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['sector_names_2']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['sector_names_2']['hide_empty'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['empty_zero'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['sector_names_2']['metadata_value'] = array(
    'Secondary' => 'Secondary',
  );
  /* Field: Activity: Program Names */
  $handler->display->display_options['fields']['program_names']['id'] = 'program_names';
  $handler->display->display_options['fields']['program_names']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['program_names']['field'] = 'program_names';
  /* Field: Activity: Location Names */
  $handler->display->display_options['fields']['location_names']['id'] = 'location_names';
  $handler->display->display_options['fields']['location_names']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['location_names']['field'] = 'location_names';
  $handler->display->display_options['fields']['location_names']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['external'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['location_names']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['location_names']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['location_names']['alter']['html'] = 0;
  $handler->display->display_options['fields']['location_names']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['location_names']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['location_names']['hide_empty'] = 0;
  $handler->display->display_options['fields']['location_names']['empty_zero'] = 0;
  $handler->display->display_options['fields']['location_names']['hide_alter_empty'] = 0;
  /* Field: Activity: Status */
  $handler->display->display_options['fields']['status_name']['id'] = 'status_name';
  $handler->display->display_options['fields']['status_name']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['status_name']['field'] = 'status_name';
  $handler->display->display_options['fields']['status_name']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['external'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['status_name']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['status_name']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['status_name']['alter']['html'] = 0;
  $handler->display->display_options['fields']['status_name']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['status_name']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['status_name']['hide_empty'] = 0;
  $handler->display->display_options['fields']['status_name']['empty_zero'] = 0;
  $handler->display->display_options['fields']['status_name']['hide_alter_empty'] = 0;
  /* Field: Activity: Start date */
  $handler->display->display_options['fields']['actual_start_date']['id'] = 'actual_start_date';
  $handler->display->display_options['fields']['actual_start_date']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['actual_start_date']['field'] = 'actual_start_date';
  $handler->display->display_options['fields']['actual_start_date']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['external'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['actual_start_date']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['actual_start_date']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['alter']['html'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['actual_start_date']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['actual_start_date']['hide_empty'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['empty_zero'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['actual_start_date']['date_format'] = 'shortest';
  /* Field: Activity: End date */
  $handler->display->display_options['fields']['actual_completion_date']['id'] = 'actual_completion_date';
  $handler->display->display_options['fields']['actual_completion_date']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['actual_completion_date']['field'] = 'actual_completion_date';
  $handler->display->display_options['fields']['actual_completion_date']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['external'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['alter']['html'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['actual_completion_date']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['actual_completion_date']['hide_empty'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['empty_zero'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['actual_completion_date']['date_format'] = 'shortest';
  /* Field: Activity: Commitment amount */
  $handler->display->display_options['fields']['commitment_amount']['id'] = 'commitment_amount';
  $handler->display->display_options['fields']['commitment_amount']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['commitment_amount']['field'] = 'commitment_amount';
  $handler->display->display_options['fields']['commitment_amount']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['external'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['commitment_amount']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['commitment_amount']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['alter']['html'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['commitment_amount']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['commitment_amount']['hide_empty'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['empty_zero'] = 0;
  $handler->display->display_options['fields']['commitment_amount']['hide_alter_empty'] = 0;
  /* Field: Activity: Disbursement amount */
  $handler->display->display_options['fields']['disbursement_amount']['id'] = 'disbursement_amount';
  $handler->display->display_options['fields']['disbursement_amount']['table'] = 'cached_amp_activity';
  $handler->display->display_options['fields']['disbursement_amount']['field'] = 'disbursement_amount';
  $handler->display->display_options['fields']['disbursement_amount']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['external'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['alter']['html'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['disbursement_amount']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['disbursement_amount']['hide_empty'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['empty_zero'] = 0;
  $handler->display->display_options['fields']['disbursement_amount']['hide_alter_empty'] = 0;
  /* Sort criterion: Activity: Start date */
  $handler->display->display_options['sorts']['actual_start_date']['id'] = 'actual_start_date';
  $handler->display->display_options['sorts']['actual_start_date']['table'] = 'cached_amp_activity';
  $handler->display->display_options['sorts']['actual_start_date']['field'] = 'actual_start_date';
  /* Contextual filter: Organization Role: Organisation having Role */
  $handler->display->display_options['arguments']['organisation_role']['id'] = 'organisation_role';
  $handler->display->display_options['arguments']['organisation_role']['table'] = 'amp_org_role';
  $handler->display->display_options['arguments']['organisation_role']['field'] = 'organisation_role';
  $handler->display->display_options['arguments']['organisation_role']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['organisation_role']['default_argument_skip_url'] = 0;
  $handler->display->display_options['arguments']['organisation_role']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['organisation_role']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['organisation_role']['break_phrase'] = 1;
  $handler->display->display_options['arguments']['organisation_role']['not'] = 0;
  $handler->display->display_options['arguments']['organisation_role']['role_value'] = array(
    'DN' => 'DN',
  );
  /* Contextual filter: Sector: Activity Sectors */
  $handler->display->display_options['arguments']['amp_sector_id']['id'] = 'amp_sector_id';
  $handler->display->display_options['arguments']['amp_sector_id']['table'] = 'amp_activity_sector';
  $handler->display->display_options['arguments']['amp_sector_id']['field'] = 'amp_sector_id';
  $handler->display->display_options['arguments']['amp_sector_id']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['amp_sector_id']['default_argument_skip_url'] = 0;
  $handler->display->display_options['arguments']['amp_sector_id']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['amp_sector_id']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['amp_sector_id']['summary_options']['items_per_page'] = '25';
  $handler->display->display_options['arguments']['amp_sector_id']['break_phrase'] = 1;
  $handler->display->display_options['arguments']['amp_sector_id']['not'] = 0;
  /* Contextual filter: Location: Activity Locations */
  $handler->display->display_options['arguments']['location_id']['id'] = 'location_id';
  $handler->display->display_options['arguments']['location_id']['table'] = 'amp_location';
  $handler->display->display_options['arguments']['location_id']['field'] = 'location_id';
  $handler->display->display_options['arguments']['location_id']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['location_id']['default_argument_skip_url'] = 0;
  $handler->display->display_options['arguments']['location_id']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['location_id']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['location_id']['summary_options']['items_per_page'] = '25';
  $handler->display->display_options['arguments']['location_id']['break_phrase'] = 1;
  $handler->display->display_options['arguments']['location_id']['not'] = 0;
  /* Contextual filter: Activity: Name */
  $handler->display->display_options['arguments']['name']['id'] = 'name';
  $handler->display->display_options['arguments']['name']['table'] = 'cached_amp_activity';
  $handler->display->display_options['arguments']['name']['field'] = 'name';
  $handler->display->display_options['arguments']['name']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['name']['default_argument_skip_url'] = 0;
  $handler->display->display_options['arguments']['name']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['name']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['name']['summary_options']['items_per_page'] = '25';
  $handler->display->display_options['arguments']['name']['glossary'] = 0;
  $handler->display->display_options['arguments']['name']['limit'] = '0';
  $handler->display->display_options['arguments']['name']['transform_dash'] = 0;
  $handler->display->display_options['arguments']['name']['break_phrase'] = 0;
  $handler->display->display_options['arguments']['name']['use_like'] = 1;
  /* Contextual filter: Category Values: Activities Projects Budget */
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['id'] = 'amp_categoryvalue_id';
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['table'] = 'amp_activities_categoryvalues';
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['field'] = 'amp_categoryvalue_id';
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['default_argument_skip_url'] = 0;
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['summary_options']['items_per_page'] = '25';
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['break_phrase'] = 1;
  $handler->display->display_options['arguments']['amp_categoryvalue_id']['not'] = 0;
  /* Contextual filter: Program: Activity Programs */
  $handler->display->display_options['arguments']['amp_program_id']['id'] = 'amp_program_id';
  $handler->display->display_options['arguments']['amp_program_id']['table'] = 'amp_activity_program';
  $handler->display->display_options['arguments']['amp_program_id']['field'] = 'amp_program_id';
  $handler->display->display_options['arguments']['amp_program_id']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['amp_program_id']['default_argument_skip_url'] = 0;
  $handler->display->display_options['arguments']['amp_program_id']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['amp_program_id']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['amp_program_id']['summary_options']['items_per_page'] = '25';
  $handler->display->display_options['arguments']['amp_program_id']['break_phrase'] = 1;
  $handler->display->display_options['arguments']['amp_program_id']['not'] = 0;
  /* Contextual filter: Activity: Start date */
  $handler->display->display_options['arguments']['actual_start_date']['id'] = 'actual_start_date';
  $handler->display->display_options['arguments']['actual_start_date']['table'] = 'cached_amp_activity';
  $handler->display->display_options['arguments']['actual_start_date']['field'] = 'actual_start_date';
  $handler->display->display_options['arguments']['actual_start_date']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['actual_start_date']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['actual_start_date']['summary_options']['override'] = TRUE;
  $handler->display->display_options['arguments']['actual_start_date']['condition_operator'] = '>=';
  /* Contextual filter: Activity: End date */
  $handler->display->display_options['arguments']['actual_completion_date']['id'] = 'actual_completion_date';
  $handler->display->display_options['arguments']['actual_completion_date']['table'] = 'cached_amp_activity';
  $handler->display->display_options['arguments']['actual_completion_date']['field'] = 'actual_completion_date';
  $handler->display->display_options['arguments']['actual_completion_date']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['actual_completion_date']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['actual_completion_date']['summary_options']['override'] = TRUE;
  $handler->display->display_options['arguments']['actual_completion_date']['condition_operator'] = '<=';
  /* Contextual filter: Organization Role: Organisation having Role */
  $handler->display->display_options['arguments']['organisation_role_1']['id'] = 'organisation_role_1';
  $handler->display->display_options['arguments']['organisation_role_1']['table'] = 'amp_org_role';
  $handler->display->display_options['arguments']['organisation_role_1']['field'] = 'organisation_role';
  $handler->display->display_options['arguments']['organisation_role_1']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['organisation_role_1']['default_argument_skip_url'] = 0;
  $handler->display->display_options['arguments']['organisation_role_1']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['organisation_role_1']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['organisation_role_1']['break_phrase'] = 1;
  $handler->display->display_options['arguments']['organisation_role_1']['not'] = 0;
  $handler->display->display_options['arguments']['organisation_role_1']['role_value'] = array(
    'BA' => 'BA',
  );
  /* Filter criterion: Activity: Filter Public */
  $handler->display->display_options['filters']['filter_public']['id'] = 'filter_public';
  $handler->display->display_options['filters']['filter_public']['table'] = 'cached_amp_activity';
  $handler->display->display_options['filters']['filter_public']['field'] = 'filter_public';

  /* Display: Content pane */
  $handler = $view->new_display('panel_pane', 'Content pane', 'panel_pane_1');
  $handler->display->display_options['defaults']['title'] = FALSE;
  $handler->display->display_options['defaults']['empty'] = FALSE;
  /* No results behavior: Global: Text area */
  $handler->display->display_options['empty']['area']['id'] = 'area';
  $handler->display->display_options['empty']['area']['table'] = 'views';
  $handler->display->display_options['empty']['area']['field'] = 'area';
  $handler->display->display_options['empty']['area']['empty'] = FALSE;
  $handler->display->display_options['empty']['area']['content'] = 'No results found';
  $handler->display->display_options['empty']['area']['format'] = 'plain_text';
  $handler->display->display_options['empty']['area']['tokenize'] = 0;
  $handler->display->display_options['pane_title'] = 'Projects search result pane view';
  $handler->display->display_options['argument_input'] = array(
    'amp_org_id' => array(
      'type' => 'panel',
      'context' => 'entity:file.extension',
      'context_optional' => 1,
      'panel' => '0',
      'fixed' => '',
      'label' => 'AMP Organizations: Organization Id',
    ),
    'amp_sector_id' => array(
      'type' => 'panel',
      'context' => 'entity:file.extension',
      'context_optional' => 0,
      'panel' => '1',
      'fixed' => '',
      'label' => 'AMP Sector Id: AMP Sector Id',
    ),
    'location_id' => array(
      'type' => 'panel',
      'context' => 'entity:file.extension',
      'context_optional' => 0,
      'panel' => '2',
      'fixed' => '',
      'label' => 'AMP Projects: Locations Ids',
    ),
    'name' => array(
      'type' => 'panel',
      'context' => 'entity:file.extension',
      'context_optional' => 0,
      'panel' => '3',
      'fixed' => '',
      'label' => 'AMP Projects: Project title',
    ),
  );
  $handler->display->display_options['inherit_panels_path'] = '1';

  /* Display: Feed */
  $handler = $view->new_display('feed', 'Feed', 'feed_1');
  $handler->display->display_options['pager']['type'] = 'some';
  $handler->display->display_options['pager']['options']['items_per_page'] = '0';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['style_plugin'] = 'xls';
  $handler->display->display_options['style_options']['filename'] = 'search-results';
  $handler->display->display_options['path'] = 'admin/search-xls-feed-dummy-path.xls';
  $translatables['projects_search_result'] = array(
    t('Master'),
    t('Projects search'),
    t('more'),
    t('Apply'),
    t('Reset'),
    t('Sort by'),
    t('Asc'),
    t('Desc'),
    t('Items per page'),
    t('- All -'),
    t('Offset'),
    t('Name'),
    t('Donors'),
    t('Beneficiary Agencies'),
    t('Sectors'),
    t('Primary Sectors'),
    t('Secondary Sectors'),
    t('Program Names'),
    t('Location Names'),
    t('Status'),
    t('Start date'),
    t('End date'),
    t('Commitment amount'),
    t('Disbursement amount'),
    t('All'),
    t('Content pane'),
    t('No results found'),
    t('Projects search result pane view'),
    t('View panes'),
    t('Feed'),
  );
  $export['projects_search_result'] = $view;

  $view = new view;
  $view->name = 'top_activities_by_disbursment';
  $view->description = '';
  $view->tag = 'default';
  $view->base_table = 'node';
  $view->human_name = 'Top activities by disbursment';
  $view->core = 7;
  $view->api_version = '3.0-alpha1';
  $view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */

  /* Display: Master */
  $handler = $view->new_display('default', 'Master', 'default');
  $handler->display->display_options['access']['type'] = 'perm';
  $handler->display->display_options['cache']['type'] = 'none';
  $handler->display->display_options['query']['type'] = 'views_query';
  $handler->display->display_options['query']['options']['query_comment'] = FALSE;
  $handler->display->display_options['exposed_form']['type'] = 'basic';
  $handler->display->display_options['pager']['type'] = 'some';
  $handler->display->display_options['pager']['options']['items_per_page'] = '10';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['style_plugin'] = 'table';
  $handler->display->display_options['style_options']['columns'] = array(
    'title' => 'title',
  );
  $handler->display->display_options['style_options']['default'] = '-1';
  $handler->display->display_options['style_options']['info'] = array(
    'title' => array(
      'sortable' => 0,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
  );
  $handler->display->display_options['style_options']['override'] = 1;
  $handler->display->display_options['style_options']['sticky'] = 0;
  $handler->display->display_options['style_options']['empty_table'] = 0;
  /* Field: Content: Title */
  $handler->display->display_options['fields']['title']['id'] = 'title';
  $handler->display->display_options['fields']['title']['table'] = 'node';
  $handler->display->display_options['fields']['title']['field'] = 'title';
  $handler->display->display_options['fields']['title']['label'] = 'Project title';
  $handler->display->display_options['fields']['title']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['title']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['title']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['title']['alter']['external'] = 0;
  $handler->display->display_options['fields']['title']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['title']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['title']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 0;
  $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 0;
  $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['title']['alter']['html'] = 0;
  $handler->display->display_options['fields']['title']['element_label_colon'] = 0;
  $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['title']['hide_empty'] = 0;
  $handler->display->display_options['fields']['title']['empty_zero'] = 0;
  $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['title']['link_to_node'] = 0;
  /* Field: Content: Primary sector */
  $handler->display->display_options['fields']['field_primary_sector']['id'] = 'field_primary_sector';
  $handler->display->display_options['fields']['field_primary_sector']['table'] = 'field_data_field_primary_sector';
  $handler->display->display_options['fields']['field_primary_sector']['field'] = 'field_primary_sector';
  $handler->display->display_options['fields']['field_primary_sector']['label'] = 'Sector working groups';
  $handler->display->display_options['fields']['field_primary_sector']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['external'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['alter']['html'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['field_primary_sector']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['field_primary_sector']['hide_empty'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['empty_zero'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['field_primary_sector']['field_api_classes'] = 0;
  /* Field: Content: Donor */
  $handler->display->display_options['fields']['field_donor']['id'] = 'field_donor';
  $handler->display->display_options['fields']['field_donor']['table'] = 'field_data_field_donor';
  $handler->display->display_options['fields']['field_donor']['field'] = 'field_donor';
  $handler->display->display_options['fields']['field_donor']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['external'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['field_donor']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['field_donor']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['field_donor']['alter']['html'] = 0;
  $handler->display->display_options['fields']['field_donor']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['field_donor']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['field_donor']['hide_empty'] = 0;
  $handler->display->display_options['fields']['field_donor']['empty_zero'] = 0;
  $handler->display->display_options['fields']['field_donor']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['field_donor']['field_api_classes'] = 0;
  /* Field: Content: DP last period */
  $handler->display->display_options['fields']['field_dp_last_period']['id'] = 'field_dp_last_period';
  $handler->display->display_options['fields']['field_dp_last_period']['table'] = 'field_data_field_dp_last_period';
  $handler->display->display_options['fields']['field_dp_last_period']['field'] = 'field_dp_last_period';
  $handler->display->display_options['fields']['field_dp_last_period']['label'] = 'Planned disbursements last period';
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['external'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['alter']['html'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['field_dp_last_period']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['field_dp_last_period']['hide_empty'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['empty_zero'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['field_dp_last_period']['settings'] = array(
    'thousand_separator' => ' ',
    'decimal_separator' => '.',
    'scale' => '2',
    'prefix_suffix' => 1,
  );
  $handler->display->display_options['fields']['field_dp_last_period']['field_api_classes'] = 0;
  /* Field: Content: DA last period */
  $handler->display->display_options['fields']['field_da_last_period']['id'] = 'field_da_last_period';
  $handler->display->display_options['fields']['field_da_last_period']['table'] = 'field_data_field_da_last_period';
  $handler->display->display_options['fields']['field_da_last_period']['field'] = 'field_da_last_period';
  $handler->display->display_options['fields']['field_da_last_period']['label'] = 'Actual disbursements last period';
  $handler->display->display_options['fields']['field_da_last_period']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['external'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['alter']['html'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['field_da_last_period']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['field_da_last_period']['hide_empty'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['empty_zero'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['field_da_last_period']['settings'] = array(
    'thousand_separator' => ' ',
    'decimal_separator' => '.',
    'scale' => '2',
    'prefix_suffix' => 1,
  );
  $handler->display->display_options['fields']['field_da_last_period']['field_api_classes'] = 0;
  /* Field: Content: DP current period */
  $handler->display->display_options['fields']['field_dp_current_period']['id'] = 'field_dp_current_period';
  $handler->display->display_options['fields']['field_dp_current_period']['table'] = 'field_data_field_dp_current_period';
  $handler->display->display_options['fields']['field_dp_current_period']['field'] = 'field_dp_current_period';
  $handler->display->display_options['fields']['field_dp_current_period']['label'] = 'Planned disbursements current period';
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['external'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['trim_whitespace'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['alter']['html'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['field_dp_current_period']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['field_dp_current_period']['hide_empty'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['empty_zero'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['hide_alter_empty'] = 0;
  $handler->display->display_options['fields']['field_dp_current_period']['settings'] = array(
    'thousand_separator' => ' ',
    'decimal_separator' => '.',
    'scale' => '2',
    'prefix_suffix' => 1,
  );
  $handler->display->display_options['fields']['field_dp_current_period']['field_api_classes'] = 0;
  /* Sort criterion: Content: DP current period (field_dp_current_period) */
  $handler->display->display_options['sorts']['field_dp_current_period_value']['id'] = 'field_dp_current_period_value';
  $handler->display->display_options['sorts']['field_dp_current_period_value']['table'] = 'field_data_field_dp_current_period';
  $handler->display->display_options['sorts']['field_dp_current_period_value']['field'] = 'field_dp_current_period_value';
  $handler->display->display_options['sorts']['field_dp_current_period_value']['order'] = 'DESC';
  /* Filter criterion: Content: Published */
  $handler->display->display_options['filters']['status']['id'] = 'status';
  $handler->display->display_options['filters']['status']['table'] = 'node';
  $handler->display->display_options['filters']['status']['field'] = 'status';
  $handler->display->display_options['filters']['status']['value'] = 1;
  $handler->display->display_options['filters']['status']['group'] = 0;
  $handler->display->display_options['filters']['status']['expose']['operator'] = FALSE;
  /* Filter criterion: Content: Type */
  $handler->display->display_options['filters']['type']['id'] = 'type';
  $handler->display->display_options['filters']['type']['table'] = 'node';
  $handler->display->display_options['filters']['type']['field'] = 'type';
  $handler->display->display_options['filters']['type']['value'] = array(
    'top_activities_by_disbursement' => 'top_activities_by_disbursement',
  );
  $translatables['top_activities_by_disbursment'] = array(
    t('Master'),
    t('more'),
    t('Apply'),
    t('Reset'),
    t('Sort by'),
    t('Asc'),
    t('Desc'),
    t('Project title'),
    t('Sector working groups'),
    t('Donor'),
    t('Planned disbursements last period'),
    t('Actual disbursements last period'),
    t('Planned disbursements current period'),
  );
  $export['top_activities_by_disbursment'] = $view;

  return $export;
}
