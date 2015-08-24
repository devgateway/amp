<?php
/**
 * @file
 * ampstaff_profiles.features.inc
 */

/**
 * Implements hook_ctools_plugin_api().
 */
function ampstaff_profiles_ctools_plugin_api($module = NULL, $api = NULL) {
  if ($module == "ds" && $api == "ds") {
    return array("version" => "1");
  }
  if ($module == "field_group" && $api == "field_group") {
    return array("version" => "1");
  }
  if ($module == "strongarm" && $api == "strongarm") {
    return array("version" => "1");
  }
}

/**
 * Implements hook_views_api().
 */
function ampstaff_profiles_views_api($module = NULL, $api = NULL) {
  return array("api" => "3.0");
}

/**
 * Implements hook_node_info().
 */
function ampstaff_profiles_node_info() {
  $items = array(
    'staff' => array(
      'name' => t('Staff Profile'),
      'base' => 'node_content',
      'description' => t('Can be used to create pages with information about staff.'),
      'has_title' => '1',
      'title_label' => t('Name'),
      'help' => '',
    ),
  );
  drupal_alter('node_info', $items);
  return $items;
}
