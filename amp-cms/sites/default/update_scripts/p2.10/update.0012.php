<?php

// Provide a list of modules to be installed.
$modules = array(
  'captcha',
  'recaptcha',

  'entityform',
  'entity2text',

  'rules',
);
_us_module__install($modules);

// Clear system caches.
drupal_flush_all_caches();

// Prepare a list of features to be installed.
$feature_names = array(
  'ampcontact',
);
_us_features__install($feature_names);

// Add some example organizations.
$organizations = array(
  'Government',
  'Donor agency',
  'Civil society',
  'Researcher',
  'Student',
);
variable_set('amp_contact_organizations', $organizations);
