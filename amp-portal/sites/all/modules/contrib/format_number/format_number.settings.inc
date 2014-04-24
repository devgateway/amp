<?php

/**
 * @file
 * Implement module settings and user settings forms.
 */

/**
 * Module settings form.
 */
function format_number_site_settings_form() {
  $form = array();
  $form['format_number_decimal_point'] = array(
    '#type' => 'radios',
    '#title' => t('Decimal point'),
    '#options' => format_number_get_decimal_point_options(),
    '#default_value' => variable_get('format_number_decimal_point', '.'),
    '#description' => t('Select the character that will be used as decimal point.'),
  );
  $form['format_number_thousands_sep'] = array(
    '#type' => 'radios',
    '#title' => t('Thousands separator'),
    '#options' => format_number_get_thousands_separator_options(),
    '#default_value' => variable_get('format_number_thousands_sep', ','),
    '#description' => t('Select the character that will be used as thousands separator.'),
  );
  $form['format_number_user_configurable'] = array(
    '#type' => 'radios',
    '#title' => t('User-configurable number format'),
    '#default_value' => variable_get('format_number_user_configurable', 0),
    '#options' => array(t('Disabled'), t('Enabled')),
    '#description' => t('When enabled, users can set their own number formatting options.'),
  );
  $form['#validate'] = array('format_number_site_settings_form_validate');
  return system_settings_form($form);
}

/**
 * Validate the module settings form.
 */
function format_number_site_settings_form_validate($form, &$form_state) {
  if ($form_state['values']['format_number_decimal_point'] == $form_state['values']['format_number_thousands_sep']) {
    form_set_error('format_number_thousands_sep', t('Decimal point and thousands separator cannot be defined to use the same symbol.'));
  }
}

/**
 * User settings form.
 *
 * @param array $edit
 *   The array of form values submitted by the user.
 * @return array
 *   A $form array containing the form elements to display.
 */
function format_number_user_settings_form(&$edit) {
  $form = array();
  $form['format_number'] = array(
    '#type' => 'fieldset',
    '#title' => t('Number format settings'),
    '#weight' => 7,
    '#collapsible' => TRUE, '#collapsed' => FALSE,
  );
  $form['format_number']['decimal_point'] = array(
    '#type' => 'radios',
    '#title' => t('Decimal point'),
    '#options' => format_number_get_decimal_point_options(),
    '#default_value' => (isset($edit['decimal_point']) && drupal_strlen($edit['decimal_point']) ? $edit['decimal_point'] : variable_get('format_number_decimal_point', '.')),
    '#description' => t('Select the character that will be used as decimal point.'),
  );
  $form['format_number']['thousands_sep'] = array(
    '#type' => 'radios',
    '#title' => t('Thousands separator'),
    '#options' => format_number_get_thousands_separator_options(),
    '#default_value' => (isset($edit['thousands_sep']) && drupal_strlen($edit['thousands_sep']) ? $edit['thousands_sep'] : variable_get('format_number_thousands_sep', ',')),
    '#description' => t('Select the character that will be used as thousands separator.'),
  );
  return $form;
}

/**
 * Validate the user settings form.
 */
function format_number_user_settings_form_validate($form, &$form_state) {
  $edit = &$form_state['values'];
  if (isset($edit['decimal_point']) && $edit['decimal_point'] == $edit['thousands_sep']) {
    form_set_error('thousands_sep', t('Decimal point and thousands separator cannot be defined to use the same symbol.'));
  }
}
