<?php

// Update module weight.
db_update('system')
  ->fields(array('weight' => 20))
  ->condition('name', 'helpergeneric')
  ->execute();

// Update module weight.
db_update('system')
  ->fields(array('weight' => 20))
  ->condition('name', 'helpertheme')
  ->execute();

// Update module weight.
db_update('system')
  ->fields(array('weight' => 20))
  ->condition('name', 'helperviews')
  ->execute();

// Provide a list of modules to be installed.
$modules = array(
  'contextual',

  'elements',

  'contentblock',
  'helpergeneric',
  'helpertheme',
  'helperviews',
);
_us_module__install($modules);

// Automatically replace legacy fields with regular fields for new bundles.
$title_settings = array(
  'auto_attach' => array(
    'title' => 'title',
  ),
  'hide_label' => array(
    'entity' => 'entity',
    'page' => 0,
  ),
);
variable_set('title_contentblock', $title_settings);

// Clear system caches.
drupal_flush_all_caches();
