<?php

/**
 * Menu callback: Retrieve a JSON object containing autocomplete suggestions for existing surveys.
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
    /** @var activityMapperAutocompleteBase $handler */
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

/**
 * Menu callback: Activity search export.
 */
function ampapi_activity_search_export() {
  $output = array();

  $view_name = 'activities';
  $display_id = 'export_page';
  $view = views_get_view($view_name);

  // @TODO: Move validation to menu access callback.
  if (!$view || !$view->access($display_id)) {
    $output['error'] = 'Export failed.';
    drupal_json_output($output);
    exit;
  }

  $exposed_input = $_GET;
  unset($exposed_input['q']);
  unset($exposed_input['_']);
  unset($exposed_input['attach']);
  $exposed_input = array_filter($exposed_input);
  $view->set_exposed_input($exposed_input);

  $view->set_display($display_id);
  $view->pre_execute();
  $data = $view->render($display_id);

  // Make sure data is UTF-8 with BOM for Microsoft Excel compliance
  if (function_exists('mb_detect_encoding')) {
    $encoding = mb_detect_encoding($data);
    if ($encoding != 'UTF-8') {
      $new_data = drupal_convert_to_utf8($data, $encoding);
      if ($new_data) {
        $data = $new_data;
      }
    }
  }
  if (strncmp($data, "\xEF\xBB\xBF", 3)) {
    $data = "\xEF\xBB\xBF" . $data;
  }

  // Prepare the destination directory.
  $directory = 'public://temp-downloads';
  if (!file_prepare_directory($directory, FILE_CREATE_DIRECTORY)) {
    $output['error'] = 'Failed to create directory. Please try again later or contact site administrator.';
  }

  // file_unmanaged_save_data() deals with file name collisions.
  $uri = $directory . '/activities-' . REQUEST_TIME . '.csv';

  // @see drupal_delete_file_if_stale()
  if ($path = file_unmanaged_save_data($data, $uri, $replace = FILE_EXISTS_RENAME)) {
    $output['file_path'] = $path;

    $output['file_url'] = file_create_url($path);

    $output['file_size'] = _ampapi_human_filesize($path);
    $output['markup'] = l(t('Download CSV'), $output['file_url']) . ' <em>(' . t('size: !size', array('!size' => $output['file_size'])) . ')</em>';
  }
  else {
    $output['error'] = 'Failed to create file. Please try again later or contact site administrator.';
  }

  drupal_json_output($output);
}

