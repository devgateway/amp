<?php

/**
 * @file: Enable AMP Agreements.
 * @desc: This script can be used to enable the Agreements listing, it also adds a new menu item.
 */


// Prepare a list of features to be installed.
$feature_names = array(
  'ampagreements',
);
_us_features__install($feature_names);

// Clear system caches.
drupal_flush_all_caches();

// HACK: Load the view, otherwise the menu link will not be created.
views_invalidate_cache();
$view = views_get_view('agreements');

// Create a menu link.
$link = array(
  'link_path' => 'agreements',
  'link_title' => 'Agreements',
  'weight' => 0,
);
_us_menu__create_link($link, 'main-menu');
