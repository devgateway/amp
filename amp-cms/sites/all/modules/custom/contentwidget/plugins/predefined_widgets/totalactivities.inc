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
  'hidden' => TRUE,
  'title' => 'Total Activities',
  'description' => 'Will display the total commitments, disbursements and the total number of activities in system.',
  'render callback' => 'contentwidget_totalactivities_widget_render',
  'settings form' => 'contentwidget_totalactivities_widget_settings',
  'settings storage' => 'contentwidget_totalactivities_widget_settings_storage',
  'resource api path' => 'data/report/custom/paginate',
  'defaults' => array(
  ),
);

function contentwidget_totalactivities_widget_retrieve_data($plugin, $widget_settings, $langcode = NULL) {
  $api_status = helpergeneric_check_api_status();
  if (!$api_status['online']) {
    drupal_set_message($api_status['message'], 'warning');
    return;
  }

  $langcode = (empty($langcode) ? LANGUAGE_NONE : $langcode);
  $cache_id = "contentblock:widget:{$langcode}:totalactivities:" . md5(serialize($widget_settings));

  // Try to get the response from cache.
  $report_totals = array();
  if ($cached = cache_get($cache_id, 'cache')) {
    $report_totals = $cached->data;
    return $report_totals;
  }
  else {
    // Prepare a list of parameters for the request.
    $parameters = array();
    if ($langcode != LANGUAGE_NONE) {
      $parameters['language'] = $langcode;
    }

    $variables = array();
    $variables['headers'] = array(
      'Accept' => 'application/json',
      'Content-Type' => 'application/json',
    );

    $request_body = array(
      'add_columns' => array(),
      'add_measures' => array(),
    );

    $id_plugin = ampapi_get_mapper_by_property_name('id');
    $request_body['add_columns'][] = $id_plugin['report field key'];

    $actual_commitments_plugin = ampapi_get_mapper_by_property_name('actual_commitments');
    $request_body['add_measures'][] = $actual_commitments_plugin['report field key'];

    $actual_disbursements_plugin = ampapi_get_mapper_by_property_name('actual_disbursements');
    $request_body['add_measures'][] = $actual_disbursements_plugin['report field key'];

    $request_body['page'] = 1;
    $request_body['recordsPerPage'] = 1;
    $request_body['regenerate'] = TRUE;
    $request_body['rowTotals'] = TRUE;
    $request_body['name'] = 'api:totalactivities';

    $api_settings = helpergeneric_get_api_settings();

    // Handle the global "Humanitarian Aid" filter.
    // @see helpergeneric_api_settings_form()
    if (!empty($api_settings['humanitarian_aid_only'])) {
      $humanitarian_aid_plugin = ampapi_get_mapper_by_property_name('humanitarian_aid');
      $class_name = ctools_plugin_get_class($humanitarian_aid_plugin, 'handler');
      /** @var activityMapperOptionsInterface $handler */
      $handler = new $class_name();

      // Only display data related to humanitarian aid.
      // NOTE: Filters for the custom reports endpoint need to be inside a "filters" key.
      $ha_key = $humanitarian_aid_plugin['report field key'];
      $ha_value = $handler->getMatchedValue($humanitarian_aid_plugin, TRUE);
      $request_body['filters']['columnFilters'][$ha_key] = array($ha_value);
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

    // Set the API Resource Endpoint.
    $resource_path = $plugin['resource api path'];
    if (!empty($parameters)) {
      $resource_path .= '?' . drupal_http_build_query($parameters);
    }

    // Make a GET request.
    $response = restclient_post($resource_path, $variables);
  }

  // @TODO: Add support for more response codes.
  if (restclient_response_code($response) != RESTCLIENT_RESPONSE_SUCCESS) {
    drupal_set_message(t('Could not retrieve data from the API. Please try again later or check the API Settings.'), 'error');
    return;
  }

  if (empty($response->data)) {
    drupal_set_message(t('No data retrieved from the API. Please try again later or check the API Settings.'), 'error');
    return;
  }

  $response_data = json_decode($response->data);

  if (!empty($response_data->error)) {
    $t_args = array();
    $t_args['%message'] = reset($response_data->error);
    drupal_set_message(t('The API returned the following status message: %message', $t_args), 'error');;
    return;
  }

  // Get the number formatting pattern.
  $number_format = NULL;
  $currency = '';
  foreach ($response_data->settings as $info) {
    if ($info->id == 'ammountPattern') {
      $number_format = $info->value;
    }

    // @HACK: The id for currency is "1"!!!
    // @see: Documentation for /rest/gis/settings
    if ($info->id == '1') {
      $currency = $info->value->defaultId;
    }
  }
  if (is_null($number_format)) {
    $number_format = '###0.00';
  }

  // @see https://php.net/manual/en/numberformatter.create.php
  // @see http://www.icu-project.org/apiref/icu4c/classDecimalFormat.html#details
  $formatter_locale = NULL;
  $formatter = new NumberFormatter($formatter_locale, NumberFormatter::PATTERN_DECIMAL, $number_format);

  foreach ($response_data->headers as $h_info) {
    // @TODO: use mappers.
    if (!in_array($h_info->hierarchicalName, array('[Actual Commitments]', '[Actual Disbursements]'))) {
      continue;
    }

    if (!isset($response_data->page->pageArea->contents->{$h_info->hierarchicalName}->value)) {
      continue;
    }

    if ($formatter) {
      $value = $response_data->page->pageArea->contents->{$h_info->hierarchicalName}->value;
      // Display configuration in millions.
      // @TODO: Add configuration options.
      $value = $value / 1000 / 1000;

      $amount = '<span class="amount">' . $formatter->format($value) . '</span> ';
      $amount .= '<span class="text">' . t('Millions !currencysymbol', array('!currencysymbol' => $currency)) . '</span>';
    }
    else {
      $amount = check_plain($response_data->page->pageArea->contents->{$h_info->hierarchicalName}->displayedValue);
    }

    switch ($h_info->hierarchicalName) {
      case '[Actual Commitments]':
        $report_totals['actual_commitments'] = $amount;
        break;
      case '[Actual Disbursements]':
        $report_totals['actual_disbursements'] = $amount;
        break;
    }
  }

  if (!empty($response_data->page->totalRecords)) {
    $report_totals['activities_count'] = '<span class="amount">' . intval($response_data->page->totalRecords) . '</span> ';
  }

  // 1 hours cache life time for requests should be plenty for now.
  $expire = 3600;
  cache_set($cache_id, $report_totals, 'cache', REQUEST_TIME + $expire);

  return $report_totals;
}

/**
 * Render callback for the 'Top Donors' predefined widget.
 */
function contentwidget_totalactivities_widget_render($plugin, $widget_settings, $entity, $langcode) {
  // Provide some default values.
  $widget_settings += $plugin['defaults'];

  $data = contentwidget_totalactivities_widget_retrieve_data($plugin, $widget_settings, $langcode);
  if (empty($data)) {
    return;
  }

  $info_blocks = array(
    'actual_commitments' => 'Total Commitments',
    'actual_disbursements' => 'Total Disbursements',
    'activities_count' => 'Total Activities',
  );

  $build = array();

  // Create the table headers.
  foreach ($info_blocks as $key => $label) {
    $build[$key] = array(
      '#type' => 'container',
      '#attributes' => array(
        'class' => array(
          'infoblock',
          drupal_html_class($key),
        ),
      ),
    );

    $build[$key]['label'] = array(
      '#prefix' => '<h3 class="header">',
      '#suffix' => '</h3>',
      '#markup' => t($label),
    );

    $build[$key]['info'] = array(
      '#type' => 'container',
      '#children' => $data[$key],
      '#attributes' => array(
        'class' => array('info'),
      ),
    );
  }

  return array('#markup' => drupal_render($build));
}

/**
 * Settings form for the 'Top Donors' predefined widget.
 */
function contentwidget_totalactivities_widget_settings(&$element, &$form_state, $plugin, $widget_settings) {
  $settings_form = array();
  return $settings_form;
}

/**
 * Returns an serialized string for storing the widget settings in the database.
 */
function contentwidget_totalactivities_widget_settings_storage($widget_name, $form_values) {
  $settings = array();
  return serialize($settings);
}
