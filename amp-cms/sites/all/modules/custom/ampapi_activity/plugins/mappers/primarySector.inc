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
  // Changed in 2.12.
  // @TODO: Make things simpler.
  'report filter key' => 'Primary Sector',

  'report filter endpoint' => 'filters/sectors',

  // These are used to filter out entries:
  // When filterId is available, we'll try 'report sub-filter filter-id'.
  'report sub-filter filter-id' => 'Primary Sector',

  'handler' => 'primarySectorMapper',
);

class primarySectorMapper extends activityMapperSubFilterAutocomplete {
}
