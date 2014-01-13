<?php
// $Id: update.0065.php,v 1.1 2012/12/10 15:58:47 apetrosyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'views_export_xls'
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_feature('ampp_projects');