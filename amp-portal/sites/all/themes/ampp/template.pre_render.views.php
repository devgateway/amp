<?php
// $Id: template.pre_render.views.php,v 1.4 2013/11/01 20:13:42 vamirbekyan Exp $


/**
 * Implements hook_views_pre_render().
 */
function ampp_views_pre_render(&$view) {
  $function = '__' . __FUNCTION__ . '__' . $view->name;
  if (function_exists($function)) {
    $function($view);
  }
}

/**
 * Custom function for slideshows view
 */
function __ampp_views_pre_render__homepage_slideshow(&$view) {
  switch ($view->current_display) {
    case 'panel_pane_5' :
      foreach($view->result as $result) {
        if (isset($result->_field_data['nid']['entity']->field_show_title_in_slideshow['und']) && $result->_field_data['nid']['entity']->field_show_title_in_slideshow['und']['0']['value'] == 0) {
          $result->node_title = '';
        };
      }
      break;
  }
}


/**
 * Custom function for slideshows view
 */
function __ampp_views_pre_render__slideshows(&$view) {
  switch ($view->current_display) {
    case 'block_1' :
      if ($view->result['0']->_field_data['nid']['entity']->field_show_title_in_slideshow['und']['0']['value'] == 0) {
        $view->result['0']->node_title = '';
      };
      break;
  }
}
