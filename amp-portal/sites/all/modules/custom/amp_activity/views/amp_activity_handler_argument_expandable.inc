<?php

/**
 * Filters activities by expandable IDs.
 */
class amp_activity_handler_argument_expandable extends views_handler_argument_numeric {
  /**
   * Set up the query for this argument.
   */
  function query($group_by = FALSE) {
    if (!empty($this->options['break_phrase'])) {
      views_break_phrase($this->argument, $this);
    }
    else {
      $this->value = array($this->argument);
    }

    if (count($this->value) == 0) {
      return;
    }

    $this->ensure_my_table();

    // Expand the provided IDs for the final query.
    $this->expand_argument_ids();

    $placeholder = $this->placeholder();
    $null_check = empty($this->options['not']) ? '' : "OR $this->table_alias.$this->real_field IS NULL";

    if (count($this->value) > 1) {
      $operator = empty($this->options['not']) ? 'IN' : 'NOT IN';
      $this->query->add_where_expression(0, "$this->table_alias.$this->real_field $operator ($placeholder) $null_check", array($placeholder => $this->value));
    }
    else {
      $operator = empty($this->options['not']) ? '=' : '!=';
      $this->query->add_where_expression(0, "$this->table_alias.$this->real_field $operator $placeholder $null_check", array($placeholder => $this->argument));
    }
  }

  /**
   * Expand the argument IDs for the final query.
   */
  private function expand_argument_ids() {
    $callback = $this->definition['expand callback'];

    foreach ($this->value as $id) {
      $extra_ids = $callback($id);
      $this->value = array_merge($this->value, $extra_ids);
    }
  }
}
