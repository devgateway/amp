<?php
/**
 * @file
 * ampet_blog.ds.inc
 */

/**
 * Implements hook_ds_field_settings_info().
 */
function ampet_blog_ds_field_settings_info() {
  $export = array();

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'node|blog|default';
  $ds_fieldsetting->entity_type = 'node';
  $ds_fieldsetting->bundle = 'blog';
  $ds_fieldsetting->view_mode = 'default';
  $ds_fieldsetting->settings = array(
    'title_field' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
  );
  $export['node|blog|default'] = $ds_fieldsetting;

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'node|blog|full';
  $ds_fieldsetting->entity_type = 'node';
  $ds_fieldsetting->bundle = 'blog';
  $ds_fieldsetting->view_mode = 'full';
  $ds_fieldsetting->settings = array(
    'blog_sidebar' => array(
      'weight' => '4',
      'label' => 'hidden',
      'format' => 'default',
      'formatter_settings' => array(
        'show_title' => 0,
        'title_wrapper' => '',
        'ctools' => 'a:3:{s:4:"conf";a:10:{s:13:"variable_name";s:0:"";s:10:"identifier";s:0:"";s:10:"build_mode";s:7:"default";s:5:"links";N;s:12:"remove_title";N;s:12:"linked_title";N;s:14:"override_title";i:1;s:19:"override_title_text";s:0:"";s:12:"machine_name";s:11:"blogsidebar";s:5:"style";s:6:"normal";}s:4:"type";s:11:"cbreference";s:7:"subtype";s:11:"cbreference";}',
        'load_terms' => 0,
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
    'linkedmap_block' => array(
      'weight' => '5',
      'label' => 'hidden',
      'format' => 'default',
      'formatter_settings' => array(
        'show_title' => 0,
        'title_wrapper' => '',
        'ctools' => 'a:3:{s:4:"conf";a:5:{s:13:"variable_name";s:0:"";s:12:"machine_name";s:9:"linkedmap";s:5:"style";s:6:"normal";s:14:"override_title";i:0;s:19:"override_title_text";s:0:"";}s:4:"type";s:11:"cbreference";s:7:"subtype";s:11:"cbreference";}',
        'load_terms' => 0,
        'ft' => array(
          'classes' => 'cbreference-block cbreference-linkedmap cbreference-view-mode-normal',
        ),
      ),
    ),
    'title_field' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
  );
  $export['node|blog|full'] = $ds_fieldsetting;

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'node|blog|teaser_featured';
  $ds_fieldsetting->entity_type = 'node';
  $ds_fieldsetting->bundle = 'blog';
  $ds_fieldsetting->view_mode = 'teaser_featured';
  $ds_fieldsetting->settings = array(
    'title_field' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
    'body' => array(
      'formatter_settings' => array(
        'ft' => array(),
      ),
    ),
  );
  $export['node|blog|teaser_featured'] = $ds_fieldsetting;

  $ds_fieldsetting = new stdClass();
  $ds_fieldsetting->api_version = 1;
  $ds_fieldsetting->id = 'node|blog|teaser_normal';
  $ds_fieldsetting->entity_type = 'node';
  $ds_fieldsetting->bundle = 'blog';
  $ds_fieldsetting->view_mode = 'teaser_normal';
  $ds_fieldsetting->settings = array(
    'title_field' => array(
      'formatter_settings' => array(
        'ft' => array(
          'func' => 'theme_ds_field_reset',
        ),
      ),
    ),
    'body' => array(
      'formatter_settings' => array(
        'ft' => array(),
      ),
    ),
  );
  $export['node|blog|teaser_normal'] = $ds_fieldsetting;

  return $export;
}

/**
 * Implements hook_ds_custom_fields_info().
 */
function ampet_blog_ds_custom_fields_info() {
  $export = array();

  $ds_field = new stdClass();
  $ds_field->api_version = 1;
  $ds_field->field = 'blog_sidebar';
  $ds_field->label = 'Blog Sidebar';
  $ds_field->field_type = 7;
  $ds_field->entities = array(
    'node' => 'node',
  );
  $ds_field->ui_limit = 'blog|full';
  $ds_field->properties = array(
    'default' => array(),
    'settings' => array(
      'show_title' => array(
        'type' => 'checkbox',
      ),
      'title_wrapper' => array(
        'type' => 'textfield',
        'description' => 'Eg: h1, h2, p',
      ),
      'ctools' => array(
        'type' => 'ctools',
      ),
    ),
  );
  $export['blog_sidebar'] = $ds_field;

  return $export;
}

/**
 * Implements hook_ds_layout_settings_info().
 */
function ampet_blog_ds_layout_settings_info() {
  $export = array();

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'node|blog|default';
  $ds_layout->entity_type = 'node';
  $ds_layout->bundle = 'blog';
  $ds_layout->view_mode = 'default';
  $ds_layout->layout = 'simplelayout';
  $ds_layout->settings = array(
    'regions' => array(
      'main' => array(
        0 => 'title_field',
        1 => 'body',
      ),
    ),
    'fields' => array(
      'title_field' => 'main',
      'body' => 'main',
    ),
    'classes' => array(
      'layout_class' => array(
        'clearfix' => 'clearfix',
      ),
    ),
    'wrappers' => array(
      'main' => 'div',
      'ds_hidden' => 'div',
    ),
    'layout_wrapper' => 'div',
    'layout_attributes' => '',
    'layout_attributes_merge' => 1,
    'layout_link_attribute' => '',
    'layout_link_custom' => '',
  );
  $export['node|blog|default'] = $ds_layout;

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'node|blog|full';
  $ds_layout->entity_type = 'node';
  $ds_layout->bundle = 'blog';
  $ds_layout->view_mode = 'full';
  $ds_layout->layout = 'doublelayout';
  $ds_layout->settings = array(
    'regions' => array(
      'left' => array(
        0 => 'group_title_wrapper',
        1 => 'title_field',
        2 => 'field_blog_event_date',
        3 => 'body',
      ),
      'right' => array(
        4 => 'blog_sidebar',
        5 => 'linkedmap_block',
      ),
    ),
    'fields' => array(
      'group_title_wrapper' => 'left',
      'title_field' => 'left',
      'field_blog_event_date' => 'left',
      'body' => 'left',
      'blog_sidebar' => 'right',
      'linkedmap_block' => 'right',
    ),
    'classes' => array(
      'layout_class' => array(
        'clearfix' => 'clearfix',
        'layout-right-sidebar' => 'layout-right-sidebar',
      ),
      'left' => array(
        'layout-block' => 'layout-block',
        'layout-separator' => 'layout-separator',
      ),
      'right' => array(
        'layout-separator' => 'layout-separator',
      ),
    ),
    'wrappers' => array(
      'left' => 'div',
      'right' => 'div',
      'ds_hidden' => 'div',
    ),
    'layout_wrapper' => 'div',
    'layout_attributes' => '',
    'layout_attributes_merge' => 1,
    'layout_link_attribute' => '',
    'layout_link_custom' => '',
    'hide_page_title' => '1',
    'page_option_title' => '',
  );
  $export['node|blog|full'] = $ds_layout;

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'node|blog|teaser_featured';
  $ds_layout->entity_type = 'node';
  $ds_layout->bundle = 'blog';
  $ds_layout->view_mode = 'teaser_featured';
  $ds_layout->layout = 'mobject';
  $ds_layout->settings = array(
    'regions' => array(
      'side' => array(
        0 => 'field_blog_image',
      ),
      'main' => array(
        1 => 'title_field',
        2 => 'body',
      ),
    ),
    'fields' => array(
      'field_blog_image' => 'side',
      'title_field' => 'main',
      'body' => 'main',
    ),
    'classes' => array(
      'layout_class' => array(
        'clearfix' => 'clearfix',
      ),
    ),
    'wrappers' => array(
      'side' => 'div',
      'main' => 'div',
      'ds_hidden' => 'div',
    ),
    'layout_wrapper' => 'div',
    'layout_attributes' => '',
    'layout_attributes_merge' => 1,
    'layout_link_attribute' => '',
    'layout_link_custom' => '',
  );
  $export['node|blog|teaser_featured'] = $ds_layout;

  $ds_layout = new stdClass();
  $ds_layout->api_version = 1;
  $ds_layout->id = 'node|blog|teaser_normal';
  $ds_layout->entity_type = 'node';
  $ds_layout->bundle = 'blog';
  $ds_layout->view_mode = 'teaser_normal';
  $ds_layout->layout = 'simplelayout';
  $ds_layout->settings = array(
    'regions' => array(
      'main' => array(
        0 => 'field_blog_image',
        1 => 'title_field',
        2 => 'body',
      ),
    ),
    'fields' => array(
      'field_blog_image' => 'main',
      'title_field' => 'main',
      'body' => 'main',
    ),
    'classes' => array(
      'layout_class' => array(
        'clearfix' => 'clearfix',
      ),
    ),
    'wrappers' => array(
      'main' => 'div',
      'ds_hidden' => 'div',
    ),
    'layout_wrapper' => 'div',
    'layout_attributes' => '',
    'layout_attributes_merge' => 1,
    'layout_link_attribute' => '',
    'layout_link_custom' => '',
  );
  $export['node|blog|teaser_normal'] = $ds_layout;

  return $export;
}
