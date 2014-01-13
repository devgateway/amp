<?php
/**
 * @file
 * ampp_general.features.taxonomy.inc
 */

/**
 * Implements hook_taxonomy_default_vocabularies().
 */
function ampp_general_taxonomy_default_vocabularies() {
  return array(
    'institutions' => array(
      'name' => 'Institutions',
      'machine_name' => 'institutions',
      'description' => 'Institutions',
      'hierarchy' => '1',
      'module' => 'taxonomy',
      'weight' => '0',
    ),
  );
}
