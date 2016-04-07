<?php

/**
 * Implements hook_form_FORM_ID_alter() for contentblock_form().
 */
function ampet_slide_form_contentblock_edit_fixed_form_alter(&$form, &$form_state) {
  if ($form['#entity']->machine_name == 'welcome') {
    $using_slider = (empty($form['#entity']->field_cbslides)) ? FALSE : TRUE;
    $form['enable_slider'] = array(
      '#type' => 'checkbox',
      '#title' => t('Use a Slider'),
      '#description' => t('If you want, you can use a slider instead a static welcome header.'),
      '#default_value' => $using_slider,
    );
    // Show slides when the checkbox is checked.
    $form['field_cbslides']['#states'] = array(
      'visible' => array(
        ':input[name="enable_slider"]' => array('checked' => TRUE),
      ),
    );
    // Hide the other fields when the checkbox is checked.
    $form['field_cblinks']['#states'] = array(
      'visible' => array(
        ':input[name="enable_slider"]' => array('checked' => FALSE),
      ),
    );
    $form['field_cbimage']['#states'] = array(
      'visible' => array(
        ':input[name="enable_slider"]' => array('checked' => FALSE),
      ),
    );
    $form['field_cbbody']['#states'] = array(
      'visible' => array(
        ':input[name="enable_slider"]' => array('checked' => FALSE),
      ),
    );
    $form['#validate'][] = 'ampet_slide_form_contentblock_slide_count_validate';
  }
}

/**
 * A simple form validation.
 *
 * We check if that user has at least 2 slides.
 */
function ampet_slide_form_contentblock_slide_count_validate($form, &$form_state) {
  if ($form_state['values']['enable_slider'] && in_array('_contentblock_entity_form_submit', $form_state['submit_handlers'], TRUE)) {
    $ief = reset($form_state['inline_entity_form']);
    if (!empty($ief['entities']) && count($ief['entities']) < 2) {
      form_set_error('field_cbslides', t('Make sure you are using more than 1 slide, otherwise using a slider just doesn\'t make sense.'));
    }
  }
  elseif ($form_state['values']['enable_slider'] == FALSE && in_array('_contentblock_entity_form_submit', $form_state['submit_handlers'], TRUE)) {
    $ief_key = key($form_state['inline_entity_form']);
    $form_state['inline_entity_form'][$ief_key]['entities'] = array();
    $form_state['values']['field_cbslides'][LANGUAGE_NONE]['entities'] = array();
  }
}

/**
 * Implements hook_node_validate().
 */
function ampet_slide_node_validate($node, $form, &$form_state) {
  if ($node->type == 'slide') {
    $langcode = isset($form_state['entity_translation']['form_langcode']) ? $form_state['entity_translation']['form_langcode'] : LANGUAGE_NONE;
    $values = $form_state['values'];
    if (!empty($values['body'][$langcode][0]['value'])) {
      $max_char_count = 320;
      $body_char_count = _contentblock_cbreference_count_markup($values['body'][$langcode][0]['value']);
      if ($body_char_count > $max_char_count) {
        form_set_error('body', t('Field Description should not have more than %max characters (HTML markup does not count), %count given.', array('%max' => $max_char_count, '%count' => $body_char_count)));
      }
    }
  }
}

/**
 * Implements hook_form_alter().
 */
function ampet_slide_inline_entity_form_entity_form_alter(&$form, &$form_state) {
  $entity = $form['#entity'];
  if ($entity->type == 'slide') {
    $form['#element_validate'][] = 'ampet_slide_ief_slide_validate';
  }
}

/**
 * An additional form validation.
 */
function ampet_slide_ief_slide_validate(&$form, &$form_state) {
  $entity = $form['#entity'];
  $langcode = isset($entity->language) ? $entity->language : LANGUAGE_NONE;
  $values = drupal_array_get_nested_value($form_state['values'], $form['#array_parents']);
  if (!empty($values['body'][$langcode][0]['value'])) {
    $max_char_count = 320;
    $body_char_count = _contentblock_cbreference_count_markup($values['body'][$langcode][0]['value']);
    if ($body_char_count > $max_char_count) {
      $keys_path = implode('][', $form['#array_parents']);
      form_set_error($keys_path . '][body', t('Field Description should not have more than %max characters (HTML markup does not count), %count given.', array('%max' => $max_char_count, '%count' => $body_char_count)));
    }
  }
}

/**
 * Implements hook_field_display_alter().
 */
function ampet_slide_field_display_alter(&$display, $context) {
  // Apply only to nodes of CT 'slide'.
  if ($context['entity_type'] == 'node' && $context['entity']->type == 'slide') {
    if ($context['field']['field_name'] == 'title_field') {
      $display = array(
        'label' => 'hidden',
        'module' => 'title',
        'settings' => array(
          'title_class' => 'contentblock-title',
          'title_link' => '',
          'title_style' => 'h2',
        ),
        'type' => 'title_linked',
        'weight' => 1,
      );
    }
    if ($context['field']['field_name'] == 'field_slider_image') {
      $display['settings']['image_style'] = 'img-hfeatured';
    }
    if ($context['field']['field_name'] == 'field_slider_link') {
      $display = array(
        'label' => 'hidden',
        'settings' => array(),
        'type' => 'hidden',
      );
    }
  }
}

/**
 * Implements hook_node_view_alter().
 */
function ampet_slide_node_view_alter(&$build) {
  // We want to alter only the slide CT.
  if ($build['#node']->type == 'slide') {
    $entity_langcode = entity_translation_get_existing_language('node', $build['#node']);
    $wrapper = entity_metadata_wrapper('node', $build['#node']);
    $wrapper->language($entity_langcode);

    // Get the link.
    $link = $wrapper->field_slider_link->value();
    if (!empty($link['url'])) {
      $view['field_slider_image'][0]['#path']['path'] = $link['url'];
      $view['field_slider_image'][0]['#path']['options']['attributes'] = $link['attributes'];

      $svgicon = helpertheme_get_svg_icons('add', array('width' => '3em', 'height' => '3em')) . ' ';
      $link_title = $svgicon . '<span class="text">' . t('read more') . '</span>';

      $build['view_link'] = array(
        '#theme' => 'link',
        '#path' => $link['url'],
        '#text' => $link_title,
        '#options' => array(
          'html' => TRUE,
          'attributes' => array(
              'class' => array('more-link'),
              'title' => t('read more'),
            ) + $link['attributes'],
        ),
        '#weight' => 50,
      );
    }
  }
}
