<?php
// $Id: update.0016.php,v 1.1 2012/01/19 16:43:47 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'link',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_features(array('ampp_content_types.custom', 'ampp_homepage', 'ampp_general'));