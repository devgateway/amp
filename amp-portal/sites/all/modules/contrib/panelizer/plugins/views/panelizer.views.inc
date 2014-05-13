<?php

/**
 * @file Panelizer views integration code.
 */

/**
 * Implementation of hook_views_data_alter().
 */
function panelizer_views_data_alter(&$data) {
  $data['node']['panelizer_link'] = array(
    'field' => array(
      'title' => t('Panelizer link'),
      'help' => t('Provide a link to panelizer-related operations on the content.'),
      'handler' => 'panelizer_handler_field_node_link',

    ),
  );
}

/**
 * Implementation of hook_views_handlers().
 */
function panelizer_views_handlers() {
  return array(
    'info' => array(
      'path' => drupal_get_path('module', 'panelizer') . '/plugins/views',
    ),
    'handlers' => array(
      // field handlers
      'panelizer_handler_field_node_link' => array(
        'parent' => 'views_handler_field_node_link',
      ),
    ),
  );
}

/**
 * Implementation of hook_views_plugins().
 */
function panelizer_views_plugins() {
  $path = drupal_get_path('module', 'panelizer') . '/plugins/views';
  return array(
    'row' => array(
      'panelizer_node_view' => array(
        'title' => t('Panelizer display'),
        'help' => t('Render nodes using the panels display for any that have been panelized.'),
        'handler' => 'panelizer_plugin_row_panelizer_node_view',
        'parent' => 'node',
        'base' => array('node'), // only works with 'node' as base.
        'path' => $path,
        'uses options' => TRUE,
        'type' => 'normal',
      ),
    ),
  );
}
