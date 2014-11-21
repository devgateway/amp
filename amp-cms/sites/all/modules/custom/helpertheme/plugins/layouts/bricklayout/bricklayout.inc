<?php

/**
 * @file
 * Custom layout definition.
 */

/**
 * Implements hook_panels_layouts().
 */
$plugin = array(
  'title' => t('BrickLayout'),
  'category' => t('Custom Layouts'),
  'icon' => 'icon.png',
  'theme' => 'panels_bricklayout',
  // CSS is excluded because of the need to create a single aggregated CSS.
  //'css' => 'style.css',
  'admin css' => '../custom.admin.css',
  'regions' => array(
    'col_a1' => t('Column A1'),
    'col_b1' => t('Column B1'),
    'col_c1' => t('Column C1'),
    'col_c2' => t('Column C2'),
    'col_d1' => t('Column D1'),
    'col_d2' => t('Column D2'),
  ),
);
