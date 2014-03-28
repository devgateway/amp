<?php

/**
 *
 */
class views_handler_field_amount_commitment extends views_handler_field_amount {

  function query() {
    $table_alias = $this->query->ensure_table('amp_funding');
    $this->field_alias = $this->query->add_field(NULL, "ARRAY_AGG($table_alias.amp_donor_org_id)", 'amp_activity_amount_commitment');
  }

  function render($values) {
    $donor_ids = $values->amp_activity_amount_commitment;

    $pos = strpos($donor_ids, '{');

    if ($pos !== FALSE) {
      $donor_ids_array = explode(',', substr($donor_ids, 1, strlen($donor_ids) - 2));
      // Transaction types are:
      // Commitments = 0, Disbursements = 1, Expenditures = 2
      $transactino_type = 0;
      $values->amp_activity_amount_commitment = $this->get_amount($values->amp_activity_id, array_unique($donor_ids_array), $transactino_type);
    }

    return parent::render($values);
  }
}
