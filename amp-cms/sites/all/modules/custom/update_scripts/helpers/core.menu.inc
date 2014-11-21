<?php

/**
 * @file
 * Update Scripts helper functions for the core Menu module.
 */

/**
 * Create a new menu.
 */
function _us_menu__create_menu($menu_name, $title, $description = '') {
  module_load_include('module', 'menu');
  module_load_include('inc', 'menu', 'menu.admin');

  // Check if the menu exists.
  if (menu_load($menu_name)) {
    return;
  }

  $form_state = array();
  $form_state['values'] = array();
  $form_state['values']['menu_name'] = $menu_name;
  $form_state['values']['title'] = $title;
  $form_state['values']['description'] = $description;

  menu_edit_menu_submit(array('#insert' => TRUE), $form_state);
  return $menu_name;
}

/**
 * Delete a custom menu and all its menu links.
 */
function _us_menu__delete_menu($menu_name) {
  module_load_include('inc', 'menu');

  if ($menu = menu_load($menu_name)) {
    menu_delete($menu);
  }
}

/**
 * Create a new menu link.
 */
function _us_menu__create_link($item, $menu_name, $force = FALSE) {
  module_load_include('inc', 'menu');

  // Check if the menu exists.
  if (!menu_load($menu_name)) {
    return;
  }

  $link_exists = db_select('menu_links')
    ->fields('menu_links', array('mlid'))
    ->condition('link_path', $item['link_path'])
    ->condition('menu_name', $menu_name)
    ->condition('module', 'system', '<>')
    ->execute()->fetchField();

  if ($link_exists && !$force) {
    return FALSE;
  }

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
function _us_menu__delete_links($menu_name) {
  module_load_include('inc', 'menu');

  menu_delete_links($menu_name);
}

/**
 * Delete a specific link.
 *
 * @param string $link
 *   The $link item to be deleted as returned by menu_load_links().
 */
function _us_menu__delete_link($link) {
  module_load_include('inc', 'menu');

  // To speed up the deletion process, we reset some link properties that
  // would trigger re-parenting logic in _menu_delete_item() and
  // _menu_update_parental_status().
  $link['has_children'] = FALSE;
  $link['plid'] = 0;
  _menu_delete_item($link);
}
