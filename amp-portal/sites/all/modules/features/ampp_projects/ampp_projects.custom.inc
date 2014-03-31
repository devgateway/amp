<?php
// $Id: ampp_projects.custom.inc,v 1.121.6.3 2014/01/09 02:20:07 vamirbekyan Exp $

global $amp_db_exists;
$amp_db_exists = FALSE;

class OnOffBudgetIds {
  public static $onBudgetProjectsId = 0;
  public static $offBudgetProjectsId = 0;
}


/**
 *
 */
function ampp_projects_date_popup_process_alter(&$element, &$form_state, $context) {
  switch ($element['#name']) {
    case 'period_start' :
      $element['date']['#title'] = '';
      break;
    case 'period_end' :
      $element['date']['#title'] = t('to');
      break;
  }
  unset($element['date']['#description']);
  //unset($element['time']['#description']);
}


/**
* Implementation of hook_init().
*/
function ampp_projects_init() {
  global $language;
  $lang_def = variable_get('language_default', '');
  if (empty($lang_def) || $lang_def->language == $language->language) {
    $amp_language = '';
  } else {
    $amp_language = $language->language;
  }

  // Get the on/off budget projects ids
  getOnOffBudgetIDs();

  $ampp_projects_settings = array(
    'url' => url('ampp_search/hs-dropbox'),
    'lang' => $amp_language,
  );
  drupal_add_js(array('ampp_projects' => $ampp_projects_settings), 'setting');

  drupal_add_js(drupal_get_path('module', 'ampp_projects') . '/tokeninput/src/jquery.tokeninput.js');
  drupal_add_js(drupal_get_path('module', 'ampp_projects') . '/ampp_projects.js');

  drupal_add_css("div.hidden-locations, #loading-count {display:none;} span.more-locations {cursor: pointer;}",array('type' => 'inline'));
  drupal_add_library('system', 'ui.dialog');

  //Let's check if AMP database is configured and if not throw a worning message and also do not try to connect to it later
  amp_db_exists();
  ampp_projects_check_currency_code();
}


/**
 * Checking if AMP database is configured
 *
 * @TODO: Switch to drupal_static() and check if the DB connection actually works.
 */
function amp_db_exists() {
  global $amp_db_exists;

  global $databases;
  if (isset($databases['amp_projects']) && is_array($databases['amp_projects'])) {
    $amp_db_exists = TRUE;
  }
}


/*
 * implementation hook_ctools_plugin_directory
 */
function ampp_projects_ctools_plugin_directory($owner, $plugin_type) {
  if($owner == 'ctools' && $plugin_type == 'content_types') {
    return 'plugins/content_types';
  }
}

function getOnOffBudgetIDs() {
  db_set_active("amp_projects");

  $subquery = db_select('amp_category_class', 'cc')
    ->fields('cc', array('id'))
    ->condition('cc.category_name','Activity Budget','=');

  $query = db_select('amp_category_value', 'cv')
    ->fields('cv', array('id'))
    ->condition('cv.amp_category_class_id',$subquery,'IN')
    ->condition('cv.category_value','Off Budget','=');

  $result = $query->execute();
  foreach ($result as $record) {
    OnOffBudgetIds::$offBudgetProjectsId = $record->id;
  }

  $query = db_select('amp_category_value', 'cv')
    ->fields('cv', array('id'))
    ->condition('cv.amp_category_class_id',$subquery,'IN')
    ->condition('cv.category_value','On Budget','=');

  $result = $query->execute();
  foreach ($result as $record) {
    OnOffBudgetIds::$onBudgetProjectsId = $record->id;
  }

  db_set_active();
}


/**
 * Sectors JSON array from URL
 * @return multitype:mixed
 */
function ampp_get_sectors_from_url() {
  $sectors = array();

  $sectors_ids = isset($_GET["sectors"]) ? $_GET["sectors"] : 'all';
  if ($sectors_ids != 'all' and !empty($sectors_ids)){
    db_set_active("amp_projects");
    $items = array();

    global $language;
    global $user;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

    $query = db_select('amp_sector', 'sector');
    $query->fields('sector', array('amp_sector_id'));
    $query->addExpression($translate_field, 'name');
    $query->condition('sector.amp_sector_id', explode(",", $sectors_ids), "IN");

    if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_get_sectors_from_url', dpq($query, 1));

    $result = $query->execute();
    foreach ($result as $record) {
      $sectors[] = array('id'=>$record->amp_sector_id, 'name' => addslashes(check_plain($record->name)));
    }
    db_set_active();
  }

  return $sectors;
}


/*
 *
 */
function ampp_get_primarysectors_from_url() {
  $sectors = array();
  $sectors_ids = isset($_GET["primarysectors"]) ? $_GET["primarysectors"] : 'all';
  if($sectors_ids != 'all' and !empty($sectors_ids)){
    db_set_active("amp_projects");
    $items = array();

    global $language;
    global $user;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

    $query = db_select('amp_sector', 'sector');
    $query->fields('sector', array('amp_sector_id'));
    $query->addExpression($translate_field, 'name');
    $query->condition('sector.amp_sector_id', explode(",", $sectors_ids), "IN");

    if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_get_primarysectors_from_url', dpq($query, 1));

    $result = $query->execute();
    foreach ($result as $record) {
      $sectors[] = array('id'=>$record->amp_sector_id, 'name' => addslashes(check_plain($record->name)));
    }
    db_set_active();
  }
  return $sectors;
}


/*
 *
 */
function ampp_get_secondarysectors_from_url() {
  $sectors = array();
  $sectors_ids = isset($_GET["secondarysectors"]) ? $_GET["secondarysectors"] : 'all';
  if($sectors_ids != 'all' and !empty($sectors_ids)){
    db_set_active("amp_projects");
    $items = array();

    global $language;
    global $user;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

    $query = db_select('amp_sector', 'sector');
    $query->fields('sector', array('amp_sector_id'));
    $query->addExpression($translate_field, 'name');
    $query->condition('sector.amp_sector_id',explode(",", $sectors_ids), "IN");

    if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_get_secondarysectors_from_url', dpq($query, 1));

    $result = $query->execute();
    foreach ($result as $record) {
      $sectors[] = array('id'=>$record->amp_sector_id, 'name' => addslashes(check_plain($record->name)));
    }
    db_set_active();
  }
  return $sectors;
}


/*
 *
 */
function ampp_get_programs_from_url() {
  $programs = array();
  $programs_ids = isset($_GET["programs"]) ? $_GET["programs"] : 'all';
  if($programs_ids != 'all' and !empty($programs_ids)){
    db_set_active("amp_projects");
    $items = array();

    $query = db_select('amp_theme', 'program');
    $query->fields('program', array('amp_theme_id', 'name'));
    if (variable_get('amp_limit_programs_to_activities', 1)) {
      $query->join('amp_activity_program', 'activity', 'activity.amp_program_id = program.amp_theme_id');
    }
    $query->where('getprogramsettingid(program.amp_theme_id) = ' . variable_get('amp_programs_level', 2));
    $query->condition('program.amp_theme_id', explode(",",$programs_ids), "IN");

    $result = $query->distinct()->execute()->fetchAll();
    foreach ($result as $record) {
      $programs[] = array('id'=>$record->amp_theme_id, 'name'=>addslashes(check_plain($record->name)));
    }
    db_set_active();
  }
  return $programs;
}


/*
 *
 */
function ampp_get_keywords_from_url($source_arg = TRUE) {
  $keywords = isset($_GET["keywords"]) ? $_GET["keywords"] : array();

  if (!is_array($keywords)) {
    $keywords = array($keywords);
  }

  return $keywords;
}


/**
 * Donors JSON array from URL
 * @return multitype:mixed
 */
function ampp_get_donors_from_url() {
  $donors = array();
  $donors_ids = isset($_GET["donors"]) ? $_GET["donors"] : 'all';

  db_set_active("amp_projects");
  $items = array();
  if ($donors_ids != 'all' && !empty($donors_ids)) {

    global $language;
    global $user;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', donor.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

    $query = db_select('amp_organisation', 'donor');
    $query->distinct();
    $query->fields('donor', array('amp_org_id', 'acronym'));
    $query->addExpression($translate_field, 'name');
    if (variable_get('amp_show_only_donors', 0)) {
      $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = donor.amp_org_id');
      $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
    }
    $query->condition('donor.amp_org_id', explode(',', $donors_ids), 'IN');
    $or_statement = db_or()->condition('donor.deleted', 'true', '<>')->condition('donor.deleted', NULL, 'is');
    $query->condition($or_statement);

    if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_get_donors_from_url', dpq($query, 1));

    $result = $query->execute();
    foreach ($result as $record) {
      $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
      $donors[] = array('id' => $record->amp_org_id, 'name' => $name);
    }

  }

  db_set_active();
  return $donors;
}


/**
 * Agencies JSON array from URL
 * @return multitype:mixed
 */
function ampp_get_agencies_from_url() {
  $agencies = array();
  $agencies_ids = isset($_GET["agencies"]) ? $_GET["agencies"] : 'all';

  db_set_active("amp_projects");
  $items = array();
  if ($agencies_ids != 'all' && !empty($agencies_ids)) {

    global $language;
    global $user;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', agency.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

    $query = db_select('amp_organisation', 'agency');
    $query->distinct();
    $query->fields('agency', array('amp_org_id', 'acronym'));
    $query->addExpression($translate_field, 'name');
    $query->condition('agency.amp_org_id', explode(',', $agencies_ids), 'IN');
    $or_statement = db_or()->condition('agency.deleted', 'true', '<>')->condition('agency.deleted', NULL, 'is');
    $query->condition($or_statement);

    $result = $query->execute();
    foreach ($result as $record) {
      $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
      $agencies[] = array('id' => $record->amp_org_id, 'name' => $name);
    }

  }

  db_set_active();
  return $agencies;
}


/**
 * Locations JSON array from URL
 */
function ampp_get_locations_from_url() {
  $locations = array();
  $locations_ids = isset($_GET["locations"]) ? $_GET["locations"] : 'all';
  if($locations_ids != 'all' and !empty($locations_ids)){
    db_set_active("amp_projects");
    $items = array();

    global $language;
    global $user;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpCategoryValueLocations', 'name', location.id, 'amp_category_value_location', 'location_name', 'id', '" . $language->language . "')";

    $query = db_select('amp_category_value_location', 'location');
    $query->fields('location', array('id'));
    $query->addExpression($translate_field, 'location_name');
    $query->condition('location.id', explode(",", $locations_ids), "IN");

    $result = $query->execute();
    foreach ($result as $record) {
      $parents = get_location_path($record->id);
      if (!empty($parents)) {
        $name = $parents . ' -> ' . $record->location_name;
      } else {
        $name = $record->location_name;
      }
      $locations[] = array('id' => $record->id, 'name' => check_plain($name));
    }
    db_set_active();
  }
  return $locations;
}


/**
 * Period JSON array from URL
 */
function ampp_get_period_from_url() {
  $period = array();

  $period['period_start'] = 'all';
  if (isset($_GET['period_start'])) {
    $period['period_start'] = $_GET['period_start'];
  }

  $period['period_end'] = 'all';
  if (isset($_GET['period_end'])) {
    $period['period_end'] = $_GET['period_end'];
  }

  return $period;
}


/*
 *
 */
function ampp_get_onoffbudget_from_url($source_arg = TRUE) {

  $budgetflags = isset($_GET["budget"]) ? $_GET["budget"] : 'all';

  if (!$source_arg) {
    $budgetflags = 'all';
  }
  if ($budgetflags == 'all') {
    return '';
  }

  return array("$budgetflags");
}


/*
 *
 */
function ampp_get_regionalprojects_from_url($source_arg = TRUE) {
  if (!$source_arg) {
    $regionalprojects = 'all';
  }
  if (isset($_GET["showregionalprojects"]) && isset($_GET["regionalprojects"]) && $regionalprojects == 'all') {
    return 0;
  }
  return 1;
}

/**
 * Get the total amount for given type of transactions:
 *  - AMP_TRANSACTION_TYPE_COMMITMENT for commitments
 *  - AMP_TRANSACTION_TYPE_DISBURSEMENT for disbursments
 */
function amp_get_transactions_amount($transaction_type) {
  $cache_id = __FUNCTION__ . ':noargs:' . $transaction_type;
  $args = _ampp_get_search_arguments(TRUE);

  // Use cache in no arguments were provided.
  $use_cache = TRUE;
  foreach ($args as $value) {
    if ($value != 'all') {
      $use_cache = FALSE;
    }
  }

  if ($use_cache && $cache = cache_get($cache_id)) {
    return $cache->data;
  }

  $view = views_get_view('projects_search_result');
  $tmpview = $view->clone_view();
  $tmpview->set_display('panel_pane_1');
  $tmpview->set_arguments($args);

  // Change the views query to get the ammounts for $transaction_type.
  $tmpview->get_total_amount_type = $transaction_type;

  // Load all rows, this makes the query very slow!
  $tmpview->set_items_per_page(0);

  $tmpview->pre_execute();
  $tmpview->execute();

  $sum = 0;
  foreach ($tmpview->result as $row) {
    $sum = $sum + $row->amount;
  }

  // Store the results in cache so things go faster next time!
  if ($use_cache) {
    cache_set($cache_id, $sum, 'cache', CACHE_TEMPORARY);
  }

  return $sum;
}

/**
 * Implementation of hook_menu().
 */
function ampp_projects_menu() {
  $items['search_form'] = array(
    'title' => 'AMPP search form',
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_form'),
    'access callback' => TRUE,
    'description' => 'Item for AMPP serach form',
    'type' => MENU_NORMAL_ITEM,
  );

  // @TODO: Switch autocomplete calls to GET.
  $items['ampp_search/%'] = array(
    'page callback' => 'ampp_search_autocomplete_callback',
    'page arguments' => array(1),
    'access arguments' => array('access content'),
    'type' => MENU_CALLBACK,
  );

  $items['ampp_search/search_result_count'] = array(
      'page callback' => 'ampp_search_result_count',
      'access callback' => TRUE,
      'type' => MENU_CALLBACK,
  );

  $items['ampp_search/ajax-sectores'] = array(
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_ajax_sectors_form'),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );

  $items['ampp_search/ajax-primarysectors'] = array(
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_ajax_primarysectors_form'),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );

  $items['ampp_search/ajax-secondarysectors'] = array(
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_ajax_secondarysectors_form'),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );

  $items['ampp_search/ajax-programs'] = array(
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_ajax_programs_form'),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );

  $items['ampp_search/ajax-donors'] = array(
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_ajax_donors_form'),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );

  $items['ampp_search/ajax-regions'] = array(
    'page callback' => 'drupal_get_form',
    'page arguments' => array('ampp_search_ajax_locations_form'),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );


  $items['ampp_search/hs-dropbox'] = array(
    'page callback' => 'ampp_search_hs_dropbox_values',
    'page arguments' => array(),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );

  $items['searchexport/searchresult.xls'] = array(
    'page callback' => 'ampp_search_result_export',
    'page arguments' => array(),
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );
  return $items;
}


/**
 * Serach result count callback.
 * Because of some custom aggregate selects
 * (such as sector, location, donor) it is not
 * possible to use views totla count.
 * TODO: Find a way to use views to get count
 */
function ampp_search_result_count($JSON = TRUE, $display_amount = FALSE) {
  global $amp_db_exists;
  if (!$amp_db_exists) {
    return;
  }

  if ($JSON && !empty($_GET['preview_amount'])) {
    $display_amount = TRUE;
  }


  $view = views_get_view('projects_search_result');
  $tmpview = $view->clone_view();
  $tmpview->set_display('panel_pane_1');
  $args = _ampp_get_search_arguments(TRUE);
  $tmpview->set_arguments($args);
  $tmpview->pre_execute();
  $tmpview->execute();
  $output = $tmpview->total_rows;

  if ($display_amount) {
    $amount_commitment = amp_get_transactions_amount(AMP_TRANSACTION_TYPE_COMMITMENT);

    $t_args = array(
      '@amount' => format_number($amount_commitment, 0),
      '@currency' => variable_get('currency_code', ''),
    );
    $output .= ' (' . t('Commitment amount: @amount @currency', $t_args) . ')';
  }

  if ($JSON) {
    $response = array('count' => $output);
    drupal_json_output($response);
    drupal_exit();
  }

  return $output;
}

/**
 * Callback handler for project search form autocomplete widgets.
 *
 * @TODO: Remove old url mapping.
 */
function ampp_search_autocomplete_callback($type = NULL) {
  global $amp_db_exists;
  $items = array();

  if (!$amp_db_exists) {
    // Go away if the AMP database is not initialized.
    drupal_json_output($items);
    exit;
  }

  $old_menu_url_mapping = array(
    'sectors' => 'ampp_search_sectors',
    'primarysectors' => 'ampp_search_primarysectors',
    'secondarysectors' => 'ampp_search_secondarysectors',
    'programs' => 'ampp_search_programs',
    'donors' => 'ampp_search_donors',
    'agencies' => 'ampp_search_agencies',
    'regions' => 'ampp_search_regions',
  );

  if (empty($type) || empty($old_menu_url_mapping[$type])) {
    drupal_json_output($items);
    exit;
  }

  if (function_exists($old_menu_url_mapping[$type])) {
    $items = $old_menu_url_mapping[$type]();
  }

  drupal_json_output($items);
  exit;
}


/**
 * Autocomplete callback handler for sectors search
 */
function ampp_search_sectors() {
  $string = $_POST['string'];
  $items = array();
  db_set_active("amp_projects");

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

  $query = db_select('amp_sector', 'sector');
  $query->fields('sector', array('amp_sector_id'));
  $query->addExpression($translate_field, 'name');
  $query->condition('cc.name', 'Primary', '=');
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->addJoin('LEFT', 'amp_sector_scheme', 'ss', 'sector.amp_sec_scheme_id = ss.amp_sec_scheme_id');
  $query->addJoin('LEFT', 'amp_classification_config', 'cc', 'cc.classification_id = ss.amp_sec_scheme_id');

  $result = $query->execute();
  foreach ($result as $record) {
    $items[] = array('id' => $record->amp_sector_id, 'name' => check_plain($record->name));
  }
  db_set_active();

  return $items;
}


/**
 * Autocomplete callback handler for primarysectors search
 */
function ampp_search_primarysectors() {
  $string = $_POST['string'];
  $items = array();
  db_set_active("amp_projects");

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

  $query = db_select('amp_sector', 'sector');
  $query->fields('sector', array('amp_sector_id'));
  $query->addExpression($translate_field, 'name');
  $query->condition('cc.name', 'Primary', '=');
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->addJoin('LEFT', 'amp_sector_scheme', 'ss', 'sector.amp_sec_scheme_id = ss.amp_sec_scheme_id');
  $query->addJoin('LEFT', 'amp_classification_config', 'cc', 'cc.classification_id = ss.amp_sec_scheme_id');

  $result = $query->execute();
  foreach ($result as $record) {
    $items[] = array('id' => $record->amp_sector_id, 'name' => check_plain($record->name));
  }
  db_set_active();

  return $items;
}


/**
 * Autocomplete callback handler for secondarysectors search
 */
function ampp_search_secondarysectors() {
  $string = $_POST['string'];
  $items = array();
  db_set_active("amp_projects");

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

  $query = db_select('amp_sector', 'sector');
  $query->fields('sector', array('amp_sector_id'));
  $query->addExpression($translate_field, 'name');
  $query->condition('cc.name', 'Secondary', '=');
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->addJoin('LEFT', 'amp_sector_scheme', 'ss', 'sector.amp_sec_scheme_id = ss.amp_sec_scheme_id');
  $query->addJoin('LEFT', 'amp_classification_config', 'cc', 'cc.classification_id = ss.amp_sec_scheme_id');

  $result = $query->execute();
  foreach ($result as $record) {
    $items[] = array('id' => $record->amp_sector_id, 'name' => check_plain($record->name));
  }
  db_set_active();

  return $items;
}


/**
 * Autocomplete callback handler for programs search
 */
function ampp_search_programs() {
  $string = $_POST['string'];
  $items = array();
  db_set_active("amp_projects");

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpTheme', 'name', program.amp_theme_id, 'amp_theme', 'name', 'amp_theme_id', '" . $language->language . "')";

  $query = db_select('amp_theme', 'program');
  $query->fields('program', array('amp_theme_id'));
  $query->addExpression($translate_field, 'name');

  $root_pid = variable_get('amp_programs_root', 0);
  if ($root_pid) {
    // we need to get all parent IDs to limit the search to those
    $rootline = hs_select_programs_rootline($root_pid);
    if (!empty($rootline)) $query->condition('program.parent_theme_id', $rootline, 'IN');
  } else {
    $query->where('getprogramsettingid(program.amp_theme_id) = ' . variable_get('amp_programs_level', 2));
  }
  if (variable_get('amp_limit_programs_to_activities', 1)) {
    $query->join('amp_activity_program', 'activity', 'activity.amp_program_id = program.amp_theme_id');
  }
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->orderBy($translate_field, 'ASC');

  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_search_programs', dpq($query, 1));

  $result = $query->distinct()->execute()->fetchAll();
  foreach ($result as $record) {
    $items[] = array('id'=>$record->amp_theme_id, 'name'=>check_plain($record->name));
  }
  db_set_active();

  return $items;
}


/**
 * Autocomplete callback handler for donors search
 */
function ampp_search_donors() {
  $string = $_POST['string'];
  $items = array();
  db_set_active("amp_projects");

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', orgs.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  $query = db_select('amp_organisation', 'orgs');
  $query->fields('orgs', array('amp_org_id', 'acronym'));
  $query->addExpression($translate_field, 'name');
  $query->condition(db_or()->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')')->where('unaccent(orgs.acronym) ILIKE unaccent(\'%' . db_like($string) . '%\')'));
  if (variable_get('amp_show_only_donors', 0)) {
    $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = orgs.amp_org_id');
    $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
  }
  $query->condition(db_or()->condition('orgs.deleted', 'true', '<>')->condition('orgs.deleted', NULL, 'is'));
  $query->groupBy('orgs.amp_org_id');
  $query->groupBy($translate_field);
  $query->groupBy('orgs.acronym');

  $result = $query->execute();

  foreach ($result as $record) {
    $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
    $items[] = array('id'=>$record->amp_org_id, 'name'=>$name);
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for agencies search
 */
function ampp_search_agencies() {
  $string = $_POST['string'];
  $items = array();
  db_set_active("amp_projects");

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', agency.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  // Get the role ID for the "Beneficiary Agency" role.
  // SELECT amp_role_id FROM amp_role WHERE role_code = 'BA';
  $ba_role_id = db_select('amp_role', 'ar')
    ->condition('ar.role_code', 'BA', '=')
    ->fields('ar', array('amp_role_id'))
    ->execute()
    ->fetchField();

  // Get the list of organizations with the "Beneficiary Agency" role.
  // SELECT DISTINCT amp_org_id, name,role FROM amp_organisation orgs LEFT JOIN amp_org_role aor ON aor.organisation = orgs.amp_org_id WHERE aor.role = 5;
  $query = db_select('amp_organisation', 'agency');
  $query->leftJoin('amp_org_role', 'aor', 'aor.organisation = agency.amp_org_id');
  $query->distinct();
  $query->fields('agency', array('amp_org_id', 'acronym'));
  $query->fields('aor', array('role'));
  $query->addExpression($translate_field, 'name');
  $query->condition('aor.role', $ba_role_id, '=');

  // Search for the provided string.
  $query_or = db_or();
  $query_or->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query_or->where('unaccent(agency.acronym) ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->condition($query_or);

  $result = $query->execute();

  foreach ($result as $record) {
    $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
    $items[] = array('id'=>$record->amp_org_id, 'name'=>$name);
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for region search
 */
function ampp_search_regions() {
  $string = $_POST['string'];
  db_set_active("amp_projects");

  // We retrieve the country setting for the AMP installation
  $query = db_select('amp_global_settings', 'setting');
  $query->fields('setting', array('settingsvalue'));
  $query->condition('setting.settingsname', 'Default Country', '=');
  $defaultCountry = $query->execute()->fetchCol();
  if (!empty($defaultCountry)) {
    $defaultCountry = current($defaultCountry);
  }

  $items = array();

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpCategoryValueLocations', 'name', loc.id, 'amp_category_value_location', 'location_name', 'id', '" . $language->language . "')";

  if (empty($defaultCountry)) {
    $query =  "SELECT id, " . $translate_field . " as location_name FROM {amp_category_value_location} as loc WHERE unaccent(" . $translate_field . ") ILIKE unaccent(:string)";
    $result = db_query($query, array(':string' => "%" . $string . "%"));
  } else {
    // Include only subnational locations if the AMP installation has a Default Country set
    $query = "SELECT id, " . $translate_field . " as location_name FROM {amp_category_value_location} as loc WHERE unaccent(" . $translate_field . ") ILIKE unaccent(:string) AND parent_location IS NOT NULL";
    $result = db_query($query,  array(':string' => "%" . $string . "%"));
  }

  foreach ($result as $record) {
    $parents = get_location_path($record->id);
    if (!empty($parents)) {
      $name = $parents . ' -> ' . $record->location_name;
    } else {
      $name = $record->location_name;
    }
    $items[] = array('id' => $record->id, 'name' => check_plain($name));
  }
  db_set_active();

  return $items;
}


/*
 * Get currency code
 */
function ampp_projects_check_currency_code() {
  global $amp_db_exists;
  if (!$amp_db_exists) return;

  db_set_active('amp_projects');
  $currency_code = db_select('amp_global_settings', 'settings')
    ->condition('settings.settingsname', 'Base Currency', "=")
    ->fields('settings',array('settingsvalue'))
    ->execute()
    ->fetchField();
  db_set_active();

  $current_code = variable_get('currency_code', NULL);
  if ($current_code != $currency_code) {
    variable_set('currency_code', $currency_code);
  }
}

/**
 *
 */
function get_location_path($childLocationId, $depth = 999) {

  // we need to show the full hierarchy but not country

  global $amp_db_exists;
  global $language;
  global $user;

  if (!$amp_db_exists) return;

  $allParentIds = array();

  db_set_active("amp_projects");

  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpCategoryValueLocations', 'parent_name', names.id, 'amp_category_value_location', 'location_name', 'id', '" . $language->language . "')";

  $query = db_select('amp_category_value_location', 'loc');
  $query->fields('loc', array('parent_location', 'parent_category_value'));
  $query->addExpression($translate_field, 'parent_name');
  $query->join('amp_category_value_location', 'names', 'names.id = loc.parent_location');
  $query->join('amp_category_value', 'cats', 'cats.id = names.parent_category_value');
  $query->condition('loc.id', $childLocationId, '=');
  $query->condition('cats.category_value', 'Country', '!=');
  $query->isNotNull('loc.parent_location');

  $result = $query->execute();
  $parents = '';
  foreach ($result as $record) {
    if ($depth != 0) {
      $parents = get_location_path($record->parent_location, $depth - 1) . '->' . $record->parent_name;
    }
    $parents = $record->parent_name;
  }

  db_set_active();

  return $parents;
}


/*
 *
 */
function ampp_search_ajax_sectors_form() {
  $form = array();
  $form['sectores'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
        'module' => 'hs_select_sectors',
        'params' => array(
          'id' => 0,
          'pid' => 0,
          'separator' => '__',
        ),

        'save_lineage' => 1,
        'enforce_deepest' => 0,
        'entity_count' => 0,
        'require_entity' => 0,
        'level_labels' => array('status' => 0),
        'dropbox' => array('status' => 1, 'limit' => 0),
        'editability' => array('status' => 1, 'max_levels' => 2),
        'resizable' => 1,
        'render_flat_select' => 0
      ),
  );
  return $form;
}


/*
 *
 */
function ampp_search_ajax_primarysectors_form() {
  $form = array();
  $form['primarysectors'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
        'module' => 'hs_select_primarysectors',
        'params' => array(
          'id' => 0,
          'pid' => 0,
          'separator' => '__',
        ),

        'save_lineage' => 1,
        'enforce_deepest' => 0,
        'entity_count' => 0,
        'require_entity' => 0,
        'level_labels' => array('status' => 0),
        'dropbox' => array('status' => 1, 'limit' => 0),
        'editability' => array('status' => 1, 'max_levels' => 2),
        'resizable' => 1,
        'render_flat_select' => 0
      ),
  );
  return $form;
}


/*
 *
 */
function ampp_search_ajax_secondarysectors_form() {
  $form = array();
  $form['secondarysectors'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
        'module' => 'hs_select_secondarysectors',
        'params' => array(
          'id' => 0,
          'pid' => 0,
          'separator' => '__',
        ),

        'save_lineage' => 1,
        'enforce_deepest' => 0,
        'entity_count' => 0,
        'require_entity' => 0,
        'level_labels' => array('status' => 0),
        'dropbox' => array('status' => 1, 'limit' => 0),
        'editability' => array('status' => 1, 'max_levels' => 2),
        'resizable' => 1,
        'render_flat_select' => 0
      ),
  );
  return $form;
}


/*
 *
 */
function ampp_search_ajax_programs_form() {
  $form = array();
  $form['programs'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
        'module' => 'hs_select_programs',
        'params' => array(
          'id' => 0,
          'pid' => variable_get('amp_programs_root', 0),
          'separator' => '__',
        ),

        'save_lineage' => 1,
        'enforce_deepest' => 0,
        'entity_count' => 0,
        'require_entity' => 0,
        'level_labels' => array('status' => 0),
        'dropbox' => array('status' => 1, 'limit' => 0),
        'editability' => array('status' => 1, 'max_levels' => 2),
        'resizable' => 1,
        'render_flat_select' => 0
      ),
  );
  return $form;
}


/*
 *
 */
function ampp_search_ajax_locations_form() {
  $form = array();
  $form['regions'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
        'module' => 'hs_select_locations',
        'params' => array(
          'id' => 0,
          'pid' => 0,
          'separator' => '__',
        ),

        'save_lineage' => 1,
        'enforce_deepest' => 0,
        'entity_count' => 0,
        'require_entity' => 0,
        'level_labels' => array('status' => 0),
        'dropbox' => array('status' => 1, 'limit' => 0),
        'editability' => array('status' => 1, 'max_levels' => 2),
        'resizable' => 1,
        'render_flat_select' => 0
      ),
  );
  return $form;
}


/*
 *
 */
function ampp_search_hs_dropbox_values() {
  $form_build_id = $_POST['form_build_id'];
  $form_id = $_POST['form_id'];


  $form = 'form_' . $form_build_id;
  $form_cache = cache_get($form, 'cache_form');
  $hs_selects_callbacks = array('sectores' => 'hs_select_sectors_get_item_name',
                  'primarysectors' => 'hs_select_primarysectors_get_item_name',
                  'secondarysectors' => 'hs_select_secondarysectors_get_item_name',
                  'programs' => 'hs_select_programs_get_item_name',
                  'donors' => 'ampp_projects_get_donor',
                  'regions' => 'hs_select_locations_get_item_name',
                 );
  $callback = '';

  $form_state = 'form_state_' .$form_build_id;
  $cache = cache_get($form_state, 'cache_form');
  if($form_cache->data) {
    foreach ($hs_selects_callbacks as $key => $func) {
      if(array_key_exists($key, $form_cache->data)) {
        $callback = $func;
        break;
      }
    }
    //if(array_key_exists($key, $search))
  }
  $json = new stdClass();
  $json->data = '';
  if(empty($callback) || !function_exists($callback)) {
    $json->error = 'callback does not exists';
    print drupal_json_encode($json);
    return ;
  }


  if(!empty($cache)) {
    $line_ages = $cache->data['storage']['hs'][0]['dropbox_lineages_selections'];
    $result = array();
    if(!empty($line_ages)) {
      foreach ($line_ages as $key => $value) {
        foreach ($value as $sub_key => $sub_value)
          $item = $callback($sub_value);
          if(!empty($item))
            $result[] = $item[0];
      }
    }

    if(!empty($result)) {
      $json->data = $result;
    }
  }
  print drupal_json_encode($json);
}


/*
 *
 */
function ampp_search_ajax_donors_form() {
  $form = array();
  $hierarchy = array();
  $hierarchy = ampp_projects_get_donors();

  $form['donors'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 10,
    '#config' => array(
        'module' => 'hs_smallhierarchy',
        'params' => array(
          'hierarchy' => $hierarchy,
          'separator' => '_',
        ),
        'save_lineage' => 1,
        'enforce_deepest' => 0,
        'entity_count' => 0,
        'require_entity' => 0,
        'level_labels' => array('status' => 0),
        'dropbox' => array('status' => 1, 'limit' => 0),
        'editability' => array('status' => 1, 'max_levels' => 1),
        'resizable' => 0,
        'render_flat_select' => 0
      ),
  );

  return $form;
}


/*
 *
 */
function ampp_projects_get_donor($id) {
  db_set_active('amp_projects');

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', orgs.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  $query = db_select('amp_organisation', 'orgs')->fields('orgs', array('amp_org_id', 'acronym'));
  $query->addExpression($translate_field, 'name');
  if (variable_get('amp_show_only_donors', 0)) {
    $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = orgs.amp_org_id');
    $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
  }
  $query->condition('orgs.amp_org_id', intval($id), '=');
  $or_statement = db_or()->condition('orgs.deleted', 'true', '<>')->condition('orgs.deleted', NULL, 'is');
  $query->condition($or_statement);

  $result = $query->execute();
  $info = array();
  foreach ($result as $record) {
    $info[] = array('id' => $record->amp_org_id, 'name' => $record->name);
  }
  db_set_active();

  return $info;
}


/*
 *
 */
function ampp_projects_get_donors() {
  db_set_active('amp_projects');

  global $language;
  global $user;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', orgs.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  $query = db_select('amp_organisation', 'orgs');
  $query->fields('orgs', array('amp_org_id', 'acronym'));
  $query->addExpression($translate_field, 'name');
  if (variable_get('amp_show_only_donors', 0)) {
    $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = orgs.amp_org_id');
    $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
  }
  $or_statement = db_or()->condition('orgs.deleted', 'true', '<>')->condition('orgs.deleted', NULL, 'is');
  $query->condition($or_statement);
  $query->orderBy('name', 'ASC');

  $result = $query->execute();

  $hierarchy = array();
  foreach ($result as $record) {
  $hierarchy[$record->amp_org_id] = array('label' => $record->name);
  }

  db_set_active();

  return $hierarchy;
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


/*
 *
 */
function activities_period_range() {
  db_set_active("amp_projects");
  $items = array();
  $query = db_select('cached_amp_activity', 'activity')
    ->fields('activity', array('actual_start_date'))
    ->condition('actual_start_date', '1900-01-01 00:00:00', '!=')
    ->orderby('actual_start_date', 'ASC')
    ->range(0,1);
  $min = $query->execute()->fetchCol();

  $query = db_select('cached_amp_activity', 'activity')
    ->fields('activity', array('actual_completion_date'))
    ->isNotNull('actual_completion_date')
    ->orderby('actual_completion_date', 'DESC')
    ->range(0,1);
  $max = $query->execute()->fetchCol();
  db_set_active();

  return array(date('Y', strtotime($min[0])), date('Y', strtotime($max[0])));
}

