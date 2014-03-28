<?php

/**
 * Drupal likes linux timestamps for dates.
 */
class amp_activity_handler_field_date_convert extends views_handler_field_date {
  /**
   * Get the value that's supposed to be rendered and convert to linux timestamp.
   */
  function get_value($values, $field = NULL) {
    $alias = isset($field) ? $this->aliases[$field] : $this->field_alias;

    if (isset($values->{$alias})) {
      $timestamp = strtotime($values->{$alias});
      if ($timestamp) {
        return $timestamp;
      }

      return 0;
    }
  }
}
