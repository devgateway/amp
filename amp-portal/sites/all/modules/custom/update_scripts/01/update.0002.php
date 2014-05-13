<?php
// $Id: update.0002.php,v 1.4 2012/01/20 20:26:44 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'strongarm',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_install_features(array('ampp_general'));
_us_revert_features(array('ampp_general'));