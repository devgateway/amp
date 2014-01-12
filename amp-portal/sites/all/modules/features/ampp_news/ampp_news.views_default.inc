<?php
/**
 * @file
 * ampp_news.views_default.inc
 */

/**
 * Implements hook_views_default_views().
 */
function ampp_news_views_default_views() {
  $export = array();

    $view = new view;
    $view->name = 'news';
    $view->description = '';
    $view->tag = 'default';
    $view->base_table = 'node';
    $view->human_name = 'News';
    $view->core = 7;
    $view->api_version = '3.0-alpha1';
    $view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */
    
    /* Display: Master */
    $handler = $view->new_display('default', 'Master', 'default');
    $handler->display->display_options['title'] = 'News list';
    $handler->display->display_options['access']['type'] = 'perm';
    $handler->display->display_options['cache']['type'] = 'none';
    $handler->display->display_options['query']['type'] = 'views_query';
    $handler->display->display_options['query']['options']['query_comment'] = FALSE;
    $handler->display->display_options['exposed_form']['type'] = 'basic';
    $handler->display->display_options['pager']['type'] = 'full';
    $handler->display->display_options['pager']['options']['items_per_page'] = '15';
    $handler->display->display_options['style_plugin'] = 'default';
    $handler->display->display_options['row_plugin'] = 'fields';
    /* Field: Content: Title */
    $handler->display->display_options['fields']['title']['id'] = 'title';
    $handler->display->display_options['fields']['title']['table'] = 'node';
    $handler->display->display_options['fields']['title']['field'] = 'title';
    $handler->display->display_options['fields']['title']['label'] = '';
    $handler->display->display_options['fields']['title']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['title']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['title']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 0;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 0;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 1;
    /* Field: Content: Body */
    $handler->display->display_options['fields']['body']['id'] = 'body';
    $handler->display->display_options['fields']['body']['table'] = 'field_data_body';
    $handler->display->display_options['fields']['body']['field'] = 'body';
    /* Sort criterion: Content: Post date */
    $handler->display->display_options['sorts']['created']['id'] = 'created';
    $handler->display->display_options['sorts']['created']['table'] = 'node';
    $handler->display->display_options['sorts']['created']['field'] = 'created';
    $handler->display->display_options['sorts']['created']['order'] = 'DESC';
    /* Contextual filter: Content: Nid */
    $handler->display->display_options['arguments']['nid']['id'] = 'nid';
    $handler->display->display_options['arguments']['nid']['table'] = 'node';
    $handler->display->display_options['arguments']['nid']['field'] = 'nid';
    $handler->display->display_options['arguments']['nid']['default_argument_type'] = 'fixed';
    $handler->display->display_options['arguments']['nid']['summary']['format'] = 'default_summary';
    /* Filter criterion: Content: Published */
    $handler->display->display_options['filters']['status']['id'] = 'status';
    $handler->display->display_options['filters']['status']['table'] = 'node';
    $handler->display->display_options['filters']['status']['field'] = 'status';
    $handler->display->display_options['filters']['status']['value'] = 1;
    $handler->display->display_options['filters']['status']['group'] = 0;
    $handler->display->display_options['filters']['status']['expose']['operator'] = FALSE;
    /* Filter criterion: Content: Type */
    $handler->display->display_options['filters']['type']['id'] = 'type';
    $handler->display->display_options['filters']['type']['table'] = 'node';
    $handler->display->display_options['filters']['type']['field'] = 'type';
    $handler->display->display_options['filters']['type']['value'] = array(
      'event' => 'event',
      'imported_item' => 'imported_item',
      'news' => 'news',
    );
    
    /* Display: List */
    $handler = $view->new_display('block', 'List', 'block');
    $handler->display->display_options['defaults']['title'] = FALSE;
    $handler->display->display_options['defaults']['fields'] = FALSE;
    /* Field: Content: Path */
    $handler->display->display_options['fields']['path']['id'] = 'path';
    $handler->display->display_options['fields']['path']['table'] = 'node';
    $handler->display->display_options['fields']['path']['field'] = 'path';
    $handler->display->display_options['fields']['path']['label'] = '';
    $handler->display->display_options['fields']['path']['exclude'] = TRUE;
    $handler->display->display_options['fields']['path']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['path']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['path']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['path']['alter']['external'] = 0;
    $handler->display->display_options['fields']['path']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['path']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['path']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['path']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['path']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['path']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['path']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['path']['alter']['html'] = 0;
    $handler->display->display_options['fields']['path']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['path']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['path']['hide_empty'] = 0;
    $handler->display->display_options['fields']['path']['empty_zero'] = 0;
    $handler->display->display_options['fields']['path']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['path']['absolute'] = 0;
    /* Field: Content: Image */
    $handler->display->display_options['fields']['field_image']['id'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['table'] = 'field_data_field_image';
    $handler->display->display_options['fields']['field_image']['field'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['label'] = '';
    $handler->display->display_options['fields']['field_image']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['external'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['html'] = 0;
    $handler->display->display_options['fields']['field_image']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['field_image']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_image']['hide_empty'] = 0;
    $handler->display->display_options['fields']['field_image']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_image']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_image']['click_sort_column'] = 'fid';
    $handler->display->display_options['fields']['field_image']['settings'] = array(
      'image_style' => 'news_list',
      'image_link' => 'content',
    );
    $handler->display->display_options['fields']['field_image']['field_api_classes'] = 0;
    /* Field: Content: Post date */
    $handler->display->display_options['fields']['created']['id'] = 'created';
    $handler->display->display_options['fields']['created']['table'] = 'node';
    $handler->display->display_options['fields']['created']['field'] = 'created';
    $handler->display->display_options['fields']['created']['label'] = '';
    $handler->display->display_options['fields']['created']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['created']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['created']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['created']['alter']['external'] = 0;
    $handler->display->display_options['fields']['created']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['created']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['created']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['created']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['created']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['created']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['created']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['created']['alter']['html'] = 0;
    $handler->display->display_options['fields']['created']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['created']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['created']['hide_empty'] = 0;
    $handler->display->display_options['fields']['created']['empty_zero'] = 0;
    $handler->display->display_options['fields']['created']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['created']['date_format'] = 'long';
    /* Field: Content: Title */
    $handler->display->display_options['fields']['title']['id'] = 'title';
    $handler->display->display_options['fields']['title']['table'] = 'node';
    $handler->display->display_options['fields']['title']['field'] = 'title';
    $handler->display->display_options['fields']['title']['label'] = '';
    $handler->display->display_options['fields']['title']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['title']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['title']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['title']['alter']['external'] = 0;
    $handler->display->display_options['fields']['title']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['title']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 0;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 0;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 0;
    /* Field: Content: Body */
    $handler->display->display_options['fields']['body']['id'] = 'body';
    $handler->display->display_options['fields']['body']['table'] = 'field_data_body';
    $handler->display->display_options['fields']['body']['field'] = 'body';
    $handler->display->display_options['fields']['body']['label'] = '';
    $handler->display->display_options['fields']['body']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['body']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['body']['alter']['path'] = '/[path]';
    $handler->display->display_options['fields']['body']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['body']['alter']['external'] = 0;
    $handler->display->display_options['fields']['body']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['body']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['body']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['body']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['body']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['body']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['body']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['body']['alter']['html'] = 0;
    $handler->display->display_options['fields']['body']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['body']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['body']['hide_empty'] = 0;
    $handler->display->display_options['fields']['body']['empty_zero'] = 0;
    $handler->display->display_options['fields']['body']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['body']['type'] = 'text_summary_or_trimmed';
    $handler->display->display_options['fields']['body']['settings'] = array(
      'trim_length' => '600',
    );
    $handler->display->display_options['fields']['body']['field_api_classes'] = 0;
    $handler->display->display_options['defaults']['sorts'] = FALSE;
    /* Sort criterion: Content: Post date */
    $handler->display->display_options['sorts']['created']['id'] = 'created';
    $handler->display->display_options['sorts']['created']['table'] = 'node';
    $handler->display->display_options['sorts']['created']['field'] = 'created';
    $handler->display->display_options['sorts']['created']['order'] = 'DESC';
    $handler->display->display_options['defaults']['filters'] = FALSE;
    /* Filter criterion: Content: Published */
    $handler->display->display_options['filters']['status']['id'] = 'status';
    $handler->display->display_options['filters']['status']['table'] = 'node';
    $handler->display->display_options['filters']['status']['field'] = 'status';
    $handler->display->display_options['filters']['status']['value'] = 1;
    $handler->display->display_options['filters']['status']['group'] = 0;
    $handler->display->display_options['filters']['status']['expose']['operator'] = FALSE;
    /* Filter criterion: Content: Type */
    $handler->display->display_options['filters']['type']['id'] = 'type';
    $handler->display->display_options['filters']['type']['table'] = 'node';
    $handler->display->display_options['filters']['type']['field'] = 'type';
    $handler->display->display_options['filters']['type']['value'] = array(
      'event' => 'event',
      'imported_item' => 'imported_item',
      'news' => 'news',
    );
    $handler->display->display_options['block_description'] = 'List';
    
    /* Display: Single item */
    $handler = $view->new_display('block', 'Single item', 'block_1');
    $handler->display->display_options['defaults']['title'] = FALSE;
    $handler->display->display_options['defaults']['pager'] = FALSE;
    $handler->display->display_options['pager']['type'] = 'none';
    $handler->display->display_options['pager']['options']['offset'] = '0';
    $handler->display->display_options['defaults']['fields'] = FALSE;
    /* Field: Content: Title */
    $handler->display->display_options['fields']['title']['id'] = 'title';
    $handler->display->display_options['fields']['title']['table'] = 'node';
    $handler->display->display_options['fields']['title']['field'] = 'title';
    $handler->display->display_options['fields']['title']['label'] = '';
    $handler->display->display_options['fields']['title']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['title']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['title']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['title']['alter']['external'] = 0;
    $handler->display->display_options['fields']['title']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['title']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 0;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 0;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 0;
    /* Field: Content: Post date */
    $handler->display->display_options['fields']['created']['id'] = 'created';
    $handler->display->display_options['fields']['created']['table'] = 'node';
    $handler->display->display_options['fields']['created']['field'] = 'created';
    $handler->display->display_options['fields']['created']['label'] = '';
    $handler->display->display_options['fields']['created']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['created']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['created']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['created']['alter']['external'] = 0;
    $handler->display->display_options['fields']['created']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['created']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['created']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['created']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['created']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['created']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['created']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['created']['alter']['html'] = 0;
    $handler->display->display_options['fields']['created']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['created']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['created']['hide_empty'] = 0;
    $handler->display->display_options['fields']['created']['empty_zero'] = 0;
    $handler->display->display_options['fields']['created']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['created']['date_format'] = 'long';
    /* Field: Content: Image */
    $handler->display->display_options['fields']['field_image']['id'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['table'] = 'field_data_field_image';
    $handler->display->display_options['fields']['field_image']['field'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['label'] = '';
    $handler->display->display_options['fields']['field_image']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['external'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['html'] = 0;
    $handler->display->display_options['fields']['field_image']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['field_image']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_image']['hide_empty'] = 0;
    $handler->display->display_options['fields']['field_image']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_image']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_image']['click_sort_column'] = 'fid';
    $handler->display->display_options['fields']['field_image']['settings'] = array(
      'image_style' => 'news_single',
      'image_link' => '',
    );
    $handler->display->display_options['fields']['field_image']['field_api_classes'] = 0;
    /* Field: Content: Body */
    $handler->display->display_options['fields']['body']['id'] = 'body';
    $handler->display->display_options['fields']['body']['table'] = 'field_data_body';
    $handler->display->display_options['fields']['body']['field'] = 'body';
    $handler->display->display_options['fields']['body']['label'] = '';
    $handler->display->display_options['fields']['body']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['body']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['body']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['body']['alter']['external'] = 0;
    $handler->display->display_options['fields']['body']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['body']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['body']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['body']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['body']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['body']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['body']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['body']['alter']['html'] = 0;
    $handler->display->display_options['fields']['body']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['body']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['body']['hide_empty'] = 0;
    $handler->display->display_options['fields']['body']['empty_zero'] = 0;
    $handler->display->display_options['fields']['body']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['body']['field_api_classes'] = 0;
    /* Field: Content: Link */
    $handler->display->display_options['fields']['field_link']['id'] = 'field_link';
    $handler->display->display_options['fields']['field_link']['table'] = 'field_data_field_link';
    $handler->display->display_options['fields']['field_link']['field'] = 'field_link';
    $handler->display->display_options['fields']['field_link']['label'] = 'Read more';
    $handler->display->display_options['fields']['field_link']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['external'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['field_link']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['field_link']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['field_link']['alter']['html'] = 0;
    $handler->display->display_options['fields']['field_link']['element_label_colon'] = 1;
    $handler->display->display_options['fields']['field_link']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_link']['hide_empty'] = 1;
    $handler->display->display_options['fields']['field_link']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_link']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_link']['click_sort_column'] = 'url';
    $handler->display->display_options['fields']['field_link']['type'] = 'link_url';
    $handler->display->display_options['fields']['field_link']['field_api_classes'] = 0;
    $handler->display->display_options['defaults']['arguments'] = FALSE;
    /* Contextual filter: Content: Nid */
    $handler->display->display_options['arguments']['nid']['id'] = 'nid';
    $handler->display->display_options['arguments']['nid']['table'] = 'node';
    $handler->display->display_options['arguments']['nid']['field'] = 'nid';
    $handler->display->display_options['arguments']['nid']['default_action'] = 'empty';
    $handler->display->display_options['arguments']['nid']['default_argument_type'] = 'fixed';
    $handler->display->display_options['arguments']['nid']['default_argument_skip_url'] = 0;
    $handler->display->display_options['arguments']['nid']['summary']['number_of_records'] = '0';
    $handler->display->display_options['arguments']['nid']['summary']['format'] = 'default_summary';
    $handler->display->display_options['arguments']['nid']['summary_options']['items_per_page'] = '25';
    $handler->display->display_options['arguments']['nid']['break_phrase'] = 0;
    $handler->display->display_options['arguments']['nid']['not'] = 0;
    $handler->display->display_options['block_description'] = 'Single item';
    $translatables['news'] = array(
      t('Master'),
      t('News list'),
      t('more'),
      t('Apply'),
      t('Reset'),
      t('Sort by'),
      t('Asc'),
      t('Desc'),
      t('Items per page'),
      t('- All -'),
      t('Offset'),
      t('Body'),
      t('All'),
      t('List'),
      t('/[path]'),
      t('Single item'),
      t('Read more'),
    );
  $export['news'] = $view;

  return $export;
}
