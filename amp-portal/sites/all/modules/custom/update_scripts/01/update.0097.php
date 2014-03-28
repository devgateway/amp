<?php

// Provide a list of modules to be installed.
$modules = array(
  'amp_activity',
  'amp_i18n',
);
// Install modules.
_us_install_modules($modules);

_us_revert_feature('ampp_projects');
