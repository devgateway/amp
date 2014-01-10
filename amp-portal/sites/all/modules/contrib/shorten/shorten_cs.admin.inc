<?php

/**
 * @file
 *   Provides the configuration page for Shorten URLs Custom Services.
 */

/**
 * Themes the configuration page.
 */
function theme_shorten_cs_admin() {
  return shorten_cs_services_table() . drupal_render(drupal_get_form('shorten_cs_add_form'));
}

/**
 * Displays the table of existing custom services.
 */
function shorten_cs_services_table() {
  $header = array(t('Name'), t('URL'), t('Type'), t('XML/JSON tag'), t('Actions'));
  $rows = array();
  $result = db_query("SELECT * FROM {shorten_cs} ORDER BY name ASC")->fetchAll();
  foreach ($result as $service) {
    $service = (array) $service;
    $service = array(
      'sid' => $service['sid'],
      'name' => check_plain($service['name']),
      'url' => check_plain($service['url']),
      'type' => $service['type'],
      'tag' => check_plain($service['tag']),
    );
    $service['actions'] = l(t('edit'), 'admin/config/services/shorten/custom/edit/' . $service['sid']) . ' ' .
      l(t('delete'), 'admin/config/services/shorten/custom/delete/' . $service['sid']);
    unset($service['sid']);
    $rows[] = $service;
  }
  if (!empty($rows)) {
    return theme('table', array('header' => $header, 'rows' => $rows));
  }
  return '';
}

/**
 * Builds the form to add a new custom service.
 */
function shorten_cs_add_form($form, &$form_state) {
  drupal_add_js(drupal_get_path('module', 'shorten_cs') . '/shorten_cs.js');
  if (!isset($form) || !is_array($form)) {
    $form = array();
  }
  $form += array('#attributes' => array('class' => 'shorten-cs-apply-js'));
  $form['name'] = array(
    '#type' => 'textfield',
    '#title' => t('Title'),
    '#description' => t('The name of the service'),
    '#required' => TRUE,
  );
  $form['url'] = array(
    '#type' => 'textfield',
    '#title' => t('API endpoint URL'),
    '#description' => t('The URL of the API endpoint, with parameters, such that the long URL can be appended to the end.') . ' ' .
      t('For example, the endpoint for TinyURL is') . ' <code>http://tinyurl.com/api-create.php?url=</code>. ' .
      t('Appending a long URL to the endpoint and then visiting that address will return data about the shortened URL.'),
    '#required' => TRUE,
  );
  $form['type'] = array(
    '#type' => 'radios',
    '#title' => t('Response type'),
    '#description' => t('The type of response the API endpoint returns.'),
    '#required' => TRUE,
    '#default_value' => 'text',
    '#options' => array(
      'text' => t('Text'),
      'xml' => 'XML',
      'json' => 'JSON',
    ),
  );
  $form['tag'] = array(
    '#type' => 'textfield',
    '#title' => t('XML tag or JSON key'),
    '#description' => t('The XML tag or JSON key that identifies the full short URL in the API response.') . ' ' .
      t('Only required for XML and JSON response types.'),
  );
  $form['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Save'),
  );
  return $form;
}

/**
 * Validates the form to add a new custom service.
 */
function shorten_cs_add_form_validate($form, $form_state) {
  $v = $form_state['values'];
  if (($v['type'] == 'xml' || $v['type'] == 'json') && empty($v['tag'])) {
    form_set_error('type', t('An XML tag or JSON key is required for services with a response type of XML or JSON.'));
  }
  $exists = db_query("SELECT COUNT(sid) FROM {shorten_cs} WHERE name = :name", array(':name' => $v['name']))->fetchField();
  if ($exists > 0) {
    form_set_error('name', t('A service with that name already exists.'));
  }
  else {
    $all_services = module_invoke_all('shorten_service');
    $all_services['none'] = t('None');
    foreach ($all_services as $key => $value) {
      if ($key == $v['name']) {
        form_set_error('name', t('A service with that name already exists.'));
        break;
      }
    }
  }
}

/**
 * Submits the form to add a new custom service.
 */
function shorten_cs_add_form_submit($form, $form_state) {
  $v = $form_state['values'];
  $record = array();
  foreach (array('name', 'url', 'type', 'tag') as $key) {
    $record[$key] = $v[$key];
  }
  drupal_write_record('shorten_cs', $record);
}

/**
 * Returns HTML representing the shorten service edit form.
 */
function shorten_cs_edit_form($service) {
  return drupal_render(drupal_get_form('shorten_cs_edit', $service));
}

/**
 * Builds the form to edit a custom service.
 */
function shorten_cs_edit($form, &$form_state, $service) {
  $form = shorten_cs_add_form($form, $form_state);
  foreach (array('name', 'url', 'type', 'tag') as $key) {
    $form[$key]['#default_value'] = $service->{$key};
  }
  $form['sid'] = array(
    '#type' => 'value',
    '#value' => $service->sid,
  );
  $form['old_name'] = array(
    '#type' => 'value',
    '#value' => $service->name,
  );
  return $form;
}

/**
 * Validates the form to edit a custom service.
 */
function shorten_cs_edit_validate($form, $form_state) {
  $v = $form_state['values'];
  if (($v['type'] == 'xml' || $v['type'] == 'json') && empty($v['tag'])) {
    form_set_error('type', t('An XML tag or JSON key is required for services with a response type of XML or JSON.'));
  }
  $exists = db_query("SELECT COUNT(sid) FROM {shorten_cs} WHERE name = :name AND sid <> :sid", array(':name' => $v['name'], ':sid' => $v['sid']))->fetchField();
  if ($exists > 0) {
    form_set_error('name', t('A service with that name already exists.'));
  }
  else {
    $all_services = module_invoke_all('shorten_service');
    $all_services['none'] = t('None');
    foreach ($all_services as $key => $value) {
      if ($key == $v['name']) {
        form_set_error('name', t('A service with that name already exists.'));
        break;
      }
    }
  }
}

/**
 * Submits the form to edit a service.
 */
function shorten_cs_edit_submit($form, $form_state) {
  $v = $form_state['values'];
  $record = array();
  foreach (array('name', 'url', 'type', 'tag', 'sid') as $key) {
    $record[$key] = $v[$key];
  }
  drupal_write_record('shorten_cs', $record, 'sid');
  if ($v['old_name'] == variable_get('shorten_service', 'is.gd')) {
    variable_set('shorten_service', $v['name']);
  }
  if ($v['old_name'] == variable_get('shorten_service_backup', 'TinyURL')) {
    variable_set('shorten_service', $v['name']);
  }
  drupal_set_message(t('The changes to service %service have been saved.', array('%service' => $record['name'])));
  $_GET['destination'] = 'admin/config/services/shorten/custom';
}

/**
 * Returns HTML representing the shorten service delete form.
 */
function shorten_cs_delete_form($service) {
  return drupal_render(drupal_get_form('shorten_cs_delete', $service));
}

/**
 * Builds the form to delete a custom service.
 */
function shorten_cs_delete($form, &$form_state, $service) {
  $form['service'] = array(
    '#type' => 'value',
    '#value' => $service->name,
  );
  $form['#submit'] = array('shorten_cs_delete_submit');
  return confirm_form($form, t('Are you sure you want to delete the custom service %service?', array('%service' => $service->name)), 'admin/config/services/shorten/custom');
}

/**
 * Submits the form to delete a custom service.
 */
function shorten_cs_delete_submit($form, $form_state) {
  $service = $form_state['values']['service'];
  if ($service == variable_get('shorten_service', 'is.gd')) {
    if (variable_get('shorten_service_backup', 'TinyURL') == 'is.gd') {
      variable_set('shorten_service', 'TinyURL');
    }
    else {
      variable_set('shorten_service', 'is.gd');
    }
    drupal_set_message(t('The default URL shortening service was deleted, so it has been reset to @service.', array('@service' => variable_get('shorten_service', 'is.gd'))));
  }
  if ($service == variable_get('shorten_service_backup', 'TinyURL')) {
    if (variable_get('shorten_service', 'is.gd') == 'TinyURL') {
      variable_set('shorten_service_backup', 'is.gd');
    }
    else {
      variable_set('shorten_service_backup', 'TinyURL');
    }
    drupal_set_message(t('The backup URL shortening service was deleted, so it has been reset to @service.', array('@service' => variable_get('shorten_service_backup', 'TinyURL'))));
  }
  db_delete('shorten_cs')
    ->condition('name', $service)
    ->execute();
  drupal_set_message(t('The service %service has been deleted.', array('%service' => $service)));
  $_GET['destination'] = 'admin/config/services/shorten/custom';
}
