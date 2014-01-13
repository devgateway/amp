<?php
// $Id: update.0078.php,v 1.2 2013/08/24 15:19:37 vamirbekyan Exp $

// Prepare an array of modules to be enabled.
$module_list = array(  
  'extlink',
);

// Enable modules and dependecies
_us_install_modules($module_list);

variable_set('extlink_class', 0);
variable_set('extlink_target', '_blank');