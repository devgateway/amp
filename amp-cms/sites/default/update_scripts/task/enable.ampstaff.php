<?php

/**
 * @file: Enable AMP Staff Profiles.
 * @desc: This script can be used to enable the Staff Profiles listing, it also adds a new menu item.
 */


// Prepare a list of features to be installed.
$feature_names = array(
  'ampstaff_profiles',
);
_us_features__install($feature_names);

// Clear system caches.
drupal_flush_all_caches();

// HACK: Load the view, otherwise the menu link will not be created.
views_invalidate_cache();
$view = views_get_view('staff');

// Create a menu link.
$link = array(
  'link_path' => 'staff',
  'link_title' => 'Staff',
  'weight' => 0,
);
_us_menu__create_link($link, 'main-menu');
