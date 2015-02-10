<?php
/**
 * @file
 * actualCommitments plugin.
 */

$plugin = array(
  'title' => t('Bilateral SSC Disbursements'),
  'description' => t(''),

  'property name' => 'ssc_bilateral_disbursements',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field ssc' => TRUE,
  'report field key' => 'Bilateral SSC Disbursements',

  'handler' => 'sscBilateralDisbursementsMapper',
);

class sscBilateralDisbursementsMapper extends activityMapperBase {
}
