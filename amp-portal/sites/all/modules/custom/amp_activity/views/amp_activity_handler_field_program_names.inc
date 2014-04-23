<?php

/**
 * AMP specific field handler that gets a dbentity translated name by it's id.
 */
class amp_activity_handler_field_program_names extends amp_activity_handler_field_translation {
  /**
   * Gets the AMP translations for the current field.
   */
  function get_amp_translations($items_list) {
    // Build the query and change the active database.
    db_set_active(AMP_DATABASE);
    $query = db_select('amp_activity_program', 'amp_activity_program');
    // Add the ID fields.
    $query->addField('amp_activity_program', 'amp_activity_id', 'amp_activity_id');
    $query->addField('amp_activity_program', 'amp_program_id', 'amp_program_id');

    // Add the translation field.
    $i18n_info = amp_i18n_get_translate_expression($this->amp_entity_type, NULL, 'amp_activity_program', 'amp_program_id');
    $query->addExpression($i18n_info['expression'], 'name', $i18n_info['args']);

    // Filter the results.
    $query->condition('amp_activity_program.amp_activity_id', $items_list, 'IN');

    // Run the query and reset the active database.
    $result = $query->execute();
    db_set_active();

    $translations = array();
    foreach ($result as $record) {
      $translations[$record->amp_activity_id][$record->amp_program_id] = $record->name;
    }

    return $translations;
  }
}
