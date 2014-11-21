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

  $form['directory_settings'] = array(
    '#type' => 'fieldset',
    '#title' => t('Directory Settings'),
    '#collapsible' => TRUE,
  );

  $form['directory_settings']['update_scripts_directory'] = array(
    '#title' => 'Path to update scripts directory',
    '#type' => 'textfield',
    '#description' => t('Update Scripts directory path relative to drupal root.'),
    '#default_value' => update_scripts_get_directory(),
    '#maxlength' => 2048,
    '#size' => 40,
  );

  $directory_status = update_scripts_directory_status();
  $form['directory_settings']['directory_status'] = array(
    '#title' => 'Directory Status',
    '#type' => 'item',
    '#description' => filter_xss_admin($directory_status['description']),
    '#markup' => $directory_status['markup'],
    '#attached' => array(
      'css' => array(
        drupal_get_path('module', 'update_scripts') . '/update_scripts.admin.css' => array(
          'preprocess' => FALSE,
        ),
      ),
    ),
  );

  $form['environment_settings'] = array(
    '#type' => 'fieldset',
    '#title' => t('Environment settings'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
    '#description' => '<h4>' . t('Does not apply for PROJECT_ENVIRONMENT == "local"') . '</h4>',
  );

  $form['environment_settings']['update_scripts_maintenance_mode'] = array(
    '#type' => 'radios',
    '#title' => t('Enforce site maintenance mode while running the scripts if not enabled:'),
    '#options' => array(
      '' => t('No'),
      'permanent' => t('Yes, permanent. Keep site into maintenance mode after scripts have run.'),
      'temporary' => t('Yes, temporary. Put site on-line after scripts have run (even on fail).'),
    ),
    '#description' => t('Put site into maintenance mode while running the update scripts.'),
    '#default_value' => variable_get('update_scripts_maintenance_mode', ''),
    '#disabled' => TRUE,
  );

  $t_args = array(
    '@url' => 'http://drupal.org/project/features',
  );
  $form['environment_settings']['update_scripts_revert_features'] = array(
    '#type' => 'radios',
    '#title' => t('Auto revert features'),
    '#options' => array(
      'no' => t('No'),
      'yes' => t('Yes, only if update scripts exist.'),
      'always' => t('Always, even if there are no scripts to run (recommended).'),
    ),
    '#description' => t('Revert <a href="@url">features</a> after the update_scripts finished running, but before running "Auto clear cache" (if enabled).', $t_args),
    '#default_value' => variable_get('update_scripts_revert_features', 'no'),
  );

  $form['environment_settings']['update_scripts_clear_cache'] = array(
    '#type' => 'radios',
    '#title' => t('Auto clear cache'),
    '#options' => array(
      'no' => t('No'),
      'yes' => t('Yes, only if update scripts exist.'),
      'always' => t('Always, even if there are no scripts to run (recommended).'),
    ),
    '#description' => t('Clear all caches after the update_scripts finished running.', $t_args),
    '#default_value' => variable_get('update_scripts_clear_cache', 'no'),
  );

  return system_settings_form($form);
}

/**
 * UPDATE_SCRIPTS_MENU_BASE_PATH . '/setup/%' menu callback.
 */
function update_scripts_setup() {
  // Get action from the last argument in the URL.
  $actions = arg();
  $action = end($actions);

  // Get update scripts directory.
  $path = update_scripts_get_directory();

  switch ($action) {
    case 'directory':
      // Create update scripts directory.
      if (!is_dir($path)) {
        if (drupal_mkdir($path, NULL, TRUE)) {
          $args = array('%path' => $path);
          drupal_set_message(t('%path directory successfully created.', $args));
        }
        else {
          $args = array('%path' => $path);
          drupal_set_message(t('%path directory does not exist and could not be created.', $args), 'error');
        }
      }
      // Make sure .htaccess file exists.
      // break;
    case 'htaccess':
      // Create private .htaccess file.
      $htaccess_path = $path . DIRECTORY_SEPARATOR . '.htaccess';
      if (is_dir($path) && !is_file($htaccess_path)) {
        // Try to create .htaccess file and warn if errors occured.
        file_create_htaccess($path, TRUE, TRUE);

        // Check if .htaccess file was successfully created.
        if (is_file($htaccess_path)) {
          $args = array('%path' => $path, '%htaccess' => '.htaccess');
          drupal_set_message(t('%htaccess file successfully created in %path.', $args));
        }
      }
      break;
  }

  // Go back to settings page.
  drupal_goto(UPDATE_SCRIPTS_MENU_BASE_PATH);
}

/**
 * View the list of update script logs.
 *
 * UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs' menu callback.
 */
function update_scripts_view_logs() {
  module_load_include('inc', 'update_scripts');
  $logs = update_scripts_get_logs();

  // Build table header.
  $header = array(
    'lid' => 'LID',
    'path' => t('Path'),
    'started' => t('Started'),
    'finished' => t('Finished'),
    'duration' => t('Duration'),
  );

  $rows = array();
  if (!empty($logs)) {
    foreach ($logs as $log) {
      $diff = array('data' => $log->finished - $log->started . ' s', 'align' => 'right');
      $duration = empty($log->finished) ? '--' : $diff;

      // Determine the log status.
      $log_status = empty($log->finished) ? 'warning' : (empty($log->with_errors) ? 'status' : 'error');

      // Build the table row.
      $rows[] = array(
        'data' => array(
          'lid' => $log->lid,
          'path' => l($log->path, UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs/' . $log->lid),
          'started' => format_date($log->started, 'short'),
          'finished' => empty($log->finished) ? '--' : format_date($log->finished, 'short'),
          'duration' => $duration,
        ),
        'class' => array($log_status),
      );
    }
  }

  // Build the table.
  $table = array(
    'header' => $header,
    'rows' => $rows,
    'empty' => t('No log entries found.'),
  );
  return theme('table', $table);
}

/**
 * View a single log entry.
 *
 * UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs/%' menu callback.
 */
function update_scripts_view_log($lid) {
  $table = array();

  if (!is_numeric($lid)) {
    drupal_not_found();
  }

  // Get log entry from database.
  module_load_include('inc', 'update_scripts');
  $log = update_scripts_get_logs($lid);
  $log = reset($log);

  if (empty($log)) {
    drupal_not_found();
  }

  // Build table headers.
  $header = array('Item', 'Value');

  // Build table rows.
  $rows = array();

  $rows[] = array(
    t('Log ID'),
    $log->lid,
  );

  $rows[] = array(
    t('Full path to script'),
    drupal_get_path('module', 'update_scripts') . '/' . $log->path,
  );

  $rows[] = array(
    t('Started to run at'),
    format_date($log->started, 'short'),
  );

  $rows[] = array(
    t('Ended its run at'),
    format_date($log->finished, 'short'),
  );

  $rows[] = array(
    t('Duration'),
    empty($log->finished) ? '--' : $log->finished - $log->started . ' s',
  );

  $rows[] = array(
    t('Returned queries'),
    count($log->queries),
  );

  $rows[] = array(
    t('Total errors'),
    count($log->errors),
  );

  // Render general information table.
  $general_information = theme('table', array('header' => $header, 'rows' => $rows));

  // Render information about queries ran.
  if (!empty($log->queries)) {
    $queries_rows = array();
    foreach ($log->queries as $record) {
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

  // Render information about errors raised.
  if (!empty($log->result)) {
    $i = 0;
    $errors_rows = array();
    foreach ($log->result as $error) {
      $errors_rows[] = array(
        ++$i,
        array(
          'data' => $error,
          'class' => 'raised_error',
        ),
      );
    }

    $table['header'] = array('Number', 'Error');

    $table['rows'] = $errors_rows;

    $table['attributes'] = array('cellpadding' => 5, 'border' => 1);

    $errors = theme('table', $table);
  }
  else {
    $errors = NULL;
  }

  $args = array(
    'back_link' => l(t('Go to the list of log entries.'), UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs'),
    'general_information' => $general_information,
    'queries' => $queries,
    'errors' => $errors,
  );
  return theme('script_log_entry', $args);
}

/**
 * Retrieve the form for running update scripts.
 *
 * UPDATE_SCRIPTS_MENU_BASE_PATH menu callback.
 */
function update_scripts_run_form($form, &$form_state) {
  // Get update scripts info.
  module_load_include('inc', 'update_scripts');
  update_scripts_include_scripts();
  $scripts_tree = update_scripts_get_info();

  if (empty($scripts_tree)) {
    // Get update scripts directory.
    $path = update_scripts_get_directory();
    $args = array('%path' => $path);

    $form['contact_information'] = array(
      '#markup' => t('No update scripts detected in %path. Please check settings page and make sure that you respect the update scripts name convention.', $args),
    );
    return $form;
  }

  // Get update scripts module path.
  $module_path = drupal_get_path('module', 'update_scripts');

  $form['phases'] = array(
    '#type' => 'vertical_tabs',
    '#attached' => array(
      'js' => array(
        $module_path . '/update_scripts.admin.js' => array('preprocess' => FALSE),
      ),
      'css' => array(
        $module_path . '/update_scripts.admin.css' => array('preprocess' => FALSE),
      ),
    ),
  );

  $weight = -50;
  foreach (update_scripts_get_phase_info() as $phase_id => $phase_info) {
    // Get a list of scripts for the curent phase.
    $scripts = (!empty($scripts_tree[$phase_id])) ? $scripts_tree[$phase_id] : array();

    // Determine the fieldset weight.
    switch ($phase_id) {
      case 'phase_task':
        // Make the task scripts fieldset appear after the current active phase scripts.
        $fieldset_weight = 49.99;
        break;
      case 'phase_environment':
        $fieldset_weight = 49.98;
        break;
      case 'other':
        $fieldset_weight = 49.97;
        break;
      default:
        if ($phase_info['#closed']) {
          $fieldset_weight = 100 + $weight;
        }
        else {
          $fieldset_weight = $weight;
        }
        $weight++;
        break;
    }

    $form['phases'][$phase_id] = array(
      '#type' => 'fieldset',
      '#title' => check_plain($phase_info['#title']),
      '#collapsible' => TRUE,
      '#collapsed' => TRUE,
      '#group' => 'phases',
      '#phase_info' => $phase_info,
      '#weight' => $fieldset_weight,
      '#attributes' => array(
        'data-closed' => FALSE,
      ),
    );

    // Hide empty phases that are not closed.
    if (!$phase_info['#closed'] && empty($scripts)) {
      $form['phases'][$phase_id]['#access'] = FALSE;
      continue;
    }

    $with_actions = FALSE;
    if (!in_array($phase_id, array('other', 'phase_environment', 'phase_task'))) {
      // Build anctions and tableselect for each phases.
      update_scripts_build_actions_dropdown($form['phases'][$phase_id], $phase_info['#closed']);

      $with_actions = TRUE;
    }

    // Go away if the phase is closed.
    if ($phase_info['#closed']) {
      $form['phases'][$phase_id]['#attributes']['data-closed'] = 'true';
      $form['phases'][$phase_id]['closed'] = array(
        '#markup' => t('This phase is closed, scripts were not scanned!'),
      );
      continue;
    }

    update_scripts_build_run_table($form['phases'][$phase_id], $scripts, $with_actions);
  }

  return $form;
}

/**
 * Helper function for update_scripts_run_form().
 */
function update_scripts_build_actions_dropdown(&$elements, $closed = FALSE) {
  $phase_id = $elements['#phase_info']['#id'];

  // Build the phase actions form.
  $elements['actions'] = array(
    '#type' => 'fieldset',
    '#title' => t('Actions'),
    '#attributes' => array('class' => array('container-inline')),
  );

  if ($closed) {
    $options = array(
      'reopen-phase' => t('Reopen Development Phase'),
    );
  }
  else {
    $options = array(
      'run-selected' => t('Run selected scripts'),
      'run-all' => t('Run all scripts'),
      'close-phase' => t('Close Development Phase'),
    );
  }

  $elements['actions']['action'] = array(
    '#type' => 'select',
    '#options' => $options,
    '#parents' => array($phase_id, 'action'),
    '#default_value' => ($closed) ? 'reopen-phase' : 'run-selected',
  );

  $elements['actions']['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Run action'),
    '#name' => 'submit_' . $phase_id,
    '#submit' => array('update_scripts_phase_submit'),
  );

}

/**
 * Helper function for update_scripts_run_form().
 */
function update_scripts_build_run_table(&$elements, $scripts = array(), $with_actions = FALSE) {
  $phase_id = $elements['#phase_info']['#id'];

  // Build the sortable table header.
  $header = array(
    'script' => t('Script'),
    'path' => t('Relative path'),
    'status' => t('Run status'),
    'operations' => t('Operations'),
  );

  $option_rows = array();
  foreach ($scripts as $id => $script) {
    // Check the status of the update script.
    $status = update_scripts_get_script_status($script);

    // Do we need translation here? $script_name sometimes contains a path.
    // @see http://www.w3.org/TR/html-markup/label.html
    // @TODO: FIX, This creates invalid markup, only parsing elements are allowed as children to label.
    $markup = '<div class="label">' . $script['script_name'] . '</div>';
    if (!empty($script['script_desc'])) {
      $markup .= '<div class="description">' . $script['script_desc'] . '</div>';
    }

    $row = array(
      'script' => array(
        'data' => array(
          '#tag' => 'div',
          '#value' => $markup,
          '#phase' => $script['phase_id'],
          '#attributes' => array(
            'class' => array(
              'script-name',
              ($with_actions ? $status['class'] : 'task-script'),
            ),
          ),
          '#theme' => 'html_tag',
        ),
      ),
      'path' => $script['path'],
      'status' => $status['text'],
      'operations' => array(),
    );

    if ($with_actions) {
      $row['#attributes'] = array(
        'class' => array(
          'script-row',
          $status['class'],
        ),
      );
    }

    // For new scripts allow users to select the 'run' checkbox by clicking on the label.
    if ($status['class'] == 'new' && $with_actions) {
      $row['script']['data']['#tag'] = 'label';
      $row['script']['data']['#attributes']['for'] = drupal_clean_css_identifier("edit-{$phase_id}[scripts][{$script['hash']}]");
    }

    // Build a list of all the accessible operations for the current node.
    $operations = array();
    if (empty($script['errors']) && function_exists($script['function'])) {
      if (!$with_actions || ($with_actions && $status['code'] == UPDATE_SCRIPTS_STATUS_NEW)) {
        $operations['run'] = array(
          'title' => t('run script'),
          'href' => UPDATE_SCRIPTS_MENU_BASE_PATH . '/run/' . $script['hash'],
          'attributes' => array(
            'title' => t('Run "!script"', array('!script' => $script['path'])),
          ),
          'query' => array(),
        );
      }
      else {
        $operations['rerun'] = array(
          'title' => t('rerun script'),
          'href' => UPDATE_SCRIPTS_MENU_BASE_PATH . '/run/' . $script['hash'],
          'attributes' => array(
            'title' => t('Rerun "!script"', array('!script' => $script['path'])),
          ),
          'query' => array(
            'rerun' => 1,
          ),
        );
      }
    }

    if (isset($script['log']['lid'])) {
      $operations['view_log'] = array(
        'title' => t('view last log'),
        'href' => UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs/' . $script['log']['lid'],
        'query' => array(),
      );
    }

    if (count($operations) > 1) {
      // Render an unordered list of operations links.
      $row['operations'] = array(
        'data' => array(
          '#theme' => 'links',
          '#links' => $operations,
          '#attributes' => array('class' => array('links', 'inline')),
        ),
      );
    }
    elseif (!empty($operations)) {
      // Render the first and only operation as a link.
      $link = reset($operations);
      $row['operations'] = array(
        'data' => array(
          '#type' => 'link',
          '#title' => $link['title'],
          '#href' => $link['href'],
          '#options' => array('query' => $link['query']),
        ),
      );
    }
    $option_rows[$script['hash']] = $row;
  }

  $elements['scripts'] = array(
    '#header' => $header,
    '#parents' => array($phase_id, 'scripts'),
    '#empty' => t('No update scripts found!'),
    '#scripts' => $scripts,
  );
  if ($with_actions) {
    $elements['scripts'] += array(
      '#type' => 'tableselect',
      '#attributes' => array(
        'class' => array('update-scripts-run'),
      ),
      '#options' => $option_rows,
      '#pre_render' => array('update_scripts_pre_render_tableselect'),
    );
  }
  else {
    $elements['scripts'] += array(
      '#theme' => 'table',
      '#attributes' => array(
        'class' => array('update-scripts-tasks-run'),
      ),
      '#rows' => $option_rows,
      '#empty' => t('No setup scripts found!'),
    );
  }
}

/**
 * Disable checkbox for update scripts that already ran or have php errors.
 */
function update_scripts_pre_render_tableselect($element) {
  foreach (element_children($element) as $key) {
    $script = $element['#scripts'][$key];
    $status = update_scripts_get_script_status($script);
    if (!empty($status['code'])) {
      $element[$key]['#attributes']['disabled'] = TRUE;
    }
    if ($script['phase_id'] == 'phase_task') {
      $element[$key]['#access'] = FALSE;
    }
  }
  return $element;
}

/**
 * Submit handler for "run update scripts" form.
 */
function update_scripts_phase_submit($form, &$form_state) {
  // Load update scripts.
  module_load_include('inc', 'update_scripts');
  update_scripts_include_scripts();

  foreach ($form_state['input'] as $key => $value) {
    if (stripos($key, 'submit_phase_') === 0) {
      $phase_id = substr($key, 7);
      $phase_info = $form['phases'][$phase_id]['#phase_info'];
      if ($phase_id != $phase_info['#id']) {
        // @TODO: Move to form validation.
        drupal_set_message(t('Phase info not found.'), 'error');
        return;
      }

      $phase_input = $form_state['input'][$phase_id];
    }
  }

  switch ($phase_input['action']) {
    case 'close-phase':
      $phase_status = variable_get('update_scripts_closed_phases', array());
      $phase_status[$phase_id] = $phase_id;
      variable_set('update_scripts_closed_phases', $phase_status);

      $t_args = array(
        '%phase' => $phase_info['#title'],
      );
      drupal_set_message(t('The %phase was closed.', $t_args));
      return;
      // break;

    case 'reopen-phase':
      $phase_status = variable_get('update_scripts_closed_phases', array());
      if (array_key_exists($phase_id, $phase_status)) {
        unset($phase_status[$phase_id]);
      }
      variable_set('update_scripts_closed_phases', $phase_status);

      $t_args = array(
        '%phase' => $phase_info['#title'],
      );
      drupal_set_message(t('The %phase was reopened.', $t_args));
      return;
      // break;

    case 'run-selected':
      // Prepare a list of update script to run.
      $scripts_to_run = array();
      foreach ($phase_input['scripts'] as $hash => $run) {
        // Load update script information.
        $script_info = update_scripts_get_info($hash);
        if (!empty($run) && empty($script_info['errors']) && empty($script_info['log'])) {
          $scripts_to_run[$hash]['path'] = $script_info['path'];
        }
      }
      break;

    case 'run-all':
      // Prepare a list of update script to run.
      foreach ($phase_input['scripts'] as $hash => $run) {
        // Load update script information.
        $script_info = update_scripts_get_info($hash);
        if (empty($script_info['errors']) && empty($script_info['log'])) {
          $scripts_to_run[$hash]['path'] = $script_info['path'];
        }
      }
      break;
  }

  if (empty($scripts_to_run)) {
    // @TODO: Move to form validation.
    drupal_set_message(t('No scripts were selected.'), 'warning');
    return;
  }

  // Form the 'operations' array for batch processing of the update scripts.
  $operations = array();

  // Sort update scripts by path.
  uasort($scripts_to_run, '_update_scripts_sort_path');

  // Add update scripts to the batch queue.
  foreach ($scripts_to_run as $hash => $info) {
    $operations[] = array('_update_scripts_exec_script', array($hash));
  }

  // Do not revert features or clear the caches for local environments.
  if (PROJECT_ENVIRONMENT != 'local') {
    // Check to see if features should be reverted.
    $revert_features = variable_get('update_scripts_revert_features', 'no');
    switch ($revert_features) {
      case 'yes':
        // Do not revert features if there were no scripts to run.
        if (empty($scripts_run_queue)) {
          break;
        }

      case 'always':
        // Revert all features.
        $operations[] = array('_us_features__revert_all', array());
        break;

      default:
        // Nothing to do!
        break;
    }

    // Check to see if caches should be cleared.
    $clear_cache = variable_get('update_scripts_clear_cache', 'no');
    switch ($clear_cache) {
      case 'yes':
        // Do not caches caches if there were no scripts to run.
        if (empty($scripts_run_queue)) {
          break;
        }

      case 'always':
        // Clear all caches.
        $operations[] = array('drupal_flush_all_caches', array());
        break;

      default:
        // Nothing to do!
        break;
    }
  }

  // Get update scripts module path.
  $module_path = drupal_get_path('module', 'update_scripts');

  // Prepare the batch data structure.
  $batch = array(
    'title' => t('Running update scripts'),
    'operations' => $operations,
    'finished' => 'update_scripts_batch_finished',
    'file' => $module_path . '/update_scripts.admin.inc',
  );

  // Make sure that the batch operations are ran.
  batch_set($batch);
}

/**
 * Executes a script. It's useful to execute this way for debugging purposes.
 */
function _update_scripts_exec_script($hash) {
  // Get update scripts info.
  module_load_include('inc', 'update_scripts');
  update_scripts_include_scripts();

  // Load update script information.
  $script_info = update_scripts_get_info($hash);

  // Make sure the update scripts exists.
  if (empty($script_info)) {
    drupal_set_message(t('Update script %hash not found!', array('%hash' => $hash)), 'error');
    return array();
  }

  // Run the update script.
  return update_scripts_run_script($hash);
}

/**
 * This function gets called after the batch processing of scripts is done.
 */
function update_scripts_batch_finished($success, $results, $operations) {
  if ($success) {
    drupal_set_message(t('Update scripts batch ran successfully.'));
  }
  else {
    drupal_set_message(t('There were errors while running update scripts batch.'), 'error');
    watchdog('php', $message, array(), WATCHDOG_ERROR);
  }
}

/**
 * Menu callback for UPDATE_SCRIPTS_MENU_BASE_PATH/run/%.
 */
function update_scripts_run_single($form, &$form_state, $hash) {
  $form = array();

  // Load update script information.
  module_load_include('inc', 'update_scripts');
  update_scripts_include_scripts();
  $script_info = update_scripts_get_info($hash);

  // Make sure the update scripts exists.
  if (empty($script_info)) {
    drupal_set_message(t('Update script "%hash" could not be found!', array('%hash' => $hash)), 'error');
    drupal_goto(UPDATE_SCRIPTS_MENU_BASE_PATH);
  }

  $form['script_hash'] = array(
    '#type' => 'value',
    '#value' => $hash,
  );

  $form['#submit'] = array('update_scripts_run_single_submit');

  if (isset($_GET['rerun'])) {
    $question = t('Rerun the script?');
    $description = t('Are you sure you want to run the update script %path again?', array('%path' => $script_info['path']));
    $confirm_text = t('Rerun script');
  }
  else {
    $question = t('Run the script?');
    $description = t('Are you sure you want to run the update script %path?', array('%path' => $script_info['path']));
    $confirm_text = t('Run script');
  }

  $destination = UPDATE_SCRIPTS_MENU_BASE_PATH;

  return confirm_form($form, $question, $destination, $description, $confirm_text, t('Cancel'));
}

/**
 * Submit callback for update_scripts_run_single().
 *
 * @TODO: Display an error message when the script was not found!
 */
function update_scripts_run_single_submit($form, &$form_state) {
  // Load update script information.
  module_load_include('inc', 'update_scripts');
  update_scripts_include_scripts();
  $hash = $form_state['values']['script_hash'];
  $script_info = update_scripts_get_info($hash);

  // Run the update script.
  $log = update_scripts_run_script($hash);

  if ($log['finished'] && !$log['with_errors']) {
    drupal_set_message(t('Update script ran successfully: %path', array('%path' => $script_info['path'])));
  }
  elseif ($log['finished'] && $log['with_errors']) {
    drupal_set_message(t('Update script with run-time errors: %path', array('%path' => $script_info['path'])), 'error');
  }
  elseif (!$log['finished']) {
    drupal_set_message(t('Update script did not finished running: %path', array('%path' => $script_info['path'])), 'error');
  }

  // Flush all caches.
  drupal_flush_all_caches();

  // Go to the execution logs page.
  drupal_goto(UPDATE_SCRIPTS_MENU_BASE_PATH . '/logs');
}
