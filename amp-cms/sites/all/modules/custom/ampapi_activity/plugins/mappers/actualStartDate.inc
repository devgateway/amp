<?php
/**
 * @file
 * actualStartDate plugin.
 */

$plugin = array(
  'title' => t('Actual Start Date'),
  'description' => t(''),

  'property name' => 'actual_start_date',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Actual Start Date',

  'handler' => 'actualStartDateMapper',
);

class actualStartDateMapper extends activityMapperBase {
  public static function getValue($plugin, $column) {
    if ($column->value == '#null') {
      return NULL;
    }

    return parent::getValue($plugin, $column);
  }
}
