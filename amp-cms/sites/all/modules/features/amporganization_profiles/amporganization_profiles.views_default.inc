<?php
/**
 * @file
 * amporganization_profiles.views_default.inc
 */

/**
 * Implements hook_views_default_views().
 */
function amporganization_profiles_views_default_views() {
  $export = array();

  $view = new view();
  $view->name = 'organizations';
  $view->description = '';
  $view->tag = 'default';
  $view->base_table = 'node';
  $view->human_name = 'Organizations';
  $view->core = 7;
  $view->api_version = '3.0';
  $view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */

  /* Display: Master */
  $handler = $view->new_display('default', 'Master', 'default');
  $handler->display->display_options['title'] = 'Organizations';
  $handler->display->display_options['use_more_always'] = FALSE;
  $handler->display->display_options['access']['type'] = 'perm';
  $handler->display->display_options['cache']['type'] = 'none';
  $handler->display->display_options['query']['type'] = 'views_query';
  $handler->display->display_options['exposed_form']['type'] = 'basic';
  $handler->display->display_options['pager']['type'] = 'none';
  $handler->display->display_options['pager']['options']['offset'] = '0';
  $handler->display->display_options['style_plugin'] = 'default';
  $handler->display->display_options['row_plugin'] = 'node';
  $handler->display->display_options['row_options']['view_mode'] = 'teaser_featured';
  $handler->display->display_options['row_options']['links'] = FALSE;
  /* No results behavior: Global: Text area */
  $handler->display->display_options['empty']['area']['id'] = 'area';
  $handler->display->display_options['empty']['area']['table'] = 'views';
  $handler->display->display_options['empty']['area']['field'] = 'area';
  $handler->display->display_options['empty']['area']['empty'] = TRUE;
  $handler->display->display_options['empty']['area']['content'] = 'There are no organizations to display!';
  $handler->display->display_options['empty']['area']['format'] = 'filtered_html';
  /* Field: Content: Organization Logo */
  $handler->display->display_options['fields']['field_organization_logo']['id'] = 'field_organization_logo';
  $handler->display->display_options['fields']['field_organization_logo']['table'] = 'field_data_field_organization_logo';
  $handler->display->display_options['fields']['field_organization_logo']['field'] = 'field_organization_logo';
  $handler->display->display_options['fields']['field_organization_logo']['label'] = '';
  $handler->display->display_options['fields']['field_organization_logo']['element_label_colon'] = FALSE;
  $handler->display->display_options['fields']['field_organization_logo']['element_default_classes'] = FALSE;
  $handler->display->display_options['fields']['field_organization_logo']['click_sort_column'] = 'fid';
  $handler->display->display_options['fields']['field_organization_logo']['settings'] = array(
    'image_style' => 'img-llogo',
    'image_link' => 'content',
  );
  /* Field: Content: Title */
  $handler->display->display_options['fields']['title']['id'] = 'title';
  $handler->display->display_options['fields']['title']['table'] = 'node';
  $handler->display->display_options['fields']['title']['field'] = 'title';
  $handler->display->display_options['fields']['title']['label'] = '';
  $handler->display->display_options['fields']['title']['alter']['word_boundary'] = FALSE;
  $handler->display->display_options['fields']['title']['alter']['ellipsis'] = FALSE;
  $handler->display->display_options['fields']['title']['element_type'] = 'h3';
  $handler->display->display_options['fields']['title']['element_class'] = 'title';
  $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
  $handler->display->display_options['fields']['title']['element_default_classes'] = FALSE;
  /* Field: Content: Organization Group */
  $handler->display->display_options['fields']['field_organization_group']['id'] = 'field_organization_group';
  $handler->display->display_options['fields']['field_organization_group']['table'] = 'field_data_field_organization_group';
  $handler->display->display_options['fields']['field_organization_group']['field'] = 'field_organization_group';
  $handler->display->display_options['fields']['field_organization_group']['label'] = '';
  $handler->display->display_options['fields']['field_organization_group']['exclude'] = TRUE;
  $handler->display->display_options['fields']['field_organization_group']['element_label_colon'] = FALSE;
  $handler->display->display_options['fields']['field_organization_group']['element_default_classes'] = FALSE;
  $handler->display->display_options['fields']['field_organization_group']['type'] = 'taxonomy_term_reference_plain';
  /* Sort criterion: Field: Title (title_field) */
  $handler->display->display_options['sorts']['title_field_value']['id'] = 'title_field_value';
  $handler->display->display_options['sorts']['title_field_value']['table'] = 'field_data_title_field';
  $handler->display->display_options['sorts']['title_field_value']['field'] = 'title_field_value';
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
    'organization' => 'organization',
  );

  /* Display: Page */
  $handler = $view->new_display('page', 'Page', 'page');
  $handler->display->display_options['defaults']['css_class'] = FALSE;
  $handler->display->display_options['css_class'] = 'organizations-list layout-grid-items layout-separator';
  $handler->display->display_options['defaults']['style_plugin'] = FALSE;
  $handler->display->display_options['style_plugin'] = 'default';
  $handler->display->display_options['style_options']['grouping'] = array(
    0 => array(
      'field' => 'field_organization_group',
      'rendered' => 0,
      'rendered_strip' => 1,
    ),
  );
  $handler->display->display_options['style_options']['row_class'] = 'layout-block layout-grid-item layout-separator';
  $handler->display->display_options['defaults']['style_options'] = FALSE;
  $handler->display->display_options['defaults']['row_plugin'] = FALSE;
  $handler->display->display_options['row_plugin'] = 'fields';
  $handler->display->display_options['row_options']['default_field_elements'] = FALSE;
  $handler->display->display_options['defaults']['row_options'] = FALSE;
  $handler->display->display_options['path'] = 'organizations';
  $translatables['organizations'] = array(
    t('Master'),
    t('Organizations'),
    t('more'),
    t('Apply'),
    t('Reset'),
    t('Sort by'),
    t('Asc'),
    t('Desc'),
    t('There are no organizations to display!'),
    t('Page'),
  );
  $export['organizations'] = $view;

  return $export;
}
