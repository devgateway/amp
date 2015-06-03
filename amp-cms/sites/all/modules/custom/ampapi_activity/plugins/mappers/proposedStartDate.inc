<?php
/**
 * @file
 * proposedStartDate plugin.
 */

$plugin = array(
  'title' => t('Proposed Start Date'),
  'description' => t(''),

  'property name' => 'proposed_start_date',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Proposed Start Date',

  'handler' => 'proposedStartDateMapper',
);

class proposedStartDateMapper extends activityMapperBase {
}
