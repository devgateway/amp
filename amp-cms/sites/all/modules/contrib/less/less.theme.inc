<?php

/**
 * @file
 * Contains alterations to theme settings forms.
 */

/**
 * Implements hook_form_FORM_ID_alter() for system_theme_settings().
 */
function less_form_system_theme_settings_alter(&$form, &$form_state) {
  preg_match('/^theme_([A-Za-z0-9_]+)_settings$/', $form['var']['#value'], $matches);
  
  // only show settings on forms for themes, not "Global settings"
  if (!empty($matches)) {
    $theme_key = $matches[1];
    
    $saved_settings = theme_get_setting('less', $theme_key);
    
    $less_settings_form = array();
    
    $theme_items = system_list('theme');
    
    if (isset($theme_items[$theme_key]) && !empty($theme_items[$theme_key]->info['less']['vars'])) {
      $less_settings_form[$theme_key] = _less_setting_form_element($theme_key, $theme_items[$theme_key]->info['less']['vars'], $saved_settings[$theme_key]);
    }
    
    // showing settings form for modules
    $system_items = system_list('module_enabled');
    
    $less_module_settings = array();
    
    foreach ($system_items as $system_item_name => $system_item) {
      if (!empty($system_item->info['less']['vars'])) {
        $less_module_settings[$system_item_name] = _less_setting_form_element($system_item_name, $system_item->info['less']['vars'], $saved_settings[$system_item_name], $system_item->info['name']);
      }
    }
    
    if (!empty($less_module_settings)) {
      
      $less_settings_form['modules'] = array(
        '#type' => 'fieldset',
        '#title' => t('Modules'),
        '#collapsible' => TRUE,
        '#collapsed' => TRUE,
        '#parents' => array('less'), // make sure all settings are stored under the system name immediately inside the themes less settings array
      );
      
      $less_settings_form['modules'] += $less_module_settings;
      
    }
    
    if (!empty($less_settings_form)) {
      
      $form['less'] = array(
        '#type' => 'fieldset',
        '#title' => t('LESS settings'),
        '#tree' => TRUE,
        '#weight' => -2, // only for debug purposes
        '#description' => t('These settings are per theme. Delete a value to use the default.'),
        '#collapsible' => TRUE,
        '#collapsed' => TRUE,
      );
      
      $form['less'] += $less_settings_form;
    }
    
    $form['less']['tokens'] = array(
      '#theme' => 'token_tree',
      '#token_types' => array('all'),
    );
    
  }
}

function _less_setting_form_element($system_name, $less_settings, $saved_settings, $title = NULL) {
  $form = array();
  
  $form = array(
    '#type' => 'fieldset',
    '#title' => isset($title) ? $title : $system_name,
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );
  
  foreach ($less_settings as $var_name => $var_value) {
    $form[$var_name] = array(
      '#type' => 'textfield',
      '#title' => $var_name,
      '#default_value' => !empty($saved_settings[$var_name]) ? $saved_settings[$var_name] : NULL,
      '#description' => 'Default value: ' . $var_value, // @TODO remove this line/change to something else?
      '#attributes' => array('placeholder' => $var_value),
    );
  }
  
  return $form;
}
