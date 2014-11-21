<?php
/**
 * @file
 * Define helper functions.
 */

/**
 * Returns an array of links for a navigation menu.
 */
function _helpertheme_color_palette_demo_page() {
  $items = array(
    '#prefix' => '<div class="helpertheme-color-palette-wrapper">',
    '#suffix' => '</div>',
  );

  // Get the list of colors for the theme.
  module_load_include('inc', 'helpertheme', 'helpertheme.colors');
  foreach (helpertheme_color_pallete() as $group_key => $color_group) {
    $items[$group_key] = array(
      '#type' => 'fieldset',
      '#title' => t($color_group['#title']),
      '#collapsible' => TRUE,
      '#collapsed' => TRUE,
      '#weight' => $color_group['#weight'],
    );

    $less_variables[$group_key] = array();
    foreach (element_children($color_group) as $color_key) {
      $items[$group_key][$color_key] = array(
        '#type' => 'container',
        '#attributes' => array(
          'class' => array('helpertheme-color', 'clearfix'),
        ),
      );

      $color_item = _helpertheme_color_palette_item($color_key, $color_group[$color_key]);
      $items[$group_key][$color_key] += $color_item['html_markup'];
      $less_variables[$group_key] += $color_item['less_variables'];
    }
  }

  $items['overview'] = array(
    '#type' => 'fieldset',
    '#title' => t('Overview'),
    '#collapsible' => TRUE,
    '#collapsed' => FALSE,
    '#attributes' => array(
      'class' => array('palette-overview'),
    ),
    '#weight' => -100,
  );

  // @TODO: Add JavaScript!
  $items['overview']['toggle'] = array(
    '#type' => 'link',
    '#title' => t('Toggle variants visibility'),
    '#href' => '',
    '#options' => array(
      'external' => TRUE,
      'fragment' => ' ',
    ),
    '#attributes' => array(
      'class' => 'palette-overview-toggle',
    ),
  );

  foreach ($less_variables as $group_key => $variables) {
    $items['overview'][$group_key] = array(
      '#type' => 'container',
      '#attributes' => array(
        'class' => 'palette-overview-group',
      ),
    );

    foreach ($variables as $name => $value) {
      $items['overview'][$group_key]['title'] = array(
        '#type' => 'html_tag',
        '#tag' => 'div',
        '#value' => $items[$group_key]['#title'],
      );

      $items['overview'][$group_key][$name] = array(
        '#type' => 'link',
        '#title' => '@' . $name,
        '#href' => '',
        '#options' => array(
          'external' => TRUE,
          'fragment' => 'colorblock-' . $name,
        ),
        '#attributes' => array(
          'class' => array(
            'palette-overview-item',
            'colorblock-' . $name,
          ),
          'title' => 'Jump to: @' . $name,
        ),
      );
      if (!in_array($name, array_keys($items[$group_key]))) {
        $items['overview'][$group_key][$name]['#attributes']['class'][] = 'is-variant';
      }
    }
  }

  // Add page specific less.
  $file_less_settings['variables'] = array();
  foreach ($less_variables as $variables) {
    $file_less_settings['variables'] += $variables;
  }
  drupal_add_css(drupal_get_path('module', 'helpertheme') . '/styles/helpertheme.demo.css.less', array('less' => $file_less_settings, 'preprocess' => FALSE));
  drupal_add_js(drupal_get_path('module', 'helpertheme') . '/scripts/helpertheme.demo.js', array('preprocess' => FALSE));

  // We don't want to hit the 100 max nexting php error when compiling the less file.
  // TODO: Fix .loopClass() less mixin.
  $file_less_settings['variables']['colorblock_list'] = implode(' ', array_keys($file_less_settings['variables']));
  drupal_add_css(drupal_get_path('module', 'helpertheme') . '/styles/helpertheme.colors.css.less', array('less' => $file_less_settings, 'preprocess' => FALSE));

  return drupal_render($items);
}

/**
 * Returns a html for a "color block".
 *
 * We don't use check_plain() since everything is from code.
 */
function _helpertheme_color_palette_item($name, $info) {
  $less_variables = array(
    $name => $info['#value'],
  );

  $output['colorblock'] = array(
    '#type' => 'container',
    '#attributes' => array(
      'class' => 'colorblock colorblock-' . $name,
      'id' => 'colorblock-' . $name,
    ),
  );

  $output['colorinfo'] = array(
    '#type' => 'container',
    '#attributes' => array(
      'class' => 'colorinfo clearfix',
    ),
  );

  $output['colorinfo']['name'] = array(
    '#type' => 'html_tag',
    '#tag' => 'div',
    '#value' => 'name: <span>@' . $name . '</span>',
    '#attributes' => array(
      'class' => 'colorname',
    ),
  );

  // $less_settings['variables']
  $output['colorinfo']['value'] = array(
    '#type' => 'html_tag',
    '#tag' => 'div',
    '#value' => 'value: <span>' . $info['#value'] . '</span>',
    '#attributes' => array(
      'class' => array(
        'colorvalue',
        'colorvalue-' . $name,
      ),
    ),
  );

  // Also display the final value.
  if (!preg_match('/^#([a-f0-9]{3}){1,2}$/iD', $info['#value'])) {
    $output['colorinfo']['value']['#attributes']['class'][] = 'display-processed-value';
  }

  $output['colorinfo']['desc'] = array(
    '#type' => 'html_tag',
    '#tag' => 'div',
    '#value' => 'used for: ' . $info['#desc'],
    '#attributes' => array(
      'class' => 'colordesc',
    ),
  );

  if (!empty($info['#variants'])) {
    $output['colorvariants'] = array(
      '#type' => 'container',
      '#attributes' => array(
        'class' => 'colorvariants clearfix',
      ),
    );

    $output['colorvariants']['variants_header'] = array(
      '#type' => 'html_tag',
      '#tag' => 'h4',
      '#value' => t('Variants:'),
    );

    foreach ($info['#variants'] as $variant_name => $variant_info) {
      $variant = _helpertheme_color_palette_item($variant_name, $variant_info);
      $output['colorvariants'][$variant_name] = $variant['html_markup'];
      $less_variables += $variant['less_variables'];
    }
  }

  return array(
    'html_markup' => $output,
    'less_variables' => $less_variables,
  );
}
