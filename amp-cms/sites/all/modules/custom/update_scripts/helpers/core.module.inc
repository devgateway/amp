<?php

/**
 * @file
 * Update Scripts helper functions for drupal core.
 */

/**
 * Install a list of modules.
 */
function _us_module__install($module_names, $action = 'install') {
  if (is_string($module_names)) {
    $module_names = array($module_names);
  }

  // Make sure the install API is available.
  include_once DRUPAL_ROOT . '/includes/install.inc';

  $results = array();

  if (empty($module_names)) {
    return $results;
  }

  // Collect data for all modules to be able to determine dependencies.
  $files = system_rebuild_module_data();

  // Remove hidden modules from display list.
  $visible_files = $files;
  foreach ($visible_files as $filename => $file) {
    if (!empty($file->info['hidden'])) {
      unset($visible_files[$filename]);
    }
  }

  module_load_include('inc', 'system', 'system.admin');
  uasort($visible_files, 'system_sort_modules_by_info_name');

  // Iterate through each of the modules.
  foreach ($visible_files as $filename => $module) {
    if ($action == 'install') {
      $modules[$module->name]['enabled'] = (in_array($module->name, $module_names)) ? TRUE : (bool) $module->status;
    }
    else {
      $modules[$module->name]['enabled'] = (in_array($module->name, $module_names)) ? FALSE : (bool) $module->status;
    }
  }

  // Sorts modules by weight.
  $sort = array();
  foreach ($modules as $name => $info) {
    $sort[$name] = $files[$name]->sort;
  }
  array_multisort($sort, $modules);

  // Makes sure all required modules are set to be enabled.
  $more_required = array();
  $missing_modules = array();
  foreach ($modules as $name => $module) {
    if ($module['enabled']) {
      // Checks that all dependencies are set to be enabled.  Stores the ones
      // that are not in $dependencies variable so that the user can be alerted
      // in the confirmation form that more modules need to be enabled.
      $dependencies = array();
      foreach (array_keys($files[$name]->requires) as $required) {
        if (empty($modules[$required]['enabled'])) {
          if (isset($files[$required])) {
            $dependencies[] = $files[$required]->info['name'];
            $modules[$required]['enabled'] = TRUE;
          }
          else {
            $missing_modules[$required]['depends'][] = $name;
            $modules[$name]['enabled'] = FALSE;
          }
        }
      }

      // Stores additional modules that need to be enabled in $more_required.
      if (!empty($dependencies)) {
        $more_required[$name] = array(
          'name' => $files[$name]->info['name'],
          'requires' => $dependencies,
        );
      }
    }
  }

  if (!empty($missing_modules)) {
    $variables = array(
      'title' => 'Missing modules: ',
      'items' => array_keys($missing_modules),
      'type' => 'ol',
      'attributes' => array(),
    );
    $list = theme('item_list', $variables);
    watchdog('debug-update-script', $list);

    drupal_set_message(t('Module(s) could not be enabled due to missing dependencies.'), 'error');
    return;
  }

  // Invokes hook_requirements('install').  If failures are detected, makes sure
  // the dependent modules aren't installed either.
  foreach ($modules as $name => $module) {
    // Invoke hook_requirements only on modules that are going to be installed.
    if ($module['enabled'] && drupal_get_installed_schema_version($name) == SCHEMA_UNINSTALLED) {
      if (!drupal_check_module($name)) {
        $modules[$name]['enabled'] = FALSE;
        foreach (array_keys($files[$name]->required_by) as $required_by) {
          $modules[$required_by]['enabled'] = FALSE;
        }
      }
    }
  }

  $actions = array(
    'enable' => array(),
    'disable' => array(),
    'install' => array(),
  );

  // Builds arrays of modules that need to be enabled, disabled, and installed.
  foreach ($modules as $name => $module) {
    if ($module['enabled']) {
      if (drupal_get_installed_schema_version($name) == SCHEMA_UNINSTALLED) {
        $actions['install'][] = $name;
        $actions['enable'][] = $name;
      }
      elseif (!module_exists($name)) {
        $actions['enable'][] = $name;
      }
    }
    elseif (module_exists($name)) {
      $actions['disable'][] = $name;
    }
  }

  // Gets list of modules prior to install process.
  $pre_install_list = module_list();

  // Reverse the 'enable' list, to order dependencies before dependents.
  krsort($actions['enable']);

  // Installs, enables, and disables modules.
  module_enable($actions['enable'], FALSE);
  module_disable($actions['disable'], FALSE);

  // Gets module list after install process, flushes caches and displays a
  // message if there are changes.
  $post_install_list = module_list(TRUE);
  if ($pre_install_list != $post_install_list) {
    drupal_flush_all_caches();
    drupal_set_message(t('The configuration options have been saved.'));
  }

  // Notify locale module about module changes, so translations can be
  // imported. This might start a batch, and only return to the redirect
  // path after that.
  module_invoke('locale', 'system_update', $actions['install']);
}

/**
 * Disable a list of modules.
 */
function _us_module__disable($module_names) {
  _us_module__install($module_names, 'disable');
}

/**
 * Uninstall a list of modules.
 */
function _us_module__uninstall($module_names) {
  if (is_string($module_names)) {
    $module_names = array($module_names);
  }

  // Disable modules before uninstalling them.
  _us_module__disable($module_names);

  // Uninstall modules.
  drupal_uninstall_modules($module_names);
}
