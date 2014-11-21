<?php

/**
 * @file
 * Main functions for the Update Scripts module.
 */

/**
 * Load the update scripts helper files.
 */
function update_scripts_include_helpers() {
  // Detect helper files.
  $pattern_array = array(
    drupal_get_path('module', 'update_scripts'),
    'helpers',
    UPDATE_SCRIPTS_HELPER_PATTERN,
  );
  $files = glob(implode(DIRECTORY_SEPARATOR, $pattern_array));

  // Load helper files.
  if (!empty($files)) {
    foreach ($files as $file) {
      require_once $file;
    }
  }
}

/**
 * Load the update scripts.
 */
function update_scripts_include_scripts() {
  static $loaded = FALSE;

  // Make sure update scripts are loaded only once.
  if ($loaded) {
    return;
  }

  // Detect update scripts.
  $scripts_directory = update_scripts_get_directory();
  if (!is_dir($scripts_directory)) {
    return;
  }

  // Scan child directories for update scripts.
  _update_scripts_scan_directory($scripts_directory);

  $loaded = TRUE;
}

/**
 * Function for recursively processing directory includes.
 */
function _update_scripts_scan_directory($current_directory) {
  // Scan to child directories.
  $directories = glob($current_directory . DIRECTORY_SEPARATOR . '*', GLOB_ONLYDIR);
  foreach ($directories as $directory) {
    // Ignore CVS directories.
    if (basename($directory) == 'CVS') {
      continue;
    }
    _update_scripts_scan_directory($directory);
  }

  // Get information about the current phase, if any and go away if the phase is closed.
  $phase_info = update_scripts_get_phase_info($current_directory);
  if (empty($phase_info) || !empty($phase_info['#closed'])) {
    return;
  }

  // Go away if the phase is disabled.
  if (!empty($phase_info['#closed'])) {
    return;
  }

  $basedir = basename($current_directory);
  $update_scripts = array();
  switch ($basedir) {
    case 'task':
    case 'temporary':
    case 'environment':
      $script_pattern = UPDATE_SCRIPTS_TASKS_PATTERN;
      if ($basedir == 'environment') {
        $script_pattern = UPDATE_SCRIPTS_ENVIRONMENT_PATTERN;
      }
      if ($basedir == 'temporary') {
        $script_pattern = UPDATE_SCRIPTS_TEMPORARY_PATTERN;
      }

      $files = scandir($current_directory);
      foreach ($files as $filename) {
        if ($filename == '.' || $filename == '..') {
          continue;
        }

        // Match filename against the environment scripts pattern.
        if (preg_match($script_pattern, $filename)) {
          $update_scripts[] = $current_directory . DIRECTORY_SEPARATOR . $filename;
        }
      }
      break;

    default:
      // Scan and include scripts found in current directory.
      $update_scripts = glob($current_directory . DIRECTORY_SEPARATOR . UPDATE_SCRIPTS_SCRIPT_PATTERN);
      break;
  }

  if (!empty($update_scripts)) {
    foreach ($update_scripts as $script_path) {
      _update_scripts_load_script($script_path);
    }
  }
}

/**
 * Load the update script code, check for errors and run status.
 *
 * @TODO: Allow all sites and current site update scripts?!?
 */
function _update_scripts_load_script($script_path = NULL) {
  // Get relative path to script.
  $relative_path = update_scripts_get_relative_path($script_path);

  // Determine script phase.
  // @TODO: REMOVE!!
  $phase_info = update_scripts_get_phase_info($script_path);

  // Retrieve script contents.
  $script_code = update_scripts_extract_script_code($script_path);

  // Create unique hash.
  $hash = _update_scritps_get_unique_id($relative_path);

  // Get the script name.
  $script_name = update_scripts_read_phpdoc($hash, $script_path, 'file');
  if (empty($script_name)) {
    $script_name = basename($relative_path);
  }

  // Get the script description.
  $script_desc = update_scripts_read_phpdoc($hash, $script_path, 'desc');

  // Store mapping from global script ID's to their paths.
  $script_info = array(
    'path' => $relative_path,
    'full_path' => $script_path,
    'phase_id' => $phase_info['#id'],
    'script_name' => $script_name,
    'script_desc' => $script_desc,
    'hash' => $hash,
    'function' => NULL,
    'errors' => FALSE,
  );

  // Start buffering the output.
  ob_start();

  // Create a function that holds the update script code.
  // This is primarily used for checking for php syntax errors.
  $args = '$script_directory = \'' . dirname($script_info['full_path']) . '\'';
  $function = create_function($args, $script_code);
  if (!empty($function)) {
    $script_info['function'] = $function;
  }

  // Output buffer should be empty because we are just creating a function.
  $errors = ob_get_clean();

  // If Output buffer is not empty then we have PHP errors in the script code.
  if (!empty($errors)) {
    // Log found PHP errors to watchdog.
    $script_info['errors'] = TRUE;
    $args = array(
      '%path' => $relative_path,
      '!errors' => $errors,
    );
    watchdog('debug-update-script', 'The are PHP errors inside update script: %path<br /><br />!errors', $args, WATCHDOG_ERROR);

    // Display error to the user.
    $args = array(
      '%path' => $relative_path,
    );
    drupal_set_message(t('The are PHP errors inside update script: %path. Check the watchdog for more details.', $args), 'error');
  }
  else {
    $script_info['log'] = _update_scripts_get_last_log_id($relative_path);
  }

  update_scripts_set_info($hash, $script_info);
}

/**
 * Get relative path for a script or directory.
 */
function update_scripts_get_relative_path($path) {
  $directory = update_scripts_get_directory();
  if (strrpos($path, $directory) === 0) {
    return drupal_substr($path, drupal_strlen($directory) + 1);
  }
  else {
    return NULL;
  }
}

/**
 * Extract PHPDoc from update scripts.
 */
function update_scripts_read_phpdoc($hash, $script_path, $token) {
  // Store the tokens for each script as name/value pairs.
  $phpdocs = &drupal_static(__FUNCTION__, array());

  // Get the token from cache, if any.
  if (isset($phpdocs[$hash])) {
    if (isset($phpdocs[$hash][$token])) {
      return $phpdocs[$hash][$token];
    }
    return '';
  }

  // Get all PHP tokens from the update script.
  $file_tokens = token_get_all(file_get_contents($script_path));

  // Get all PHPDoc style comments.
  $doc_comments = array_filter($file_tokens, function($entry) {
    // @see http://www.php.net/manual/en/tokens.php
    return $entry[0] == T_DOC_COMMENT;
  });

  if (empty($doc_comments)) {
    return '';
  }

  // The file doc comment should be the first item in the list.
  $file_doc_comment = array_shift($doc_comments);

  // Get the name and value using named subpatterns.
  // @see http://www.php.net/manual/de/regexp.reference.subpatterns.php
  $regexp = "/\@(?P<name>[a-z]+)\:(?P<value>.+)\n/";

  // Get all name/value pairs and store them.
  $matches = array();
  if (preg_match_all($regexp, $file_doc_comment[1], $matches)) {
    for ($i = 0; $i < sizeof($matches[0]); $i++) {
      $name = $matches['name'][$i];
      $value = trim($matches['value'][$i]);
      $phpdocs[$hash][$name] = $value;
    }

  }

  if (isset($phpdocs[$hash][$token])) {
    return $phpdocs[$hash][$token];
  }

  return '';
}

/**
 * Extracts a PHP script's contents, given its path.
 * @NOTE: Shouldn't be used on large files or large sets of files, because it
 * reads the whole file in memory.
 */
function update_scripts_extract_script_code($path) {
  // Get file contents.
  $contents = file_get_contents($path);

  // Sanity check.
  if ($contents === FALSE) {
    return;
  }

  // Initial trim.
  $contents = trim($contents);

  // Trim PHP start tag, if any.
  if (strpos($contents, UPDATE_SCRIPTS_SCRIPT_START) === 0) {
    $contents = drupal_substr($contents, drupal_strlen(UPDATE_SCRIPTS_SCRIPT_START));
  }

  // Trim PHP end tag, if any.
  if (strrpos($contents, UPDATE_SCRIPTS_SCRIPT_END) === drupal_strlen($contents) - drupal_strlen(UPDATE_SCRIPTS_SCRIPT_END)) {
    $contents = drupal_substr($contents, 0, drupal_strlen($contents) - drupal_strlen(UPDATE_SCRIPTS_SCRIPT_END));
  }

  // Trim the code again.
  return PHP_EOL . trim($contents) . PHP_EOL;
}

/**
 * Create unique hash using the update script relative path.
 */
function _update_scritps_get_unique_id($relative_path) {
  // Strip any non alplhanumeric character from the relative path.
  $base36_path = preg_replace('/[^a-z0-9]/', '', strtolower($relative_path));

  return str_replace(array('update', 'php'), '', $base36_path);
}

/**
 * Store information about detected update scripts.
 */
function update_scripts_set_info($hash = NULL, $script_info = NULL) {
  // Store the tokens for each script as name/value pairs.
  $scripts = &drupal_static(__FUNCTION__, array('map' => array(), 'info' => array()));

  if (is_string($hash) && isset($scripts['map'][$hash])) {
    $phase_id = $scripts['map'][$hash];
    return $scripts['info'][$phase_id][$hash];
  }
  elseif (isset($scripts) && empty($hash)) {
    return $scripts;
  }

  if (isset($hash, $script_info)) {
    $phase_id = $script_info['phase_id'];
    $scripts['map'][$hash] = $phase_id;
    $scripts['info'][$phase_id][$hash] = $script_info;
  }
}

/**
 * Retreive information about detected update scripts.
 *
 * @param string $script
 *   An unique hash used for each update script.
 *
 * @see _update_scritps_get_unique_id()
 */
function update_scripts_get_info($hash = NULL, $sort = TRUE) {
  if (!empty($hash)) {
    return update_scripts_set_info($hash);
  }

  // Get update scripts info.
  $scripts_tree = update_scripts_set_info();

  // Sort update scripts by path.
  if ($sort && !empty($scripts_tree['info'])) {
    foreach ($scripts_tree['info'] as $phase_id => &$scripts) {
      uasort($scripts, '_update_scripts_sort_path_reversed');
    }
  }

  return $scripts_tree['info'];
}

/**
 * Helper function used for executing update scripts.
 *
 * @TODO: Find a way to catch fatal errors.
 */
function update_scripts_run_script($hash) {
  // Load helper files.
  update_scripts_include_helpers();

  // Get update script information.
  $script_info = update_scripts_get_info($hash);

  if (empty($script_info)) {
    drupal_set_message(t('Update script "%hash" could not be found!', array('%hash' => $hash)), 'error');
    return;
  }

  // Start monitoring the errors.
  start_monitoring_errors();

  // Start the time and create log for DB insert.
  $log = array(
    'path' => $script_info['path'],
    'started' => time(),
    'finished' => 0,
    'with_errors' => 0,
    'queries' => array(),
    'errors' => array(),
  );

  // Insert the starting log to database.
  update_scripts_log_save($log);

  // Run the update script code.
  // Update scripts can return arrays of information regarding the ran queries,
  // just like the drupal's hook_update_N.
  $sql_queries = $script_info['function']();

  // Get the timestamp after the script finished running.
  $log['finished'] = time();

  // Stop monitoring the errors.
  stop_monitoring_errors();

  // Provide default value for $result, if the script mistakenly provided none.
  $sql_queries = is_null($sql_queries) ? array() : $sql_queries;

  // Get SQL errors inside the script.
  if (!empty($sql_queries)) {
    foreach ($sql_queries as $query_result) {
      if (!$query_result['success']) {
        $log['queries'][] = 'query: ' . $query_result['query'];
      }
    }
  }

  // Get PHP errors inside the script.
  $log['errors'] = update_scripts_get_errors();
  if (!empty($log['errors'])) {
    $log['with_errors'] = 1;
  }

  // Update log database entry.
  update_scripts_log_save($log);

  return $log;
}

/**
 * Save the log object.
 */
function update_scripts_log_save(&$log) {
  // Update path to a proper format on Windows servers.
  $log['path'] = str_replace('\\', '/', $log['path']);

  if (!isset($log['lid'])) {
    // Insert new log.
    drupal_write_record('update_scripts', $log);
  }
  else {
    // Update log.
    drupal_write_record('update_scripts', $log, array('lid'));
  }
  return $log;
}

/**
 * Returns a list of all log entries.
 */
function update_scripts_get_logs($lid = NULL) {
  $query = db_select('update_scripts', 'us')->fields('us');

  // If a log id was provided retreive only information related to that log.
  if (is_numeric($lid)) {
    $query->condition(':lid', $lid);

    // Run query and fetch results.
    $results = $query->execute()->fetchAll();

    $log = array_shift($results);
    $log->queries = unserialize($log->queries);
    $log->errors = unserialize($log->errors);
    return array($log->lid => $log);
  }

  // Run query and fetch results sorted by log id.
  $results = $query->orderBy('lid', 'DESC')->execute()->fetchAll();

  $logs = array();
  foreach ($results as $log) {
    $logs[$log->lid] = $log;
  }

  return $logs;
}

/**
 * Returns the last log if the script has previously ran.
 */
function _update_scripts_get_last_log_id($path) {
  // Update path to a proper format on Windows servers.
  $path = str_replace('\\', '/', $path);

  $sql = "SELECT lid, with_errors, finished FROM {update_scripts} WHERE path = :path ORDER BY lid DESC";
  $log = db_query($sql, array(':path' => $path))->fetchAssoc();
  return $log;
}

/**
 * Check the status of the update script.
 */
function update_scripts_get_script_status($script) {
  if (empty($script['log']) && !$script['errors']) {
    // Script never ran and does not contain syntax errors.
    return array(
      'code' => UPDATE_SCRIPTS_STATUS_NEW,
      'class' => 'new',
      'text' => 'Did not run.',
    );
  }
  elseif (empty($script['log']) && $script['errors']) {
    // Script contains syntax errors.
    return array(
      'code' => UPDATE_SCRIPTS_STATUS_FATAL,
      'class' => 'error',
      'text' => 'Contains fatal errors!',
    );
  }
  else {
    if ($script['log']['finished'] && !$script['log']['with_errors']) {
      // Script finished successfully.
      return array(
        'code' => UPDATE_SCRIPTS_STATUS_CLEAN,
        'class' => 'status',
        'text' => 'Successful.',
      );
    }
    else {
      // Script finished but with errors.
      return array(
        'code' => UPDATE_SCRIPTS_STATUS_DIRTY,
        'class' => 'warning',
        'text' => 'With errors!',
      );
    }
  }
}

/**
 * Start monitoring errors.
 */
function start_monitoring_errors() {
  global $_update_scripts_monitoring_errors, $_update_scripts_script_errors;
  $_update_scripts_monitoring_errors = TRUE;
  $_update_scripts_script_errors = array();
}

/**
 * Stop monitoring errors.
 */
function stop_monitoring_errors() {
  global $_update_scripts_monitoring_errors;
  $_update_scripts_monitoring_errors = FALSE;
}

/**
 * Return the errors monitored so far.
 */
function update_scripts_get_errors() {
  global $_update_scripts_script_errors;
  return $_update_scripts_script_errors;
}

/**
 * Get phase information from a script or directory path.
 */
function update_scripts_get_phase_info($path = NULL, $phase_id = NULL) {
  $cache = &drupal_static(__FUNCTION__, array('map' => array(), 'info' => array()));

  if (isset($phase_id) && isset($cache['info'][$phase_id])) {
    return $cache['info'][$phase_id];
  }

  // Get information for all stored phases.
  if (empty($path)) {
    return $cache['info'];
  }

  // Extract the script name and parrent directory name.
  $dirname = 'other';
  if ($relative_path = update_scripts_get_relative_path($path)) {
    $relative_path = explode(DIRECTORY_SEPARATOR, $relative_path);
    if (count($relative_path) > 0) {
      $dirname = reset($relative_path);
    }
  }

  // Return phase info from cache.
  if (isset($cache['map'][$dirname])) {
    $pid = $cache['map'][$dirname];
    if (isset($cache['info'][$pid])) {
      return $cache['info'][$pid];
    }
  }

  $phase_id = 'other';
  $phase_title = t('Other scripts');
  switch ($dirname) {
    case 'task':
    case 'environment':
      $phase_id = 'phase_' . $dirname;
      $phase_title = t('@phasename scripts', array('@phasename' => ucfirst($dirname)));
      break;

    default:
      preg_match(UPDATE_SCRIPTS_PHASES_PATTERN, $dirname, $matches);
      if (!empty($matches['phase_id'])) {
        $phase_id = 'phase_' . str_replace('.', '_', $matches['phase_id']);
        $phase_title = t('Development Phase @id', array('@id' => $matches['phase_id']));
      }
      break;
  }

  // Get the phase status.
  $phase_closed = FALSE;
  $closed_phases = variable_get('update_scripts_closed_phases', array());
  if (!empty($closed_phases[$phase_id])) {
    $phase_closed = TRUE;
  }

  // Store the phase information.
  if (!isset($cache['info'][$phase_id])) {
    $cache['map'][$dirname] = $phase_id;
    $cache['info'][$phase_id] = array(
      '#closed' => $phase_closed,
      '#id' => $phase_id,
      '#title' => $phase_title,
    );
  }

  return $cache['info'][$phase_id];
}
