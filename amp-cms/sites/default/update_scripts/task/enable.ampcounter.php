<?php

/**
 * @file: Enable AMP Counter.
 * @desc: This script can be used to enable the site views counter and display it in the footer of the page.
 */

// Provide a list of modules to be installed.
$modules = array(
  'ampcounter',
);
_us_module__install($modules);
