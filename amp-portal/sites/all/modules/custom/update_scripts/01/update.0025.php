<?php
// $Id: update.0025.php,v 1.1 2012/01/25 18:36:51 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'customerror',
);

// Enable modules and dependecies
_us_install_modules($module_list);
_us_revert_features(array('ampp_general'));
