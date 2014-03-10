<?php
/**
 *
 */
class views_handler_field_amount_commitment extends views_handler_field_amount {

  function query() {
    $this->field_alias = $this->query->add_field(null,'array_agg(amp_funding.amp_donor_org_id)',"amp_activity_amount_commitment");
  }

  function render($values){
    $donor_ids = $values->amp_activity_amount_commitment;

  $pos = strpos($donor_ids, "{");

  if($pos !== false)
  {
    $donor_ids_array = explode(",", substr($donor_ids, 1, strlen($donor_ids)-2));
    // Transaction types are:
        // Commitments = 0, Disbursements = 1, Expenditures = 2
        $transactino_type = 0;
    $values->amp_activity_amount_commitment = $this->get_amount($values->amp_activity_id, array_unique($donor_ids_array), 0);
  }
    return parent::render($values);
  }
}
