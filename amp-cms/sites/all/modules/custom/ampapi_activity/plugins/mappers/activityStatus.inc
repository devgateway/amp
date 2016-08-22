<?php
/**
 * @file
 * activityStatus plugin.
 */

$plugin = array(
  'title' => t('Status'),
  'description' => t(''),

  'property name' => 'status',
  'property info' => array(
    'type' => 'integer',
    'options list' => array('activityStatusMapper', 'optionsList'),
  ),

  'report field group' => 'columns',
  'report field key' => 'Status',

  'filters endpoint' => 'filters/activityStatus',

  'handler' => 'activityStatusMapper',
);

class activityStatusMapper extends activityMapperBase {
  public static function optionsList($plugin) {
    // Small sanity check.
    if (!is_array($plugin)) {
      $plugin = ampapi_get_mapper_by_property_name($plugin);
    }

    $response_data = ampAPIRestClient::get($plugin['filters endpoint']);
    if (empty($response_data) || empty($response_data->values)) {
      return array();
    }

    $options = array();
    /** @noinspection PhpUndefinedFieldInspection */
    foreach ($response_data->values as $value) {
      $options[intval($value->id)] = filter_xss_admin($value->name);
    }

    return $options;
  }

  public static function filter($plugin, &$api_parameters, &$request_body, $condition) {
    // API requires integers, not strings.
    foreach ($condition['value'] as $key => &$value) {
      $value = intval($value);
    }

    parent::filter($plugin, $api_parameters, $request_body, $condition);
  }
}
