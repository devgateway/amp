<?php

/**
 * @file
 * Definition of the 'CB Style' region and page style.
 */

// Plugin definition.
$plugin = array(
  'title' => t('CB Style'),
  'description' => t('Display the panes as a Content Block.'),

  'render region' => 'cbstyle_style_render_region',
  'settings form' => 'contentblock_cbstyle_style_region_settings_form',

  'render pane' => 'cbstyle_style_render_pane',
  'pane settings form' => 'contentblock_cbstyle_style_pane_settings_form',
);

/**
 * 'settings form' callback.
 */
function contentblock_cbstyle_style_region_settings_form($style_settings, $display, $pid, $type) {
  module_load_include('inc', 'ds', 'includes/ds.field_ui');
  $ds_classes = _ds_classes('ds_classes_regions');
  if (isset($ds_classes[''])) {
    unset($ds_classes['']);
  }
  $form['region_classes'] = array(
    '#type' => 'select',
    '#multiple' => TRUE,
    '#options' => $ds_classes,
    '#title' => t('Choose CSS classes for the region'),
    '#default_value' => isset($style_settings['region_classes']) ? $style_settings['region_classes'] : array(),
  );

  $form['css_id'] = array(
    '#type' => 'textfield',
    '#default_value' => isset($style_settings['css_id']) ? $style_settings['css_id'] : '',
    '#title' => t('CSS ID'),
    '#description' => t('CSS ID to apply to this pane. This may be blank.'),
  );

  $form['extra_classes'] = array(
    '#type' => 'textfield',
    '#default_value' => isset($style_settings['extra_classes']) ? $style_settings['extra_classes'] : '',
    '#title' => t('Extra CSS class'),
    '#description' => t('CSS class to apply to this pane. This may be blank.'),
  );

  return $form;
}

/**
 * 'pane settings form' callback.
 */
function contentblock_cbstyle_style_pane_settings_form($style_settings, $display, $pid, $type) {
  module_load_include('inc', 'ds', 'includes/ds.field_ui');
  $ds_classes = _ds_classes('ds_classes_regions');
  if (isset($ds_classes[''])) {
    unset($ds_classes['']);
  }
  $form['pane_classes'] = array(
    '#type' => 'select',
    '#multiple' => TRUE,
    '#options' => $ds_classes,
    '#title' => t('Choose CSS classes for the region'),
    '#default_value' => isset($style_settings['pane_classes']) ? $style_settings['pane_classes'] : array(),
  );

  return $form;
}

/**
 * Render region callback.
 */
function theme_cbstyle_style_render_region($vars) {
  $contentblock_attributes = array();
  if (!empty($vars['settings']['css_id'])) {
    // Make sure the provided CSS ID is really unique.
    $contentblock_attributes['id'] = drupal_html_id($vars['settings']['css_id']);
  }
  else {
    // Create a unique ID for the ContentBlock Widget.
    $contentblock_attributes['id'] = drupal_html_id('contentblock-' . $vars['region_id']);
  }

  // Add a class to the wrapper.
  $contentblock_attributes['class'][] = 'panel-contentblock';
  if (!empty($vars['settings']['region_classes'])) {
    $contentblock_attributes['class'] += $vars['settings']['region_classes'];
  }

  if (!empty($vars['settings']['extra_classes'])) {
    // Sanitize the provided list of classes.
    $class_list = explode(' ', $vars['settings']['extra_classes']);
    foreach ($class_list as $class) {
      $contentblock_attributes['class'][] = drupal_html_class($class);
    }
  }

  $elements = array(
    '#tree' => TRUE,
    '#prefix' => '<div' . drupal_attributes($contentblock_attributes) . '>',
    '#suffix' => '</div>',
  );

  // Process the rendered panes.
  $delta = 0;
  foreach ($vars['panes'] as $pane_id => $pane_html_content) {
    // Make sure we have some content.
    if (empty($pane_html_content)) {
      continue;
    }

    // Add the section content.
    $elements[$pane_id . '_content'] = array(
      '#markup' => $pane_html_content,
      '#weight' => 2 * $delta + 2,
    );

    if (!empty($vars['display']->content[$pane_id]->configuration['title'])) {
      $title = $vars['display']->content[$pane_id]->configuration['title'];
      // Create a class for the current contentblock.
      $elements[$pane_id . '_content']['#attributes']['class'][] = drupal_html_class('contentblock-content-' . $title);
    }

    $delta++;
  }

  return drupal_render($elements);
}

/**
 * Render callback.
 *
 * @ingroup themeable
 */
function theme_cbstyle_style_render_pane($vars) {
  // Make sure we have some content.
  if (empty($vars['content']->content)) {
    return;
  }

  $contentblock_attributes = array();
  if (!empty($vars['pane']->css['css_id'])) {
    // Provided CSS ID must be unique!
    $contentblock_attributes['id'] = drupal_html_id($vars['pane']->css['css_id']);
  }
  else {
    // Create a unique ID for the ContentBlock Widget.
    $contentblock_attributes['id'] = drupal_html_id('contentblock-' . $vars['pane']->pid);
  }

  // Add a class to the wrapper.
  $contentblock_attributes['class'][] = 'panel-contentblock';
  if (!empty($vars['settings']['pane_classes'])) {
    $contentblock_attributes['class'] += $vars['settings']['pane_classes'];
  }

  if (!empty($vars['pane']->css['css_class'])) {
    // Sanitize the provided list of classes.
    $class_list = explode(' ', $vars['pane']->css['css_class']);
    foreach ($class_list as $class) {
      $contentblock_attributes['class'][] = drupal_html_class($class);
    }
  }

  $elements = array(
    '#tree' => TRUE,
    '#prefix' => '<div' . drupal_attributes($contentblock_attributes) . '>',
    '#suffix' => '</div>',
  );

  // Add the section title.
  if (!empty($vars['content']->title)) {
    $elements['pane_title'] = array(
      '#tag' => 'h3',
      '#theme' => 'html_tag',
      '#value' => $vars['content']->title,
      '#attributes' => array(
        'class' => array(
          'pane-title',
        ),
      ),
    );
  }

  $elements['pane_content'] = array(
    '#type' => 'container',
    '#attributes' => array(
      'class' => array(
        'contentblock-pane-content',
      ),
    ),
  );
  if (is_string($vars['content']->content)) {
    $elements['pane_content']['content']['#markup'] = $vars['content']->content;
  }
  else if (is_array($vars['content']->content)) {
    $elements['pane_content'] += $vars['content']->content;
  }

  if (!empty($vars['content']->title)) {
    // Add the section content.
    $class = strip_tags($vars['content']->title);
    $elements['pane_content']['#attributes']['class'][] = drupal_html_class('contentblock-content-' . $class);
  }

  return drupal_render($elements);
}
