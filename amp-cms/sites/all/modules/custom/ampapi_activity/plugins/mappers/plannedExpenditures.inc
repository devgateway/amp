<?php
/**
 * @file
 * plannedExpenditures plugin.
 */

$plugin = array(
  'title' => t('Planned Expenditures'),
  'description' => t(''),

  'property name' => 'planned_expenditures',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field key' => 'Planned Expenditures',

  'handler' => 'plannedExpendituresMapper',
);

class plannedExpendituresMapper extends activityMapperBase {
}
