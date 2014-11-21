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
    $options = array();
    $langcode = $GLOBALS['language_content']->language;
    $cache_id = "amp:{$langcode}:activityStatusMapper:optionsList";

    // Try to get the response from cache.
    if ($cached = cache_get($cache_id, 'cache')) {
      return $cached->data;
    }

    $response = restclient_get($plugin['filters endpoint']);

    // @TODO: Add support for more 200 class response codes.
    if (restclient_response_code($response) != RESTCLIENT_RESPONSE_SUCCESS) {
      $t_args = array(
        '%message' => check_plain($response->status_message),
      );
      drupal_set_message(t('The API returned the following status message: %message', $t_args), 'error');
      return array();
    }

    if (empty($response)) {
      drupal_set_message(t('The API returned an empty response, please try again later.'), 'error');
      return array();
    }

    $response_data = json_decode($response->data);
    if (!empty($response_data->error)) {
      foreach ($response_data->error as $error_message) {
        $t_args = array(
          '%message' => check_plain($error_message),
        );
        drupal_set_message(t('The API returned the following error message: %message', $t_args), 'error');
      }
    }

    foreach ($response_data->values as $value) {
      $options[intval($value->id)] = filter_xss_admin($value->name);
    }

    if (!empty($options)) {
      // 24 hours cache life time for requests should be plenty for now.
      $expire = 60 * 60 * 24;
      cache_set($cache_id, $options, 'cache', REQUEST_TIME + $expire);
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
