<?php
/**
 * @file
 * amporganization_profiles.custom.inc
 */

/**
 * Implements hook_views_plugin_style_pre_render_grouping_sets_alter().
 *
 * @HACK: This changes the output of views_plugin_style::render().
 */
function amporganization_profiles_views_plugin_style_pre_render_grouping_sets_alter($plugin, &$sets) {
  if ($plugin->view->name != 'organizations' || $plugin->view->current_display != 'page') {
    return;
  }

  foreach ($sets as $group_name => $set_items) {
    if ($group_name == '') {
      continue;
    }

    /**
     * @HACK: The organization group taxonomy does not have translation enabled.
     * And because this feature HAS to be done in 8h (which is why we use the
     * taxonomy.module in the first place) we have to manually call t().
     */
    $translated_group_name = t($group_name);
    if ($translated_group_name != $group_name) {
      $set_items['group'] = $translated_group_name;
      $sets[$translated_group_name] = $set_items;
      unset($sets[$group_name]);
    }
  }

  // Sort for the current language.
  ksort($sets);
}
