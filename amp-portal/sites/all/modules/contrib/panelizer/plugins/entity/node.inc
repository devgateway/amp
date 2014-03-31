<?php
/**
 * @file
 * Definition of the node plugin.
 */

$plugin = array(
  'handler' => 'PanelizerEntityNode',
  'entity path' => 'node/%node',
  'hooks' => array(
    'menu' => TRUE,
    'admin_paths' => TRUE,
    'permission' => TRUE,
    'panelizer_defaults' => TRUE,
    'default_page_manager_handlers' => TRUE,
  ),
);
