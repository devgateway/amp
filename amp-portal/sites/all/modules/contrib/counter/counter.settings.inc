<?php
/**
 * @file
 * Settings page callback file for the counter module.
 */

/**
 * Menu callback;
 */
 
function counter_admin_settings() {
  $form = array();
  // only administrators can access this function
  
  // Generate the form - settings applying to all patterns first
  $form['counter_settings'] = array(
    '#type' => 'fieldset',
    '#weight' => -30,
    '#title' => t('Basic settings'),
    '#collapsible' => TRUE,
    '#collapsed' => FALSE    
  );
  
  $form['counter_settings']['counter_show_site_counter'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Site Counter'),
    '#default_value' => variable_get('counter_show_site_counter', 1)
  );    
    
  $form['counter_settings']['counter_show_unique_visitor'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Unique Visitors'),
    '#default_value' => variable_get('counter_show_unique_visitor', 1),
    '#description' => t('Show Unique Visitors based on their IP')
  );
  
  $form['counter_settings']['counter_registered_user'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Registered Users'),
    '#default_value' => variable_get('counter_registered_user', 1)
  );
  
  $form['counter_settings']['counter_unregistered_user'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Unregistered Users'),
    '#default_value' => variable_get('counter_unregistered_user', 1)
  );
  
  $form['counter_settings']['counter_blocked_user'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Blocked Users'),
    '#default_value' => variable_get('counter_blocked_user', 1)
  );
  
  $form['counter_settings']['counter_published_node'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Published Nodes'),
    '#default_value' => variable_get('counter_published_node', 1)
  );
  
  $form['counter_settings']['counter_unpublished_node'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Unpublished Nodes'),
    '#default_value' => variable_get('counter_unpublished_node', 1)
  );
  
  $form['counter_settings']['counter_show_server_ip'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Web Server IP'),
    '#default_value' => variable_get('counter_show_server_ip', 1)
  );
  
  $form['counter_settings']['counter_show_ip'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Client IP'),
    '#default_value' => variable_get('counter_show_ip', 1)
  );
 
  $form['counter_settings']['counter_show_counter_since'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Site Counter Since'),
    '#default_value' => variable_get('counter_show_counter_since', 1),
    '#description' => t('Show the first entry date in the Site Counter')
  );

  $form['counter_statistic'] = array(
    '#type' => 'fieldset',
    '#weight' => -20,
    '#title' => t('Statistic settings'),
    '#collapsible' => TRUE,
    '#collapsed' => FALSE    
  );
  $form['counter_statistic']['counter_statistic_today'] = array(
    '#type' => 'checkbox',
    '#title' => t('Visitors Today'),
    '#default_value' => variable_get('counter_statistic_today', 1)
  );
  $form['counter_statistic']['counter_statistic_week'] = array(
    '#type' => 'checkbox',
    '#title' => t('Visitors This Week'),
    '#default_value' => variable_get('counter_statistic_week', 1)
  );
  $form['counter_statistic']['counter_statistic_month'] = array(
    '#type' => 'checkbox',
    '#title' => t('Visitors This Month'),
    '#default_value' => variable_get('counter_statistic_month', 1)
  );
  $form['counter_statistic']['counter_statistic_year'] = array(
    '#type' => 'checkbox',
    '#title' => t('Visitors This Year'),
    '#default_value' => variable_get('counter_statistic_year', 1)
  );
  
  return system_settings_form($form);
}

function counter_settings_advanced() {  
  $form['counter_advanced'] = array(
    '#type' => 'fieldset',
    '#weight' => -20,
    '#title' => t('Advanced settings'),
    '#collapsible' => TRUE,
    '#collapsed' => FALSE
  );
  
  $form['counter_advanced']['counter_skip_admin'] = array(
    '#type' => 'checkbox',
    '#title' => t('Skip admin'),
    '#default_value' => variable_get('counter_skip_admin', 0),
    '#description' => t("Do not count when visitor is admin (uid=1).")
  );  
  
  $form['counter_advanced']['counter_refresh_delay'] = array(
      '#type' => 'textfield',
      '#title' => t('Delay before refresh counter data (in second)'),
      '#default_value' => variable_get('counter_refresh_delay', 10),
      '#description' => t("Delay before re-calculate counter data, otherwise read from previous value."),
      );
      
  $form['counter_advanced']['counter_insert_delay'] = array(
      '#type' => 'textfield',
      '#title' => t('Delay before next insert (in second)'),
      '#default_value' => variable_get('counter_insert_delay', 1),
      '#description' => t("Wait for certain second before next insert. Increase this value if your server can not handle too much data recording. Set to 0 for no delay."),
      );
  
  return system_settings_form($form);
}

function counter_settings_initial() {     
  $form['counter_initial'] = array(
    '#type' => 'fieldset',
    '#weight' => -10,
    '#title' => t('Initial Values'),
    '#collapsible' => TRUE,
    '#collapsed' => FALSE,
    '#description' => t("Set initial values for Site Counter.")
  );
  
  $form['counter_initial']['counter_initial_counter'] = array(
    '#type' => 'textfield',
    '#title' => t('Initial value of Site Counter'),
    '#default_value' => variable_get('counter_initial_counter', 0),
    '#description' => t('Initial value of Site Counter')
  );
  
  $form['counter_initial']['counter_initial_unique_visitor'] = array(
    '#type' => 'textfield',
    '#title' => t('Initial value of Unique Visitor'),
    '#default_value' => variable_get('counter_initial_unique_visitor', 0),
    '#description' => t('Initial value of Unique Visitor')
  );
  
  $form['counter_initial']['counter_initial_since'] = array(
    '#type' => 'textfield',
    '#title' => t("Replace 'Since' value with this string"),
    '#default_value' => variable_get('counter_initial_since', ''),
    '#description' => t("If you leave this field blank then Counter module will use data from first inserted row of Counter table. This field type is textfield, so you can enter: '2008-08-12 or 12 August 2008 06:39'.")
  );
  
  return system_settings_form($form);
}

