<?php

/**
 * @file
 * Custom layout definition.
 */

/**
 * Implements hook_panels_layouts().
 */
$plugin = array(
  'title' => t('DoubleLayout'),
  'category' => t('Custom Layouts'),
  'icon' => 'icon.png',
  'theme' => 'panels_doublelayout',
  // CSS is excluded because of the need to create a single aggregated CSS.
  //'css' => 'style.css',
  'admin css' => '../custom.admin.css',
  'regions' => array(
    'left' => t('Left'),
    'right' => t('Right'),
  ),
);
