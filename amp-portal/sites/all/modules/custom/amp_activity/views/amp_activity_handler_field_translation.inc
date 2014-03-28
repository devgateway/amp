<?php

/**
 * AMP specific field handler that gets a dbentity translated name by it's id.
 *
 * @TODO: Add a proper click_sort method.
 *        Use the field configuration to render multiple values.
 */
class amp_activity_handler_field_translation extends views_handler_field {
  var $amp_entity_type = NULL;
  var $field_translation_alias = NULL;
  var $i18n_info = NULL;

  /**
   * Construct a new field handler.
   */
  function construct() {
    parent::construct();

    if (!empty($this->definition['amp entity type'])) {
      $this->amp_entity_type = $this->definition['amp entity type'];

      $this->i18n_info = _amp_i18n_translate_entity_map($this->amp_entity_type);
      if (empty($this->i18n_info)) {
        drupal_set_message(t('Field translation info is missing'), 'error');
      }
    }
    else {
      drupal_set_message(t('Field definition is incomplete, "amp entity type" is missing'), 'error');
    }
  }

  /**
   * For now disable click sorting.
   */
  function click_sortable() {
    return FALSE;
  }

  /**
   * Called to determine what to tell the clicksorter.
   */
  function click_sort($order) {
    // if (isset($this->field_translation_alias)) {
    //   $params = $this->options['group_type'] != 'group' ? array('function' => $this->options['group_type']) : array();

    //   $i18n_info = amp_i18n_get_translate_expression($this->amp_entity_type, NULL, FALSE, $this->field_alias);

    //   $params = drupal_array_merge_deep($params, $i18n_info['args']);
    // }
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

    if (empty($this->amp_entity_type) || empty($this->i18n_info)) {
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
    $translations = $this->get_amp_translations($items_list);

    // Add translation for each row.
    foreach ($values as &$row) {
      $id = $row->{$this->field_alias};

      if (!isset($translations[$id])) {
        continue;
      }

      // The render function makes sure strings are sanitized.
      $row->{$this->field_translation_alias} = $translations[$id];
    }
  }

  /**
   * Gets the AMP translations for the current field.
   */
  function get_amp_translations($items_list) {
    // Get the entity translations.
    $translations = amp_i18n_translate_entity($this->amp_entity_type, array_keys($items_list));

    return $translations;
  }

  /**
   * Render the field.
   */
  function render($values) {
    $output = '';

    $field_values = $this->get_value($values);
    if (is_array($field_values)) {

      $remaining = $total = count($field_values);
      foreach ($field_values as $value) {
        $remaining--;

        $output .= '<span>';
        $output .= $this->sanitize_value($value);
        $output .= '</span>';

        // Add a comma sepparator between values.
        if (!empty($remaining)) {
          $output .= ', <br>';
        }

        if ($total > 6) {
          if (($total - $remaining) == 5) {
            $output .= '<span class="more-locations" id="more-' . $values->amp_activity_id . '">' . t('more...') . '</span>';
            $output .= '<span class="more-locations" style="display: none" id="less-' . $values->amp_activity_id . '">' . t('less...') . '</span>';
            $output .= '<div class="hidden-locations" id="hidden-' . $values->amp_activity_id . '">';
          }
          if (empty($remaining)) {
            $output .= '</div>';
          }
        }
      }
    }
    else {
      $output = $this->sanitize_value($field_values);
    }

    return $output;
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
      $alias = isset($field) ? $this->aliases[$field] : $this->field_alias;
    }

    if (isset($values->{$alias})) {
      return $values->{$alias};
    }
  }

  /**
   * Called to define the field_translation_alias required for click_sort.
   */
  function query() {
    parent::query();

    if (empty($this->amp_entity_type) || empty($this->i18n_info)) {
      return;
    }

    // Setup the translated field alias; field_alias is already a unique string.
    $this->field_translation_alias = $this->field_alias . '__' . $this->amp_entity_type . '_' . $this->position;
  }
}
