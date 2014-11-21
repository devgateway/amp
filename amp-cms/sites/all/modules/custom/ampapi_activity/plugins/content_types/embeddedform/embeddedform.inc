<?php

/**
 * @file
 * Plugin to handle the 'node' content type which allows individual nodes
 * to be embedded into a panel.
 */

/**
 * Plugins are described by creating a $plugin array which will be used
 * by the system that includes this file.
 */
$plugin = array(
  'title' => t('Embedded form'),
  'single' => TRUE,
  'defaults' => array(
    'form_callback' => '',
  ),
  'description' => t('Add an existing form to the panel.'),
  'category' => t('Custom'),
);

/**
 * Output function for the 'node' content type.
 *
 * Outputs a node based on the module and delta supplied in the configuration.
 */
function ampapi_activity_embeddedform_content_type_render($subtype, $conf, $panel_args) {
  $conf['form_callback'] = 'ampapi_activity_homepage_search_form';

  $block = new stdClass();
  $block->module = 'ampapi_activity';
  $block->delta = $conf['form_callback'];

  $block->title = '';

  $name = 'activities';
  $display_id = 'search_page';
  $view = views_get_view($name);
  if (empty($view)) {
    return;
  }

  if (!$view->access($display_id)) {
    return;
  }

  $view->set_display($display_id);
  $view->init_handlers();

  $output['form'] = array();
  if (isset($view->display_handler)) {
    $view->display_handler->set_option('exposed_block', TRUE);
    $exposed_form = $view->display_handler->get_plugin('exposed_form');
    $exposed_form->options['reset_button'] = FALSE;
    $exposed_form->options['submit_button'] = t('Search Activities');
    $output['form']['#markup'] = $exposed_form->render_exposed_form(TRUE);
  }
  $view->destroy();

  $block->content = drupal_render($output);
  return $block;
}

/**
 * The form to add or edit a node as content.
 */
function ampapi_activity_embeddedform_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  $form['form_callback'] = array(
    '#type' => 'textfield',
    '#default_value' => !empty($conf['form_callback']),
    '#title' => t('Form callback function'),
    '#description' => t('A function that returns a rendered form.'),
  );

  return $form;
}

/**
 * Validate the node selection.
 */
function  ampapi_activity_embeddedform_content_type_edit_form_validate(&$form, &$form_state) {
  if (empty($form_state['values']['form_callback']) || !function_exists($form_state['values']['form_callback'])) {
    form_error($form['form_callback'], t('Form does not exist.'));
  }
}

/**
 * Validate the node selection.
 */
function ampapi_activity_embeddedform_content_type_edit_form_submit($form, &$form_state) {
  foreach (array('form_callback') as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}

/**
 * Returns the administrative title for a node.
 */
function ampapi_activity_embeddedform_content_type_admin_title($subtype, $conf) {
  $form_callback = $conf['form_callback'];

  if ($form_callback) {
    if (function_exists($form_callback)) {
      return t('Form: @form_name', array('@form_name' => $form_callback));
    }
    else {
      return t('Form callabck is invalid: @form_name', array('@form_name' => $form_callback));
    }
  }

  return t('Pointing to deleted/missing form @form_name', array('@form_name' => $form_callback));
}
