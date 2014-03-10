<?php
// $Id: update.0020.php,v 1.2 2012/01/21 15:43:53 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'number',
  'list',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_features(array('ampp_general', 'ampp_content_types', 'ampp_homepage', 'feeds_news'));
