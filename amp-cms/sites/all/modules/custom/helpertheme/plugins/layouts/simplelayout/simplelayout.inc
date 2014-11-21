<?php

/**
 * @file
 * Custom layout definition.
 */

/**
 * Implements hook_panels_layouts().
 */
$plugin = array(
  'title' => t('SimpleLayout'),
  'category' => t('Custom Layouts'),
  'icon' => 'icon.png',
  'theme' => 'panels_simplelayout',
  // CSS is excluded because of the need to create a single aggregated CSS.
  //'css' => 'style.css',
  'admin css' => '../custom.admin.css',
  'regions' => array(
    'main' => t('Main'),
  ),
);
