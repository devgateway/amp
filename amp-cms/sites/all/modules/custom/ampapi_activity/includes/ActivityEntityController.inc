<?php

/**
 * @file
 * Defines the controller class for entity activity.
 */

/**
 * Controller class for activity entities.
 */
class ActivityEntityController extends ampAPIEntityController {
  public static function schemaInfo() {
    $info = array();

    // Get information from all available mapper plugins.
    foreach (ampapi_get_mappers() as $plugin) {
      if (empty($plugin['property name'])) {
        continue;
      }

      if ($class_name = ctools_plugin_get_class($plugin, 'handler')) {
        $info += call_user_func_array(array($class_name, 'propertyInfo'), array($plugin));
      }
    }

    // Add a computed property for an entity view link.
    $info['external_url'] = array(
      'schema field' => 'external_url',
      'label' => 'External URL',
      'description' => 'Link to the external entity view page.',
      'property info' => array(
        'type' => 'uri',
      ),
    );

    return $info;
  }
}
