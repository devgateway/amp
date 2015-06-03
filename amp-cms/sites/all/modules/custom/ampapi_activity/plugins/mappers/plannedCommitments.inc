<?php
/**
 * @file
 * plannedCommitments plugin.
 */

$plugin = array(
  'title' => t('Planned Commitments'),
  'description' => t(''),

  'property name' => 'planned_commitments',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field key' => 'Planned Commitments',

  'handler' => 'plannedCommitmentsMapper',
);

class plannedCommitmentsMapper extends activityMapperBase {
}
