<?php

/**
 * @file: Enable AMP Organization Profiles.
 * @desc: This script can be used to enable the Organization Profiles listing, it also adds a new menu item.
 */

// Provide a list of modules to be installed.
$modules = array(
  'taxonomy',
);
_us_module__install($modules);


// Prepare a list of features to be reverted.
$feature_names = array(
  'ampfileentity',
  'ampviewsettings',
);
_us_features__revert($feature_names);

// Clear system caches.
drupal_flush_all_caches();


// Prepare a list of features to be installed.
$feature_names = array(
  'amporganization_profiles',
);
_us_features__install($feature_names);

// Clear system caches.
drupal_flush_all_caches();


// HACK: Load the view, otherwise the menu link will not be created.
views_invalidate_cache();
$view = views_get_view('organizations');

// Create a menu link.
$link = array(
  'link_path' => 'organizations',
  'link_title' => 'Organizations',
  'weight' => 0,
);
_us_menu__create_link($link, 'main-menu');
