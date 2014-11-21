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

  'handler' => 'donorAgencyMapper',
);

class donorAgencyMapper extends activityMapperRoleAutocomplete {
}
