<?php

/**
 * @file
 * Admin page callbacks for the update_scripts module.
 */

/**
 * Callback for UPDATE_SCRIPTS_MENU_BASE_PATH/settings.
 */
function update_scripts_settings_form($form_state) {
  $form = array();

  $directory = variable_get('update_scripts_directory', conf_path() . '/update_scripts');

  $form['update_scripts_directory'] = array(
    '#title' => 'Path to update scripts directory',
    '#type' => 'textfield',
    '#description' => 'Directory will be created if possible. Path is relative to drupal root folder.',
    '#default_value' => $directory,
    '#maxlength' => 2048,
    '#size' => 40,
    '#after_build' => array('update_scripts_check_directory'),
  );

  $form['update_scripts'] = array(
    '#type' => 'fieldset',
    '#title' => t('Maintenance mode'),
  );

  $form['update_scripts']['maintenance_mode'] = array(
    '#type' => 'checkbox',
    '#title' => t('Enforce maintenance mode (not finished)'),
    '#description' => t('While in maintenance mode, the script \'/status.php\' will return an appropriate error code and message.'),
    '#default_value' => variable_get('maintenance_mode', 0),
  );

  return system_settings_form($form);
}

/**
 *
 */
function update_scripts_check_directory($form_element) {
  $directory = $form_element['#value'];
  if (strlen($directory) == 0) {
    return $form_element;
  }

  if (!is_dir($directory) && !drupal_mkdir($directory, NULL, TRUE)) {
    // If the directory does not exists and cannot be created.
    $message = 'The directory %directory does not exist and could not be created.';
    $args = array('%directory' => $directory);
    form_set_error($form_element['#parents'][0], t($message, $args));
    watchdog('php', $message, $args, WATCHDOG_ERROR);
  }

  if (is_dir($directory) && !is_writable($directory) && !drupal_chmod($directory)) {
    // If the directory is not writable and cannot be made so.
    $message = 'The directory %directory exists but is not writable and could not be made writable.';
    $args = array('%directory' => $directory);
    form_set_error($form_element['#parents'][0], t($message, $args));
    watchdog('php', $message, $args, WATCHDOG_ERROR);
  }
  elseif (is_dir($directory)) {
    // Create private .htaccess file.
    file_create_htaccess($directory);
  }

  return $form_element;
}

/**
 * Retrieve the form for running update scripts.
 */
function update_scripts_batch_run_form() {
  $form = array();

  _update_scripts_batch_run_form_append_table($form);

  $form['buttons']['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Run selected'),
  );

  return $form;
}

function _update_scripts_batch_run_form_append_table(&$form) {
  global $_update_scripts_script_info;

  // Make sure that we consider script IDs in increasing alphabetical order
  $tree = array_keys($_update_scripts_script_info);
  sort($tree);

  $table = array(
    '#theme' => 'update_scripts_run_table',
    '#tree' => TRUE,
  );

  foreach ($tree as $sid) {
    $last_log = _update_scripts_get_last_log_id($_update_scripts_script_info[$sid]['path']);
    $with_errors = (isset($last_log['with_errors'])) ? $last_log['with_errors'] : NULL;
    $last_log_id = (isset($last_log['lid'])) ? $last_log['lid'] : NULL;
    $finished = (empty($last_log['finished'])) ? NULL : $last_log['finished'];

    if ($last_log_id) {
      $run_element = array(
        '#markup' => l(t('run again'), UPDATE_SCRIPTS_MENU_BASE_PATH . '/run/' . $sid),
      );
    }
    else {
      $run_element = array(
        '#type' => 'checkbox',
        '#title' => 'Run it.',
      );
    }
    $table[UPDATE_SCRIPTS_FORM_PREFIX . $sid] = array(
      'path' => array(
        '#markup'  => $_update_scripts_script_info[$sid]['path'],
      ),
      'php_errors' => array(
        '#type' => 'hidden',
        '#value' => $_update_scripts_script_info[$sid]['errors'],
      ),
      'run' => $run_element,
      'with_errors' => array(
        '#type' => 'hidden',
        '#value' => $with_errors,
      ),
      'last_log_id' => array(
        '#type' => 'hidden',
        '#value' => $last_log_id,
      ),
      'finished' => array(
        '#type' => 'hidden',
        '#value' => $finished,
      ),
    );
  }

  $form['scripts'] = $table;
}

/**
 * Submit handler for "run update scripts" form.
 */
function update_scripts_batch_run_form_submit($form, &$form_state) {
  $scripts_to_run = array();

  // Get all info submitted about update scripts.
  $selected_scripts = $form_state['input']['scripts'];
  if (!empty($selected_scripts)) foreach ($selected_scripts as $name => $info) {
    if (strpos($name, UPDATE_SCRIPTS_FORM_PREFIX) === 0) {
      if ($info['run']) {
        $scripts_to_run[] = intval(drupal_substr($name, drupal_strlen(UPDATE_SCRIPTS_FORM_PREFIX)));
      }
    }
  }

  // Form the 'operations' array for batch processing of the update scripts.
  $operations = array();

  // Function to call.
  $function = '_update_scripts_exec_script';
  // Arguments.
  foreach ($scripts_to_run as $script_id) {
    $args = array(UPDATE_SCRIPTS_FORM_PREFIX . 'update_' . $script_id);
    $operations[] = array($function, $args);
  }

  // Add a flush caches at the end to be consistent with drush updatedb.
  $operations[] = array('drupal_flush_all_caches', array());

  // Prepare the batch data structure.
  $batch = array(
    'title' => t('Running update scripts'),
    'operations' => $operations,
    'finished' => 'update_scripts_batch_finished',
    'file' => drupal_get_path('module', 'update_scripts') . '/update_scripts.admin.inc',
  );

  // Make sure that the batch operations are ran.
  batch_set($batch);
}

/**
 * Executes a script. It's useful to execute this way for debugging purposes.
 */
function _update_scripts_exec_script() {
  module_load_include('inc', 'update_scripts', 'update_scripts.main');

  // Get arguments passed & extract script_name - the first one
  $args = func_get_args();
  $run_script_function = array_shift($args);

  // Make sure that helper scripts & update scripts are auto-detected.
  // This ensures that functions to be run are dynamically defined.
  autodetect_helper_scripts();
  autodetect_update_scripts();

  // Check that the function really exists
  if (!function_exists($run_script_function)) {
    watchdog('php', 'Calling non-existing function: ' . $run_script_function);
    return;
  }

  // Finally call the function with the given arguments
  call_user_func_array($run_script_function, $args);
}

/**
 * This function gets called after the batch processing of update scripts is done.
 */
function update_scripts_batch_finished($success, $results, $operations) {
  if ($success) {
    $message = 'Update scripts batch ran successfully.';
    drupal_set_message(t($message));
    watchdog('php', $message);
  }
  else {
    $message = 'There were errors while running update scripts batch.';
    drupal_set_message(t($message), 'error');
    watchdog('php', $message, array(), WATCHDOG_ERROR);
  }
}

/**
 * Theme update scripts admin table.
 */
function theme_update_scripts_run_table($variables) {
  $form = $variables['elements'];

  $table = array();

  $table['header'] = array(
    'Script relative path',
    'Run',
    'Last log',
  );

  $directory = variable_get('update_scripts_directory', conf_path() . '/update_scripts');
  $args = array(
    '%directory' => $directory,
  );
  $table['empty'] = t('No scripts found in %directory.', $args);

  $rows = array();
  foreach (element_children($form) as $key) {
    $wid = $form[$key]['php_errors']['#value'];
    $log_id = $form[$key]['last_log_id']['#value'];
    $log_errors = $form[$key]['with_errors']['#value'];
    $finished = $form[$key]['finished']['#value'];

    if ($wid) {
      $form[$key]['run']['#access'] = FALSE;
      $errors_log = TRUE;
    }
    else {
      $errors_log = FALSE;
    }

    if ($log_id) {
      $text = ($log_errors) ? 'with errors' : (($finished) ? 'clean' : 'not finished') ;
      $run_log = l(t($text), UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs/' . $log_id);
    }
    else {
      $run_log = NULL;
    }

    $data = array(
      drupal_render($form[$key]['path']),
      drupal_render($form[$key]['run']),
      ($run_log) ? $run_log : '--',
    );

    $row = array('data' => $data);
    $row['class'][] = 'update-script-row';
    if ($errors_log) {
      $row['class'][] = 'error';
    }
    $rows[] = $row;
  }

  $table['rows'] = $rows;
  return theme('table', $table);
}

/**
 * Menu callback for UPDATE_SCRIPTS_MENU_BASE_PATH/run/%.
 */
function update_scripts_run_single($global_script_id) {
  global $_update_scripts_script_info;

  $relative_path = $_update_scripts_script_info[$global_script_id]['path'];

  $log = _update_scripts_run_script($global_script_id, $relative_path);

  // Flush all caches, to be consistent with the behaviour of drush updatedb.
  drupal_flush_all_caches();

  // Go to the logs page.
  drupal_goto(UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs');
}


/**
 * View the list of update script logs.
 */
function update_scripts_view_logs() {
  $logs = array_reverse(update_scripts_get_logs());


  if (empty($logs)) {
    return t('No log entries found.');
  }

  $table = array();

  $table['header'] = array(
    array('data' => 'Path', 'align' => 'center'),
    array('data' => 'Start run', 'align' => 'left'),
    array('data' => 'End run', 'align' => 'left'),
    array('data' => 'Duration', 'align' => 'right')
  );

  $rows = array();
  foreach ($logs as $log) {
    $link = l($log['path'], UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs/' . $log['lid']);
    $start_time = date("d-m-Y H:i:s", $log['started']);
    $end_time = (!empty($log['finished'])) ? date("d-m-Y H:i:s", $log['finished']) : '--';
    $diff = array('data' => $log['finished'] - $log['started'] . ' s', 'align' => 'right');
    $duration = (!empty($log['finished'])) ? $diff : '--';
    $table['rows'][] = array($link, $start_time, $end_time, $duration);
  }

  return theme('table', $table);
}

/**
 * View a single log entry.
 */
function update_scripts_view_log($lid) {
  $table = array();

  if (!is_numeric($lid)) {
    drupal_not_found();
  }

  // Get log entry from database.
  $log = update_scripts_get_log($lid);

  // Table headers.
  $table['header'] = array('Item', 'Value');

  $diff = $log['finished'] - $log['started'];

  // Table rows.
  $table['rows'] = array(
    array('Full path to script', drupal_get_path('module', 'update_scripts') . '/' . $log['path']),
    array('Started to run at', date("d-m-Y H:i:s", $log['started'])),
    array('Ended its run at', date("d-m-Y H:i:s", $log['finished'])),
    array('Duration',  (!empty($log['finished'])) ? $diff . ' s' : '--'),
    array('Total queries', count($log['queries'])),
    array('Total errors', count($log['errors'])),
  );

  // Table attributes.
  $table['attributes'] = array('cellpadding' => 5, 'border' => 1);

  // Render general information
  $general_information = theme('table', $table);

  // Render information about queries ran
  if (!empty($log['queries'])) {
    $queries_rows = array();
    foreach ($log['queries'] as $record) {
      $info = array(
        'data' => $record['success'] ? 'OK' : 'Failed',
        'class' => $record['success'] ? 'query_ok' : 'query_failed',
      );
      $queries_rows[] = array($record['query'], $info);
    }

    $table['header'] = array('Query', 'Result');

    $table['rows'] = $queries_row;

    $table['attributes'] = array('cellpadding' => 5, 'border' => 1);

    $queries = theme('table', $table);
  }
  else {
    $queries = NULL;
  }

  // Render information about errors raised
  if (!empty($log['result'])) {
    $i = 0;
    $errors_rows = array();
    foreach ($log['result'] as $error) {
      $errors_rows[] = array(++$i, array('data' => $error, 'class' => 'raised_error'));
    }

    $table['header'] = array('Number', 'Error');

    $table['rows'] = $errors_rows;

    $table['attributes'] = array('cellpadding' => 5, 'border' => 1);

    $errors = theme('table', $table);
  }
  else {
    $errors = NULL;
  }

  $back_link = l(t('Go to the list of log entries.'), UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs');
  $args = array(
    'back_link' => $back_link,
    'general_information' => $general_information,
    'queries' => $queries,
    'errors' => $errors,
  );
  return theme('script_log_entry', $args);
}

