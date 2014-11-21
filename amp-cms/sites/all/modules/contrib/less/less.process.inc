<?php

/**
 * @file
 *   Contains functions related to compiling .less files.
 */

function _less_process_file(&$info) {
  
  $input_file = $info['data'];
  
  $less_devel = variable_get('less_devel', FALSE);
  $less_dir = _less_get_dir();
  
  $less_path = 'public://less/' . $less_dir;
  
  $file_uri = file_uri_target($input_file);
  $css_path = $less_path . '/' . dirname($file_uri ? $file_uri : $input_file);
  
  // Ensure the destination directory exists.
  if (!_less_ensure_directory($css_path)) {
    // Cancel the for each loop when directory doesn't exist.
    return;
  }

  $output_file = $css_path . '/' . basename($input_file, '.less');
  // Correct file names of files not following the .css.less naming convention
  if (drupal_substr($output_file, -4) != '.css') {
    $output_file .= '.css';
  }
  
  $less_settings = less_get_settings(_less_file_owner($input_file));
  
  if (!empty($info['less'])) {
    $less_settings = drupal_array_merge_deep($less_settings, (array) $info['less']);
  }
  
  $less_settings['paths'] = array_unique($less_settings['paths']);
  
  $less_settings['less_devel'] = $less_devel;
  
  // array_multisort() the data so that the hash returns the same hash regardless order of data.
  array_multisort($less_settings);
  
  // json_encode() is used because serialize() throws an error with lambda functions.
  $output_file = substr_replace($output_file, drupal_hash_base64(json_encode($less_settings)) . '.css', -3);
  
  $rebuild = FALSE;
  
  // Set $rebuild if this file or its children have been modified.
  if ($less_devel && is_file($output_file)) {
    $output_file_mtime = filemtime($output_file);
    
    if ($less_file_cache = cache_get('less:devel:' . drupal_hash_base64($input_file))) {
      
      // Iterate over each file and check if there are any changes.
      foreach ($less_file_cache->data as $filepath => $filemtime) {
        
        // Only rebuild if there has been a change to a file.
        if (is_file($filepath) && filemtime($filepath) > $filemtime) {
          $rebuild = TRUE;
          break;
        }
      }
    }
    else {
      
      // No cache data, force a rebuild for later comparison.
      $rebuild = TRUE;
    }
  }
  
  // $output_file doesn't exist or is flagged for rebuild.
  if (!is_file($output_file) || $rebuild) {
    
    $output_data = NULL;
    $error = NULL;
    
    switch (_less_inc()) {
      case 'less.php':
        list($output_data, $error) = _less_less_php($input_file, $less_settings);
        break;
      case 'lessphp':
        list($output_data, $error) = _less_lessphp($input_file, $less_settings);
        break;
      case 'less.js':
        list($output_data, $error) = _less_lessjs($input_file, $less_settings);
        break;
      default:
        $error = t('No engine found.');
        break;
    }
    
    if ($error) {
      
      $message_vars = array('@message' => $error, '%input_file' => $input_file);
      
      watchdog('LESS', 'LESS error: @message, %input_file', $message_vars, WATCHDOG_ERROR);
      
      if (user_access(LESS_PERMISSION)) {
        drupal_set_message(t('LESS error: @message, %input_file', $message_vars), 'error');
      }
    }
    
    if (isset($output_data)) {
      
      // Fix paths for images as .css is in different location.
      $output_data = _less_rewrite_paths($input_file, $output_data);
      
      file_unmanaged_save_data($output_data, $output_file, FILE_EXISTS_REPLACE);
    }

  }

  if (is_file($output_file)) {
    
    // Set render path of the stylesheet to the compiled output.
    $info['data'] = $output_file;
    
    if ($less_devel) {
      
      global $theme;
      
      $less_watch_cache = $info;
      
      $less_watch_cache['less'] = $less_settings;
      $less_watch_cache['data'] = $input_file;
      $less_watch_cache['output_file'] = $output_file;
      $less_watch_cache['theme'] = $theme;

      cache_set('less:watch:' . drupal_hash_base64(file_create_url($output_file)), $less_watch_cache);
      
      // 'preprocess' being FALSE generates a discreet <link /> rather than an @import.
      $info['preprocess'] = FALSE;
    }
  }

}

/**
 * Normalize keeping track of changed files.
 * 
 * @param string $input_file
 *   Path of source file.
 * @param array $dependencies
 *   Array of files that are @import'ed in $input_file, recursively.
 */
function _less_cache_dependencies($input_file, $dependencies = array()) {
  
  // Add $input_file to $dependencies as it is not in return from engines.
  $dependencies = array_merge(array($input_file), (array) $dependencies);
  
  $watched_files = array();
  
  foreach ($dependencies as $dependency) {
    
    // Full path on file should enforce uniqueness in associative array.
    $watched_files[realpath($dependency)] = filemtime($dependency);
  }
  
  cache_set('less:devel:' . drupal_hash_base64($input_file), $watched_files);
}
