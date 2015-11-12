<?php
/**
 * @file
 * donorAgency plugin.
 */

$plugin = array(
  'title' => t('Donor Agency'),
  'description' => t(''),

  'property name' => 'donor_agency',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Donor Agency',
  // 'report filter key' => 'Donor Agency Id',
  'report filter key' => 'Donor Id', // No need for consistency.

  'report filter endpoint' => 'filters/orgs',
  'report filter endpoint extra' => 'filters/org-roles',

  // These are used to filter out entries:
  // When filterId is available, we'll tr 'report sub-filter filter-id'.
  'report sub-filter filter-id' => 'Donor',
  'handler' => 'donorAgencyMapper',
);

class donorAgencyMapper extends activityMapperSubFilterAutocomplete {
}
