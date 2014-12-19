<?php
/**
 * @file
 * ampcontact.custom.inc
 */

/**
 * An entity-bound allowed values callback.
 */
function ampcontact_organization_values_callback($field, $instance, $entity_type, $entity, &$cacheable) {
  $cacheable = FALSE;

  $organizations = variable_get('amp_contact_organizations', array());
  if (empty($organizations)) {
    return array();
  }

  return drupal_map_assoc($organizations);
}
