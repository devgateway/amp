<?php

/**
 * @file
 * Provides a custom Top Donors widget.
 */

/**
 * Plugins are described by creating a $plugin array which will be used
 * by the system that includes this file.
 */
$plugin = array(
  'title' => 'Top Donors',
  'description' => 'Will display a list of top donors by commitments or disbursements size during a selected time period.',
  'render callback' => 'contentwidget_topdonors_widget_render',
  'settings form' => 'contentwidget_topdonors_widget_settings',
  'settings storage' => 'contentwidget_topdonors_widget_settings_storage',
  'resource api path' => 'public/donorFunding',
  'defaults' => array(
    'period' => '12',  // NOTE: This is passed to the API as "months".
    'count' => '10',
    'type' => 'commitments',
  ),
);

function contentwidget_topdonors_widget_retrieve_data($plugin, $widget_settings, $langcode = NULL) {
  $data = array();

  $api_status = helpergeneric_check_api_status();
  if (!$api_status['online']) {
    return $data;
  }

  $langcode = (empty($langcode) ? LANGUAGE_NONE : $langcode);
  $cache_id = "contentblock:widget:{$langcode}:topdonors:" . md5(serialize($widget_settings));

  // Try to get the response from cache.
  if ($cached = cache_get($cache_id, 'cache')) {
    $response = $cached->data;
  }
  else {
    // Prepare a list of parameters for the request.
    $parameters = array();

    if (!empty($widget_settings['period'])) {
      $parameters['months'] = $widget_settings['period'];
    }
    if (!empty($widget_settings['count'])) {
      $parameters['count'] = $widget_settings['count'];
    }
    if ($langcode != LANGUAGE_NONE) {
      $parameters['language'] = $langcode;
    }

    if (!empty($widget_settings['type'])) {
      // @TODO: Request and API Endpoint for this.
      switch ($widget_settings['type']) {
        case 'disbursements':
          $parameters['fundingType'] = 2;
          break;

        case 'commitments':
        default:
          $parameters['fundingType'] = 1;
          break;
      }
    }

    $variables = array();
    $variables['headers'] = array(
      'Accept' => 'application/json',
      'Content-Type' => 'application/json',
    );

    $request_body = array();

    $api_settings = helpergeneric_get_api_settings();

    // Handle the global "Humanitarian Aid" filter.
    // @see helpergeneric_api_settings_form()
    if (!empty($api_settings['humanitarian_aid_only'])) {
      $humanitarian_aid_plugin = ampapi_get_mapper_by_property_name('humanitarian_aid');
      $class_name = ctools_plugin_get_class($humanitarian_aid_plugin, 'handler');
      /** @var activityMapperOptionsInterface $handler */
      $handler = new $class_name();

      // Only display data related to humanitarian aid.
      $ha_key = $humanitarian_aid_plugin['report field key'];
      $ha_value = $handler->getMatchedValue($humanitarian_aid_plugin, TRUE);
      $request_body['columnFilters'][$ha_key] = array($ha_value);
    }

    // Handle the global "Disaster Response Marker" filter.
    // @see helpergeneric_api_settings_form()
    if (!empty($api_settings['disaster_response_marker_only'])) {
      $disaster_response_marker_plugin = ampapi_get_mapper_by_property_name('disaster_response_marker');
      $class_name = ctools_plugin_get_class($disaster_response_marker_plugin, 'handler');
      /** @var activityMapperOptionsInterface $handler */
      $handler = new $class_name();

      // Only display data related to humanitarian aid.
      $drm_key = $disaster_response_marker_plugin['report field key'];
      $drm_value = $handler->getMatchedValue($disaster_response_marker_plugin, TRUE);
      $request_body['columnFilters'][$drm_key] = array($drm_value);
    }

    $variables['body'] = empty($request_body) ? '{}' : json_encode($request_body);

    // Make a GET request.
    $response = restclient_post($plugin['resource api path'] . '?' . drupal_http_build_query($parameters), $variables);
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
 * Render callback for the 'Top Donors' predefined widget.
 */
function contentwidget_topdonors_widget_render($plugin, $widget_settings, $entity, $langcode) {
  // Provide some default values.
  $widget_settings += $plugin['defaults'];

  $data = contentwidget_topdonors_widget_retrieve_data($plugin, $widget_settings, $langcode);

  $header = array();
  if (!empty($data)) {
    if (empty($data->headers)) {
      drupal_set_message(t('Headers information missing from the API.'), 'error');
      return NULL;
    }

    // Create the table headers.
    foreach ($data->headers as $key => $label) {
      if (!empty($data->Currency) && in_array($key, array('actual-commitments', 'actual-disbursements'))) {
        $label .= ' (' . $data->Currency . ')';
      }
      $header[$key] = array(
        'data' => check_plain($label),
        'class' => array(
          drupal_html_class('col-' . $key)
        ),
      );
    }
  }

  $rows = array();
  if (isset($data->donorFunding)) {
    $number_format = isset($data->numberformat) ? $data->numberformat : NULL;
    $rows = _contentwidget_topdonors_get_data_rows($data->donorFunding, $number_format);
  }

  $build = array();
  $build['topdonors'] = array(
    '#theme' => 'table',
    '#header' => $header,
    '#rows' => $rows,
    '#attributes' => array(
      'class' => array(
        'contentblock-widget',
        'contentblock-widget-topdonors',
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
 * Settings form for the 'Top Donors' predefined widget.
 */
function contentwidget_topdonors_widget_settings(&$element, &$form_state, $plugin, $widget_settings) {
  $settings_form = array();

  // Provide some default values.
  $widget_settings += $plugin['defaults'];

  $period_options = drupal_map_assoc(array(1, 2, 3, 4, 5, 6, 9, 12, 15, 18, 24, 30, 36));
  foreach ($period_options as $key => &$value) {
    $value = format_plural($value, 'Last month', 'Last @count months');
  }
  $settings_form['period'] = array(
    '#type' => 'select',
    '#title' => t('Period'),
    '#description' => t('Displays the top donors during the selected time period.'),
    '#default_value' => $widget_settings['period'],
    '#options' => array('' => t('No specific period')) + $period_options,
  );

  $settings_form['count'] = array(
    '#type' => 'select',
    '#title' => t('Donors Count'),
    '#description' => t('The number of donors to display in the widget.'),
    '#default_value' => $widget_settings['count'],
    '#options' => drupal_map_assoc(array(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)),
  );

  // @TODO: Request for an API Endpoint that returns the values for this filter.
  $settings_form['type'] = array(
    '#type' => 'select',
    '#title' => t('Funding Type'),
    '#description' => t('The type of funding.'),
    '#default_value' => $widget_settings['type'],
    '#options' => array(
      'commitments' => 'Commitments',
      'disbursements' => 'Disbursements',
    ),
  );

  return $settings_form;
}

/**
 * Returns a list of rows for theme_table().
 */
function _contentwidget_topdonors_get_data_rows($items, $number_format = NULL) {
  $rows = array();

  // @see https://php.net/manual/en/numberformatter.create.php
  // @see http://www.icu-project.org/apiref/icu4c/classDecimalFormat.html#details
  $formatter_locale = NULL;
  $formatter = new NumberFormatter($formatter_locale, NumberFormatter::PATTERN_DECIMAL, $number_format);

  foreach ($items as $delta => $values) {
    $row = array();
    foreach (array_keys((array) $values) as $key) {

      // Update amounts according to the AMP number format pattern.
      if (is_numeric($values->$key) && in_array($key, array('actual-commitments', 'actual-disbursements'))) {
        if ($formatter) {
          $values->$key = $formatter->format($values->$key);
        }
      }

      $row_value = check_plain($values->$key);
      $row[$key] = array(
        'data' => $row_value,
        'class' => array(
          drupal_html_class('col-' . $key)
        ),
      );
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
function contentwidget_topdonors_widget_settings_storage($widget_name, $form_values) {
  $settings = array();

  if (isset($form_values[$widget_name]) && is_array($form_values[$widget_name])) {
    $settings = array_filter($form_values[$widget_name]);
    $settings = $form_values[$widget_name];
  }

  return serialize($settings);
}
