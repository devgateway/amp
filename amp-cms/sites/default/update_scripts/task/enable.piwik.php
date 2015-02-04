<?php

/**
 * @file: Enable Piwik Web Analytics.
 * @desc: This script can be used to enable analytics.
 */

// Provide a list of modules to be installed.
$modules = array(
    'piwik',
);
_us_module__install($modules);
