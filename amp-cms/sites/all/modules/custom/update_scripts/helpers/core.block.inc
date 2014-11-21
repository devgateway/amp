<?php

/**
 * @file
 * Update Scripts helper functions for the core Block module.
 */

/**
 * Update block settings.
 *
 * @param string $module
 *   The module from which the block originates.
 * @param int $delta
 *   Unique ID for block within a module.
 * @param string $theme
 *   The theme to update the block settings for.
 * @param array $settings
 *   An array of settings to update.
 */
function _us_block__update_settings($module, $delta, $theme, $settings) {
  static $available_blocks;

  // Get a list of available blocks.
  if (empty($available_blocks)) {
    // Update the 'blocks' table.
    $available_blocks = _block_rehash();
  }

  foreach ($available_blocks as $block_id => $block) {
    if ($block['module'] == $module && $block['delta'] == $delta && $block['theme'] == $theme) {
      break;
    }
  }

  $block = array_merge($block, $settings);

  // Update settings that are common to all themes.
  db_update('block')
    ->fields(array(
      'visibility' => (int) $block['visibility'],
      'pages' => trim($block['pages']),
      'custom' => (int) $block['custom'],
      'title' => $block['title'],
    ))
    ->condition('module', $block['module'])
    ->condition('delta', $block['delta'])
    ->execute();

  // Update theme specific settings.
  db_merge('block')
    ->key(array(
      'theme' => $theme,
      'delta' => $block['delta'],
      'module' => $block['module'],
    ))
    ->fields(array(
      'region' => ($block['region'] == BLOCK_REGION_NONE ? '' : $block['region']),
      'pages' => trim($block['pages']),
      'status' => (int) ($block['region'] != BLOCK_REGION_NONE),
      'weight' => (int) $block['weight'],
    ))
    ->execute();
}

/**
 * Hide blocks.
 */
function _us_block__hide($module, $delta, $theme) {
  static $available_blocks;

  // Get a list of available blocks.
  if (empty($available_blocks)) {
    // Update the 'blocks' table.
    $available_blocks = _block_rehash();
  }

  foreach ($available_blocks as $block_id => $block) {
    if ($block['module'] == $module && $block['delta'] == $delta && $block['theme'] == $theme) {
      break;
    }
  }

  // Update theme specific settings.
  db_merge('block')
    ->key(array('module' => $module, 'delta' => $delta, 'theme' => $theme))
    ->fields(array(
      'region' => BLOCK_REGION_NONE,
      'status' => 0,
    ))
    ->execute();
}
