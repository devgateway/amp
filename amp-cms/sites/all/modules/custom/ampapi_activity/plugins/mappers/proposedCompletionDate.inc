<?php
/**
 * @file
 * proposedCompletionDate plugin.
 */

$plugin = array(
  'title' => t('Proposed Completion Date'),
  'description' => t(''),

  'property name' => 'proposed_completion_date',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Proposed Completion Date',

  'handler' => 'proposedCompletionDateMapper',
);

class proposedCompletionDateMapper extends activityMapperBase {
}
