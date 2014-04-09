<?php
/**
 * @file
 * Contains administrative forms and settings.
 */

/**
 * Delegated hook_menu for admin
 *
 * Since most of hook_menu is delegated to plugins anyway, the admin menu
 * is delegated here to reduce code weight.
 */
function panelizer_admin_hook_menu(&$items) {
  // @todo -- delegate these to admin.inc
  // Configure settings pages.
  $settings_base = array(
    'access arguments' => array('administer panelizer'),
    'file' => 'includes/admin.inc',
  );

  $items['admin/config/content/panelizer'] = array(
    'title' => 'Panelizer',
    'description' => 'Configure panelizer availability and defaults',
    'page callback' => 'drupal_get_form',
    'page arguments' => array('panelizer_settings_page_form'),
    'type' => MENU_NORMAL_ITEM,
  ) + $settings_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%'] = array(
    'title callback' => 'panelizer_default_title_callback',
    'title arguments' => array(4, 5),
    'page callback' => 'panelizer_allowed_content_page',
    'page arguments' => array(4, 5),
    'type' => MENU_CALLBACK,
  ) + $settings_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%/allowed'] = array(
    'title' => 'Available content',
    'page callback' => 'panelizer_allowed_content_page',
    'page arguments' => array(4, 5),
    'type' => MENU_DEFAULT_LOCAL_TASK,
    'weight' => -10,
  ) + $settings_base;

  $tabs_base = array(
    'access callback' => 'panelizer_has_no_choice_callback',
    'access arguments' => array(4, 5),
    'page arguments' => array(4, 5, 'default'),
    'type' => MENU_LOCAL_TASK,
    'file' => 'includes/admin.inc',
  );

  $items['admin/config/content/panelizer/%panelizer_handler/%/settings'] = array(
    'title' => 'Settings',
    'page callback' => 'panelizer_default_settings_page',
    'weight' => -5,
  ) + $tabs_base;


  $items['admin/config/content/panelizer/%panelizer_handler/%/context'] = array(
    'title' => 'Contexts',
    'page callback' => 'panelizer_default_context_page',
    'weight' => -4,
  ) + $tabs_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%/layout'] = array(
    'title' => 'Layout',
    'page callback' => 'panelizer_default_layout_page',
    'weight' => -3,
  ) + $tabs_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%/content'] = array(
    'title' => 'Content',
    'page callback' => 'panelizer_default_content_page',
    'weight' => -2,
  ) + $tabs_base;

  $subtabs_base = array(
    'access callback' => 'panelizer_has_choice_callback',
    'access arguments' => array(4, 5, 7),
    'page arguments' => array(4, 5, 7),
    'type' => MENU_LOCAL_TASK,
    'file' => 'includes/admin.inc',
  );

  $items['admin/config/content/panelizer/%panelizer_handler/%/list/%'] = array(
    'title' => 'Settings',
    'page callback' => 'panelizer_default_settings_page',
    'title callback' => 'panelizer_default_name_title_callback',
    'type' => MENU_CALLBACK,
  ) + $subtabs_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%/list/%/settings'] = array(
    'title' => 'Settings',
    'page callback' => 'panelizer_default_settings_page',
    'weight' => -5,
  ) + $subtabs_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%/list/%/context'] = array(
    'title' => 'Contexts',
    'page callback' => 'panelizer_default_context_page',
    'weight' => -4,
  ) + $subtabs_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%/list/%/layout'] = array(
    'title' => 'Layout',
    'page callback' => 'panelizer_default_layout_page',
    'weight' => -3,
  ) + $subtabs_base;

  $items['admin/config/content/panelizer/%panelizer_handler/%/list/%/content'] = array(
    'title' => 'Content',
    'page callback' => 'panelizer_default_content_page',
    'weight' => -2,
  ) + $subtabs_base;
}

/**
 * Primary settings page.
 *
 * This settings page allows the administrator to select which node types
 * can be panelized, whether they have a default, and provides links to
 * edit those defaults.
 */
function panelizer_settings_page_form($form, &$form_state) {
  $form['entities'] = array('#tree' => TRUE);

  $plugins = panelizer_get_entity_plugins();
  foreach ($plugins as $entity_type => $plugin) {
    $entity_info = entity_get_info($entity_type);
    if ($entity_info) {
      if ($handler = panelizer_entity_plugin_get_handler($entity_type)) {
        $handler->settings_form($form, $form_state);
      }
    }
  }

  $form['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Save'),
  );

  return $form;
}

/**
 * Format the output of the main settings form.
 *
 * We want our checkboxes to show up in a table.
 */
function theme_panelizer_settings_page_table($variables) {
  $element = $variables['element'];

  // Render the table
  $header = $element['#header'];
  $columns = $element['#columns'];

  $rows = array();
  foreach (element_children($element) as $bundle) {
    $row = array();
    foreach ($columns as $column) {
      $row[] = drupal_render($element[$bundle][$column]);
    }

    $rows[] = $row;
  }

  $output = theme('table', array('header' => $header, 'rows' => $rows));

  // Render everything else
  $output .= drupal_render_children($element);
  return $output;
}

function panelizer_settings_page_form_validate($form, &$form_state) {
  $plugins = panelizer_get_entity_plugins();
  foreach ($plugins as $entity_type => $plugin) {
    $entity_info = entity_get_info($entity_type);
    if ($entity_info) {
      if ($handler = panelizer_entity_plugin_get_handler($entity_type)) {
        $handler->settings_form_validate($form, $form_state);
      }
    }
  }
}

function panelizer_settings_page_form_submit($form, &$form_state) {
  $plugins = panelizer_get_entity_plugins();
  foreach ($plugins as $entity_type => $plugin) {
    $entity_info = entity_get_info($entity_type);
    if ($entity_info) {
      if ($handler = panelizer_entity_plugin_get_handler($entity_type)) {
        $handler->settings_form_submit($form, $form_state);
      }
    }
  }

  // Ensure that defaults get cached to recognize new settings immediately.
  drupal_flush_all_caches();
}

/**
 * Page to configure what content is available for a given node type.
 */
function panelizer_allowed_content_page($handler, $bundle) {
  if (!$handler->is_panelized($bundle)) {
    return MENU_NOT_FOUND;
  }

  ctools_include('common', 'panels');
  return drupal_get_form('panels_common_settings', 'panelizer_' . $handler->entity_type . ':' . $bundle);
}


/**
 * Page to configure basic settings for a panelizer default.
 */
function panelizer_default_settings_page($handler, $bundle, $name) {
  $panelizer = $handler->get_default_panelizer_object($bundle, $name);
  if (empty($panelizer)) {
    return MENU_NOT_FOUND;
  }

  $form_state = array(
    'panelizer' => &$panelizer,
    'no_redirect' => TRUE,
    'reset button' => TRUE,
  );

  if ($handler->has_panel_choice($bundle)) {
    $form_state['has title'] = TRUE;
  }

  ctools_include('common', 'panelizer');
  $output = drupal_build_form('panelizer_settings_form', $form_state);
  if (!empty($form_state['executed'])) {
    if (empty($form_state['clicked_button']['#reset'])) {
      ctools_include('export');
      ctools_export_crud_save('panelizer_defaults', $panelizer);
      drupal_set_message(t('The settings have been updated.'));
    }
    else {
      panelizer_delete_panelizer_defaults($panelizer);
      drupal_set_message(t('The panelizer settings have been reset to the default.'));
    }

    drupal_goto($_GET['q']);
  }

  return $output;
}

/**
 * Delete a panelizer node panel from the database.
 */
function panelizer_delete_panelizer_defaults($panelizer) {
  if (!empty($panelizer->pnid)) {
    if (!empty($panelizer->did)) {
      panels_delete_display($panelizer->did);
    }
    db_delete('panelizer_defaults')
      ->condition('pnid', $panelizer->pnid)
      ->execute();
  }
}

/**
 * Page to configure what content is available for a given node type.
 */
function panelizer_default_context_page($handler, $bundle, $name) {
  $cache_key = $handler->entity_type . ':' . $bundle . ':' . $name;
  $panelizer = panelizer_context_cache_get('default', $cache_key);
  if (empty($panelizer)) {
    return MENU_NOT_FOUND;
  }

  $form_state = array(
    'panelizer' => &$panelizer,
    'panelizer type' => 'default',
    'cache key' => $cache_key,
    'no_redirect' => TRUE,
  );

  ctools_include('common', 'panelizer');
  $output = drupal_build_form('panelizer_default_context_form', $form_state);
  if (!empty($form_state['executed'])) {
    if (!empty($form_state['clicked_button']['#write'])) {
      drupal_set_message(t('The settings have been updated.'));
      ctools_include('export');
      ctools_export_crud_save('panelizer_defaults', $panelizer);
    }
    else {
      drupal_set_message(t('Changes have been discarded.'));
    }

    panelizer_context_cache_clear('default', $cache_key);
    drupal_goto($_GET['q']);
  }

  return $output;
}

/**
 * Pass through to the panels layout editor.
 */
function panelizer_default_layout_page($handler, $bundle, $name, $step = NULL, $layout = NULL) {
  $panelizer = $handler->get_default_panelizer_object($bundle, $name);
  if (empty($panelizer)) {
    return MENU_NOT_FOUND;
  }

  $display = $panelizer->display;
  $display->context = $handler->get_contexts($panelizer);

  if ($name == 'default' && $handler->has_default_panel($bundle)) {
    $path = 'admin/config/content/panelizer/' . $handler->entity_type . '/' . $bundle;
  }
  else {
    $path = 'admin/config/content/panelizer/' . $handler->entity_type . '/' . $bundle . '/list/' . $name ;
  }

  $form_state = array(
    'display' => $display,
    'wizard path' => $path . '/layout/%step',
    'allowed_layouts' => 'panelizer_' . $handler->entity_type . ':' . $bundle,
  );

  ctools_include('common', 'panelizer');
  $output = panelizer_change_layout_wizard($form_state, $step, $layout);
  if (!empty($form_state['complete'])) {
    $panelizer->display = $form_state['display'];
    ctools_export_crud_save('panelizer_defaults', $panelizer);
    drupal_set_message(t('The layout has been changed.'));
    drupal_goto($path . '/content');
  }

  return $output;
}

/**
 * Pass through to the panels content editor.
 */
function panelizer_default_content_page($handler, $bundle, $name) {
  $panelizer = $handler->get_default_panelizer_object($bundle, $name);
  if (empty($panelizer)) {
    return MENU_NOT_FOUND;
  }

  $cache_key = 'panelizer:default:' . $handler->entity_type . ':' . $bundle . ':' . $name;

  $form_state = array(
    'display cache' => panels_edit_cache_get($cache_key),
    'no_redirect' => TRUE,
  );

  ctools_include('common', 'panelizer');
  $output = drupal_build_form('panelizer_edit_content_form', $form_state);
  if (!empty($form_state['executed'])) {
    if (!empty($form_state['clicked_button']['#save-display'])) {
      drupal_set_message(t('The settings have been updated.'));
      $panelizer->display = $form_state['display'];
      ctools_export_crud_save('panelizer_defaults', $panelizer);
    }
    else {
      drupal_set_message(t('Changes have been discarded.'));
    }

    panels_edit_cache_clear($form_state['display cache']);
    drupal_goto($_GET['q']);
  }

  ctools_set_no_blocks(FALSE);
  drupal_set_page_content($output);
  $page = element_info('page');
  return $page;
}
