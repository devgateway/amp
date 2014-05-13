<?php

/**
 * Implements template_preprocess_views_view_unformatted().
 */
function ampp_preprocess_views_view_unformatted(&$variables) {
  $function = '__' . __FUNCTION__ . '__' . $variables['view']->name;
  if (function_exists($function)) {
    $function($variables);
  }
}

/**
 * Implements template_preprocess_views_slideshow().
 */
function ampp_preprocess_views_slideshow(&$variables) {
  $function = '__' . __FUNCTION__ . '__' . $variables['view']->name;
  if (function_exists($function)) {
    $function($variables);
  }
}

/**
 * Implements template_preprocess_views_view_fields().
 */
function ampp_preprocess_views_view_fields(&$variables) {
  $function = '__' . __FUNCTION__ . '__' . $variables['view']->name;
  if (function_exists($function)) {
    $function($variables);
  }
}

/**
 * Implements template_preprocess_views_view_table().
 */
function ampp_preprocess_views_view_table(&$variables) {
  $function = '__' . __FUNCTION__ . '__' . $variables['view']->name;
  if (function_exists($function)) {
    $function($variables);
  }
}

/*******************************************************************************
 * Helper functions for custom template_preprocess_HOOK() implementations.
 */

/**
 * Implements template_preprocess_views_view_fields() for homepage_slideshow view.
 */
function __ampp_preprocess_views_view_fields__homepage_slideshow(&$variables) {
  switch ($variables['view']->current_display) {
    case 'block_1': // News slideshow block
      // Only nodes that have field_image or field_slideshow_image were queried.
      // By default we display the slideshow specific image.
      $use_original_image = FALSE;

      // Get the value of "Use original image for slideshow" field.
      $use_original_image = !empty($variables['row']->field_field_slideshow_image_toggle[0]['raw']['value']);

      // We can just swap the images since they produce the same wrapper markup.
      if ($use_original_image) {
        unset($variables['fields']['field_slideshow_image']);
      }
      else {
        unset($variables['fields']['field_image']);
      }

      break;
  }
}

/**
 * Implements template_preprocess_views_view_table() for projects_search_result view.
 */
function __ampp_preprocess_views_view_table__projects_search_result(&$variables) {
  switch ($variables['view']->current_display) {
    case 'panel_pane_1':
      foreach ($variables['view']->field as $field_name => $handler) {
        $handler_class = get_class($handler);
        switch ($handler_class) {
          case 'amp_activity_handler_field_date_convert':
            $variables['header_classes'][$field_name] .= ' date-header';
            foreach ($variables['field_classes'][$field_name] as $delta => &$value) {
              $value .= ' date-field';
            }
            break;
          case 'amp_activity_handler_field_transaction':
            $variables['header_classes'][$field_name] .= ' transaction-header';
            foreach ($variables['field_classes'][$field_name] as $delta => &$value) {
              $value .= ' transaction-field';
            }
            break;
        }
      }
      break;
  }
}
