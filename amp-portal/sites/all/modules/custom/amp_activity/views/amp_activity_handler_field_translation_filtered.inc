<?php

/**
 * Just adds an extra 'metadata' field settings option for translatable fields
 * which can be used for fields like: organisations filtered by role, sectors
 * filtered by classification.
 */
class amp_activity_handler_field_translation_filtered extends amp_activity_handler_field_translation {
  var $value_form_type = 'checkboxes';

  function init(&$view, &$options) {
    parent::init($view, $options);

    $this->metadata_operator = $this->options['metadata_operator'];
    $this->metadata_value = $this->options['metadata_value'];
  }

  function option_definition() {
    $options = parent::option_definition();

    $options['metadata_operator']['default'] = 'in';
    $options['metadata_value']['default'] = array();

    return $options;
  }

  /**
   * Add a form element to select the AMP Role for this argument.
   */
  function options_form(&$form, &$form_state) {
    ctools_include('dependent');

    $form['clear_markup_start'] = array(
      '#markup' => '<div class="clearfix">',
    );

    $options = $this->metadata_operator_options();

    $form['metadata_operator'] = array(
      '#type' => count($options) < 10 ? 'radios' : 'select',
      '#title' => t('Operator'),
      '#default_value' => $this->metadata_operator,
      '#options' => $options,
      '#prefix' => '<div class="views-group-box views-left-30">',
      '#suffix' => '</div>',
    );

    $this->get_metadata_value_options();
    $options = $this->metadata_value_options;
    $default_value = (array) $this->metadata_value;

    $form['metadata_value'] = array(
      '#type' => 'checkboxes',
      '#title' => t($this->definition['options label']),
      '#options' => $options,
      '#default_value' => $default_value,
      // These are only valid for 'select' type, but do no harm to checkboxes.
      '#multiple' => TRUE,
      '#size' => count($options) > 8 ? 8 : count($options),
      '#prefix' => '<div class="views-group-box views-right-70"><div id="edit-options-value-wrapper">',
      '#suffix' => '</div></div>',
      '#dependency' => array(
        'radio:options[metadata_operator]' => array('in', 'not in'),
      ),
    );

    $form['clear_markup_end'] = array(
      '#markup' => '</div>',
    );

    parent::options_form($form, $form_state);
  }

  /**
   * Simple submit handler
   */
  function options_submit(&$form, &$form_state) {
    $options = parent::options_submit();

    // Drupal's FAPI system automatically puts '0' in for any checkbox that
    // was not set, and the key to the checkbox if it is set.
    // Unfortunately, this means that if the key to that checkbox is 0,
    // we are unable to tell if that checkbox was set or not.

    // Luckily, the '#value' on the checkboxes form actually contains
    // *only* a list of checkboxes that were set, and we can use that
    // instead.

    $form_state['values']['options']['metadata_value'] = $form['metadata_value']['#value'];
  }

  function metadata_operators() {
    $operators = array(
      'in' => array(
        'title' => t('Is one of'),
        'short' => t('in'),
        'short_single' => t('='),
        'values' => 1,
      ),
      'not in' => array(
        'title' => t('Is not one of'),
        'short' => t('not in'),
        'short_single' => t('<>'),
        'values' => 1,
      ),
      'any' => array(
        'title' => t('Is any value'),
        'short' => t('any'),
        'values' => 1,
      ),
    );

    return $operators;
  }

  /**
   * Build strings from the operators() for 'select' options
   */
  function metadata_operator_options($which = 'title') {
    $options = array();
    foreach ($this->metadata_operators() as $id => $info) {
      $options[$id] = $info[$which];
    }

    return $options;
  }

  function admin_summary() {
    $info = $this->metadata_operators();

    $this->get_metadata_value_options();

    if (!is_array($this->metadata_value)) {
      return;
    }

    $operator = check_plain($info[$this->metadata_operator]['short']);
    $values = '';

    // Remove every element which is not known.
    foreach ($this->metadata_value as $value) {
      if (!isset($this->metadata_value_options[$value])) {
        unset($this->metadata_value[$value]);
      }
    }
    // Choose different kind of ouput for 0, a single and multiple values.
    if (count($this->metadata_value) == 0 && $operator != 'any') {
      $values = t('Unknown');
    }
    else {
      if (count($this->metadata_value) == 1) {
        // If any, use the 'single' short name of the operator instead.
        if (isset($info[$this->metadata_operator]['short_single'])) {
          $operator = check_plain($info[$this->metadata_operator]['short_single']);
        }

        $keys = $this->metadata_value;
        $value = array_shift($keys);
        $values = check_plain($this->metadata_value_options[$value]);
      }
      else {
        foreach ($this->metadata_value as $value) {
          if ($values !== '') {
            $values .= ', ';
          }
          if (drupal_strlen($values) > 8) {
            $values .= '...';
            break;
          }
          $values .= check_plain($this->metadata_value_options[$value]);
        }
      }
    }

    return $operator . (($values !== '') ? ' ' . $values : '');
  }

  /**
   * Child classes should be used to override this function and set the
   * 'value options', unless 'options callback' is defined as a valid function
   * or static public method to generate these values.
   *
   * This can use a guard to be used to reduce database hits as much as
   * possible.
   */
  function get_metadata_value_options() {
    if (isset($this->metadata_value_options)) {
      return;
    }

    if (isset($this->definition['options callback']) && is_callable($this->definition['options callback'])) {
      if (isset($this->definition['options arguments']) && is_array($this->definition['options arguments'])) {
        $this->metadata_value_options = call_user_func_array($this->definition['options callback'], $this->definition['options arguments']);
      }
      else {
        $this->metadata_value_options = call_user_func($this->definition['options callback']);
      }
    }
  }

  /**
   * Provides a unique placeholders for the metadata_code field.
   */
  function _get_metadata_value_ids() {
    $items = array();
    if (empty($this->metadata_value)) {
      return $items;
    }

    // Get a list of roles keyed by metadata_code.
    $metadata_info = $this->definition['options callback']();

    foreach ($this->metadata_value as $metadata_code) {
      if (!empty($metadata_info[$metadata_code])) {
        $items[] = $metadata_info[$metadata_code];
      }
    }

    return $items;
  }
}
