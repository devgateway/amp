<?php

// Provide a list of modules to be installed.
$modules = array(
  'efq_views',

  'ampapi',
  'ampapi_activity',
);
_us_module__install($modules);

// Prepare a list of features to be installed.
$feature_names = array(
  'ampactivities',
);
_us_features__install($feature_names);
