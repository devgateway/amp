<?php
/**
 * @file
 * actualCompletionDate plugin.
 */

$plugin = array(
  'title' => t('Actual Completion Date'),
  'description' => t(''),

  'property name' => 'actual_completion_date',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Actual Completion Date',

  'handler' => 'actualCompletionDateMapper',
);

class actualCompletionDateMapper extends activityMapperBase {
  public static function getValue($plugin, $column) {
    if ($column->value == '#null') {
      return NULL;
    }

    return parent::getValue($plugin, $column);
  }
}
