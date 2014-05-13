<?php
// $Id: update.0011.php,v 1.2 2012/01/19 16:44:20 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'feedback',
  'captcha',
  'image_captcha',
  'ampp_feedback',
);

// Enable modules and dependecies
_us_install_modules($module_list);
