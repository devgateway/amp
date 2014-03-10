<?php

/**
 * @file
 * Update Scripts helper functions for the core Menu module.
 */

/**
 * Create a new menu.
 */
function _us_menu_menu_save($menu_name, $title, $description = '') {
  module_load_include('module', 'menu');
  module_load_include('inc', 'menu', 'menu.admin');

  // Strip invalid characters.
  __us_menu_clean_menu_name($menu_name);

  $form_state = array();
  $form_state['values'] = array();
  $form_state['values']['menu_name'] = $menu_name;
  $form_state['values']['title'] = $title;
  $form_state['values']['description'] = $description;

  menu_edit_menu_submit(array('#insert' => TRUE), $form_state);
  return 'menu-' . $menu_name;
}

/**
 * Create a new menu link.
 */
function _us_menu_link_save($item, $menu_name) {
  module_load_include('inc', 'menu');

  // Strip invalid characters.
  __us_menu_clean_menu_name($menu_name);
  $item += array(
    'menu_name' => $menu_name,
  );

  $mlid = menu_link_save($item);
  menu_cache_clear_all();
  return $mlid;
}

/**
 * Delete all links from menu.
 */
function _us_menu_delete_links($menu_name) {
  module_load_include('inc', 'menu');

  // Strip invalid characters.
  __us_menu_clean_menu_name($menu_name);

  menu_delete_links($menu_name);
}

/**
 * Clean menu machine-name.
 */
function __us_menu_clean_menu_name(&$menu_name) {
  $menu_name = strtolower($menu_name);
  $pattern = '/[^0-9a-z-]+/';
  $menu_name = preg_replace($pattern, '', $menu_name);
}

