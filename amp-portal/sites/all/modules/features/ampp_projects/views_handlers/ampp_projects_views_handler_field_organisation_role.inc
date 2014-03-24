<?php

/**
 * Can be used to show Organizations that have a specific Role.
 */
class ampp_projects_views_handler_field_organisation_role extends views_handler_field {
  var $value_form_type = 'checkboxes';

  function init(&$view, &$options) {
    parent::init($view, $options);

    $this->role_operator = $this->options['role_operator'];
    $this->role_value = $this->options['role_value'];
  }

  function option_definition() {
    $options = parent::option_definition();

    $options['role_operator']['default'] = 'in';
    $options['role_value']['default'] = array();

    return $options;
  }

  /**
   * Add a form element to select the AMP Role for this argument.
   */
  function options_form(&$form, &$form_state) {
    $form['clear_markup_start'] = array(
      '#markup' => '<div class="clearfix">',
    );
    $this->show_role_operator_form($form, $form_state);
    $this->show_role_value_form($form, $form_state);
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

    $form_state['values']['options']['role_value'] = $form['role_value']['#value'];
  }

  /**
   * Shortcut to display the operator form.
   */
  function show_role_operator_form(&$form, &$form_state) {
    $options = $this->role_operator_options();

    $form['role_operator'] = array(
      '#type' => count($options) < 10 ? 'radios' : 'select',
      '#title' => t('Operator'),
      '#default_value' => $this->role_operator,
      '#options' => $options,
    );

    $form['role_operator']['#prefix'] = '<div class="views-group-box views-left-30">';
    $form['role_operator']['#suffix'] = '</div>';
  }

  function role_operators() {
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
    );

    return $operators;
  }

  /**
   * Build strings from the operators() for 'select' options
   */
  function role_operator_options($which = 'title') {
    $options = array();
    foreach ($this->role_operators() as $id => $info) {
      $options[$id] = $info[$which];
    }

    return $options;
  }

  function admin_summary() {
    $info = $this->role_operators();

    $this->get_role_value_options();

    if (!is_array($this->role_value)) {
      return;
    }

    $operator = check_plain($info[$this->role_operator]['short']);
    $values = '';

    // Remove every element which is not known.
    foreach ($this->role_value as $value) {
      if (!isset($this->role_value_options[$value])) {
        unset($this->role_value[$value]);
      }
    }
    // Choose different kind of ouput for 0, a single and multiple values.
    if (count($this->role_value) == 0) {
      $values = t('Unknown');
    }
    else if (count($this->role_value) == 1) {
      // If any, use the 'single' short name of the operator instead.
      if (isset($info[$this->role_operator]['short_single'])) {
        $operator = check_plain($info[$this->role_operator]['short_single']);
      }

      $keys = $this->role_value;
      $value = array_shift($keys);
      $values = check_plain($this->role_value_options[$value]);
    }
    else {
      foreach ($this->role_value as $value) {
        if ($values !== '') {
          $values .= ', ';
        }
        if (drupal_strlen($values) > 8) {
          $values .= '...';
          break;
        }
        $values .= check_plain($this->role_value_options[$value]);
      }
    }

    return $operator . (($values !== '') ? ' ' . $values : '');
  }

  /**
   * Shortcut to display the value form.
   */
  function show_role_value_form(&$form, &$form_state) {
    $this->get_role_value_options();
    $options = $this->role_value_options;
    $default_value = (array) $this->role_value;

    $form['role_value'] = array(
      '#type' => 'checkboxes',
      '#title' => t('Role'),
      '#options' => $options,
      '#default_value' => $default_value,
      // These are only valid for 'select' type, but do no harm to checkboxes.
      '#multiple' => TRUE,
      '#size' => count($options) > 8 ? 8 : count($options),
    );

    $form['role_value']['#prefix'] = '<div class="views-group-box views-right-70"><div id="edit-options-value-wrapper">';
    $form['role_value']['#suffix'] = '</div></div>';
  }

  /**
   * Child classes should be used to override this function and set the
   * 'value options', unless 'options callback' is defined as a valid function
   * or static public method to generate these values.
   *
   * This can use a guard to be used to reduce database hits as much as
   * possible.
   */
  function get_role_value_options() {
    if (isset($this->role_value_options)) {
      return;
    }

    if (isset($this->definition['options callback']) && is_callable($this->definition['options callback'])) {
      if (isset($this->definition['options arguments']) && is_array($this->definition['options arguments'])) {
        $this->role_value_options = call_user_func_array($this->definition['options callback'], $this->definition['options arguments']);
      }
      else {
        $this->role_value_options = call_user_func($this->definition['options callback']);
      }
    }
  }

  /**
   * Provides a unique placeholders for the role_code field.
   */
  function _get_role_value_ids() {
    $items = array();
    if (empty($this->role_value)) {
      return $items;
    }

    // Get a list of roles keyed by role_code.
    $roles = _ampp_projects_get_amp_roles();

    foreach ($this->role_value as $role_code) {
      if (!empty($roles[$role_code])) {
        $items[] = $roles[$role_code]->amp_role_id;
      }
    }
    return $items;
  }

  /**
   * Provides a unique placeholders for the role_code field.
   */
  function _get_role_placeholder() {
    static $count;

    if (empty($count)) {
      $count = 1;
      return ':' . $this->table_alias . '__org_role_ids';
    }
    else {
      $count++;
      return ':' . $this->table_alias . '__org_role_ids_' . $count;
    }
  }

  /**
   * Alter the field query to only display organisations with certain roles.
   */
  function query($group_by = FALSE) {
    $this->ensure_my_table();

    // Add the field.
    $params = $this->options['group_type'] != 'group' ? array('function' => $this->options['group_type']) : array();
    $langcode = $GLOBALS['language']->language;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', amp_org_role.organisation, 'amp_organisation', 'name', 'amp_org_id', '${langcode}')";

    // Translate the Role Codes to Role IDs.
    $role_value_ids = $this->_get_role_value_ids();
    $role_placeholder = $this->_get_role_placeholder();
    $params['placeholders'][$role_placeholder] = $role_value_ids;

    // Filter by Role ID.
    if (count($role_value_ids) > 1) {
      $role_operator = ($this->role_operator == 'in') ? 'IN' : 'NOT IN';
      $condition = "amp_org_role.role $role_operator ($role_placeholder)";
    }
    else if (count($role_value_ids) == 1) {
      $role_operator = ($this->role_operator == 'in') ? '=' : '!=';
      $condition = "amp_org_role.role $role_operator $role_placeholder";
    }

    $fieldsql = "ARRAY_TO_STRING(ARRAY_AGG(DISTINCT(CASE WHEN ${condition} THEN ${translate_field} END)), ', ')";
    $this->field_alias = $this->query->add_field(NULL, $fieldsql, 'organisation_name', $params);
  }
}
