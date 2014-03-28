<?php

/**
 * Can be used to show Organizations that have a specific Role.
 */
class amp_activity_handler_field_organisation_names extends amp_activity_handler_field_translation_filtered {
  /**
   * Gets the AMP translations for the current field.
   */
  function get_amp_translations($items_list) {
    // Translate the Role Codes to Role IDs.
    $metadata_value_ids = $this->_get_metadata_value_ids();

    // Build the query and change the active database.
    db_set_active(AMP_DATABASE);
    $query = db_select('amp_org_role', 'amp_org_role');
    // Add the ID fields.
    $query->addField('amp_org_role', 'activity', 'amp_activity_id');
    $query->addField('amp_org_role', 'organisation', 'amp_org_id');

    // Add the translation field.
    $query->innerJoin('amp_organisation', 'amp_organisation', 'amp_organisation.amp_org_id = amp_org_role.organisation');
    $i18n_info = amp_i18n_get_translate_expression($this->amp_entity_type, NULL, 'amp_organisation', 'amp_org_id');
    $query->addExpression($i18n_info['expression'], 'name', $i18n_info['args']);

    // Filter by Role ID.
    if (count($metadata_value_ids) > 1) {
      $metadata_operator = ($this->metadata_operator == 'in') ? 'IN' : 'NOT IN';
      $query->condition('amp_org_role.role', $metadata_value_ids, $metadata_operator);
    }
    else {
      if (count($metadata_value_ids) == 1) {
        $metadata_operator = ($this->metadata_operator == 'in') ? '=' : '!=';
        $query->condition('amp_org_role.role', reset($metadata_value_ids), $metadata_operator);
      }
    }

    // Filter the results.
    $query->condition('amp_org_role.activity', $items_list, 'IN');

    // Run the query and reset the active database.
    $result = $query->execute();
    db_set_active();

    $translations = array();
    foreach ($result as $record) {
      $translations[$record->amp_activity_id][$record->amp_org_id] = $record->name;
    }

    return $translations;
  }
}
