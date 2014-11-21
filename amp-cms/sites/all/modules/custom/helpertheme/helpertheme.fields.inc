<?php
/**
 * @file
 * Define helper functions.
 */

/**
 * Implements hook_element_info().
 *
 * Declare a new custom Form API element type.
 */
function helpertheme_element_info() {
  $types['radioicons'] = array(
    '#input' => TRUE,
    '#process' => array('form_process_radioicons'),
    '#theme_wrappers' => array('radios'),
    '#pre_render' => array('form_pre_render_conditional_form_element'),
  );
  return $types;
}

/**
 * Expands a radios element into individual radio elements.
 *
 * @see form_process_radios()
 */
function form_process_radioicons($element, &$form_state) {
  if (empty($element['#options'])) {
    return $element;
  }

  $element_name = $element['#name'];
  $option_key = $element_name . '_option';

  // Add a containter for all the options.
  $element[$option_key] = array(
    '#type' => 'container',
    '#tree' => TRUE,
  );
  $element[$option_key] += _helpertheme_build_radioicons($element);

  return $element;
}

/**
 * Returns render array for options.
 */
function _helpertheme_build_radioicons($element) {
  $items = array();

  $count = 0;
  $max = count($element['#options']);
  $weight = 0;
  foreach ($element['#options'] as $key => $choice) {
    $count++;
    $weight += 0.001;

    // Generate the parents as the autogenerator does, so we will have a
    // unique id for each radio button.
    $parents_for_id = array_merge($element['#parents'], array('option', $key));

    $items[$key] = array(
      '#type' => 'radio',
      '#title' => $choice,
      '#title_extra_class' => array('radioicon-label'),
      '#return_value' => $key,
      '#default_value' => isset($element['#default_value']) ? $element['#default_value'] : FALSE,
      '#parents' => $element['#parents'],
      '#id' => drupal_html_id('edit-' . implode('-', $parents_for_id)),
      '#ajax' => isset($element['#ajax']) ? $element['#ajax'] : NULL,
      '#weight' => $weight,
    );

    if (isset($element['#icons'][$key])) {
      $items[$key]['#title_extra_icon'] = $element['#icons'][$key];
    }
  }

  return $items;
}

/**
 * Implements hook_ds_field_theme_functions_info()
 *
 * Define theme functions for fields.
 *
 * Note that 'theme_ds_field_' is always needed, so the suggestions can work.
 *
 * @return $field_theme_functions
 *   A collection of field theming functions.
 */
function helpertheme_ds_field_theme_functions_info() {
  return array(
    'theme_ds_field_helpertheme_block' => t('HelperTheme: Block'),
    'theme_ds_field_helpertheme_list' => t('HelperTheme: List'),
    );
}

/**
 * Render field using minmal HTML.
 */
function theme_ds_field_helpertheme_block($variables) {
  $output = '';
  $config = $variables['ds-config'];
  $classes = isset($config['classes']) ? ' ' . $config['classes'] : '';

  // Add a simple wrapper.
  $output .= '<div class="field field-helpertheme-block field-name-' . strtr($variables['element']['#field_name'], '_', '-') . $classes . '">';

  // Render the label.
  if (!$variables['label_hidden']) {
    $output .= '<div class="label label-' . $variables['element']['#label_display'] . '">' . $variables['label'];
    if (!isset($config['lb-col'])) {
      $output .= ':&nbsp;';
    }
    $output .= '</div>';
  }

  // Render the items.
  foreach ($variables['items'] as $delta => $item) {
    $output .= drupal_render($item);
  }
  $output .="</div>";

  return $output;
}

/**
 * Render field as a simple HTML list.
 */
function theme_ds_field_helpertheme_list($variables) {
  $output = '';
  $config = $variables['ds-config'];
  $classes = isset($config['classes']) ? ' ' . $config['classes'] : '';

  // Add a simple wrapper.
  $output .= '<div class="field field-helpertheme-list field-name-' . strtr($variables['element']['#field_name'], '_', '-') . $classes . '">';

  // Render the label.
  if (!$variables['label_hidden']) {
    $output .= '<div class="label label-' . $variables['element']['#label_display'] . '">' . $variables['label'];
    if (!isset($config['lb-col'])) {
      $output .= ':&nbsp;';
    }
    $output .= '</div>';
  }

  // Render the items.
  $rendered_items = array();
  foreach ($variables['items'] as $delta => $item) {
    $rendered_items[] = drupal_render($item);
  }
  $output .= theme('item_list', array('items' => $rendered_items));

  $output .="</div>";

  return $output;
}
