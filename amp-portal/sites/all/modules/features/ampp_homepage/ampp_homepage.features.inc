<?php
/**
 * @file
 * ampp_homepage.features.inc
 */

/**
 * Implements hook_ctools_plugin_api().
 */
function ampp_homepage_ctools_plugin_api() {
  list($module, $api) = func_get_args();
  if ($module == "page_manager" && $api == "pages_default") {
    return array("version" => "1");
  }
  list($module, $api) = func_get_args();
  if ($module == "strongarm" && $api == "strongarm") {
    return array("version" => "1");
  }
}

/**
 * Implements hook_views_api().
 */
function ampp_homepage_views_api() {
  list($module, $api) = func_get_args();
  if ($module == "views" && $api == "views_default") {
    return array("version" => "3.0");
  }
}

function ampp_homepage_preprocess_html(&$variables) {
  if(drupal_is_front_page())
  {
  	$variables['classes_array'][] = "page-homepage";
  }
}
