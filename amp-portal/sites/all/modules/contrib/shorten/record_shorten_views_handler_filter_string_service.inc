<?php

/**
 * @file
 *   Allows choosing the Shorten URLs service.
 */

/**
 * Sets up a form for choosing the Shorten URLs service.
 */
class record_shorten_views_handler_filter_string_service extends views_handler_filter_many_to_one {
  function get_value_options() {
    $this->value_options = drupal_map_assoc(array_keys(module_invoke_all('shorten_service')));
  }
}
