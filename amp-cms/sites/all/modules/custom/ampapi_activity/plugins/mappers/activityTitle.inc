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

  'handler' => 'activityTitleMapper',
);

class activityTitleMapper extends activityMapperBase {
  /**
   * Filter handler.
   */
  public static function filter($plugin, &$api_parameters, &$request_body, $condition) {
    // We use SOLR search for the title.
    $plugin['report field group'] = 'others';
    $plugin['report field key'] = 'keyword';
    parent::filter($plugin, $api_parameters, $request_body, $condition);
  }
}
