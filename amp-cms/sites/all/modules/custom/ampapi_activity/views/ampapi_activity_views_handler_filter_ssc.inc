<?php

class ampapi_activity_views_handler_filter_ssc extends views_handler_filter_in_operator {
  /**
   * Change available options.
   */
  function get_value_options() {
    $this->value_options = array(
      'standard' => t('Standard Activities'),
      'ssc' => t('South-South Cooperation Activities'),
    );

    return $this->value_options;
  }

  function query() {
    if (in_array('ssc', $this->value)) {
      $this->query->query->entityCondition($this->field, TRUE);
    }
  }

  /**
   * Render our chunk of the exposed filter form when selecting
   *
   * You can override this if it doesn't do what you expect.
   */
  function exposed_form(&$form, &$form_state) {
    parent::exposed_form($form, $form_state);
    if (!variable_get('helpergeneric_amp_ssc_status', FALSE) && !empty($this->options['expose']['identifier'])) {
      $identifier = $this->options['expose']['identifier'];
      $form[$identifier]['#access'] = FALSE;
    }
  }
}
