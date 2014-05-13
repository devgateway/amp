<?php


$plugin = array(
  'single' => TRUE,
  'title' => t('Recent news'),
  'description' => t('Renders recent news slideshow and other news'),
  'category' => t('AMPP Widgets'),
  'defaults' => array(
    'to_show' => 6,
    'more_text' => 'Read all news and events',
    'more_url' => 'news',
    'more_show' => 1,
    'show_only' => 0,
  ),
  'hook theme' => 'ampp_news_recent_news_content_type_theme',
);


/*
* Plugin output function
*/
function ampp_news_recent_news_content_type_render($subtype, $conf, $panel_args, $context) {

  $block = new stdClass();

  $vars = array();

  if (!isset($conf['show_only'])) $conf['show_only'] = 0;

  if ($conf['show_only'] == 0 || $conf['show_only'] == 2) {
    $footer_view = views_get_view('homepage_slideshow');
    $footer_view->set_items_per_page($conf['to_show']);
    $footer_view->set_display('block_4');
    $footer_view->pre_execute();
    $footer_view->execute();
    $footer_content = $footer_view->preview('block_4');
  }
  if ($conf['show_only'] == 0 || $conf['show_only'] == 1) {
    $view = views_get_view('homepage_slideshow');
    $view->set_display('block_1');
    $view->pre_execute();
    $view->execute();
  }
  if ($conf['show_only'] == 0) {
    // $view->footer['area']->options['format'] = 'html';
    $view->footer['area']->options['content'] = $footer_content;
    $vars['content'] = $view->preview('block_1');
  }
  if ($conf['show_only'] == 1) {
    $view->footer['area']->options['content'] = '';
    $vars['content'] = $view->preview('block_1');
  }
  if ($conf['show_only'] == 2) {
    $vars['content'] = $footer_content;
  }

  // more link
  $conf['more_text'] = trim($conf['more_text']);
  if ( empty($conf['more_text']) || ($conf['more_show'] && (isset($view) && $view->total_rows <= $conf['to_show'])) ) {
    $vars['more'] = false;
  } else {
    $vars['more'] = true;
    $vars['more_text'] = $conf['more_text'];
    $vars['more_url'] = $conf['more_url'];
  }

  $block->content = theme('ampp_news_recent_news', array('vars' => $vars));

  return $block;
}


/*
 * Implementing hook_content_type_edit_form()
 */
function ampp_news_recent_news_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  $form['show_only'] = array(
    '#type' => 'select',
    '#title' => t("Show"),
    '#options' => array (0 => t('Both slideshow and news listing'), 1 => t('Slideshow only'), 2 => t('News listing only')),
    '#default_value' => isset($conf['show_only']) ? $conf['show_only'] : $form_state['plugin']['defaults']['show_only'],
  );
  $form['to_show'] = array(
    '#type' => 'textfield',
    '#size' => 2,
    '#maxlength' => 2,
    '#required' => TRUE,
    '#element_validate' => array('recent_news_to_show_validate'),
    '#title' => t('Count'),
    '#description' => t('Number of news to show'),
    '#default_value' => intval($conf['to_show']) ? $conf['to_show'] : $form_state['plugin']['defaults']['to_show'],
  );
  $form['more_text'] = array(
    '#type' => 'textfield',
    '#size' => 20,
    '#maxlength' => 255,
    '#title' => t("Text for 'more' link"),
    '#description' => t('Leave blank to hide the link'),
    '#default_value' => isset($conf['more_show']) ? $conf['more_text'] : $form_state['plugin']['defaults']['more_text'],
  );
  $form['more_url'] = array(
    '#type' => 'textfield',
    '#size' => 20,
    '#maxlength' => 255,
    '#title' => t("Relative URL for 'more' link"),
    '#description' => t('Do not use forward slash for relative URLs'),
    '#default_value' => $conf['more_url'] ? $conf['more_url'] : $form_state['plugin']['defaults']['more_url'],
  );
  $form['more_show'] = array(
    '#type' => 'checkbox',
    '#title' => t("Display 'more' link only if there is more content"),
    '#default_value' => isset($conf['more_show']) ? $conf['more_show'] : $form_state['plugin']['defaults']['more_show'],
  );
  return $form;
}


/*
* Handles edit for submit
*/
function ampp_news_recent_news_content_type_edit_form_submit(&$form, &$form_state) {
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}


/*
* Validation function
*/
function recent_news_to_show_validate($element, &$form_state, $form) {
   if (!intval($element['#value']) || intval($element['#value']) < 0) {
     form_error($element, t('Must be greater than zero integer value'));
   }
}


/*
* Theme function
*/
function ampp_news_recent_news_content_type_theme(&$theme, $plugin) {
  $theme['ampp_news_recent_news'] = array(
    'arguments' => array('vars' => NULL),
    'path' => $plugin['path'],
    'template' => 'recent_news',
  );
}