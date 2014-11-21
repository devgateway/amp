<?php

/**
 * @file
 * Sort handler for entityqueue position.
 */

/**
 * Sort handler for entityqueue position.
 *
 * @ingroup views_sort_handlers
 */
class entityqueue_handler_sort_position extends views_handler_sort {

  /**
   * Called to add the sort to a query.
   */
  function query() {
    $this->ensure_my_table();
    $field_table = $this->definition['field table'];
    $field_table_alias = FALSE;

    $tables = $this->query->table_queue;
    foreach ($tables as $info) {
      if ($info['table'] == $field_table) {
        $field_table_alias = $info['alias'];
        break;
      }
    }
    // Add the field.
    if ($field_table_alias) {
      $this->query->add_orderby($field_table_alias, $this->real_field, $this->options['order']);
    }
    else {
      drupal_set_message(t('In order to sort by the queue position, you need to add the Entityqueue: Queue relationship.'), 'error');
    }
  }
}
