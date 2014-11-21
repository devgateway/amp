<?php

/**
 * @file
 * Update Scripts helper functions for the Internationalization related module.
 */

/**
 * Update language negotiation settings.
 * @see language_types_configurable()
 * @see language_negotiation_info()
 */
function _us_locale_languages_negotiation_update($settings = array()) {
  $configurable_types = language_types_configurable(FALSE);
  $language_providers = language_negotiation_info();

  // Populate the $settings array with the default structure.
  $configurable_types_settings = array();
  foreach ($configurable_types as $value) {
    $configurable_types_settings[$value] = array(
      'enabled' => array(),
      'weight' => array(),
    );
  }
  $settings  = array_merge_recursive($settings, $configurable_types_settings);

  foreach ($configurable_types as $type) {
    $negotiation = array();

    // Make sure "language-default" is always enabled.
    $enabled_providers = $settings[$type]['enabled'];
    $enabled_providers[LANGUAGE_NEGOTIATION_DEFAULT] = TRUE;

    $providers_weight = $settings[$type]['weight'];
    foreach ($providers_weight as $id => $weight) {
      if ($enabled_providers[$id]) {
        $provider = $language_providers[$id];
        $provider['weight'] = $weight;
        $negotiation[$id] = $provider;
      }
    }

    language_negotiation_set($type, $negotiation);
    variable_set("locale_language_providers_weight_$type", $providers_weight);
  }

  // Update non-configurable language types and the related language negotiation
  // configuration.
  language_types_set();
}

/**
 * Update default site language.
 */
function _us_locale_change_default_language($langcode) {
  $languages = language_list();
  if (isset($languages[$langcode])) {
    variable_set('language_default', $languages[$langcode]);
  }
}
