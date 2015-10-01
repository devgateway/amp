<?php

/**
 * Returns HTML for a form element.
 *
 * @see theme_form_element()
 */
function helpertheme_theme_form_element($variables) {
  $element = &$variables['element'];

  // This function is invoked as theme wrapper, but the rendered form element
  // may not necessarily have been processed by form_builder().
  $element += array(
    '#title_display' => 'before',
  );

  // Add element #id for #type 'item'.
  if (isset($element['#markup']) && !empty($element['#id'])) {
    $attributes['id'] = $element['#id'];
  }
  // Add element's #type and #name as class to aid with JS/CSS selectors.
  $attributes['class'] = array('form-item');
  if (!empty($element['#type'])) {
    $attributes['class'][] = 'form-type-' . strtr($element['#type'], '_', '-');
  }
  if (!empty($element['#name'])) {
    $attributes['class'][] = 'form-item-' . strtr($element['#name'], array(' ' => '-', '_' => '-', '[' => '-', ']' => ''));
  }
  // Add a class for disabled elements to facilitate cross-browser styling.
  if (!empty($element['#attributes']['disabled'])) {
    $attributes['class'][] = 'form-disabled';
  }
  if (!empty($element['#return_value'])) {
    $attributes['class'][] = 'form-item-value-' . strtr($element['#return_value'], array(' ' => '-', '_' => '-', '[' => '-', ']' => ''));
  }

  if (!empty($element['#wrapper_attributes'])) {
    $attributes = array_merge_recursive($attributes, $element['#wrapper_attributes']);
  }
  $output = '<div' . drupal_attributes($attributes) . '>' . "\n";

  // If #title is not set, we don't display any label or required marker.
  if (!isset($element['#title'])) {
    $element['#title_display'] = 'none';
  }
  $prefix = isset($element['#field_prefix']) ? '<span class="field-prefix">' . $element['#field_prefix'] . '</span> ' : '';
  $suffix = isset($element['#field_suffix']) ? ' <span class="field-suffix">' . $element['#field_suffix'] . '</span>' : '';

  switch ($element['#title_display']) {
    case 'before':
    case 'invisible':
      $output .= ' ' . theme('form_element_label', $variables);
      $output .= ' ' . $prefix . $element['#children'] . $suffix . "\n";
      break;

    case 'after':
      $output .= ' ' . $prefix . $element['#children'] . $suffix;
      $output .= ' ' . theme('form_element_label', $variables) . "\n";
      break;

    case 'none':
    case 'attribute':
      // Output no label and no required marker, only the children.
      $output .= ' ' . $prefix . $element['#children'] . $suffix . "\n";
      break;
  }

  if (!empty($element['#description'])) {
    $output .= '<div class="description">' . $element['#description'] . "</div>\n";
  }

  $output .= "</div>\n";

  return $output;
}

/**
 * Returns HTML for a form element label and required marker.
 * Added '#title_extra_class', '#title_extra_icon' and other title options.
 *
 * @see theme_form_element_label()
 */
function helpertheme_form_element_label($variables) {
  $custom_label = FALSE;
  if (isset($variables['element']['#title_extra_class'])) {
    $custom_label = TRUE;
  }
  elseif (isset($variables['element']['#title_extra_icon'])) {
    $custom_label = TRUE;
  }
  elseif (isset($variables['element']['#title_extra_suffix'])) {
    $custom_label = TRUE;
  }
  elseif (isset($variables['element']['#title_alternative_tag'])) {
    $custom_label = TRUE;
  }

  if ($custom_label) {
    module_load_include('inc', 'helpertheme', 'helpertheme.theme');
    return _helpertheme_form_element_label_custom($variables);
  }

  return theme_form_element_label($variables);
}

/**
 * Helper for: helpertheme_form_element_label()
 */
function _helpertheme_form_element_label_custom($variables) {
  $element = $variables['element'];

  // If title and required marker are both empty, output no label.
  if ((!isset($element['#title']) || $element['#title'] === '') && empty($element['#required'])) {
    return '';
  }

  // If the element is required, a required marker is appended to the label.
  $required = !empty($element['#required']) ? theme('form_required_marker', array('element' => $element)) : '';

  $tag = 'label';
  if (!empty($element['#title_alternative_tag'])) {
    $tag = $element['#title_alternative_tag'];
  }

  $attributes = array();
  // Style the label as class option to display inline with the element.
  if ($element['#title_display'] == 'after') {
    $attributes['class'][] = 'option';
  }
  // Show label only to screen readers to avoid disruption in visual flows.
  elseif ($element['#title_display'] == 'invisible') {
    $attributes['class'][] = 'element-invisible';
  }

  // Set the title class.
  if (!empty($element['#title_extra_class']) && is_array($element['#title_extra_class'])) {
    $attributes['class'] = array_merge($attributes['class'], $element['#title_extra_class']);
  }

  // Set the title icon.
  $icon = '';
  if (!empty($element['#title_extra_icon'])) {
    $icon = $element['#title_extra_icon'] . ' ';
  }

  // Set the title suffix.
  $suffix = '';
  if (!empty($element['#title_extra_suffix'])) {
    $suffix = $element['#title_extra_suffix'] . ' ';
  }

  if (!empty($element['#id']) && empty($element['#title_alternative_tag'])) {
    $attributes['for'] = $element['#id'];
  }

  if (empty($element['#title_cleaned'])) {
    $element['#title'] = filter_xss_admin($element['#title']);
  }

  // The leading whitespace helps visually separate fields from inline labels.
  return " <{$tag}" . drupal_attributes($attributes) . '>' . $icon . $element['#title'] . $required . "</{$tag}>\n" . $suffix;
}

/**
 * Returns HTML for a single local action link.
 *
 * @see helpertheme_theme_registry_alter()
 */
function helpertheme_menu_local_action($variables) {
  $link = $variables['element']['#link'];

  $link_options = array();
  if (isset($link['localized_options'])) {
    $link_options = $link['localized_options'];
  }

  $theme_extra = array();
  if (isset($link_options['theme extra'])) {
    $theme_extra = $link_options['theme extra'];
    unset($link_options['theme extra']);
  }

  if (!empty($theme_extra['entity action'])) {
    $title = $link['title'];
    if (isset($theme_extra['title'])) {
      $title = $theme_extra['title'];
      $link['title'] = $theme_extra['title'];
    }

    $link_options['attributes']['class'][] = 'entity-action';

    if (!empty($theme_extra['icon'])) {
      // Add icon.
      $link['title'] = _helpertheme_menu_local_action_title($title, $theme_extra['icon'], '2em');
      $link_options['html'] = TRUE;

      // Add link title.
      $link_options['attributes']['title'] = check_plain($title);
      $link_options['attributes']['class'][] = 'has-icon';
    }
    else {
      $link_options['attributes']['class'][] = 'no-icon';
    }
  }

  $output = '<li>';
  if (isset($theme_extra['prefix'])) {
    $output .= $theme_extra['prefix'];
  }
  if (isset($link['href'])) {
    $output .= l($link['title'], $link['href'], $link_options);
  }
  elseif (!empty($link['localized_options']['html'])) {
    $output .= $link['title'];
  }
  else {
    $output .= check_plain($link['title']);
  }
  if (isset($theme_extra['suffix'])) {
    $output .= $theme_extra['suffix'];
  }
  $output .= "</li>\n";

  return $output;
}

/**
 * Helper function for helpertheme_menu_local_action().
 */
function _helpertheme_menu_local_action_title($title, $icon, $size) {
  $svgicon = helpertheme_get_svg_icons($icon, array('width' => $size, 'height' => $size));

  return $svgicon . '<span class="title">' . check_plain($title) . '</span>';
}

/**
 * Implements hook_media_wysiwyg_token_to_markup_alter().
 */
function helpertheme_media_wysiwyg_token_to_markup_alter(&$element, $tag_info, $settings) {
  if (!empty($element['content']['#type']) && $element['content']['#type'] == 'container') {
    unset($element['content']['#type']);
  }
}

/**
 * Default theme function activities search forms.
 *
 * @see template_preprocess_views_exposed_form()
 */
function helpertheme_preprocess_views_exposed_form(&$variables) {
  // @TODO: Find a proper way to only alter the activities search form.
  if ($variables['form']['#id'] != 'views-exposed-form-activities-search-page') {
    return;
  }

  if (drupal_is_front_page()) {
    $variables['theme_hook_suggestions'][] = 'views_exposed_form__homepage_search';

    if (isset($variables['widgets']['filter-title'])) {
      $variables['basewidget'] = clone $variables['widgets']['filter-title'];
      unset($variables['widgets']['filter-title']);
    }

    $extra_widgets_toggle = '<input type="checkbox" name="vew" id="extra-widgets-toggle" class="extra-widgets-toggle" />';

    $extra_widgets_toggle .= '<label for="extra-widgets-toggle" class="extra-widgets-toggle-label">';
    $svgicon = helpertheme_get_svg_icons('arrow', array('width' => '1.2em', 'height' => '1.2em'));
    $extra_widgets_toggle .= '<span class="more">' . $svgicon . t('more') . '</span>';

    $svgicon = helpertheme_get_svg_icons('arrow-up', array('width' => '1.2em', 'height' => '1.2em'));
    $extra_widgets_toggle .= '<span class="less">' . $svgicon . t('less') . '</span>';
    $extra_widgets_toggle .= '</label>';

    $variables['extra_widgets_toggle'] = $extra_widgets_toggle;
  }
}
