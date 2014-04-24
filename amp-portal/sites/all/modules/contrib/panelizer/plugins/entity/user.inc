<?php
/**
 * @file
 * Definition of the user plugin.
 */

$plugin = array(
  'handler' => 'PanelizerEntityUser',
  'entity path' => 'user/%user',
  'hooks' => array(
    'menu' => TRUE,
    'admin_paths' => TRUE,
    'permission' => TRUE,
    'panelizer_defaults' => TRUE,
    'default_page_manager_handlers' => TRUE,
  ),
);
