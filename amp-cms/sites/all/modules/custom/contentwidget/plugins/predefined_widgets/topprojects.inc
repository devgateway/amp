<?php

/**
 * @file
 * Provides a custom Top Projects widget.
 */

/**
 * Plugins are described by creating a $plugin array which will be used
 * by the system that includes this file.
 */
$plugin = array(
  'title' => 'Top Projects',
  'description' => 'Will display a list of top projects (by commitment size) during a selected time period.',
  'render callback' => 'contentwidget_topprojects_widget_render',
  'settings form' => 'contentwidget_topprojects_widget_settings',
  'settings storage' => 'contentwidget_topprojects_widget_settings_storage',
  'resource api path' => 'public/topprojects',
  'defaults' => array(
    'period' => '12',  // NOTE: This is passed to the API as "months".
    'count' => '10',
    'sort' => 'default',
    'ssc' => FALSE,
    'visible_columns' => array(),
  ),
);

function contentwidget_topprojects_widget_retrieve_data($plugin, $widget_settings, $langcode = NULL) {
  $data = array();

  $api_status = helpergeneric_check_api_status();
  if (!$api_status['online']) {
    return $data;
  }

  $langcode = (empty($langcode) ? LANGUAGE_NONE : $langcode);
  $cache_id = "contentblock:widget:{$langcode}:topprojects:" . md5(serialize($widget_settings));

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

    // Display SSC projects instead.
    if (!empty($widget_settings['ssc']) && ampapi_activity_ssc_status()) {
      // Some magic strings, see the API docs.
      $request_body['reportType'] = 'C';
      $request_body['projectType'] = array('S');
    }

    $variables['body'] = empty($request_body) ? '{}' : json_encode($request_body);

    // Make a GET request.
    $response = restclient_post($plugin['resource api path'] . '?' . drupal_http_build_query($parameters), $variables);

    // We want to cache only successful responses.
    if (restclient_response_code($response) == RESTCLIENT_RESPONSE_SUCCESS) {
      // 1 hours cache life time for requests should be plenty for now.
      $expire = 3600;
      cache_set($cache_id, $response, 'cache', REQUEST_TIME + $expire);
    }
  }

  // @TODO: Add support for more response codes.
  if (restclient_response_code($response) == RESTCLIENT_RESPONSE_SUCCESS) {
    if (!empty($response->data)) {
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
 * Render callback for the 'Top Projects' predefined widget.
 */
function contentwidget_topprojects_widget_render($plugin, $widget_settings, $entity, $langcode) {
  // Provide some default values.
  $widget_settings += $plugin['defaults'];

  $data = contentwidget_topprojects_widget_retrieve_data($plugin, $widget_settings, $langcode);

  $header = array();
  if (!empty($data)) {
    if (empty($data->headers)) {
      drupal_set_message(t('Headers information missing from the API.'), 'error');

      return NULL;
    }

    if (empty($widget_settings['visible_columns'])) {
      $widget_settings['visible_columns'] = array_keys((array) $data->headers);
    }

    // Create the table headers.
    foreach ($widget_settings['visible_columns'] as $key) {
      if (!isset($data->headers->$key)) {
        continue;
      }

      $label = $data->headers->$key;

      // Add currency code to the columns that contain measures.
      // @TODO: Request and API update in order to remove this hardcoding.
      $currency_columns = array(
        'actual-commitments',
        'actual-disbursements',
        'bilateral-ssc-commitments',
        'triangular-ssc-commitments',
      );
      if (!empty($data->Currency) && in_array($key, $currency_columns)) {
        $label = $label . ' (' . $data->Currency . ')';
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
  if (isset($data->topprojects)) {
    $number_format = isset($data->numberformat) ? $data->numberformat : NULL;
    $rows = _contentwidget_topprojects_get_data_rows($widget_settings['visible_columns'], $data->topprojects, $number_format);
  }

  $build = array();
  $build['topprojects'] = array(
    '#theme' => 'table',
    '#header' => $header,
    '#rows' => $rows,
    '#attributes' => array(
      'class' => array(
        'contentblock-widget',
        'contentblock-widget-topprojects',
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
 * Settings form for the 'Top Projects' predefined widget.
 */
function contentwidget_topprojects_widget_settings(&$element, &$form_state, $plugin, $widget_settings) {
  $settings_form = array();

  // Provide some default values.
  $widget_settings += $plugin['defaults'];
  $form_state['topprojects'] = array(
    'field_name' => $element['#field_name'],
    'language' => $element['#language'],
    'input_name_prefix' => $element['#field_name'] . '[' . $element['#language'] . '][settings][topprojects]',
  );
  $form_state['topprojects']['wrapper_id'] = drupal_html_class($form_state['topprojects']['input_name_prefix'] . '-options');

  if (ampapi_activity_ssc_status()) {
    $settings_form['ssc'] = array(
      '#type' => 'checkbox',
      '#title' => t('South-South Cooperation'),
      '#description' => t('Display the top SSC projects instead.'),
      '#default_value' => $widget_settings['ssc'],
    );
  }
  else {
    $settings_form['ssc'] = array(
      '#type' => 'value',
      '#value' => FALSE,
    );
  }

  $period_options = drupal_map_assoc(array(1, 2, 3, 4, 5, 6, 9, 12, 15, 18, 24, 30, 36));
  foreach ($period_options as $key => &$value) {
    $value = format_plural($value, 'Previous month', 'Previous @count months');
  }
  $settings_form['period'] = array(
    '#type' => 'select',
    '#title' => t('Period'),
    '#description' => t('Displays the top projects during the selected time period.'),
    '#default_value' => $widget_settings['period'],
    '#options' => array('' => t('No specific period')) + $period_options,
  );

  $settings_form['count'] = array(
    '#type' => 'select',
    '#title' => t('Projects Count'),
    '#description' => t('The number of projects to display in the widget.'),
    '#default_value' => $widget_settings['count'],
    '#options' => drupal_map_assoc(array(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)),
  );

  $settings_form['sort'] = array(
    '#type' => 'select',
    '#title' => t('Default Sort'),
    '#description' => t('The order in which to display the projects.') . ' <b>(not implemented yet!)</b>',
    '#default_value' => $widget_settings['sort'],
    '#options' => array(
      'default' => t('Default'),
      'commitment-desc' => t('Commitment size descending'),
    ),
    '#disabled' => TRUE,
  );

  if (isset($form_state['input'][$element['#field_name']][$element['#language']][0]['settings']['topprojects'])) {
    $display_all_columns = (bool) $form_state['input'][$element['#field_name']][$element['#language']][0]['settings']['topprojects']['display_all_columns'];
  }
  else {
    $display_all_columns = empty($widget_settings['visible_columns']) ? TRUE : FALSE;
  }
  $settings_form['display_all_columns'] = array(
    '#type' => 'checkbox',
    '#title' => t('Display all columns'),
    '#description' => t('Whether or not to display all table columns. Un-check to select visible columns.'),
    '#default_value' => $display_all_columns,
    '#ajax' => array(
      'event' => 'click',
      'callback' => 'contentwidget_widget_ajax_callback',
      'wrapper' => $form_state['topprojects']['wrapper_id'],
      'progress' => 'throbber',
    ),
  );

  $settings_form['visible_columns'] = array(
    '#prefix' => '<div id="' . $form_state['topprojects']['wrapper_id'] . '" class="form-dependent-items-wrapper">',
    '#suffix' => '</div>',
  );

  if (empty($display_all_columns)) {
    $data = contentwidget_topprojects_widget_retrieve_data($widget_settings);

    if (!empty($data->headers)) {
      $available_columns = array();
      foreach ($data->headers as $key => $value) {
        $available_columns[check_plain($key)] = check_plain($value);
      }

      $settings_form['visible_columns'] += array(
        '#type' => 'checkboxes',
        '#title' => t('Visible Columns'),
        '#default_value' => $widget_settings['visible_columns'],
        '#description' => t('Select columns to display in the table.'),
        '#options' => $available_columns,
        '#multiple' => TRUE,
        '#states' => array(
          'visible' => array(
            ':input[name="' . $form_state['topprojects']['input_name_prefix'] . '[display_all_columns]"]' => array('checked' => FALSE),
          ),
        ),
      );
    }
  }

  return $settings_form;
}

/**
 * Returns a list of rows for theme_table().
 */
function _contentwidget_topprojects_get_data_rows($visible_columns, $items, $number_format = NULL) {
  $rows = array();

  // @see https://php.net/manual/en/numberformatter.create.php
  // @see http://www.icu-project.org/apiref/icu4c/classDecimalFormat.html#details
  $formatter_locale = NULL;
  $formatter = new NumberFormatter($formatter_locale, NumberFormatter::PATTERN_DECIMAL, $number_format);

  foreach ($items as $delta => $values) {
    $row = array();
    foreach ($visible_columns as $key) {
      // If the value is empty, we don't just skip it, but we add an empty cell.
      if (!isset($values->$key)) {
        $empty_value = '';
        // The key will tell us whether the value should be 0 or empty string.
        if (in_array($key, array('actual-commitments', 'actual-disbursements'))) {
          $empty_value = 0;
        }
        $row[$key] = array(
          'data' => $empty_value,
          'class' => array(
            drupal_html_class('col-' . $key)
          ),
        );
      }
      else {
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
    }

    if (!empty($row)) {
      $rows[$delta] = $row;
    }
  }

  return $rows;
}

/**
 * Ajax callback: Displays the options contents of the selected radioblock.
 */
function contentwidget_widget_ajax_callback($form, $form_state) {
  $commands = array();

  // Update the form elements via ajax.
  $field_name = $form_state['topprojects']['field_name'];
  $language = $form_state['topprojects']['language'];
  if (!empty($form_state['values'][$field_name][$language][0]['settings']['topprojects'])) {
    // Prepend messages to the form elements.
    $widget_settings_form = $form[$field_name][$language][0]['settings']['topprojects'];
    $widget_settings_form['visible_columns']['#prefix'] .= theme('status_messages');
    $markup = drupal_render($widget_settings_form['visible_columns']);

    $selector = '#' . $form_state['topprojects']['wrapper_id'];
    $commands[] = ajax_command_replace($selector, $markup);
  }

  return array('#type' => 'ajax', '#commands' => $commands);
}

/**
 * Returns an serialized string for storing the widget settings in the database.
 */
function contentwidget_topprojects_widget_settings_storage($widget_name, $form_values) {
  $settings = array();

  if (isset($form_values[$widget_name]) && is_array($form_values[$widget_name])) {
    $settings = $form_values[$widget_name];
  }

  if (empty($form_values[$widget_name]['display_all_columns']) && !empty($settings['visible_columns'])) {
    $settings['visible_columns'] = array_filter($settings['visible_columns']);
  }
  else {
    $settings['visible_columns'] = array();
  }

  return serialize($settings);
}
