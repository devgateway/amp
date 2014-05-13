<?php
/**
 * @file
 * ampp_projects.strongarm.inc
 */

/**
 * Implements hook_strongarm().
 */
function ampp_projects_strongarm() {
  $export = array();

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'language_content_type_top_activities_by_disbursement';
  $strongarm->value = '0';
  $export['language_content_type_top_activities_by_disbursement'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'menu_options_top_activities_by_disbursement';
  $strongarm->value = array();
  $export['menu_options_top_activities_by_disbursement'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'menu_parent_top_activities_by_disbursement';
  $strongarm->value = 'main-menu:0';
  $export['menu_parent_top_activities_by_disbursement'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'node_options_top_activities_by_disbursement';
  $strongarm->value = array(
    0 => 'status',
    1 => 'promote',
  );
  $export['node_options_top_activities_by_disbursement'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'node_preview_top_activities_by_disbursement';
  $strongarm->value = '1';
  $export['node_preview_top_activities_by_disbursement'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'node_submitted_top_activities_by_disbursement';
  $strongarm->value = 1;
  $export['node_submitted_top_activities_by_disbursement'] = $strongarm;

  return $export;
}
