<?php
/**
 * @file
 * actualDisbursements plugin.
 */

$plugin = array(
  'title' => t('Actual Disbursements'),
  'description' => t(''),

  'property name' => 'actual_disbursements',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field key' => 'Actual Disbursements',

  'handler' => 'actualDisbursementsMapper',
);

class actualDisbursementsMapper extends activityMapperBase {
}
