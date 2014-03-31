<?php

/**
 *
 */
class views_handler_field_amount_disbursement extends views_handler_field_amount {

  function query() {
    $table_alias = $this->query->ensure_table('amp_funding');
    $this->field_alias = $this->query->add_field(NULL, "ARRAY_AGG($table_alias.amp_donor_org_id)", 'amp_activity_amount_disbursement');
  }

  function render($values) {
    $donor_ids = $values->amp_activity_amount_disbursement;

    $pos = strpos($donor_ids, '{');

    if ($pos !== FALSE) {
      $donor_ids_array = explode(",", substr($donor_ids, 1, strlen($donor_ids) - 2));
      $values->amp_activity_amount_disbursement = $this->get_amount($values->amp_activity_id, array_unique($donor_ids_array), AMP_TRANSACTION_TYPE_DISBURSEMENT);
    }

    return parent::render($values);
  }
}
