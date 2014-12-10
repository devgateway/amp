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
  'title' => t('Embedded Search Form'),
  'single' => TRUE,
  'defaults' => array(
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
  $block = new stdClass();
  $block->module = 'ampapi_activity';


  $svgicon = helpertheme_get_svg_icons('search', array('width' => '2em', 'height' => '2em'));
  $block->title = '<div class="title">' . $svgicon . t('Search Activities') . '</div>';
  $block->title_is_xss_free = TRUE;

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
    $exposed_form->options['submit_button'] = t('Search');
    $output['form']['#markup'] = $exposed_form->render_exposed_form(TRUE);
  }
  $view->destroy();

  $block->delta = 'embeddedform-' . $name . '-' . $display_id;
  $block->content = drupal_render($output);
  return $block;
}

/**
 * The form to add or edit a node as content.
 */
function ampapi_activity_embeddedform_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  return $form;
}

/**
 * Returns the administrative title for a node.
 */
function ampapi_activity_embeddedform_content_type_admin_title($subtype, $conf) {
  return t('Embedded Search Form');
}
