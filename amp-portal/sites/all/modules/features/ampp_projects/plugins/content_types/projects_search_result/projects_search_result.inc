<?php 

/**
 * @file
 * Projects Search Result
 *
 */

$plugin = array(
  'single' => TRUE,
  'title' => t('Projects Search Result'),
  'description' => t('Projects Search Result'),
  'category' => array(t('AMPP Widgets')),
  'defaults' => array('name_flag'=>1,
    'name_label' => t('Project title'),
    'amp_org_name_agg_flag'=>1,
    'amp_org_name_agg_label' => t('Donors'),
    'amp_sector_name_agg_flag' => 1,
    'amp_sector_name_agg_label' => t('Sectors'),
    'amp_primarysector_name_agg_flag' => 0,
    'amp_primarysector_name_agg_label' => t('Primary sectors'),
    'amp_secondarysector_name_agg_flag' => 0,
    'amp_secondarysector_name_agg_label' => t('Secondary sectors'),
    'amp_theme_name_agg_flag' => 0,
    'amp_theme_name_agg_label' => t('Programs'),
    'category_location_name_agg_flag' => 1,
    'category_location_name_agg_label' => t('Locations'),
    'actual_start_date_flag' => 1,
    'actual_start_date_label' => t('Start date'),
    'actual_completion_date_flag' => 1,
    'actual_completion_date_label' => t('End date'),
    'commitment_amount_flag' => 1,
    'commitment_amount_label' => t('Commitment amount'),
    'disbursement_amount_flag' => 1,
    'disbursement_amount_label' => t('Disbursement amount'),
    'hide_first_column' => 0,
    'show_status_column' => 0,
    'allowxlsexport' => 0
  ),
  'render callback' => 'ampp_projects_projects_search_result_render'
);

/**
 * Run-time rendering of the body of the block.
 *
 * @param $subtype
 * @param $conf
 *   Configuration as done at admin time.
 * @param $args
 * @param $context
 *   Context - in this case we don't have any.
 *
 * @return
 *   An object with at least title and content members.
 */
function ampp_projects_projects_search_result_render($subtype, $conf, $args, $context) {
  
  global $user;
  
  module_load_include('inc', 'node', 'node.pages');
    
  $_SESSION["projects_search_result_conf"] = serialize($conf);
  
  $block = new stdClass();
  $view = views_get_view('projects_search_result');
  $tmpview = $view->clone_view();
  $tmpview->set_display('panel_pane_1');
  $block->title = t('Projects search');
	
  $sectors_array = array();	
  $sectors_ids = empty($_GET["sectors"]) ? '' : $_GET["sectors"];
  if($sectors_ids != 'all' and !empty($sectors_ids)){
    $sectors_array = explode(",", $sectors_ids);
  }
  $sectors_ids = empty($_GET["primarysectors"]) ? '' : $_GET["primarysectors"];
  if($sectors_ids != 'all' and !empty($sectors_ids)){
    $sectors_array = array_merge($sectors_array, explode(",", $sectors_ids));
  }
  $sectors_ids = empty($_GET["secondarysectors"]) ? '' : $_GET["secondarysectors"];
  if($sectors_ids != 'all' and !empty($sectors_ids)){
    $sectors_array = array_merge($sectors_array, explode(",", $sectors_ids));
  }	
	
  $args[0] = !empty($_GET["donors"])? $_GET["donors"] : "all";
  $args[1] = (count($sectors_array) > 0) ? implode(',', $sectors_array) : "all";
  $args[2] = !empty($_GET["locations"])? $_GET["locations"] : "all";
  $args[3] = !empty($_GET["keywords"])? $_GET["keywords"] : "all";
  $args[4] = !empty($_GET["budget"])? $_GET["budget"] : "all";
  $args[5] = !empty($_GET["programs"])? $_GET["programs"] : "all";
  $args[6] = !empty($_GET["period_start"])? $_GET["period_start"] : "all";
  $args[7] = !empty($_GET["period_end"])? $_GET["period_end"] : "all";
	
  // we need to prepend all descendant locations
  $args[2] = implode(',', addDescendantLocations(explode(',', $args[2])));
  
  $showregionalprojects = !empty($_GET["showregionalprojects"])? $_GET["showregionalprojects"] : "all";
    
  if (isset($conf["allowxlsexport"]) && $conf["allowxlsexport"] == 1) {
    $block->content =  l(t('Export in XLS format'), 'searchexport/searchresult.xls', 
	    					array('attributes' => array('class' => array('export-link')),
	    						  'query' => array( 
	    						     'donors' => $args[0],
                                     'sectors' => $args[1],
                                     'programs' => $args[5],
                                     'locations' => $args[2],
                                     'period_start' => $args[6],
                                     'period_end' => $args[7],
                                     'keywords' => $args[3],
                                     'budget' => $args[4],
                                     'showregionalprojects' => $showregionalprojects,
                                     'hide_first_column' => (isset($conf['hide_first_column']) && $conf['hide_first_column']) ? 1 : 0,
	    							 'show_status_column' => (!isset($conf['show_status_column']) || $conf['show_status_column'] == 0) ? 0 : 1,
	    					      )));

    global $base_root;
    $export_url = url('searchexport/searchresult.xls', 
					array('attributes' => array('class' => array('export-link')),
						  'query' => array( 
						     'donors' => $args[0],
                               'sectors' => $args[1],
                               'programs' => $args[5],
                               'locations' => $args[2],
                               'period_start' => $args[6],
                               'period_end' => $args[7],
                               'keywords' => $args[3],
                               'budget' => $args[4],
                               'showregionalprojects' => $showregionalprojects,
                               'hide_first_column' => (isset($conf['hide_first_column']) && $conf['hide_first_column']) ? 1 : 0,
							 'show_status_column' => (!isset($conf['show_status_column']) || $conf['show_status_column'] == 0) ? 0 : 1,
					)));
	if (module_exists('shorten')) {
      $block->content .=  '<br><a class="shorten-url" href="http://' . shorten_url($base_root . $export_url) . '"><b>' . t('Permanent link') . '</b> </a><br>';
    } 
  }
  
  $tmpview->set_arguments($args);  
  $tmpview->pre_execute(); 
  $tmpview->execute();

  if (isset($user) && $user->uid == 1 && variable_get('log_db_queries', 0)) watchdog('ampp_projects_projects_search_result_render', dpq($tmpview->build_info['query'], 1));
  //watchdog('ampp_projects', 'results TOTAL: ' . $tmpview->total_rows);
  
  // optionally hide the first column
  if (isset($conf['hide_first_column']) && $conf['hide_first_column']) {
    unset($tmpview->field['amp_activity_id']); 
  }
  
  // optionally show the Status column
  if (!isset($conf['show_status_column']) || $conf['show_status_column'] == 0) {
    unset($tmpview->field['name_1']);
  }
  
  //return text message if no result
  $block->content .= ($tmpview->total_rows) ? $tmpview->preview('panel_pane_1') : t(variable_get('amp_no_search_results_text', 'No results found'));
  return $block;
}


/*
 *
 */
function ampp_projects_preprocess_html(&$variables) {
  	$variables['classes_array'][] = "page-search";
}


/*
 *
 */
function ampp_projects_projects_search_result_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];
 
  $form['note_show_columns'] = array(
        '#type' => 'item',
        '#markup' => '<div><b>' . t('Show columns') . '</b></div>',
        '#description' => t('At least one column should be selected.'),
        '#weight' => 0,
  );

  //hide($form['override_title']);
  //hide($form['override_title_text']);
  hide($form['override_title_markup']);
  
  // Project title
  $form['name_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Project title'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );  
  $form['name_fieldset']['name_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Project title column'),
    '#default_value' => isset($conf['name_flag']) ? intval($conf['name_flag']) : $form_state['plugin']['defaults']['name_flag'],
  );
  $form['name_fieldset']['name_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['name_label']) &&  !empty($conf['name_label']) ? $conf['name_label'] : $form_state['plugin']['defaults']['name_label'],
  );
  // Donors
  $form['amp_org_name_agg_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Donors'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['amp_org_name_agg_fieldset']['amp_org_name_agg_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Donors column'),
    '#default_value' => isset($conf['amp_org_name_agg_flag']) ? intval($conf['amp_org_name_agg_flag']) : $form_state['plugin']['defaults']['amp_org_name_agg_flag'],
  );
  $form['amp_org_name_agg_fieldset']['amp_org_name_agg_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['amp_org_name_agg_label']) &&  !empty($conf['amp_org_name_agg_label']) ? $conf['amp_org_name_agg_label'] : $form_state['plugin']['defaults']['amp_org_name_agg_label'],
  );
   // Sectors
  $form['amp_sector_name_agg_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Sectors'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['amp_sector_name_agg_fieldset']['amp_sector_name_agg_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Sectors column'),
    '#default_value' => isset($conf['amp_sector_name_agg_flag']) ? intval($conf['amp_sector_name_agg_flag']) : $form_state['plugin']['defaults']['amp_sector_name_agg_flag'],
  );
  $form['amp_sector_name_agg_fieldset']['amp_sector_name_agg_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['amp_sector_name_agg_label']) &&  !empty($conf['amp_sector_name_agg_label']) ? $conf['amp_sector_name_agg_label'] : $form_state['plugin']['defaults']['amp_sector_name_agg_label'],
  );
  // Primary Sectors
  $form['amp_primarysector_name_agg_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Primary Sectors'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['amp_primarysector_name_agg_fieldset']['amp_primarysector_name_agg_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Primary Sectors column'),
    '#default_value' => isset($conf['amp_primarysector_name_agg_flag']) ? intval($conf['amp_primarysector_name_agg_flag']) : $form_state['plugin']['defaults']['amp_primarysector_name_agg_flag'],
  );
  $form['amp_primarysector_name_agg_fieldset']['amp_primarysector_name_agg_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['amp_primarysector_name_agg_label']) &&  !empty($conf['amp_primarysector_name_agg_label']) ? $conf['amp_primarysector_name_agg_label'] : $form_state['plugin']['defaults']['amp_primarysector_name_agg_label'],
  );
  // Secondary Sectors
  $form['amp_secondarysector_name_agg_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Secondary Sectors'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['amp_secondarysector_name_agg_fieldset']['amp_secondarysector_name_agg_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Secondary Sectors column'),
    '#default_value' => isset($conf['amp_secondarysector_name_agg_flag']) ? intval($conf['amp_secondarysector_name_agg_flag']) : $form_state['plugin']['defaults']['amp_secondarysector_name_agg_flag'],
  );
  $form['amp_secondarysector_name_agg_fieldset']['amp_secondarysector_name_agg_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['amp_secondarysector_name_agg_label']) &&  !empty($conf['amp_secondarysector_name_agg_label']) ? $conf['amp_secondarysector_name_agg_label'] : $form_state['plugin']['defaults']['amp_secondarysector_name_agg_label'],
  );
  // Programs
  $form['amp_theme_name_agg_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Programs'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['amp_theme_name_agg_fieldset']['amp_theme_name_agg_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Programs column'),
    '#default_value' => isset($conf['amp_theme_name_agg_flag']) ? intval($conf['amp_theme_name_agg_flag']) : $form_state['plugin']['defaults']['amp_theme_name_agg_flag'],
  );
  $form['amp_theme_name_agg_fieldset']['amp_theme_name_agg_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['amp_theme_name_agg_label']) &&  !empty($conf['amp_theme_name_agg_label']) ? $conf['amp_theme_name_agg_label'] : $form_state['plugin']['defaults']['amp_theme_name_agg_label'],
  );
  // Locations
  $form['category_location_name_agg_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Locations'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['category_location_name_agg_fieldset']['category_location_name_agg_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Locations column'),
    '#default_value' => isset($conf['category_location_name_agg_flag']) ? intval($conf['category_location_name_agg_flag']) : $form_state['plugin']['defaults']['category_location_name_agg_flag'],
  );
  $form['category_location_name_agg_fieldset']['category_location_name_agg_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['category_location_name_agg_label']) &&  !empty($conf['category_location_name_agg_label']) ? $conf['category_location_name_agg_label'] : $form_state['plugin']['defaults']['category_location_name_agg_label'],
  );
  // Start Date
  $form['actual_start_date_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Start date'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['actual_start_date_fieldset']['actual_start_date_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Start date column'),
    '#default_value' => isset($conf['actual_start_date_flag']) ? intval($conf['actual_start_date_flag']) : $form_state['plugin']['defaults']['actual_start_date_flag'],
  );
  $form['actual_start_date_fieldset']['actual_start_date_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['actual_start_date_label']) &&  !empty($conf['actual_start_date_label']) ? $conf['actual_start_date_label'] : $form_state['plugin']['defaults']['actual_start_date_label'],
  );
  // End Date
  $form['actual_completion_date_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('End date'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['actual_completion_date_fieldset']['actual_completion_date_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show End date column'),
    '#default_value' => isset($conf['actual_completion_date_flag']) ? intval($conf['actual_completion_date_flag']) : $form_state['plugin']['defaults']['actual_completion_date_flag'],
  );
  $form['actual_completion_date_fieldset']['actual_completion_date_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['actual_completion_date_label']) &&  !empty($conf['actual_completion_date_label']) ? $conf['actual_completion_date_label'] : $form_state['plugin']['defaults']['actual_completion_date_label'],
  );
  // Commitment amount
  $form['commitment_amount_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Commitment amount'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['commitment_amount_fieldset']['commitment_amount_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Commitment amount column'),
    '#default_value' => isset($conf['commitment_amount_flag']) ? intval($conf['commitment_amount_flag']) : $form_state['plugin']['defaults']['commitment_amount_flag'],
  );
  $form['commitment_amount_fieldset']['commitment_amount_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['commitment_amount_label']) &&  !empty($conf['commitment_amount_label']) ? $conf['commitment_amount_label'] : $form_state['plugin']['defaults']['commitment_amount_label'],
  );
  // Disbursement amount
  $form['disbursement_amount_fieldset'] = array(
    '#type' => 'fieldset',
    '#title' => t('Disbursement amount'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  $form['disbursement_amount_fieldset']['disbursement_amount_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Disbursement amount column'),
    '#default_value' => isset($conf['disbursement_amount_flag']) ? intval($conf['disbursement_amount_flag']) : $form_state['plugin']['defaults']['disbursement_amount_flag'],
  );
  $form['disbursement_amount_fieldset']['disbursement_amount_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Label'),
    '#size' => 30, 
    '#default_value' => isset($conf['disbursement_amount_label']) &&  !empty($conf['disbursement_amount_label']) ? $conf['disbursement_amount_label'] : $form_state['plugin']['defaults']['disbursement_amount_label'],
  );
  $form['hide_first_column'] = array(
    '#type' => 'checkbox',
    '#title' => t('Hide the first column'),
    '#default_value' => isset($conf['hide_first_column']) ? intval($conf['hide_first_column']) : $form_state['plugin']['defaults']['hide_first_column'],
  );
  $form['show_status_column'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show the Status column'),
    '#default_value' => isset($conf['show_status_column']) ? intval($conf['show_status_column']) : $form_state['plugin']['defaults']['show_status_column'],
  );
  $form['allowxlsexport'] = array(
    '#type' => 'checkbox',
    '#title' => t('Enable Export To XLS option'),
    '#default_value' => isset($conf['allowxlsexport']) ? intval($conf['allowxlsexport']) : $form_state['plugin']['defaults']['allowxlsexport'],
  );
  
  return $form;
} 


/*
 * handles edit for submit
 */
function ampp_projects_projects_search_result_content_type_edit_form_submit(&$form, &$form_state) { 
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}
