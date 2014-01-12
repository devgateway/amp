<?php

/**
 * @file
 * Main functions for the Update Scripts module.
 */

/**
 * Returns TRUE if we want to be protected against automated update.php runs.
 * TODO: Improve and enable support for update.php.
 */
function protect_against_updatephp_run() {
  return TRUE;
//  return variable_get('update_scripts_no_auto', FALSE);
}

/**
 * Autodetect the helper scripts
 */
function autodetect_helper_scripts() {
  static $helper_scripts_detected = FALSE;

  // Prevent auto-detection of helper scripts more than once per HTTP request
  if ($helper_scripts_detected) {
    return;
  }

  // Include helper scripts.
  $directory = drupal_get_path('module', 'update_scripts');

  // This elegant approach taken from: http://us3.php.net/manual/en/function.require-once.php#92566
  $files = glob($directory . '/helpers/' . UPDATE_SCRIPTS_HELPER_PATTERN);
  array_walk($files, create_function('$v, $i', 'return require_once($v);'));

  // Mark the fact that the scripts have been detected.
  $helper_scripts_detected = TRUE;
}

/**
 * Autodetect the update scripts.
 */
function autodetect_update_scripts() {
  static $_update_scripts_scripts_detected = FALSE;

  // Prevent auto-detection of update scripts more than once per HTTP request
  if ($_update_scripts_scripts_detected) {
    return;
  }

  // Detect update scripts.
  $directory = variable_get('update_scripts_directory', conf_path() . '/update_scripts');
  if (!is_dir($directory)) {
    return;
  }

  // Scan for update scripts.
  $directories = glob($directory . '/*', GLOB_ONLYDIR);
  array_walk($directories, create_function('$v, $i', '_update_scripts_scan_folder_for_scripts($v);'));

  // Prevent running of any script using update.php if the 'update_scripts_no_auto' variable is set.
  if (protect_against_updatephp_run()) {
    // Include the file with the API to set the schema version of a module
    require_once('includes/install.inc');

    // Here, _update_scripts_global_script_id() increments the ID once more, so that no script can be run with update.php
    drupal_set_installed_schema_version('update_scripts', _update_scripts_global_script_id());
  }

  // Mark the fact that the scripts have been detected.
  $_update_scripts_scripts_detected = TRUE;
}

/**
 * Function for recursively processing directory includes.
 */
function _update_scripts_scan_folder_for_scripts($directory) {
  // Recurse to child directories.
  $directories = glob($directory . '/*', GLOB_ONLYDIR);
  array_walk($directories, create_function('$v, $i', 'return _update_scripts_scan_folder_for_scripts($v);'));

  // Include scripts in current directory.
  $files = glob($directory . '/' . UPDATE_SCRIPTS_SCRIPT_PATTERN);
  array_walk($files, create_function('$v, $i', 'return _update_scripts_require_script($v, _update_scripts_global_script_id());'));
}

/**
 * Similar to PHP require directive. A few notable differences:
 *  - Wraps the script in a function.
 *  - Inserts code for recording start & end time at the beginning & end of the function.
 *  - Inserts code for recording this execution in the module schema.
 */
function _update_scripts_require_script($path, $global_script_id) {
  global $_update_scripts_script_info;

  // Retrieve script contents
  $script_contents = _update_scripts_extract_script_contents($path);

  // Get relative path to script
  $relative_path = _update_scripts_get_relative_path($path);

  // Store mapping from global script ID's to their paths
  $_update_scripts_script_info[$global_script_id] = array(
    'path' => $relative_path,
    'errors' => FALSE,
  );

  // Wrap the script contents into a function.
  $code = "function _update_scripts_script_$global_script_id() { $script_contents } ";
  $run_script_code = "return _update_scripts_run_script($global_script_id, '$relative_path');";

  // Generate code compatible with update.php
  $code .= "function update_scripts_update_$global_script_id() { $run_script_code } ";

  // Start buffering the output.
  ob_start();

  // Create the update functions.
  eval($code);

  // Output buffer should be empty because we are just creating 2 function.
  $errors = ob_get_clean();

  // If Output buffer is not empty then we have PHP errors in the script code.
  if (!empty($errors)) {
    // Log found PHP error to watchdog.
    $message = "PHP error inside update script: !path !errors";
    $args = array(
      '!path' => $relative_path,
      '!errors' => $errors,
    );
    watchdog('php', $message, $args, WATCHDOG_ERROR);

    // Get the previously inserted watchdog entry.
    // TODO: change... could cause problems.
    $wid = db_query("SELECT wid FROM {watchdog} ORDER BY wid DESC LIMIT 1")->fetchField();

    // Display error to the user
    $message = 'PHP error inside update script: !path';
    $args = array(
      '!path' => l($relative_path, 'admin/reports/event/' . $wid),
    );
    drupal_set_message(t($message, $args), 'error');

    $_update_scripts_script_info[$global_script_id]['errors'] = $wid;
  }
}

/**
 * Returns the current global script id.
 */
function _update_scripts_global_script_id() {
  static $global_script_id = 0;
  return ++$global_script_id;
}

/**
 * Extracts a PHP script's contents, given its path.
 * Gets rid of <?php and ?> and the start & end of the file, if they exist.
 * This shouldn't be used on large files, because it reads the whole file in memory.
 */
function _update_scripts_extract_script_contents($path) {
  // Get file contents.
  $contents = file_get_contents($path);

  // Sanity check.
  if ($contents === FALSE) {
    return;
  }

  // Initial trim
  $contents = trim($contents);

  // Trim PHP start tag, if any
  if (strpos($contents, UPDATE_SCRIPTS_SCRIPT_START) === 0) {
    $contents = drupal_substr($contents, drupal_strlen(UPDATE_SCRIPTS_SCRIPT_START));
  }

  // Trim PHP end tag, if any. We need PHP 5.0+ for this
  if (strrpos($contents, UPDATE_SCRIPTS_SCRIPT_END) === drupal_strlen($contents) - drupal_strlen(UPDATE_SCRIPTS_SCRIPT_END)) {
    $contents = drupal_substr($contents, 0, drupal_strlen($contents) - drupal_strlen(UPDATE_SCRIPTS_SCRIPT_END));
  }

  // Trim the code again, and add a newline at the end. We want to generate beautiful code.
  return trim($contents) . "\n";
}

/**
 * Get relative script path.
 */
function _update_scripts_get_relative_path($path) {
  $directory = variable_get('update_scripts_directory', conf_path() . '/update_scripts');
  if (strrpos($path, $directory) === 0) {
    return drupal_substr($path, drupal_strlen($directory) + 1);
  }
  else {
    return NULL;
  }
}

/**
 * Returns true if the script has previously ran.
 */
function _update_scripts_get_last_log_id($path) {
  $sql = "SELECT lid, with_errors, finished FROM {update_scripts} WHERE path = :path ORDER BY lid DESC";
  $log = db_query($sql, array(':path' => $path))->fetchAssoc();
  return $log;
}

/**
 * Helper function used for executing update hooks.
 */
function _update_scripts_run_script($global_script_id, $relative_path) {
  // Fix command-line bootstraping issues.
  refresh_module_metadata();

  // Create log for DB insert.
  $log = array(
    'path' => $relative_path,
    'started' => time(),
    'finished' => 0,
    'with_errors' => 0,
    'queries' => array(),
    'errors' => array(),
  );

  // Insert the log to database.
  update_scripts_log_save($log);

  // Start the timer.
  //$log['started'] = microtime(true);
  $log['started'] = time();

  // Start monitoring the errors.
  start_monitoring_errors();

  // Call the helper function.
  $helper_function = "_update_scripts_script_$global_script_id";
  $sql_queries = $helper_function();

  // Stop timer.
  //$log['finished'] = microtime(true);
  $log['finished'] = time();

  // Stop monitoring the errors
  stop_monitoring_errors();

  // Provide default value for $result, if the script mistakenly provided none.
  $sql_queries = is_null($sql_queries) ? array() : $sql_queries;

  // Get SQL errors inside the script.
  if (!empty($sql_queries)) foreach ($sql_queries as $query_result) {
    if (!$query_result['success']) {
      $log['queries'][] = 'query: ' . $query_result['query'];
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
 * Correctly bootstraps the module list and schema. Required for
 *   drupal_write_record() to work correctly.
 */
function refresh_module_metadata() {
  // Refresh the module list to exclude the disabled modules.
  module_list(TRUE, FALSE);

  // Force to regenerate the stored list of hook implementations.
  module_implements('', FALSE, TRUE);

  // Regenerate schema
  drupal_get_schema(NULL, TRUE);
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
 * Save the log object.
 */
function update_scripts_log_save(&$log) {
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
  $query = db_select('update_scripts', 'us');

  if (is_numeric($lid)) {
    $query->fields('us')->condition(':lid', $lid);
  }
  else {
    $query->fields('us', array('lid', 'path', 'started', 'finished', 'with_errors'));
  }

  $results = $query->execute()->fetchAll();

  if (is_numeric($lid)) {
    $log = (array) array_shift($results);
    $log['queries'] = unserialize($log['queries']);
    $log['errors'] = unserialize($log['errors']);
    return $log;
  }

  $logs = array();
  foreach ($results as $log) {
    $logs[$log->lid] = (array) $log;
  }

  return $logs;
}

/**
 * Returns a single log entry.
 */
function update_scripts_get_log($lid) {
  return update_scripts_get_logs($lid);
}

