<?php
// $Id: template.preprocess.views.php,v 1.2 2012/01/25 04:12:59 vamirbekyan Exp $


/**
 * Implements template_preprocess_views_view_unformatted().
 */
function ampp_preprocess_views_view_unformatted(&$vars) {
  $function = '__' . __FUNCTION__ . '__' . $vars['view']->name;
  if (function_exists($function)) {
    $function($vars);
  }
}


/**
 * Implements template_preprocess_views_slideshow().
 */
function ampp_preprocess_views_slideshow(&$vars) {
  $function = '__' . __FUNCTION__ . '__' . $vars['view']->name;
  if (function_exists($function)) {
    $function($vars);
  }
}


/**
 * Implements template_preprocess_views_view_fields().
 */
function ampp_preprocess_views_view_fields(&$vars) {
  $function = '__' . __FUNCTION__ . '__' . $vars['view']->name;
  if (function_exists($function)) {
    $function($vars);
  }
}