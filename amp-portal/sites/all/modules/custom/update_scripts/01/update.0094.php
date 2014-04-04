<?php

// Provide a list of modules to be disabled and uninstalled.
$modules = array(
  'devel',
    'devel_generate',
    'devel_node_access',
);
// Uninstall modules
_us_uninstall_modules($modules);
