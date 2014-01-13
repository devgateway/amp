<?php
// $Id: 

$module_list = array('hs_select_locations', 'hs_select_sectors');

// Enable modules and dependecies
_us_install_modules($module_list);
_us_revert_feature('ampp_projects');