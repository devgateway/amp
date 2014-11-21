<?php
/**
 * @file
 * ampet_blog.views_default.inc
 */

/**
 * Implements hook_views_default_views().
 */
function ampet_blog_views_default_views() {
  $export = array();

  $view = new view();
  $view->name = 'blog_listing';
  $view->description = '';
  $view->tag = 'default';
  $view->base_table = 'node';
  $view->human_name = 'Blog Listing';
  $view->core = 7;
  $view->api_version = '3.0';
  $view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */

  /* Display: Master */
  $handler = $view->new_display('default', 'Master', 'default');
  $handler->display->display_options['title'] = 'Blog';
  $handler->display->display_options['use_more_always'] = FALSE;
  $handler->display->display_options['access']['type'] = 'perm';
  $handler->display->display_options['cache']['type'] = 'none';
  $handler->display->display_options['query']['type'] = 'views_query';
  $handler->display->display_options['query']['options']['distinct'] = TRUE;
  $handler->display->display_options['exposed_form']['type'] = 'basic';
  $handler->display->display_options['pager']['type'] = 'full';
  $handler->display->display_options['pager']['options']['items_per_page'] = '6';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['pager']['options']['id'] = '0';
  $handler->display->display_options['pager']['options']['quantity'] = '9';
  $handler->display->display_options['style_plugin'] = 'default';
  $handler->display->display_options['row_plugin'] = 'node';
  $handler->display->display_options['row_options']['links'] = FALSE;
  /* No results behavior: Global: Unfiltered text */
  $handler->display->display_options['empty']['area_text_custom']['id'] = 'area_text_custom';
  $handler->display->display_options['empty']['area_text_custom']['table'] = 'views';
  $handler->display->display_options['empty']['area_text_custom']['field'] = 'area_text_custom';
  $handler->display->display_options['empty']['area_text_custom']['empty'] = TRUE;
  $handler->display->display_options['empty']['area_text_custom']['content'] = '<p>You have not published any blog entries. New blog entries can be added from the content creation page.</p>';
  /* Field: Content: Title */
  $handler->display->display_options['fields']['title']['id'] = 'title';
  $handler->display->display_options['fields']['title']['table'] = 'node';
  $handler->display->display_options['fields']['title']['field'] = 'title';
  $handler->display->display_options['fields']['title']['label'] = '';
  $handler->display->display_options['fields']['title']['alter']['word_boundary'] = FALSE;
  $handler->display->display_options['fields']['title']['alter']['ellipsis'] = FALSE;
  /* Sort criterion: Content: Post date */
  $handler->display->display_options['sorts']['created']['id'] = 'created';
  $handler->display->display_options['sorts']['created']['table'] = 'node';
  $handler->display->display_options['sorts']['created']['field'] = 'created';
  $handler->display->display_options['sorts']['created']['order'] = 'DESC';
  /* Filter criterion: Content: Published */
  $handler->display->display_options['filters']['status']['id'] = 'status';
  $handler->display->display_options['filters']['status']['table'] = 'node';
  $handler->display->display_options['filters']['status']['field'] = 'status';
  $handler->display->display_options['filters']['status']['value'] = 1;
  $handler->display->display_options['filters']['status']['group'] = 1;
  $handler->display->display_options['filters']['status']['expose']['operator'] = FALSE;
  /* Filter criterion: Content: Type */
  $handler->display->display_options['filters']['type']['id'] = 'type';
  $handler->display->display_options['filters']['type']['table'] = 'node';
  $handler->display->display_options['filters']['type']['field'] = 'type';
  $handler->display->display_options['filters']['type']['value'] = array(
    'blog' => 'blog',
  );

  /* Display: Listing */
  $handler = $view->new_display('panel_pane', 'Listing', 'listing');
  $handler->display->display_options['defaults']['pager'] = FALSE;
  $handler->display->display_options['pager']['type'] = 'featured';
  $handler->display->display_options['pager']['options']['items_per_page'] = '6';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['pager']['options']['id'] = '0';
  $handler->display->display_options['pager']['options']['quantity'] = '9';
  $handler->display->display_options['pager']['options']['items_first_page'] = '5';
  $handler->display->display_options['defaults']['style_plugin'] = FALSE;
  $handler->display->display_options['style_plugin'] = 'default';
  $handler->display->display_options['defaults']['style_options'] = FALSE;
  $handler->display->display_options['defaults']['row_plugin'] = FALSE;
  $handler->display->display_options['row_plugin'] = 'ds';
  $handler->display->display_options['row_options']['view_mode'] = 'teaser_normal';
  $handler->display->display_options['row_options']['alternating'] = 1;
  $handler->display->display_options['row_options']['grouping'] = 0;
  $handler->display->display_options['row_options']['advanced'] = 0;
  $handler->display->display_options['row_options']['delta_fieldset']['delta_fields'] = array();
  $handler->display->display_options['row_options']['grouping_fieldset']['group_field'] = 'node|created';
  $handler->display->display_options['row_options']['default_fieldset']['view_mode'] = 'teaser_normal';
  $handler->display->display_options['row_options']['alternating_fieldset']['alternating'] = TRUE;
  $handler->display->display_options['row_options']['alternating_fieldset']['item_0'] = 'teaser_featured';
  $handler->display->display_options['row_options']['alternating_fieldset']['item_1'] = 'teaser_normal';
  $handler->display->display_options['row_options']['alternating_fieldset']['item_2'] = 'teaser_normal';
  $handler->display->display_options['row_options']['alternating_fieldset']['item_3'] = 'teaser_normal';
  $handler->display->display_options['row_options']['alternating_fieldset']['item_4'] = 'teaser_normal';
  $handler->display->display_options['row_options']['alternating_fieldset']['item_5'] = 'teaser_normal';
  $handler->display->display_options['defaults']['row_options'] = FALSE;
  $handler->display->display_options['defaults']['arguments'] = FALSE;
  /* Contextual filter: Content: Blog entry type (field_blog_entry_type) */
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['id'] = 'field_blog_entry_type_value';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['table'] = 'field_data_field_blog_entry_type';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['field'] = 'field_blog_entry_type_value';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['default_argument_type'] = 'fixed';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['summary']['number_of_records'] = '0';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['summary']['format'] = 'default_summary';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['summary_options']['items_per_page'] = '25';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['limit'] = '0';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['case'] = 'lower';
  $handler->display->display_options['arguments']['field_blog_entry_type_value']['break_phrase'] = TRUE;
  $handler->display->display_options['allow']['use_pager'] = 0;
  $handler->display->display_options['allow']['items_per_page'] = 0;
  $handler->display->display_options['allow']['offset'] = 0;
  $handler->display->display_options['allow']['link_to_view'] = 0;
  $handler->display->display_options['allow']['more_link'] = 0;
  $handler->display->display_options['allow']['path_override'] = 0;
  $handler->display->display_options['allow']['title_override'] = 'title_override';
  $handler->display->display_options['allow']['exposed_form'] = 0;
  $handler->display->display_options['allow']['fields_override'] = 0;
  $handler->display->display_options['argument_input'] = array(
    'field_blog_entry_type_value' => array(
      'type' => 'user',
      'context' => 'entity:contentblock.field-cbbody',
      'context_optional' => 0,
      'panel' => '0',
      'fixed' => '',
      'label' => 'Content: Blog entry type (field_blog_entry_type)',
    ),
  );
  $handler->display->display_options['inherit_panels_path'] = '1';
  $translatables['blog_listing'] = array(
    t('Master'),
    t('Blog'),
    t('more'),
    t('Apply'),
    t('Reset'),
    t('Sort by'),
    t('Asc'),
    t('Desc'),
    t('Items per page'),
    t('- All -'),
    t('Offset'),
    t('« first'),
    t('‹ previous'),
    t('next ›'),
    t('last »'),
    t('<p>You have not published any blog entries. New blog entries can be added from the content creation page.</p>'),
    t('Listing'),
    t('All'),
    t('View panes'),
  );
  $export['blog_listing'] = $view;

  return $export;
}
