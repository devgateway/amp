<?php
// $Id: update.0019.php,v 1.2 2012/01/20 20:25:54 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'feeds',
  'feeds_ui',
  'feeds_imagegrabber',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_install_features(array('feeds_news'));
_us_revert_features(array('ampp_content_types.custom', 'feeds_news'));