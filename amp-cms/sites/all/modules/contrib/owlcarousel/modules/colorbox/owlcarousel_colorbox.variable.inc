<?php

/**
 * @file
 * owlcarousel_colorbox.variable.inc
 */

/**
 * Implements hook_variable_info().
 */
function owlcarousel_colorbox_variable_info($options) {
  $variable['owlcarousel_colorbox_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Colorbox', array(), $options),
    'description' => t('Enable Colorbox support.', array(), $options),
    'repeat' => array(
      'type' => 'boolean',
      'default' => FALSE
    ),
    'group' => 'owlcarousel'
  );
  $variable['owlcarousel_colorbox_imagestyle_[instance_id]'] = array(
    'type' => 'multiple',
    'title' => t('Colorbox Image Style', array(), $options),
    'description' => t('The image style to apply to Colorbox images.', array(), $options),
    'repeat' => array(
      'type' => 'select',
      'options callback' => 'image_style_options'
    ),
    'group' => 'owlcarousel'
  );

  return $variable;
}
