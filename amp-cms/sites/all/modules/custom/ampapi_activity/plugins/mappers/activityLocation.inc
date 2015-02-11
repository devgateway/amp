<?php
/**
 * @file
 * activityLocation plugin.
 */

$plugin = array(
  'title' => t('Location'),
  'description' => t(''),

  'property name' => 'location',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Location',

  'report filter endpoint' => 'filters/locations',

  'handler' => 'activityMapperAutocompleteBase',
);
