<?php

/**
 * @file
 * Update Scripts helper functions for the Panels module.
 */

/**
 * Enable panel page.
 */
function _us_panels__enable_page($page_name) {
  // Load the page manager cached object.
  $page = page_manager_cache_load($page_name);
  if ($function = ctools_plugin_get_function($page->subtask, 'enable callback')) {
    $result = $function($page, FALSE);
    menu_rebuild();

    // We want to re-cache this if it's changed so that status is properly
    // updated on the changed form.
    if (!empty($page->changed)) {
      page_manager_set_page_cache($page);
    }
  }
}
