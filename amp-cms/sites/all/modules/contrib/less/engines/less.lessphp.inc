<?php

/**
 * @file
 * Libraries integration file for 'lessphp' library.
 */


/**
 * Process files using 'leafo/lessphp'.
 */
function _less_lessphp($input_file, $less_settings) {
  
  $output_data = NULL;
  $error = NULL;
  
  $less = new lessc();
  
  if (method_exists($less, 'registerFunction') && is_array($less_settings['functions'])) {
    foreach ($less_settings['functions'] as $funcion => $callback) {
      $less->registerFunction($funcion, $callback);
    }
  }
  
  if (method_exists($less, 'setVariables')) {
    $less->setVariables($less_settings['variables']);
  }
  
  // @TODO fix this, this should be working with legacy library; import paths
  // LESS variables compatibility for less.php alternate library.
  if (class_exists('Less_Version')) {
    
    // Make so imports work from original file directory.
    $less->addImportDir(dirname($input_file));
  }
  
  // Try to capture errors.
  try {
    
    if ($less_settings['less_devel']) {
      
      $less->setPreserveComments(TRUE);
      $cache = $less->cachedCompile($input_file);
      
      _less_cache_dependencies($input_file, $cache['files']);
      
      $output_data = $cache['compiled'];
    }
    else {
      
      $output_data = $less->compileFile($input_file);
    }
  }
  catch (Exception $e) {
    
    $error = $e->getMessage();
  }
  
  return array($output_data, $error);
}
