<?php
/**
 * @file
 * Contains hooks implementations that deal with cleaning up various forms.
 */

/**
 * Implements hook_update_projects_alter().
 *
 * Hide project-specific modules from the module update list.
 */
function helpergeneric_update_projects_alter(&$projects) {
  $not_updated = array();
  foreach ($projects as $project_name => $project_info) {
    if ($project_info['project_type'] == 'core') {
      continue;
    }

    // Determine where the module is located.
    $projet_path = drupal_get_path($project_info['project_type'], $project_info['name']);

    // Projects like 'features_extra' don't contain a module called 'features_extra' so an empty path is returned.
    $project_location = '';
    if (!empty($projet_path)) {
      $project_location = dirname($projet_path);
    }

    // Do not query updates for custom modules and features.
    switch ($project_location) {
      case 'sites/all/modules/custom':
      case 'sites/all/modules/features':
        // Hide project-specific modules from the update list.
        $not_updated[] = $projects[$project_name]['info']['name'];
        unset($projects[$project_name]);
        break;
    }
  }

  // if (!empty($not_updated)) {
  //   $message = 'The following modules are not being checked for updates: @moduleslist';
  //   drupal_set_message(t($message, array('@moduleslist' => implode(', ', $not_updated))), 'warning');
  // }
}

/**
 * Implements hook_features_api_alter().
 *
 * Prevent exporting of some features API components.
 */
function helpergeneric_features_api_alter(&$components) {
  // We manage menu_links manually or via update scripts.
  unset($components['menu_links']);
  unset($components['variable_realm']);
}

/**
 * Implements hook_features_component_export_alter().
 *
 * Filter the available compoenents in the (re)create feature form.
 */
function helpergeneric_features_component_export_alter(&$component_export, $component) {
  switch ($component) {
    case 'dependencies':
      // Hide custom features from dependencies.
      $entries_list = array(
        'ampcontentblock',
        'ampet_blog',
        'ampet_page',
        'ampfileentity',
        'amphomepage',
        'amppermissions',
        'ampsettings',
        'ampviewsettings',
      );
      break;

    case 'menu_custom':
      // Hide menus.
      $entries_list = array(
        'devel',
        // 'main-menu',
        'management',
        'navigation',
        'user-menu',
      );
      break;

    case 'user_permission':
      $entries_list = array(
        // Hide admin_menu module permissions.
        'access administration menu',
        'flush caches',
        'display drupal links',

        // Hide devel module permissions.
        'access devel information',
        'execute php code',
        'switch users',

        // Hide ds_ui module permissions.
        'admin_view_modes',
        'admin_fields',
        'admin_classes',

        // Hide less module permissions.
        'administer less',
      );
      break;

    case 'variable':
      // Hide variables that should not be exported.
      $entries_list = array(
        // Custom variables.
        'date_format_shortest',
        'helpergeneric_cleanup_ui_force',
        'helpergeneric_disabled_languages',
        // 'helpertheme_load_external_fonts',
        'update_scripts_user_one_updated',

        // Hide CORE variables.
        'additional_settings__active_tab_blog',
        'additional_settings__active_tab_page',
        'cache',
        'cache_lifetime',
        'clean_url',
        'cron_key',
        'cron_last',
        'cron_semaphore',
        'css_js_query_string',
        'date_default_timezone',
        'date_first_day',
        'date_format_long',
        'date_format_medium',
        'date_format_short',
        'dblog_row_limit',
        'drupal_private_key',
        'drupal_css_cache_files',
        'drupal_http_request_fails',
        'drupal_js_cache_files',
        'error_level',
        'file_temporary_path',
        'htmlpurifier_version_current',
        // 'image_toolkit',
        'install_profile',
        'install_task',
        'install_time',
        'javascript_parsed',
        // 'job_scheduler_rebuild_all',
        'menu_expanded',
        'menu_masks',
        'menu_rebuild_needed',
        'page_cache_maximum_age',
        'page_compression',
        'path_alias_whitelist',
        'preprocess_css',
        'preprocess_js',
        'site_default_country',
        'site_mail',
        'site_name',
        'teaser_length',
        'theme_default',

        // Cache specific variables.
        'cache_flush_cache',
        'cache_flush_cache_field',
        'cache_flush_cache_filter',
        'cache_flush_cache_form',
        'cache_flush_cache_htmlpurifier',
        'cache_flush_cache_image',
        'cache_flush_cache_libraries',
        'cache_flush_cache_menu',
        'cache_flush_cache_page',
        'cache_flush_cache_panels',
        'cache_flush_cache_path',
        'cache_flush_cache_restclient',
        'cache_flush_cache_token',
        'cache_flush_cache_variable',
        'cache_flush_cache_views',
        'cache_flush_cache_views_data',

        // Hide admin_menu.module variables.
        'admin_menu_devel_modules_enabled',

        // Hide ctools.module variables.
        'ctools_last_cron',

        // Hide date.module variables.
        'date_api_version',
        'date_views_month_format_with_year',
        'date_views_month_format_without_year',
        'date_views_day_format_with_year',
        'date_views_day_format_without_year',
        'date_views_week_format_with_year',
        'date_views_week_format_without_year',

        // Hide entity.module variables.
        'entity_cache_tables_created',

        // Hide entityreference.module variables.
        'entityreference:base-tables',

        // Hide features.module variables.
        'features_codecache',
        'features_default_export_path',
        'features_feature_locked',
        'features_ignored_orphans',
        'features_lock_mode',
        'features_semaphore',

        // Hide less.module variables.
        'less_devel',
        'less_dir',
        'less_engine',
        'less_watch',

        // Hide locale.module variables.
        'language_count',
        'language_default',

        // Hide media.module variables.
        'media_icon_base_directory',
        // Dialog options.
        'media_dialogclass',
        'media_modal',
        'media_draggable',
        'media_resizable',
        'media_minwidth',
        'media_width',
        'media_height',
        'media_position',
        'media_zindex',
        'media_backgroundcolor',
        'media_opacity',

        // Hide media_wysiwyg.module variables.
        // 'media_wysiwyg_wysiwyg_allowed_attributes',
        // 'media_wysiwyg_wysiwyg_allowed_types',
        // 'media_wysiwyg_wysiwyg_browser_plugins',
        // 'media_wysiwyg_wysiwyg_default_view_mode',
        // 'media_wysiwyg_wysiwyg_icon_title',
        // 'media_wysiwyg_wysiwyg_upload_directory',
        'media_wysiwyg_view_mode_audio_file_wysiwyg_view_mode',
        'media_wysiwyg_view_mode_audio_file_wysiwyg_view_mode_status',
        'media_wysiwyg_view_mode_audio_wysiwyg_restricted_view_modes',
        'media_wysiwyg_view_mode_audio_wysiwyg_restricted_view_modes_status',
        'media_wysiwyg_view_mode_document_file_wysiwyg_view_mode',
        'media_wysiwyg_view_mode_document_file_wysiwyg_view_mode_status',
        'media_wysiwyg_view_mode_document_wysiwyg_restricted_view_modes',
        'media_wysiwyg_view_mode_document_wysiwyg_restricted_view_modes_status',
        'media_wysiwyg_view_mode_image_file_wysiwyg_view_mode',
        'media_wysiwyg_view_mode_image_file_wysiwyg_view_mode_status',
        'media_wysiwyg_view_mode_image_wysiwyg_restricted_view_modes',
        'media_wysiwyg_view_mode_image_wysiwyg_restricted_view_modes_status',
        'media_wysiwyg_view_mode_video_file_wysiwyg_view_mode',
        'media_wysiwyg_view_mode_video_file_wysiwyg_view_mode_status',
        'media_wysiwyg_view_mode_video_wysiwyg_restricted_view_modes',
        'media_wysiwyg_view_mode_video_wysiwyg_restricted_view_modes_status',

        // Hide recaptcha.module variables.
        'recaptcha_private_key',
        'recaptcha_public_key',

        // Hide restclient.module variables.
        'restclient_caching',
        'restclient_filepath',
        'restclient_hostname',
        'restclient_testing',

        // Hide update.module variables.
        'update_last_check',

        // Hide update_scripts.module variables.
        'update_scripts_clear_cache',
        'update_scripts_directory',
        'update_scripts_revert_features',

        // Hide variable.module variables.
        'variable_module_list',

        // Hide views.module variables.
        'views_defaults',
        'views_show_additional_queries',

        // Hide views_ui.module variables.
        'views_ui_always_live_preview',
        'views_ui_custom_theme',
        'views_ui_display_embed',
        'views_ui_show_advanced_column',
        'views_ui_show_advanced_help_warning',
        'views_ui_show_listing_filters',
        'views_ui_show_master_display',
        'views_ui_show_performance_statistics',
        'views_ui_show_preview_information',
        'views_ui_show_sql_query',
        'views_ui_show_sql_query_where',
      );
      break;

    case 'views_view':
      // Hide views.
      $entries_list = array(
        'redirects',
      );
      break;

  }

  // Remove SVG icon information.
  if ($component == 'variable') {
    foreach ($component_export['options']['sources'] as $name) {
      if (strpos($name, HELPERTHEME_SVG_ICON_NAME_PREFIX) === 0) {
        // Only remove the variable and do not display any message.
        unset($component_export['options']['sources'][$name]);
      }
    }
  }

  if (empty($entries_list)) {
    return;
  }

  $removed_entries_list = array();
  foreach ($entries_list as $name) {
    if (isset($component_export['options']['sources'][$name])) {
      unset($component_export['options']['sources'][$name]);
      $removed_entries_list[] = check_plain($name);
    }

    if ($component == 'dependencies') {
      if (isset($component_export['options']['detected'][$name])) {
        unset($component_export['options']['detected'][$name]);
      }
    }
  }

  // if (!empty($removed_entries_list)) {
  //   $message = 'The following are not listed in the <b>%name</b> components export list: @entries';
  //   $t_args = array(
  //     '%name' => $component_export['name'] . " (${component})",
  //     '@entries' => implode(', ', $removed_entries_list),
  //   );
  //   drupal_set_message(t($message, $t_args), 'warning');
  // }
}

/**
 * Implements hook_form_alter().
 */
function helpergeneric_form_alter(&$form, &$form_state, $form_id) {
  if ($form_id == 'user_login') {
    drupal_set_title('Log in');
    $form['actions']['cancel'] = array(
      '#type' => 'link',
      '#title' => t('Lost password?'),
      '#href' => 'user/password',
      '#weight' => 20,
    );
  }

  if ($form_id == 'user_admin_settings') {
    if (isset($form['admin_role'])) {
      $form['admin_role']['#access'] = FALSE;
    }
    if (isset($form['personalization'])) {
      $form['personalization']['#access'] = FALSE;
    }
  }

  // Group actions in the exposed filters form.
  if ($form_id == 'views_exposed_form') {
    $form['actions'] = array('#type' => 'actions');

    if (isset($form['submit'])) {
      $form['actions']['submit'] = $form['submit'];
      unset($form['submit']);
    }

    if (isset($form['reset'])) {
      $form['actions']['reset'] = $form['reset'];
      unset($form['reset']);
    }
  }

  if (!helpergeneric_cleanup_ui()) {
    return;
  }

  if ($form_id == 'menu_edit_item') {
    _helpergeneric_cleanup_menu_form($form, $form_state, $form_id);
  }
  if ($form_id == 'file_entity_add_upload') {
    _helpergeneric_cleanup_file_form($form, $form_state, $form_id);
  }
  if ($form_id == 'file_entity_edit') {
    _helpergeneric_cleanup_file_form($form, $form_state, $form_id);
  }
  if ($form_id == 'media_wysiwyg_format_form') {
    _helpergeneric_cleanup_file_form($form, $form_state, $form_id);
  }
  if ($form_id == 'user_profile_form') {
    _helpergeneric_cleanup_user_form($form, $form_state, $form_id);
  }
}

/**
 * Implements hook_form_FORM_ID_alter() for contentblock_form().
 */
function helpergeneric_form_contentblock_form_alter(&$form, &$form_state, $form_id) {
  if (helpergeneric_cleanup_ui()) {
    _helpergeneric_cleanup_contentblock_form($form, $form_state, $form_id);
  }
}

/**
 * Implements hook_form_FORM_ID_alter() for node_form().
 */
function helpergeneric_form_node_form_alter(&$form, &$form_state, $form_id) {
  if (helpergeneric_cleanup_ui()) {
    _helpergeneric_cleanup_node_form($form, $form_state, $form_id);
  }

  // Allways hide menu setings on node form. Helps prevent issues with i18n_menu
  if (isset($form['menu'])) {
    $form['menu']['#access'] = FALSE;
  }

  // Control "Event Date" field visibility.
  if (isset($form['field_blog_entry_type'], $form['field_blog_event_date'])) {
    // Show the "Event Date" only if blog entry type is event.
    $type_langcode = $form['field_blog_entry_type']['#language'];
    $form['field_blog_event_date']['#states'] = array(
      'visible' => array(
        ':input[name="field_blog_entry_type[' . $type_langcode . '][event]"]' => array('checked' => TRUE),
      ),
    );
    // Remove event date value on submit if field_blog_entry_type is not an event.
    $form['#validate'][] = 'helpergeneric_node_form_event_date_validate';
  }

  // Restrict summary length.
  $form['#validate'][] = 'helpergeneric_node_form_summary_validate';
}

/**
 * Form validation handler for node_form_alter().
 */
function helpergeneric_node_form_summary_validate($form, &$form_state) {
  $entity_type = $form['#entity_type'];
  $entity = $form_state[$entity_type];

  // Find out the field language.
  $langcode = isset($entity->language) ? $entity->language : LANGUAGE_NONE;
  $body_langcode = field_language($entity_type, $entity, 'body', $langcode);
  if (empty($form_state['values']['body'][$body_langcode][0]['summary'])) {
    return;
  }

  $teaser_length = variable_get('teaser_length', 600);
  $summary_length = drupal_strlen(trim($form_state['values']['body'][$body_langcode][0]['summary']));
  if ($summary_length > $teaser_length) {
    form_set_error('body][und][0][summary', t('The Summary must be less than %max characters, you provided %count characters.', array('%max' => $teaser_length, '%count' => $summary_length)));
  }
}

/**
 * Form validation handler for node_form_alter().
 */
function helpergeneric_node_form_event_date_validate($form, &$form_state) {
  $entity_type = $form['#entity_type'];
  $entity = $form_state[$entity_type];

  // Find out the entity language.
  $langcode = isset($entity->language) ? $entity->language : LANGUAGE_NONE;

  // Get the submitted input values.
  if (isset($form_state['input']['field_blog_entry_type']) && isset($form_state['values']['field_blog_event_date'])) {
    // Find out the actual language to use (field might be untranslatable).
    $type_langcode = field_language($entity_type, $entity, 'field_blog_event_date', $langcode);
    $blog_entry_type = $form_state['input']['field_blog_entry_type'][$type_langcode];

    // Find out the actual language to use (field might be untranslatable).
    $date_langcode = field_language($entity_type, $entity, 'field_blog_event_date', $langcode);
    if (empty($blog_entry_type['event'])) {
      // Remove event date if the blog entry is not tagged as an event..
      $form_state['values']['field_blog_event_date'][$date_langcode] = array();
    }
    else if (empty($form_state['values']['field_blog_event_date'][$date_langcode][0]['value'])) {
      // Display an error if the user submitted an event blog but did not provide an event date.
      form_set_error('field_blog_event_date', t('The blog entry is tagged as an event, please provide a valid event date.'));
    }
  }
}

/**
 * Helper function required by hook_form_FORM_ID_alter() for contentblock_form().
 */
function _helpergeneric_cleanup_contentblock_form(&$form, &$form_state, $form_id) {
  // Replace vertical tabs with normal fieldsets.
  _helpergeneric_alter_vertical_tabs($form);

  if (isset($form['redirect'])) {
    $form['redirect']['#access'] = FALSE;
  }

  if (isset($form['path'])) {
    $form['path']['#access'] = FALSE;
  }
}

/**
 * Helper function required by hook_form_FORM_ID_alter() for file_entity_edit().
 */
function _helpergeneric_cleanup_file_form(&$form, &$form_state, $form_id) {
  // Replace vertical tabs with normal fieldsets.
  _helpergeneric_alter_vertical_tabs($form);

  if (isset($form['destination'])) {
    $form['destination']['#access'] = FALSE;
  }

  if (isset($form['path'])) {
    $form['path']['#access'] = FALSE;
  }

  if (isset($form['redirect'])) {
    $form['redirect']['#access'] = FALSE;
  }
  else if (isset($form['options']['fields']['redirect'])) {
    $form['options']['fields']['redirect']['#access'] = FALSE;
  }

  if (isset($form['user'])) {
    $form['user']['#access'] = FALSE;
  }
}

/**
 * Helper function required by hook_form_FORM_ID_alter() for menu_edit_item().
 */
function _helpergeneric_cleanup_menu_form(&$form, &$form_state, $form_id) {
  if (isset($form['enabled'])) {
    $form['enabled']['#access'] = FALSE;
  }

  if (isset($form['expanded'])) {
    $form['expanded']['#access'] = FALSE;
  }

  if (isset($form['parent'])) {
    $form['parent']['#access'] = FALSE;
  }
}

/**
 * Helper function required by hook_form_FORM_ID_alter() for node_form().
 */
function _helpergeneric_cleanup_node_form(&$form, &$form_state, $form_id) {
  // Replace vertical tabs with normal fieldsets.
  _helpergeneric_alter_vertical_tabs($form);

  if (isset($form['author'])) {
    $form['author']['#access'] = FALSE;
  }

  // Hide "Comment settings" fieldset for all node types except "Blog entry".
  if ($form['#node']->type != 'blog') {
    $form['comment_settings']['#access'] = FALSE;
  }

  if (isset($form['redirect'])) {
    $form['redirect']['#access'] = FALSE;
  }

  if (isset($form['path'])) {
    $form['path']['#access'] = FALSE;
  }

  if (isset($form['revision_information'])) {
    $form['revision_information']['#access'] = FALSE;
  }

  if (isset($form['options']['promote'])) {
    $form['options']['promote']['#access'] = FALSE;
  }

  if (isset($form['options']['sticky'])) {
    $form['options']['sticky']['#access'] = FALSE;
  }

  // Move the translation fieldset to a vertical tab.
  if (isset($form['translation'])) {
    $form['translation']['status']['#access'] = FALSE;
    $form['translation']['name']['#access'] = FALSE;
    $form['translation']['created']['#access'] = FALSE;
  }
}

/**
 * Helper function required by hook_form_FORM_ID_alter() for user_profile_form().
 */
function _helpergeneric_cleanup_user_form(&$form, &$form_state, $form_id) {
  // Replace vertical tabs with normal fieldsets.
  _helpergeneric_alter_vertical_tabs($form);

  if (isset($form['redirect'])) {
    $form['redirect']['#access'] = FALSE;
  }

  if (isset($form['path'])) {
    $form['path']['#access'] = FALSE;
  }
}

/**
 * Replace vertical tabs with normal fieldsets.
 */
function _helpergeneric_alter_vertical_tabs(&$form) {
  // Do not display as vertical tabs.
  $form['additional_settings'] = array();
  foreach (element_children($form) as $key) {
    if (!isset($form[$key]['#group']) || $form[$key]['#group'] != 'additional_settings') {
      continue;
    }

    unset($form[$key]['#group']);
    switch ($key) {
      default:
        $form[$key]['#collapsed'] = FALSE;
        $form[$key]['#collapsible'] = FALSE;
        break;
    }
  }

  if (!empty($form['#groups'])) {
    foreach (element_children($form['#groups']) as $key) {
      $form['#groups'][$key]->format_type = 'fieldset';
    }
  }
}



/**
 * Implement hook_form_alter() for views_exposed_form.
 */
function helpergeneric_form_views_exposed_form_alter(&$form, &$form_state) {
}
