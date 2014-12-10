<?php
/**
 * @file
 * ampet_page.field_group.inc
 */

/**
 * Implements hook_field_group_info().
 */
function ampet_page_field_group_info() {
  $export = array();

  $field_group = new stdClass();
  $field_group->disabled = FALSE; /* Edit this to true to make a default field_group disabled initially */
  $field_group->api_version = 1;
  $field_group->identifier = 'group_title_wrapper|node|page|full';
  $field_group->group_name = 'group_title_wrapper';
  $field_group->entity_type = 'node';
  $field_group->bundle = 'page';
  $field_group->mode = 'full';
  $field_group->parent_name = '';
  $field_group->data = array(
    'label' => 'Title Wrapper',
    'weight' => '0',
    'children' => array(
      0 => 'title_field',
    ),
    'format_type' => 'div',
    'format_settings' => array(
      'label' => 'Title Wrapper',
      'instance_settings' => array(
        'id' => '',
        'classes' => 'page-title-wrapper',
        'description' => '',
        'show_label' => '0',
        'label_element' => 'h3',
        'effect' => 'none',
        'speed' => 'none',
      ),
      'formatter' => 'open',
    ),
  );
  $export['group_title_wrapper|node|page|full'] = $field_group;

  return $export;
}
