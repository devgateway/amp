<?php
/**
 * @file
 * actualApprovalDate plugin.
 */

$plugin = array(
  'title' => t('Actual Approval Date'),
  'description' => t(''),

  'property name' => 'actual_approval_date',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Actual Approval Date',

  'handler' => 'actualApprovalDateMapper',
);

class actualApprovalDateMapper extends activityMapperBase {
}
