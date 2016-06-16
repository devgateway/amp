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
  // Changed in 2.12.
  // @TODO: Make things simpler.
  'report filter key' => 'Secondary Sector',

  'report filter endpoint' => 'filters/sectors',
  // These are used to filter out entries:
  // When filterId is available, we'll try 'report sub-filter filter-id'.
  'report sub-filter filter-id' => 'Secondary Sector',

  'handler' => 'secondarySectorMapper',
);

class secondarySectorMapper extends activityMapperSubFilterAutocomplete {
}
