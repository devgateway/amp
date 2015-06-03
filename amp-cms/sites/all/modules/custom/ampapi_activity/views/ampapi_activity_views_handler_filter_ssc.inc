<?php

class ampapi_activity_views_handler_filter_ssc extends views_handler_filter_in_operator {
  function init(&$view, &$options) {
    parent::init($view, $options);

    // Hide the exposed form element when SSC functionality is disabled.
    // It makes no sense to override is_exposed() because it is almost never used.
    if (!ampapi_activity_ssc_status()) {
      $this->options['exposed'] = FALSE;
    }
  }

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

  /**
   * Add this filter to the query.
   */
  function query() {
    if (in_array('ssc', $this->value)) {
      $this->query->query->entityCondition($this->field, TRUE);
    }
  }
}
