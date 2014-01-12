<?php

/**
 * @file
 *   A Views field handler for users' IP address.
 */

/**
 * Restricts access to viewing the IP address field.
 * There doesn't seem to be a standard for what permissions are necessary.
 */
class record_shorten_views_handler_field_hostname extends views_handler_field {
  function options_form(&$form, &$form_state) {
    parent::options_form($form, $form_state);
    $form['notice'] = array(
      '#value' => '<p class="form-item"><strong>' .
        t('Users must have both the "administer users" and "access site reports" permission to view this field.')
        . '</strong></p>',
      '#weight' => -10,
    );
  }
  function render($values) {
    if (!user_access('administer users') || !user_access('access site reports')) {
      return;
    }
    return parent::render($values);
  }
}
