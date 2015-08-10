<?php
/**
 * @file
 * secondarySector plugin.
 */

$plugin = array(
  'title' => t('Secondary Sector'),
  'description' => t(''),

  'property name' => 'secondary_sector',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Secondary Sector',
  'report filter key' => 'Secondary Sector Id',

  'report filter endpoint' => 'filters/sectors',
  'report sub-filter type' => 'Secondary',

  'handler' => 'secondarySectorMapper',
);

class secondarySectorMapper extends activityMapperSubFilterAutocomplete {
}
