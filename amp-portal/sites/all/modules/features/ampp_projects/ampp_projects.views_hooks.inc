<?php
/**
 * @file
 * Define views stuff.
 *
 * @TODO: Move to amp_activity.module.
 */

/**
 * Implements hook_views_data_alter().
 */
function ampp_projects_views_data_alter(&$data) {
  // TABLE: cached_amp_activity
  $data['cached_amp_activity']['commitment_amount'] = array(
    'title' => t('Commitment amount'),
    'help' => t('Transaction Commitment amount'),
    'field' => array(
      'handler' => 'views_handler_field_amount_commitment',
      'float' => TRUE,
    ),
  );

  $data['cached_amp_activity']['disbursement_amount'] = array(
    'title' => t('Disbursement amount'),
    'help' => t('Transaction Disbursement amount'),
    'field' => array(
      'handler' => 'views_handler_field_amount_disbursement',
      'float' => TRUE,
    ),
  );

  // TABLE: amp_funding
  // Required for commitment_amount and disbursement_amount.
  $data['amp_funding']['table']['group'] = t('Funding');
  $data['amp_funding']['table']['join'] = array(
    // Directly links to amp_activity table.
    'cached_amp_activity' => array(
      'left_field' => 'amp_activity_id',
      'field' => 'amp_activity_id',
    ),
  );
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
 * Implements hook_views_query_alter().
 */
function ampp_projects_views_query_alter(&$view, &$query) {
  global $language;

  if ($view->name != 'projects_search_result') {
    return;
  }

  // @TODO: Move this to amp_activity_handler_argument_string
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpActivityVersion', 'name', cached_amp_activity.amp_activity_id, 'amp_activity_version', 'name', 'amp_activity_id', '" . $language->language . "')";
  foreach ($view->query->where as &$where) {
    foreach ($where['conditions'] as $k => $v) {
      if ($v['field'] == 'cached_amp_activity.name' && $v['operator'] == 'ILIKE') {
        unset($where['conditions'][$k]);
        $view->query->add_where_expression(0, 'unaccent(' . $translate_field . ') ILIKE unaccent(\'' . $v['value'] . '\')');
      }
    }
  }

  // @TODO: Cleanup, move to a field handler.
  if (isset($view->get_total_amount_type)) {
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
  if ($view->name != 'top_activities_by_disbursment') {
    return;
  }

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
}

/**
 * Returns all arguments required for the projects_search_result view.
 *
 * @TODO: Switch from using arguments to exposed filters.
 */
function _ampp_get_search_arguments($for_views = FALSE) {
  $processed_arguments = array();
  // Untill we use exposed filters instead of arguments, we need to send them to views in the right order.
  $arguments_list = array(
    '0' => 'donors',
    '1' => 'sectors',
    '2' => 'locations',
    '3' => 'keywords',
    '4' => 'budget',
    '5' => 'programs',
    '6' => 'period_start',
    '7' => 'period_end',
    '8' => 'agencies',
  );

  foreach ($arguments_list as $delta => $name) {
    $argkey = ($for_views) ? $delta: $name;

    // In some situations args migth end up in the request under a different name.
    // @TODO: Cleanup projects_search_form content type.
    $name = _ampp_get_search_argument_query_translation($name);

    switch ($name) {
      case 'sectors':
        // Returns an aggregated list of sector IDs for views or as sepparate keys for each:
        // 'sectors', 'primarysectors', 'secondarysectors'
        $values = _ampp_get_search_argument_sectors($name, $for_views);
        if ($for_views) {
          $processed_arguments[$argkey] = $values;
        }
        else {
          $processed_arguments += $values;
        }

        break;

      case 'locations':
        // Expand location argument for views.
        if ($for_views) {
          $processed_arguments[$argkey] = _ampp_get_search_argument_location($name, $for_views);
        }
        else {
          $processed_arguments[$argkey] = _ampp_get_search_argument_value($name, $for_views);
        }
        break;

      default:
        $processed_arguments[$argkey] = _ampp_get_search_argument_value($name, $for_views);
        break;
    }
  }

  return $processed_arguments;
}

/**
 * Helper function for _ampp_get_search_arguments()
 *
 * This function make sure the arguments get to views in all cases.
 */
function _ampp_get_search_argument_query_translation($name) {
  $arg_mapping = array(
    'agencies' => 'agency',
    'donors' => 'donor',
    'keywords' => 'keyword',
    'locations' => 'region',
    'primary_sectors' => 'primary_sector',
    'programs' => 'program',
    'secondary_sectors' => 'secondary_sector',
    'sectors' => 'sector',
  );

  if (isset($arg_mapping[$name])) {
    $new_name = $arg_mapping[$name];
    if (isset($_REQUEST[$new_name])) {
      return $new_name;
    }
  }

  return $name;
}

/**
 * Helper function for _ampp_get_search_arguments()
 */
function _ampp_get_search_argument_value($name, $for_views) {
  if (empty($_REQUEST[$name]) || $_REQUEST[$name] === 'all') {
    return ($for_views) ? 'all' : '';
  }

  $value = $_REQUEST[$name];
  if (is_array($value)) {
    return implode(',', $value);
  }

  return urldecode($value);
}

/**
 * Helper function for _ampp_get_search_arguments()
 */
function _ampp_get_search_argument_location($name, $for_views) {
  if (empty($_REQUEST[$name]) || $_REQUEST[$name] === 'all') {
    return ($for_views) ? 'all' : '';
  }

  // We need to add all descendant locations.
  $locations = $_REQUEST[$name];
  if (is_string($locations)) {
    $locations = explode(',', urldecode($locations));
  }
  $locations = _ampp_get_search_argument_location_descendants($locations);

  return implode(',', $locations);
}

/**
 * Helper function for _ampp_get_search_arguments()
 *
 * Returns either an array of sectors keyed by sector type or an aggregated array of all sectors.
 */
function _ampp_get_search_argument_sectors($name, $for_views) {
  $list = array();

  $arg_name = _ampp_get_search_argument_query_translation('sectors');
  $list['sectors'] = empty($_REQUEST[$arg_name]) ? '' : $_REQUEST[$arg_name];
  $arg_name = _ampp_get_search_argument_query_translation('primarysectors');
  $list['primarysectors'] = empty($_REQUEST[$arg_name]) ? '' : $_REQUEST[$arg_name];
  $arg_name = _ampp_get_search_argument_query_translation('secondarysectors');
  $list['secondarysectors'] = empty($_REQUEST[$arg_name]) ? '' : $_REQUEST[$arg_name];

  if (empty($list['sectors']) && empty($list['primarysectors']) && empty($list['secondarysectors'])) {
    return ($for_views) ? 'all' : $list;
  }

  if ($list['sectors'] === 'all' && $list['primarysectors'] === 'all' && $list['secondarysectors'] === 'all') {
    return ($for_views) ? 'all' : array('sectors' => '', 'primarysectors' => '', 'secondarysectors' => '');
  }

  // Get an aggregated list of sectors.
  $aggregated_list = array();
  foreach ($list as $key => $value) {
    // Strip out arguments that don't have any influence.
    if ($value === 'all') {
      $list[$key] = '';
      break;
    }

    if (is_string($value)) {
      $value = explode(',', urldecode($value));
    }
    $aggregated_list += $value;
  }

  if ($for_views) {
    return implode(',', $aggregated_list);
  }

  return $list;
}

/**
 * Returns the children of an array location IDs.
 */
function _ampp_get_search_argument_location_descendants($location_ids) {
  $child_location_ids = array();

  if (is_numeric($location_ids)) {
    $location_ids = array($location_ids);
  }

  db_set_active('amp_projects');
  foreach ($location_ids as $lid) {
    $child_ids = db_select('amp_category_value_location', 'acvl')
      ->fields('acvl', array('id'))
      ->condition('acvl.parent_location', $lid, '=')
      ->execute()
      ->fetchCol();

    foreach ($child_ids as $child_id) {
      $child_location_ids[] = $child_id;
    }
  }
  db_set_active();

  return array_merge($location_ids, $child_location_ids);
}
