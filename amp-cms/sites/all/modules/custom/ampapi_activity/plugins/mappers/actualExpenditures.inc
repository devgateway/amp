<?php
/**
 * @file
 * actualExpenditures plugin.
 */

$plugin = array(
  'title' => t('Actual Expenditures'),
  'description' => t(''),

  'property name' => 'actual_expenditures',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'measures',
  'report field type' => 'currency',
  'report field key' => 'Actual Expenditures',

  'handler' => 'actualExpendituresMapper',
);

class actualExpendituresMapper extends activityMapperBase {
}
