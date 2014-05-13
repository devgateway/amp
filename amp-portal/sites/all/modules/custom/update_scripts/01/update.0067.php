<?php
// $Id: update.0067.php,v 1.1 2012/12/26 14:37:35 apetrosyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'feeds_excel'
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_feature('ampp_general');
_us_revert_feature('ampp_projects');