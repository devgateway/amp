<?php
/**
 * @file
 * ampactivities.views_default.inc
 */

/**
 * Implements hook_views_default_views().
 */
function ampactivities_views_default_views() {
  $export = array();

  $view = new view();
  $view->name = 'activities';
  $view->description = '';
  $view->tag = 'default';
  $view->base_table = 'efq_activity';
  $view->human_name = 'Activities';
  $view->core = 7;
  $view->api_version = '3.0';
  $view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */

  /* Display: Master */
  $handler = $view->new_display('default', 'Master', 'default');
  $handler->display->display_options['title'] = 'Activities';
  $handler->display->display_options['css_class'] = 'activities-search-results layout-block layout-separator';
  $handler->display->display_options['use_more_always'] = FALSE;
  $handler->display->display_options['access']['type'] = 'none';
  $handler->display->display_options['cache']['type'] = 'none';
  $handler->display->display_options['query']['type'] = 'views_query';
  $handler->display->display_options['exposed_form']['type'] = 'basic';
  $handler->display->display_options['exposed_form']['options']['submit_button'] = 'Search Activities';
  $handler->display->display_options['exposed_form']['options']['reset_button'] = TRUE;
  $handler->display->display_options['pager']['type'] = 'full';
  $handler->display->display_options['pager']['options']['items_per_page'] = '10';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['pager']['options']['id'] = '0';
  $handler->display->display_options['pager']['options']['quantity'] = '7';
  $handler->display->display_options['style_plugin'] = 'table';
  $handler->display->display_options['style_options']['columns'] = array(
    'external_url' => 'external_url',
    'title' => 'title',
    'beneficiary_agency' => 'beneficiary_agency',
    'donor_agency' => 'donor_agency',
    'primary_sector' => 'primary_sector',
    'location' => 'location',
    'actual_start_date' => 'actual_start_date',
    'actual_completion_date' => 'actual_completion_date',
    'actual_commitments' => 'actual_commitments',
    'actual_disbursements' => 'actual_disbursements',
    'ssc_bilateral_commitments' => 'ssc_bilateral_commitments',
    'ssc_triangular_commitments' => 'ssc_triangular_commitments',
  );
  $handler->display->display_options['style_options']['default'] = '-1';
  $handler->display->display_options['style_options']['info'] = array(
    'external_url' => array(
      'sortable' => 0,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'title' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'beneficiary_agency' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'donor_agency' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'primary_sector' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'location' => array(
      'sortable' => 0,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'actual_start_date' => array(
      'sortable' => 1,
      'default_sort_order' => 'desc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'actual_completion_date' => array(
      'sortable' => 1,
      'default_sort_order' => 'desc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'actual_commitments' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 1,
    ),
    'actual_disbursements' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 1,
    ),
    'ssc_bilateral_commitments' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 1,
    ),
    'ssc_triangular_commitments' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 1,
    ),
  );
  /* No results behavior: Global: Unfiltered text */
  $handler->display->display_options['empty']['area_text_custom']['id'] = 'area_text_custom';
  $handler->display->display_options['empty']['area_text_custom']['table'] = 'views';
  $handler->display->display_options['empty']['area_text_custom']['field'] = 'area_text_custom';
  $handler->display->display_options['empty']['area_text_custom']['empty'] = TRUE;
  $handler->display->display_options['empty']['area_text_custom']['content'] = 'No activities matching search criteria.';
  /* Field: Activity: Title */
  $handler->display->display_options['fields']['title']['id'] = 'title';
  $handler->display->display_options['fields']['title']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['title']['field'] = 'title';
  $handler->display->display_options['fields']['title']['link_to_entity'] = 0;
  /* Field: Activity: Actual Commitments */
  $handler->display->display_options['fields']['actual_commitments']['id'] = 'actual_commitments';
  $handler->display->display_options['fields']['actual_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_commitments']['field'] = 'actual_commitments';
  $handler->display->display_options['fields']['actual_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Actual Disbursements */
  $handler->display->display_options['fields']['actual_disbursements']['id'] = 'actual_disbursements';
  $handler->display->display_options['fields']['actual_disbursements']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_disbursements']['field'] = 'actual_disbursements';
  $handler->display->display_options['fields']['actual_disbursements']['link_to_entity'] = 0;
  /* Sort criterion: Activity: Actual Start Date */
  $handler->display->display_options['sorts']['actual_start_date']['id'] = 'actual_start_date';
  $handler->display->display_options['sorts']['actual_start_date']['table'] = 'efq_activity';
  $handler->display->display_options['sorts']['actual_start_date']['field'] = 'actual_start_date';
  $handler->display->display_options['sorts']['actual_start_date']['order'] = 'DESC';

  /* Display: Search Page */
  $handler = $view->new_display('page', 'Search Page', 'search_page');
  $handler->display->display_options['defaults']['fields'] = FALSE;
  /* Field: Activity: External URL */
  $handler->display->display_options['fields']['external_url']['id'] = 'external_url';
  $handler->display->display_options['fields']['external_url']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['external_url']['field'] = 'external_url';
  $handler->display->display_options['fields']['external_url']['label'] = '';
  $handler->display->display_options['fields']['external_url']['element_label_colon'] = FALSE;
  $handler->display->display_options['fields']['external_url']['link_to_entity'] = 0;
  $handler->display->display_options['fields']['external_url']['display_as_link_text'] = 'details';
  /* Field: Activity: Title */
  $handler->display->display_options['fields']['title']['id'] = 'title';
  $handler->display->display_options['fields']['title']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['title']['field'] = 'title';
  $handler->display->display_options['fields']['title']['link_to_entity'] = 0;
  /* Field: Activity: Beneficiary Agency */
  $handler->display->display_options['fields']['beneficiary_agency']['id'] = 'beneficiary_agency';
  $handler->display->display_options['fields']['beneficiary_agency']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['beneficiary_agency']['field'] = 'beneficiary_agency';
  $handler->display->display_options['fields']['beneficiary_agency']['link_to_entity'] = 0;
  /* Field: Activity: Donor Agency */
  $handler->display->display_options['fields']['donor_agency']['id'] = 'donor_agency';
  $handler->display->display_options['fields']['donor_agency']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['donor_agency']['field'] = 'donor_agency';
  $handler->display->display_options['fields']['donor_agency']['link_to_entity'] = 0;
  /* Field: Activity: Primary Sector */
  $handler->display->display_options['fields']['primary_sector']['id'] = 'primary_sector';
  $handler->display->display_options['fields']['primary_sector']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['primary_sector']['field'] = 'primary_sector';
  $handler->display->display_options['fields']['primary_sector']['link_to_entity'] = 0;
  /* Field: Activity: Location */
  $handler->display->display_options['fields']['location']['id'] = 'location';
  $handler->display->display_options['fields']['location']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['location']['field'] = 'location';
  $handler->display->display_options['fields']['location']['link_to_entity'] = 0;
  /* Field: Activity: Actual Start Date */
  $handler->display->display_options['fields']['actual_start_date']['id'] = 'actual_start_date';
  $handler->display->display_options['fields']['actual_start_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_start_date']['field'] = 'actual_start_date';
  $handler->display->display_options['fields']['actual_start_date']['link_to_entity'] = 0;
  /* Field: Activity: Actual Completion Date */
  $handler->display->display_options['fields']['actual_completion_date']['id'] = 'actual_completion_date';
  $handler->display->display_options['fields']['actual_completion_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_completion_date']['field'] = 'actual_completion_date';
  $handler->display->display_options['fields']['actual_completion_date']['link_to_entity'] = 0;
  /* Field: Activity: Actual Commitments */
  $handler->display->display_options['fields']['actual_commitments']['id'] = 'actual_commitments';
  $handler->display->display_options['fields']['actual_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_commitments']['field'] = 'actual_commitments';
  $handler->display->display_options['fields']['actual_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Actual Disbursements */
  $handler->display->display_options['fields']['actual_disbursements']['id'] = 'actual_disbursements';
  $handler->display->display_options['fields']['actual_disbursements']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_disbursements']['field'] = 'actual_disbursements';
  $handler->display->display_options['fields']['actual_disbursements']['link_to_entity'] = 0;
  /* Field: Activity: Bilateral SSC Commitments */
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['id'] = 'ssc_bilateral_commitments';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['field'] = 'ssc_bilateral_commitments';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Triangular SSC Commitments */
  $handler->display->display_options['fields']['ssc_triangular_commitments']['id'] = 'ssc_triangular_commitments';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['field'] = 'ssc_triangular_commitments';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['link_to_entity'] = 0;
  $handler->display->display_options['defaults']['filter_groups'] = FALSE;
  $handler->display->display_options['defaults']['filters'] = FALSE;
  /* Filter criterion: Activity: Title */
  $handler->display->display_options['filters']['title']['id'] = 'title';
  $handler->display->display_options['filters']['title']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['title']['field'] = 'title';
  $handler->display->display_options['filters']['title']['group'] = 1;
  $handler->display->display_options['filters']['title']['exposed'] = TRUE;
  $handler->display->display_options['filters']['title']['expose']['operator_id'] = 'title_op';
  $handler->display->display_options['filters']['title']['expose']['label'] = 'Search Terms';
  $handler->display->display_options['filters']['title']['expose']['description'] = 'search keywords within activities';
  $handler->display->display_options['filters']['title']['expose']['operator'] = 'title_op';
  $handler->display->display_options['filters']['title']['expose']['identifier'] = 'search';
  /* Filter criterion: Activity: Donor Agency */
  $handler->display->display_options['filters']['donor_agency']['id'] = 'donor_agency';
  $handler->display->display_options['filters']['donor_agency']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['donor_agency']['field'] = 'donor_agency';
  $handler->display->display_options['filters']['donor_agency']['value'] = '';
  $handler->display->display_options['filters']['donor_agency']['group'] = 1;
  $handler->display->display_options['filters']['donor_agency']['exposed'] = TRUE;
  $handler->display->display_options['filters']['donor_agency']['expose']['operator_id'] = 'donor_agency_op';
  $handler->display->display_options['filters']['donor_agency']['expose']['label'] = 'Donor Agency';
  $handler->display->display_options['filters']['donor_agency']['expose']['description'] = 'filter activities by donor agency';
  $handler->display->display_options['filters']['donor_agency']['expose']['operator'] = 'donor_agency_op';
  $handler->display->display_options['filters']['donor_agency']['expose']['identifier'] = 'da';
  $handler->display->display_options['filters']['donor_agency']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Beneficiary Agency */
  $handler->display->display_options['filters']['beneficiary_agency']['id'] = 'beneficiary_agency';
  $handler->display->display_options['filters']['beneficiary_agency']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['beneficiary_agency']['field'] = 'beneficiary_agency';
  $handler->display->display_options['filters']['beneficiary_agency']['value'] = '';
  $handler->display->display_options['filters']['beneficiary_agency']['group'] = 1;
  $handler->display->display_options['filters']['beneficiary_agency']['exposed'] = TRUE;
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['operator_id'] = 'beneficiary_agency_op';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['label'] = 'Beneficiary Agency';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['description'] = 'filter activities by beneficiary agency';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['operator'] = 'beneficiary_agency_op';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['identifier'] = 'ba';
  $handler->display->display_options['filters']['beneficiary_agency']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Primary Sector */
  $handler->display->display_options['filters']['primary_sector']['id'] = 'primary_sector';
  $handler->display->display_options['filters']['primary_sector']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['primary_sector']['field'] = 'primary_sector';
  $handler->display->display_options['filters']['primary_sector']['value'] = '';
  $handler->display->display_options['filters']['primary_sector']['exposed'] = TRUE;
  $handler->display->display_options['filters']['primary_sector']['expose']['operator_id'] = 'primary_sector_op';
  $handler->display->display_options['filters']['primary_sector']['expose']['label'] = 'Primary Sector';
  $handler->display->display_options['filters']['primary_sector']['expose']['description'] = 'filter activities by primary sector';
  $handler->display->display_options['filters']['primary_sector']['expose']['operator'] = 'primary_sector_op';
  $handler->display->display_options['filters']['primary_sector']['expose']['identifier'] = 'psec';
  $handler->display->display_options['filters']['primary_sector']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Location */
  $handler->display->display_options['filters']['location']['id'] = 'location';
  $handler->display->display_options['filters']['location']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['location']['field'] = 'location';
  $handler->display->display_options['filters']['location']['value'] = '';
  $handler->display->display_options['filters']['location']['exposed'] = TRUE;
  $handler->display->display_options['filters']['location']['expose']['operator_id'] = 'location_op';
  $handler->display->display_options['filters']['location']['expose']['label'] = 'Location';
  $handler->display->display_options['filters']['location']['expose']['description'] = 'filter activities by location';
  $handler->display->display_options['filters']['location']['expose']['operator'] = 'location_op';
  $handler->display->display_options['filters']['location']['expose']['identifier'] = 'loc';
  $handler->display->display_options['filters']['location']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Activities Search Type */
  $handler->display->display_options['filters']['activities_search_type']['id'] = 'activities_search_type';
  $handler->display->display_options['filters']['activities_search_type']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['activities_search_type']['field'] = 'activities_search_type';
  $handler->display->display_options['filters']['activities_search_type']['value'] = array(
    'standard' => 'standard',
  );
  $handler->display->display_options['filters']['activities_search_type']['exposed'] = TRUE;
  $handler->display->display_options['filters']['activities_search_type']['expose']['operator_id'] = 'activities_search_type_op';
  $handler->display->display_options['filters']['activities_search_type']['expose']['label'] = 'Activities Search Type';
  $handler->display->display_options['filters']['activities_search_type']['expose']['operator'] = 'activities_search_type_op';
  $handler->display->display_options['filters']['activities_search_type']['expose']['identifier'] = 'stype';
  $handler->display->display_options['filters']['activities_search_type']['expose']['required'] = TRUE;
  $handler->display->display_options['path'] = 'activities';

  /* Display: Export Page */
  $handler = $view->new_display('views_data_export', 'Export Page', 'export_page');
  $handler->display->display_options['pager']['type'] = 'none';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['style_plugin'] = 'views_data_export_csv';
  $handler->display->display_options['style_options']['provide_file'] = 0;
  $handler->display->display_options['style_options']['parent_sort'] = 1;
  $handler->display->display_options['style_options']['quote'] = 1;
  $handler->display->display_options['style_options']['trim'] = 0;
  $handler->display->display_options['style_options']['replace_newlines'] = 0;
  $handler->display->display_options['style_options']['header'] = 1;
  $handler->display->display_options['style_options']['keep_html'] = 0;
  $handler->display->display_options['defaults']['fields'] = FALSE;
  /* Field: Entity: Entity ID */
  $handler->display->display_options['fields']['entity_id']['id'] = 'entity_id';
  $handler->display->display_options['fields']['entity_id']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['entity_id']['field'] = 'entity_id';
  $handler->display->display_options['fields']['entity_id']['label'] = 'ID';
  $handler->display->display_options['fields']['entity_id']['link_to_entity'] = 0;
  /* Field: Activity: Title */
  $handler->display->display_options['fields']['title']['id'] = 'title';
  $handler->display->display_options['fields']['title']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['title']['field'] = 'title';
  $handler->display->display_options['fields']['title']['link_to_entity'] = 0;
  /* Field: Activity: Beneficiary Agency */
  $handler->display->display_options['fields']['beneficiary_agency']['id'] = 'beneficiary_agency';
  $handler->display->display_options['fields']['beneficiary_agency']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['beneficiary_agency']['field'] = 'beneficiary_agency';
  $handler->display->display_options['fields']['beneficiary_agency']['link_to_entity'] = 0;
  /* Field: Activity: Donor Agency */
  $handler->display->display_options['fields']['donor_agency']['id'] = 'donor_agency';
  $handler->display->display_options['fields']['donor_agency']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['donor_agency']['field'] = 'donor_agency';
  $handler->display->display_options['fields']['donor_agency']['link_to_entity'] = 0;
  /* Field: Activity: Primary Sector */
  $handler->display->display_options['fields']['primary_sector']['id'] = 'primary_sector';
  $handler->display->display_options['fields']['primary_sector']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['primary_sector']['field'] = 'primary_sector';
  $handler->display->display_options['fields']['primary_sector']['link_to_entity'] = 0;
  /* Field: Activity: Location */
  $handler->display->display_options['fields']['location']['id'] = 'location';
  $handler->display->display_options['fields']['location']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['location']['field'] = 'location';
  $handler->display->display_options['fields']['location']['link_to_entity'] = 0;
  /* Field: Activity: Actual Start Date */
  $handler->display->display_options['fields']['actual_start_date']['id'] = 'actual_start_date';
  $handler->display->display_options['fields']['actual_start_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_start_date']['field'] = 'actual_start_date';
  $handler->display->display_options['fields']['actual_start_date']['link_to_entity'] = 0;
  /* Field: Activity: Actual Completion Date */
  $handler->display->display_options['fields']['actual_completion_date']['id'] = 'actual_completion_date';
  $handler->display->display_options['fields']['actual_completion_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_completion_date']['field'] = 'actual_completion_date';
  $handler->display->display_options['fields']['actual_completion_date']['link_to_entity'] = 0;
  /* Field: Activity: Actual Commitments */
  $handler->display->display_options['fields']['actual_commitments']['id'] = 'actual_commitments';
  $handler->display->display_options['fields']['actual_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_commitments']['field'] = 'actual_commitments';
  $handler->display->display_options['fields']['actual_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Actual Disbursements */
  $handler->display->display_options['fields']['actual_disbursements']['id'] = 'actual_disbursements';
  $handler->display->display_options['fields']['actual_disbursements']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_disbursements']['field'] = 'actual_disbursements';
  $handler->display->display_options['fields']['actual_disbursements']['link_to_entity'] = 0;
  /* Field: Activity: Bilateral SSC Commitments */
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['id'] = 'ssc_bilateral_commitments';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['field'] = 'ssc_bilateral_commitments';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Triangular SSC Commitments */
  $handler->display->display_options['fields']['ssc_triangular_commitments']['id'] = 'ssc_triangular_commitments';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['field'] = 'ssc_triangular_commitments';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['link_to_entity'] = 0;
  $handler->display->display_options['defaults']['filter_groups'] = FALSE;
  $handler->display->display_options['defaults']['filters'] = FALSE;
  /* Filter criterion: Activity: Title */
  $handler->display->display_options['filters']['title']['id'] = 'title';
  $handler->display->display_options['filters']['title']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['title']['field'] = 'title';
  $handler->display->display_options['filters']['title']['group'] = 1;
  $handler->display->display_options['filters']['title']['exposed'] = TRUE;
  $handler->display->display_options['filters']['title']['expose']['operator_id'] = 'title_op';
  $handler->display->display_options['filters']['title']['expose']['label'] = 'Search Terms';
  $handler->display->display_options['filters']['title']['expose']['description'] = 'search keywords within activities';
  $handler->display->display_options['filters']['title']['expose']['operator'] = 'title_op';
  $handler->display->display_options['filters']['title']['expose']['identifier'] = 'search';
  /* Filter criterion: Activity: Donor Agency */
  $handler->display->display_options['filters']['donor_agency']['id'] = 'donor_agency';
  $handler->display->display_options['filters']['donor_agency']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['donor_agency']['field'] = 'donor_agency';
  $handler->display->display_options['filters']['donor_agency']['value'] = '';
  $handler->display->display_options['filters']['donor_agency']['group'] = 1;
  $handler->display->display_options['filters']['donor_agency']['exposed'] = TRUE;
  $handler->display->display_options['filters']['donor_agency']['expose']['operator_id'] = 'donor_agency_op';
  $handler->display->display_options['filters']['donor_agency']['expose']['label'] = 'Donor Agency';
  $handler->display->display_options['filters']['donor_agency']['expose']['description'] = 'filter activities by donor agency';
  $handler->display->display_options['filters']['donor_agency']['expose']['operator'] = 'donor_agency_op';
  $handler->display->display_options['filters']['donor_agency']['expose']['identifier'] = 'da';
  $handler->display->display_options['filters']['donor_agency']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Beneficiary Agency */
  $handler->display->display_options['filters']['beneficiary_agency']['id'] = 'beneficiary_agency';
  $handler->display->display_options['filters']['beneficiary_agency']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['beneficiary_agency']['field'] = 'beneficiary_agency';
  $handler->display->display_options['filters']['beneficiary_agency']['value'] = '';
  $handler->display->display_options['filters']['beneficiary_agency']['group'] = 1;
  $handler->display->display_options['filters']['beneficiary_agency']['exposed'] = TRUE;
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['operator_id'] = 'beneficiary_agency_op';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['label'] = 'Beneficiary Agency';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['description'] = 'filter activities by beneficiary agency';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['operator'] = 'beneficiary_agency_op';
  $handler->display->display_options['filters']['beneficiary_agency']['expose']['identifier'] = 'ba';
  $handler->display->display_options['filters']['beneficiary_agency']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Primary Sector */
  $handler->display->display_options['filters']['primary_sector']['id'] = 'primary_sector';
  $handler->display->display_options['filters']['primary_sector']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['primary_sector']['field'] = 'primary_sector';
  $handler->display->display_options['filters']['primary_sector']['value'] = '';
  $handler->display->display_options['filters']['primary_sector']['exposed'] = TRUE;
  $handler->display->display_options['filters']['primary_sector']['expose']['operator_id'] = 'primary_sector_op';
  $handler->display->display_options['filters']['primary_sector']['expose']['label'] = 'Primary Sector';
  $handler->display->display_options['filters']['primary_sector']['expose']['description'] = 'filter activities by primary sector';
  $handler->display->display_options['filters']['primary_sector']['expose']['operator'] = 'primary_sector_op';
  $handler->display->display_options['filters']['primary_sector']['expose']['identifier'] = 'psec';
  $handler->display->display_options['filters']['primary_sector']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Location */
  $handler->display->display_options['filters']['location']['id'] = 'location';
  $handler->display->display_options['filters']['location']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['location']['field'] = 'location';
  $handler->display->display_options['filters']['location']['value'] = '';
  $handler->display->display_options['filters']['location']['exposed'] = TRUE;
  $handler->display->display_options['filters']['location']['expose']['operator_id'] = 'location_op';
  $handler->display->display_options['filters']['location']['expose']['label'] = 'Location';
  $handler->display->display_options['filters']['location']['expose']['description'] = 'filter activities by location';
  $handler->display->display_options['filters']['location']['expose']['operator'] = 'location_op';
  $handler->display->display_options['filters']['location']['expose']['identifier'] = 'loc';
  $handler->display->display_options['filters']['location']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Activities Search Type */
  $handler->display->display_options['filters']['activities_search_type']['id'] = 'activities_search_type';
  $handler->display->display_options['filters']['activities_search_type']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['activities_search_type']['field'] = 'activities_search_type';
  $handler->display->display_options['filters']['activities_search_type']['value'] = array(
    'standard' => 'standard',
  );
  $handler->display->display_options['filters']['activities_search_type']['exposed'] = TRUE;
  $handler->display->display_options['filters']['activities_search_type']['expose']['operator_id'] = 'activities_search_type_op';
  $handler->display->display_options['filters']['activities_search_type']['expose']['label'] = 'Activities Search Type';
  $handler->display->display_options['filters']['activities_search_type']['expose']['operator'] = 'activities_search_type_op';
  $handler->display->display_options['filters']['activities_search_type']['expose']['identifier'] = 'stype';
  $handler->display->display_options['path'] = 'activities/export/csv';
  $handler->display->display_options['displays'] = array(
    'search_page' => 'search_page',
    'default' => 0,
  );
  $handler->display->display_options['segment_size'] = '100';
  $translatables['activities'] = array(
    t('Master'),
    t('Activities'),
    t('more'),
    t('Search Activities'),
    t('Reset'),
    t('Sort by'),
    t('Asc'),
    t('Desc'),
    t('Items per page'),
    t('- All -'),
    t('Offset'),
    t('« first'),
    t('‹ previous'),
    t('next ›'),
    t('last »'),
    t('No activities matching search criteria.'),
    t('Title'),
    t('Actual Commitments'),
    t('Actual Disbursements'),
    t('Search Page'),
    t('Beneficiary Agency'),
    t('Donor Agency'),
    t('Primary Sector'),
    t('Location'),
    t('Actual Start Date'),
    t('Actual Completion Date'),
    t('Bilateral SSC Commitments'),
    t('Triangular SSC Commitments'),
    t('Search Terms'),
    t('search keywords within activities'),
    t('filter activities by donor agency'),
    t('filter activities by beneficiary agency'),
    t('filter activities by primary sector'),
    t('filter activities by location'),
    t('Activities Search Type'),
    t('Export Page'),
    t('ID'),
  );
  $export['activities'] = $view;

  return $export;
}
