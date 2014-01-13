<?php
/**
 * @file
 * ampp_homepage.views_default.inc
 */

/**
 * Implements hook_views_default_views().
 */
function ampp_homepage_views_default_views() {
  $export = array();

    $view = new view;
    $view->name = 'homepage_slideshow';
    $view->description = '';
    $view->tag = 'default';
    $view->base_table = 'node';
    $view->human_name = 'Homepage slideshow';
    $view->core = 7;
    $view->api_version = '3.0-alpha1';
    $view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */
    
    /* Display: Master */
    $handler = $view->new_display('default', 'Master', 'default');
    $handler->display->display_options['access']['type'] = 'perm';
    $handler->display->display_options['cache']['type'] = 'none';
    $handler->display->display_options['query']['type'] = 'views_query';
    $handler->display->display_options['query']['options']['query_comment'] = FALSE;
    $handler->display->display_options['exposed_form']['type'] = 'basic';
    $handler->display->display_options['pager']['type'] = 'none';
    $handler->display->display_options['pager']['options']['offset'] = '0';
    $handler->display->display_options['style_plugin'] = 'slideshow';
    $handler->display->display_options['row_plugin'] = 'fields';
    /* Relationship: Nodequeue: Queue */
    $handler->display->display_options['relationships']['nodequeue_rel']['id'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['table'] = 'node';
    $handler->display->display_options['relationships']['nodequeue_rel']['field'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['required'] = 1;
    $handler->display->display_options['relationships']['nodequeue_rel']['limit'] = 0;
    $handler->display->display_options['relationships']['nodequeue_rel']['names'] = array(
      'homepage_slideshow' => 0,
      'donor_profiles' => 0,
      'maps' => 0,
      'news_and_events' => 0,
    );
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
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 1;
    /* Field: Content: Image */
    $handler->display->display_options['fields']['field_image']['id'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['table'] = 'field_data_field_image';
    $handler->display->display_options['fields']['field_image']['field'] = 'field_image';
    /* Sort criterion: Nodequeue: Subqueue ID */
    $handler->display->display_options['sorts']['sqid']['id'] = 'sqid';
    $handler->display->display_options['sorts']['sqid']['table'] = 'nodequeue_nodes';
    $handler->display->display_options['sorts']['sqid']['field'] = 'sqid';
    $handler->display->display_options['sorts']['sqid']['relationship'] = 'nodequeue_rel';
    $handler->display->display_options['filter_groups']['operator'] = 'OR';
    $handler->display->display_options['filter_groups']['groups'] = array(
      0 => 'AND',
      1 => 'AND',
    );
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
      'event' => 'event',
      'news' => 'news',
    );
    $handler->display->display_options['filters']['type']['group'] = 1;
    /* Filter criterion: Content: Promoted to front page */
    $handler->display->display_options['filters']['promote']['id'] = 'promote';
    $handler->display->display_options['filters']['promote']['table'] = 'node';
    $handler->display->display_options['filters']['promote']['field'] = 'promote';
    $handler->display->display_options['filters']['promote']['value'] = '1';
    $handler->display->display_options['filters']['promote']['group'] = 1;
    
    /* Display: Main slideshow */
    $handler = $view->new_display('panel_pane', 'Main slideshow', 'panel_pane_5');
    $handler->display->display_options['defaults']['title'] = FALSE;
    $handler->display->display_options['defaults']['style_plugin'] = FALSE;
    $handler->display->display_options['style_plugin'] = 'slideshow';
    $handler->display->display_options['style_options']['slideshow_type'] = 'views_slideshow_ddblock';
    $handler->display->display_options['style_options']['slideshow_skin'] = 'default';
    $handler->display->display_options['style_options']['skin_info'] = array(
      'class' => 'default',
      'name' => 'Default (This option is for views slideshow cycle, it is not used for views_slideshow ddblock)',
      'module' => 'views_slideshow_ddblock',
      'path' => '',
      'stylesheets' => array(),
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'title' => 0,
      'body' => 0,
      'field_image' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'title' => 0,
      'body' => 0,
      'field_image' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['timeout'] = '5000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['speed'] = '700';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['delay'] = '0';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['start_paused'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['remember_slide_days'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['items_per_slide'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['wait_for_image_load_timeout'] = '3000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartype'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartypenobg'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_ddblock'] = array(
      'widget' => 'cycle',
      'debug_info' => 'none',
      'mapping_wrapper' => array(
        'mappings' => array(
          0 => array(
            'target' => 'node_id',
            'source' => '',
          ),
          1 => array(
            'target' => 'slide_image',
            'source' => 'field_image',
          ),
          2 => array(
            'target' => 'slide_title',
            'source' => 'title',
          ),
          3 => array(
            'target' => 'slide_text',
            'source' => 'body',
          ),
          4 => array(
            'target' => 'slide_read_more',
            'source' => '',
          ),
          5 => array(
            'target' => 'pager_image',
            'source' => '',
          ),
          6 => array(
            'target' => 'pager_text',
            'source' => '',
          ),
          7 => array(
            'target' => '',
            'source' => '',
          ),
          8 => array(
            'target' => '',
            'source' => '',
          ),
          9 => array(
            'target' => '',
            'source' => '',
          ),
          10 => array(
            'target' => '',
            'source' => '',
          ),
          11 => array(
            'target' => '',
            'source' => '',
          ),
          12 => array(
            'target' => '',
            'source' => '',
          ),
          13 => array(
            'target' => '',
            'source' => '',
          ),
          14 => array(
            'target' => '',
            'source' => '',
          ),
          15 => array(
            'target' => '',
            'source' => '',
          ),
        ),
      ),
      'template' => 'default',
      'template_size_wrapper' => array(
        'template_size' => 'default',
      ),
      'content_container' => array(
        'container' => 'div.slide',
        'overflow' => 0,
      ),
      'settings' => array(
        'fx' => 'fade',
        'speed' => '500',
        'timeout' => '5000',
        'order' => 'none',
        'pause' => 1,
        'next' => 1,
        'pager_toggle' => 1,
        'pager_settings' => array(
          'pager' => 'custom-pager',
          'pager_container' => '.custom-pager-item',
          'nr_of_pager_items' => '3',
          'pager_position' => 'left',
          'pager_event' => 'click',
          'pager_disable_click' => 1,
          'pager_fast' => 1,
          'pager_pause' => 1,
        ),
        'pager2' => 1,
        'pager2_settings' => array(
          'pager2_event' => 'click',
          'pager2_position' => array(
            'slide' => 'slide',
            'pager' => 0,
          ),
          'pager2_pager' => array(
            'pager2_pager_prev' => '',
            'pager2_pager_next' => '',
            'pager2_pager_hide' => 0,
          ),
          'pager2_slide' => array(
            'pager2_slide_prev' => '',
            'pager2_slide_next' => '',
            'pager2_slide_hide' => 0,
          ),
        ),
        'slide_text' => 1,
        'slide_text_settings' => array(
          'slide_text_container' => 'div.slide-text',
          'slide_text_position' => 'bottom',
          'slide_text_jquery' => 0,
          'slide_jquery' => array(
            'slide_text_before_effect' => 'hide',
            'slide_text_before_speed' => '500',
            'slide_text_after_effect' => 'show',
            'slide_text_after_speed' => '500',
          ),
        ),
        'custom' => array(
          'custom_jquery' => '',
        ),
      ),
      'template_submit' => 'get template sizes',
    );
    $handler->display->display_options['defaults']['style_options'] = FALSE;
    $handler->display->display_options['defaults']['row_plugin'] = FALSE;
    $handler->display->display_options['row_plugin'] = 'fields';
    $handler->display->display_options['defaults']['row_options'] = FALSE;
    $handler->display->display_options['defaults']['relationships'] = FALSE;
    /* Relationship: Nodequeue: Queue */
    $handler->display->display_options['relationships']['nodequeue_rel']['id'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['table'] = 'node';
    $handler->display->display_options['relationships']['nodequeue_rel']['field'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['required'] = 1;
    $handler->display->display_options['relationships']['nodequeue_rel']['limit'] = 0;
    $handler->display->display_options['relationships']['nodequeue_rel']['names'] = array(
      'homepage_slideshow' => 0,
      'donor_profiles' => 0,
      'maps' => 0,
      'news_and_events' => 0,
    );
    $handler->display->display_options['defaults']['fields'] = FALSE;
    /* Field: Content: Show title in slideshow */
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['id'] = 'field_show_title_in_slideshow';
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['table'] = 'field_data_field_show_title_in_slideshow';
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['field'] = 'field_show_title_in_slideshow';
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['label'] = '';
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['external'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['alter']['html'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['hide_empty'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_show_title_in_slideshow']['field_api_classes'] = 0;
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
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 1;
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
      'image_style' => 'homepage_main_slideshow',
      'image_link' => '',
    );
    $handler->display->display_options['fields']['field_image']['field_api_classes'] = 0;
    $handler->display->display_options['defaults']['sorts'] = FALSE;
    /* Sort criterion: Nodequeue: Position */
    $handler->display->display_options['sorts']['position']['id'] = 'position';
    $handler->display->display_options['sorts']['position']['table'] = 'nodequeue_nodes';
    $handler->display->display_options['sorts']['position']['field'] = 'position';
    $handler->display->display_options['sorts']['position']['relationship'] = 'nodequeue_rel';
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
      'homepage_slideshow' => 'homepage_slideshow',
    );
    $handler->display->display_options['filters']['type']['group'] = 0;
    /* Filter criterion: Content translation: Language */
    $handler->display->display_options['filters']['language']['id'] = 'language';
    $handler->display->display_options['filters']['language']['table'] = 'node';
    $handler->display->display_options['filters']['language']['field'] = 'language';
    $handler->display->display_options['filters']['language']['value'] = array(
      '***CURRENT_LANGUAGE***' => '***CURRENT_LANGUAGE***',
    );
    $handler->display->display_options['filters']['language']['group'] = 1;
    /* Filter criterion: Content translation: Language */
    $handler->display->display_options['filters']['language_1']['id'] = 'language_1';
    $handler->display->display_options['filters']['language_1']['table'] = 'node';
    $handler->display->display_options['filters']['language_1']['field'] = 'language';
    $handler->display->display_options['filters']['language_1']['value'] = array(
      'und' => 'und',
    );
    $handler->display->display_options['filters']['language_1']['group'] = 1;
    
    /* Display: News slideshow block */
    $handler = $view->new_display('block', 'News slideshow block', 'block_1');
    $handler->display->display_options['defaults']['style_plugin'] = FALSE;
    $handler->display->display_options['style_plugin'] = 'slideshow';
    $handler->display->display_options['style_options']['slideshow_type'] = 'views_slideshow_cycle';
    $handler->display->display_options['style_options']['slideshow_skin'] = 'default';
    $handler->display->display_options['style_options']['skin_info'] = array(
      'class' => 'default',
      'name' => 'Default (This option is for views slideshow cycle, it is not used for views_slideshow ddblock)',
      'module' => 'views_slideshow_ddblock',
      'path' => '',
      'stylesheets' => array(),
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['hide_on_single_slide'] = 1;
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'field_slideshow_image_1' => 'field_slideshow_image_1',
      'field_image' => 0,
      'field_slideshow_image_toggle' => 0,
      'field_slideshow_image' => 0,
      'created' => 0,
      'title' => 0,
      'nid' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['views_slideshow_pager_fields_hover'] = 1;
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['enable'] = 1;
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'nid' => 'nid',
      'field_image' => 0,
      'field_slideshow_image_toggle' => 0,
      'field_slideshow_image' => 0,
      'created' => 0,
      'title' => 0,
      'field_slideshow_image_1' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['views_slideshow_pager_fields_hover'] = 1;
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['timeout'] = '5000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['speed'] = '700';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['delay'] = '0';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['start_paused'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['remember_slide_days'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['items_per_slide'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['wait_for_image_load_timeout'] = '3000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartype'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartypenobg'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_ddblock'] = array(
      'widget' => 'cycle',
      'debug_info' => 'none',
      'mapping_wrapper' => array(
        'mappings' => array(
          0 => array(
            'target' => 'node_id',
            'source' => '',
          ),
          1 => array(
            'target' => 'slide_image',
            'source' => 'field_slideshow_image',
          ),
          2 => array(
            'target' => 'slide_title',
            'source' => 'created',
          ),
          3 => array(
            'target' => 'slide_text',
            'source' => 'title',
          ),
          4 => array(
            'target' => 'slide_read_more',
            'source' => '',
          ),
          5 => array(
            'target' => 'pager_image',
            'source' => 'field_slideshow_image_1',
          ),
          6 => array(
            'target' => 'pager_text',
            'source' => '',
          ),
          7 => array(
            'target' => '',
            'source' => '',
          ),
          8 => array(
            'target' => '',
            'source' => '',
          ),
          9 => array(
            'target' => '',
            'source' => '',
          ),
          10 => array(
            'target' => '',
            'source' => '',
          ),
          11 => array(
            'target' => '',
            'source' => '',
          ),
          12 => array(
            'target' => '',
            'source' => '',
          ),
          13 => array(
            'target' => '',
            'source' => '',
          ),
          14 => array(
            'target' => '',
            'source' => '',
          ),
          15 => array(
            'target' => '',
            'source' => '',
          ),
        ),
      ),
      'template' => 'default',
      'template_size_wrapper' => array(
        'template_size' => 'default',
      ),
      'content_container' => array(
        'container' => 'div.slide',
        'overflow' => 0,
      ),
      'settings' => array(
        'fx' => 'fade',
        'speed' => '500',
        'timeout' => '5000',
        'order' => 'none',
        'pause' => 1,
        'next' => 0,
        'pager_toggle' => 1,
        'pager_settings' => array(
          'pager' => 'custom-pager',
          'pager_container' => '.custom-pager-item',
          'nr_of_pager_items' => '3',
          'pager_position' => 'left',
          'pager_event' => 'click',
          'pager_disable_click' => 1,
          'pager_fast' => 1,
          'pager_pause' => 1,
        ),
        'pager2' => 0,
        'pager2_settings' => array(
          'pager2_event' => 'click',
          'pager2_position' => array(
            'slide' => 'slide',
            'pager' => 0,
          ),
          'pager2_pager' => array(
            'pager2_pager_prev' => 'prev',
            'pager2_pager_next' => 'next',
            'pager2_pager_hide' => 1,
          ),
          'pager2_slide' => array(
            'pager2_slide_prev' => '',
            'pager2_slide_next' => '',
            'pager2_slide_hide' => 1,
          ),
        ),
        'slide_text' => 1,
        'slide_text_settings' => array(
          'slide_text_container' => 'div.slide-text',
          'slide_text_position' => 'bottom',
          'slide_text_jquery' => 0,
          'slide_jquery' => array(
            'slide_text_before_effect' => 'hide',
            'slide_text_before_speed' => '500',
            'slide_text_after_effect' => 'show',
            'slide_text_after_speed' => '500',
          ),
        ),
        'custom' => array(
          'custom_jquery' => '',
        ),
      ),
      'template_submit' => 'get template sizes',
    );
    $handler->display->display_options['defaults']['style_options'] = FALSE;
    $handler->display->display_options['defaults']['row_plugin'] = FALSE;
    $handler->display->display_options['row_plugin'] = 'fields';
    $handler->display->display_options['defaults']['row_options'] = FALSE;
    $handler->display->display_options['defaults']['footer'] = FALSE;
    /* Footer: Global: Text area */
    $handler->display->display_options['footer']['area']['id'] = 'area';
    $handler->display->display_options['footer']['area']['table'] = 'views';
    $handler->display->display_options['footer']['area']['field'] = 'area';
    $handler->display->display_options['footer']['area']['empty'] = TRUE;
    $handler->display->display_options['footer']['area']['content'] = 'some content...';
    $handler->display->display_options['footer']['area']['format'] = 'plain_text';
    $handler->display->display_options['footer']['area']['tokenize'] = 0;
    $handler->display->display_options['defaults']['relationships'] = FALSE;
    /* Relationship: Nodequeue: Queue */
    $handler->display->display_options['relationships']['nodequeue_rel']['id'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['table'] = 'node';
    $handler->display->display_options['relationships']['nodequeue_rel']['field'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['required'] = 1;
    $handler->display->display_options['relationships']['nodequeue_rel']['limit'] = 0;
    $handler->display->display_options['relationships']['nodequeue_rel']['names'] = array(
      'homepage_slideshow' => 0,
      'donor_profiles' => 0,
      'maps' => 0,
      'news_and_events' => 0,
    );
    $handler->display->display_options['defaults']['fields'] = FALSE;
    /* Field: Content: Image */
    $handler->display->display_options['fields']['field_image']['id'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['table'] = 'field_data_field_image';
    $handler->display->display_options['fields']['field_image']['field'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['label'] = '';
    $handler->display->display_options['fields']['field_image']['exclude'] = TRUE;
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
      'image_style' => '',
      'image_link' => 'content',
    );
    $handler->display->display_options['fields']['field_image']['field_api_classes'] = 0;
    /* Field: Content: Use original image for slideshow */
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['id'] = 'field_slideshow_image_toggle';
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['table'] = 'field_data_field_slideshow_image_toggle';
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['field'] = 'field_slideshow_image_toggle';
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['label'] = '';
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['exclude'] = TRUE;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['external'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['alter']['html'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['hide_empty'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image_toggle']['field_api_classes'] = 0;
    /* Field: Content: Slideshow image */
    $handler->display->display_options['fields']['field_slideshow_image']['id'] = 'field_slideshow_image';
    $handler->display->display_options['fields']['field_slideshow_image']['table'] = 'field_data_field_slideshow_image';
    $handler->display->display_options['fields']['field_slideshow_image']['field'] = 'field_slideshow_image';
    $handler->display->display_options['fields']['field_slideshow_image']['label'] = '';
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['external'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['alter']['html'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['field_slideshow_image']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_slideshow_image']['empty'] = '<img src="no_slideshow_image">';
    $handler->display->display_options['fields']['field_slideshow_image']['hide_empty'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_slideshow_image']['click_sort_column'] = 'fid';
    $handler->display->display_options['fields']['field_slideshow_image']['settings'] = array(
      'image_style' => '',
      'image_link' => 'content',
    );
    $handler->display->display_options['fields']['field_slideshow_image']['field_api_classes'] = 0;
    /* Field: Content: Post date */
    $handler->display->display_options['fields']['created']['id'] = 'created';
    $handler->display->display_options['fields']['created']['table'] = 'node';
    $handler->display->display_options['fields']['created']['field'] = 'created';
    $handler->display->display_options['fields']['created']['label'] = '';
    $handler->display->display_options['fields']['created']['exclude'] = TRUE;
    $handler->display->display_options['fields']['created']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['created']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['created']['alter']['path'] = '[path]';
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
    $handler->display->display_options['fields']['title']['alter']['alter_text'] = 1;
    $handler->display->display_options['fields']['title']['alter']['text'] = '<span class="slideshow_created">[created]</span>
    <span class="slideshow_title">[title]</span>';
    $handler->display->display_options['fields']['title']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['title']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['title']['alter']['external'] = 0;
    $handler->display->display_options['fields']['title']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['title']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 0;
    /* Field: Content: Nid */
    $handler->display->display_options['fields']['nid']['id'] = 'nid';
    $handler->display->display_options['fields']['nid']['table'] = 'node';
    $handler->display->display_options['fields']['nid']['field'] = 'nid';
    $handler->display->display_options['fields']['nid']['label'] = '';
    $handler->display->display_options['fields']['nid']['alter']['alter_text'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['text'] = '<span></span>';
    $handler->display->display_options['fields']['nid']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['external'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['html'] = 0;
    $handler->display->display_options['fields']['nid']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['nid']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['nid']['hide_empty'] = 0;
    $handler->display->display_options['fields']['nid']['empty_zero'] = 0;
    $handler->display->display_options['fields']['nid']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['nid']['link_to_node'] = 0;
    $handler->display->display_options['defaults']['sorts'] = FALSE;
    /* Sort criterion: Nodequeue: Position */
    $handler->display->display_options['sorts']['position']['id'] = 'position';
    $handler->display->display_options['sorts']['position']['table'] = 'nodequeue_nodes';
    $handler->display->display_options['sorts']['position']['field'] = 'position';
    $handler->display->display_options['sorts']['position']['relationship'] = 'nodequeue_rel';
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
    $handler->display->display_options['filters']['type']['group'] = 0;
    /* Filter criterion: Content: Slideshow image (field_slideshow_image:fid) */
    $handler->display->display_options['filters']['field_slideshow_image_fid']['id'] = 'field_slideshow_image_fid';
    $handler->display->display_options['filters']['field_slideshow_image_fid']['table'] = 'field_data_field_slideshow_image';
    $handler->display->display_options['filters']['field_slideshow_image_fid']['field'] = 'field_slideshow_image_fid';
    $handler->display->display_options['filters']['field_slideshow_image_fid']['operator'] = 'not empty';
    $handler->display->display_options['filters']['field_slideshow_image_fid']['group'] = 0;
    /* Filter criterion: Content: Published */
    $handler->display->display_options['filters']['status_1']['id'] = 'status_1';
    $handler->display->display_options['filters']['status_1']['table'] = 'node';
    $handler->display->display_options['filters']['status_1']['field'] = 'status';
    $handler->display->display_options['filters']['status_1']['value'] = '1';
    $handler->display->display_options['filters']['status_1']['group'] = 1;
    /* Filter criterion: Content: Type */
    $handler->display->display_options['filters']['type_1']['id'] = 'type_1';
    $handler->display->display_options['filters']['type_1']['table'] = 'node';
    $handler->display->display_options['filters']['type_1']['field'] = 'type';
    $handler->display->display_options['filters']['type_1']['value'] = array(
      'event' => 'event',
      'imported_item' => 'imported_item',
      'news' => 'news',
    );
    $handler->display->display_options['filters']['type_1']['group'] = 1;
    /* Filter criterion: Content: Use original image for slideshow (field_slideshow_image_toggle) */
    $handler->display->display_options['filters']['field_slideshow_image_toggle_value']['id'] = 'field_slideshow_image_toggle_value';
    $handler->display->display_options['filters']['field_slideshow_image_toggle_value']['table'] = 'field_data_field_slideshow_image_toggle';
    $handler->display->display_options['filters']['field_slideshow_image_toggle_value']['field'] = 'field_slideshow_image_toggle_value';
    $handler->display->display_options['filters']['field_slideshow_image_toggle_value']['value'] = array(
      1 => '1',
    );
    $handler->display->display_options['filters']['field_slideshow_image_toggle_value']['group'] = 1;
    /* Filter criterion: Content: Image (field_image:fid) */
    $handler->display->display_options['filters']['field_image_fid']['id'] = 'field_image_fid';
    $handler->display->display_options['filters']['field_image_fid']['table'] = 'field_data_field_image';
    $handler->display->display_options['filters']['field_image_fid']['field'] = 'field_image_fid';
    $handler->display->display_options['filters']['field_image_fid']['operator'] = 'not empty';
    $handler->display->display_options['filters']['field_image_fid']['group'] = 1;
    
    /* Display: Recent news block */
    $handler = $view->new_display('block', 'Recent news block', 'block_4');
    $handler->display->display_options['defaults']['title'] = FALSE;
    $handler->display->display_options['title'] = 'Other news:';
    $handler->display->display_options['defaults']['use_more'] = FALSE;
    $handler->display->display_options['defaults']['pager'] = FALSE;
    $handler->display->display_options['pager']['type'] = 'some';
    $handler->display->display_options['pager']['options']['items_per_page'] = '6';
    $handler->display->display_options['pager']['options']['offset'] = '0';
    $handler->display->display_options['defaults']['style_plugin'] = FALSE;
    $handler->display->display_options['style_plugin'] = 'default';
    $handler->display->display_options['defaults']['style_options'] = FALSE;
    $handler->display->display_options['defaults']['row_plugin'] = FALSE;
    $handler->display->display_options['row_plugin'] = 'fields';
    $handler->display->display_options['defaults']['row_options'] = FALSE;
    $handler->display->display_options['defaults']['relationships'] = FALSE;
    /* Relationship: Nodequeue: Queue */
    $handler->display->display_options['relationships']['nodequeue_rel']['id'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['table'] = 'node';
    $handler->display->display_options['relationships']['nodequeue_rel']['field'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['required'] = 0;
    $handler->display->display_options['relationships']['nodequeue_rel']['limit'] = 0;
    $handler->display->display_options['relationships']['nodequeue_rel']['names'] = array(
      'homepage_slideshow' => 0,
      'donor_profiles' => 0,
      'maps' => 0,
      'news_and_events' => 0,
    );
    $handler->display->display_options['defaults']['fields'] = FALSE;
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
      'image_style' => 'homepage_news_teaser',
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
    $handler->display->display_options['fields']['created']['alter']['path'] = '[path]';
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
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 0;
    $handler->display->display_options['defaults']['sorts'] = FALSE;
    /* Sort criterion: Content: Sticky */
    $handler->display->display_options['sorts']['sticky']['id'] = 'sticky';
    $handler->display->display_options['sorts']['sticky']['table'] = 'node';
    $handler->display->display_options['sorts']['sticky']['field'] = 'sticky';
    $handler->display->display_options['sorts']['sticky']['order'] = 'DESC';
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
    /* Filter criterion: Content: Promoted to front page */
    $handler->display->display_options['filters']['promote']['id'] = 'promote';
    $handler->display->display_options['filters']['promote']['table'] = 'node';
    $handler->display->display_options['filters']['promote']['field'] = 'promote';
    $handler->display->display_options['filters']['promote']['value'] = '0';
    /* Filter criterion: Nodequeue: In queue */
    $handler->display->display_options['filters']['in_queue']['id'] = 'in_queue';
    $handler->display->display_options['filters']['in_queue']['table'] = 'nodequeue_nodes';
    $handler->display->display_options['filters']['in_queue']['field'] = 'in_queue';
    $handler->display->display_options['filters']['in_queue']['relationship'] = 'nodequeue_rel';
    $handler->display->display_options['filters']['in_queue']['value'] = '0';
    
    /* Display: Left slideshow block */
    $handler = $view->new_display('block', 'Left slideshow block', 'block_2');
    $handler->display->display_options['defaults']['style_plugin'] = FALSE;
    $handler->display->display_options['style_plugin'] = 'slideshow';
    $handler->display->display_options['style_options']['slideshow_type'] = 'views_slideshow_cycle';
    $handler->display->display_options['style_options']['slideshow_skin'] = 'default';
    $handler->display->display_options['style_options']['skin_info'] = array(
      'class' => 'default',
      'name' => 'Default (This option is for views slideshow cycle, it is not used for views_slideshow ddblock)',
      'module' => 'views_slideshow_ddblock',
      'path' => '',
      'stylesheets' => array(),
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'field_link' => 0,
      'field_image' => 0,
      'title' => 0,
      'nid' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['enable'] = 1;
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'nid' => 'nid',
      'field_link' => 0,
      'field_image' => 0,
      'title' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['views_slideshow_pager_fields_hover'] = 1;
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['timeout'] = '5000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['speed'] = '700';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['delay'] = '0';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['start_paused'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['remember_slide_days'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['items_per_slide'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['wait_for_image_load_timeout'] = '3000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartype'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartypenobg'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_ddblock'] = array(
      'widget' => 'cycle',
      'debug_info' => 'none',
      'mapping_wrapper' => array(
        'mappings' => array(
          0 => array(
            'target' => 'node_id',
            'source' => '',
          ),
          1 => array(
            'target' => 'slide_image',
            'source' => '',
          ),
          2 => array(
            'target' => 'slide_title',
            'source' => '',
          ),
          3 => array(
            'target' => 'slide_text',
            'source' => '',
          ),
          4 => array(
            'target' => 'slide_read_more',
            'source' => '',
          ),
          5 => array(
            'target' => 'pager_image',
            'source' => '',
          ),
          6 => array(
            'target' => 'pager_text',
            'source' => '',
          ),
          7 => array(
            'target' => '',
            'source' => '',
          ),
          8 => array(
            'target' => '',
            'source' => '',
          ),
          9 => array(
            'target' => '',
            'source' => '',
          ),
          10 => array(
            'target' => '',
            'source' => '',
          ),
          11 => array(
            'target' => '',
            'source' => '',
          ),
          12 => array(
            'target' => '',
            'source' => '',
          ),
          13 => array(
            'target' => '',
            'source' => '',
          ),
          14 => array(
            'target' => '',
            'source' => '',
          ),
          15 => array(
            'target' => '',
            'source' => '',
          ),
        ),
      ),
      'template' => 'default',
      'template_size_wrapper' => array(
        'template_size' => 'default',
      ),
      'content_container' => array(
        'container' => 'div.slide',
        'overflow' => 0,
      ),
      'settings' => array(
        'fx' => 'fade',
        'speed' => '500',
        'timeout' => '5000',
        'order' => 'none',
        'pause' => 1,
        'next' => 0,
        'pager_toggle' => 1,
        'pager_settings' => array(
          'pager' => 'custom-pager',
          'pager_container' => '.custom-pager-item',
          'nr_of_pager_items' => '3',
          'pager_position' => 'left',
          'pager_event' => 'click',
          'pager_disable_click' => 1,
          'pager_fast' => 1,
          'pager_pause' => 1,
        ),
        'pager2' => 0,
        'pager2_settings' => array(
          'pager2_event' => 'click',
          'pager2_position' => array(
            'slide' => 'slide',
            'pager' => 0,
          ),
          'pager2_pager' => array(
            'pager2_pager_prev' => 'prev',
            'pager2_pager_next' => 'next',
            'pager2_pager_hide' => 1,
          ),
          'pager2_slide' => array(
            'pager2_slide_prev' => '',
            'pager2_slide_next' => '',
            'pager2_slide_hide' => 1,
          ),
        ),
        'slide_text' => 1,
        'slide_text_settings' => array(
          'slide_text_container' => 'div.slide-text',
          'slide_text_position' => 'bottom',
          'slide_text_jquery' => 0,
          'slide_jquery' => array(
            'slide_text_before_effect' => 'hide',
            'slide_text_before_speed' => '500',
            'slide_text_after_effect' => 'show',
            'slide_text_after_speed' => '500',
          ),
        ),
        'custom' => array(
          'custom_jquery' => '',
        ),
      ),
      'template_submit' => 'get template sizes',
    );
    $handler->display->display_options['defaults']['style_options'] = FALSE;
    $handler->display->display_options['defaults']['row_plugin'] = FALSE;
    $handler->display->display_options['row_plugin'] = 'fields';
    $handler->display->display_options['defaults']['row_options'] = FALSE;
    $handler->display->display_options['defaults']['fields'] = FALSE;
    /* Field: Content: Link */
    $handler->display->display_options['fields']['field_link']['id'] = 'field_link';
    $handler->display->display_options['fields']['field_link']['table'] = 'field_data_field_link';
    $handler->display->display_options['fields']['field_link']['field'] = 'field_link';
    $handler->display->display_options['fields']['field_link']['label'] = '';
    $handler->display->display_options['fields']['field_link']['exclude'] = TRUE;
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
    $handler->display->display_options['fields']['field_link']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['field_link']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_link']['hide_empty'] = 0;
    $handler->display->display_options['fields']['field_link']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_link']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_link']['click_sort_column'] = 'url';
    $handler->display->display_options['fields']['field_link']['type'] = 'link_plain';
    $handler->display->display_options['fields']['field_link']['field_api_classes'] = 0;
    /* Field: Content: Image */
    $handler->display->display_options['fields']['field_image']['id'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['table'] = 'field_data_field_image';
    $handler->display->display_options['fields']['field_image']['field'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['label'] = '';
    $handler->display->display_options['fields']['field_image']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['make_link'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['path'] = '[field_link]';
    $handler->display->display_options['fields']['field_image']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['external'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['target'] = '_blank';
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
      'image_style' => 'homepage_left_slideshow',
      'image_link' => '',
    );
    $handler->display->display_options['fields']['field_image']['field_api_classes'] = 0;
    /* Field: Content: Title */
    $handler->display->display_options['fields']['title']['id'] = 'title';
    $handler->display->display_options['fields']['title']['table'] = 'node';
    $handler->display->display_options['fields']['title']['field'] = 'title';
    $handler->display->display_options['fields']['title']['label'] = '';
    $handler->display->display_options['fields']['title']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['title']['alter']['make_link'] = 1;
    $handler->display->display_options['fields']['title']['alter']['path'] = '[field_link]';
    $handler->display->display_options['fields']['title']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['title']['alter']['external'] = 1;
    $handler->display->display_options['fields']['title']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['title']['alter']['target'] = '_blank';
    $handler->display->display_options['fields']['title']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 0;
    /* Field: Content: Nid */
    $handler->display->display_options['fields']['nid']['id'] = 'nid';
    $handler->display->display_options['fields']['nid']['table'] = 'node';
    $handler->display->display_options['fields']['nid']['field'] = 'nid';
    $handler->display->display_options['fields']['nid']['label'] = '';
    $handler->display->display_options['fields']['nid']['alter']['alter_text'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['text'] = '<span></span>';
    $handler->display->display_options['fields']['nid']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['external'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['html'] = 0;
    $handler->display->display_options['fields']['nid']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['nid']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['nid']['hide_empty'] = 0;
    $handler->display->display_options['fields']['nid']['empty_zero'] = 0;
    $handler->display->display_options['fields']['nid']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['nid']['link_to_node'] = 0;
    $handler->display->display_options['defaults']['sorts'] = FALSE;
    /* Sort criterion: Nodequeue: Position */
    $handler->display->display_options['sorts']['position']['id'] = 'position';
    $handler->display->display_options['sorts']['position']['table'] = 'nodequeue_nodes';
    $handler->display->display_options['sorts']['position']['field'] = 'position';
    $handler->display->display_options['sorts']['position']['relationship'] = 'nodequeue_rel';
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
      'donor_profile' => 'donor_profile',
    );
    $handler->display->display_options['filters']['type']['group'] = 0;
    /* Filter criterion: Content: Image (field_image:fid) */
    $handler->display->display_options['filters']['field_image_fid']['id'] = 'field_image_fid';
    $handler->display->display_options['filters']['field_image_fid']['table'] = 'field_data_field_image';
    $handler->display->display_options['filters']['field_image_fid']['field'] = 'field_image_fid';
    $handler->display->display_options['filters']['field_image_fid']['operator'] = 'not empty';
    $handler->display->display_options['filters']['field_image_fid']['group'] = 0;
    /* Filter criterion: Content translation: Language */
    $handler->display->display_options['filters']['language']['id'] = 'language';
    $handler->display->display_options['filters']['language']['table'] = 'node';
    $handler->display->display_options['filters']['language']['field'] = 'language';
    $handler->display->display_options['filters']['language']['value'] = array(
      '***CURRENT_LANGUAGE***' => '***CURRENT_LANGUAGE***',
    );
    $handler->display->display_options['filters']['language']['group'] = 1;
    /* Filter criterion: Content translation: Language */
    $handler->display->display_options['filters']['language_1']['id'] = 'language_1';
    $handler->display->display_options['filters']['language_1']['table'] = 'node';
    $handler->display->display_options['filters']['language_1']['field'] = 'language';
    $handler->display->display_options['filters']['language_1']['value'] = array(
      'und' => 'und',
    );
    $handler->display->display_options['filters']['language_1']['group'] = 1;
    
    /* Display: Right slideshow block */
    $handler = $view->new_display('block', 'Right slideshow block', 'block_3');
    $handler->display->display_options['defaults']['style_plugin'] = FALSE;
    $handler->display->display_options['style_plugin'] = 'slideshow';
    $handler->display->display_options['style_options']['slideshow_type'] = 'views_slideshow_cycle';
    $handler->display->display_options['style_options']['slideshow_skin'] = 'default';
    $handler->display->display_options['style_options']['skin_info'] = array(
      'class' => 'default',
      'name' => 'Default (This option is for views slideshow cycle, it is not used for views_slideshow ddblock)',
      'module' => 'views_slideshow_ddblock',
      'path' => '',
      'stylesheets' => array(),
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'field_link' => 0,
      'field_image' => 0,
      'title' => 0,
      'field_image_1' => 0,
      'nid' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['top']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['enable'] = 1;
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['type'] = 'views_slideshow_pager_fields';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['views_slideshow_pager_fields_fields'] = array(
      'nid' => 'nid',
      'field_link' => 0,
      'field_image' => 0,
      'title' => 0,
      'field_image_1' => 0,
    );
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_pager']['views_slideshow_pager_fields_hover'] = 1;
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['weight'] = '1';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_controls']['type'] = 'views_slideshow_controls_text';
    $handler->display->display_options['style_options']['widgets']['bottom']['views_slideshow_slide_counter']['weight'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['timeout'] = '5000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['speed'] = '700';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['delay'] = '0';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['start_paused'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['remember_slide_days'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['items_per_slide'] = '1';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['wait_for_image_load_timeout'] = '3000';
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartype'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_cycle']['cleartypenobg'] = 0;
    $handler->display->display_options['style_options']['views_slideshow_ddblock'] = array(
      'widget' => 'cycle',
      'debug_info' => 'none',
      'mapping_wrapper' => array(
        'mappings' => array(
          0 => array(
            'target' => 'node_id',
            'source' => '',
          ),
          1 => array(
            'target' => 'slide_image',
            'source' => '',
          ),
          2 => array(
            'target' => 'slide_title',
            'source' => '',
          ),
          3 => array(
            'target' => 'slide_text',
            'source' => '',
          ),
          4 => array(
            'target' => 'slide_read_more',
            'source' => '',
          ),
          5 => array(
            'target' => 'pager_image',
            'source' => '',
          ),
          6 => array(
            'target' => 'pager_text',
            'source' => '',
          ),
          7 => array(
            'target' => '',
            'source' => '',
          ),
          8 => array(
            'target' => '',
            'source' => '',
          ),
          9 => array(
            'target' => '',
            'source' => '',
          ),
          10 => array(
            'target' => '',
            'source' => '',
          ),
          11 => array(
            'target' => '',
            'source' => '',
          ),
          12 => array(
            'target' => '',
            'source' => '',
          ),
          13 => array(
            'target' => '',
            'source' => '',
          ),
          14 => array(
            'target' => '',
            'source' => '',
          ),
          15 => array(
            'target' => '',
            'source' => '',
          ),
        ),
      ),
      'template' => 'default',
      'template_size_wrapper' => array(
        'template_size' => 'default',
      ),
      'content_container' => array(
        'container' => 'div.slide',
        'overflow' => 0,
      ),
      'settings' => array(
        'fx' => 'fade',
        'speed' => '500',
        'timeout' => '5000',
        'order' => 'none',
        'pause' => 1,
        'next' => 0,
        'pager_toggle' => 1,
        'pager_settings' => array(
          'pager' => 'custom-pager',
          'pager_container' => '.custom-pager-item',
          'nr_of_pager_items' => '3',
          'pager_position' => 'left',
          'pager_event' => 'click',
          'pager_disable_click' => 1,
          'pager_fast' => 1,
          'pager_pause' => 1,
        ),
        'pager2' => 0,
        'pager2_settings' => array(
          'pager2_event' => 'click',
          'pager2_position' => array(
            'slide' => 'slide',
            'pager' => 0,
          ),
          'pager2_pager' => array(
            'pager2_pager_prev' => 'prev',
            'pager2_pager_next' => 'next',
            'pager2_pager_hide' => 1,
          ),
          'pager2_slide' => array(
            'pager2_slide_prev' => '',
            'pager2_slide_next' => '',
            'pager2_slide_hide' => 1,
          ),
        ),
        'slide_text' => 1,
        'slide_text_settings' => array(
          'slide_text_container' => 'div.slide-text',
          'slide_text_position' => 'bottom',
          'slide_text_jquery' => 0,
          'slide_jquery' => array(
            'slide_text_before_effect' => 'hide',
            'slide_text_before_speed' => '500',
            'slide_text_after_effect' => 'show',
            'slide_text_after_speed' => '500',
          ),
        ),
        'custom' => array(
          'custom_jquery' => '',
        ),
      ),
      'template_submit' => 'get template sizes',
    );
    $handler->display->display_options['defaults']['style_options'] = FALSE;
    $handler->display->display_options['defaults']['row_plugin'] = FALSE;
    $handler->display->display_options['row_plugin'] = 'fields';
    $handler->display->display_options['defaults']['row_options'] = FALSE;
    $handler->display->display_options['defaults']['relationships'] = FALSE;
    /* Relationship: Nodequeue: Queue */
    $handler->display->display_options['relationships']['nodequeue_rel']['id'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['table'] = 'node';
    $handler->display->display_options['relationships']['nodequeue_rel']['field'] = 'nodequeue_rel';
    $handler->display->display_options['relationships']['nodequeue_rel']['required'] = 1;
    $handler->display->display_options['relationships']['nodequeue_rel']['limit'] = 0;
    $handler->display->display_options['relationships']['nodequeue_rel']['names'] = array(
      'homepage_slideshow' => 0,
      'donor_profiles' => 0,
      'maps' => 0,
      'news_and_events' => 0,
    );
    $handler->display->display_options['defaults']['fields'] = FALSE;
    /* Field: Content: Link */
    $handler->display->display_options['fields']['field_link']['id'] = 'field_link';
    $handler->display->display_options['fields']['field_link']['table'] = 'field_data_field_link';
    $handler->display->display_options['fields']['field_link']['field'] = 'field_link';
    $handler->display->display_options['fields']['field_link']['label'] = '';
    $handler->display->display_options['fields']['field_link']['exclude'] = TRUE;
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
    $handler->display->display_options['fields']['field_link']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['field_link']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['field_link']['hide_empty'] = 0;
    $handler->display->display_options['fields']['field_link']['empty_zero'] = 0;
    $handler->display->display_options['fields']['field_link']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['field_link']['click_sort_column'] = 'url';
    $handler->display->display_options['fields']['field_link']['type'] = 'link_plain';
    $handler->display->display_options['fields']['field_link']['field_api_classes'] = 0;
    /* Field: Content: Image */
    $handler->display->display_options['fields']['field_image']['id'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['table'] = 'field_data_field_image';
    $handler->display->display_options['fields']['field_image']['field'] = 'field_image';
    $handler->display->display_options['fields']['field_image']['label'] = '';
    $handler->display->display_options['fields']['field_image']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['make_link'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['path'] = '[field_link]';
    $handler->display->display_options['fields']['field_image']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['external'] = 1;
    $handler->display->display_options['fields']['field_image']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['field_image']['alter']['target'] = '_blank';
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
      'image_style' => 'homepage_right_slideshow',
      'image_link' => '',
    );
    $handler->display->display_options['fields']['field_image']['field_api_classes'] = 0;
    /* Field: Content: Title */
    $handler->display->display_options['fields']['title']['id'] = 'title';
    $handler->display->display_options['fields']['title']['table'] = 'node';
    $handler->display->display_options['fields']['title']['field'] = 'title';
    $handler->display->display_options['fields']['title']['label'] = '';
    $handler->display->display_options['fields']['title']['alter']['alter_text'] = 0;
    $handler->display->display_options['fields']['title']['alter']['make_link'] = 1;
    $handler->display->display_options['fields']['title']['alter']['path'] = '[field_link]';
    $handler->display->display_options['fields']['title']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['title']['alter']['external'] = 1;
    $handler->display->display_options['fields']['title']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['title']['alter']['target'] = '_blank';
    $handler->display->display_options['fields']['title']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['title']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['title']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['title']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['title']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['title']['alter']['html'] = 0;
    $handler->display->display_options['fields']['title']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['title']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['title']['hide_empty'] = 0;
    $handler->display->display_options['fields']['title']['empty_zero'] = 0;
    $handler->display->display_options['fields']['title']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['title']['link_to_node'] = 0;
    /* Field: Content: Nid */
    $handler->display->display_options['fields']['nid']['id'] = 'nid';
    $handler->display->display_options['fields']['nid']['table'] = 'node';
    $handler->display->display_options['fields']['nid']['field'] = 'nid';
    $handler->display->display_options['fields']['nid']['label'] = '';
    $handler->display->display_options['fields']['nid']['alter']['alter_text'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['text'] = '<span></span>';
    $handler->display->display_options['fields']['nid']['alter']['make_link'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['absolute'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['external'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['replace_spaces'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['trim_whitespace'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['nl2br'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['word_boundary'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['ellipsis'] = 1;
    $handler->display->display_options['fields']['nid']['alter']['strip_tags'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['trim'] = 0;
    $handler->display->display_options['fields']['nid']['alter']['html'] = 0;
    $handler->display->display_options['fields']['nid']['element_label_colon'] = FALSE;
    $handler->display->display_options['fields']['nid']['element_default_classes'] = 1;
    $handler->display->display_options['fields']['nid']['hide_empty'] = 0;
    $handler->display->display_options['fields']['nid']['empty_zero'] = 0;
    $handler->display->display_options['fields']['nid']['hide_alter_empty'] = 0;
    $handler->display->display_options['fields']['nid']['link_to_node'] = 0;
    $handler->display->display_options['defaults']['sorts'] = FALSE;
    /* Sort criterion: Nodequeue: Position */
    $handler->display->display_options['sorts']['position']['id'] = 'position';
    $handler->display->display_options['sorts']['position']['table'] = 'nodequeue_nodes';
    $handler->display->display_options['sorts']['position']['field'] = 'position';
    $handler->display->display_options['sorts']['position']['relationship'] = 'nodequeue_rel';
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
      'map' => 'map',
    );
    $handler->display->display_options['filters']['type']['group'] = 0;
    /* Filter criterion: Content: Image (field_image:fid) */
    $handler->display->display_options['filters']['field_image_fid']['id'] = 'field_image_fid';
    $handler->display->display_options['filters']['field_image_fid']['table'] = 'field_data_field_image';
    $handler->display->display_options['filters']['field_image_fid']['field'] = 'field_image_fid';
    $handler->display->display_options['filters']['field_image_fid']['operator'] = 'not empty';
    $handler->display->display_options['filters']['field_image_fid']['group'] = 0;
    /* Filter criterion: Content translation: Language */
    $handler->display->display_options['filters']['language']['id'] = 'language';
    $handler->display->display_options['filters']['language']['table'] = 'node';
    $handler->display->display_options['filters']['language']['field'] = 'language';
    $handler->display->display_options['filters']['language']['value'] = array(
      '***CURRENT_LANGUAGE***' => '***CURRENT_LANGUAGE***',
    );
    $handler->display->display_options['filters']['language']['group'] = 1;
    /* Filter criterion: Content translation: Language */
    $handler->display->display_options['filters']['language_1']['id'] = 'language_1';
    $handler->display->display_options['filters']['language_1']['table'] = 'node';
    $handler->display->display_options['filters']['language_1']['field'] = 'language';
    $handler->display->display_options['filters']['language_1']['value'] = array(
      'und' => 'und',
    );
    $handler->display->display_options['filters']['language_1']['group'] = 1;
    $translatables['homepage_slideshow'] = array(
      t('Master'),
      t('more'),
      t('Apply'),
      t('Reset'),
      t('Sort by'),
      t('Asc'),
      t('Desc'),
      t('queue'),
      t('Image'),
      t('Main slideshow'),
      t('View panes'),
      t('News slideshow block'),
      t('some content...'),
      t('<img src="no_slideshow_image">'),
      t('[path]'),
      t('<span class="slideshow_created">[created]</span>
    <span class="slideshow_title">[title]</span>'),
      t('<span></span>'),
      t('Recent news block'),
      t('Other news:'),
      t('Left slideshow block'),
      t('[field_link]'),
      t('_blank'),
      t('Right slideshow block'),
    );
  $export['homepage_slideshow'] = $view;

  return $export;
}
