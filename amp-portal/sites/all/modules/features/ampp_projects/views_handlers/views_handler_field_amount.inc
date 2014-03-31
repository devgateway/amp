<?php

/**
 *
 */
class views_handler_field_amount extends views_handler_field {
  /**
   * Add currency_code to the end of the label.
   */
  function label() {
    $suffix = '';

    $currency_code = variable_get('currency_code', '');
    if (!empty($currency_code)) {
      $suffix = " ($currency_code)";
    }

    return parent::label() . $suffix;
  }

  /**
   * Get totall amount of activity with given id of given type
   *
   * @param integer $amp_activity_id
   * @param integer $transaction_type
   */
  protected function get_amount($amp_activity_id, $donor_ids_array, $transaction_type) {
    /**
     * Some records in DB has no donor.
     * As amount is depend on donor, we return 0 for NULL donors
     */
    if (in_array('NULL', $donor_ids_array)) {
      foreach ($donor_ids_array as $key => $value) {
        if ($value == 'NULL') {
          unset($donor_ids_array[$key]);
        }
      }
    }
    if (empty($donor_ids_array)) {
      return 0;
    }

    db_set_active("amp_projects");
    $query = db_select('cached_amp_activity', 'activity');
    $query->addField('activity', 'amp_activity_id', 'act_id');
    $query->condition('activity.amp_activity_id', $amp_activity_id, "=")
      ->condition('fd.transaction_type', $transaction_type, "=")
      ->groupBy('activity.amp_activity_id')
      ->addExpression('SUM(fd.transaction_amount / getexchange(cur.currency_code, fd.transaction_date ))', 'transaction_amount');

    if (!empty($donor_ids_array) and count($donor_ids_array) == 1) {
      $query->condition('f.amp_donor_org_id', $donor_ids_array[0], "=");
    }
    else if (count($donor_ids_array) > 1) {
      $query->condition('f.amp_donor_org_id', $donor_ids_array, "IN");
    }
    $query->addJoin('LEFT', 'amp_funding', 'f', 'activity.amp_activity_id = f.amp_activity_id');
    $query->addJoin('LEFT', 'amp_funding_detail', 'fd', 'f.amp_funding_id = fd.amp_funding_id');
    $query->addJoin('LEFT', 'amp_currency', 'cur', 'fd.amp_currency_id = cur.amp_currency_id');

    $amp_adjustment_actual_query = db_select('amp_category_value', 'amp_category_value')
      ->fields('amp_category_value', array('id'))
      ->condition('category_value', 'Actual', '=');
    $query->condition('fd.adjustment_type', $amp_adjustment_actual_query, 'IN');

    $result = $query->execute()->fetchAssoc();
    db_set_active();

    if (module_exists('format_number')) {
      return format_number($result['transaction_amount'], 0);
    }
    else {
      return $result['transaction_amount'];
    }
  }
}
