<?php

/**
 * @file
 * owlcarousel.theme.inc
 *
 * Implements theme functions for Owl Carousel.
 */

/**
 * Template process carousel theme.
 */
function template_process_owlcarousel(&$vars) {
  $instance_id = $vars['settings']['instance'];
  $vars['settings']['attributes']['class'][] = $vars['settings']['id'];
}

/**
 * Template preprocess carousel wrapper.
 */
function template_preprocess_owlcarousel_wrapper(&$variables) {
  if (count($variables['output']['#items']) < 2) {
    $variables['output']['#settings']['attributes']['class'][] = 'disabled';
  }
}

/**
 * Theme declaration for Owl Carousel wrapper.
 */
function theme_owlcarousel_wrapper($variables) {
  $attributes = $variables['output']['#settings']['attributes'];

  return '<div' . drupal_attributes($attributes) . '>' . drupal_render($variables['output']) . '</div>';
}

/**
 * Theme declaration for Owl Carousel.
 */
function theme_owlcarousel($variables) {
  $instance = $variables['settings']['id'];
  $settings = $variables['settings'];
  $items = $variables['items'];

  $output = array(
    '#output' => array(
      'settings' => $settings,
      'items' => $items
    ),
    '#pre_render' => array('owlcarousel_pre_render_cache'),
  );

  return drupal_render($output);
}

/**
 * Construct element for render.
 */
function owlcarousel_pre_render_cache($element) {
  $settings = $element['#output']['settings']['instance'];
  $instance = $element['#output']['settings']['id'];

  $output = array(
    '#theme' => 'owlcarousel_list',
    '#items' => $element['#output']['items'],
    '#settings' => $element['#output']['settings']
  );

  // Load carousel settings from the instance id.
  $instance_settings = _owlcarousel_return_carousel_instance_settings($settings);

  // Provide legacy settings alter.
  drupal_alter('owlcarousel_settings', $instance_settings, $instance);

  // Element output /w attached.
  $element['#markup'] = owlcarousel_build($output);
  
  // Provide alter before carousel is rendered.
  drupal_alter('owlcarousel_pre_render', $element);

  $element['#children'] = drupal_render($element['#markup']);
  $element['#attached'] = array(
    'js' => array(
      array(
        'data' => drupal_get_path('module', 'owlcarousel') . '/includes/js/owlcarousel.settings.js',
        'type' => 'file',
        'scope' => 'footer'
      ),
      array(
        'data' => array('owlcarousel' => array(
          $instance => array(
            'settings' => $instance_settings,
            'views' => array(
              'ajax_pagination' => variable_get('owlcarousel_override_ajax_pagination_' . $settings)
            ),
          ),
        ),),
        'type' => 'setting'
      )
    ),
    'library' => array(
      array(
        'owlcarousel',
        'owl-carousel'
      )
    ),
  );

  return $element;
}

/**
 * Build final output array.
 */
function owlcarousel_build($output) {
  return array(
    '#output' => $output,
    '#theme_wrappers' => array('owlcarousel_wrapper'),
  );
}

/**
 * Default theme implementation for lists
 */
function theme_owlcarousel_list(&$vars) {
  $items = &$vars['items'];
  $output = '';

  if (!empty($items)) {
    foreach ($items as $i => $item) {
      if ($item['row']) {
        $striping = $i % 2 == 0 ? 'item-odd' : 'item-even';
        $output .= theme('owlcarousel_list_item', array(
          'item' => $item['row'],
          'class' => drupal_html_class('item-' . $i). ' ' . drupal_html_class($striping),
          'group' => $vars['settings']['instance']
        ));
      }
    }
  }

  return $output;
}


/**
 * Default theme implementation for carousel items
 */
function theme_owlcarousel_list_item(&$vars) {
  return '<div class="' . $vars['class'] . '">' . $vars['item'] . '</div>';
}
