<?php

/**
 * @TODO: Remove this SQL security nightmare.
 */

$plugin = array(
  'single' => TRUE,
  'title' => t('Custom query'),
  'description' => t('Allows constructing SELECT SQL queries of your choice. It is limited but still be very careful.'),
  'category' => array(t('AMPP Widgets')),
  'defaults' => array(
    'database' => '',
    'columns' => '',
    'labels' => '',
    'table' => '',
    'where' => '',
    'sortable' => '',
    'limit' => '',
  ),
  'render callback' => 'ampp_general_query_render',
  'hook theme' => 'ampp_general_query_content_type_theme',
);

/**
 *
 */
function ampp_general_query_render($subtype, $conf, $args, $context) {
  $block = new stdClass();

  $variables = array(
    'labels' => array(),
    'rows' => array(),
    'message' => NULL,
  );

  // Expand labels.
  $labels_arr = explode(',', $conf['labels']);
  $labels_arr = array_filter($labels_arr);

  // Translate labels.
  foreach ($labels_arr as $label) {
    $variables['labels'][] = t(check_plain($label));
  }

  db_set_active($conf['database']);
  try {
    $query = ampp_general_build_query($conf);
    $result = $query->execute()->fetchAll(PDO::FETCH_ASSOC);

    if (empty($result)) {
      $variables['message'] = 'No items to dispaly.';
    }
    else {
      foreach ($result as $delta => $record) {
        foreach ($record as $key => $value) {
          $variables['rows'][$delta][$key] = check_plain($value);
        }
      }
    }
  }
  catch (Exception $e) {
    $variables['message'] = 'An error occured, items could not be retreived.';
  }
  db_set_active();

  $block->content = theme('ampp_general_query', $variables);

  return $block;
}

/**
 * Implementing hook_content_type_edit_form()
 */
function ampp_general_query_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  //$form['override_title']['#access'] = false;
  //$form['override_title_text']['#access'] = false;
  $form['override_title_markup']['#access'] = FALSE;

  global $databases;
  foreach ($databases as $k => $v) {
    $dbs[$k] = $k;
  }

  $form['database'] = array(
    '#type' => 'select',
    '#title' => t('Database'),
    '#options' => $dbs,
    '#default_value' => $conf['database'] ? $conf['database'] : '',
  );

  $form['columns'] = array(
    '#type' => 'textfield',
    '#title' => t('Column names'),
    '#default_value' => $conf['columns'] ? $conf['columns'] : '',
    '#description' => t('Comma delimited. Aggregate functions like SUM can be used.'),
    '#element_validate' => array('ampp_general_query_columns_validate'),
  );

  $form['labels'] = array(
    '#type' => 'textfield',
    '#title' => t('Column labels'),
    '#default_value' => $conf['labels'] ? $conf['labels'] : '',
    '#description' => t('Comma delimited.'),
    '#element_validate' => array('ampp_general_query_labels_validate'),
  );

  $form['table'] = array(
    '#type' => 'textfield',
    '#title' => t('Table name(s)'),
    '#default_value' => $conf['table'] ? $conf['table'] : '',
    '#description' => t('Table name to be queried.'),
    '#element_validate' => array('ampp_general_query_table_validate'),
  );

  $form['where'] = array(
    '#type' => 'textfield',
    '#title' => t('Where clause'),
    '#default_value' => $conf['where'] ? $conf['where'] : '',
    '#description' => t('Valid SQL syntax should be used. Use ":iso2" marker for user navigatoin language value.'),
    '#element_validate' => array('ampp_general_query_where_validate'),
  );

  $form['sortable'] = array(
    '#type' => 'textfield',
    '#title' => t('Sort by clause'),
    '#default_value' => $conf['sortable'] ? $conf['sortable'] : '',
    '#description' => t('Valid SQL syntax should be used. It should include column name and direction: "date DESC, title ASC"'),
    '#element_validate' => array('ampp_general_query_sortable_validate'),
  );

  $form['limit'] = array(
    '#type' => 'textfield',
    '#size' => 3,
    '#maxlength' => 3,
    '#title' => t('Limit'),
    '#default_value' => $conf['limit'] ? $conf['limit'] : '',
    '#description' => t('Numeric values only. If left blank default value 10 will be used.'),
    '#element_validate' => array('ampp_general_query_limit_validate'),
  );

  $form['#validate'][] = 'query_form_validate';

  return $form;
}

/**
 * Handles edit for submit
 */
function ampp_general_query_content_type_edit_form_submit(&$form, &$form_state) {
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}

/**
 * Custom query form validation handler.
 */
function query_form_validate($form, &$form_state) {
  db_set_active($form_state['values']['database']);
  try {
    $query = ampp_general_build_query($form_state['values']);
    $sql = (string) $query;
    drupal_set_message($sql, 'query to be executed');
    $result = $query->execute();
  }
  catch (Exception $e) {
    form_set_error('columns', t('Resulting SQL has an error where executed against "!db": ', array('!db' => $form_state['values']['database'])) . '<br>"' . $query . '"<br><font style="color:red">' . $e->getMessage() . '</font>');
  }
  db_set_active();
}

/**
 *
 */
function ampp_general_build_query($conf) {
  global $language;

  // Expand columns.
  $columns_arr = explode(',', $conf['columns']);
  $columns_arr = array_filter($columns_arr);

  $table_name = trim($conf['table']);
  $select_query = db_select($table_name);

  $select_query->fields($table_name, $columns_arr);

  if (!empty($conf['where'])) {
    $select_query->where($conf['where'], array(':iso2' => $language->language));
  }

  if (!empty($conf['sortable'])) {
    $order_by_expressions = explode(',', $conf['sortable']);
    foreach ($order_by_expressions as $expression) {
      $expression = explode(' ', trim($expression));
      if (count($expression) == 1) {
        $expression[0] = trim($expression[0]);
        $select_query->orderBy($expression[0]);
      }
      else if (count($expression) == 2) {
        $expression[0] = trim($expression[0]);
        $select_query->orderBy($expression[0], $expression[1]);
      }
    }
  }

  $limit = empty($conf['limit']) ? 10 : (int) $conf['limit'];
  if (!empty($limit)) {
    $select_query->range(0, $limit);
  }

  return $select_query;
}

/**
 * Element validation function
 */
function ampp_general_query_columns_validate($element, &$form_state, $form) {
  // Expand columns.
  $columns_arr = explode(',', $form_state['values']['columns']);
  $columns_arr = array_filter($columns_arr);

  if (empty($element['#value']) || empty($columns_arr)) {
    form_error($element, t('Column names cannot be empty'));
  }
}

/**
 * Element validation function
 */
function ampp_general_query_labels_validate($element, &$form_state, $form) {
  // Expand labels.
  $labels_arr = explode(',', $element['#value']);
  $labels_arr = array_filter($labels_arr);

  // Expand columns.
  $columns_arr = explode(',', $form_state['values']['columns']);
  $columns_arr = array_filter($columns_arr);

  if (count($labels_arr) != count($columns_arr)) {
    form_error($element, t('Column name and label counts should be equal.'));
  }
}

/**
 * Element validation function
 */
function ampp_general_query_table_validate($element, &$form_state, $form) {
  // Expand tables.
  $tables_arr = explode(',', $element['#value']);
  $tables_arr = array_filter($tables_arr);

  if (empty($element['#value']) || empty($tables_arr)) {
    form_error($element, t('Table name(s) cannot be empty'));
  }
}

/**
 * Element validation function
 */
function ampp_general_query_where_validate($element, &$form_state, $form) {
}

/**
 * Element validation function
 */
function ampp_general_query_sortable_validate($element, &$form_state, $form) {
}

/**
 * Element validation function
 */
function ampp_general_query_limit_validate($element, &$form_state, $form) {
  $element['#value'] = trim($element['#value']);
  if (!empty($element['#value']) && (!intval($element['#value']) || intval($element['#value'] == 0))) {
    form_error($element, t('Limit should be numeric value'));
  }
}

/**
 * Theme function
 */
function ampp_general_query_content_type_theme(&$theme, $plugin) {
  $theme['ampp_general_query'] = array(
    'path' => $plugin['path'],
    'template' => 'query',
  );
}
