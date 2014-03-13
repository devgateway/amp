<?php
/**
 * @file
 * Define views stuff.
 */

/**
 * Implements hook_views_data().
 */
function ampp_projects_views_data() {
  // TABLE: cached_amp_activity
  $data['cached_amp_activity']['table']['group'] = t('AMP Activity');
  $data['cached_amp_activity']['table']['base'] = array(
    'field' => 'amp_activity_id',
    'title' => t('Cached AMP Activity'),
    'help' => t('Contains information related to AMP Activities (projects).'),
    'database' => 'amp_projects'
  );

  $data['cached_amp_activity']['amp_activity_id'] = array(
    'title' => t('ID'),
    'help' => t('The Activity ID '),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array('handler' => 'ampp_projects_handler_activity_id'),
    'filter' => array('handler' => 'views_handler_filter_amp_activity_id'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  $data['cached_amp_activity']['name'] = array(
    'title' => t('Project title'),
    'help' => t('Title for the project'),
    'argument' => array('handler' => 'amp_projects_handler_argument_string'),
    'field' => array(
      'handler' => 'ampp_projects_handler_activity_name',
      'click sortable' => TRUE
    ),
    'filter' => array('handler' => 'views_handler_filter_string'),
    'sort' => array('handler' => 'views_handler_sort_string')
  );

  $data['cached_amp_activity']['actual_start_date'] = array(
    'title' => t('Start date'),
    'help' => t('Date of the start of project'),
    'argument' => array('handler' => 'ampp_projects_date_views_argument_handler'),
    'field' => array(
      'handler' => 'ampp_projects_handler_postgre_timestamp',
      'click sortable' => TRUE
    ),
    'filter' => array('handler' => 'views_handler_filter_date'),
    'sort' => array('handler' => 'views_handler_sort_date')
  );

  $data['cached_amp_activity']['actual_completion_date'] = array(
    'title' => t('End date'),
    'help' => t('Date of the actual completion'),
    'argument' => array('handler' => 'ampp_projects_date_views_argument_handler'),
    'field' => array(
      'handler' => 'ampp_projects_handler_postgre_timestamp',
      'click sortable' => TRUE
    ),
    'filter' => array('handler' => 'views_handler_filter_date'),
    'sort' => array('handler' => 'views_handler_sort_date')
  );

  $data['cached_amp_activity']['draft'] = array(
    'title' => t('Draft project'),
    'help' => t('Is it draft project'),
    'filter' => array(
      'handler' => 'views_handler_filter_boolean_operator',
      'use equal' => FALSE,
    ),
  );

  $data['cached_amp_activity']['approval_status'] = array(
    'title' => t('Approval Status'),
    'help' => t('Activity approval status'),
    'filter' => array('handler' => 'views_handler_filter_approval_status_in_operator')
  );

  $data['cached_amp_activity']['amp_team_id'] = array(
    'title' => t('Amp team id Filter'),
    'help' => t('Amp team id Filter'),
    'filter' => array('handler' => 'views_handler_filter_amp_team_id')
  );

  // Commitment Amount
  $data['cached_amp_activity']['commitment_amount'] = array(
    'title' => t('Commitment amount'),
    'help' => t('Transaction Commitment amount'),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array(
      'handler' => 'views_handler_field_amount_commitment',
      'float' => TRUE,
    ),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  // Disbursement amount
  $data['cached_amp_activity']['disbursement_amount'] = array(
    'title' => t('Disbursement amount'),
    'help' => t('Transaction Disbursement amount'),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array(
      'handler' => 'views_handler_field_amount_disbursement',
      'float' => TRUE,
    ),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  // TABLE: cached_v_status
  $data['cached_v_status']['table']['group'] = t('AMP Projects');
  $data['cached_v_status']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id'
    )
  );

  $data['cached_v_status']['name'] = array(
    'group' => t('AMP Projects'),
    'title' => t('Status'),
    'help' => t('Status name of AMP projects'),
    'field' => array(
      'handler' => 'views_handler_field_status',
      'click sortable' => TRUE
    ),
    'sort' => array('handler' => 'views_handler_sort'),
  );

  // TABLE: amp_activity_program
  $data['amp_activity_program']['table']['group'] = t('AMP Projects');
  $data['amp_activity_program']['table']['join'] = array(
    // Directly links to amp_activity table.
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
    ),
  );

  $data['amp_activity_program']['amp_program_id_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('Program Ids'),
    'help' => t('Program Ids of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_program_ids'),
  );

  // TABLE: amp_activity_sector
  $data['amp_activity_sector']['table']['group'] = t('AMP Projects');
  $data['amp_activity_sector']['table']['join'] = array(
    // Directly links to amp_activity table.
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
    ),
  );

  $data['amp_activity_sector']['amp_sector_id_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('Sector Ids'),
    'help' => t('Sectors Ids of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_sector_ids'),
  );

  // TABLE: amp_sector
  $data['amp_sector']['table']['group'] = t('AMP Projects');
  $data['amp_sector']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_table' => 'amp_activity_sector',
      'left_field' => 'amp_sector_id',
      'field' => 'amp_sector_id'
    )
  );

  $data['amp_sector']['amp_sector_name_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('Sector Names'),
    'help' => t('Sectors names of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_sector_names'),
  );

  $data['amp_sector']['amp_sector_id'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Sector Id'),
    'help' => t('Sectors id of AMP project'),
    'argument' => array('handler' => 'views_handler_argument_sector_id'),
    'field' => array('handler' => 'views_handler_field'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  // TABLE: amp_primarysector
  $data['amp_primarysector']['table']['join'] = array(
    'cached_amp_activity' => array(
      'table' => 'amp_sector',
      'left_table' => 'amp_activity_sector',
      'left_field' => 'amp_sector_id',
      'field' => 'amp_sector_id',
      'extra' => 'amp_activity_sector.classification_config_id=1'
    )
  );

  $data['amp_primarysector']['amp_primarysector_name_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('Primary Sector Names'),
    'help' => t('Primary Sectors names of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_primarysector_names'),
  );

  // TABLE: amp_secondarysector
  $data['amp_secondarysector']['table']['join'] = array(
    'cached_amp_activity' => array(
      'table' => 'amp_sector',
      'left_table' => 'amp_activity_sector',
      'left_field' => 'amp_sector_id',
      'field' => 'amp_sector_id',
      'extra' => 'amp_activity_sector.classification_config_id=2'
    )
  );

  $data['amp_secondarysector']['amp_secondarysector_name_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('Secondary Sector Names'),
    'help' => t('Secondary Sectors names of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_secondarysector_names'),
  );

  // TABLE: amp_theme
  $data['amp_theme']['table']['group'] = t('AMP Projects');
  $data['amp_theme']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_table' => 'amp_activity_program',
      'left_field' => 'amp_program_id',
      'field' => 'amp_theme_id'
    )
  );

  $data['amp_theme']['amp_theme_name_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('Program Names'),
    'help' => t('Program names of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_program_names'),
  );

  $data['amp_theme']['amp_theme_id'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Program Id'),
    'help' => t('Program id of AMP project'),
    'argument' => array('handler' => 'views_handler_argument_program_id'),
    'field' => array('handler' => 'views_handler_field'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  // TABLE: amp_activity_location
  $data['amp_activity_location']['table']['group'] = t('AMP Projects');
  $data['amp_activity_location']['table']['join'] = array(
    // Directly links to amp_activity table.
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
    ),
  );
  $data['amp_activity_location']['amp_location_id_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Location Ids'),
    'help' => t('Location Ids of AMP projects'),
    'field' => array(
      'handler' => 'ampp_projects_handler_location_ids',
    ),
  );

  // TABLE: amp_location
  $data['amp_location']['table']['group'] = t('AMP Projects');
  $data['amp_location']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_table' => 'amp_activity_location',
      'left_field' => 'amp_location_id',
      'field' => 'amp_location_id'
    )
  );

  $data['amp_location']['amp_location_region_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Locations Names'),
    'help' => t('Locations name of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_location_names'),
  );

  $data['amp_location']['amp_location_id'] = array(
    'title' => t('Location Id'),
    'help' => t('Id of the location'),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array('handler' => 'views_handler_field'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  $data['amp_location']['location_id'] = array(
    'title' => t('Locations Ids'),
    'help' => t('List of locations'),
    'argument' => array('handler' => 'views_handler_argument_location_id'),
    'field' => array('handler' => 'views_handler_field'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  // TABLE: amp_category_value_location
  $data['amp_category_value_location']['table']['group'] = t('AMP Projects');
  $data['amp_category_value_location']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_table' => 'amp_location',
      'left_field' => 'location_id',
      'field' => 'id'
    ),
  );

  $data['amp_category_value_location']['id'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Category value locations id'),
    'help' => t('AMP Category value locations id'),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array('handler' => 'views_handler_field'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  $data['amp_category_value_location']['category_location_name_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Category Locations Names'),
    'help' => t('AMP Projects category Locations  names'),
    'field' => array('handler' => 'ampp_projects_handler_category_location_names'),
  );

  // TABLE: amp_funding
  $data['amp_funding']['table']['group'] = t('AMP Projects');
  $data['amp_funding']['table']['join'] = array(
    // Directly links to amp_activity table.
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',

    ),
  );
  $data['amp_funding']['amp_org_id_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Organizations Ids'),
    'help' => t('Organizations Ids of AMP projects'),
    'field' => array(
      'handler' => 'ampp_projects_handler_org_ids',
    ),
  );

  // TABLE: amp_organisation
  $data['amp_organisation']['table']['group'] = t('AMP Projects');
  $data['amp_organisation']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_table' => 'amp_funding',
      'left_field' => 'amp_donor_org_id',
      'field' => 'amp_org_id'
    )
  );

  $data['amp_organisation']['amp_org_name_agg'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Organizations Names'),
    'help' => t('Locations name of AMP projects'),
    'field' => array('handler' => 'ampp_projects_handler_org_names'),
  );

  $data['amp_organisation']['amp_org_id'] = array(
    'title' => t('Organization Id'),
    'help' => t('Id of the Organization'),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array('handler' => 'views_handler_field'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  // TABLE: amp_activities_categoryvalues
  $data['amp_activities_categoryvalues']['table']['group'] = t('AMP Projects');
  $data['amp_activities_categoryvalues']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
      'type' => 'INNER',
    )
  );

  $data['amp_activities_categoryvalues']['amp_categoryvalue_id'] = array(
    'group' => t('AMP Projects'),
    'title' => t('AMP Projects Budget'),
    'help' => t('Budget type'),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array('handler' => 'views_handler_field'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  // TABLE: amp_org_role
  $data['amp_org_role']['table']['group'] = t('Organization Role');
  $data['amp_org_role']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'activity',
    )
  );

  $data['amp_org_role']['role'] = array(
    'title' => t('Role'),
    'help' => t('Get information related to the organization roles.'),
    'relationship' => array(
      'handler' => 'views_handler_relationship',
      'base' => 'amp_role',
      'field' => 'amp_role_id',
      'label' => t('role'),
    ),
  );

  $data['amp_org_role']['organisation_role'] = array(
    'title' => t('Organisation having Role'),
    'help' => t('Filter by Organizations that have a specific role'),
    'real field' => 'organisation',
    'argument' => array(
      'handler' => 'ampp_projects_views_handler_argument_organisation_role',
      'options callback' => '_ampp_projects_get_amp_roles',
      'options arguments' => array('role_code'),
    ),
    'numeric' => TRUE,
    'filter' => array(
      'handler' => 'views_handler_filter_numeric',
    ),
  );

  // TABLE: amp_role
  $data['amp_role']['table']['group'] = t('Organization Role');
  $data['amp_role']['table']['base'] = array(
    'field' => 'amp_role_id',
    'title' => t('AMP Role'),
    'help' => t('Contains information related to AMP Roles.'),
    'database' => 'amp_projects'
  );

  $data['amp_role']['amp_role_id'] = array(
    'title' => t('Role ID'),
    'help' => t('The node ID.'),
    'argument' => array('handler' => 'views_handler_argument_numeric'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
    'sort' => array('handler' => 'views_handler_sort_numeric')
  );

  $data['amp_role']['name'] = array(
    'title' => t('Name'),
    'help' => t('The name of the role.'),
    'argument' => array('handler' => 'views_handler_argument_string'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array('handler' => 'views_handler_filter_string'),
    'sort' => array('handler' => 'views_handler_sort')
  );

  $data['amp_role']['role_code'] = array(
    'title' => t('Role Code'),
    'help' => t('The code of the role.'),
    'argument' => array('handler' => 'views_handler_argument_string'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_in_operator',
      'options callback' => '_ampp_projects_get_amp_roles',
      'options arguments' => array('role_code'),
    ),
    'sort' => array('handler' => 'views_handler_sort')
  );

  return $data;
}

/**
 * Implements hook_views_pre_build().
 */
function ampp_projects_views_pre_build(&$view) {
  if (($view->name == 'top_activities_by_disbursment') && ($view->current_display == 'default')) {
    if (isset($_SESSION['top_activities_by_disbursement_conf'])) {
      $conf = unserialize($_SESSION['top_activities_by_disbursement_conf']);

      $itemscount = $conf['rows_to_show'];
      $view->display_handler->options['items_per_page'] = $itemscount;
      $view->display_handler->options['pager']['options']['items_per_page'] = $itemscount;

      // configuring sorting and order
      if (isset($conf['default_sort']) && !empty($conf['default_sort'])) {
        foreach ($view->sort as $k => $v) {
          if ($k != $conf['default_sort']) {
            unset($view->sort[$k]);
          }
        }

        // configuring order and order
        if (isset($conf['default_order']) && !empty($conf['default_order'])) {
          $view->sort[$conf['default_sort']]->options['order'] = $conf['default_order'];
        }
      }
    }
  }
}

/**
 * Implements hook_views_pre_execute().
 */
function ampp_projects_views_pre_execute(&$view) {
  global $language;

  if ($view->name == 'projects_search_result' && !isset($view->get_total_amount_type)) {
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpActivityVersion', 'name', cached_amp_activity.amp_activity_id, 'amp_activity_version', 'name', 'amp_activity_id', '" . $language->language . "')";
    $view->build_info['query']->groupBy('cached_amp_activity.amp_activity_id')
      ->groupBy($translate_field)
      ->groupBy('cached_amp_activity.actual_start_date')
      ->groupBy('cached_amp_activity.actual_completion_date')
      ->groupBy('cached_v_status.name');
    $view->build_info['count_query']->groupBy('cached_amp_activity.amp_activity_id')
      ->groupBy('cached_amp_activity.name')
      ->groupBy('cached_amp_activity.actual_start_date')
      ->groupBy('cached_amp_activity.actual_completion_date');
  }
  else {
    if ($view->name == 'projects_search_result' && isset($view->get_total_amount_type)) {
      $view->build_info['query']->groupBy('cached_amp_activity.amp_activity_id')
        ->groupBy('(fd.transaction_amount / getexchange(cur.currency_code, fd.transaction_date ))');
    }
  }

  if ($view->name == 'projects_search_result' && $view->current_display == 'feed_1') {
    unset($view->field['amp_activity_id']);

    // optionally show the Status column
    if (!isset($_GET['show_status_column']) || $_GET['show_status_column'] == 0) {
      unset($view->field['name_1']);
    }
  }
}

/**
 * Implements hook_views_query_alter().
 */
function ampp_projects_views_query_alter(&$view, &$query) {
  global $language;

  if ($view->name == 'projects_search_result') {
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpActivityVersion', 'name', cached_amp_activity.amp_activity_id, 'amp_activity_version', 'name', 'amp_activity_id', '" . $language->language . "')";
    foreach ($view->query->where as &$where) {
      foreach ($where['conditions'] as $k => $v) {
        if ($v['field'] == 'cached_amp_activity.name' && $v['operator'] == 'ILIKE') {
          unset($where['conditions'][$k]);
          $view->query->add_where_expression(0, 'unaccent(' . $translate_field . ') ILIKE unaccent(\'' . $v['value'] . '\')');
        }
      }
    }
  }

  if ($view->name == 'projects_search_result' && isset($view->get_total_amount_type)) {
    $view->query->fields = array();
    $view->query->orderby = array();
    $view->query->add_field(NULL, 'cached_amp_activity.amp_activity_id', 'amp_activity_id');
    $view->query->add_field(NULL, '(fd.transaction_amount / getexchange(cur.currency_code, fd.transaction_date))', 'amount');

    // joining the 'amp_funding' table
    $join = new views_join;
    $join->table = 'amp_funding';
    $join->left_table = 'cached_amp_activity';
    $join->left_field = 'amp_activity_id';
    $join->field = 'amp_activity_id';
    $join->type = 'LEFT';
    $join->adjusted = 'TRUE';
    $view->query->table_queue['amp_funding'] = array(
      'table' => 'amp_funding',
      'num' => 1,
      'alias' => 'f',
      'join' => $join,
      'relationship' => 'cached_amp_activity'
    );
    // add the table
    $view->query->tables['amp_funding']['amp_activity_id'] = array(
      'count' => 1,
      'alias' => 'f'
    );

    // joining the 'amp_funding_detail' table
    $join = new views_join;
    $join->table = 'amp_funding_detail';
    $join->left_table = 'amp_funding';
    $join->left_field = 'amp_funding_id';
    $join->field = 'amp_funding_id';
    $join->type = 'LEFT';
    $join->adjusted = 'TRUE';
    $view->query->table_queue['amp_funding_detail'] = array(
      'table' => 'amp_funding_detail',
      'num' => 1,
      'alias' => 'fd',
      'join' => $join,
      'relationship' => 'amp_funding'
    );
    // add the table
    $view->query->tables['amp_funding_detail']['amp_funding_id'] = array(
      'count' => 1,
      'alias' => 'fd'
    );

    // joining the 'amp_currency' table
    $join = new views_join;
    $join->table = 'amp_currency';
    $join->left_table = 'amp_funding_detail';
    $join->left_field = 'amp_currency_id';
    $join->field = 'amp_currency_id';
    $join->type = 'LEFT';
    $join->adjusted = 'TRUE';
    $view->query->table_queue['amp_currency'] = array(
      'table' => 'amp_currency',
      'num' => 1,
      'alias' => 'cur',
      'join' => $join,
      'relationship' => 'amp_funding_detail'
    );
    $view->query->tables['amp_currency']['amp_currency_id'] = array(
      'count' => 1,
      'alias' => 'cur'
    );
    $view->query->group_operator = 'AND';
    $view->query->add_where(0, 'fd.transaction_type', $view->get_total_amount_type, '=');
  }
}

/**
 * Implements hook_views_pre_render().
 */
function ampp_projects_views_pre_render(&$view) {
  switch ($view->name) {
    case 'projects_search_result':
      switch ($view->current_display) {
        case 'default':
        case 'panel_pane_1':
        case 'feed_1':
          if (isset($_SESSION['projects_search_result_conf'])) {
            $conf = unserialize($_SESSION['projects_search_result_conf']);
            $fields_to_remove = _ampp_projects_get_view_field_names($view->name, $view->current_display);

            foreach ($fields_to_remove as $field_name) {

              if (isset($conf[$field_name . '_label']) && $conf[$field_name . '_label'] != '') {
                $view->field[$field_name]->options['label'] = t($conf[$field_name . '_label']);
              }
              if (isset($conf[$field_name . '_flag']) && $conf[$field_name . '_flag'] != 1) {
                unset($view->field[$field_name]);
                unset($view->display_handler->handlers['field'][$field_name]);
              }
            }
          }
          break;
      }
      break;

    case 'top_activities_by_disbursment':
      if ($view->current_display == 'default') {
        if (isset($_SESSION['top_activities_by_disbursement_conf'])) {
          $conf = unserialize($_SESSION['top_activities_by_disbursement_conf']);

          foreach ($view->field as &$field) {
            $name = (substr($field->field, 0, 6) == 'field_') ? substr($field->field, 6) : $field->field;
            if (isset($conf['heading_' . $name]) && !empty($conf['heading_' . $name])) {
              $field->options['label'] = t($conf['heading_' . $name]);
            }
          }
        }
      }
      break;
  }

  if ($view->current_display == 'panel_pane_1' || $view->current_display == 'default' || $view->current_display == 'feed_1') {

  }

}

/**
 * Helper function for ampp_projects_views_pre_render().
 */
function _ampp_projects_get_view_field_names($view, $display_id) {
  $result = array();
  if (!empty($view->display[$display_id]->display_options['fields'])) {
    foreach ($view->display[$display_id]->display_options['fields'] as $field) {
      $result[] = $field['field'];
    }
  }

  return $result;
}

/**
 * Returns a list of options for amp_role filters and arguments.
 *
 * @TODO: Add caching!
 */
function _ampp_projects_get_amp_roles($key = NULL) {
  $items = array();

  if (!empty($key) && !in_array($key, array('role_code', 'amp_role_id'))) {
    return $items;
  }

  db_set_active('amp_projects');
  $query = db_select('amp_role', 'ar')->fields('ar');
  foreach ($query->execute()->fetchAll(PDO::FETCH_OBJ) as $role) {
    if (isset($key)) {
      $items[$role->$key] = $role->name;
    }
    else {
      $items[$role->role_code] = $role;
    }
  }
  db_set_active();

  return $items;
}
