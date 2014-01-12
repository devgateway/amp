<?php
// $Id: update.0064.php,v 1.1 2012/12/06 22:25:40 apetrosyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(
  'hs_select_primarysectors',
  'hs_select_secondarysectors',
  'hs_select_programs',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_feature('ampp_projects');