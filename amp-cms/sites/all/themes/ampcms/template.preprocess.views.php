<?php
/**
 * @file
 * This file contains view preprocess functions.
 */

/**
 * Implements template_preprocess_views_view().
 */
function ampcms_preprocess_views_view(&$variables) {
  $function = '__' . __FUNCTION__ . '__' . $variables['view']->name;
  if (function_exists($function)) {
    $function($variables);
  }
}

/**
 * Implements template_preprocess_views_view_unformatted().
 */
function ampcms_preprocess_views_view_unformatted(&$variables) {
  $function = '__' . __FUNCTION__ . '__' . $variables['view']->name;
  if (function_exists($function)) {
    $function($variables);
  }
}

/**
 * Implements template_preprocess_views_view_fields().
 */
function ampcms_preprocess_views_view_fields(&$variables) {
  $function = '__' . __FUNCTION__ . '__' . $variables['view']->name;
  if (function_exists($function)) {
    $function($variables);
  }
}

/*******************************************************************************
 * Helper functions for template_preprocess_HOOK() implementations.
 */

/**
 * Implements ampcms_preprocess_views_view() for homepage view.
 */
function __ampcms_preprocess_views_view__homepage(&$vars) {
  switch ($vars['view']->current_display) {
    case 'news_events':
      if (!empty($vars['view']->result)) {
        // @HACK: Remove views contextual links, needed for more-link next to contentblock header.
        $vars['title_suffix']['contextual_links'] = array();
        if (in_array('contextual-links-region', $vars['classes_array'])) {
          $key = array_search('contextual-links-region', $vars['classes_array']);
          unset($vars['classes_array'][$key]);
        }

        $vars['more'] = '<div class="more-link">' . l(t('see all'), 'blog/news-and-events') . '</div>';
      }

      // @HACK: Change the title, This is a way to avoid needing panels translation.
      $vars['view']->set_title(t('News And Events'));
      break;
  }
}

/**
 * Implements ampcms_preprocess_views_view() for blog_listing view.
 */
function __ampcms_preprocess_views_view__blog_listing(&$vars) {
  switch ($vars['view']->current_display) {
    case 'listing':
      if ($vars['view']->query->pager->current_page == 0) {
        $vars['classes_array'][] = 'views-first-page';
      }

      // @HACK: Change the title, This is a way to avoid needing panels translation.
      if (!empty($vars['view']->args) && reset($vars['view']->args) == 'news+events') {
        $vars['view']->set_title(t('News And Events'));
      }
      else {
        $vars['view']->set_title(t('Blog'));
      }

      break;
  }
}

/**
 * Implements ampcms_preprocess_views_view() for activities view.
 */
function __ampcms_preprocess_views_view__activities(&$vars) {
  switch ($vars['view']->current_display) {
    case 'search_page':
      // Reattach the form to the view.

      // $result = $vars['view']->display_handler->get_special_blocks();
      // if (!empty($result)) {
      //   $special_block_type = '-exp';
      //   $special_block = $vars['view']->display_handler->view_special_blocks($special_block_type);
      //   $vars['attachment_before'] .= $special_block['content'];
      // }
      // dpm($result, '$result');

      if (!empty($vars['view']->query->pager->total_items)) {
        // TODO: Use built in "Result summary".
        $info = array(
          'current_page' => $vars['view']->query->pager->current_page,
          'items_per_page' => $vars['view']->query->pager->options['items_per_page'],
          'total_items' => $vars['view']->query->pager->total_items,
        );
        $vars['attachment_before'] .= theme('amp_report_info', $info);
      }

      if (!empty($vars['view']->query->query->metaData['report_totals'])) {
        $report_totals = $vars['view']->query->query->metaData['report_totals'];
        $vars['attachment_after'] .= theme('amp_report_totals', $report_totals);
      }

      break;
  }
}
