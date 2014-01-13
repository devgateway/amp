<?php

// Prepare an array of modules to be enabled.
$module_list = array(  
  'piwik',
);

// Enable modules and dependecies
_us_install_modules($module_list);

// Enforce Piwik shared settings
_us_revert_feature('ampp_general');