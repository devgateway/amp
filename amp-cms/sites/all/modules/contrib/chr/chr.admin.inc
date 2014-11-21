<?php

/**
 * @file
 * Admin settings
 */

/**
 * Form settings array
 */
function chr_admin_settings_form($form, &$form_state) {
  $form['#access'] = array('administer chr');
  
  $form['chr_override_drupal_http_request'] = array(
    '#type' => 'checkbox',
    '#title' => t('Override Drupal HTTP Request'),
    '#description' => t('Set all HTTP requests to use cURL instead. Requires Drupal 7.21 or higher.'),
    '#disabled' => !(VERSION >= 7.21),
    '#default_value' => variable_get('chr_override_drupal_http_request', FALSE),
  );
  
  $form['chr_debug'] = array(
    '#type' => 'checkbox',
    '#title' => t('Enable Debug Output'),
    '#description' => t('Display all HTTP requests/response objects via Devel.'),
    '#default_value' => variable_get('chr_debug'),
    '#access' => module_exists('devel'),
  );

  // @todo add proxy settings (see README)

  $form['#submit'][] = 'chr_admin_settings_form_submit';

  return system_settings_form($form);
}

/**
 * Submit callback
 */
function chr_admin_settings_form_submit(&$form, &$form_state) {
  $values =& $form_state['values'];
  
  // Check if override is enabled
  if ($values['chr_override_drupal_http_request'] == TRUE) {
    variable_set('chr_original_http_request_function_value', variable_get('drupal_http_request_function', FALSE));
    variable_set('drupal_http_request_function', 'chr_curl_http_request');
  }
  else {
    // Restore original override
    variable_set('drupal_http_request_function', variable_get('chr_original_http_request_function_value', FALSE));
  }
}