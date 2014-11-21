<?php

/**
 * @file
 *  Contains page callback for LESS watch functionality.
 */

/**
 * Page callback for 'ajax/less/watch'.
 * 
 * Handles AJAX requests to check for changes to files while in developer mode.
 */
function _less_watch() {
  
  drupal_page_is_cacheable(FALSE);
  
  $changed_files = array();
  
  if (variable_get('less_watch', FALSE)) {
    
    $files = (isset($_POST['less_files']) && is_array($_POST['less_files'])) ? $_POST['less_files'] : array();
    
    foreach ($files as $file) {
      
      $file_url_parts = drupal_parse_url($file);
      
      if ($cache = cache_get('less:watch:' . drupal_hash_base64($file_url_parts['path']))) {
        $cached_data = $cache->data;
        
        $current_mtime = filemtime($cached_data['output_file']);
        
        $styles = array(
          '#items' => array(
            $cached_data['data'] => $cached_data,
          ),
        );
        
        global $theme;
        
        $theme = $cached_data['theme'];
        
        $styles = _less_pre_render($styles);
        
        if (filemtime($styles['#items'][$cached_data['data']]['data']) > $current_mtime) {
          $changed_files[] = array(
            'old_file' => $file_url_parts['path'],
            'new_file' => file_create_url($styles['#items'][$cached_data['data']]['data']),
          );
        }
      }
    }
  }
  
  return $changed_files;
}

