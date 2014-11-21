<?php
/**
 * @file
 * amphomepage.pages_default.inc
 */

/**
 * Implements hook_default_page_manager_pages().
 */
function amphomepage_default_page_manager_pages() {
  $page = new stdClass();
  $page->disabled = FALSE; /* Edit this to true to make a default page disabled initially */
  $page->api_version = 1;
  $page->name = 'homepage';
  $page->task = 'page';
  $page->admin_title = 'Homepage';
  $page->admin_description = '';
  $page->path = 'homepage';
  $page->access = array();
  $page->menu = array();
  $page->arguments = array();
  $page->conf = array(
    'admin_paths' => FALSE,
  );
  $page->default_handlers = array();
  $handler = new stdClass();
  $handler->disabled = FALSE; /* Edit this to true to make a default handler disabled initially */
  $handler->api_version = 1;
  $handler->name = 'page_homepage_panel_context';
  $handler->task = 'page';
  $handler->subtask = 'homepage';
  $handler->handler = 'panel_context';
  $handler->weight = 0;
  $handler->conf = array(
    'title' => 'Panel',
    'no_blocks' => 0,
    'pipeline' => 'standard',
    'body_classes_to_remove' => '',
    'body_classes_to_add' => '',
    'css_id' => '',
    'css' => '',
    'contexts' => array(),
    'relationships' => array(),
  );
  $display = new panels_display();
  $display->layout = 'homelayout';
  $display->layout_settings = array();
  $display->panel_settings = array(
    'style_settings' => array(
      'default' => NULL,
      'middle' => NULL,
      'col_a1' => array(
        'region_classes' => array(
          'clearfix' => 'clearfix',
          'layout-separator' => 'layout-separator',
        ),
        'css_id' => '',
        'extra_classes' => 'homepage-featured-block',
      ),
      'col_b1' => NULL,
      'col_c1' => NULL,
      'col_c2' => NULL,
    ),
    'col_a1' => array(
      'style' => 'cbstyle',
    ),
  );
  $display->cache = array();
  $display->title = 'Homepage';
  $display->uuid = 'b19fafba-6766-4546-8297-028cdc1d8e00';
  $display->content = array();
  $display->panels = array();
    $pane = new stdClass();
    $pane->pid = 'new-0e11dc69-3e89-4097-b411-0fe4d457d8c4';
    $pane->panel = 'col_a1';
    $pane->type = 'cbreference';
    $pane->subtype = 'cbreference';
    $pane->shown = TRUE;
    $pane->access = array();
    $pane->configuration = array(
      'machine_name' => 'welcome',
      'override_title' => 0,
      'override_title_text' => '',
      'style' => 'normal',
    );
    $pane->cache = array();
    $pane->style = array(
      'settings' => NULL,
    );
    $pane->css = array(
      'css_id' => '',
      'css_class' => 'homepage-welcome-block featured-dark-block clearfix',
    );
    $pane->extras = array();
    $pane->position = 0;
    $pane->locks = array();
    $pane->uuid = '0e11dc69-3e89-4097-b411-0fe4d457d8c4';
    $display->content['new-0e11dc69-3e89-4097-b411-0fe4d457d8c4'] = $pane;
    $display->panels['col_a1'][0] = 'new-0e11dc69-3e89-4097-b411-0fe4d457d8c4';
    $pane = new stdClass();
    $pane->pid = 'new-de8ccb7b-ba00-4de0-b3c3-330669076ae6';
    $pane->panel = 'col_a1';
    $pane->type = 'embeddedform';
    $pane->subtype = 'embeddedform';
    $pane->shown = TRUE;
    $pane->access = array();
    $pane->configuration = array(
      'form_callback' => 'ampapi_activity_homepage_search_form',
      'override_title' => 0,
      'override_title_text' => '',
    );
    $pane->cache = array();
    $pane->style = array(
      'settings' => NULL,
    );
    $pane->css = array(
      'css_id' => '',
      'css_class' => 'homepage-form-block featured-grey-block clearfix',
    );
    $pane->extras = array();
    $pane->position = 1;
    $pane->locks = array();
    $pane->uuid = 'de8ccb7b-ba00-4de0-b3c3-330669076ae6';
    $display->content['new-de8ccb7b-ba00-4de0-b3c3-330669076ae6'] = $pane;
    $display->panels['col_a1'][1] = 'new-de8ccb7b-ba00-4de0-b3c3-330669076ae6';
    $pane = new stdClass();
    $pane->pid = 'new-1c8d39e2-6fba-41bf-92b5-a71bd4312ea9';
    $pane->panel = 'col_b1';
    $pane->type = 'cbreference';
    $pane->subtype = 'cbreference';
    $pane->shown = TRUE;
    $pane->access = array();
    $pane->configuration = array(
      'machine_name' => 'linkedmap',
      'override_title' => 0,
      'override_title_text' => '',
      'style' => 'wide',
    );
    $pane->cache = array();
    $pane->style = array(
      'settings' => NULL,
    );
    $pane->css = array();
    $pane->extras = array();
    $pane->position = 0;
    $pane->locks = array();
    $pane->uuid = '1c8d39e2-6fba-41bf-92b5-a71bd4312ea9';
    $display->content['new-1c8d39e2-6fba-41bf-92b5-a71bd4312ea9'] = $pane;
    $display->panels['col_b1'][0] = 'new-1c8d39e2-6fba-41bf-92b5-a71bd4312ea9';
    $pane = new stdClass();
    $pane->pid = 'new-1c8790d9-6462-463f-bad4-a6b334250708';
    $pane->panel = 'col_b1';
    $pane->type = 'cbreference';
    $pane->subtype = 'cbreference';
    $pane->shown = TRUE;
    $pane->access = array();
    $pane->configuration = array(
      'machine_name' => 'homewidgets',
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
    $pane->uuid = '1c8790d9-6462-463f-bad4-a6b334250708';
    $display->content['new-1c8790d9-6462-463f-bad4-a6b334250708'] = $pane;
    $display->panels['col_b1'][1] = 'new-1c8790d9-6462-463f-bad4-a6b334250708';
  $display->hide_title = PANELS_TITLE_NONE;
  $display->title_pane = '0';
  $handler->conf['display'] = $display;
  $page->default_handlers[$handler->name] = $handler;
  $pages['homepage'] = $page;

  return $pages;

}
