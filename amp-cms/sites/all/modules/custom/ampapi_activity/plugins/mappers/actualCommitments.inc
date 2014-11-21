<?php
/**
 * @file
 * actualCommitments plugin.
 */

$plugin = array(
  'title' => t('Actual Commitments'),
  'description' => t(''),

  'property name' => 'actual_commitments',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field key' => 'Actual Commitments',

  'handler' => 'actualCommitmentsMapper',
);

class actualCommitmentsMapper extends activityMapperBase {
}
