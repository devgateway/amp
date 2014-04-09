<?php
// $Id:

_us_install_features(array('ampp_general'));

_us_uninstall_modules(array('ampp_feedback'));

$module_list = array('hierarchical_select', 'hs_taxonomy', 'hs_flatlist', 'hs_taxonomy_views', 'ampp_feedback');

// Enable modules and dependecies
_us_install_modules($module_list);