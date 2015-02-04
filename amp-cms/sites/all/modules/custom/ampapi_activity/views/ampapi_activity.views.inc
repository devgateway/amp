<?php

/**
 * Implements hook_views_data_alter().
 */
function ampapi_activity_views_data_alter(&$data) {
  foreach (entity_get_info() as $entity_type => $entity_info) {
    if (empty($entity_info['api resource path'])) {
      continue;
    }

    $efq_data_key = 'efq_' . $entity_type;
    if (empty($data[$efq_data_key])) {
      continue;
    }

    // Remove fields.
    unset($data[$efq_data_key]['rendered_entity']);

    // View Link is a generated field.
    if (!empty($data[$efq_data_key]['external_url']['field']['handler'])) {
      $data[$efq_data_key]['external_url']['field']['handler'] = 'ampapi_activity_views_handler_field_uri';
      unset($data[$efq_data_key]['external_url']['argument']);
      unset($data[$efq_data_key]['external_url']['filter']);
      unset($data[$efq_data_key]['external_url']['sort']);
    }
    
    // Get information from all available mapper plugins.
    foreach (ampapi_get_mappers() as $plugin) {
      if (empty($plugin['property name'])) {
        continue;
      }
      $property_name = $plugin['property name'];
      
      if (isset($plugin['property info']['options list'])) {
        $data[$efq_data_key][$property_name]['filter']['handler'] = 'ampapi_activity_views_handler_filter_in_operator';
        $data[$efq_data_key][$property_name]['filter']['options arguments'] = array($plugin['name']);
      }

      if (isset($plugin['report filter endpoint'])) {
        $data[$efq_data_key][$property_name]['filter']['handler'] = 'ampapi_activity_views_handler_filter_in_operator';
      }
      
    	if (isset($plugin['report field type']) && ($plugin['report field type'] == 'currency')) {
      	$data[$efq_data_key][$property_name]['field']['handler'] = 'ampapi_activity_views_handler_field_with_currency';
      }

      // Remove sort from fields that do not support it.
      // if (isset($plugin['property sort callback'])) {
      //   continue;
      // }

      // if (isset($data[$efq_data_key][$property_name]['sort'])) {
      //   unset($data[$efq_data_key][$property_name]['sort']);
      // }
    }
  }
}
