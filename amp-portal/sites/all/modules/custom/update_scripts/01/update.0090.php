<?php
// $Id: update.0002.php,v 1.4 2012/01/20 20:26:44 vamirbekyan Exp $

_us_uninstall_modules(array('tollbar'));

// Prepare an array of modules to be enabled.
$module_list = array(
  'admin_menu',
  'admin_menu_toolbar',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_feature('ampp_general');