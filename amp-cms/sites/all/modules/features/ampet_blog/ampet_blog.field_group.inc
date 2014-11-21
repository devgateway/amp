<?php
/**
 * @file
 * ampet_blog.field_group.inc
 */

/**
 * Implements hook_field_group_info().
 */
function ampet_blog_field_group_info() {
  $export = array();

  $field_group = new stdClass();
  $field_group->disabled = FALSE; /* Edit this to true to make a default field_group disabled initially */
  $field_group->api_version = 1;
  $field_group->identifier = 'group_resources|node|blog|form';
  $field_group->group_name = 'group_resources';
  $field_group->entity_type = 'node';
  $field_group->bundle = 'blog';
  $field_group->mode = 'form';
  $field_group->parent_name = '';
  $field_group->data = array(
    'label' => 'Resources',
    'weight' => '5',
    'children' => array(
      0 => 'field_blog_image',
    ),
    'format_type' => 'tab',
    'format_settings' => array(
      'label' => 'Resources',
      'instance_settings' => array(
        'required_fields' => 1,
        'classes' => '',
        'description' => '',
      ),
      'formatter' => 'closed',
    ),
  );
  $export['group_resources|node|blog|form'] = $field_group;

  return $export;
}
