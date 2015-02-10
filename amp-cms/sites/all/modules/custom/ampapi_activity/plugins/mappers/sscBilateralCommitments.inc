<?php
/**
 * @file
 * actualCommitments plugin.
 */

$plugin = array(
  'title' => t('Bilateral SSC Commitments'),
  'description' => t(''),

  'property name' => 'ssc_bilateral_commitments',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field ssc' => TRUE,
  'report field key' => 'Bilateral SSC Commitments',

  'handler' => 'sscBilateralCommitmentsMapper',
);

class sscBilateralCommitmentsMapper extends activityMapperBase {
}
