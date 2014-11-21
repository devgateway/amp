<?php

/**
 * @file
 * Update Scripts helper functions for the core Menu module.
 */

/**
 * Delete field instances.
 */
function _us_fields__delete_instances($field_names, $entity_type, $bundle) {
  if (is_string($field_names)) {
    $field_names = array($field_names);
  }

  foreach ($field_names as $field_name) {
    $field = field_info_field($field_name);
    $instance = field_info_instance($entity_type, $field_name, $bundle);

    if (!empty($bundle) && $field && !$field['locked']) {
      field_delete_instance($instance);
    }

    // Call field_purge_batch() to remove the field from the system.
    field_purge_batch(1000);
  }
}
