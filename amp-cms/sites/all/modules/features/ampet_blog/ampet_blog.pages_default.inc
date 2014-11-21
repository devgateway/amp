<?php
/**
 * @file
 * ampet_blog.pages_default.inc
 */

/**
 * Implements hook_default_page_manager_pages().
 */
function ampet_blog_default_page_manager_pages() {
  $page = new stdClass();
  $page->disabled = FALSE; /* Edit this to true to make a default page disabled initially */
  $page->api_version = 1;
  $page->name = 'blog_listing';
  $page->task = 'page';
  $page->admin_title = 'Blog Listing';
  $page->admin_description = '';
  $page->path = 'blog/!type';
  $page->access = array();
  $page->menu = array();
  $page->arguments = array(
    'type' => array(
      'id' => 1,
      'identifier' => 'Blog entry type',
      'name' => 'string',
      'settings' => array(
        'use_tail' => 0,
      ),
    ),
  );
  $page->conf = array(
    'admin_paths' => FALSE,
  );
  $page->default_handlers = array();
  $handler = new stdClass();
  $handler->disabled = FALSE; /* Edit this to true to make a default handler disabled initially */
  $handler->api_version = 1;
  $handler->name = 'page_blog_listing_panel_context';
  $handler->task = 'page';
  $handler->subtask = 'blog_listing';
  $handler->handler = 'panel_context';
  $handler->weight = -29;
  $handler->conf = array(
    'title' => 'Listing',
    'no_blocks' => 0,
    'pipeline' => 'standard',
    'body_classes_to_remove' => '',
    'body_classes_to_add' => 'layout-right-sidebar',
    'css_id' => '',
    'css' => '',
    'contexts' => array(),
    'relationships' => array(),
  );
  $display = new panels_display();
  $display->layout = 'doublelayout';
  $display->layout_settings = array(
    'items' => array(
      'canvas' => array(
        'type' => 'row',
        'contains' => 'column',
        'children' => array(
          0 => 'main',
        ),
        'parent' => NULL,
      ),
      'main' => array(
        'type' => 'column',
        'width' => 100,
        'width_type' => '%',
        'children' => array(
          0 => 'main-row',
        ),
        'parent' => 'canvas',
      ),
      'main-row' => array(
        'type' => 'row',
        'contains' => 'region',
        'children' => array(
          0 => 'center',
          1 => 'sidebar',
        ),
        'parent' => 'main',
      ),
      'center' => array(
        'type' => 'region',
        'title' => 'Center',
        'width' => '39.55827807721361',
        'width_type' => '%',
        'parent' => 'main-row',
        'class' => '',
      ),
      'sidebar' => array(
        'type' => 'region',
        'title' => 'Sidebar',
        'width' => '60.44172192278639',
        'width_type' => '%',
        'parent' => 'main-row',
        'class' => '',
      ),
    ),
  );
  $display->panel_settings = array(
    'style_settings' => array(
      'center' => array(
        'css_id' => '',
        'region_classes' => array(
          'layout-block' => 'layout-block',
        ),
        'extra_classes' => '',
      ),
      'default' => NULL,
      'sidebar' => NULL,
      'left' => array(
        'region_classes' => array(
          'layout-block' => 'layout-block',
          'layout-separator' => 'layout-separator',
        ),
        'css_id' => '',
        'extra_classes' => '',
      ),
      'right' => NULL,
    ),
    'center' => array(
      'style' => 'cbstyle',
    ),
    'left' => array(
      'style' => 'cbstyle',
    ),
  );
  $display->cache = array();
  $display->title = '';
  $display->uuid = '2068dfa4-d7f6-43c6-8236-99f8ec66dfeb';
  $display->content = array();
  $display->panels = array();
    $pane = new stdClass();
    $pane->pid = 'new-55b153a5-5c99-47e0-b729-4310bc433690';
    $pane->panel = 'left';
    $pane->type = 'views_panes';
    $pane->subtype = 'blog_listing-listing';
    $pane->shown = TRUE;
    $pane->access = array(
      'plugins' => array(
        0 => array(
          'name' => 'string_equal',
          'settings' => array(
            'operator' => '!=',
            'value' => 'news-and-events',
            'case' => 0,
          ),
          'context' => 'argument_string_1',
          'not' => FALSE,
        ),
      ),
    );
    $pane->configuration = array(
      'override_title' => 0,
      'override_title_text' => '',
      'arguments' => array(
        'field_blog_entry_type_value' => '',
      ),
    );
    $pane->cache = array();
    $pane->style = array(
      'settings' => array(
        'pane_classes' => array(
          'layout-separator' => 'layout-separator',
        ),
      ),
      'style' => 'cbstyle',
    );
    $pane->css = array(
      'css_id' => '',
      'css_class' => 'blog-listing-page two-columns-grid',
    );
    $pane->extras = array();
    $pane->position = 0;
    $pane->locks = array();
    $pane->uuid = '55b153a5-5c99-47e0-b729-4310bc433690';
    $display->content['new-55b153a5-5c99-47e0-b729-4310bc433690'] = $pane;
    $display->panels['left'][0] = 'new-55b153a5-5c99-47e0-b729-4310bc433690';
    $pane = new stdClass();
    $pane->pid = 'new-73fac06a-b9c2-44b0-87e8-af5decb62053';
    $pane->panel = 'left';
    $pane->type = 'views_panes';
    $pane->subtype = 'blog_listing-listing';
    $pane->shown = TRUE;
    $pane->access = array(
      'plugins' => array(
        0 => array(
          'name' => 'string_equal',
          'settings' => array(
            'operator' => '=',
            'value' => 'news-and-events',
            'case' => 1,
          ),
          'context' => 'argument_string_1',
          'not' => FALSE,
        ),
      ),
    );
    $pane->configuration = array(
      'arguments' => array(
        'field_blog_entry_type_value' => 'news+events',
      ),
      'override_title' => 0,
      'override_title_text' => '',
    );
    $pane->cache = array();
    $pane->style = array(
      'settings' => array(
        'pane_classes' => array(
          'layout-separator' => 'layout-separator',
        ),
      ),
      'style' => 'cbstyle',
    );
    $pane->css = array(
      'css_id' => '',
      'css_class' => 'blog-listing-page two-columns-grid',
    );
    $pane->extras = array();
    $pane->position = 1;
    $pane->locks = array();
    $pane->uuid = '73fac06a-b9c2-44b0-87e8-af5decb62053';
    $display->content['new-73fac06a-b9c2-44b0-87e8-af5decb62053'] = $pane;
    $display->panels['left'][1] = 'new-73fac06a-b9c2-44b0-87e8-af5decb62053';
    $pane = new stdClass();
    $pane->pid = 'new-393bf714-7fc9-4a31-af3f-e73b5471db64';
    $pane->panel = 'right';
    $pane->type = 'cbreference';
    $pane->subtype = 'cbreference';
    $pane->shown = TRUE;
    $pane->access = array();
    $pane->configuration = array(
      'machine_name' => 'blogsidebar',
      'override_title' => 0,
      'override_title_text' => '',
      'style' => 'normal',
    );
    $pane->cache = array();
    $pane->style = array(
      'settings' => NULL,
    );
    $pane->css = array();
    $pane->extras = array();
    $pane->position = 0;
    $pane->locks = array();
    $pane->uuid = '393bf714-7fc9-4a31-af3f-e73b5471db64';
    $display->content['new-393bf714-7fc9-4a31-af3f-e73b5471db64'] = $pane;
    $display->panels['right'][0] = 'new-393bf714-7fc9-4a31-af3f-e73b5471db64';
    $pane = new stdClass();
    $pane->pid = 'new-37e37bc6-d4d2-4622-8786-a9d55cc86e51';
    $pane->panel = 'right';
    $pane->type = 'cbreference';
    $pane->subtype = 'cbreference';
    $pane->shown = TRUE;
    $pane->access = array();
    $pane->configuration = array(
      'machine_name' => 'linkedmap',
      'override_title' => 0,
      'override_title_text' => '',
      'style' => 'normal',
    );
    $pane->cache = array();
    $pane->style = array(
      'settings' => NULL,
    );
    $pane->css = array();
    $pane->extras = array();
    $pane->position = 1;
    $pane->locks = array();
    $pane->uuid = '37e37bc6-d4d2-4622-8786-a9d55cc86e51';
    $display->content['new-37e37bc6-d4d2-4622-8786-a9d55cc86e51'] = $pane;
    $display->panels['right'][1] = 'new-37e37bc6-d4d2-4622-8786-a9d55cc86e51';
  $display->hide_title = PANELS_TITLE_NONE;
  $display->title_pane = 'new-55b153a5-5c99-47e0-b729-4310bc433690';
  $handler->conf['display'] = $display;
  $page->default_handlers[$handler->name] = $handler;
  $pages['blog_listing'] = $page;

  return $pages;

}
