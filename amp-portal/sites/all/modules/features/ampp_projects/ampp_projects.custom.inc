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
  if ($lang_def == '' || $lang_def->language == $language->language) {
    $lang = '';  
  } else {
    $lang = $language->language;
  }
 
  // cache_clear_all('module_implements', 'cache_bootstrap'); ///////////// ! Cache
  getOnOffBudgetIDs(); // Get the on/off budget projects ids

  drupal_add_js(drupal_get_path('module', 'ampp_projects') . '/tokeninput/src/jquery.tokeninput.js');
  drupal_add_js(array('ampp_projects' => array('lang' => $lang)), 'setting');
  drupal_add_js(drupal_get_path('module', 'ampp_projects') . '/ampp_projects.js');  
  
  $ampp_projects_settings = array(
  	'url' => url('ampp_search/hs-dropbox')
  );
  
  drupal_add_js(array('ampp_projects' => $ampp_projects_settings), 'setting');
  drupal_add_css("div.hidden-locations, #loading-count {display:none;} span.more-locations {cursor: pointer;}",array('type' => 'inline'));
  drupal_add_library('system', 'ui.dialog');
  //Let's check if AMP database is configured and if not throw a worning message and also do not try to connect to it later
  amp_db_exists();
  variable_set('currency_code', ampp_projects_get_currency_code());
}


/**
 * Checking if AMP database is configured
 */
function amp_db_exists() {
  global $amp_db_exists;
  
  global $databases;
  if (isset($databases['amp_projects']) && is_array($databases['amp_projects'])) $amp_db_exists = TRUE;
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
 * Implementation of hook_views_data
 */
function ampp_projects_views_data() {

  // ************* Activity  ****************** //
  
  $data['cached_amp_activity']['table']['group'] = t('AMP Projects');
  $data['cached_amp_activity']['table']['base'] = array(
    'field' => 'amp_activity_id',
    'title' => t('AMP Projects'),
    'help' => t('Table help description'),
    'database' => 'amp_projects'
  );
  
  $data['cached_amp_activity']['amp_activity_id'] = array(
	'title' => t('Id'),
	'help' => t('Id for the project'),
	'field' => array('handler' => 'ampp_projects_handler_activity_id'),
	'argument' => array('handler' => 'views_handler_argument_numeric'),
	'filter' => array('handler' => 'views_handler_filter_amp_activity_id'),
	'sort' => array('handler' => 'views_handler_sort_numeric'));
  
  $data['cached_amp_activity']['name'] = array(
	'title' => t('Project title'),
	'help' => t('Title for the project'),
	'field' => array('handler' => 'ampp_projects_handler_activity_name', 'click sortable' => TRUE),
	'argument' => array('handler' => 'amp_projects_handler_argument_string'), //views_handler_argument_string
	'filter' => array('handler' => 'views_handler_filter_string'),
	'sort' => array('handler' => 'views_handler_sort_string'));
  
  $data['cached_v_status']['table']['group']  = t('AMP Projects');
  $data['cached_v_status']['table']['join'] = array(
    'cached_amp_activity' => array(
        'left_field' => 'amp_activity_id',
        'field' => 'amp_activity_id'
    )
  );
  
  $data['cached_v_status']['name'] = array(
    'group' =>  t('AMP Projects'),
    'title' => t('Status'),
    'help' => t('Status name of AMP projects'),
    'field' => array('handler' => 'views_handler_field_status', 'click sortable' => TRUE),
    'sort' => array('handler' => 'views_handler_sort'),
  );
  
  $data['cached_amp_activity']['actual_start_date'] = array(
    'title' => t('Start date'),
    'help' => t('Date of the start of project'),
    'field' => array('handler' => 'ampp_projects_handler_postgre_timestamp', 'click sortable' => TRUE),
    'argument' => array('handler' => 'ampp_projects_date_views_argument_handler'),
    'filter' => array('handler' => 'views_handler_filter_date'),
    'sort' => array('handler' => 'views_handler_sort_date'));
  
  $data['cached_amp_activity']['actual_completion_date'] = array(
    'title' => t('End date'),
    'help' => t('Date of the actual completion'),
    'field' => array('handler' => 'ampp_projects_handler_postgre_timestamp', 'click sortable' => TRUE),
    'argument' => array('handler' => 'ampp_projects_date_views_argument_handler'),
    'filter' => array('handler' => 'views_handler_filter_date'),
    'sort' => array('handler' => 'views_handler_sort_date'));
  
  $data['cached_amp_activity']['draft'] = array(
      'title' => t('Draft project'),
      'help' => t('Is it draft project'),
      'filter' => array('handler' => 'views_handler_filter_boolean_operator', 'use equal' => FALSE,),
      );
      
  $data['cached_amp_activity']['approval_status'] = array(
  	'title' => t('Approval Status'),
  	'help' => t('Activity approval status'),
  	'filter' => array('handler' => 'views_handler_filter_approval_status_in_operator')
  );
  
  $data['cached_amp_activity']['amp_team_id'] = array(
  	'title' => t('Amp team id Filter'),
  	'help'  => t('Amp team id Filter'),
  	'filter' => array('handler' => 'views_handler_filter_amp_team_id')
  );
  
  // Custom (not from tabel) fields
  // Commitment Amount
  $data['cached_amp_activity']['commitment_amount'] = array(
  'title' => t('Commitment amount'),
  'help' => t('Transaction Commitment amount'),
  'field' => array('handler' => 'views_handler_field_amount_commitment', 'float' => TRUE,),
  'argument' => array('handler' => 'views_handler_argument_numeric'),
  'filter' => array('handler' => 'views_handler_filter_numeric'),
  'sort' => array('handler' => 'views_handler_sort_numeric'));
  
  // Disbursement amount
  $data['cached_amp_activity']['disbursement_amount'] = array(
    'title' => t('Disbursement amount'),
	'help' => t('Transaction Disbursement amount'),
	'field' => array('handler' => 'views_handler_field_amount_disbursement', 'float' => TRUE,),
	'argument' => array('handler' => 'views_handler_argument_numeric'),
	'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric'));

  
  // ******** Sector of activity  ********* //
  
  $data['amp_activity_sector']['table']['group']  = t('AMP Projects');
  $data['amp_activity_sector']['table']['join'] = array(
	// Directly links to amp_activity table.
	'cached_amp_activity' => array(
		'left_field' => 'amp_activity_id',
		'field' => 'amp_activity_id',
	),
  );
  
  $data['amp_activity_sector']['amp_sector_id_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('Sector Ids'),
	'help' => t('Sectors Ids of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_sector_ids'),
  );
  
  $data['amp_sector']['table']['group']  = t('AMP Projects');
  $data['amp_sector']['table']['join'] = array(
	'cached_amp_activity' => array(
		'left_table' => 'amp_activity_sector',
		'left_field' => 'amp_sector_id',
		'field' => 'amp_sector_id'
	)
  );
  
  $data['amp_sector']['amp_sector_name_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('Sector Names'),
	'help' => t('Sectors names of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_sector_names'),
  );
  
  $data['amp_sector']['amp_sector_id'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Sector Id'),
	'help' => t('Sectors id of AMP project'),
	'field' => array('handler' => 'views_handler_field'),
	'argument' => array('handler' => 'views_handler_argument_sector_id'),
	//'filter' => array('handler' => 'views_handler_filter_sector_id'),
	'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric')
  );
  // Primary and secondary sectors
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
	'group' =>  t('AMP Projects'),
	'title' => t('Primary Sector Names'),
	'help' => t('Primary Sectors names of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_primarysector_names'),
  );
  
  
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
	'group' =>  t('AMP Projects'),
	'title' => t('Secondary Sector Names'),
	'help' => t('Secondary Sectors names of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_secondarysector_names'),
  );
  // ******** Program of activity  ********* //
  
  $data['amp_activity_program']['table']['group']  = t('AMP Projects');
  $data['amp_activity_program']['table']['join'] = array(
	// Directly links to amp_activity table.
	'cached_amp_activity' => array(
		'left_field' => 'amp_activity_id',
		'field' => 'amp_activity_id',
	),
  );
  
  $data['amp_activity_program']['amp_program_id_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('Program Ids'),
	'help' => t('Program Ids of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_program_ids'),
  );
  
  $data['amp_theme']['table']['group']  = t('AMP Projects');
  $data['amp_theme']['table']['join'] = array(
	'cached_amp_activity' => array(
		'left_table' => 'amp_activity_program',
		'left_field' => 'amp_program_id',
		'field' => 'amp_theme_id'
	)
  );
  
  $data['amp_theme']['amp_theme_name_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('Program Names'),
	'help' => t('Program names of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_program_names'),
  );
  
  $data['amp_theme']['amp_theme_id'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Program Id'),
	'help' => t('Program id of AMP project'),
	'field' => array('handler' => 'views_handler_field'),
	'argument' => array('handler' => 'views_handler_argument_program_id'),
	//'filter' => array('handler' => 'views_handler_filter_sector_id'),
	'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric')
  );
  
  // ******* Location of activity ******** //
  
  $data['amp_activity_location']['table']['group']  = t('AMP Projects');
  $data['amp_activity_location']['table']['join'] = array(
	// Directly links to amp_activity table.
	'cached_amp_activity' => array(
		'left_field' => 'amp_activity_id',
		'field' => 'amp_activity_id',
	),
  );
  $data['amp_activity_location']['amp_location_id_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Location Ids'),
	'help' => t('Location Ids of AMP projects'),
	'field' => array(
		'handler' => 'ampp_projects_handler_location_ids',
	),
  );
  
  $data['amp_location']['table']['group']  = t('AMP Projects');
  $data['amp_location']['table']['join'] = array(
	'cached_amp_activity' => array(
		'left_table' => 'amp_activity_location',
		'left_field' => 'amp_location_id',
		'field' => 'amp_location_id'
	)
  );
  
  $data['amp_location']['amp_location_region_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Locations Names'),
	'help' => t('Locations name of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_location_names'),
  );
  
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
  	'help'  => t('AMP Category value locations id'),
  	'field' => array('handler' => 'views_handler_field'),
  	'argument' => array('handler' => 'views_handler_argument_numeric'),
	'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric')
  );
  
  $data['amp_category_value_location']['category_location_name_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Category Locations Names'),
	'help' => t('AMP Projects category Locations  names'),
	'field' => array('handler' => 'ampp_projects_handler_category_location_names'),
  );
  
  $data['amp_location']['amp_location_id'] = array(
	'title' => t('Location Id'),
	'help' => t('Id of the location'),
	'field' => array('handler' => 'views_handler_field'),
	'argument' => array('handler' => 'views_handler_argument_numeric'), //ampp_projects_handler_argument_location
	'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric')
  );
  
  $data['amp_location']['location_id'] = array(
  	'title' => t('Locations Ids'),
  	'help' => t('List of locations'),
  	'field' => array('handler' => 'views_handler_field'),
 	'argument' => array('handler' => 'views_handler_argument_location_id'),
    'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric')
  );
  // ******* Organization/Donor of activity ******** //
  
  $data['amp_funding']['table']['group']  = t('AMP Projects');
  $data['amp_funding']['table']['join'] = array(
	// Directly links to amp_activity table.
	'cached_amp_activity' => array(
		'left_field' => 'amp_activity_id',
		'field' => 'amp_activity_id',
				
	),
  );
  $data['amp_funding']['amp_org_id_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Organizations Ids'),
	'help' => t('Organizations Ids of AMP projects'),
	'field' => array(
			'handler' => 'ampp_projects_handler_org_ids',
	),
  );
  
  $data['amp_organisation']['table']['group']  = t('AMP Projects');
  $data['amp_organisation']['table']['join'] = array(
	'cached_amp_activity' => array(
    	'left_table' => 'amp_funding',
    	'left_field' => 'amp_donor_org_id',
    	'field' => 'amp_org_id'
	)
  );
  
  $data['amp_organisation']['amp_org_name_agg'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Organizations Names'),
	'help' => t('Locations name of AMP projects'),
	'field' => array('handler' => 'ampp_projects_handler_org_names'),
  );
  
  $data['amp_organisation']['amp_org_id'] = array(
	'title' => t('Organization Id'),
	'help' => t('Id of the Organization'),
	'field' => array('handler' => 'views_handler_field'),
	'argument' => array('handler' => 'views_handler_argument_numeric'),
	'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric')
  );
  
  
  $data['amp_activities_categoryvalues']['table']['group']  = t('AMP Projects');
  $data['amp_activities_categoryvalues']['table']['join'] = array(
	'cached_amp_activity' => array(
		'left_field' => 'amp_activity_id',
		'field' => 'amp_activity_id',
		'type' => 'INNER',
	)
  );
  
  $data['amp_activities_categoryvalues']['amp_categoryvalue_id'] = array(
	'group' =>  t('AMP Projects'),
	'title' => t('AMP Projects Budget'),
	'help' => t('Budget type'),
	'field' => array('handler' => 'views_handler_field'),
	'argument' => array('handler' => 'views_handler_argument_numeric'),
	'filter' => array('handler' => 'views_handler_filter_numeric'),
	'sort' => array('handler' => 'views_handler_sort_numeric')
  );
  
  return $data;
}


/**
 * Sectors JSON array from URL
 * @return multitype:mixed
 */
function ampp_get_sectors_from_url() {
  global $amp_db_exists;
  if (!$amp_db_exists) return;

  $sectors = array();
  $sectors_ids = isset($_GET["sectors"]) ? $_GET["sectors"] : 'all';
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
       
    if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_get_sectors_from_url', dpq($query, 1));
    
    $result = $query->execute();    
    foreach ($result as $record) {
      $sectors[] = drupal_json_encode(array('id'=>$record->amp_sector_id, 'name' => addslashes(check_plain($record->name))));
    }
    db_set_active();
  } 
  return $sectors;
}


/*
 *
 */
function ampp_get_primarysectors_from_url() {
  global $amp_db_exists;
  if (!$amp_db_exists) return;

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
      $sectors[] = drupal_json_encode(array('id'=>$record->amp_sector_id, 'name' => addslashes(check_plain($record->name))));
    }
    db_set_active();
  } 
  return $sectors;
}


/*
 *
 */
function ampp_get_secondarysectors_from_url() {
  global $amp_db_exists;
  if (!$amp_db_exists) return;

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
      $sectors[] = drupal_json_encode(array('id'=>$record->amp_sector_id, 'name' => addslashes(check_plain($record->name))));
    }
    db_set_active();
  } 
  return $sectors;
}


/*
 *
 */
function ampp_get_programs_from_url() {
  global $amp_db_exists;
  if (!$amp_db_exists) return;

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
      $programs[] = drupal_json_encode(array('id'=>$record->amp_theme_id, 'name'=>addslashes(check_plain($record->name))));
    }
    db_set_active();
  } 
  return $programs;
}


/*
 *
 */
function ampp_get_keywords_from_url($source_arg = TRUE) {
	$keywords = isset($_GET["keywords"]) ? $_GET["keywords"] : 'all';
	if(!$source_arg) {
		$keywords = 'all';
	}
	
	if($keywords == 'all')
		return '';
		
	return array("$keywords");
}


/**
 * Donors JSON array from URL
 * @return multitype:mixed
 */
function ampp_get_donors_from_url() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

  $donors = array();
  $donors_ids = isset($_GET["donors"]) ? $_GET["donors"] : 'all';
  
  db_set_active("amp_projects");
  $items = array();
  if ($donors_ids != 'all' && !empty($donors_ids)) {
    
    global $language;
    global $user;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', donor.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')"; 
    
    $query = db_select('amp_organisation', 'donor');
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
      $donors[] = drupal_json_encode(array('id' => $record->amp_org_id, 'name' => $name));
    }
    
  }
  
  db_set_active();
  return $donors;
}


/**
 * Locations JSON array from URL
 */
function ampp_get_locations_from_url() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

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
    
    if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_get_locations_from_url', dpq($query, 1));
    
    $result = $query->execute();
    foreach ($result as $record) {
      $parents = get_location_path($record->id);
      if (!empty($parents)) {
        $name = $parents . ' -> ' . $record->location_name;
      } else {
        $name = $record->location_name;
      }
      $locations[] = drupal_json_encode(array('id' => $record->id, 'name' => check_plain($name)));
    }
    db_set_active();
  }
  return $locations;
}


/**
 * Period JSON array from URL
 */
function ampp_get_period_from_url() {

  if( isset($_GET["period_start"]) && isset($_GET["period_end"])) {
    $period_start = $_GET["period_start"];
    $period_end = $_GET["period_end"];
    return array($period_start, $period_end);
  }
  return '';
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
 * Get block content with total amounts
 */
function total_block_content() {
  $amount_commitment = get_total_amount(0);
  $amount_disbursement = get_total_amount(1);
  return total_block_html($amount_commitment, $amount_disbursement);
}


/**
 * Get total amount of given type (1|0)
 * Use url as arguments.
 * TODO: Find a way to use views to get totals
 */
function get_total_amount($type) {

  global $user;
  global $amp_db_exists;
  if (!$amp_db_exists) return;
  
  $donors_ids = array();
  if (isset($_GET["donors"])) {
    $donors_ids = $_GET["donors"];
    $donor_ids_array = explode(",", $donors_ids);
  }
  
  $sectors_ids = array();
  if (isset($_GET["sectors"])) {
    $sectors_ids = $_GET["sectors"];
    $sector_ids_array = explode(",", $sectors_ids);
  }
  
  $sectors_array = array();
  $sectors_ids = empty($_GET["sectors"]) ? '' : $_GET["sectors"];
  if ($sectors_ids != 'all' and !empty($sectors_ids)){
	$sectors_array = explode(",", $sectors_ids);
  }

  $sectors_ids = empty($_GET["primarysectors"]) ? '' : $_GET["primarysectors"];
  if ($sectors_ids != 'all' and !empty($sectors_ids)){
	$sectors_array = array_merge($sectors_array, explode(",",$sectors_ids));
  }

  $sectors_ids = empty($_GET["secondarysectors"]) ? '' : $_GET["secondarysectors"];
  if ($sectors_ids != 'all' and !empty($sectors_ids)) {
  	$sectors_array = array_merge($sectors_array, explode(",",$sectors_ids));
  }
  
  $sector_ids_array = $sectors_array;
  
  $programs_ids = array();
  if (isset($_GET["programs"])) {
    $programs_ids = $_GET["programs"];
    $program_ids_array = explode(",", $programs_ids);
  }

  $locations_ids = array();
  if (isset($_GET["locations"])) {
    $locations_ids = $_GET["locations"];
    $location_ids_array = explode(",", $locations_ids);
  }
  
  $period_start = isset($_GET["period_start"]) ? $_GET["period_start"] : '';
  $period_end = isset($_GET["period_end"]) ? $_GET["period_end"] : '';
  
  $keywords = isset($_GET["keywords"]) ? $_GET["keywords"] : '';
  
  db_set_active("amp_projects");
  
  // Start constructing the SQL query
  $query = db_select('amp_funding_detail', 'fd');
  
  $query->condition('fd.transaction_type', $type, "=")->addExpression('SUM(fd.transaction_amount / getexchange(cur.currency_code, fd.transaction_date ))', 'transaction_amount');
  if ($donors_ids != 'all' and !empty($donor_ids_array[0])){
      $query->condition('f.amp_donor_org_id', $donor_ids_array, "IN");
  }
  $query->addJoin('LEFT', 'amp_funding', 'f', 'f.amp_funding_id = fd.amp_funding_id');
  $query->addJoin('LEFT', 'cached_amp_activity', 'activity', 'activity.amp_activity_id = f.amp_activity_id');
  $query->addJoin('LEFT', 'amp_currency', 'cur', 'fd.amp_currency_id = cur.amp_currency_id');
  
  $snippet  = "activity.amp_team_id is not null";
  $query->where($snippet);
  
  $snippet  = "activity.approval_status IN ('approved', 'startedapproved')";
  $snippet .= " AND activity.draft <> true"; 
  $snippet .= " AND activity.amp_team_id IN (select amp_team_id from amp_team where parent_team_id IN (select amp_team_id from amp_team where access_type = 'Management'))"; 
  $snippet .= " OR (";
  $snippet .= "activity.approval_status IN ('approved', 'startedapproved')";
  $snippet .= " AND activity.draft <> true";
  $snippet .= " AND activity.amp_activity_id IN (select distinct(activity) from amp_org_role where organisation IN (select amp_org_id from amp_team_orgs where amp_team_id IN (select amp_team_id from amp_team where parent_team_id in (select amp_team_id from amp_team where access_type='Management')))))";
      
  $query->where($snippet);
  
  if ($sectors_ids != 'all' and !empty($sector_ids_array[0])){
    foreach ($sector_ids_array as $sector_id){
      $sector_ids_array = array_merge($sector_ids_array, getChildSectorsIds($sector_id));
    }
    $query->addJoin('LEFT', 'amp_activity_sector', 'sector', 'sector.amp_activity_id = activity.amp_activity_id');
    $query->condition('sector.amp_sector_id', $sector_ids_array, "IN");
  }
  
  if ($programs_ids != 'all' and !empty($program_ids_array[0])){
    foreach ($program_ids_array as $program_id){
      $program_ids_array = array_merge($program_ids_array, getChildProgramsIds($program_id));
    }
	$query->addJoin('LEFT', 'amp_activity_program', 'program', 'program.amp_activity_id = activity.amp_activity_id');
	$query->condition('program.amp_program_id', $program_ids_array, "IN");
  }
  
  $regionalprojects = isset($_GET["showregionalprojects"]) ? $_GET["showregionalprojects"] : 0;
  if ($regionalprojects == 1) {
  	   if($locations_ids != 'all' && !empty($location_ids_array[0])){
  	   	   	$tmp_location_ids_array = $location_ids_array;
		    foreach ($tmp_location_ids_array as $id){
		      $location_ids_array = array_merge($location_ids_array, getParentLocationIds($id));
		    }
  	   }
  }
  
  if ($locations_ids != 'all' and !empty($location_ids_array[0])){
    $query->addJoin('LEFT', 'amp_activity_location', 'al', 'al.amp_activity_id = activity.amp_activity_id');
    $query->addJoin('LEFT', 'amp_location', 'am_loc', 'am_loc.amp_location_id=al.amp_location_id');
    $query->condition('am_loc.location_id', $location_ids_array, "IN");
  }
  
  $budgetflag = isset($_GET["budget"]) ? $_GET["budget"] : '';
  if (!empty($budgetflag) && $budgetflag != "all" ) {	 
	$catvalue = intval( $budgetflag );
	$query->addJoin('INNER', 'amp_activities_categoryvalues', 'acv', 'acv.amp_activity_id = activity.amp_activity_id');
	$query->condition('acv.amp_categoryvalue_id', $catvalue, '=');
  }
  
  if ($keywords !== 'all' && !empty($keywords)) {
    $query->where('unaccent(activity.name) ILIKE unaccent(\'%' . db_like($keywords) . '%\')');
  }

  //condition for start and end date period
  if (!empty($period_start) && $period_start != "all" ) {
    $query->condition('activity.actual_start_date', $period_start, '>=');
  }
  if (!empty($period_end) && $period_end != "all" ) {
    $query->condition('activity.actual_completion_date', $period_end, '<=');
  }
  
  $result = $query->execute()->fetchAssoc();
  
  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('get_total_amount', dpq($query, 1));
  
  db_set_active();
  return $result['transaction_amount'] ;
}


/**
 * HTML of totals block
 */
function total_block_html($amount_commitment, $amount_disbursement){
  if (module_exists('format_number')) {
    $amount_commitment = empty($amount_commitment) ? "0" : format_number($amount_commitment, 0);
    $amount_disbursement = empty($amount_disbursement) ? "0" : format_number($amount_disbursement, 0);
  } else {
    $amount_commitment = empty($amount_commitment) ? "0" : number_format(intval($amount_commitment));
    $amount_disbursement = empty($amount_disbursement) ? "0" : number_format(intval($amount_disbursement));
  }
  $html = '<div class="total-amounts view-projects-search-result">';
  $html .= '<table class="views-table"><thead><tr>';
  $html .= '<th class="views-field"></th>';
  $html .= '<th  class="views-field">'.t('Commitment amount').' ('.variable_get('currency_code').")".'</th>';
  $html .= '<th class="views-field">'.t('Disbursement amount').' ('.variable_get('currency_code').")".'</th>';
  $html .= '</tr></thead>';
  
  $html .= '<td>'.t("Total").':'.'</td>';
  $html .= '<td class="views-field">'.$amount_commitment.'</td>';
  $html .= '<td class="views-field">'.$amount_disbursement.'</td>';
  $html .= '</tr></thead>';
    
  $html .= '</table>';
  $html .= '</div>';
  return $html;
}


/**
 * Implementation of hook_block_info
 */
/*
function ampp_projects_block_info() {
  $blocks['ampp_search'] = array(
    'info' => t('Search AMP Projects'), 
  );
  
  $blocks['ampp_search_result_totals'] = array(
      'info' => t('Search result totals'),
  );
  return $blocks;
}
*/


/**
 * 
 * Implements hook_block_configure().
 */
function ampp_projects_block_configure($delta) {
  $form = array();
  switch($delta) {
    case 'ampp_search_result_totals':
      $form['result_totals_set_precision'] = array(
          '#type' => 'checkbox',
          '#title' => t('Round'),
          '#description' => t('If checked, the number will be rounded.'),
          '#default_value' => variable_get('result_totals_set_precision', 1),
      );
      $form['result_totals_precision'] = array(
          '#type' => 'textfield',
          '#title' => t('Precision'),
          '#default_value' => variable_get('result_totals_precision'),
          '#description' => t('Specify how many digits to print after the decimal point.'),
          '#dependency' => array('edit-options-set-precision' => array(TRUE)),
          '#size' => 2,
      );
  return $form;
  }
}


/**
 *
 * Implements hook_block_save().
 */
function ampp_projects_block_save($delta = '', $edit = array()){
  switch($delta) {
    case 'ampp_search_result_totals':
      variable_set('result_totals_set_precision', (int)$edit['result_totals_set_precision']);
      variable_set('result_totals_precision', (int)$edit['result_totals_precision']);
    break;
  }
  return;
}


/**
 * Implementation of hook_block_view
 */
function ampp_projects_block_view($delta=''){
  switch($delta) {
    case 'ampp_search':
     // $block['subject'] = t('Search Projects'); // Most forms don't have a subject 
    //  $block['content'] = drupal_get_form('ampp_search_form');
      
      break;
    case 'ampp_search_result_totals':
      $block['content'] = total_block_content();
      break;
  }
  return $block;
}


/**
 * Implementation of hook__menu
 * 
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
  
  $items['ampp_search/sectors'] = array(
    'page callback' => 'ampp_search_sectors',  	
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );
  
  $items['ampp_search/primarysectors'] = array(
    'page callback' => 'ampp_search_primarysectors',  	
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );
  
  $items['ampp_search/secondarysectors'] = array(
    'page callback' => 'ampp_search_secondarysectors',  	
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );
  
  $items['ampp_search/programs'] = array(
    'page callback' => 'ampp_search_programs',  	
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );
  
  $items['ampp_search/donors'] = array(
    'page callback' => 'ampp_search_donors',  	
    'access callback' => TRUE,
    'type' => MENU_CALLBACK,
  );
  
  $items['ampp_search/regions'] = array(
    'page callback' => 'ampp_search_regions',  	
    'access callback' => TRUE,
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
function ampp_search_result_count($JSON = true){

  global $amp_db_exists;
  if (!$amp_db_exists) return;


  $sectors_array = array();     
  if (empty($_GET["currentSectors"])) {
    $sectors_ids = !empty($_GET["sectors"]) ? $_GET["sectors"] : "all";
  } else {
    $sectors_ids = implode(',', $_GET["currentSectors"]);
    if (empty($sectors_ids)) $sectors_ids = "all";
  }
  if($sectors_ids != 'all' && !empty($sectors_ids)){
    $sectors_array = explode(",", $sectors_ids);
  }
  if (empty($_GET["currentPrimarySectors"])) {
    $sectors_ids = !empty($_GET["primarysectors"]) ? $_GET["primarysectors"] : "all";
  } else {
    $sectors_ids = implode(',', $_GET["currentPrimarySectors"]);
    if (empty($sectors_ids)) $sectors_ids = "all";
  }
  if($sectors_ids != 'all' && !empty($sectors_ids)){
    $sectors_array = array_merge($sectors_array, explode(",", $sectors_ids));
  }
  if (empty($_GET["currentSecondarySectors"])) {
    $sectors_ids = !empty($_GET["secondarysectors"]) ? $_GET["secondarysectors"] : "all";
  } else {
    $sectors_ids = implode(',', $_GET["currentSecondarySectors"]);
    if (empty($sectors_ids)) $sectors_ids = "all";
  }
  if($sectors_ids != 'all' && !empty($sectors_ids)){
    $sectors_array = array_merge($sectors_array, explode(",", $sectors_ids));
  }

  //watchdog('ampp_projects', 'ajax GET: ' . serialize($_GET));
  
  if (empty($_GET["currentDonors"])) {
    $args[0] = !empty($_GET["donors"]) ? $_GET["donors"] : "all";
  } else {
    $args[0] = implode(',', $_GET["currentDonors"]);
    if (empty($args[0])) $args[0] = "all";
  }
  
  $args[1] = (count($sectors_array) > 0) ? implode(',', $sectors_array) : "all";
  
  if (empty($_GET["currentRegions"])) {
    $args[2] = !empty($_GET["locations"]) ? $_GET["locations"] : "all";
  } else {
    $args[2] = implode(',', $_GET["currentRegions"]);
    if (empty($args[2])) $args[2] = "all";
  }
  
  if (empty($_GET["currentKeywords"])) {
    $args[3] = !empty($_GET["keywords"]) ? $_GET["keywords"] : "all";
  } else {
    $args[3] = $_GET["currentKeywords"];
    if (empty($args[3])) $args[3] = "all";
  }
  
  $args[4] = !empty($_GET["budget"]) ? $_GET["budget"] : "all";
  
  if (empty($_GET["currentPrograms"])) {
    $args[5] = !empty($_GET["programs"]) ? $_GET["programs"] : "all";
  } else {
    $args[5] = implode(',', $_GET["currentPrograms"]);
    if (empty($args[5])) $args[5] = "all";
  }
  
  if (empty($_GET["currentPeriodStart"])) {
    $args[6] = !empty($_GET["period_start"]) ? $_GET["period_start"] : "all";
  } else {
    $args[6] = $_GET["currentPeriodStart"];
    if (empty($args[6])) $args[6] = "all";
  }
  if (empty($_GET["currentPeriodEnd"])) {
    $args[7] = !empty($_GET["period_end"]) ? $_GET["period_end"] : "all";
  } else {
    $args[7] = $_GET["currentPeriodEnd"];
    if (empty($args[7])) $args[7] = "all";
  }

  //watchdog('ampp_projects', 'ajax ARGS: ' . serialize($args));

  $view = views_get_view('projects_search_result');
  $tmpview = $view->clone_view();
  $tmpview->set_display('panel_pane_1');
  $tmpview->set_arguments($args); 
  $tmpview->pre_execute();
  $tmpview->execute();
  
  //watchdog('ampp_projects', 'ajax TOTAL: ' . $tmpview->total_rows);
  
  if ($JSON) {
    $response = array('count' => $tmpview->total_rows);
    drupal_json_output($response);
    drupal_exit();  
  } else {
    return $tmpview->total_rows;
  }
}


/**
 * Callback handler for sectors search
 */
function ampp_search_sectors() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

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

  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_search_sectors', dpq($query, 1));
  
  $result = $query->execute();  
  foreach ($result as $record) {
    $items[] = array('id' => $record->amp_sector_id, 'name' => check_plain($record->name));
  }
  db_set_active();
  drupal_json_output($items);
}


/*
 *
 */
function ampp_search_primarysectors() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

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

  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_search_primarysectors', dpq($query, 1));
  
  $result = $query->execute();  
  foreach ($result as $record) {
    $items[] = array('id' => $record->amp_sector_id, 'name' => check_plain($record->name));
  }
  db_set_active();
  drupal_json_output($items);
}


/*
 *
 */
function ampp_search_secondarysectors() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

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
  
  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_search_secondarysectors', dpq($query, 1));
  
  $result = $query->execute();  
  foreach ($result as $record) {
    $items[] = array('id' => $record->amp_sector_id, 'name' => check_plain($record->name));
  }
  db_set_active();
  drupal_json_output($items);
}


/*
 *
 */
function ampp_search_programs() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

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
  drupal_json_output($items);
}


/**
 * Callback handler for donors search
 */
function ampp_search_donors() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

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

  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_search_donors', dpq($query, 1));
  
  $result = $query->execute();
  
  foreach ($result as $record) {
    $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
    $items[] = array('id'=>$record->amp_org_id, 'name'=>$name);
  }
  db_set_active();
  drupal_json_output($items);
}


/**
 * Callback handler for region search
 */
function ampp_search_regions() {

  global $amp_db_exists;
  if (!$amp_db_exists) return;

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

  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_search_regions', $query);
  
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
  drupal_json_output($items);
}


/**
 *
 */
function ampp_projects_views_pre_build(&$view) {
  if ( ($view->name == 'top_activities_by_disbursment') && ($view->current_display == 'default') ) {
    if (isset($_SESSION['top_activities_by_disbursement_conf'])) { 
      $conf = unserialize($_SESSION['top_activities_by_disbursement_conf']);
      
      $itemscount = $conf['rows_to_show'];
      $view->display_handler->options['items_per_page'] = $itemscount;
      $view->display_handler->options['pager']['options']['items_per_page'] = $itemscount;
      
      // confuguring sorting and order
      if (isset($conf['default_sort']) && !empty($conf['default_sort'])) {       
        foreach ($view->sort as $k => $v) { 
          if ($k != $conf['default_sort']) unset($view->sort[$k]);
        }

        // confuguring order and order        
        if (isset($conf['default_order']) && !empty($conf['default_order'])) {
          $view->sort[$conf['default_sort']]->options['order'] = $conf['default_order'];
        }
      }
    }
  }  
}


/*
 * @Implementation of hook_views_pre_execute
 */
function ampp_projects_views_pre_execute(&$view){

  global $language;

  if ( $view->name == 'projects_search_result' && $view->current_display != 'block_search_result_totall') {
  
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpActivityVersion', 'name', cached_amp_activity.amp_activity_id, 'amp_activity_version', 'name', 'amp_activity_id', '" . $language->language . "')";
  
    $view->build_info['query']
      ->groupBy('cached_amp_activity.amp_activity_id')
      ->groupBy($translate_field)
      ->groupBy('cached_amp_activity.actual_start_date')
      ->groupBy('cached_amp_activity.actual_completion_date')
      ->groupBy('cached_v_status.name');
   
    $view->build_info['count_query']
      ->groupBy('cached_amp_activity.amp_activity_id')
      ->groupBy('cached_amp_activity.name')
      ->groupBy('cached_amp_activity.actual_start_date')
      ->groupBy('cached_amp_activity.actual_completion_date');
  }
  
  if ( $view->name == 'projects_search_result' && $view->current_display == 'feed_1') {  
    unset($view->field['amp_activity_id']);
  
    // optionally show the Status column
    if (!isset($_GET['show_status_column']) || $_GET['show_status_column'] == 0) {
      unset($view->field['name_1']);
    }
  }
  
  //dpq($view->build_info['query']);
}


/*
 * Get currency code 
 */
function ampp_projects_get_currency_code(){
  
  global $amp_db_exists;
  if (!$amp_db_exists) return;
  
  db_set_active("amp_projects");
  $currency_code = db_select('amp_global_settings', 'settings')
                ->condition('settings.settingsname', 'Base Currency', "=")
                ->fields('settings',array('settingsvalue'))
                ->execute()
                ->fetchField();
  db_set_active();
  return $currency_code;
}


/*
 * Get sectors children ids
 * @param integer $parentSectorId
 * @return Ambigous <multitype:, multitype:unknown >
 */
function getChildSectorsIds($parentSectorId){
  
  global $amp_db_exists;
  if (!$amp_db_exists) return;
  
  $allChildIds = array();
  db_set_active("amp_projects");

  $childIds = db_select('amp_sector', 's')
  ->fields('s',array('amp_sector_id'))
  ->condition('s.parent_sector_id', $parentSectorId, '=')
  ->execute()
  ->fetchAll(PDO::FETCH_COLUMN);
  
  if(!empty($childIds)){
    foreach ($childIds as $childId){
      $allChildIds[] = $childId;
      $allChildIds = array_merge($allChildIds, getChildSectorsIds($childId));
    }
  }
    
  db_set_active();
  return $allChildIds;
}


/*
 *
 */
function getChildProgramsIds($parentProgramId){
  
  global $amp_db_exists;
  if (!$amp_db_exists) return;
  
  $allChildIds = array();
  db_set_active("amp_projects");

  $childIds = db_select('amp_team', 't')
  ->fields('t',array('amp_team_id'))
  ->condition('t.parent_team_id', $parentProgramId, '=')
  ->execute()
  ->fetchAll(PDO::FETCH_COLUMN);
  
  if(!empty($childIds)){
    foreach ($childIds as $childId){
      $allChildIds[] = $childId;
      $allChildIds = array_merge($allChildIds, getChildProgramsIds($childId));
    }
  }
    
  db_set_active();
  return $allChildIds;
}


/*
 *
 */
function getParentLocationIds($childLocationId) {
  
  global $amp_db_exists;

  if (!$amp_db_exists) return;
  
  $allParentIds = array();

  if (!empty($_GET["RegionalProjects"])) {
    $regionalinclude = $_GET["RegionalProjects"];
  } else {
    $regionalinclude = $_GET["showregionalprojects"];
  }
   
  if ($regionalinclude == '1') {
	  db_set_active("amp_projects");

	  $parentIds = db_select('amp_category_value_location', 'l')
	    ->fields('l', array('parent_location'))
	    ->condition('l.id', $childLocationId, '=')
	    ->isNotNull('l.parent_location')
	    ->execute()
	    ->fetchAll(PDO::FETCH_COLUMN);
	  
	  if (!empty($parentIds)) {
	    foreach ($parentIds as $parentId) {
	      $allParentIds[] = $parentId;
	      $allParentIds = array_merge($allParentIds, getParentLocationIds($parentId));
	    }
	  }
	    
	  db_set_active();
  }
  return $allParentIds;
}


/*
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
       
  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('get_location_path', $query);
    
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
  
  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_projects_get_donor', dpq($query, 1));
  
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

  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_projects_get_donors', dpq($query, 1));
  
  $result = $query->execute();
  
  $hierarchy = array();
  foreach ($result as $record) {
	$hierarchy[$record->amp_org_id] = array('label' => $record->name);
  }
	
  db_set_active();
  	
  return $hierarchy;
}


/*
 *
 */
function ampp_projects_views_query_alter(&$view, &$query) {
	if($view->name == 'projects_search_result' && $view->current_display != "block_search_result_totall") {
		/*$query->where[0]['conditions'][] = array(
			'field' => 'cached_amp_activity.amp_team_id',
			'value' => 'select amp_team_id from amp_team where parent_team_id in (select amp_team_id from amp_team where access_type="Management")',
			'operator' => 'in'
		);*/
		//dsm($query, 'f');
	}
}


/*
 *
 */
function ampp_projects_views_pre_render(&$view) {
  if ( ($view->name == 'projects_search_result')
        && ($view->current_display == 'panel_pane_1' || $view->current_display == 'default' || $view->current_display == 'feed_1') ) {

    if(isset($_SESSION["projects_search_result_conf"])) { 
		$conf = unserialize($_SESSION["projects_search_result_conf"]);
	    $fields_to_remove = get_views_field_names('projects_search_result');

	    foreach ($fields_to_remove as $field_name) {
	    	
	    	if(isset($conf[$field_name."_label"]) && $conf[$field_name."_label"] != '' )
	    	{
	    		$view->field[$field_name]->options['label']=t($conf[$field_name."_label"]);
	    	}
	    	if(isset($conf[$field_name."_flag"]) && $conf[$field_name."_flag"] != 1 )
	    	{
		      unset($view->field[$field_name]);            
		      unset($view->display_handler->handlers['field'][$field_name]);  
	        }
	    }
     }
  }
  
  if ( ($view->name == 'top_activities_by_disbursment') && ($view->current_display == 'default') ) {
    if (isset($_SESSION['top_activities_by_disbursement_conf'])) { 
      $conf = unserialize($_SESSION['top_activities_by_disbursement_conf']);

      foreach ( $view->field as &$field ) {     
        $name = (substr($field->field, 0, 6) == 'field_') ? substr($field->field, 6) : $field->field;     
        if (isset($conf['heading_' . $name]) && !empty($conf['heading_' . $name])) {
          $field->options['label'] = t($conf['heading_' . $name]);
        }
      }
    }
  }
}


/*
 *
 */
function get_views_field_names($view_name, $display = 'default') {
   $view = views_get_view($view_name);
   $result = array();
   if(!empty($view->display[$display]->display_options['fields'])) {
	   foreach($view->display[$display]->display_options['fields'] as $field){
	       $result[] = $field["field"];
	   }
   }
   return $result;
}


/*
 *
 */
function ampp_search_result_export() {
	
  $view = views_get_view('projects_search_result');
  $tmpview = $view->clone_view();
  $tmpview->set_display('feed_1');
	
  $sectors_array = Array();
		
  $sectors_ids = empty($_GET["sectors"]) ? '' : $_GET["sectors"];
  if($sectors_ids != 'all' and !empty($sectors_ids)){
	$sectors_array = explode(",",$sectors_ids);
  }

   $sectors_ids = empty($_GET["primarysectors"]) ? '' : $_GET["primarysectors"];
  if($sectors_ids != 'all' and !empty($sectors_ids)){
	$sectors_array = array_merge($sectors_array,explode(",",$sectors_ids));
  }

  $sectors_ids = empty($_GET["secondarysectors"]) ? '' : $_GET["secondarysectors"];
  if($sectors_ids != 'all' and !empty($sectors_ids)){
	$sectors_array = array_merge($sectors_array,explode(",",$sectors_ids));
  }
	
  $args[0] = !empty($_GET["donors"])? $_GET["donors"] : "all";
  $args[1] = (count($sectors_array) > 0) ? implode(',',$sectors_array) : "all";
  $args[2] = !empty($_GET["locations"])? $_GET["locations"] : "all";
  $args[3] = !empty($_GET["keywords"])? $_GET["keywords"] : "all";
  $args[4] = !empty($_GET["budget"])? $_GET["budget"] : "all";
  $args[5] = !empty($_GET["programs"])? $_GET["programs"] : "all";
	
  echo $tmpview->preview('feed_1',$args);
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