<?php

/**
 * @file
 * Insert support for ImageField module.
 */

/**
 * Implementation of hook_insert_widgets().
 */
function image_insert_widgets() {
  return array(
    'image_image' => array(
      'element_type' => 'managed_file',
      'wrapper' => '.image-widget',
      'fields' => array(
        'alt' => 'input[name$="[alt]"], textarea[name$="[alt]"]',
        'title' => 'input[name$="[title]"], textarea[name$="[title]"]',
        'description' => 'input[name$="[description]"], textarea[name$="[description]"]',
      ),
    ),
  );
}

/**
 * Implementation of hook_insert_styles().
 */
function image_insert_styles() {
  $image_styles = image_styles();
  $insert_styles = array();
  foreach ($image_styles as $style) {
    $insert_styles['image_' . $style['name']] = array(
      'label' => t($style['name']),
    );
  }
  return $insert_styles;
}

/**
 * Implementation of hook_insert_content().
 */
function image_insert_content($item, $style, $widget) {
  $style_name = preg_replace('/^image_/', '', $style['name']);
  return theme(array('image_insert_image__' . str_replace('-', '_', $style_name), 'image_insert_image'), array('item' => $item, 'widget' => $widget, 'style_name' => $style_name));
}

/**
 * Theme the content that will be inserted for Image styles.
 */
function template_preprocess_image_insert_image(&$vars) {
  $vars['file'] = file_load($vars['item']['fid']);

  // We call this function merely to trigger access to the
  // image_style_generate() callback. We actually use image_style_path() to
  // build the final location of the file.
  $url = image_style_url($vars['style_name'], $vars['file']->uri);

  $vars['uri'] = image_style_path($vars['style_name'], $vars['file']->uri);
  $vars['url'] = insert_create_url($vars['uri']);
  $vars['class'] = !empty($vars['widget']['settings']['insert_class']) ? $vars['widget']['settings']['insert_class'] : '';
}
