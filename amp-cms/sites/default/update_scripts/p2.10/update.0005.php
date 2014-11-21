<?php

// Provide a list of modules to be installed.
$modules = array(
  'ds',
    'ds_extras',
    // 'ds_devel',
    // 'ds_forms',
    // 'ds_format',
    // 'ds_search',
    // 'ds_ui',
  'rel',

  'entity',
    'entity_token',
  'entityqueue',
  'entityreference',

  'email',
  'field_group',
  'link',

  'htmlpurifier',

  'imagecache_actions',
    // 'image_effects_text',
    // 'image_effects_text_test',
    // 'image_styles_admin',
    // 'imagecache_autorotate',
    'imagecache_canvasactions',
    // 'imagecache_coloractions',
    // 'imagecache_customactions',

  'media',
    'media_internet',
  'media_vimeo',
  'media_youtube',
  'file_entity',

  'panels',
    // 'panels_ipe',
    // 'panels_mini',
    // 'panels_node',
    // ctools:
    'page_manager',

  'views',
    // 'views_ui',
    // 'views_slideshow',
    // 'views_slideshow_cycle',
    // ctools:
    'views_content',
    // date:
    'date_views',
    // efq_views:
    'efq_views',

);
_us_module__install($modules);

// Enable Field Templates. (Customize the labels and the HTML output of your fields.)
variable_set('ds_extras_field_template', 1);

// Set the Display Suite default field template to "Minimal". In this way all
// fields will have the minimum required HTML markup.
// Make sure that for each field the value of "Field Template" is "Use sitewide default".
variable_set('ft-default', 'theme_ds_field_minimal');

// Hide the colon by default on the reset field template.
variable_set('ft-kill-colon', 1);

// Make fields from other modules available on the "Manage display" screens.
variable_set('ds_extras_fields_extra', 1);
variable_set('ds_extras_fields_extra_list', '');

// Do not allow a view mode per node.
variable_set('ds_extras_switch_view_mode', 0);

// Allow users to select weather or not the title is visible on the "Full content" view mode.
variable_set('ds_extras_hide_page_title', 1);

// Disable Drupal blocks/regions.
variable_set('ds_extras_hide_page_sidebars', 0);

// Enables view permissions on all Display Suite fields.
variable_set('ds_extras_field_permissions', 0);

// Disable views displays. (Manage the layout of your Views layout with Field UI.)
variable_set('ds_extras_vd', 0);

// Do not show view mode switcher. (Adds a field with links to switch view modes inline with Ajax.)
variable_set('ds_extras_switch_field', 0);

// Adds a hidden region to the layouts. Fields will be built but not printed.
variable_set('ds_extras_hidden_region', 1);

// Hide "Manage form display" link.
variable_set('rel_build_form_registration', 0);

// Always show the advanced display settings.
variable_get('views_ui_show_advanced_column', 1);

// Always show the master display.
variable_set('views_ui_show_master_display', 1);

// Hide missing advanced help warning message.
variable_set('views_ui_show_advanced_help_warning', 0);

// Disable update views preview on changes.
variable_set('views_ui_always_live_preview', 0);

// Show the SQL query during live preview.
variable_set('views_ui_show_sql_query', 1);

// Show performance statistics  during live preview.
variable_set('views_ui_show_performance_statistics', 1);

// Provide a list of modules to be installed.
$modules = array(
  'helpergeneric',
  'helpertheme',
);
_us_module__install($modules);
