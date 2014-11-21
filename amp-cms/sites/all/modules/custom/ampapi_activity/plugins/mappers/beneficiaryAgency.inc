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
  'report filter key' => 'Beneficiary Agency Id',

  'report filter endpoint' => 'filters/orgs',
  'report filter endpoint extra' => 'filters/org-roles',

  'handler' => 'beneficiaryAgencyMapper',
);

class beneficiaryAgencyMapper extends activityMapperRoleAutocomplete {
}
