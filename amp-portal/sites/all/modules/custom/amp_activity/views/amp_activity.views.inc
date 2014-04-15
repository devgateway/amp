<?php
/**
 * @file
 * Provide views data and handlers for amp_activity.
 */

/**
 * Implements hook_views_data_alter().
 */
function amp_activity_views_data_alter(&$data) {
  /**
   * TABLE DEFINITION: cached_amp_activity.
   */
  $data['cached_amp_activity']['table']['group'] = t('Activity');
  $data['cached_amp_activity']['table']['base'] = array(
    'field' => 'amp_activity_id',
    'title' => t('AMP Activities'),
    'help' => t('Contains information related to Cached AMP Activities (projects).'),
    'database' => AMP_DATABASE,
  );

  /**
   * REALS FIELDS: cached_amp_activity.
   */
  $data['cached_amp_activity']['amp_activity_id'] = array(
    'title' => t('ID'),
    'help' => t('The Activity ID'),
    'argument' => array(
      'handler' => 'views_handler_argument_numeric',
      'numeric' => TRUE,
    ),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_numeric',
    ),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
  );

  $data['cached_amp_activity']['name'] = array(
    'title' => t('Name'),
    'help' => t('Name for the activity.'),
    'argument' => array(
      'handler' => 'amp_activity_handler_argument_string',
    ),
    'field' => array(
      'help' => t('Name for the activity.') . ' (i18n)',
      'handler' => 'amp_activity_handler_field_translation',
      'real field' => 'amp_activity_id',
      'amp entity type' => 'activity_name',
      'click sortable' => TRUE,
    ),
  );

  /**
   * DATE FIELDS: cached_amp_activity.
   */

  // Define the base for all date specific handlers.
  $_date_handler_base = array(
    'argument' => array(
      'handler' => 'amp_activity_handler_argument_date',
    ),
    'field' => array(
      'handler' => 'amp_activity_handler_field_date_convert',
      'click sortable' => TRUE,
    ),
    'sort' => array(
      'handler' => 'views_handler_sort_date',
    ),
  );

  $data['cached_amp_activity']['date_created'] = array(
    'title' => t('Created Date'),
    'help' => t('The date when the activity was created.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['date_updated'] = array(
    'title' => t('Updated Date'),
    'help' => t('The date when the activity was last update.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['proposed_approval_date'] = array(
    'title' => t('Proposed Approval Date'),
    'help' => t('The proposed approval date of the activity.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['proposed_approval_date'] = array(
    'title' => t('Proposed Approval Date'),
    'help' => t('The proposed approval date of the activity.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['actual_approval_date'] = array(
    'title' => t('Actual Approval Date'),
    'help' => t('The actual approval date of the activity.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['original_comp_date'] = array(
    'title' => t('Original Completion Date'),
    'help' => t('The activity original completion date.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['proposed_start_date'] = array(
    'title' => t('Proposed Start Date'),
    'help' => t('The proposed start date of the activity.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['actual_start_date'] = array(
    'title' => t('Actual Start Date'),
    'help' => t('The actual start date of the activity.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['proposed_completion_date'] = array(
    'title' => t('Proposed Completion Date'),
    'help' => t('The proposed completion date of the activity.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['actual_completion_date'] = array(
    'title' => t('Actual Completion Date'),
    'help' => t('The actual completion date of the activity.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['contracting_date'] = array(
    'title' => t('Final Date for Contracting'),
    'help' => t('The final date for contracting.'),
  ) + $_date_handler_base;

  $data['cached_amp_activity']['disbursments_date'] = array(
    'title' => t('Final Date for Disbursements'),
    'help' => t('The final date for disbursements.'),
  ) + $_date_handler_base;

  /**
   * CUSTOM FIELDS: cached_amp_activity.
   */
  $data['cached_amp_activity']['filter_public'] = array(
    'title' => t('Filter Public'),
    'help' => t('Filters out Activities that are not public.'),
    'filter' => array(
      'handler' => 'amp_activity_handler_filter_activity_filter_public',
      'real field' => 'amp_activity_id',
    ),
  );

  $data['cached_amp_activity']['view_link'] = array(
    'title' => t('View Link'),
    'help' => t('A link to the activity displayed as an "icon".'),
    'field' => array(
      'handler' => 'amp_activity_handler_field_view_activity',
      'real field' => 'amp_activity_id',
    ),
  );

  $data['cached_amp_activity']['organisation_names'] = array(
    'title' => t('Organisations having Role'),
    'real field' => 'organisation',
    'field' => array(
      'help' => t('Show Organizations that have a specific role.'),
      'handler' => 'amp_activity_handler_field_organisation_names',
      'real field' => 'amp_activity_id',
      'amp entity type' => 'organisation_name',
      'options callback' => '_ampp_projects_get_amp_roles',
      'options label' => 'Roles',
      'options arguments' => array('metadata_code'),
    ),
  );

  $data['cached_amp_activity']['sector_names'] = array(
    'title' => t('Sectors by classification'),
    'help' => t('Retrives Sectors of a particulary classification for the Activity on pre_render.') . ' (i18n)',
    'field' => array(
      'handler' => 'amp_activity_handler_field_sector_names',
      'real field' => 'amp_activity_id',
      'amp entity type' => 'sector_name',
      'options callback' => '_ampp_projects_get_amp_sector_schemes',
      'options label' => 'Sectors',
      'options arguments' => array('metadata_code'),
    ),
  );

  $data['cached_amp_activity']['program_names'] = array(
    'title' => t('Program Names'),
    'help' => t('Retrives Programs for the Activity on pre_render.') . ' (i18n)',
    'field' => array(
      'handler' => 'amp_activity_handler_field_program_names',
      'real field' => 'amp_activity_id',
      'amp entity type' => 'program_name',
    ),
  );

  $data['cached_amp_activity']['location_names'] = array(
    'title' => t('Location Names'),
    'help' => t('Retrives Programs for the Activity on pre_render.') . ' (i18n)',
    'field' => array(
      'handler' => 'amp_activity_handler_field_location_names',
      'real field' => 'amp_activity_id',
      'amp entity type' => 'location_name',
    ),
  );

  $data['cached_amp_activity']['status_name'] = array(
    'title' => t('Status'),
    'help' => t('The status of the activity, human readable.'),
    'field' => array(
      'handler' => 'amp_activity_handler_field_status',
      'real field' => 'amp_activity_id',
      'click sortable' => TRUE,
    ),
  );

  $data['cached_amp_activity']['period_start_date'] = array(
    'title' => t('Period Start Date'),
    'help' => t('Activity period start date.'),
    'argument' => array(
      'handler' => 'amp_activity_handler_argument_period',
      'real field' => 'actual_start_date',
      'amp real field' => 'period_start_field',
    ),
  );

  $data['cached_amp_activity']['period_end_date'] = array(
    'title' => t('Period End Date'),
    'help' => t('Activity period end date.'),
    'argument' => array(
      'handler' => 'amp_activity_handler_argument_period',
      'real field' => 'actual_completion_date',
      'amp real field' => 'period_end_field',
    ),
  );

  $data['cached_amp_activity']['commitment_amount'] = array(
    'title' => t('Commitment amount'),
    'help' => t('Transaction Commitment amount'),
    'field' => array(
      'handler' => 'amp_activity_handler_field_transaction',
      'real field' => 'amp_activity_id',
      'amp transaction type' => AMP_TRANSACTION_TYPE_COMMITMENT,
      'float' => TRUE,
    ),
  );

  $data['cached_amp_activity']['disbursement_amount'] = array(
    'title' => t('Disbursement amount'),
    'help' => t('Transaction Disbursement amount'),
    'field' => array(
      'handler' => 'amp_activity_handler_field_transaction',
      'real field' => 'amp_activity_id',
      'amp transaction type' => AMP_TRANSACTION_TYPE_DISBURSEMENT,
      'float' => TRUE,
    ),
  );

  /**
   * TABLE DEFINITION: amp_org_role.
   */
  $data['amp_org_role']['table']['group'] = t('Organization Role');
  $data['amp_org_role']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'activity',
      'type' => 'INNER',
    ),
  );

  $data['amp_org_role']['organisation_role'] = array(
    'title' => t('Organisation having Role'),
    'real field' => 'organisation',
    'argument' => array(
      'help' => t('Filter by Organizations that have a specific role'),
      'handler' => 'amp_activity_handler_argument_organisation_role',
      'options callback' => '_ampp_projects_get_amp_roles',
      'options label' => 'Roles',
      'options arguments' => array('metadata_code'),
    ),
  );

  /**
   * TABLE DEFINITION: amp_activity_sector.
   */
  $data['amp_activity_sector']['table']['group'] = t('Sector');
  $data['amp_activity_sector']['table']['join'] = array(
    // Directly links to amp_activity table.
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
    ),
  );

  $data['amp_activity_sector']['amp_sector_id'] = array(
    'title' => t('Activity Sectors'),
    'help' => t('Filter Activities by Sectors ID'),
    'argument' => array(
      'handler' => 'amp_activity_handler_argument_expandable',
      'expand callback' => '_amp_activity_get_child_sector_ids',
    ),
  );

  /**
   * TABLE DEFINITION: amp_activity_location.
   */
  $data['amp_activity_location']['table']['group'] = t('Location');
  $data['amp_activity_location']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
    ),
  );

  /**
   * TABLE DEFINITION: amp_location.
   */
  $data['amp_location']['table']['group'] = t('Location');
  $data['amp_location']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_table' => 'amp_activity_location',
      'left_field' => 'amp_location_id',
      'field' => 'amp_location_id'
    ),
  );

  // We need amp_activity_location to get the Location ID.
  $data['amp_location']['location_id'] = array(
    'title' => t('Activity Locations'),
    'help' => t('Filter Activities by Location ID'),
    'argument' => array(
      'handler' => 'amp_activity_handler_argument_expandable',
      'expand callback' => '_amp_activity_get_location_parent_ids',
    ),
  );

  /**
   * TABLE DEFINITION: amp_activity_program.
   */
  $data['amp_activity_program']['table']['group'] = t('Program');
  $data['amp_activity_program']['table']['join'] = array(
    // Directly links to amp_activity table.
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
    ),
  );

  $data['amp_activity_program']['amp_program_id'] = array(
    'title' => t('Activity Programs'),
    'help' => t('Filter Activities by Program ID'),
    'argument' => array(
      'handler' => 'amp_activity_handler_argument_expandable',
      'expand callback' => '_amp_activity_get_child_program_ids',
    ),
  );

  /**
   * TABLE DEFINITION: amp_activities_categoryvalues.
   */
  $data['amp_activities_categoryvalues']['table']['group'] = t('Category Values');
  $data['amp_activities_categoryvalues']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
      'type' => 'INNER',
      'extra' => array(
        array(
          'field' => 'amp_categoryvalue_id',
          'value' => _amp_activity_get_onoff_budget_ids(),
          'operator' => 'IN',
          'numeric' => TRUE,
        ),
      ),
    ),
  );

  $data['amp_activities_categoryvalues']['amp_categoryvalue_id'] = array(
    'title' => t('Activities Projects Budget'),
    'help' => t('Budget type'),
    'argument' => array(
      'handler' => 'views_handler_argument_numeric',
    ),
  );

  /**
   * TABLE DEFINITION: cached_v_status.
   */
  $data['cached_v_status']['table']['group'] = t('Activity Status');
  $data['cached_v_status']['table']['join'] = array(
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id'
    ),
  );

  $data['cached_v_status']['amp_status_id'] = array(
    'title' => t('Activity Status ID'),
    'help' => t('Filters Activities by status ID.'),
    'argument' => array(
      'handler' => 'views_handler_argument_numeric',
    ),
    // The status field is displayed only on some settings.
    // @see: amp_activity_handler_field_status
  );

  return $data;
}

/**
 * Implements hook_views_pre_execute().
 */
function amp_activity_views_pre_execute(&$view) {
  if ($view->name != 'projects_search_result') {
    return;
  }

  // Hide fields before rendering.
  switch ($view->current_display) {
    case 'default':
    case 'panel_pane_1':
    case 'feed_1':
      // Get the 'projects_search_result' pane configuration form session.
      if (!isset($_SESSION['projects_search_result_conf'])) {
        break;
      }
      $pane_conf = unserialize($_SESSION['projects_search_result_conf']);

      foreach ($view->display_handler->handlers['field'] as $field_name => $field_def) {
        if (!isset($pane_conf[$field_name])) {
          continue;
        }

        // Manually hide the field.
        // @TODO: Find a way to remove the field before runing the query.
        if (empty($pane_conf[$field_name]['show'])) {
          unset($view->field[$field_name]);
          unset($view->display_handler->handlers['field'][$field_name]);
          continue;
        }

        // Set the field column label.
        if (!empty($pane_conf[$field_name]['label'])) {
          $view->field[$field_name]->options['label'] = t($pane_conf[$field_name]['label']);
        }
      }
      break;
  }

  // Alter the XLS export.
  if ($view->current_display == 'feed_1') {
    // Remove the view_link from the XLS.
    unset($view->field['view_link']);

    // Toggle the status column visibility.
    if (empty($_GET['show_status_column'])) {
      unset($view->field['status_name']);
    }
  }
}

/**
 * Returns a list of options for amp_role filters and arguments.
 *
 * @TODO: Add caching!
 */
function _ampp_projects_get_amp_roles($key = NULL) {
  $items = array();

  if (!empty($key) && !in_array($key, array('metadata_code', 'metadata_id'))) {
    return $items;
  }

  if ($key == 'metadata_id') {
    $key = 'amp_role_id';
  }
  else {
    if ($key == 'metadata_code') {
      $key = 'role_code';
    }
  }

  db_set_active('amp_projects');
  $query = db_select('amp_role', 'ar')->fields('ar');
  foreach ($query->execute()->fetchAll(PDO::FETCH_OBJ) as $role) {
    if (isset($key)) {
      $items[$role->$key] = $role->name;
    }
    else {
      $items[$role->role_code] = $role->amp_role_id;
    }
  }
  db_set_active();

  return $items;
}

/**
 * Returns a list of options for amp_role filters and arguments.
 *
 * @TODO: Add caching!
 */
function _ampp_projects_get_amp_sector_schemes($key = NULL) {
  $items = array();

  if (!empty($key) && !in_array($key, array('metadata_code', 'metadata_id'))) {
    return $items;
  }

  if ($key == 'metadata_id') {
    $key = 'id';
  }
  else {
    if ($key == 'metadata_code') {
      $key = 'name';
    }
  }

  db_set_active('amp_projects');
  $query = db_select('amp_classification_config', 'acc')->fields('acc');
  foreach ($query->execute()->fetchAll(PDO::FETCH_OBJ) as $role) {
    if (isset($key)) {
      $items[$role->$key] = $role->name;
    }
    else {
      $items[$role->name] = $role->id;
    }
  }
  db_set_active();

  return $items;
}
