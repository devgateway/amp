<?php
// $Id: color.inc,v 1.12 2012/08/13 15:39:14 vamirbekyan Exp $

// Put the logo path into JavaScript for the live preview.
// logo
$default_used = false;
if (theme_get_setting('default_logo', 'ampp')) {
    // default logo is used
    $default_used = true;
    $logo_path = '/' . drupal_get_path('theme', 'ampp') . '/logo.png';
} else {
    // custom logo is used
    $logo_path = theme_get_setting('logo_path', 'ampp');
    if (file_uri_scheme($logo_path) == 'public') {
      $logo_path = file_uri_target($logo_path);
    }
    $logo_path = image_style_url('logo', $logo_path);
}
drupal_add_js(array('color' => array('logo' => $logo_path)), 'setting');

$info = array(
  // Available colors and color labels used in theme.
  'fields' => array(
    'site_title' => t('Site name'),
    'site_slogan' => t('Site slogan'),
    'top_header_bg' => t('Top header background'),
    'top_header_login_button_top' => t('Top header Login button top color'),
    'top_header_login_button_bottom' => t('Top header Login button bottom color'),
    'top_header_menu_font' => t('Top header menu font'),
    'amp_menu_font' => t('AMP menu font'),
    'amp_menu_bg' => t('AMP menu background'),
    'block_header_bg' => t('Block header background'),
    'link' => t('Links'),
    'form_bg' => t('Search and Quick contact forms background'),
    'footer_bg' => t('Footer background'),
  ),
  // Pre-defined color schemes.
  'schemes' => array(
    'default' => array(
      'title' => t('AMPP'),
      'colors' => array(
        'site_title' => '#fefefe',
        'site_slogan' => '#fefefd',
        'top_header_bg' => '#4c6b7d',
        'top_header_menu_font' => '#fefefc',
        'top_header_login_button_top' => '#ffe400',
        'top_header_login_button_bottom' => '#ff8500',
        'amp_menu_font' => '#376093',
        'amp_menu_bg' => '#f2f2f2',
        'block_header_bg' => '#4c6b7c',
        'link' => '#376092',
        'form_bg' => '#f2f2f3',
        'footer_bg' => '#8b8b8b',
      ),
    ),
    'blue' => array(
      'title' => t('AMPP Blue'),
      'colors' => array(
        'site_title' => '#fefefe',
        'site_slogan' => '#fefefd',
        'top_header_bg' => '#00699d',
        'top_header_menu_font' => '#fefefc',
        'amp_menu_font' => '#376093',
        'amp_menu_bg' => '#f2f2f2',
        'block_header_bg' => '#263c65',
        'link' => '#376092',
        'form_bg' => '#f3f9fe',
        'footer_bg' => '#0070a8',
      ),
    ),
  ),

  // CSS files (excluding @import) to rewrite with new color scheme.
  'css' => array(
    'css/color.css',
  ),

  // Files to copy.
  'copy' => array(
    'logo.png',
  ),

  // Gradient definitions.
  'gradients' => array(
    array(
    ),
  ),

  // Color areas to fill (x, y, width, height).
  'fill' => array(),

  // Coordinates of all the theme slices (x, y, width, height)
  // with their filename as used in the stylesheet.
  'slices' => array(),

  // Reference color used for blending. Matches the base.png's colors.
  'blend_target' => '#ffffff',

  // Preview files.
  'preview_css' => 'color/preview.css',
  'preview_js' => 'color/preview.js',
  'preview_html' => 'color/preview.html',

  // Base file for image generation.
  'base_image' => 'color/base.png',
);
