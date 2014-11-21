<?php
/**
 * @file
 * activityId plugin.
 */

$plugin = array(
  'title' => t('Activity Id'),
  'description' => t(''),

  'property name' => 'id',
  'property info' => array(
    'type' => 'int',
  ),

  'report field group' => 'columns',
  'report field key' => 'Activity Id',

  'handler' => 'activityIdMapper',
);

class activityIdMapper extends activityMapperBase {
  // public static function filter($plugin, &$api_parameters, &$request_body, $condition) {
  //   // Doesn't work properly on the API.
  //   $plugin_title = $plugin['title'];
  //   throw new ampAPIEntityQueryHelperException("Filter is not supported for \"{$plugin_title}\"");
  // }

  // public static function sort($plugin, &$api_parameters, &$request_body, $order) {
  //   // Doesn't work properly on the API.
  //   $plugin_title = $plugin['title'];
  //   throw new ampAPIEntityQueryHelperException("Sort is not supported for \"{$plugin_title}\"");
  // }

  public static function getValue($plugin, $column) {
    return intval($column->value);
  }
}
