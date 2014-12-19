<?php

/**
 * @file
 * Provides a custom Last Updated widget.
 */

/**
 * Plugins are described by creating a $plugin array which will be used
 * by the system that includes this file.
 */
$plugin = array(
  'title' => 'Last Updated',
  'description' => 'Will display a list of Last Updated activities.',
  'render callback' => 'contentwidget_lastupdated_widget_render',
  'settings form' => 'contentwidget_lastupdated_widget_settings',
  'settings storage' => 'contentwidget_lastupdated_widget_settings_storage',
  'retrieve data' => 'contentwidget_lastupdated_widget_retrieve_data',
  'defaults' => array(
    'period' => '12',
    'count' => '10',
    'sort' => 'default',
    'visible_columns' => array(),
  ),
);

function contentwidget_lastupdated_widget_retrieve_data($widget_settings, $langcode = NULL) {
  $data = array();

  $langcode = (empty($langcode) ? LANGUAGE_NONE : $langcode);
  $cache_id = "contentblock:widget:{$langcode}:lastupdated:" . md5(serialize($widget_settings));

  // Try to get the response from cache.
  if ($cached = cache_get($cache_id, 'cache')) {
   $response = $cached->data;
  }
  else {
    $api_status = helpergeneric_check_api_status();
    if (!$api_status['online']) {
      drupal_set_message($api_status['message'], 'warning');
      return $data;
    }

    // Prepare a list of parameters for the request.
    $parameters = array();

    if (!empty($widget_settings['count'])) {
      $parameters['count'] = $widget_settings['count'];
    }
    if (!empty($langcode) && $langcode != LANGUAGE_NONE) {
      $parameters['language'] = $langcode;
    }

    $variables = array();
    $variables['headers'] = array(
      'Accept' => 'application/json',
      'Content-Type' => 'application/json',
    );
    // Required, even though we pass the parameters in the URL like for GET requests.
    $variables['body'] = '{}';

    // Set the API Resource Endpoint.
    $resource_path = 'gis/last-updated';

    // Make a POST request.
    $response = restclient_post($resource_path . '?' . drupal_http_build_query($parameters), $variables);
  }

  // @TODO: Add support for more response codes.
  if (restclient_response_code($response) == RESTCLIENT_RESPONSE_SUCCESS) {
    if (!empty($response->data)) {
      // 1 hours cache life time for requests should be plenty for now.
      $expire = 3600;
      cache_set($cache_id, $response, 'cache', REQUEST_TIME + $expire);
      $data = json_decode($response->data);
    }
    else {
      drupal_set_message(t('No data retrieved from the API. Please try again later or check the API Settings.'), 'error');
    }
  }
  else {
    drupal_set_message(t('Could not retrieve data from the API. Please try again later or check the API Settings.'), 'error');
  }

  return $data;
}

/**
 * Render callback for the 'Last Updated' predefined widget.
 */
function contentwidget_lastupdated_widget_render($plugin, $widget_settings, $entity, $langcode) {
  // Provide some default values.
  $widget_settings += $plugin['defaults'];

  $data = contentwidget_lastupdated_widget_retrieve_data($widget_settings, $langcode);

  $header = array();
  if (!empty($data)) {
    if (empty($data->headers) ||  empty($data->headers[0])) {
      drupal_set_message(t('Headers information missing from the API.'), 'error');
      return NULL;
    }

    // Create the table headers.
    foreach ($data->headers[0] as $key => $header_text) {
      $header[$key] = array(
        'data' => check_plain($header_text),
        'class' => array(
          drupal_html_class('col-' . $key)
        ),
      );
    }
  }

  $rows = array();
  if (isset($data->activities)) {
    $rows = _contentwidget_lastupdated_get_data_rows($data->activities);
  }

  $build = array();
  $build['lastupdated'] = array(
    '#theme' => 'table',
    '#header' => $header,
    '#rows' => $rows,
    '#attributes' => array(
      'class' => array(
        'contentblock-widget',
        'contentblock-widget-lastupdated',
        'styled-lightblue-table',
      ),
    ),
    '#empty' => t('No data available.'),
    '#sticky' => FALSE,
    '#prefix' => '<div class="large-table-wrapper">',
    '#suffix' => '</div>',
  );

  return array('#markup' => drupal_render($build));
}

/**
 * Settings form for the 'Last Updated' predefined widget.
 */
function contentwidget_lastupdated_widget_settings(&$element, &$form_state, $plugin, $widget_settings) {
  $settings_form = array();

  // Provide some default values.
  $widget_settings += $plugin['defaults'];
  $form_state['lastupdated'] = array(
    'field_name' => $element['#field_name'],
    'language' => $element['#language'],
    'input_name_prefix' => $element['#field_name'] . '[' . $element['#language'] . '][settings][lastupdated]',
  );
  $form_state['lastupdated']['wrapper_id'] = drupal_html_class($form_state['lastupdated']['input_name_prefix'] . '-options');

  $settings_form['count'] = array(
    '#type' => 'select',
    '#title' => t('Projects Count'),
    '#description' => t('The number of projects to display in the widget.'),
    '#default_value' => $widget_settings['count'],
    '#options' => drupal_map_assoc(array(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)),
  );

  return $settings_form;
}

/**
 * Returns a list of rows for theme_table().
 */
function _contentwidget_lastupdated_get_data_rows($items) {
  $rows = array();

  foreach ($items as $delta => $values) {
    $row = array();
    $count = 0;
    foreach ((array) $values as $value) {
      $row_value = check_plain($value);
      $row[$count] = array(
        'data' => $row_value,
        'class' => array(
          drupal_html_class('col-' . $count)
        ),
      );
      $count++;
    }

    if (!empty($row)) {
      $rows[$delta] = $row;
    }
  }

  return $rows;
}

/**
 * Returns an serialized string for storing the widget settings in the database.
 */
function contentwidget_lastupdated_widget_settings_storage($widget_name, $form_values) {
  $settings = array();

  if (isset($form_values[$widget_name]) && is_array($form_values[$widget_name])) {
    $settings = array_filter($form_values[$widget_name]);
    $settings = $form_values[$widget_name];
  }

  return serialize($settings);
}
