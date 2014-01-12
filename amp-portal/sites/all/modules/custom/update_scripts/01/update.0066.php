<?php
// $Id: update.0066.php,v 1.1 2012/12/18 15:11:46 apetrosyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'imce','imce_wysiwyg'
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_feature('ampp_general');