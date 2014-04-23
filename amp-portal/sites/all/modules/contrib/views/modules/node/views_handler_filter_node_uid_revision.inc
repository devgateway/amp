<?php
// $Id: views_handler_filter_node_uid_revision.inc,v 1.1 2011/12/23 05:58:49 vamirbekyan Exp $
/**
 * @file
 * Filter handler to check for revisions a certain user has created.
 */
class views_handler_filter_node_uid_revision extends views_handler_filter_user_name {
  function query($group_by = FALSE) {
    $this->ensure_my_table();

    $placeholder = $this->placeholder();

    $args = array_values($this->value);

    $this->query->add_where_expression($this->options['group'], "$this->table_alias.uid IN($placeholder) " . $condition . " OR
      ((SELECT COUNT(*) FROM {node_revision} nr WHERE nr.uid IN($placeholder) AND nr.nid = $this->table_alias.nid) > 0)", array($placeholder => $args),
      $args);
  }
}
