<?php
/**
 * @file
 * primarySector plugin.
 */

$plugin = array(
  'title' => t('Primary Sector'),
  'description' => t(''),

  'property name' => 'primary_sector',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Primary Sector',
  'report filter key' => 'Primary Sector Id',

  'report filter endpoint' => 'filters/sectors',
  'report sub-filter type' => 'Primary',

  'handler' => 'primarySectorMapper',
);

class primarySectorMapper extends activityMapperSubFilterAutocomplete {
}
