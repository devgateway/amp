<?php
/**
 * @file
 * ampviewsettings.ds.inc
 */

/**
 * Implements hook_ds_custom_fields_info().
 */
function ampviewsettings_ds_custom_fields_info() {
  $export = array();

  $ds_field = new stdClass();
  $ds_field->api_version = 1;
  $ds_field->field = 'linkedmap_block';
  $ds_field->label = 'Linked Map Block';
  $ds_field->field_type = 7;
  $ds_field->entities = array(
    'node' => 'node',
  );
  $ds_field->ui_limit = '';
  $ds_field->properties = array(
    'default' => array(),
    'settings' => array(
      'show_title' => array(
        'type' => 'checkbox',
      ),
      'title_wrapper' => array(
        'type' => 'textfield',
        'description' => 'Eg: h1, h2, p',
      ),
      'ctools' => array(
        'type' => 'ctools',
      ),
    ),
  );
  $export['linkedmap_block'] = $ds_field;

  return $export;
}

/**
 * Implements hook_ds_view_modes_info().
 */
function ampviewsettings_ds_view_modes_info() {
  $export = array();

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'alternative';
  $ds_view_mode->label = 'Alternative';
  $ds_view_mode->entities = array(
    'contentblock' => 'contentblock',
  );
  $export['alternative'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'teaser_featured';
  $ds_view_mode->label = 'Teaser: Featured';
  $ds_view_mode->entities = array(
    'node' => 'node',
  );
  $export['teaser_featured'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'teaser_normal';
  $ds_view_mode->label = 'Teaser: Normal';
  $ds_view_mode->entities = array(
    'node' => 'node',
  );
  $export['teaser_normal'] = $ds_view_mode;

  return $export;
}
