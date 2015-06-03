<?php
/**
 * @file
 * plannedDisbursements plugin.
 */

$plugin = array(
  'title' => t('Planned Disbursements'),
  'description' => t(''),

  'property name' => 'planned_disbursements',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field key' => 'Planned Disbursements',

  'handler' => 'plannedDisbursementsMapper',
);

class plannedDisbursementsMapper extends activityMapperBase {
}
