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

    // Expose "Activities Search Type" filter.
    $data[$efq_data_key]['activities_search_type'] = array(
      'title' => t('Activities Search Type'),
      'help' => t('Filter activities by type: Standard Activities vs South-South Cooperation (SSC) Activities.'),
    );
    $data[$efq_data_key]['activities_search_type']['filter'] = array(
      'handler' => 'ampapi_activity_views_handler_filter_ssc',
    );

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

      if (isset($plugin['report filter key'])) {
        // @TODO: Also override contextual filters (arguments) if necessary.
        switch($plugin['property info']['type']) {
          case 'boolean':
            $data[$efq_data_key][$property_name]['filter']['handler'] = 'ampapi_activity_views_handler_filter_boolean';
            break;
          case 'text':
          default:
            if (isset($plugin['report filter endpoint'])) {
              $data[$efq_data_key][$property_name]['filter']['handler'] = 'ampapi_activity_views_handler_filter_in_operator';
            }
        }
      }
      else {
        // Remove the default filter and argument handlers since they won't work.
        unset($data[$efq_data_key][$property_name]['filter']);
        unset($data[$efq_data_key][$property_name]['argument']);
      }

      if (isset($plugin['report field type']) && ($plugin['report field type'] == 'currency')) {
        $data[$efq_data_key][$property_name]['field']['handler'] = 'ampapi_activity_views_handler_field_with_currency';
      }
    }
  }
}
