<?php
/**
 * @file
 * activityTitle plugin.
 */

$plugin = array(
  'title' => t('Title'),
  'description' => t(''),

  'property name' => 'title',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'Project Title',
  'report filter key' => 'keyword',

  'handler' => 'activityTitleMapper',
);

class activityTitleMapper extends activityMapperBase {
  /**
   * Filter handler.
   */
  public static function filter($plugin, &$api_parameters, &$request_body, $condition) {
    // The filter for this field is not a normal filter.
    $plugin['report field group'] = 'others';
    parent::filter($plugin, $api_parameters, $request_body, $condition);
  }
}
