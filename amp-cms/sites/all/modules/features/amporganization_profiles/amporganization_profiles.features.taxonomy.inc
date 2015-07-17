<?php
/**
 * @file
 * amporganization_profiles.features.taxonomy.inc
 */

/**
 * Implements hook_taxonomy_default_vocabularies().
 */
function amporganization_profiles_taxonomy_default_vocabularies() {
  return array(
    'organization_groups' => array(
      'name' => 'Organization Groups',
      'machine_name' => 'organization_groups',
      'description' => 'Organization Groups are used to group organizations on listing pages.',
      'hierarchy' => 0,
      'module' => 'taxonomy',
      'weight' => 0,
    ),
  );
}
