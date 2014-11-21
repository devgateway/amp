<?php

// Provide a list of modules to be installed.
$modules = array(
  'locale',
  // 'translation', // REPALCED BY 'entity_translation'

  'i18n',
    // 'i18n_block',
    // 'i18n_contact',
    // 'i18n_field',
    // 'i18n_forum',
    'i18n_menu',
    // 'i18n_node',
    // 'i18n_panels', // We try to avoid using this for only a few strings.
    // 'i18n_path',
    // 'i18n_redirect',
    // 'i18n_select',
    'i18n_string',
    // 'i18n_sync',
    // 'i18n_taxonomy',
    'i18n_translation',
    // 'i18n_user',
    'i18n_variable',

    'entity_translation',
      'entity_translation_i18n_menu',
      // 'entity_translation_upgrade',

    // 'l10n_client',
    'l10n_update',
    // 'potx',
    'title',
    // 'translation_overview',
    // 'translation_table',

  'variable',
    // 'variable_admin',
    // 'variable_example',
    'variable_realm',
    'variable_store',
    // 'variable_views',
);
_us_module__install($modules);

// Clear system caches.
drupal_flush_all_caches();

// Prepare a list of features to be installed.
$feature_names = array(
  'ampi18n',
);
_us_features__install($feature_names);


// Revert all features and clear system caches.
_us_features__revert_all();
drupal_flush_all_caches();


// The translation system only translates strings in the i18n_string_allowed_formats variable.
$translatable_formats = array();
foreach (filter_formats() as $key => $format_info) {
  if (in_array($key, array('plain_text'))) {
    $translatable_formats[$key] = $key;
  }
}
variable_set('i18n_string_allowed_formats', $translatable_formats);

// Make sure only enabled languages are displayed.
variable_set('entity_translation_languages_enabled', TRUE);
variable_set('i18n_language_list', I18N_LANGUAGE_ENABLED);
