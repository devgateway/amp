<?php

$plugin = array(
  'schema' => 'panelizer_defaults',
  'access' => 'administer panelizer',

  'menu' => array(
    'menu prefix' => 'admin/config/content/panelizer/%panelizer_handler',
    'menu item' => '%',
    'menu title' => 'Panelizer',
    'menu description' => 'Add, edit or delete custom content layouts.',
    'items' => array(
      'edit' => array('path' => 'list/%ctools_export_ui/settings'),
      'context' => array('path' => 'list/%ctools_export_ui/context'),
      'content' => array('path' => 'list/%ctools_export_ui/content'),
      'layout' => array('path' => 'list/%ctools_export_ui/layout'),
    ),
  ),

  'title singular' => t('panelizer default'),
  'title singular proper' => t('Panelizer default'),
  'title plural' => t('panelizer defaults'),
  'title plural proper' => t('Panelizer defaults'),

  'handler' => array(
    'class' => 'panelizer_defaults_ui',
  ),

  'allowed operations' => array(
    'edit' => array('title' => t('Settings')),
    'context' => array('title' => t('Context')),
    'layout' => array('title' => t('Layout')),
    'content' => array('title' => t('Content')),
  ),
);

