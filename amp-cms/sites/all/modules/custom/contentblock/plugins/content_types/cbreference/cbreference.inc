<?php

/**
 * @file
 * Plugin to handle the 'entity' content type which allows individual entities
 * to be embedded into a panel.
 */

/**
 * Plugins are described by creating a $plugin array which will be used
 * by the system that includes this file.
 */
$plugin = array(
  'category' => t('Custom'),
  'title' => t('CB Reference'),
  'description' => t('Add a entity from your site as content stored in a variable.'),
  'defaults' => array(
    'machine_name' => '',
    'style' => 'normal',
  ),
);

/**
 * Output function for the 'entity' content type.
 *
 * Outputs a entity based on the module and delta supplied in the configuration.
 */
function contentblock_cbreference_content_type_render($subtype, $conf, $panel_args) {
  if (empty($conf['machine_name'])) {
    return;
  }

  $cbreference_info = contentblock_get_fixed_types($conf['machine_name']);
  if (empty($cbreference_info)) {
    return;
  }

  $block = new stdClass();
  $block->module = 'contentblock';
  $block->delta = $conf['machine_name'];
  $block->subtype = 'cbreference_machine_name';

  $block_css_classes = array();
  $block_css_classes[] = drupal_html_class('cbreference-' . $conf['machine_name']);
  if (!empty($cbreference_info['is section'])) {
    $block_css_classes[] = 'cbreference-section';
  }
  else {
    $block_css_classes[] = 'cbreference-block';
  }

  $entities = entity_load('contentblock', array(), array('machine_name' => $conf['machine_name']));
  $entity = reset($entities);

  if (empty($entity)) {
    $block->content['#contextual_links'] = array(
      'contentblock' => array('admin/settings/content-sections', array($conf['machine_name'])),
    );

    if (!empty($cbreference_info['is section'])) {
      $message = t('You have not configured this section. Click !here to go to the section settings page to add content and/or remove this message and title.', array(
        '!here' => l(t('here'), 'admin/settings/content-sections/' . $conf['machine_name']),
      ));
    }
    else {
      if ($block->delta == 'welcome') {
        $message = t('You have not configured this block. Click !here to go to the block settings page to edit content.', array(
          '!here' => l(t('here'), 'admin/settings/content-sections/' . $conf['machine_name']),
        ));

        $block->content['#prefix'] = '<div class="empty-contentblock clearfix">';
        $block->content['#suffix'] = '</div>';
      }
      elseif ($block->delta == 'totalactivities') {
        $message = t('You have not configured this block. Click !here to go to the block settings page to edit content.', array(
          '!here' => l(t('here'), 'admin/settings/content-sections/' . $conf['machine_name']),
        ));

        $block->content['#prefix'] = '<div class="empty-contentblock clearfix">';
        $block->content['#suffix'] = '</div>';
      }
      else {
        $message = t('You have not configured this block. Click !here to go to the block settings page to edit content and/or remove this message and title.', array(
          '!here' => l(t('here'), 'admin/settings/content-sections/' . $conf['machine_name']),
        ));

        $block->content['#prefix'] = '<div class="layout-block layout-separator">';
        $block->content['#suffix'] = '</div>';
      }
    }

    $block->content['title'] = array(
      '#markup' => '<h3 class="contentblock-title">' . $cbreference_info['title'] . '</h3>',
    );
    $block->content['content'] = array(
      '#markup' => '<p>' . $message . '</p>',
    );

    return $block;
  }

  $block->content['#contextual_links'] = array(
    'contentblock' => array('contentblock', array($entity->identifier())),
  );

  $view_mode = 'default';
  if (!entity_access('view', 'contentblock', $entity)) {
    return;
  }

  module_load_include('inc', 'contentblock', 'contentblock.cbreference');
  $function = 'contentblock_cbreference_render__' . $entity->machine_name;
  if (!empty($cbreference_info['is section'])) {
   $function = 'contentblock_cbreference_render__section';
  }
  if (function_exists($function)) {
    $return = $function($entity, $conf);
    if (empty($return)) {
      return;
    }

    if (isset($return['#view_mode'])) {
      $block_css_classes[] = drupal_html_class('cbreference-view-mode-' . $return['#view_mode']);
    }

    // Make sure we don't display an empty block.
    $children = element_children($return);
    if (empty($children)) {
      return;
    }

    $block->content[] = $return;
  }

  if (!empty($block_css_classes)) {
    $block->css_class = implode(' ', $block_css_classes);
  }

  return $block;
}

/**
 * The form to add or edit a entity as content.
 */
function contentblock_cbreference_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  $form['machine_name'] = array(
    '#title' => t('Machine Name'),
    '#description' => t('The machine_name of the referenced content.'),
    '#type' => 'textfield',
    '#default_value' => $conf['machine_name'],
    '#maxlength' => 512,
    '#weight' => -20,
  );

  $form['style'] = array(
    '#type' => 'radios',
    '#title' => t('Style'),
    '#default_value' => $conf['style'],
    '#options' => array(
      'normal' => 'Normal',
      'wide' => 'Wide',
    ),
  );

  return $form;
}

/**
 * Validate the entity selection.
 */
function contentblock_cbreference_content_type_edit_form_submit($form, &$form_state) {
  foreach (array('machine_name', 'style') as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}

/**
 * Returns the administrative title for a entity.
 */
function contentblock_cbreference_content_type_admin_title($subtype, $conf) {
  if (!empty($conf['machine_name'])) {
    return t('CB Reference, machine_name: %machine_name', array('%machine_name' => $conf['machine_name']));
  }
}
