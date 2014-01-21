<?php 

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


/*
 *
 */
function ampp_general_query_render($subtype, $conf, $args, $context) {
  $block = new stdClass();
  
  $labels = trim($conf['labels']);
  $labels = trim($labels, ',');
  $labels = trim($labels);
  $labels_arr = explode(',', $labels);
  foreach ($labels_arr as $k => $v) $labels_arr[$k] = t($labels_arr[$k]);
  $vars['labels'] = $labels_arr;
  
  $query = build_query($conf);
  $active = db_set_active($conf['database']);
  try {   
    $result = db_query($query)->fetchAll();
    db_set_active($active);
    $vars['success'] = 1;
  } catch (Exception $e) {
    $vars['success'] = 0;
    db_set_active($active);   
  }
  
  foreach ($result as $record) {
    $vars['values'][] = (array) $record;
  }
  $vars['values'] = $result;
  $block->content = theme('ampp_general_query', array('vars' => $vars));
  $vars['error_msg'] = t('Error...');

  return $block;
}


/*
 * Implementing hook_content_type_edit_form()
 */
function ampp_general_query_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];
  
  //$form['override_title']['#access'] = false;
  //$form['override_title_text']['#access'] = false;
  $form['override_title_markup']['#access'] = false;
  
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
    '#element_validate' => array('columns_validate'), 
  );
    
  $form['labels'] = array(
    '#type' => 'textfield',
    '#title' => t('Column labels'),
    '#default_value' => $conf['labels'] ? $conf['labels'] : '', 
    '#description' => t('Comma delimited.'), 
    '#element_validate' => array('labels_validate'),
  );
  
  $form['table'] = array(
    '#type' => 'textfield',
    '#title' => t('Table name(s)'),
    '#default_value' => $conf['table'] ? $conf['table'] : '', 
    '#description' => t('Valid SQL syntax should be used.'),
    '#element_validate' => array('table_validate'), 
  );
  
  $form['where'] = array(
    '#type' => 'textfield',
    '#title' => t('Where clause'),
    '#default_value' => $conf['where'] ? $conf['where'] : '', 
    '#description' => t('Valid SQL syntax should be used. Use "!iso2" marker for user navigatoin language value.'),
    '#element_validate' => array('where_validate'), 
  );

  $form['sortable'] = array(
    '#type' => 'textfield',
    '#title' => t('Sort by clause'),
    '#default_value' => $conf['sortable'] ? $conf['sortable'] : '', 
    '#description' => t('Valid SQL syntax should be used.'),
    '#element_validate' => array('sortable_validate'), 
  );

  $form['limit'] = array(
    '#type' => 'textfield',
    '#size' => 3,
    '#maxlength' => 3,
    '#title' => t('Limit'),
    '#default_value' => $conf['limit'] ? $conf['limit'] : '', 
    '#description' => t('Numeric values only. If left blank default value 10 will be used.'),
    '#element_validate' => array('limit_validate'), 
  );

  $form['#validate'][] = 'query_form_validate';
  
  return $form;
}


/*
* Handles edit for submit
*/
function ampp_general_query_content_type_edit_form_submit(&$form, &$form_state) {
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}


/*
 *
 */
function query_form_validate($form, &$form_state) {
  
  $active = db_set_active($form_state['values']['database']);

  $query = build_query($form_state['values']);
  try {   
    $result = db_query($query);
    db_set_active($active);
  } catch (Exception $e) {
    db_set_active($active);
    form_set_error('columns', t('Resulting SQL has an error where executed against "!db": ', array('!db' => $form_state['values']['database'])) . '<br>"' . $query . '"<br><font style="color:red">' . $e->getMessage() . '</font>');
  }
}


/*
 *
 */
function build_query($conf) {

  global $language;

  $columns = trim($conf['columns']);
  $columns = trim($columns, ',');
  $columns = trim($columns);
  $columns_arr = explode(',', $columns); 
  
  $labels = trim($conf['labels']);
  $labels = trim($labels, ',');
  $labels = trim($labels);
  $labels_arr = explode(',', $labels); 
  
  foreach ($columns_arr as $k => $v) {
    $columns_labels_arr[] = $v . ' as ' . $labels_arr[$k]; 
  }
  
  $query = 'SELECT ' . implode(',', $columns_labels_arr) . ' FROM ' . $conf['table'];

  if (!empty($conf['where'])) {
    $query .= ' WHERE ' . $conf['where'];
    $query = format_string($query, array('!iso2' => $language->language));
  }
  
  if (!empty($conf['sortable'])) {
    $query .= ' ORDER BY ' . $conf['sortable'];
  }
  
  if (!empty($conf['limit'])) {
    $query .= ' LIMIT ' . intval($conf['limit']);
  } else {
    $query .= ' LIMIT 10';
  }
  
  return $query;
}


/*
* Validation function
*/
function columns_validate($element, &$form_state, $form) {
  $element['#value'] = trim($element['#value']);
  $element['#value'] = trim($element['#value'], ',');
  $element['#value'] = trim($element['#value']);
  $columns_arr = explode(',', $element['#value']);
  if (empty($element['#value']) || empty($columns_arr)) {
    form_error($element, t('Column names cannot be empty'));
  }
}


/*
* Validation function
*/
function labels_validate($element, &$form_state, $form) {
  $element['#value'] = trim($element['#value']);
  $element['#value'] = trim($element['#value'], ',');
  $element['#value'] = trim($element['#value']);
  $labels_arr = explode(',', $element['#value']);
  
  $columns = trim($form_state['values']['columns']);
  $columns = trim($columns, ',');
  $columns = trim($columns);
  $columns_arr = explode(',', $columns);
  
  if (count($labels_arr) != count($columns_arr)) {
     form_error($element, t('Column name and label counts should be equal.'));
  }
}


/*
* Validation function
*/
function table_validate($element, &$form_state, $form) {
  $element['#value'] = trim($element['#value']);
  $element['#value'] = trim($element['#value'], ',');
  $element['#value'] = trim($element['#value']);
  $tables_arr = explode(',', $element['#value']);
  if (empty($element['#value']) || empty($tables_arr)) {
     form_error($element, t('Table name(s) cannot be empty'));
  }
}


/*
* Validation function
*/
function where_validate($element, &$form_state, $form) {
}


/*
* Validation function
*/
function sortable_validate($element, &$form_state, $form) {
}


/*
* Validation function
*/
function limit_validate($element, &$form_state, $form) {
  $element['#value'] = trim($element['#value']);
  if (!empty($element['#value']) && (!intval($element['#value']) || intval($element['#value'] == 0 ))) {
     form_error($element, t('Limit should be numeric value'));
  }
}


/*
* Theme function
*/
function ampp_general_query_content_type_theme(&$theme, $plugin) {
  $theme['ampp_general_query'] = array(
    'arguments' => array('vars' => NULL),
    'path' => $plugin['path'],
    'template' => 'query',
  );
}