<?php

/**
 * @file
 * Contains the administration pages for LESS.
 */

/**
 * Form for LESS module settings.
 */
function less_settings_form($form, &$form_state) {

  $form['less_flush'] = array(
    '#type' => 'fieldset',
    '#collapsible' => FALSE,
    '#value' => t('Click this button regenerate all LESS files once.'),
  );

  $form['less_flush']['flush'] = array(
    '#type' => 'submit',
    '#submit' => array('_flush_less'),
    '#value' => t('Flush LESS files'),
  );
  
  $less_engines = array(
    libraries_detect('less.js'),
    libraries_detect('less.php'),
    libraries_detect('lessphp'),
  );
  
  $form['less_engine'] = array(
    '#type' => 'radios',
    '#title' => t('LESS engine'),
    '#options' => array(),
    '#required' => TRUE,
    '#default_value' => variable_get('less_engine', 'lessphp'),
  );
  
  foreach ($less_engines as $less_engine) {
    
    $form['less_engine']['#options'][$less_engine['name']] = $less_engine['name'];
    
    $form['less_engine'][$less_engine['name']] = array(
      '#type' => 'radio',
      '#title' => t('@engine_name - <a href="@vendor_url">@vendor_url</a>', array('@engine_name' => $less_engine['name'], '@vendor_url' => $less_engine['vendor url'])),
      '#return_value' => $less_engine['name'],
      '#description' => t('Missing - Click vendor link above to read installation instructions.'),
      '#disabled' => !($less_engine['installed']),
    );
    
    if ($less_engine['installed']) {
      $form['less_engine'][$less_engine['name']]['#description'] = t('v%version Installed', array('%version' => $less_engine['version']));
    }
    
  }
  
  $form['less_devel'] = array(
    '#type' => 'checkbox',
    '#title' => t('LESS developer mode'),
    '#description' => t('Enable developer mode to ensure LESS files are regenerated every page load.'),
    '#default_value' => variable_get('less_devel', FALSE),
  );
  
  $form['less_watch'] = array(
    '#type' => 'checkbox',
    '#title' => t('LESS watch mode'),
    '#description' => t('Enable watch mode while developer mode is active to automatically reload styles when changes are detected, including changes to @import-ed files. Does not cause a page reload.'),
    '#default_value' => variable_get('less_watch', TRUE),
    '#states' => array(
      'enabled' => array(
        ':input[name="less_devel"]' => array('checked' => TRUE),
      ),
    ),
  );
  
  $form['#submit'] = array('less_settings_form_submit');
  
  return system_settings_form($form);
}

/**
 * Form submission function.
 * 
 * Trigger clear of LESS module cache data.
 */
function less_settings_form_submit($form, &$form_state) {
  cache_clear_all('less:', 'cache', TRUE);
}

/**
 * Submit handler for cache clear button.
 */
function _flush_less($form, &$form_state) {

  _less_get_dir(TRUE);
  cache_clear_all('less:', 'cache', TRUE);

  drupal_set_message(t('LESS files cache cleared.'), 'status');
}
