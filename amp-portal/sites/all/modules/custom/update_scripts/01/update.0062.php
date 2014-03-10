<?php
// $Id: update.0062.php,v 1.1 2012/08/16 17:42:19 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'stringoverrides',
);

// Enable modules and dependecies
_us_install_modules($module_list);