<?php

$plugin = array(
    'single' => TRUE,
    'title' => t('News list'),
    'description' => t('News list'),
    'category' => array(t('AMPP Widgets')),
    'defaults' => array(),
    'render callback' => 'ampp_projects_news_list_render'
);


/*
 *
 */
function ampp_news_news_list_render($subtype, $conf, $args, $context) {
  $block = new stdClass();
  $view = views_get_view('news');
  $view->set_display('block');
  $block->title = t('News and events');
  $block->content = $view->preview('block',$args);
  return $block;
}
