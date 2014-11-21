<?php
/**
 * @file
 * organizationRoles plugin.
 */

$plugin = array(
  'title' => t('Organization Roles'),
  'description' => t(''),

  'property name' => 'organization_roles',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Organization Roles',

  'handler' => 'organizationRolesMapper',
);

class organizationRolesMapper extends activityMapperBase {
}
