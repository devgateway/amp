<?php

// Provide a list of CORE modules to be disabled and uninstalled.
$modules = array(
  'block',         // Replaced by panels and custom pane content.

  // The following lines are required only if the "Standard" installation profile was used.
  // 'contextual', // Contextual links 'always' get in the way of your theme.
  // 'dashboard',  // It isn't really helping anyone.
  // 'overlay',    // Is really not helpful for developers.
  // 'toolbar',    // Replaced by the more accessible admin_menu_toolbar.
  'update',        // It's simply annoying to see this on all environments.
);
_us_module__uninstall($modules);

// Provide a list of modules to be installed.
// The following lines are required only if the "Minimal" installation profile was used.
$modules = array(
  // Enable requried core modules.
  // 'comment',
  // 'field_ui',
  'file',
  'image',
  // 'help', // Required?
  'list',
  'menu',
  'number',
  'options',
  // 'path',
  // 'syslog',
  // 'taxonomy',
);
_us_module__install($modules);
