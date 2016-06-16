<?php
/**
 * @file
 * beneficiaryAgency plugin.
 */

$plugin = array(
  'title' => t('Beneficiary Agency'),
  'description' => t(''),

  'property name' => 'beneficiary_agency',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Beneficiary Agency',
  // Changed in 2.12.
  // @TODO: Make things simpler.
  'report filter key' => 'Beneficiary Agency',

  'report filter endpoint' => 'filters/orgs',
  'report filter endpoint extra' => 'filters/org-roles',

  // These are used to filter out entries:
  // When filterId is available, we'll try 'report sub-filter filter-id'.
  'report sub-filter filter-id' => 'Beneficiary Agency',

  'handler' => 'beneficiaryAgencyMapper',
);

class beneficiaryAgencyMapper extends activityMapperSubFilterAutocomplete {
}
