<?php
// $Id: update.0057.php,v 1.1 2012/08/06 21:56:02 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  // Drupal core modules
  'color',
);

// Enable modules and dependecies
_us_install_modules($module_list);