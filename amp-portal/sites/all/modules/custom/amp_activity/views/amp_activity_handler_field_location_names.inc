<?php

/**
 * AMP specific field handler that gets a dbentity translated name by it's id.
 */
class amp_activity_handler_field_location_names extends amp_activity_handler_field_translation {
  /**
   * Gets the AMP translations for the current field.
   */
  function get_amp_translations($items_list) {
    // Build the query and change the active database.
    db_set_active(AMP_DATABASE);
    $query = db_select('amp_activity_location', 'amp_activity_location');

    // The location is stored in amp_category_value_location and wee need to
    // join several tables to get the id+translation from the amp_activity_id.
    $query->leftJoin('amp_location', 'amp_location', 'amp_location.amp_location_id = amp_activity_location.amp_location_id');
    $query->leftJoin('amp_category_value_location', 'amp_category_value_location', 'amp_category_value_location.id = amp_location.location_id');

    // Add the ID fields.
    $query->addField('amp_activity_location', 'amp_activity_id', 'amp_activity_id');
    $query->addField('amp_category_value_location', 'id', 'activity_location_id');

    // Add the translation field.
    $i18n_info = amp_i18n_get_translate_expression($this->amp_entity_type, NULL, 'amp_category_value_location', 'id');
    $query->addExpression($i18n_info['expression'], 'name', $i18n_info['args']);

    // Filter the results.
    $query->condition('amp_activity_location.amp_activity_id', $items_list, 'IN');

    // Run the query and reset the active database.
    $result = $query->execute();
    db_set_active();

    $translations = array();
    foreach ($result as $record) {
      $translations[$record->amp_activity_id][$record->activity_location_id] = $record->name;
    }

    return $translations;
  }
}
