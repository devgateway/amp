<?php
/**
 * @file
 * Define helper functions.
 */

/**
 * Returns an array of links for a navigation menu.
 *
 * @see: menu_navigation_links();
 */
function helpertheme_menu_navigation_links($menu_name, $levels = 0) {
  // For now only return one level.
  if ($levels > 1) {
    $levels = 1;
  }

  // Get the menu hierarchy for the current page.
  $tree = menu_tree_page_data($menu_name, $levels);

  // Create a single level of links.
  $router_item = menu_get_item();
  $links = array();
  foreach ($tree as $item) {
    if ($link_tree = _helpertheme_prepare_tree($item)) {
      $key = 'menu-' . $item['link']['mlid'];
      if (!empty($link_tree['below'])) {
        $key .= ' has-sublevel';
      }
      // Keyed with the unique mlid to generate classes in theme_links().
      $links[$key] = $link_tree;
    }
  }

  return $links;
}

/**
 * Helper for helpertheme_menu_navigation_links().
 */
function _helpertheme_prepare_tree($item, $is_sublevel = FALSE) {
  if ($item['link']['hidden']) {
    return;
  }

  $link = $item['link']['localized_options'];
  $link['href'] = $item['link']['href'];
  $link['title'] = $item['link']['title'];

  // Add classes in order to ease the styling of links.
  $link['attributes']['class'][] = 'link';
  $link['attributes']['class'][] = $is_sublevel ? 'link-sublevel' : 'link-toplevel';
  $link['attributes']['class'][] = 'link-' . drupal_html_class($link['title']);

  // Prepare child elements.
  if (!empty($item['below'])) {
    foreach ($item['below'] as $subitems) {
      if ($link_subitems = _helpertheme_prepare_tree($subitems, TRUE)) {
        $link['below']['menu-' . $subitems['link']['mlid']] = $link_subitems;
      }
    }

    // If any submenus are present add an extra class.
    if (!empty($link['below'])) {
      $link['attributes']['class'][] = 'with-sublevel';
    }
  }

  return $link;
}
