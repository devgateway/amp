<?php
/**
 * @file
 * ampet_slide.ds.inc
 */

/**
 * Implements hook_ds_field_settings_info().
 */
function ampet_slide_ds_field_settings_info() {
  $export = array();

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'node|slide|default';
  $ds_fieldsetting->entity_type = 'node';
  $ds_fieldsetting->bundle = 'slide';
  $ds_fieldsetting->view_mode = 'default';
  $ds_fieldsetting->settings = array(
    'title_field' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
  );
  $export['node|slide|default'] = $ds_fieldsetting;

  return $export;
}

/**
 * Implements hook_ds_layout_settings_info().
 */
function ampet_slide_ds_layout_settings_info() {
  $export = array();

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'node|slide|default';
  $ds_layout->entity_type = 'node';
  $ds_layout->bundle = 'slide';
  $ds_layout->view_mode = 'default';
  $ds_layout->layout = 'simplelayout';
  $ds_layout->settings = array(
    'regions' => array(
      'main' => array(
        0 => 'field_slider_image',
        1 => 'title_field',
        2 => 'body',
        3 => 'field_slider_link',
      ),
    ),
    'fields' => array(
      'field_slider_image' => 'main',
      'title_field' => 'main',
      'body' => 'main',
      'field_slider_link' => 'main',
    ),
    'classes' => array(
      'layout_class' => array(
        '' => '',
      ),
    ),
    'wrappers' => array(
      'main' => 'div',
      'ds_hidden' => 'div',
    ),
    'layout_wrapper' => 'div',
    'layout_attributes' => '',
    'layout_attributes_merge' => 1,
    'layout_link_attribute' => '',
    'layout_link_custom' => '',
  );
  $export['node|slide|default'] = $ds_layout;

  return $export;
}
