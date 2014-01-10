<?php

/**
 * @file
 * Contains default views on behalf of the feedback module.
 */

/**
 * Implements hook_views_default_views().
 */
function feedback_views_default_views() {
  $view = new view;
  $view->name = 'feedback_messages';
  $view->description = 'Override the default feedback messages report.';
  $view->tag = 'feedback';
  $view->base_table = 'feedback';
  $view->human_name = '';
  $view->core = 0;
  $view->api_version = '3.0-alpha1';
  $view->disabled = TRUE; /* Edit this to true to make a default view disabled initially */

  /* Display: Defaults */
  $handler = $view->new_display('default', 'Defaults', 'default');
  $handler->display->display_options['title'] = 'Feedback Messages';
  $handler->display->display_options['access']['type'] = 'perm';
  $handler->display->display_options['access']['perm'] = 'view feedback messages';
  $handler->display->display_options['cache']['type'] = 'none';
  $handler->display->display_options['query']['type'] = 'views_query';
  $handler->display->display_options['exposed_form']['type'] = 'basic';
  $handler->display->display_options['pager']['type'] = 'full';
  $handler->display->display_options['pager']['options']['items_per_page'] = 50;
  $handler->display->display_options['style_plugin'] = 'table';
  $handler->display->display_options['style_options']['columns'] = array(
    'location' => 'location',
    'timestamp' => 'timestamp',
    'name' => 'name',
    'message' => 'message',
    'useragent' => 'message',
    'view_entry' => 'message',
    'fid' => 'message',
  );
  $handler->display->display_options['style_options']['default'] = '-1';
  $handler->display->display_options['style_options']['info'] = array(
    'location' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'timestamp' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'name' => array(
      'sortable' => 1,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'message' => array(
      'align' => '',
      'separator' => '',
    ),
    'useragent' => array(
      'sortable' => 0,
      'default_sort_order' => 'asc',
      'align' => '',
      'separator' => '',
    ),
    'view_entry' => array(
      'align' => '',
      'separator' => '',
    ),
    'fid' => array(
      'align' => '',
      'separator' => '',
    ),
  );
  $handler->display->display_options['style_options']['override'] = 1;
  $handler->display->display_options['style_options']['sticky'] = 0;
  $handler->display->display_options['style_options']['empty_table'] = 0;
  /* Header: Global: Text area */
  $handler->display->display_options['header']['text']['id'] = 'text';
  $handler->display->display_options['header']['text']['table'] = 'views';
  $handler->display->display_options['header']['text']['field'] = 'area';
  $handler->display->display_options['header']['text']['empty'] = FALSE;
  $handler->display->display_options['header']['text']['content'] = 'Open Feedback Messages';
  $handler->display->display_options['header']['text']['format'] = 'full_html';
  /* No results behavior: Global: Text area */
  $handler->display->display_options['empty']['area']['id'] = 'area';
  $handler->display->display_options['empty']['area']['table'] = 'views';
  $handler->display->display_options['empty']['area']['field'] = 'area';
  $handler->display->display_options['empty']['area']['empty'] = FALSE;
  $handler->display->display_options['empty']['area']['content'] = 'There are no feedback entries.';
  /* Relationship: Feedback: User Id */
  $handler->display->display_options['relationships']['uid']['id'] = 'uid';
  $handler->display->display_options['relationships']['uid']['table'] = 'feedback';
  $handler->display->display_options['relationships']['uid']['field'] = 'uid';
  $handler->display->display_options['relationships']['uid']['required'] = 0;
  /* Field: Feedback: Location */
  $handler->display->display_options['fields']['location']['id'] = 'location';
  $handler->display->display_options['fields']['location']['table'] = 'feedback';
  $handler->display->display_options['fields']['location']['field'] = 'location';
  $handler->display->display_options['fields']['location']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['location']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['location']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['location']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['location']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['location']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['location']['alter']['html'] = 0;
  $handler->display->display_options['fields']['location']['hide_empty'] = 0;
  $handler->display->display_options['fields']['location']['empty_zero'] = 0;
  $handler->display->display_options['fields']['location']['display_as_link'] = 1;
  /* Field: Feedback: Timestamp */
  $handler->display->display_options['fields']['timestamp']['id'] = 'timestamp';
  $handler->display->display_options['fields']['timestamp']['table'] = 'feedback';
  $handler->display->display_options['fields']['timestamp']['field'] = 'timestamp';
  $handler->display->display_options['fields']['timestamp']['label'] = 'Date';
  $handler->display->display_options['fields']['timestamp']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['external'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['timestamp']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['timestamp']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['timestamp']['alter']['html'] = 0;
  $handler->display->display_options['fields']['timestamp']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['timestamp']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['timestamp']['hide_empty'] = 0;
  $handler->display->display_options['fields']['timestamp']['empty_zero'] = 0;
  $handler->display->display_options['fields']['timestamp']['date_format'] = 'short';
  /* Field: User: Name */
  $handler->display->display_options['fields']['name']['id'] = 'name';
  $handler->display->display_options['fields']['name']['table'] = 'users';
  $handler->display->display_options['fields']['name']['field'] = 'name';
  $handler->display->display_options['fields']['name']['relationship'] = 'uid';
  $handler->display->display_options['fields']['name']['label'] = 'User';
  $handler->display->display_options['fields']['name']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['name']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['name']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['name']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['name']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['name']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['name']['alter']['html'] = 0;
  $handler->display->display_options['fields']['name']['hide_empty'] = 0;
  $handler->display->display_options['fields']['name']['empty_zero'] = 0;
  $handler->display->display_options['fields']['name']['link_to_user'] = 1;
  $handler->display->display_options['fields']['name']['overwrite_anonymous'] = 0;
  /* Field: Feedback: Message */
  $handler->display->display_options['fields']['message']['id'] = 'message';
  $handler->display->display_options['fields']['message']['table'] = 'feedback';
  $handler->display->display_options['fields']['message']['field'] = 'message';
  $handler->display->display_options['fields']['message']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['message']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['message']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['message']['alter']['external'] = 0;
  $handler->display->display_options['fields']['message']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['message']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['message']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['message']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['message']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['message']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['message']['alter']['html'] = 0;
  $handler->display->display_options['fields']['message']['element_type'] = 'div';
  $handler->display->display_options['fields']['message']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['message']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['message']['hide_empty'] = 0;
  $handler->display->display_options['fields']['message']['empty_zero'] = 0;
  /* Field: Feedback: User agent */
  $handler->display->display_options['fields']['useragent']['id'] = 'useragent';
  $handler->display->display_options['fields']['useragent']['table'] = 'feedback';
  $handler->display->display_options['fields']['useragent']['field'] = 'useragent';
  $handler->display->display_options['fields']['useragent']['label'] = '';
  $handler->display->display_options['fields']['useragent']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['external'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['useragent']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['useragent']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['useragent']['alter']['html'] = 0;
  $handler->display->display_options['fields']['useragent']['element_type'] = 'div';
  $handler->display->display_options['fields']['useragent']['element_label_colon'] = 1;
  $handler->display->display_options['fields']['useragent']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['useragent']['hide_empty'] = 0;
  $handler->display->display_options['fields']['useragent']['empty_zero'] = 0;
  /* Field: Feedback: Link */
  $handler->display->display_options['fields']['view_entry']['id'] = 'view_entry';
  $handler->display->display_options['fields']['view_entry']['table'] = 'feedback';
  $handler->display->display_options['fields']['view_entry']['field'] = 'view_entry';
  $handler->display->display_options['fields']['view_entry']['label'] = '';
  $handler->display->display_options['fields']['view_entry']['alter']['alter_text'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['make_link'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['external'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['view_entry']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['view_entry']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['view_entry']['alter']['html'] = 0;
  $handler->display->display_options['fields']['view_entry']['element_label_colon'] = 0;
  $handler->display->display_options['fields']['view_entry']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['view_entry']['hide_empty'] = 0;
  $handler->display->display_options['fields']['view_entry']['empty_zero'] = 0;
  /* Field: Feedback: Delete link */
  $handler->display->display_options['fields']['fid']['id'] = 'fid';
  $handler->display->display_options['fields']['fid']['table'] = 'feedback';
  $handler->display->display_options['fields']['fid']['field'] = 'fid';
  $handler->display->display_options['fields']['fid']['ui_name'] = 'Feedback: Delete link';
  $handler->display->display_options['fields']['fid']['label'] = '';
  $handler->display->display_options['fields']['fid']['alter']['alter_text'] = 1;
  $handler->display->display_options['fields']['fid']['alter']['text'] = ' delete';
  $handler->display->display_options['fields']['fid']['alter']['make_link'] = 1;
  $handler->display->display_options['fields']['fid']['alter']['path'] = 'admin/reports/feedback/[fid]/delete';
  $handler->display->display_options['fields']['fid']['alter']['absolute'] = 0;
  $handler->display->display_options['fields']['fid']['alter']['external'] = 0;
  $handler->display->display_options['fields']['fid']['alter']['replace_spaces'] = 0;
  $handler->display->display_options['fields']['fid']['alter']['trim'] = 0;
  $handler->display->display_options['fields']['fid']['alter']['nl2br'] = 0;
  $handler->display->display_options['fields']['fid']['alter']['word_boundary'] = 1;
  $handler->display->display_options['fields']['fid']['alter']['ellipsis'] = 1;
  $handler->display->display_options['fields']['fid']['alter']['strip_tags'] = 0;
  $handler->display->display_options['fields']['fid']['alter']['html'] = 0;
  $handler->display->display_options['fields']['fid']['element_label_colon'] = 0;
  $handler->display->display_options['fields']['fid']['element_wrapper_type'] = 'span';
  $handler->display->display_options['fields']['fid']['element_default_classes'] = 1;
  $handler->display->display_options['fields']['fid']['hide_empty'] = 0;
  $handler->display->display_options['fields']['fid']['empty_zero'] = 0;
  $handler->display->display_options['fields']['fid']['separator'] = '';
  $handler->display->display_options['fields']['fid']['format_plural'] = 0;
  /* Sort criterion: Feedback: Timestamp */
  $handler->display->display_options['sorts']['timestamp']['id'] = 'timestamp';
  $handler->display->display_options['sorts']['timestamp']['table'] = 'feedback';
  $handler->display->display_options['sorts']['timestamp']['field'] = 'timestamp';
  /* Filter criterion: Feedback: Status */
  $handler->display->display_options['filters']['status']['id'] = 'status';
  $handler->display->display_options['filters']['status']['table'] = 'feedback';
  $handler->display->display_options['filters']['status']['field'] = 'status';
  $handler->display->display_options['filters']['status']['value'] = '0';
  $handler->display->display_options['filters']['status']['expose']['operator'] = FALSE;

  /* Display: Page */
  $handler = $view->new_display('page', 'Page', 'page_1');
  $handler->display->display_options['path'] = 'admin/reports/feedback';
  $handler->display->display_options['menu']['type'] = 'normal';
  $handler->display->display_options['menu']['title'] = 'Feedback messages';
  $handler->display->display_options['menu']['description'] = 'View feedback messages.';
  $handler->display->display_options['menu']['weight'] = '0';
  $handler->display->display_options['menu']['name'] = 'management';

  /* Display: Attachment */
  $handler = $view->new_display('attachment', 'Attachment', 'attachment_1');
  $handler->display->display_options['defaults']['title'] = FALSE;
  $handler->display->display_options['pager']['type'] = 'some';
  $handler->display->display_options['defaults']['header'] = FALSE;
  /* Header: Global: Text area */
  $handler->display->display_options['header']['text']['id'] = 'area';
  $handler->display->display_options['header']['text']['table'] = 'views';
  $handler->display->display_options['header']['text']['field'] = 'area';
  $handler->display->display_options['header']['text']['empty'] = FALSE;
  $handler->display->display_options['header']['text']['content'] = 'Processed Feedback Messages';
  $handler->display->display_options['defaults']['filters'] = FALSE;
  /* Filter criterion: Feedback: Status */
  $handler->display->display_options['filters']['status']['id'] = 'status';
  $handler->display->display_options['filters']['status']['table'] = 'feedback';
  $handler->display->display_options['filters']['status']['field'] = 'status';
  $handler->display->display_options['filters']['status']['value'] = '1';
  $handler->display->display_options['filters']['status']['expose']['operator'] = FALSE;
  $handler->display->display_options['displays'] = array(
    'page_1' => 'page_1',
    'default' => 0,
  );
  $handler->display->display_options['attachment_position'] = 'after';
  $handler->display->display_options['render_pager'] = TRUE;
  $translatables['feedback_messages'] = array(
    t('Defaults'),
    t('Feedback Messages'),
    t('more'),
    t('Apply'),
    t('Reset'),
    t('Sort by'),
    t('Asc'),
    t('Desc'),
    t('Items per page'),
    t('- All -'),
    t('Offset'),
    t('Open Feedback Messages'),
    t('There are no feedback entries.'),
    t('User'),
    t('Location'),
    t('Date'),
    t('Message'),
    t(' delete'),
    t('admin/reports/feedback/[fid]/delete'),
    t('.'),
    t('Page'),
    t('Attachment'),
    t('Processed Feedback Messages'),
  );
  $views[$view->name] = $view;
  return $views;
}
