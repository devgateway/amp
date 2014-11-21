<?php

/**
 * @file
 * Administration pages for restclient module
 */

/**
 * Administration form for restclient module
 */
function restclient_admin_settings($form, &$form_state) {

  $options = array(
    RESTCLIENT_LIBRARY_DRUPAL => t('Drupal'),
    RESTCLIENT_FILES_SYSTEM => t('Testing Files'),
  );

  if (module_exists('chr')) {
    $options[RESTCLIENT_LIBRARY_CURL] = t('cURL');
  }

  $form['restclient_active_library'] = array(
    '#type' => 'radios',
    '#title' => t('Active Library'),
    '#description' => t('Select which function to use to make HTTP requests. cURL is more feature rich and supports more authentication methods however it requires php-curl to be installed. Drupal has an internal HTTP request function which will work on any installation but has fewer options. Defaults to Drupal.'),
    '#options' => $options,
    '#default_value' => variable_get('restclient_active_library', RESTCLIENT_LIBRARY_DRUPAL),
  );

  $form['location'] = array(
    '#type' => 'fieldset',
    '#title' => t('Location'),
    '#description' => t('Enter the location of the server.'),
    '#collapsible' => FALSE,
    '#collapsed' => FALSE,
  );
  $form['location']['restclient_hostname'] = array(
    // @todo change to URL field
    '#type' => 'textfield',
    '#title' => t('Hostname'),
    '#description' => t('The hostname for the server with no trailing slash. Ex: http://localhost:80/rest'),
    '#size' => 40,
    '#maxlength' => 255,
    '#default_value' => isset($form_state['values']['restclient_hostname']) ? $form_state['values']['restclient_hostname'] : variable_get('restclient_hostname', 'http://localhost'),
    // @todo add attributes to store pattern to check in browser
  );

  $form['restclient_watchdog'] = array(
    '#title' => t('Log all error to Watchdog'),
    '#type' => 'checkbox',
    '#default_value' => isset($form_state['values']['restclient_watchdog']) ? $form_state['values']['restclient_watchdog'] : variable_get('restclient_watchdog', TRUE),
  );

  $form['restclient_debug'] = array(
    '#title' => t('Enable Debug Mode'),
    '#type' => 'checkbox',
    '#default_value' => isset($form_state['values']['restclient_debug']) ? $form_state['values']['restclient_debug'] : variable_get('restclient_debug', FALSE),
  );

  $form['restclient_testing'] = array(
    '#title' => t('Capture Test Data'),
    '#type' => 'checkbox',
    '#default_value' => isset($form_state['values']['restclient_testing']) ? $form_state['values']['restclient_testing'] : variable_get('restclient_testing', FALSE),
  );

  $form['restclient_filepath'] = array(
    '#type' => 'textfield',
    '#title' => t('File Path'),
    '#description' => t('Path to save the text files.'),
    '#default_value' => isset($form_state['values']['restclient_filepath']) ? $form_state['values']['restclient_filepath'] : variable_get('restclient_filepath', "private://restclient_testing"),
    '#states' => array(
      'invisible' => array(
        ':input[name="restclient_testing"]' => array('checked' => FALSE),
      ),
    ),
  );

  $form['#access'] = array('administer restclient');
  $form['#validate'][] = 'restclient_admin_settings_validate';

  return system_settings_form($form);
}

/**
 * Validation callback
 */
function restclient_admin_settings_validate($form, &$form_state) {
  $values = $form_state['values'];

  // Check for hostname
  // @todo change to valid_url function
  // @see api.drupal.org
  if (!filter_var($values['restclient_hostname'], FILTER_VALIDATE_URL, FILTER_FLAG_SCHEME_REQUIRED)) {
    form_set_error('restclient_hostname', t('Invalid URL'));
  }
}

