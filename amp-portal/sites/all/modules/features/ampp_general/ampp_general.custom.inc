<?php


/*
 * implementation hook_ctools_plugin_directory
 */
function ampp_general_ctools_plugin_directory($owner, $plugin_type) {
    if($owner == 'ctools' && $plugin_type == 'content_types') {
        return 'plugins/content_types';
    }
}


/*
 * Implementing hook_menu_alter()
 */
function ampp_general_menu_alter(&$items) {
  $items['node/%node/panelizer']['page arguments'] = $items['node/%node/panelizer/content']['page arguments'];
  unset($items['node/%node/panelizer/settings']);
  unset($items['node/%node/panelizer/context']);
  $items['node/%node/panelizer/content']['type'] = MENU_DEFAULT_LOCAL_TASK;

  unset($items['admin/appearance/install']);
}


/**
 * Implements hook_views_pre_view.
 */
/*
function ampp_general_views_pre_view(&$view, &$display_id, &$args){
  // Make sure this is only for the specific view you want to modified
  if ($view->name == "homepage_slideshow" && $display_id == 'block_4') {
    $view->set_items_per_page(variable_get('site_frontpage_news_teaser_records', 6));
  }
}
*/


/*
 * Implementing hook_form_alter()
 */
function ampp_general_form_alter(&$form, &$form_state, $form_id) {

  // customize panels custom content edit form
  if ($form_id == 'ctools_custom_content_type_edit_form') {
    $form['substitute']['#access'] = 0;
    $form['contexts']['#access'] = 0;
    $form['legend']['#access'] = 0;
  }

  // adding a new flag to menu items
  if ($form_id == 'menu_edit_item') {
    $form['external'] = array (
      '#type' => 'checkbox',
      '#title' => t('Open in a new browser window'),
      '#default_value' => variable_get('menu_edit_item_menu-' . $form['mlid']['#value'], 0),
      '#description' => t('Forces the item to open in a new browser window.'),
      '#weight' => 1,
    );
    array_unshift($form['#submit'], '_menu_edit_item_external');
  }
}


/*
 *
 */
function _menu_edit_item_external($form, $form_state) {
  if ($form_state['values']['external']) {
    $form_state['values']['options']['attributes']['target'] = '_blank';
  }
  variable_set('menu_edit_item_menu-' . $form_state['values']['mlid'], $form_state['values']['external']);
}