<?php

/**
 * @file
 * Contains the menu callbacks for the projects_search_form.
 *
 * @TODO: Cleanup and use the amp_i18n.module functions.
 */

include_once('projects_search_form.helpers.inc');

/**
 * Implementation of hook_menu(). Helper for ampp_projects_menu().
 */
function _projects_search_form_menu_helper(&$items, $file_path) {
  $items_base = array(
    'file path' => dirname($file_path),
    'file' => 'projects_search_form_menu.inc',
  );

  // @TODO: Switch autocomplete calls to GET.
  $items['ampp_search/autocomplete/%'] = array(
    'page callback' => 'ampp_search_autocomplete_callback',
    'page arguments' => array(2),
    'access arguments' => array('access content'),
    'type' => MENU_CALLBACK,
  ) + $items_base;

  $items['ampp_search/forminfo'] = array(
    'page callback' => 'ampp_search_forminfo_callback',
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  ) + $items_base;

  // @TODO: Switch autocomplete calls to GET.
  $items['ampp_search/modalform/%'] = array(
    'page callback' => 'ampp_search_modalform_callback',
    'page arguments' => array(2),
    'access arguments' => array('access content'),
    'type' => MENU_CALLBACK,
  ) + $items_base;

  $items['ampp_search/modalform_values'] = array(
    'page callback' => 'ampp_search_get_modalform_values',
    'page arguments' => array(),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  ) + $items_base;

  $items['searchexport/searchresult.xls'] = array(
    'page callback' => 'ampp_search_result_export',
    'page arguments' => array(),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  ) + $items_base;
}

/**
 * Menu callback for ampp_search/autocomplete/%.
 *
 * @TODO: Cleanup and remove old URL mapping.
 */
function ampp_search_autocomplete_callback($type = NULL) {
  $items = array();

  $old_menu_url_mapping = array(
    'sectors' => 'ampp_search_autocomplete_sectors',

    'primarysectors' => 'ampp_search_autocomplete_sectors_primary',
    'sectors_primary' => 'ampp_search_autocomplete_sectors_primary',

    'secondarysectors' => 'ampp_search_autocomplete_sectors_secondary',
    'sectors_secondary' => 'ampp_search_autocomplete_sectors_secondary',

    'programs' => 'ampp_search_autocomplete_programs',

    'donors' => 'ampp_search_autocomplete_organisations_dn',
    'organisations_dn' => 'ampp_search_autocomplete_organisations_dn',

    'agencies' => 'ampp_search_autocomplete_organisations_ba',
    'organisations_ba' => 'ampp_search_autocomplete_organisations_ba',

    'regions' => 'ampp_search_autocomplete_locations',
    'locations' => 'ampp_search_autocomplete_locations',
  );

  if (empty($type) || empty($old_menu_url_mapping[$type])) {
    drupal_json_output($items);
    exit;
  }

  if (function_exists($old_menu_url_mapping[$type])) {
    $items = $old_menu_url_mapping[$type]();
  }

  drupal_json_output($items);
}

/**
 * Menu callback for ampp_search/forminfo.
 */
function ampp_search_forminfo_callback() {
  $message = amp_activity_projects_search_form_count_projects();

  if (!empty($_GET['preview_amount'])) {
    $message .= ' (';
    $message .= amp_activity_projects_search_form_preview_amount();
    $message .= ')';
  }
  drupal_json_output(array('message' => $message));
}

/**
 * Helper function for ampp_search_forminfo_callback().
 */
function amp_activity_projects_search_form_count_projects() {
  $orig = views_get_view('projects_search_result');
  $view = $orig->clone_view();
  $view->set_display('panel_pane_1');
  $args = _ampp_get_search_arguments(TRUE);
  $view->set_arguments($args);

  // @TODO: find a way to reduce the query time.
  // $view->query->fields = array();
  // $view->query->add_field(NULL, 'cached_amp_activity.amp_activity_id', 'amp_activity_id');
  // $view->query->orderby = array();

  $view->pre_execute();
  $view->execute();
  $projects_number = (int) $view->total_rows;

  $t_args = array('@number' => $projects_number);
  return '<span>' . t('Number of projects to show: @number', $t_args) . '</span>';
}

/**
 * Helper function for ampp_search_forminfo_callback().
 */
function amp_activity_projects_search_form_preview_amount() {
  $amount_commitment = amp_get_transactions_amount(AMP_TRANSACTION_TYPE_COMMITMENT);

  $t_args = array(
    '@amount' => format_number($amount_commitment, 0),
    '@currency' => variable_get('currency_code', ''),
  );

  return '<span>' . t('Commitment amount: @amount @currency', $t_args) . '</span>';
}

/**
 * Menu callback for ampp_search/modalform/%.
 */
function ampp_search_modalform_callback($type) {
  // Disable the admin menu inside the iframe.
  module_invoke('admin_menu', 'suppress');

  $old_menu_url_mapping = array(
    'sectors' => 'ampp_search_ajax_sectors_form',
    'sectors_primary' => 'ampp_search_ajax_primarysectors_form',
    'sectors_secondary' => 'ampp_search_ajax_secondarysectors_form',
    'programs' => 'ampp_search_ajax_programs_form',
    'locations' => 'ampp_search_ajax_locations_form',
    'organisations_dn' => 'ampp_search_ajax_donors_form',
    // 'organisations_ba' => '',
  );

  if (empty($type) || empty($old_menu_url_mapping[$type])) {
    return t('An error has occurred. Please try again later, or contact the site administrator.');
  }

  if (!function_exists($old_menu_url_mapping[$type])) {
    return t('An error has occurred. Please try again later, or contact the site administrator.');
  }

  return drupal_get_form($old_menu_url_mapping[$type]);
}

/**
 * Menu callback for ampp_search/modalform_values.
 *
 * @TODO: Cleanup and optimize!
 */
function ampp_search_get_modalform_values() {
  $form_build_id = $_POST['form_build_id'];
  $form_id = $_POST['form_id'];

  $form_cache = cache_get('form_' . $form_build_id, 'cache_form');
  $hs_selects_callbacks = array(
    'sectors' => 'hs_select_sectors_get_item_name',
    'sectors_primary' => 'hs_select_primarysectors_get_item_name',
    'sectors_secondary' => 'hs_select_secondarysectors_get_item_name',
    'programs' => 'hs_select_programs_get_item_name',
    'locations' => 'hs_select_locations_get_item_name',
    'organisations_dn' => 'ampp_projects_get_donor',
    // 'organisations_ba' => '',
  );

  $callback = '';

  $form_state = 'form_state_' .$form_build_id;
  $cache = cache_get($form_state, 'cache_form');

  if ($form_cache->data) {
    foreach ($hs_selects_callbacks as $key => $func) {
      if (array_key_exists($key, $form_cache->data)) {
        $callback = $func;
        break;
      }
    }
  }

  $json = new stdClass();
  $json->data = '';
  if (empty($callback) || !function_exists($callback)) {
    $json->error = 'callback does not exists';
    print drupal_json_encode($json);
    return ;
  }


  if (!empty($cache)) {
    $line_ages = $cache->data['storage']['hs'][0]['dropbox_lineages_selections'];
    $result = array();
    if (!empty($line_ages)) {
      foreach ($line_ages as $key => $value) {
        foreach ($value as $sub_key => $sub_value) {
          $item = $callback($sub_value);
          if (!empty($item)) {
            $result[] = $item[0];
          }
        }
      }
    }

    if (!empty($result)) {
      $json->data = $result;
    }
  }

  print drupal_json_encode($json);
}

/**
 * Menu callback: searchexport/searchresult.xls
 *
 * @TOOD: Set proper content headers.
 */
function ampp_search_result_export() {
  $view = views_get_view('projects_search_result');
  $tmpview = $view->clone_view();
  $tmpview->set_display('feed_1');

  $args = _ampp_get_search_arguments(TRUE);

  echo $tmpview->preview('feed_1', $args);
}
