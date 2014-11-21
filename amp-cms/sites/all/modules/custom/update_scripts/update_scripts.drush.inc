<?php

/**
 * @file
 * Update Scripts module drush integration.
 *
 * @TODO:
 *  - Exit when there is a script that contains syntax errors while executing 'drush update-scripts-run'!
 *  - Merge drush and UI run functionality.
 */

/**
 * Implements hook_drush_help().
 */
function update_scripts_drush_help($section) {
  switch ($section) {
    case 'drush:update-scripts':
      return dt("List all the update scripts for your site and show the status for each of them.");

    case 'drush:update-scripts-run':
      return dt("Update Scripts module is used for continuous integration development and micro-releases. It can be used to propagate features and database changes from a development machine to a staging or production environment and also to other development environments. Use this command to run those update scripts via a command line interface.");
  }
}

/**
 * Implements hook_drush_command().
 */
function update_scripts_drush_command() {
  $items = array();

  // Define command for drush_update_scripts_list()
  $items['update-scripts-list'] = array(
    'description' => "Show a list of update scripts in your site, including status.",
    'bootstrap' => DRUSH_BOOTSTRAP_DRUPAL_DATABASE,
    'drupal dependencies' => array('update_scripts'),
    'aliases' => array('usl'),
  );

  // Define command for drush_update_scripts_run()
  $items['update-scripts-run'] = array(
    'description' => "Run one or more update scripts.",
    'bootstrap' => DRUSH_BOOTSTRAP_DRUPAL_DATABASE,
    'arguments' => array(
      'scripts' => 'A space delimited list of update scripts to run. No option should be passed when a list of scripts is provided!',
    ),
    'options' => array(
      'e'   => 'Same as \'--env-only\'',
      'env-only' => 'Run only environment specific update script.',
      'rs'   => 'Same as \'--run\'',
      'run' => 'Run update scripts, omitting the environment specific script.',
      'ra'   => 'Same as \'--run-all\'',
      'run-all' => 'Run all update scripts, including the environment specific script. Passing \'all\' as an argument is also accepted for backwards compatibility!',
    ),
    'examples' => array(
      'drush update-scripts-run --run-all -y' => 'Run all update scripts including environment specific ones and skip user confirmation.',
      'drush usr --env-only' => 'Run environment specific update scripts.',
      'drush usr p1.0/update.0016.php' => 'Run update scripts 16.',
      'drush usr update.production.php' => 'Change the current environment settings to match a production environment.',
    ),
    'drupal dependencies' => array('update_scripts'),
    'aliases' => array('usr'),
  );

  return $items;
}

/**
 * Get a list of all feature modules.
 */
function drush_update_scripts_list() {
  module_load_include('inc', 'update_scripts');
  update_scripts_include_scripts();

  // Create a table header for the results.
  $table_header = array(
    dt('Path'),
    dt('Run status'),
  );

  // Get a list of update scripts.
  $update_scripts = update_scripts_get_info(NULL, FALSE);

  if (empty($update_scripts)) {
    $args = array(
      '@path' => update_scripts_get_directory(),
    );
    $message = t('No update scripts detected in @path. Please check settings page and make sure that you respect the update scripts name convention.', $args);
    drush_print($message);
  }

  foreach (update_scripts_get_phase_info() as $phase_id => $phase_info) {
    if (!empty($update_scripts[$phase_id])) {
      $table = array();

      // The first table row is the header.
      $table[] = array(
        dt('Path'),
        dt('Run status'),
      );

      foreach ($update_scripts[$phase_id] as $hash => &$script_info) {
        // Check the status of the update script.
        $status = update_scripts_get_script_status($script_info);

        // Append update script info.
        $table[] = array(
          $script_info['path'],
          dt($status['text']),
        );
      }

      // Print the phase title and table.
      drush_print(PHP_EOL . $phase_info['#title']);
      drush_print_table($table, TRUE);
    }
  }

}

/**
 * Revert a feature to it's code definition.
 */
function drush_update_scripts_run() {
  // Store the scripts queue.
  $scripts_run_queue = array();
  $extra_scripts = array();

  // Get the arguments.
  $argument_list = func_get_args();

  if (count($argument_list) == 1 && reset($argument_list) == 'all') {
    // Passing 'all' in the scripts array is supported for backward compatibility.
    drush_set_option('run-all', TRUE);
    // Remove 'all' from the argument list.
    array_shift($argument_list);
  }
  elseif (count($argument_list) >= 1) {
    // Run the list of scripts provided as parameter.
    drush_set_option('run-list', TRUE);
  }

  // Load the update scripts.
  module_load_include('inc', 'update_scripts');
  update_scripts_include_scripts();

  // Determine the action to take. Ignore the action option if a list of update scripts is provided.
  $action = NULL;
  if (drush_get_option(array('run-list'))) {
    // Run only the provided list of update script.
    $action = 'run-list';
  }
  elseif (drush_get_option(array('e', 'env-only'))) {
    // Run only environment specific update script.
    $action = 'env';
  }
  elseif (drush_get_option(array('rs', 'run'))) {
    // Run update scripts, omitting the environment specific script.
    $action = 'run';
  }
  elseif (drush_get_option(array('ra', 'run-all'))) {
    // Run all update scripts, including the environment specific script.
    $action = 'run-all';
  }
  else {
    // Display a warning text and exit if no action is provided.
    drush_log(dt('Nothing to do, see "drush help update-scripts-run"'), 'warning');
    return;
  }

  // Get a list of update scripts.
  $update_scripts = update_scripts_get_info(NULL, FALSE);

  if (empty($update_scripts)) {
    $args = array(
      '@path' => update_scripts_get_directory(),
    );
    $message = t('No update scripts detected in @path. Please check settings page and make sure that you respect the update scripts name convention.', $args);
    drush_print($message);
  }

  switch ($action) {
    case 'run-all':
    case 'run':
      foreach (array_keys($update_scripts) as $phase_id) {
        foreach ($update_scripts[$phase_id] as $hash => &$script_info) {
          if ($script_info['errors']) {
            continue;
          }

          // Environment specific update scripts need to run last.
          if ($script_info['phase_id'] == 'phase_environment') {
            continue;
          }

          // We don't run task specific update scripts in a batch.
          if ($script_info['phase_id'] == 'phase_task') {
            continue;
          }

          // Only add scripts that did not run before.
          if (!empty($script_info['log'])) {
            continue;
          }

          // Add the script to the end of the run queue.
          $scripts_run_queue[] = $script_info;
        }
      }

      // Make sure environment update scripts are being run when necessary.
      if ($action != 'run-all') {
        break;
      }

    case 'env':
      foreach (array_keys($update_scripts) as $phase_id) {
        foreach ($update_scripts[$phase_id] as $hash => &$script_info) {
          if ($script_info['errors']) {
            continue;
          }

          // Skip over normal update scripts.
          if ($script_info['phase_id'] != 'phase_environment') {
            continue;
          }

          // Run the environment script only for the current PROJECT_ENVIRONMENT.
          // @TODO: Find a better way to determine the exact environment script.
          if ($script_info['path'] != 'environment/setup.' . PROJECT_ENVIRONMENT . '.php') {
            continue;
          }

          // Add the script to the end of the run queue.
          $extra_scripts[] = $script_info;
        }
      }
      break;

    case 'run-list':
      foreach (array_keys($update_scripts) as $phase_id) {
        foreach ($update_scripts[$phase_id] as $hash => &$script_info) {
          if ($script_info['errors']) {
            continue;
          }

          // Only add to the run queue scripts that were provided in the list of arguments.
          if (!in_array($script_info['path'], $argument_list)) {
            continue;
          }

          // Add the script to the end of the run queue.
          // The order is set when moving the scripts to the $scripts_run_queue.
          $extra_scripts[] = $script_info;
        }
      }
      break;
  }

  if ($action == 'run-list') {
    // Add the list of scripts provided as arguments in the exact order.
    foreach ($argument_list as $script_path) {
      foreach ($extra_scripts as $script_info) {
        if ($script_info['path'] == $script_path) {
          $scripts_run_queue[] = $script_info;
        }
      }
    }
  }
  else {
    // The environment specific update script need to run last.
    $scripts_run_queue = array_merge($scripts_run_queue, $extra_scripts);
  }

  /*
   * Do some update scripts magic.
   *
   * We do not stop here since we need to be able to revert the features and
   * clear the caches when there are no scripts to run.
   */
  if (!empty($scripts_run_queue)) {
    // Display a list of queued scripts.
    drush_print(dt('The following update scripts will run:'));
    foreach ($scripts_run_queue as $script_info) {
      drush_print($script_info['path'], 2);
    }

    // Ask for confirmation and abort if no confirmation not received.
    if (!drush_confirm(dt('Do you really want to continue?'))) {
      return drush_user_abort();
    }

    // Run one script at a time.
    foreach ($scripts_run_queue as $script_info) {
      $log = update_scripts_run_script($script_info['hash']);
      if ($log['finished'] && !$log['with_errors']) {
        drush_log(dt('!script finished runing.', array('!script' => $script_info['path'])), 'success');
      }
      elseif ($log['finished'] && $log['with_errors']) {
        drush_log(dt('!script finished runing with errors.', array('!script' => $script_info['path'])), 'error');
      }
      else {
        drush_log(dt('!script did not run.', array('!script' => $script_info['path'])), 'error');
      }
    }
  }

  /*
   * Do not revert features or clear the caches for local environments.
   */
  if (PROJECT_ENVIRONMENT == 'local') {
    drush_print(dt('Skiping "Auto revert features" and "Auto clear cache".'));
  }
  else {
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
        _us_features__revert_all();
        drush_print(dt('All features have been reverted.'));
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
        drupal_flush_all_caches();
        drush_print(dt('All caches have been cleared.'));
        break;

      default:
        // Nothing to do!
        break;
    }
  }

  // Display message if there are no script in the run queue.
  if (empty($scripts_run_queue)) {
    return drush_log(dt('There were no update scripts to run.'), 'ok');
  }
}
