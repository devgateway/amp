<?php

/**
 * Custom filter handler for views can be used to dinamically change the real field.
 *
 * @TODO: Remove $_SESSION dependency!
 */
class amp_activity_handler_argument_period extends amp_activity_handler_argument_date {
  var $amp_real_field = NULL;

  /**
   * Construct a new field handler.
   */
  function construct() {
    parent::construct();

    if (!empty($this->definition['amp real field'])) {
      $this->amp_real_field = $this->definition['amp real field'];
    }
    else {
      drupal_set_message(t('Field definition is incomplete, "amp real field" is missing'), 'error');
    }
  }

  /**
   * Change the real field.
   */
  function init(&$view, &$options) {
    parent::init($view, $options);

    // Get the 'projects_search_result' pane configuration form session.
    if (!isset($_SESSION['projects_search_result_conf'])) {
      return;
    }
    $pane_conf = unserialize($_SESSION['projects_search_result_conf']);

    if (isset($pane_conf[$this->amp_real_field])) {
      $this->real_field = $pane_conf[$this->amp_real_field];
    }
  }
}
