<?php

/**
 * @file
 * Specialized relationship handler to add entityqueues.
 */

class entityqueue_handler_relationship_entityqueue extends views_handler_relationship_entity_reverse {

  function option_definition() {
    $options = parent::option_definition();

    $options['limit']['default'] = FALSE;
    $options['queues']['default'] = array();

    return $options;
  }

  /**
   * Default options form that provides the label widget that all fields
   * should have.
   */
  function options_form(&$form, &$form_state) {
    parent::options_form($form, $form_state);

    $form['limit'] = array(
      '#type' => 'checkbox',
      '#title' => t('Limit to one or more queues (recommended)'),
      '#default_value' => $this->options['limit'],
    );

    $views_data = views_fetch_data($this->table);
    $subqueues = entityqueue_subqueue_load_by_target_type($views_data['table']['entity type']);
    $form['queues'] = array(
      '#prefix' => '<div><div id="edit-options-queues">',
      '#suffix' => '</div></div>',
      '#type' => 'checkboxes',
      '#title' => t('Queues'),
      '#options' => entityqueue_get_options($subqueues),
      '#default_value' => $this->options['queues'],
      '#process' => array('form_process_checkboxes', 'ctools_dependent_process'),
      '#dependency' => array('edit-options-limit' => array(TRUE)),
    );
  }

  /**
   * Overrides views_handler_relationship_entity_reverse::query().
   *
   * Adds an extra condition to the second join from the parent query.
   */
  function query() {
    $this->ensure_my_table();
    // First, relate our base table to the current base table to the
    // field, using the base table's id field to the field's column.
    $views_data = views_fetch_data($this->table);
    $left_field = $views_data['table']['base']['field'];

    $first = array(
      'left_table' => $this->table_alias,
      'left_field' => $left_field,
      'table' => $this->definition['field table'],
      'field' => $this->definition['field field'],
    );
    if (!empty($this->options['required'])) {
      $first['type'] = 'INNER';
    }

    // Add an extra condition to limit results based on the queue selection.
    if (!empty($this->options['limit'])) {
      $queues = array_keys(array_filter($this->options['queues']));
      if (!empty($queues)) {
        $queue_names = db_select('entityqueue_subqueue', 'esq')
          ->fields('esq', array('queue'))
          ->distinct()
          ->condition('esq.name', $queues, 'IN')
          ->execute()
          ->fetchCol();
        $first['extra'] = array(array(
          'field' => 'bundle',
          'value' => $queue_names,
        ));
      }
    }

    if (!empty($this->definition['join_extra'])) {
      if (!isset($first['extra'])) {
        $first['extra'] = array();
      }
      $first['extra'] += $this->definition['join_extra'];
    }

    if (!empty($this->definition['join_handler']) && class_exists($this->definition['join_handler'])) {
      $first_join = new $this->definition['join_handler'];
    }
    else {
      $first_join = new views_join();
    }
    $first_join->definition = $first;
    $first_join->construct();
    $first_join->adjusted = TRUE;

    $this->first_alias = $this->query->add_table($this->definition['field table'], $this->relationship, $first_join);

    // Second, relate the field table to the entity specified using
    // the entity id on the field table and the entity's id field.
    $second = array(
      'left_table' => $this->first_alias,
      'left_field' => 'entity_id',
      'table' => $this->definition['base'],
      'field' => $this->definition['base field'],
    );

    if (!empty($this->options['required'])) {
      $second['type'] = 'INNER';
    }

    // Add an extra condition to limit results based on the queue selection.
    if (!empty($this->options['limit'])) {
      $queues = array_keys(array_filter($this->options['queues']));
      if (!empty($queues)) {
        $second['extra'] = array(array(
          'field' => 'name',
          'value' => $queues,
        ));
      }
    }

    if (!empty($this->definition['join_handler']) && class_exists($this->definition['join_handler'])) {
      $second_join = new $this->definition['join_handler'];
    }
    else {
      $second_join = new views_join();
    }
    $second_join->definition = $second;
    $second_join->construct();
    $second_join->adjusted = TRUE;

    // use a short alias for this:
    $alias = $this->definition['field_name'] . '_' . $this->table;

    $this->alias = $this->query->add_relationship($alias, $second_join, $this->definition['base'], $this->relationship);
  }
}
