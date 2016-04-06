<?php

/**
 * @file
 * owlcarousel.admin.inc
 */

/**
 * Owl Carousel instance settings overview.
 */
function owlcarousel_settings_form() {
  drupal_add_library('system', 'drupal.ajax');
  variable_include();

  $build['groups'] = array(
    '#type' => 'vertical_tabs',
  );

  $group_variables = variable_list_group('owlcarousel');
  $instances = owlcarousel_instance_callback_list();

  // Per instance setting variables.
  foreach ($instances as $instance) {
    foreach ($group_variables as $var) {
      $instance_variables[$instance][$var['name']] = variable_get_child($var['name'], $instance);
    }
    $build['groups'][$instance] = array(
      '#type' => 'fieldset',
      '#title' => $instance,
      '#collapsible' => TRUE,
      '#collapsed' => TRUE,
      '#id' => $instance,
    );
    $build['groups'][$instance]['actions'] = array(
      '#type' => 'container',
      '#weight' => 10,
    );
    $build['groups'][$instance]['actions']['edit'] = array(
      '#type' => 'markup',
      '#markup' => l(t('Edit'), 'admin/config/user-interface/owlcarousel/' . $instance, array('attributes' => array(
        'class' => array('button')
      ))),
      '#weight' => 1,
    );
    $build['groups'][$instance]['actions']['export'] = array(
      '#type' => 'markup',
      '#markup' => l(t('Export'), 'admin/config/user-interface/owlcarousel/' . $instance . '/export', array('attributes' => array(
        'class' => array('button')
      ))),
      '#weight' => 2,
    );
    if ($instance != 'owlcarousel_settings_default') {
      $build['groups'][$instance]['actions']['delete'] = array(
        '#type' => 'markup',
        '#markup' => l(t('Delete'), 'admin/config/user-interface/owlcarousel/' . $instance . '/delete/nojs', array('attributes' => array(
          'class' => array('button', 'use-ajax')))),
        '#weight' => 3,
      );
    }

    // List per instance variables.
    $build['groups'][$instance]['list'] = owlcarousel_variable_admin_group_list($instance_variables, $instance);
  }

  // Add & remove instance settings.
  $build['groups']['owlcarousel_settings_add'] = array(
    '#type' => 'fieldset',
    '#title' => t('Add settings group'),
    '#collapsible' => TRUE, 
    '#collapsed' => TRUE,
    '#weight' => -10,
  );
  $build['groups']['owlcarousel_settings_add']['name'] = array(
    '#type' => 'textfield',
    '#title' => t('Name'),
    '#description' => t('Identifier for group of settings.'),
    '#required' => TRUE,
  );
  $build['groups']['owlcarousel_settings_add']['actions'] = array(
    '#type' => 'container',
    '#title' => t('Add new instance settings group'),
  );
  // @todo, validation.
  $build['groups']['owlcarousel_settings_add']['actions']['add'] = array(
    '#type' => 'submit',
    '#value' => t('Add group'),
    '#submit' => array('owlcarousel_variable_settings_import_form_submit'),
  );
  $build['groups']['owlcarousel_settings_add']['actions']['import'] = array(
    '#type' => 'markup',
    '#markup' => l(t('Import'), 'admin/config/user-interface/owlcarousel/import', array('attributes' => array(
      'class' => array('button')
    ))),
  );

  return $build;
}

/**
 * Instance form validation handler.
 */
function owlcarousel_settings_form_validate($form, $form_state) {
  if (strlen($form_state['values']['name']) >= 65) {
    form_set_error('name', t('The group name is too long.'));
  }
}

/**
 * Build list of variables for each instance group.
 */
function owlcarousel_variable_admin_group_list($variables, $instance) {
  $list = array();

  foreach ($variables[$instance] as $name => $variable) {
    $type = variable_get_type($variable['type']);
    $list[$instance][$variable['title']] = array(
      '#type' => $type['title'],
      '#title' => l($variable['title'], 'admin/config/user-interface/owlcarousel/' . $instance, array('fragment' => 'edit-' . drupal_html_id($variable['name']))),
      '#markup' => variable_format_value($variable),
      '#description' => isset($variable['description']) ? $variable['description'] : '',
    );
  }

  return array(
    '#theme' => 'table',
    '#rows' => $list[$instance],
  );
}

/**
 * Import settings form.
 */
function owlcarousel_variable_settings_import_form($form, $form_state) {
  $form['container'] = array(
    '#type' => 'fieldset',
    '#title' => t('Settings Import'),
  );
  $form['container']['name'] = array(
    '#type' => 'textfield',
    '#title' => t('Name'),
    '#description' => t('Identifier for group of settings.'),
    '#required' => TRUE,
  );
  $form['container']['import'] = array(
    '#type' => 'textarea',
    '#description' => t('Paste list of carousel instance setting variables.'),
    '#rows' => 10,
  );

  $form['actions'] = array(
    '#type' => 'container',
    '#title' => t('Add new instance settings group'),
  );
  // @todo, validation.
  $form['actions']['add'] = array(
    '#type' => 'submit',
    '#value' => t('Add group'),
  );
    
  return $form;
}

/**
 * Instance form validation handler.
 */
function owlcarousel_variable_settings_import_form_validate($form, $form_state) {
  if (strlen($form_state['values']['name']) >= 65) {
    form_set_error('name', t('The group name is too long =(.'));
  }
}

/**
 * Submit handler, variable settings form import.
 *
 * @todo, merge submit function with import condition.
 */
function owlcarousel_variable_settings_import_form_submit($form, $form_state) {
  $message = t('Name already exists as group of settings identifier.');
  $result = FALSE;

  // Readable name.
  $name = $form_state['values']['name'];

  // Machine name.
  $machine_name = 'owlcarousel_settings_' . drupal_clean_css_identifier(drupal_strtolower($name));

  // Check if instance name already exists
  $result = variable_get($machine_name);
  if (!$result) {
    $message = t('New group @name has been added.', array('@name' => $name));

    variable_set($machine_name, $name);
  }

  if (isset($form_state['values']['import'])) {
    $message = t('New group @name has been imported.', array('@name' => $name));

    $variables = drupal_json_decode($form_state['values']['import']);
    owlcarousel_create_instance_settings($machine_name, $variables);
  }

  drupal_set_message($message, 'status');
}

/**
 * Per carousel instance settings form.
 */
function owlcarousel_instance_settings_form($instance) {
  module_load_include('form.inc', 'variable_realm');
  $form = drupal_get_form('variable_group_form', 'owlcarousel', $instance);
  $form['#submit'][] = 'owlcarousel_variable_settings_submit';

  return $form;
}

/**
 * Additional variable settings form submit handler.
 */
function owlcarousel_variable_settings_submit($form, &$form_state) {
  $form_state['redirect'] = 'admin/config/user-interface/owlcarousel';

  // Clear settings cache.
  cache_clear_all('*', 'cache', TRUE);
}

/**
 * Menu callback, export Owl Carousel instance settings.
 */
function owlcarousel_export_instance_settings($instance) {
  require_once(DRUPAL_ROOT . '/includes/utility.inc');
  $variables = _owlcarousel_return_carousel_instance_settings($instance);

  $form['container'] = array(
    '#type' => 'fieldset',
    '#title' => t('Settings Export'),
  );
  $form['container']['export'] = array(
    '#type' => 'textarea',
    '#rows' => 10,
    '#value' => drupal_json_encode($variables),
    '#description' => t('List of carousel instance setting variables.'),
  );

  return $form;
}

/**
 * Create an instance group with a pre-defined group of settings.
 */
function owlcarousel_create_instance_settings($instance, $variables) {
  // Set settings identifier variable.
  variable_set_value($instance, $instance);

  foreach ($variables as $name => $variable) {
    $str_name = drupal_strtolower(preg_replace('/(?<=\\w)(?=[A-Z])/', '_$1', $name));
    variable_set_value('owlcarousel_' . $str_name . '_' . $instance, $variable);

    if (is_array($variable)) {
      foreach ($variable as $key => $value) {
        if ($key) {
          variable_set_value('owlcarousel_' . $str_name . '_' . $instance . '_items', $value);
        }
        else {
          variable_set_value('owlcarousel_' . $str_name . '_' . $instance . '_width', $value);
        }
      }
    }
    else {
      variable_set_value('owlcarousel_' . $str_name . '_' . $instance, $variable);
    }
  }
}

/** 
 * Menu callback, delete Owl Carousel instance settings.
 */
function owlcarousel_delete_instance_settings($id = NULL, $js) {
  $message = t('Settings group @id could not be found.', array('@id' => $id));

  if ($id) {
    variable_del($id);
    $message = t('Settings group @id has been deleted.', array('@id' => $id));

    if ($js == 'ajax') {
      $message = drupal_set_message($message, 'status');

      $commands = array();
      $commands[] = ajax_command_remove('#' . $id);
      $commands[] = ajax_command_remove('#owlcarousel-settings-form .vertical-tab-button.selected');
      $commands[] = ajax_command_prepend('#page', theme('status_messages', $message));
      $page = array('#type' => 'ajax', '#commands' => $commands);
      ajax_deliver($page);
      exit;
    }
  }

  drupal_set_message($message, 'status');
  // Return user to settings overview.
  drupal_goto('admin/config/user-interface/owlcarousel');
}
