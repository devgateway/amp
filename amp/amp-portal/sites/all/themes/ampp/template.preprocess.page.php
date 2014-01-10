<?php
// $Id: template.preprocess.page.php,v 1.37 2013/11/06 19:10:55 vamirbekyan Exp $

static $site_domain;


function ampp_preprocess_html(&$vars) { 
  $vars['is_front'] = 0;
  if (!isset($_GET['q']) || empty($_GET['q']) || $_GET['q'] == variable_get('site_frontpage', 'node')) {
    $vars['is_front'] = 1; 
  }
}


/* 
 * Returns a breadcrumb array for a given groupnode.
 */
function ampp_breadcrumb($node = NULL) { 
  // on the home page
  if (!isset($_GET['q']) || empty($_GET['q']) || $_GET['q'] == variable_get('site_frontpage', 'node')) return array();

  $bc[] = l(t('Home'), '<front>');
  
  // News page
  if (isset($args[0]) && $args[0] == 'news') {
    $bc[] = t('News');
  }
  if (isset($args[0]) && $args[0] == 'node') {
    switch ($node->type) {
      case 'page' :
        $bc[] = t($node->title);
        break;
      case 'news' :
      case 'event' :
      case 'imported_item' :
        $bc[] = l(t('News'), 'news');
        $bc[] = t($node->title);
    }
  } 
  return $bc;
}


/**
 * Preprocessor for menu_item.
 * Checks whether the current item has children that
 * were not rendered, and loads and renders them.
 */
function ampp_theme_menu_item_($link, $has_children, $menu = '', $in_active_trail = FALSE, $extra_class = NULL) {
  global $theme;
  static $cookie, $settings, $function;

  if (!isset($cookie)) {
    $settings =  variable_get('dhtml_menu_effects', unserialize(DHTML_MENU_DEFAULT));

    // Do not use this feature when keeping only one menu open at a time - the active path will always be open.
    $cookie = !empty($_COOKIE['dhtml_menu']) && empty($settings['siblings']) ? explode(',', $_COOKIE['dhtml_menu']) : array();
    // Cascade up to the original theming function.
    $registry = variable_get('dhtml_menu_theme', array());
    $function = isset($registry[$theme]) ? $registry[$theme]['menu_item'] : 'theme_menu_item';
  }

  /* When theme('menu_item') is called, the menu tree below it has been
   * rendered already. Since we are done on this recursion level,
   * one element must be popped off the stack.
   */
  $item = _dhtml_menu_stack();

  // If this item should not have DHTML, then return to the "parent" function.
  if (!empty($item['dhtml_disabled'])) {
    $extra_class .= ' no-dhtml ';
    return $function($link, $has_children, $menu, $in_active_trail, $extra_class);
  }
  // Otherwise, add a class to identify this element for JS.
  $extra_class .= ' dhtml-menu ';

  // If there are children, but they were not loaded...
  if ($has_children && !$menu) {
    // Load the tree below the current position.
    $tree = _dhtml_menu_subtree($item);

    // Render it...
    $menu = menu_tree_output($tree);
    if (!$menu) $has_children = FALSE; // Sanitize tree. If we found no children, the item has none.
  }

  // If the current item can expand, and is neither saved as open nor in the active trail, close it.
  if ($menu && !($in_active_trail || in_array(substr($item['options']['attributes']['id'], 5), $cookie))) {
    $extra_class .= ' collapsed start-collapsed ';
  }

  $class = ($menu ? 'expanded' : ($has_children ? 'collapsed' : 'leaf'));
  if (!empty($extra_class)) {
    $class .= ' '. $extra_class;
  }
  if ($in_active_trail) {
    $class .= ' active-trail';
  }
  if ($has_children) return '<li class="'. $class .'">' . '<span class="menu_switch"><a href="" class="dhtml-menu">&nbsp;</a></span>' . '<span class="menu_span">' . $link . '</span>' . $menu . "</li>\n";
  else return '<li class="'. $class .'">' . $link . $menu . "</li>\n";
}


/**
 * Implemenation of phptemplate_menu_item_link
 */
function ampp_menu_item_link($link) {
  if (empty($link['localized_options'])) {
    $link['localized_options'] = array();
  }

  // CHECK IF THE LINK HAS 'HTTP' AND ADD _BLANK IF TRUE
  if( substr($link['href'], 0, 4) == 'http' ) {
    $link['localized_options']['attributes']['target'] = '_blank';
  }
  return l($link['title'], $link['href'], $link['localized_options']);
}


/*
 * Custom menu_tree implementatoin that returns only first N levels
 */
function _ampp_menu_tree($menu_name = 'navigation', $depth = 0) {
  static $menu_output = array();

  if (!isset($menu_output[$menu_name])) {
    //drupal 7 lets us to pass max_depth
    $tree = menu_tree_page_data($menu_name, $depth);
    $menu_output[$menu_name] = menu_tree_output($tree);
  }
  return $menu_output[$menu_name];
}

/**
 * Process variables for page.tpl.php
 */
function ampp_preprocess_page(&$vars) {
  global $base_path, $user;
  $theme_path = path_to_theme();
  $vars['theme_path'] = $theme_path;

  // Printer friendly versoin
  if (isset($_GET['printer']) && $_GET['printer'] == 1) {
    $vars['template_files'] = array('page_pf');
  } elseif(isset($_GET['format']) && $_GET['format'] == 'simple') {
    $vars['simple_page'] = 'simple';
  }

  if ($user) {
    $user_can_view_tabs = (
        $user->uid == 1 ||
        arg(0) == 'admin' ||
        array_intersect(array('editor', 'webmaster'), $user->roles)
        );
  }
  else {
    $user_can_view_tabs = FALSE;
  }

  // Add IE CSS fix.
  if(!isset($vars['head'])) $vars['head'] = '';
  $vars['head'] .= '<!--[if lte IE 8]><link rel="stylesheet" type="text/css" href="' . $theme_path . '/css/iex.css"><![endif]-->' . "\n";
  $vars['head'] .= '<!--[if lte IE 6]><link rel="stylesheet" type="text/css" href="' . $theme_path . '/css/ie6.css"><![endif]-->' . "\n";

  // SEO optimization, avoid duplicate titles in search indexes for pager pages
  if (isset($_GET['page']) || isset($_GET['sort'])) {
    $vars['meta'] .= '<meta name="robots" content="noindex,follow" />'. "\n";
  }

  // Populate header and footer vars.
  // logo
  $vars['toggle_logo'] = theme_get_setting('toggle_logo', 'ampp');
  $default_used = false;
  if (theme_get_setting('default_logo', 'ampp')) {
    // default logo is used
    $default_used = true;
    $logo_path = drupal_get_path('theme', 'ampp') . '/logo.png';
  } else {
    // custom logo is used
    $logo_path = theme_get_setting('logo_path', 'ampp');
    if (file_uri_scheme($logo_path) == 'public') {
      $logo_path = file_uri_target($logo_path);
    }
  }
  
  $vars['site_name'] = variable_get('site_name', '');
  
  if ($default_used) {
    $vars['header_logo'] = l(theme('image', array('path' => $logo_path, 'alt' => $vars['site_name'], 'title' => $vars['site_name'])), '<front>', array('html' => true, 'attributes' => array('id' => 'ampp_logo_anchor', 'title' => $vars['site_name'])));
  } else {
    $vars['header_logo'] = l(theme('image_style', array('style_name' => 'logo', 'path' => $logo_path, 'alt' => $vars['site_name'], 'title' => $vars['site_name'])), '<front>', array('html' => true, 'attributes' => array('id' => 'ampp_logo_anchor', 'title' => $vars['site_name'])));
  }
  $vars['header_links'] = theme('header_links_menu');
  $vars['amp_links'] = theme_get_setting('toggle_amp_menu', 'ampp') ? theme('amp_links_menu') : '';
  $vars['footer_links'] = theme('footer_links_menu');
  $vars['footer_text'] = variable_get('site_footer_text', '');
  $vars['dg_footer'] = theme_get_setting('toggle_dg_footer', 'ampp') ? variable_get('dg_footer_html', '') : '';
  
  $addthis_lable = trim(variable_get('site_share_label', ''));
  if (!empty($addthis_lable)) $addthis_lable = '<span id="addthis_lable">' . $addthis_lable . ': </span>';
  
  // we need to check if we are running over SSL first
  if (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS']) {
    $protocol = 'https';
  } else {
    $protocol = 'http';
  }
  
  $addthis = $addthis_lable . '<div class="addthis_toolbox addthis_default_style "><a class="addthis_button_preferred_1"></a><a class="addthis_button_preferred_2"></a>
    <a class="addthis_button_preferred_3"></a></div><script type="text/javascript" src="' . $protocol . '://s7.addthis.com/js/250/addthis_widget.js#pubid=vamirbekyan"></script>';
  $vars['share_links'] = theme_get_setting('toggle_share_links', 'ampp') ? $addthis : '';
  
  // Create navigation bar.
  $menu_name = variable_get('menu_main_links_source', 'main-links');
  $vars['main_links_menu'] = _ampp_menu_tree($menu_name, 2);

  // Hide node title because all nodes go through panels. Exception is page type
  if (isset($vars['node']->type) && ($vars['node']->type != 'page') && arg(2) == '') {
    $vars['title'] = '';
  }
  
  // footer logo
  $default_used = false;
  if (theme_get_setting('default_footer_logo', 'ampp')) {
    // default footer logo is used
    $default_used = true;
    $footer_logo_path = drupal_get_path('theme', 'ampp') . '/footer-logo.png';
  } else {
    // custom footer logo is used
    $footer_logo_path = theme_get_setting('footer_logo_path', 'ampp');
    if (file_uri_scheme($footer_logo_path) == 'public') {
      $footer_logo_path = file_uri_target($footer_logo_path);
    } 
    // $footer_logo_path = variable_get('file_public_path', 'sites/default/files') . '/' . $footer_logo_path;
  }
  if ($default_used) {
    $vars['footer_logo'] = theme('image', array('path' => $footer_logo_path, 'alt' => $vars['site_name'], 'title' => $vars['site_name']));
  } else {
    $vars['footer_logo'] = theme('image_style', array('style_name' => 'footer_logo', 'path' => $footer_logo_path, 'alt' => $vars['site_name'], 'title' => $vars['site_name']));
  } 
  
  // construct breadcrumb
  $bc = isset($vars['node']) ? ampp_breadcrumb($vars['node']) : null;
  $vars['breadcrumb'] = !is_null($bc) ? theme_breadcrumb(array('breadcrumb' => $bc)) : '';
  
  // Hide page title and breadcrumb for the homepage
  if ($vars['is_front']) {
    $vars['title'] = '';
    $vars['breadcrumb'] = '';
  } else {
    if(array_key_exists('node', $vars)) {
      $node = $vars['node'];
      $hp = variable_get('site_frontpage', 'homepage');
      if (drupal_lookup_path('alias', 'node/' . $node->nid) == $hp || 'node/' . $node->nid == $hp) {
        $vars['title'] = '';
        $vars['breadcrumb'] = '';
      }
    }
  }
}


/**
 * Implements hook_process_page().
 */
function ampp_process_page(&$vars) {
  // Hook into color.module.
  if (module_exists('color')) {
    _color_page_alter($vars);
  }
  if (!empty($vars['title'])) {
    $translation = t($vars['title']);
    if (!empty($translation)) {
      $vars['title'] = $translation;     
    }
  }
}