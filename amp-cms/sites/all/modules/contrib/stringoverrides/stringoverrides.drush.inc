<?php

/**
 * @file
 * Drush interface for String Overrides.
 */

/**
 * Implements hook_drush_command().
 */
function stringoverrides_drush_command() {
  $items['stringoverrides-list'] = array(
    'description' => 'List all the string overrides associated with the site.',
    'options' => array(
      'language' => 'The language for the available overrides. Defaults to "en".',
    ),
    'examples' => array(
      'drush stringoverrides-list' => 'Lists all string overrides on the site.',
      'drush stringoverrides-list --language fr' => 'Lists all French string overrides on the site.',
    ),
    'core' => array(7, 8),
  );
  $items['stringoverrides-set'] = array(
    'description' => 'Set a string override for the site.',
    'arguments' => array(
      'original' => 'The original source text to override. Defaults to "en".',
      'replacement' => 'The replacement text.',
    ),
    'options' => array(
      'language' => 'The language for the override. Defaults to "en".',
      'context' => 'The context for the desired override. Defaults to "".',
    ),
    'core' => array(7, 8),
  );
  $items['stringoverrides-remove'] = array(
    'description' => 'Removes a string override from the system.',
    'arguments' => array(
      'original' => 'The original source text to override. Defaults to "en".',
    ),
    'options' => array(
      'language' => 'The language for the override. Defaults to "en".',
      'context' => 'The context for the desired override. Defaults to "".',
    ),
    'core' => array(7, 8),
  );
  $items['stringoverrides-enable'] = array(
    'description' => 'Enables the given string override.',
    'arguments' => array(
      'original' => 'The original source text of the override.',
    ),
    'options' => array(
      'language' => 'The language for the override. Defaults to "en".',
      'context' => 'The context for the desired override. Defaults to "".',
    ),
    'core' => array(7, 8),
  );
  $items['stringoverrides-disable'] = array(
    'description' => 'Disables the given String Override.',
    'arguments' => array(
      'original' => 'The original source text of the override.',
    ),
    'options' => array(
      'language' => 'The language for the override. Defaults to "en".',
      'context' => 'The context for the desired override. Defaults to "".',
    ),
    'core' => array(7, 8),
  );
  return $items;
}

/**
 * Implements hook_drush_help().
 */
function stringoverrides_drush_help($section) {
  switch ($section) {
    case 'drush:stringoverrides-list':
      return dt('Retrieves all the String Overrides associated with the site.');
    case 'drush:stringoverrides-set':
      return dt('Set a String Overrides associated with the site.');
    case 'drush:stringoverrides-enable':
      return dt('Enables a string override on the site.');
    case 'drush:stringoverrides-disable':
      return dt('Disables a string override on the site.');
    case 'drush:stringoverrides-remove':
      return dt('Removes a string override on the site.');
  }
}

/**
 * Drush Callback; Lists all the String Overrides.
 */
function drush_stringoverrides_list() {
  // Load the administration interface for API dependencies.
  module_load_include('inc', 'stringoverrides', 'stringoverrides.admin');
  $strings = array();
  $language = drush_get_option('language', 'en');

  // Retrieve the string overrides from the variables table.
  $words = array(
    FALSE => variable_get("locale_custom_disabled_strings_$language", array()),
    TRUE => variable_get("locale_custom_strings_$language", array())
  );
  foreach ($words as $enabled => $custom_strings) {
    foreach ($custom_strings as $context => $translations) {
      foreach ($translations as $source => $translation) {
        $strings[] = array(
          'enabled' => $enabled,
          'context' => $context,
          'source' => $source,
          'translation' => $translation,
        );
      }
    }
  }

  // Only display the table if there are string overrides.
  if (empty($strings)) {
    drush_print(dt('There are currently no string overrides associated with the given language.'));
  }
  else {
    // Sort the strings before displaying them.
    usort($strings, 'stringoverrides_admin_word_sort');

    // Construct and display the table.
    $rows[] = array(dt(''), dt('Original'), dt('Replacement'), dt('Context'));
    foreach ($strings as $string) {
      $rows[] = array(
        $string['enabled'] ? dt('[X]') : dt('[ ]'),
        $string['source'],
        $string['translation'],
        $string['context'],
      );
    }

    // Display in a table.
    drush_print_table($rows, TRUE);
  }
}

/**
 * Drush Callback; Set a String Overrides.
 */
function drush_stringoverrides_set($original, $replacement) {
  // Get the available options.
  $language = drush_get_option('language', 'en');
  $context = drush_get_option('context', '');

  // Load all the strings.
  $custom_strings = variable_get('locale_custom_strings_' . $language, array());
  $custom_strings[$context][$original] = $replacement;
  variable_set('locale_custom_strings_' . $language, $custom_strings);
  drush_print(dt('The string override has been set.'));
}

/**
 * Drush Callback; Enable a String Override.
 */
function drush_stringoverrides_enable($original) {
  // Get the available options.
  $language = drush_get_option('language', 'en');
  $context = drush_get_option('context', '');

  // Load the current overrides.
  $disabled = variable_get("locale_custom_disabled_strings_$language", array());
  $enabled = variable_get("locale_custom_strings_$language", array());

  // Make sure the override exists.
  if (isset($disabled[$context][$original])) {
    // Swap the values.
    $enabled[$context][$original] = $disabled[$context][$original];
    unset($disabled[$context][$original]);
    variable_set("locale_custom_disabled_strings_$language", $disabled);
    variable_set("locale_custom_strings_$language", $enabled);
    drush_print(dt('The string override has been enabled.'));
  }
  else {
    drush_print(dt('Given disabled string override not found.'));
  }
}

/**
 * Drush Callback; Disable a String Override.
 */
function drush_stringoverrides_disable($original) {
  // Get the available options.
  $language = drush_get_option('language', 'en');
  $context = drush_get_option('context', '');

  // Load the current overrides.
  $disabled = variable_get("locale_custom_disabled_strings_$language", array());
  $enabled = variable_get("locale_custom_strings_$language", array());

  // Make sure the override exists.
  if (isset($enabled[$context][$original])) {
    // Swap the values.
    $disabled[$context][$original] = $enabled[$context][$original];
    unset($enabled[$context][$original]);
    variable_set("locale_custom_disabled_strings_$language", $disabled);
    variable_set("locale_custom_strings_$language", $enabled);
    drush_print(dt('The string override has been disabled.'));
  }
  else {
    drush_print(dt('Given enabled string override not found.'));
  }
}

/**
 * Drush Callback; Removes a String Override.
 */
function drush_stringoverrides_remove($original) {
  // Get the available options.
  $language = drush_get_option('language', 'en');
  $context = drush_get_option('context', '');

  // Load the current overrides.
  $disabled = variable_get("locale_custom_disabled_strings_$language", array());
  $enabled = variable_get("locale_custom_strings_$language", array());

  // Remove the string override.
  unset($disabled[$context][$original]);
  unset($enabled[$context][$original]);
  variable_set("locale_custom_disabled_strings_$language", $disabled);
  variable_set("locale_custom_strings_$language", $enabled);
  drush_print(dt('String override removed.'));
}
