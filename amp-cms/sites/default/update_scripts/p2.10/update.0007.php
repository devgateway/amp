<?php

// Prepare a list of features to be installed.
$feature_names = array(
  'ampcontentblock',
  'ampet_blog',
  'ampet_page',
  'ampfileentity',

  'amphomepage',
  'amppermissions',
  'ampsettings',
  'ampviewsettings',
);
_us_features__install($feature_names);

// Clear system caches.
drupal_flush_all_caches();

// Prepare a list of features to be reverted.
$feature_names = array(
  'ampcontentblock',
  'ampet_blog',
  'ampet_page',
  'ampfileentity',

  'amphomepage',
  'amppermissions',
  'ampsettings',
  'ampviewsettings',
);
_us_features__revert($feature_names);

// Clear system caches.
drupal_flush_all_caches();


// Change site homepage.
variable_set('site_frontpage', 'homepage');


// Delete all links from menu.
_us_menu__delete_links('user-menu');

$link = array(
  'link_path' => 'admin/content',
  'link_title' => 'Content',
  'weight' => -45,
);
_us_menu__create_link($link, 'user-menu');

$link = array(
  'link_path' => 'admin/people',
  'link_title' => 'People',
  'weight' => -44,
);
_us_menu__create_link($link, 'user-menu');

$link = array(
  'link_path' => 'admin/settings',
  'link_title' => 'Settings',
  'weight' => -43,
);
_us_menu__create_link($link, 'user-menu');

$link = array(
  'link_path' => 'admin/info',
  'link_title' => 'Info',
  'weight' => -42,
);
_us_menu__create_link($link, 'user-menu');


// Delete all links from menu.
_us_menu__delete_links('main-menu');

// Add "Homepage" link to the main menu.
$link = array(
  'link_path' => '<front>',
  'link_title' => 'Homepage',
  'weight' => -20,
);
_us_menu__create_link($link, 'main-menu');

// Add "Blog" link to the main menu.
$link = array(
  'link_path' => 'blog',
  'link_title' => 'Blog',
  'weight' => 10,
);
_us_menu__create_link($link, 'main-menu');
