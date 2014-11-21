<?php
/**
 * @file
 * This file contains theme functions overrides.
 */

/**
 * Implements theme_menu_tree() for the main_menu.
 */
function ampcms_menu_tree__main_menu__toplevel($variables) {
  return '<ul class="navigation-list-toplevel clearfix">' . $variables['tree'] . '</ul>';
}

/**
 * Implements theme_menu_tree() for main_menu sublevel.
 */
function ampcms_menu_tree__main_menu__sublevel($variables) {
  return '<ul class="navigation-list-sublevel clearfix">' . $variables['tree'] . '</ul>';
}

/**
 * Implements theme_menu_link() for main_menu.
 */
function ampcms_menu_link__main_menu(array $variables) {
  // Use drupal static cache so that it can be cleared from external functions.
  $top_level_nav = &drupal_static('ampcms_menu_link__main_menu', array());

  // Remove the 'leaf' class.
  if (in_array('leaf', $variables['element']['#attributes']['class'])) {
    $variables['element']['#attributes']['class'] = array_diff($variables['element']['#attributes']['class'], array('leaf'));
  }

  // Remove the 'expanded' class.
  if (in_array('expanded', $variables['element']['#attributes']['class'])) {
    $variables['element']['#attributes']['class'] = array_diff($variables['element']['#attributes']['class'], array('expanded'));
  }

  // Remove the 'collapsed' class.
  if (in_array('collapsed', $variables['element']['#attributes']['class'])) {
    $variables['element']['#attributes']['class'] = array_diff($variables['element']['#attributes']['class'], array('collapsed'));
  }

  // Get the Parent Link ID.
  $plid = $variables['element']['#original_link']['plid'];

  if (empty($plid)) {
    // Style the menu toplevel.
    $top_level_nav[] = $variables['element']['#original_link']['mlid'];
    return __ampcms_menu_link__level($variables, 'toplevel');
  }

  if (!empty($plid) && in_array($plid, $top_level_nav)) {
    // Style the menu sublevel.
    return __ampcms_menu_link__level($variables, 'sublevel');
  }

  // Directly call the original theme function.
  return theme_menu_link($variables);
}

/**
 * Helper for theme_menu_tree().
 */
function __ampcms_menu_link__level(&$variables, $level = NULL) {
  $element = $variables['element'];

  $mlid = $element['#original_link']['mlid'];
  $plid = $element['#original_link']['plid'];

  switch ($level) {
    case 'toplevel':
      // Add list item class.
      $element['#attributes']['class'][] = 'top-listitem';

      // Add title as list item class.
      $extra_class = $element['#title'];
      if (function_exists('transliteration_get')) {
        $extra_class = transliteration_get($extra_class);
      }
      $extra_class = drupal_html_class('top-listitem-' . $extra_class);
      $element['#attributes']['class'][] = $extra_class;

      // Add link class.
      $element['#localized_options']['attributes']['class'][] = 'top-link';

      // Set the link title if not provided.
      if (empty($element['#localized_options']['attributes']['title'])) {
        $element['#localized_options']['attributes']['title'] = $element['#title'];
      }

      if ($element['#below']) {
        // Change the override the rendering of the subitems wrapper.
        $element['#below']['#theme_wrappers'] = array('menu_tree__main_menu__sublevel');

        // Add list item class.
        $element['#attributes']['class'][] = 'has-sublevel';

        // Add link class.
        $element['#localized_options']['attributes']['class'][] = 'with-sublevel';
      }

      break;

    case 'sublevel':
      // Add list item class.
      $element['#attributes']['class'][] = 'sublevel-wrapper';

      // Add link class.
      $element['#localized_options']['attributes']['class'][] = 'sublevel-title';

      if ($element['#below']) {
        // Add link class.
        $element['#localized_options']['attributes']['class'][] = 'with-sublevel';
      }
      break;
  }

  // Render the submenu items.
  $output = '';
  if ($element['#below']) {
    $sub_menu = drupal_render($element['#below']);
    $output .= '<div class="navigation-sublevel clearfix">' . $sub_menu . '</div>';
  }

  // Get the svg icon for this menu link.
  $svgicon = '';
  $selected_icon = variable_get(HELPERTHEME_SVG_ICON_NAME_PREFIX . $mlid, '');
  if (!empty($selected_icon)) {
    $svgicon = helpertheme_get_svg_icons($selected_icon, array('width' => '1.5em', 'height' => '1.5em')) . ' ';
  }

  // Render the menu link.
  $link_title = $svgicon . '<span class="text">' . check_plain($element['#title']) . '</span>';
  $link = l($link_title, $element['#href'], array_merge_recursive($element['#localized_options'], array('html' => TRUE)));

  $tag = 'li';
  return "<$tag" . drupal_attributes($element['#attributes']) . '>' . $link . $output . "</$tag>\n";
}
