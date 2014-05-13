<?php

/**
 * Get Commitment and disbursment ammounts.
 */
class amp_activity_handler_field_transaction extends views_handler_field_numeric {
  var $amp_transaction_type = NULL;
  var $field_transaction_alias= NULL;

  /**
   * Construct a new field handler.
   */
  function construct() {
    parent::construct();

    if (isset($this->definition['amp transaction type'])) {
      $this->amp_transaction_type = $this->definition['amp transaction type'];
    }
    else {
      drupal_set_message(t('Field definition is incomplete, "amp transaction type" is missing'), 'error');
    }
  }

  /**
   * Add currency_code to the end of the label.
   */
  function label() {
    $suffix = '';

    $currency_code = amp_activity_get_base_currency();
    if (!empty($currency_code)) {
      $suffix = " ($currency_code)";
    }

    return parent::label() . $suffix;
  }

  /**
   * Get the field values before the field is rendered.
   *
   * @param $values
   *   An array of all objects returned from the query.
   */
  function pre_render(&$values) {
    if (empty($values)) {
      return;
    }

    if (!isset($this->amp_transaction_type)) {
      return;
    }

    // Get a list of activities which require transaction details.
    $activities_list = array();
    foreach ($values as $row) {
      if (isset($row->{$this->field_alias})) {
        $activities_list[$row->{$this->field_alias}] = $row->{$this->field_alias};
      }
    }

    if (empty($activities_list)) {
      return;
    }

    // Get the activity transaction details.
    $amounts_in_base_currency = $this->get_funding_amounts($activities_list);

    // Add transaction amounts for each row.
    foreach ($values as &$row) {
      $id = $row->{$this->field_alias};

      if (!isset($amounts_in_base_currency[$id])) {
        continue;
      }

      // The render function makes sure strings are sanitized.
      $row->{$this->field_transaction_alias} = $amounts_in_base_currency[$id];
    }
  }

  /**
   * Gets the AMP transaction for the current field.
   */
  function get_funding_amounts($activities_list) {
    // Only display 'Actual' commitments and disbursements.
    $adjustment_type_id = _amp_activity_get_adjustment_type_id(AMP_ADJUSTMENT_TYPE_ACTUAL);

    // If the view is filtered by Donors, only show values for the selected Donors.
    $donors_list = array();
    foreach ($this->view->argument as $argument_handler) {
      if (isset($argument_handler->role_value) && in_array('DN', $argument_handler->role_value)) {
        if (empty($argument_handler->argument)) {
          continue;
        }

        if ($argument_handler->argument != 'all' && !empty($argument_handler->value)) {
          $donors_list = $argument_handler->value;
        }
      }
    }

    return _amp_activity_get_activity_funding_details($this->amp_transaction_type, $adjustment_type_id, $activities_list, $donors_list);
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
    $alias = $this->field_transaction_alias;

    if (empty($this->field_transaction_alias)) {
      $alias = isset($field) ? $this->aliases[$field] : $this->field_alias;
    }

    if (isset($values->{$alias})) {
      return $values->{$alias};
    }
  }

  /**
   * Called to define the field_transaction_alias.
   */
  function query() {
    parent::query();

    if (!isset($this->amp_transaction_type)) {
      return;
    }

    // Setup the transaction field alias.
    $this->field_transaction_alias = '_transaction_type_' . $this->amp_transaction_type . '_' . $this->position;
  }
}
