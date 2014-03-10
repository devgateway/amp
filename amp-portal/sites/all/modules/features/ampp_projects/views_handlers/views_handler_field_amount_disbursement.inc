<?php
/**
 *
 */
class views_handler_field_amount_disbursement extends views_handler_field_amount {

  function query() {
    $this->field_alias = $this->query->add_field(null, 'array_agg(amp_funding.amp_donor_org_id)' ,"amp_activity_amount_disbursement");
  }

  function render($values){
    $donor_ids = $values->amp_activity_amount_disbursement;

    $pos = strpos($donor_ids, "{");

  if($pos !== false)
  {
    $donor_ids_array = explode(",", substr($donor_ids, 1, strlen($donor_ids)-2));
    // Transaction types are:
    // Commitments = 0, Disbursements = 1, Expenditures = 2
    $transactino_type = 1;
    $values->amp_activity_amount_disbursement = $this->get_amount($values->amp_activity_id, array_unique($donor_ids_array), $transactino_type);
  }
    return parent::render($values);
  }
}
