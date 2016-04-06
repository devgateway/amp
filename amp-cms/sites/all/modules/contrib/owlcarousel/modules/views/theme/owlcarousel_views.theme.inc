<?php

/**
 * @file
 * owlcarousel_views.theme.inc
 */

/**
 * Add helper variables for carousel view.
 */
function template_process_owlcarousel_views(&$vars) {
  if (!empty($vars['view'])) {
    $options = $vars['options'];
    $view = $vars['view'];

    $function = !module_exists('variable') ? 'variable_get' : 'variable_get_value';
    if ($function('owlcarousel_override_ajax_pagination_' . $options['instance'])) {
      drupal_add_js(drupal_get_path('module', 'owlcarousel_views') . '/js/owlcarousel.views.js', array('scope' => 'footer', 'type' => 'file'));
      drupal_add_css(drupal_get_path('module', 'owlcarousel_views') . '/css/owlcarousel.views.css', array('scope' => 'header', 'type' => 'file'));
    }

    $items = array();
    $row_plugin = $view->style_plugin->row_plugin;
    foreach ($vars['rows'] as $id => $row) {
      $view->row_index = $id;

      if ($item['row'] = $row_plugin->render($row)) {
        $items[] = $item;
      }
    }

    // Add the slide items.
    $options['id'] = $vars['options']['id'];
    $vars['items'] = $items;
    $vars['settings'] = $options;
  }
}

/**
 * Theme callback for pager.
 */
function theme_owlcarousel_views_pager($vars) {
  global $pager_page_array, $pager_total;

  $element = $vars['element'];
  $options = $vars['options'];

  $prev_label = (!isset($options['prev_label'])) ? $options['prev_label'] : 'previous';
  $next_label = (!isset($options['next_label'])) ? $options['next_label'] : 'next';

  if ($pager_total[$element] > 1) {
    $items = array(
      array(
        'class' => array('pager-previous'),
        'data' => l(t($prev_label), '', array('attributes' => array('class' => 'previous'), 'external' => TRUE, 'fragment' => 'pre')),
      ),
      array(
        'class' => array('pager-next'),
        'data' => l(t($next_label), '', array('attributes' => array('class' => 'next'), 'external' => TRUE, 'fragment' => 'next')),
      ),
    );

    return theme('item_list', array(
      'items' => $items,
      'attributes' => array('class' => array('pager')),
    ));
  }
}
