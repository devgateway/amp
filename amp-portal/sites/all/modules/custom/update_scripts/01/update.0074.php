<?php
// $Id: update.0074.php,v 1.2 2013/04/25 02:09:04 vamirbekyan Exp $

$module_list = array(
  // Drupal core modules
  'date_api',
  'date_popup',
  'date_views',
  'date',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_feature('ampp_projects');