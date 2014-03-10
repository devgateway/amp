<?php
class amp_projects_handler_argument_string extends views_handler_argument_string {

function init(&$view, &$options) {
    parent::init($view, $options);
  }

  function option_definition() {
    $options = parent::option_definition();
    $options['use_like'] = array('default' => FALSE);

    return $options;
  }

  function options_form(&$form, &$form_state) {
    parent::options_form($form, $form_state);
    $form['use_like'] = array(
      '#type' => 'checkbox',
      '#title' => t('Contains string'),
      '#description' => t('Uses sql like insted of equals.'),
      '#default_value' => $this->options['use_like'],
      '#fieldset' => 'more',
    );
  }

  /**
   * Build the summary query based on a string
   */
  function summary_query() {
    $this->placeholder_length = $this->placeholder();
    if (empty($this->definition['many to one'])) {
      $this->ensure_my_table();
    }
    else {
      $this->table_alias = $this->helper->summary_join();
    }

    if (empty($this->options['glossary'])) {
      // Add the field.
      $this->base_alias = $this->query->add_field($this->table_alias, $this->real_field);
      $this->query->set_count_field($this->table_alias, $this->real_field);
    }
    else {
      // Add the field.
      $formula = $this->get_formula();
      $params = array(
        'placeholders' => array(
          $this->placeholder_length => intval($this->options['limit']),
        ),
      );

      $this->base_alias = $this->query->add_field(NULL, $formula, $this->field . '_truncated', $params);
      $this->query->set_count_field(NULL, $formula, $this->field, $this->field . '_truncated');
    }

    $this->summary_name_field();
    return $this->summary_basics(FALSE);
  }

  /**
   * Get the formula for this argument.
   *
   * $this->ensure_my_table() MUST have been called prior to this.
   */
  function get_formula() {
    return "SUBSTRING($this->table_alias.$this->real_field, 1, $this->placeholder_length)";
  }

  /**
   * Build the query based upon the formula
   */
  function query($group_by = FALSE) {
    $argument = $this->argument;
    if (!empty($this->options['transform_dash'])) {
      $argument = strtr($argument, '-', ' ');
    }
    $this->placeholder_length = $this->placeholder();

    if (!empty($this->options['break_phrase'])) {
      views_break_phrase_string($argument, $this);
    }
    else {
      $this->value = array($argument);
      $this->operator = 'or';
    }

    if (!empty($this->definition['many to one'])) {
      if (!empty($this->options['glossary'])) {
        $this->helper->formula = TRUE;
      }
      $this->helper->ensure_my_table();

      $this->helper->placeholders = array($this->placeholder_length => intval($this->options['limit']));
      $this->helper->add_filter();
      return;
    }

    $this->ensure_my_table();
    $formula = FALSE;
    if (empty($this->options['glossary'])) {
      $field = "$this->table_alias.$this->real_field";
    }
    else {
      $formula = TRUE;
      $field = $this->get_formula();
    }

    if (count($this->value) > 1) {
      $operator = 'IN';
      $argument = $this->value;
    } else {
      $operator = '=';
    }

    if ($formula) {
      $placeholder = $this->placeholder();
      $placeholder_length = $this->placeholder_length;
      if ($operator == 'IN') {
        $field .= " IN($placeholder)";
      }
      else {
        $field .= ' = ' . $placeholder;
      }
      $placeholders = array(
        $placeholder_length => intval($this->options['limit']),
        $placeholder => $argument,
      );
      $this->query->add_where_expression(0, $field, $placeholders);
    }
    else {
      if($this->options['use_like']) {
        $this->query->add_where(0, $field, '%' . db_like($argument) . '%', 'ILIKE');
      } else {
          $this->query->add_where(0, $field, $argument, $operator);
      }
    }
  }

  function summary_argument($data) {
    $value = $this->case_transform($data->{$this->base_alias}, 'path_case');
    if (!empty($this->options['transform_dash'])) {
      $value = strtr($value, ' ', '-');
    }
    return $value;
  }

  function case_transform($string, $option) {
    global $multibyte;

    switch ($this->options[$option]) {
      default:
        return $string;
      case 'upper':
        return drupal_strtoupper($string);
      case 'lower':
        return drupal_strtolower($string);
      case 'ucfirst':
        return drupal_strtoupper(drupal_substr($string, 0, 1)) . drupal_substr($string, 1);
      case 'ucwords':
        if ($multibyte == UNICODE_MULTIBYTE) {
          return mb_convert_case($string, MB_CASE_TITLE);
        } else {
          return ucwords($string);
        }
    }
  }

  function get_sort_name() {
    return t('Alphabetical', array(), array('context' => 'Sort order'));
  }

  function title() {
    $this->argument = $this->case_transform($this->argument, 'case');
    if (!empty($this->options['transform_dash'])) {
      $this->argument = strtr($this->argument, '-', ' ');
    }

    if (!empty($this->options['break_phrase'])) {
      views_break_phrase($this->argument, $this);
    }
    else {
      $this->value = array($this->argument);
      $this->operator = 'or';
    }

    if (empty($this->value)) {
      return !empty($this->definition['empty field name']) ? $this->definition['empty field name'] : t('Uncategorized');
    }

    if ($this->value === array(-1)) {
      return !empty($this->definition['invalid input']) ? $this->definition['invalid input'] : t('Invalid input');
    }

    return implode($this->operator == 'or' ? ' + ' : ', ', $this->title_query());
  }

  /**
   * Override for specific title lookups.
   */
  function title_query() {
    return drupal_map_assoc($this->value, 'check_plain');
  }

  function summary_name($data) {
    return $this->case_transform(parent::summary_name($data), 'case');
  }
 }