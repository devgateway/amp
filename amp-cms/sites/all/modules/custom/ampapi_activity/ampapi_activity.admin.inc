<?php

/**
 * @file
 * This file contains search settings form functions.
 */

/**
 * Page callback for 'admin/settings/search'.
 */
function ampapi_activity_search_settings_form($form, &$form_state) {
  $search_settings = ampapi_activity_get_search_settings();

  $form['ampapi_activity_search_settings'] = array(
    '#tree' => TRUE,
  );

  $form_elements['ssc'] = array(
    '#type' => 'fieldset',
    '#title' => t('SSC Integration'),
    '#collapsible' => FALSE,
    '#collapsed' => FALSE,
    '#parents' => array('ampapi_activity_search_settings'), // Remove the fieldset from the submitted values structure.
  );

  $form_elements['ssc']['ssc_status'] = array(
    '#type' => 'checkbox',
    '#title' => t('Enable South-South Cooperation search and widgets.'),
    '#default_value' => $search_settings['ssc_status'],
  );


  $full_columns_list = array();
  $measures_list = array();
  $mappers = ampapi_get_mappers();
  foreach ($mappers as $key => $plugin) {
    if (empty($plugin['property name'])) {
      continue;
    }

    // Hide SSC specific fields when SSC functionality is disabled.
    // TODO: Update the form via ajax and toggle SSC fields visibility.
    if (!$search_settings['ssc_status'] && !empty($plugin['report field ssc'])) {
      //continue;
    }

    if ($plugin['report field group'] == 'measures') {
      $measures_list[$plugin['property name']] = $plugin['title'];
    }

    $full_columns_list[$plugin['property name']] = $plugin['title'];
  }
  ksort($full_columns_list);

  $form_elements['columns'] = array(
    '#type' => 'fieldset',
    '#title' => t('Enabled Columns'),
    '#description' => t('A list of columns to be displayed in the search results table.'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
    '#parents' => array('ampapi_activity_search_settings'), // Remove the fieldset from the submitted values structure.
  );

  // We always want the activity id and title to be visible.
  $controllable_columns_list = $full_columns_list;
  unset($controllable_columns_list['id']);
  unset($controllable_columns_list['title']);

  $notes = theme('item_list', array('items' => array(
    t('Only columns that are also enabled in AMP will be displayed.'),
    t('For SSC columns to be displayed the SSC integration needs to be enabled.'),
  )));
  $form_elements['columns']['enabled_columns'] = array(
    '#type' => 'checkboxes',
    '#title' => t('Enabled search columns'),
    '#description' => t('Please select the enabled columns. NOTES: !notes', array('!notes' => $notes)),
    '#options' => $controllable_columns_list,
    '#default_value' => $search_settings['enabled_columns'],
    '#element_validate' => array('ampapi_activity_search_enabled_columns_validate'),
  );

  $form_elements['sort'] = array(
    '#type' => 'fieldset',
    '#title' => t('Default Sort'),
    '#description' => t('<h4>Feature not implemented yet.</h4>') . t("The column on which the results should be sorted by default if the user didn't change the sort."),

    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
    '#parents' => array('ampapi_activity_search_settings'), // Remove the fieldset from the submitted values structure.
  );

  // TODO: Allow administrators to change the default sort.
  $form_elements['sort']['default_sort'] = array(
    '#disabled' => TRUE,
    '#type' => 'radios',
    '#title' => t('Default Sort'),
    '#description' => t('Please select the default sort column.'),
    '#options' => $full_columns_list,
    '#default_value' => $search_settings['default_sort'],
    '#element_validate' => array('ampapi_activity_search_enabled_columns_validate'),
  );

  $form['ampapi_activity_search_settings'] += $form_elements;
  $form['#submit'][] = 'ampapi_activity_search_settings_form_submit';

  return system_settings_form($form);
}

/**
 * Form element validation handler for enabled_columns checkboxes.
 */
function ampapi_activity_search_enabled_columns_validate($element, &$form_state, $form) {
  // Only save the list of enabled columns.
  $key = 'ampapi_activity_search_settings';
  $enabled_columns = $form_state['values'][$key]['enabled_columns'];
  $form_state['values'][$key]['enabled_columns'] = array_filter($enabled_columns);
}


/**
 * Form submission handler for ampapi_activity_search_settings_form().
 */
function ampapi_activity_search_settings_form_submit($form, &$form_state) {
  views_invalidate_cache();
}
