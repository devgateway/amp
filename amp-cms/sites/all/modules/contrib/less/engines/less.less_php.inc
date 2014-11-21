<?php

/**
 * @file
 *   Libraries integration file for 'less.php' library.
 */


/**
 * Processes files using 'oyejorge/less.php'.
 */
function _less_less_php($input_file, $less_settings) {
  
  $output_data = NULL;
  $error = NULL;
  
  $parser = new Less_Parser();
  
  $parser->ModifyVars($less_settings['variables']);
  
  $import_dirs = array();
  
  // Less.js does not allow path aliasing. Set aliases to blank for consistency.
  foreach ($less_settings['paths'] as $path) {
    $import_dirs[$path] = '';
  }
  
  $parser->SetImportDirs($import_dirs);
  
  // Try to capture errors.
  try {
    
    if ($less_settings['less_devel']) {
      
      $parser->SetOption('sourceMap', TRUE);
      // $parser->SetOption('outputSourceFiles', TRUE); // Might not need this.
      $parser->SetOption('sourceMapRootpath', '/'); // @link https://github.com/oyejorge/less.php/issues/127
      $parser->SetOption('sourceMapBasepath', DRUPAL_ROOT); // @link https://github.com/oyejorge/less.php/issues/127
    }

    $parser->parseFile($input_file);
    
    $output_data = $parser->getCss();
    
    // Dependencies list is not populated until after Less_Parser::getCss().
    if ($less_settings['less_devel']) {
      
      $dependencies = $parser->AllParsedFiles();
      
      _less_cache_dependencies($input_file, $dependencies);
    }
  }
  catch (Exception $e) {
    
    $error = $e->getMessage();
  }
  
  return array($output_data, $error);
}
