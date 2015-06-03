<?php

/**
 * @file: Enable String Overrides.
 * @desc: This script can be used to allow the editing of English strings. See: AMP-19098
 */

// Provide a list of modules to be installed.
$modules = array(
  'stringoverrides',
);
_us_module__install($modules);
