<?php

/**
 * AMP specific field handler that gets a dbentity translated name by it's id.
 */
class amp_activity_handler_field_status extends views_handler_field {
  /**
   * Gets the AMP translations for the current field.
   *
   * @TODO: Get translated name from the AMP DB.
   */
  function get_amp_status_translations($items_list) {
    // Build the query and change the active database.
    db_set_active(AMP_DATABASE);
    $query = db_select('cached_v_status', 'cached_v_status');
    // Add the ID fields.
    $query->addField('cached_v_status', 'amp_activity_id', 'amp_activity_id');
    $query->addField('cached_v_status', 'amp_status_id', 'amp_status_id');
    $query->addField('cached_v_status', 'name', 'name');

    // Filter the results.
    $query->condition('cached_v_status.amp_activity_id', $items_list, 'IN');

    // Run the query and reset the active database.
    $result = $query->execute();
    db_set_active();

    $translations = array();
    foreach ($result as $record) {
      $translations[$record->amp_activity_id] = t($record->name);
    }

    return $translations;
  }

  /**
   * Get the value that's supposed to be rendered.
   *
   * @param $values
   *   An object containing all retrieved values.
   * @param $field
   *   Optional name of the field where the value is stored.
   */
  function get_value($values, $field = NULL) {
    $alias = $this->field_translation_alias;

    if (empty($this->field_translation_alias)) {
      return NULL;
    }

    if (isset($values->{$alias})) {
      return $values->{$alias};
    }
  }

  /**
   * Get the translated entity name before the field is rendered.
   *
   * This gives the handlers some time to set up before any handler has
   * been rendered.
   *
   * @param $values
   *   An array of all objects returned from the query.
   */
  function pre_render(&$values) {
    if (empty($values)) {
      return;
    }

    // Get a list of entities to translate.
    $items_list = array();
    foreach ($values as $row) {
      if (isset($row->{$this->field_alias})) {
        $items_list[$row->{$this->field_alias}] = $row->{$this->field_alias};
      }
    }

    if (empty($items_list)) {
      return;
    }

    // Get the entity translations.
    $translations = $this->get_amp_status_translations($items_list);

    // Add translation for each row.
    foreach ($values as $id => &$row) {
      $id = $row->{$this->field_alias};

      if (!isset($translations[$id])) {
        continue;
      }

      // The render function makes sure strings are sanitized.
      $row->{$this->field_translation_alias} = $translations[$id];
    }
  }

  /**
   * Called to define the field_translation_alias required for click_sort.
   */
  function query() {
    parent::query();

    // Setup the translated field alias; field_alias is already a unique string.
    $this->field_translation_alias = $this->field_alias . '__status_' . $this->position;
  }
}
