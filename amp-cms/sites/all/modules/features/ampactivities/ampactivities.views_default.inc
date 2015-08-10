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
    'primary_program' => 'primary_program',
    'beneficiary_agency' => 'beneficiary_agency',
    'donor_agency' => 'donor_agency',
    'organization_roles' => 'organization_roles',
    'primary_sector' => 'primary_sector',
    'secondary_sector' => 'secondary_sector',
    'location' => 'location',
    'actual_approval_date' => 'actual_approval_date',
    'actual_start_date' => 'actual_start_date',
    'actual_completion_date' => 'actual_completion_date',
    'proposed_start_date' => 'proposed_start_date',
    'proposed_completion_date' => 'proposed_completion_date',
    'original_completion_date' => 'original_completion_date',
    'status' => 'status',
    'planned_commitments' => 'planned_commitments',
    'planned_disbursements' => 'planned_disbursements',
    'planned_expenditures' => 'planned_expenditures',
    'actual_commitments' => 'actual_commitments',
    'actual_disbursements' => 'actual_disbursements',
    'actual_expenditures' => 'actual_expenditures',
    'ssc_bilateral_commitments' => 'ssc_bilateral_commitments',
    'ssc_bilateral_disbursements' => 'ssc_bilateral_disbursements',
    'ssc_triangular_commitments' => 'ssc_triangular_commitments',
    'ssc_triangular_disbursements' => 'ssc_triangular_disbursements',
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
    'primary_program' => array(
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
    'organization_roles' => array(
      'sortable' => 0,
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
    'secondary_sector' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'location' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'actual_approval_date' => array(
      'sortable' => 1,
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
    'proposed_start_date' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'proposed_completion_date' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'original_completion_date' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'status' => array(
      'sortable' => 0,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'planned_commitments' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'planned_disbursements' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'planned_expenditures' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'actual_commitments' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 0,
    ),
    'actual_disbursements' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 0,
    ),
    'actual_expenditures' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'ssc_bilateral_commitments' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 0,
    ),
    'ssc_bilateral_disbursements' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
    'ssc_triangular_commitments' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => 'views-align-right',
      'separator' => '',
      'empty_column' => 0,
    ),
    'ssc_triangular_disbursements' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
      'empty_column' => 0,
    ),
  );
  /* No results behavior: Global: Unfiltered text */
  $handler->display->display_options['empty']['area_text_custom']['id'] = 'area_text_custom';
  $handler->display->display_options['empty']['area_text_custom']['table'] = 'views';
  $handler->display->display_options['empty']['area_text_custom']['field'] = 'area_text_custom';
  $handler->display->display_options['empty']['area_text_custom']['empty'] = TRUE;
  $handler->display->display_options['empty']['area_text_custom']['content'] = 'No activities matching search criteria.';
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
  /* Field: Activity: Primary Program */
  $handler->display->display_options['fields']['primary_program']['id'] = 'primary_program';
  $handler->display->display_options['fields']['primary_program']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['primary_program']['field'] = 'primary_program';
  $handler->display->display_options['fields']['primary_program']['link_to_entity'] = 0;
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
  /* Field: Activity: Organization Roles */
  $handler->display->display_options['fields']['organization_roles']['id'] = 'organization_roles';
  $handler->display->display_options['fields']['organization_roles']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['organization_roles']['field'] = 'organization_roles';
  $handler->display->display_options['fields']['organization_roles']['link_to_entity'] = 0;
  /* Field: Activity: Primary Sector */
  $handler->display->display_options['fields']['primary_sector']['id'] = 'primary_sector';
  $handler->display->display_options['fields']['primary_sector']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['primary_sector']['field'] = 'primary_sector';
  $handler->display->display_options['fields']['primary_sector']['link_to_entity'] = 0;
  /* Field: Activity: Secondary Sector */
  $handler->display->display_options['fields']['secondary_sector']['id'] = 'secondary_sector';
  $handler->display->display_options['fields']['secondary_sector']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['secondary_sector']['field'] = 'secondary_sector';
  $handler->display->display_options['fields']['secondary_sector']['link_to_entity'] = 0;
  /* Field: Activity: Location */
  $handler->display->display_options['fields']['location']['id'] = 'location';
  $handler->display->display_options['fields']['location']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['location']['field'] = 'location';
  $handler->display->display_options['fields']['location']['link_to_entity'] = 0;
  /* Field: Activity: Actual Approval Date */
  $handler->display->display_options['fields']['actual_approval_date']['id'] = 'actual_approval_date';
  $handler->display->display_options['fields']['actual_approval_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_approval_date']['field'] = 'actual_approval_date';
  $handler->display->display_options['fields']['actual_approval_date']['link_to_entity'] = 0;
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
  /* Field: Activity: Proposed Start Date */
  $handler->display->display_options['fields']['proposed_start_date']['id'] = 'proposed_start_date';
  $handler->display->display_options['fields']['proposed_start_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['proposed_start_date']['field'] = 'proposed_start_date';
  $handler->display->display_options['fields']['proposed_start_date']['link_to_entity'] = 0;
  /* Field: Activity: Proposed Completion Date */
  $handler->display->display_options['fields']['proposed_completion_date']['id'] = 'proposed_completion_date';
  $handler->display->display_options['fields']['proposed_completion_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['proposed_completion_date']['field'] = 'proposed_completion_date';
  $handler->display->display_options['fields']['proposed_completion_date']['link_to_entity'] = 0;
  /* Field: Activity: Original Completion Date */
  $handler->display->display_options['fields']['original_completion_date']['id'] = 'original_completion_date';
  $handler->display->display_options['fields']['original_completion_date']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['original_completion_date']['field'] = 'original_completion_date';
  $handler->display->display_options['fields']['original_completion_date']['link_to_entity'] = 0;
  /* Field: Activity: Status */
  $handler->display->display_options['fields']['status']['id'] = 'status';
  $handler->display->display_options['fields']['status']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['status']['field'] = 'status';
  $handler->display->display_options['fields']['status']['link_to_entity'] = 0;
  /* Field: Activity: Planned Commitments */
  $handler->display->display_options['fields']['planned_commitments']['id'] = 'planned_commitments';
  $handler->display->display_options['fields']['planned_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['planned_commitments']['field'] = 'planned_commitments';
  $handler->display->display_options['fields']['planned_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Planned Disbursements */
  $handler->display->display_options['fields']['planned_disbursements']['id'] = 'planned_disbursements';
  $handler->display->display_options['fields']['planned_disbursements']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['planned_disbursements']['field'] = 'planned_disbursements';
  $handler->display->display_options['fields']['planned_disbursements']['link_to_entity'] = 0;
  /* Field: Activity: Planned Expenditures */
  $handler->display->display_options['fields']['planned_expenditures']['id'] = 'planned_expenditures';
  $handler->display->display_options['fields']['planned_expenditures']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['planned_expenditures']['field'] = 'planned_expenditures';
  $handler->display->display_options['fields']['planned_expenditures']['link_to_entity'] = 0;
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
  /* Field: Activity: Actual Expenditures */
  $handler->display->display_options['fields']['actual_expenditures']['id'] = 'actual_expenditures';
  $handler->display->display_options['fields']['actual_expenditures']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['actual_expenditures']['field'] = 'actual_expenditures';
  $handler->display->display_options['fields']['actual_expenditures']['link_to_entity'] = 0;
  /* Field: Activity: Bilateral SSC Commitments */
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['id'] = 'ssc_bilateral_commitments';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['field'] = 'ssc_bilateral_commitments';
  $handler->display->display_options['fields']['ssc_bilateral_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Bilateral SSC Disbursements */
  $handler->display->display_options['fields']['ssc_bilateral_disbursements']['id'] = 'ssc_bilateral_disbursements';
  $handler->display->display_options['fields']['ssc_bilateral_disbursements']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_bilateral_disbursements']['field'] = 'ssc_bilateral_disbursements';
  $handler->display->display_options['fields']['ssc_bilateral_disbursements']['link_to_entity'] = 0;
  /* Field: Activity: Triangular SSC Commitments */
  $handler->display->display_options['fields']['ssc_triangular_commitments']['id'] = 'ssc_triangular_commitments';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['field'] = 'ssc_triangular_commitments';
  $handler->display->display_options['fields']['ssc_triangular_commitments']['link_to_entity'] = 0;
  /* Field: Activity: Triangular SSC Disbursements */
  $handler->display->display_options['fields']['ssc_triangular_disbursements']['id'] = 'ssc_triangular_disbursements';
  $handler->display->display_options['fields']['ssc_triangular_disbursements']['table'] = 'efq_activity';
  $handler->display->display_options['fields']['ssc_triangular_disbursements']['field'] = 'ssc_triangular_disbursements';
  $handler->display->display_options['fields']['ssc_triangular_disbursements']['link_to_entity'] = 0;
  /* Sort criterion: Activity: Title */
  $handler->display->display_options['sorts']['title']['id'] = 'title';
  $handler->display->display_options['sorts']['title']['table'] = 'efq_activity';
  $handler->display->display_options['sorts']['title']['field'] = 'title';
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
  $handler->display->display_options['filters']['primary_sector']['group'] = 1;
  $handler->display->display_options['filters']['primary_sector']['exposed'] = TRUE;
  $handler->display->display_options['filters']['primary_sector']['expose']['operator_id'] = 'primary_sector_op';
  $handler->display->display_options['filters']['primary_sector']['expose']['label'] = 'Primary Sector';
  $handler->display->display_options['filters']['primary_sector']['expose']['description'] = 'filter activities by primary sector';
  $handler->display->display_options['filters']['primary_sector']['expose']['operator'] = 'primary_sector_op';
  $handler->display->display_options['filters']['primary_sector']['expose']['identifier'] = 'psec';
  $handler->display->display_options['filters']['primary_sector']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Secondary Sector */
  $handler->display->display_options['filters']['secondary_sector']['id'] = 'secondary_sector';
  $handler->display->display_options['filters']['secondary_sector']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['secondary_sector']['field'] = 'secondary_sector';
  $handler->display->display_options['filters']['secondary_sector']['value'] = '';
  $handler->display->display_options['filters']['secondary_sector']['group'] = 1;
  $handler->display->display_options['filters']['secondary_sector']['exposed'] = TRUE;
  $handler->display->display_options['filters']['secondary_sector']['expose']['operator_id'] = 'secondary_sector_op';
  $handler->display->display_options['filters']['secondary_sector']['expose']['label'] = 'Secondary Sector';
  $handler->display->display_options['filters']['secondary_sector']['expose']['description'] = 'filter activities by secondary sector';
  $handler->display->display_options['filters']['secondary_sector']['expose']['operator'] = 'secondary_sector_op';
  $handler->display->display_options['filters']['secondary_sector']['expose']['identifier'] = 'secondary_sector';
  $handler->display->display_options['filters']['secondary_sector']['type'] = 'tokeninput';
  /* Filter criterion: Activity: National Plan Objective */
  $handler->display->display_options['filters']['national_plan_objective']['id'] = 'national_plan_objective';
  $handler->display->display_options['filters']['national_plan_objective']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['national_plan_objective']['field'] = 'national_plan_objective';
  $handler->display->display_options['filters']['national_plan_objective']['value'] = '';
  $handler->display->display_options['filters']['national_plan_objective']['group'] = 1;
  $handler->display->display_options['filters']['national_plan_objective']['exposed'] = TRUE;
  $handler->display->display_options['filters']['national_plan_objective']['expose']['operator_id'] = 'national_plan_objective_op';
  $handler->display->display_options['filters']['national_plan_objective']['expose']['label'] = 'National Plan Objective';
  $handler->display->display_options['filters']['national_plan_objective']['expose']['description'] = 'filter activities by national plan objective';
  $handler->display->display_options['filters']['national_plan_objective']['expose']['operator'] = 'national_plan_objective_op';
  $handler->display->display_options['filters']['national_plan_objective']['expose']['identifier'] = 'npo';
  $handler->display->display_options['filters']['national_plan_objective']['type'] = 'tokeninput';
  /* Filter criterion: Activity: Location */
  $handler->display->display_options['filters']['location']['id'] = 'location';
  $handler->display->display_options['filters']['location']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['location']['field'] = 'location';
  $handler->display->display_options['filters']['location']['value'] = '';
  $handler->display->display_options['filters']['location']['group'] = 1;
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
  $handler->display->display_options['filters']['activities_search_type']['group'] = 1;
  $handler->display->display_options['filters']['activities_search_type']['exposed'] = TRUE;
  $handler->display->display_options['filters']['activities_search_type']['expose']['operator_id'] = 'activities_search_type_op';
  $handler->display->display_options['filters']['activities_search_type']['expose']['label'] = 'Activities Search Type';
  $handler->display->display_options['filters']['activities_search_type']['expose']['operator'] = 'activities_search_type_op';
  $handler->display->display_options['filters']['activities_search_type']['expose']['identifier'] = 'stype';
  $handler->display->display_options['filters']['activities_search_type']['expose']['required'] = TRUE;
  /* Filter criterion: Activity: Humanitarian Aid */
  $handler->display->display_options['filters']['humanitarian_aid']['id'] = 'humanitarian_aid';
  $handler->display->display_options['filters']['humanitarian_aid']['table'] = 'efq_activity';
  $handler->display->display_options['filters']['humanitarian_aid']['field'] = 'humanitarian_aid';
  $handler->display->display_options['filters']['humanitarian_aid']['value'] = '0';
  $handler->display->display_options['filters']['humanitarian_aid']['group'] = 1;
  $handler->display->display_options['filters']['humanitarian_aid']['exposed'] = TRUE;
  $handler->display->display_options['filters']['humanitarian_aid']['expose']['operator_id'] = '';
  $handler->display->display_options['filters']['humanitarian_aid']['expose']['label'] = 'Humanitarian Aid';
  $handler->display->display_options['filters']['humanitarian_aid']['expose']['operator'] = 'humanitarian_aid_op';
  $handler->display->display_options['filters']['humanitarian_aid']['expose']['identifier'] = 'aid';
  $handler->display->display_options['filters']['humanitarian_aid']['expose']['required'] = TRUE;

  /* Display: Search Page */
  $handler = $view->new_display('page', 'Search Page', 'search_page');
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
    t('Primary Program'),
    t('Beneficiary Agency'),
    t('Donor Agency'),
    t('Organization Roles'),
    t('Primary Sector'),
    t('Secondary Sector'),
    t('Location'),
    t('Actual Approval Date'),
    t('Actual Start Date'),
    t('Actual Completion Date'),
    t('Proposed Start Date'),
    t('Proposed Completion Date'),
    t('Original Completion Date'),
    t('Status'),
    t('Planned Commitments'),
    t('Planned Disbursements'),
    t('Planned Expenditures'),
    t('Actual Commitments'),
    t('Actual Disbursements'),
    t('Actual Expenditures'),
    t('Bilateral SSC Commitments'),
    t('Bilateral SSC Disbursements'),
    t('Triangular SSC Commitments'),
    t('Triangular SSC Disbursements'),
    t('Search Terms'),
    t('search keywords within activities'),
    t('filter activities by donor agency'),
    t('filter activities by beneficiary agency'),
    t('filter activities by primary sector'),
    t('filter activities by secondary sector'),
    t('National Plan Objective'),
    t('filter activities by national plan objective'),
    t('filter activities by location'),
    t('Activities Search Type'),
    t('Humanitarian Aid'),
    t('Search Page'),
    t('Export Page'),
  );
  $export['activities'] = $view;

  return $export;
}
