<?php
/**
 * @file
 * actualCommitments plugin.
 */

$plugin = array(
  'title' => t('Triangular SSC Commitments'),
  'description' => t(''),

  'property name' => 'ssc_triangular_commitments',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field ssc' => TRUE,
  'report field key' => 'Triangular SSC Commitments',

  'handler' => 'sscTriangularCommitments',
);

class sscTriangularCommitments extends activityMapperBase {
}
