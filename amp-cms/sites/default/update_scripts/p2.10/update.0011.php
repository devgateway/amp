<?php

// Provide a list of modules to be installed.
$modules = array(
  'chr',
  'restclient',

  'efq_views',
  'views_data_export',

  'contentwidget',

  'ampapi',
  'ampapi_activity',
);
_us_module__install($modules);

// Clear system caches.
drupal_flush_all_caches();

// Prepare a list of features to be installed.
$feature_names = array(
  'ampactivities',
);
_us_features__install($feature_names);

