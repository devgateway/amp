<?php

/**
 * Menu callback; Retrieve a JSON object containing autocomplete suggestions for existing surveys.
 */
function ampapi_activity_autocomplete($property_name, $string = '') {
  $matches = array();

  $request_type = 'autocomplete';
  if (empty($string) && !empty($_GET['string'])) {
    $request_type = 'tokeninput';
    $string = $_GET['string'];
  }

  $plugin = ampapi_get_mapper_by_property_name($property_name);
  if (empty($plugin)) {
    throw new activityMapperException(t('Missing handler for @property', array('@property' => $property_name)));
  }

  // @HACK: Temporary!
  if ($plugin['report field key'] == 'Donor Agency') {
    $plugin['report field key'] = 'Donor'; // Yes, we have a consistent API.
  }

  $class_name = ctools_plugin_get_class($plugin, 'handler');
  if (empty($class_name) || !in_array('activityMapperAutocompleteInterface', class_implements($class_name))) {
    throw new activityMapperException(t('Could not load API plugin handler for @plugin', array('@plugin' => $plugin['title'])));
  }

  if ($string) {
    $handler = new $class_name();
    $autocomplete_values = $handler->autocomplete($plugin, array($string), $request_type);
    foreach ($autocomplete_values as $value) {
      if ($request_type == 'autocomplete') {
        $matches[$value['name']] = $value['name'];
      }
      else {
        $matches[] = $value;
      }
    }
  }

  drupal_json_output($matches);
}
