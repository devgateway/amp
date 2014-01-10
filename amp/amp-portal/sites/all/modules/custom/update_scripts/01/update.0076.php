<?php
// $Id: update.0076.php,v 1.2 2013/05/31 20:30:29 vharutyunyan Exp $

$module_list = array(
  'shorten',
);

// Enable modules and dependecies
_us_install_modules($module_list);

_us_revert_feature('ampp_general');