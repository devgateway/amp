<?php

/*
 * define plugin
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('External source'),
  'description' => t('Provides possibility for embedding external source HTML through iframe'),
  'category' => array(t('AMPP Widgets')),
  'defaults' => array(
    'source' => 'http://',
    'width' => '300',
    'height' => '200',
  ),
);

/**
 *
 */
function ampp_projects_ext_source_content_type_render($subtype, $conf, $panel_args, $context) {
  $block = new stdClass();

  $block->content = '<iframe src="' . $conf['source'] . '" width="' . $conf['width'] . '" height="' . $conf['height'] . '" frameborder="0"></iframe>';

  return $block;
}

/*
 * Implementing hook_content_type_edit_form()
 */
function ampp_projects_ext_source_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  $form['override_title_markup']['#access'] = FALSE;

  $form['source'] = array(
    '#title' => t('Source'),
    '#type' => 'textfield',
    '#size' => 60,
    '#maxlength' => 255,
    '#title' => t("Absolute url to the external source"),
    '#default_value' => $conf['source'] ? $conf['source'] : $form_state['plugin']['defaults']['source'],
  );

  $form['width'] = array(
    '#title' => t('Width'),
    '#type' => 'textfield',
    '#size' => 4,
    '#maxlength' => 4,
    '#description' => t("Width of the iframe in pixels"),
    '#default_value' => $conf['width'] ? $conf['width'] : $form_state['plugin']['defaults']['width'],
    '#element_validate' => array('_width_height_validate'),
  );

  $form['height'] = array(
    '#title' => t('Height'),
    '#type' => 'textfield',
    '#size' => 4,
    '#maxlength' => 4,
    '#description' => t("Height of the iframe in pixels"),
    '#default_value' => $conf['height'] ? $conf['height'] : $form_state['plugin']['defaults']['height'],
    '#element_validate' => array('_width_height_validate'),
  );

  return $form;
}

/*
* Handles edit for submit
*/
function ampp_projects_ext_source_content_type_edit_form_submit(&$form, &$form_state) {
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}

/*
* Validation function
*/
function _width_height_validate($element, &$form_state, $form) {
  if (!intval($element['#value']) || intval($element['#value']) < 0) {
    form_error($element, t('Must be greater than zero integer value'));
  }
}
