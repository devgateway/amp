<?php
/**
 * @file
 * ampp_general.features.menu_custom.inc
 */

/**
 * Implements hook_menu_default_menu_custom().
 */
function ampp_general_menu_default_menu_custom() {
  $menus = array();

  // Exported menu: menu-amp-links
  $menus['menu-amp-links'] = array(
    'menu_name' => 'menu-amp-links',
    'title' => 'AMP links',
    'description' => '',
    'language' => 'und',
    'i18n_mode' => '5',
  );
  // Exported menu: menu-footer-links
  $menus['menu-footer-links'] = array(
    'menu_name' => 'menu-footer-links',
    'title' => 'Footer links',
    'description' => '',
    'language' => 'und',
    'i18n_mode' => '5',
  );
  // Exported menu: menu-header-links
  $menus['menu-header-links'] = array(
    'menu_name' => 'menu-header-links',
    'title' => 'Header links',
    'description' => '',
    'language' => 'und',
    'i18n_mode' => '5',
  );
  // Translatables
  // Included for use with string extractors like potx.
  t('AMP links');
  t('Footer links');
  t('Header links');


  return $menus;
}
