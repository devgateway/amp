<?php
/**
 * @file
 * originalCompletionDate plugin.
 */

$plugin = array(
  'title' => t('Original Completion Date'),
  'description' => t(''),

  'property name' => 'original_completion_date',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Original Completion Date',

  'handler' => 'originalCompletionDateMapper',
);

class originalCompletionDateMapper extends activityMapperBase {
}
