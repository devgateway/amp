<?php
/**
 * @file
 * amporganization_profiles.features.inc
 */

/**
 * Implements hook_ctools_plugin_api().
 */
function amporganization_profiles_ctools_plugin_api($module = NULL, $api = NULL) {
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
function amporganization_profiles_views_api($module = NULL, $api = NULL) {
  return array("api" => "3.0");
}

/**
 * Implements hook_node_info().
 */
function amporganization_profiles_node_info() {
  $items = array(
    'organization' => array(
      'name' => t('Organization Profile'),
      'base' => 'node_content',
      'description' => t('Can be used to create pages with information about organizations.'),
      'has_title' => '1',
      'title_label' => t('Name'),
      'help' => '',
    ),
  );
  drupal_alter('node_info', $items);
  return $items;
}
