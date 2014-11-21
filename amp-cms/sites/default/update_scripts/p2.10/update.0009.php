<?php

// Provide a list of modules to be installed.
$modules = array(
  'less',
);
_us_module__install($modules);

// Set the default "LESS engine".
variable_set('less_engine', 'less.php');

// Enable theme and set it as default.
_us_theme__enable('ampcms', TRUE);

// Disable defaul Drupal theme.
_us_theme__disable('bartik');
