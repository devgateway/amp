<?php

/**
* Custom filter handler for views, that handles DATETIME
*/

class ampp_projects_date_views_argument_handler extends views_handler_argument {

  /*
   *
   */
  function option_definition() {
    $options = parent::option_definition();
    $options['condition_operator'] = array('default' => '=');
    return $options;
  }


  /*
   *
   */
  function operators() {
    $operators = array(
      '<' => array(
        'title' => t('Is less than'),
        'method' => 'op_simple',
        'short' => t('<'),
        'values' => 1,
      ),
      '<=' => array(
        'title' => t('Is less than or equal to'),
        'method' => 'op_simple',
        'short' => t('<='),
        'values' => 1,
      ),
      '=' => array(
        'title' => t('Is equal to'),
        'method' => 'op_simple',
        'short' => t('='),
        'values' => 1,
      ),
      '!=' => array(
        'title' => t('Is not equal to'),
        'method' => 'op_simple',
        'short' => t('!='),
        'values' => 1,
      ),
      '>=' => array(
        'title' => t('Is greater than or equal to'),
        'method' => 'op_simple',
        'short' => t('>='),
        'values' => 1,
      ),
      '>' => array(
        'title' => t('Is greater than'),
        'method' => 'op_simple',
        'short' => t('>'),
        'values' => 1,
      ),

    );

    return $operators;
  }


  /*
   *
   */
  function operator_options($which = 'title') {
    $options = array();
    foreach ($this->operators() as $id => $info) {
      $options[$id] = $info[$which];
    }
    return $options;
  }


  /*
   *
   */
  function options_form(&$form, &$form_state) {
    //parent::options_form($form, $form_state);

    $options = $this->operator_options();
    if (!empty($options)) {
      $form['condition_operator'] = array(
        '#type' => count($options) < 10 ? 'radios' : 'select',
        '#title' => t('Operator'),
        '#default_value' => $this->options['condition_operator'],
        '#options' => $options,
      );
    }
  }


  /*
   *
   */
  function operator_values($values = 1) {
    $options = array();
    foreach ($this->operators() as $id => $info) {
      if ($info['values'] == $values) {
        $options[] = $id;
      }
    }

    return $options;
  }


  /*
   *
   */
  function admin_summary() {
    if (!empty($this->options['exposed'])) {
      return t('exposed');
    }

    $options = $this->operator_options('short');
    $operator = $options[$this->options['condition_operator']];

    return t('@op argument', array('@op' => $operator));
  }


  /*
   *
   */
  function query($group_by = FALSE) {
    $this->ensure_my_table();
    $field = "$this->table_alias.$this->real_field";

    $info = $this->operators();
    if (!empty($info[$this->options['condition_operator']]['method'])) {
      $this->{$info[$this->options['condition_operator']]['method']}($field);
    }
  }


  /*
   *
   */
  function op_simple($field) {
    $this->value = array($this->argument);
    $group = 0;
    if (isset($this->options['group'])) $group = $this->options['group'];
    $this->query->add_where($group, $field, $this->value[0], $this->options['condition_operator']);
  }
}
