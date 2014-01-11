<?php

/*
 * define plugin
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('Projects data is as of'),
  'description' => t('Returns date of projects data'),
  'category' => array(t('Widgets')),
  'hook theme' => 'ampp_projects_projects_as_of_content_type_theme',
);


/**
 *
 */
function ampp_projects_projects_as_of_content_type_render($subtype, $conf, $panel_args, $context) {
	global $amp_db_exists;
	$block = new stdClass();
	$block->content = '';
	
	if($amp_db_exists) {
      $date = _amp_projects_get_projects_as_of_records();
      $block->content = theme('ampp_projects_projects_as_of', array('date' => $date));
	}
	return $block;
}


/**
 *
 */
function ampp_projects_projects_as_of_content_type_theme(&$theme, $plugin) {
    $theme['ampp_projects_projects_as_of'] = array(
		'arguments' => array('date' => NULL),
		'path' => $plugin['path'],
		'template' => 'projects_as_of'
    );
}


/**
 *
 */
function _amp_projects_get_projects_as_of_records() {
	db_set_active("amp_projects");
	$query = 'SELECT TO_TIMESTAMP(prev_fire_time/1000) as date FROM {qrtz_triggers}
	           WHERE job_name = \'Refresh Mondrian Cache\'';
		
    $result = db_query($query);
    foreach ($result as $record) {
      $date = format_date($record->date, 'short');
    }
	db_set_active();	
	return 	$date;
}