<?php
/**
 * @file
 * actualCommitments plugin.
 */

$plugin = array(
  'title' => t('Triangular SSC Disbursements'),
  'description' => t(''),

  'property name' => 'ssc_triangular_disbursements',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field ssc' => TRUE,
  'report field key' => 'Triangular SSC Disbursements',

  'handler' => 'sscTriangularDisbursementsMapper',
);

class sscTriangularDisbursementsMapper extends activityMapperBase {
}
