<?php

/*
 * define plugin
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('Data Quality'),
  'description' => t('Provides donors data quality table'),
  'category' => array(t('AMPP Widgets')),
  'hook theme' => 'ampp_projects_data_quality_content_type_theme',
);

/**
 *
 */
function ampp_projects_data_quality_content_type_render($subtype, $conf, $panel_args, $context) {
  $block = new stdClass();
  $block->content = t('Could not connect to the activities database. Please try again later. If the problem persists contact the site administrator.');

  if (!amp_activity_check_db()) {
    return $block;
  }

  $data_quality = _amp_projects_get_data_quality_records();
  $block->content = theme('ampp_projects_data_quality', array('quality_records' => $data_quality));

  return $block;
}

/**
 *
 */
function ampp_projects_data_quality_content_type_theme(&$theme, $plugin) {
  $theme['ampp_projects_data_quality'] = array(
    'arguments' => array('quality_records' => NULL),
    'path' => $plugin['path'],
    'template' => 'data_quality'
  );
}

/**
 *
 */
function _amp_projects_get_data_quality_records() {
  db_set_active("amp_projects");
  $query = '
    SELECT
      at.name AS name,
      CASE WHEN date_updated is null THEN date_created ELSE date_updated END as latest_date
    FROM
      {cached_amp_activity} a,
      {amp_team} at
    WHERE
      a.amp_team_id = at.amp_team_id
      AND amp_activity_id IN
        (
          SELECT MAX(a.amp_activity_id) as amp_activity_id
          FROM {cached_amp_activity} a, {amp_team} at
          WHERE a.amp_team_id = at.amp_team_id
            AND at.computation = true
          GROUP BY at.name
        )
    ORDER BY at.name';

  $result = db_query($query);
  $data_quality = array();
  foreach ($result as $record) {
    $data_quality[$record->name] = format_date(strtotime($record->latest_date), 'shortest');
  }
  db_set_active();

  return $data_quality;
}
