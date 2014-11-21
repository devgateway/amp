<?php
/**
 * @file
 * ampcontentblock.ds.inc
 */

/**
 * Implements hook_ds_field_settings_info().
 */
function ampcontentblock_ds_field_settings_info() {
  $export = array();

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'contentblock|hidden|default';
  $ds_fieldsetting->entity_type = 'contentblock';
  $ds_fieldsetting->bundle = 'hidden';
  $ds_fieldsetting->view_mode = 'default';
  $ds_fieldsetting->settings = array(
    'title_field' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
    'field_cbblocks' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_helpertheme_block',
        ),
      ),
    ),
  );
  $export['contentblock|hidden|default'] = $ds_fieldsetting;

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'contentblock|predefined|default';
  $ds_fieldsetting->entity_type = 'contentblock';
  $ds_fieldsetting->bundle = 'predefined';
  $ds_fieldsetting->view_mode = 'default';
  $ds_fieldsetting->settings = array(
    'title_field' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
    'field_cbwidget' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
  );
  $export['contentblock|predefined|default'] = $ds_fieldsetting;

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'contentblock|text|default';
  $ds_fieldsetting->entity_type = 'contentblock';
  $ds_fieldsetting->bundle = 'text';
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
  $export['contentblock|text|default'] = $ds_fieldsetting;

  return $export;
}

/**
 * Implements hook_ds_layout_settings_info().
 */
function ampcontentblock_ds_layout_settings_info() {
  $export = array();

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'contentblock|hidden|default';
  $ds_layout->entity_type = 'contentblock';
  $ds_layout->bundle = 'hidden';
  $ds_layout->view_mode = 'default';
  $ds_layout->layout = 'simplelayout';
  $ds_layout->settings = array(
    'regions' => array(
      'main' => array(
        0 => 'title_field',
        1 => 'field_cbimage',
        2 => 'field_cblinks',
        3 => 'field_cbblocks',
        4 => 'field_cbbody',
      ),
    ),
    'fields' => array(
      'title_field' => 'main',
      'field_cbimage' => 'main',
      'field_cblinks' => 'main',
      'field_cbblocks' => 'main',
      'field_cbbody' => 'main',
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
  $export['contentblock|hidden|default'] = $ds_layout;

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'contentblock|predefined|default';
  $ds_layout->entity_type = 'contentblock';
  $ds_layout->bundle = 'predefined';
  $ds_layout->view_mode = 'default';
  $ds_layout->layout = 'simplelayout';
  $ds_layout->settings = array(
    'regions' => array(
      'main' => array(
        0 => 'title_field',
        1 => 'field_cbwidget',
      ),
    ),
    'fields' => array(
      'title_field' => 'main',
      'field_cbwidget' => 'main',
    ),
    'classes' => array(
      'layout_class' => array(
        'layout-block' => 'layout-block',
        'layout-separator' => 'layout-separator',
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
  $export['contentblock|predefined|default'] = $ds_layout;

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'contentblock|text|default';
  $ds_layout->entity_type = 'contentblock';
  $ds_layout->bundle = 'text';
  $ds_layout->view_mode = 'default';
  $ds_layout->layout = 'simplelayout';
  $ds_layout->settings = array(
    'regions' => array(
      'main' => array(
        0 => 'title_field',
        1 => 'field_cbbody',
      ),
    ),
    'fields' => array(
      'title_field' => 'main',
      'field_cbbody' => 'main',
    ),
    'classes' => array(
      'layout_class' => array(
        'layout-block' => 'layout-block',
        'layout-separator' => 'layout-separator',
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
  $export['contentblock|text|default'] = $ds_layout;

  return $export;
}
