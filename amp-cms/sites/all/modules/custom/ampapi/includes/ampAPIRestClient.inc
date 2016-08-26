<?php

// FIXME: Namespaces are currently not supported: https://www.drupal.org/node/1399974
// namespace ampApi;

/**
 * Custom Wrapper around the restclient.module utilities.
 */
class ampAPIRestClient {

  /**
   * Make a GET request to the AMP API.
   *
   * @param $endpoint
   * @param array $options
   * @param bool $use_cache
   * @return array
   */
  public static function get($endpoint, $options = array(), $use_cache = TRUE) {
    // TODO: Test the API status here!
    // if ($endpoint != 'test') {
    //   $api_status = ampAPIRestClient::status();
    //   if (!$api_status['online']) {
    //     return array();
    //   }
    // }

    return ampAPIRestClient::_make_request('get', $endpoint, $options, $use_cache);
  }

  /**
   * Make a POST request to the AMP API.
   *
   * @param $endpoint
   * @param array $options
   * @param bool $use_cache
   * @return array
   */
  public static function post($endpoint, $options = array(), $use_cache = TRUE) {
    // TODO: Test the API status here!
    // if ($endpoint != 'test') {
    //   $api_status = ampAPIRestClient::status();
    //   if (!$api_status['online']) {
    //     return array();
    //   }
    // }

    return ampAPIRestClient::_make_request('post', $endpoint, $options, $use_cache);
  }

  /**
   * Tries to determine if the API is online or not.
   *
   * @param bool $link
   *    Whether or not error messages include a link to the API settings page.
   *
   * @return array
   *   An associative array with information about the API status. It contains
   *   the following keys:
   *   - online: boolean with the status of the API.
   *   - message: The message returned by the API.
   */
  public static function status($link = TRUE) {
    static $status;
    if (isset($status)) {
      return $status;
    }

    $status = array(
      'online' => FALSE,
      'message' => '',
    );

    $use_cache = FALSE;
    $response_data = ampAPIRestClient::_make_request('get', 'test', array(), $use_cache);
    $expected_response = 'Hey, This is AMP API Test Root Path';
    if (!empty($response_data['message']) && $response_data['message'] == $expected_response) {
      $status['online'] = TRUE;
      $status['message'] = $response_data['message'];
    }
    else {
      if (user_access('helpergeneric administer settings') && $link) {
        $t_args = array('!settings' => l(t('API Settings'), 'admin/settings/api'));
        $status['message'] = t('Could not contact the API, check the !settings page or the API status.', $t_args);
      }
      else {
        $status['message'] = t('Could not contact the API, please try again later.');
      }
    }

    // The error is displayed only the first time this function is called.
    if (!$status['online'] && !empty($status['message'])) {
      drupal_set_message($status['message'], 'warning');
    }

    return $status;
  }

  /**
   * Helper function for rest requests that also caches results.
   *
   * NOTE: Check the API status before making a request.
   *
   * @param string $request_type
   * @param string $endpoint
   * @param array $options
   * @param bool $use_cache
   * @return array
   */
  private static function _make_request($request_type, $endpoint, $options, $use_cache) {
    global $language_content;
    $langcode = $language_content->language;

    $cache_id = "ampAPIRestClient:{$langcode}:{$request_type}:{$endpoint}:" . md5(serialize($options));
    // Try to get the response from cache.
    if ($use_cache && $cached = cache_get($cache_id, 'cache')) {
      return $cached->data;
    }

    $options += array('reset' => TRUE, 'error_handling' => TRUE);

    // The API is slow at the moment.
    if (!isset($options['timeout'])) {
      $options['timeout'] = (int) variable_get('ampapi_timeout', 120);
    }

    if (!empty($langcode) && $langcode != LANGUAGE_NONE) {
      $options['parameters']['language'] = $langcode;
    }

    switch ($request_type) {
      case 'post':
        $response = restclient_post($endpoint, $options);
        break;
      case 'get':
        // Fallback to the default handler.
      default:
        $response = restclient_get($endpoint, $options);
        break;
    }

    $t_args = array();
    if (empty($response)) {
      // Just in case somebody removes the 'error_handling' flag.
      $msg = 'The API failed to respond, please try again later.';
      $msg .= 'Request: <pre>%request</pre>';
      ampAPIRestClient::log($msg, $t_args);
      return array();
    }

    if (empty($response->request)) {
      // Just in case somebody removes the 'error_handling' flag.
      $msg = 'Failed to make the API request, please check the API settings.';
      if (isset($response->error)) {
        $t_args['%error'] = $response->error;
        $msg .= 'Request: <pre>%error</pre>';
      }
      ampAPIRestClient::log($msg, $t_args);
      return array();
    }

    // Always add the request information.
    $t_args['%code'] = (int) $response->code;
    $t_args['%request'] = $response->request;

    if (empty($response->data)) {
      // Handle error responses without any data.
      if (restclient_response_code($response) != RESTCLIENT_RESPONSE_SUCCESS) {
        if (!empty($response->error)) {
          $t_args['%message'] = $response->error;
          $msg = 'The API returned %code with the following error message: <pre>%message</pre>';
          $msg .= 'Request: <pre>%request</pre>';
          ampAPIRestClient::log($msg, $t_args);
          return array();
        }
        if (!empty($response->status_message)) {
          $t_args['%message'] = $response->status_message;
          $msg = 'The API returned %code with the following status message: <pre>%message</pre>';
          $msg .= 'Request: <pre>%request</pre>';
          ampAPIRestClient::log($msg, $t_args);
          return array();
        }
      }

      $msg = 'The API returned %code with an empty response, please try again later.';
      $msg .= 'Request: <pre>%request</pre>';
      ampAPIRestClient::log($msg, $t_args);
      return array();
    }

    $response_data = json_decode($response->data);
    if (empty($response_data)) {
      $contains_html = FALSE;
      if ($response->data != strip_tags($response->data)) {
        $contains_html = TRUE;
      }

      // Special case for handling the API test endpoint.
      // The response data might be a valid JSON object containing errors.
      if (!$contains_html && $endpoint == 'test' && is_string($response->data)) {
        return array('message' => check_plain($response->data));
      }

      if (!$contains_html) {
        $t_args['%message'] = $response->data;
      }
      else {
        // Cleanup the response HTML.
        $response_message = $response->data;

        // Cleanup whitespace.
        $response_message = str_replace('&nbsp;', ' ', $response_message);
        $response_message = str_replace("\t", ' ', $response_message);
        $response_message = str_replace("\r", "\n", $response_message);

        // Remove HTML comments.
        $response_message = preg_replace('/<!--(.|\s)*?-->/', '', $response_message);

        // Remove script tags including content.
        $response_message = preg_replace('#<script(.*?)>(.*?)</script>#is', '', $response_message);

        // Remove style tags including content.
        $response_message = preg_replace('#<style(.*?)>(.*?)</style>#is', '', $response_message);


        // Remove form tags including content.
        $response_message = preg_replace('#<form(.*?)>(.*?)</form>#is', '', $response_message);

        // Remove link tags including content.
        $response_message = preg_replace('#<a(.*?)>(.*?)</a>#is', '', $response_message);

        // Cleanup the whitespace inside the title tag.
        $response_message = preg_replace('/<title>[\s\n]*([^<\n]+)[\n\s]+([^<\n]+)[\s\n]*<\/title>/im', '<title> $1 $2 </title>', $response_message);

        // Remove all remaining HTML tags (for some the content is kept).
        $response_message = strip_tags($response_message);

        // Cleanup whitespace.
        $response_message = preg_replace('/\n\ +/s', "\n", $response_message);
        $response_message = preg_replace('/\n+/s', "\n", $response_message);

        $t_args['%message'] = $response_message;
      }

      $msg = 'The API returned %code with an invalid JSON response, the following has been returned: <pre>%message</pre>';
      $msg .= 'Request: <pre>%request</pre>';
      ampAPIRestClient::log($msg, $t_args);
      return array();
    }

    if (!empty($response_data->error)) {
      ampAPIRestClient::_process_json_errors($response, (array) $response_data->error);
      return array();
    }

    if ($use_cache) {
      // Cache the request for 1 hour.
      $expire = 60 * 60;
      cache_set($cache_id, $response_data, 'cache', REQUEST_TIME + $expire);
    }

    return $response_data;
  }

  /**
   * Wrapper around the Drupal watchdog that also displays a message.
   *
   * @param $message
   * @param $variables
   * @param int $severity
   */
  public static function log($message, $variables = array(), $severity = WATCHDOG_ERROR) {
    // Display error only to users with the 'admin user' role.
    $role = user_role_load_by_name('admin user');
    if ($GLOBALS['user']->uid == 1 || ($GLOBALS['user']->uid && user_has_role($role))) {
      drupal_set_message(t($message, $variables), 'warning');
    }

    $category = 'amp api';
    watchdog($category, $message, $variables, $severity);
  }

  /**
   * Parses and logs the errors inside the API response JSON data.
   *
   * @param $response
   * @param $response_errors
   * @return array
   */
  private static function _process_json_errors($response, $response_errors) {
    $args = array();

    // Always add the request information.
    $args['%code'] = (int) $response->code;
    $args['%request'] = $response->request;

    if (is_string($response_errors)) {
      $args['!error'] = check_plain($response_errors);
      $msg = 'The API returned the following error message: !error.';
    }
    else {
      if (is_array($response_errors)) {
        $errors = array();
        foreach ($response_errors as $response_error) {
          if (is_string($response_error)) {
            $errors[] = check_plain($response_error);
          }
          else {
            if (is_array($response_error)) {
              foreach ($response_error as $entry) {
                if (!is_string($entry)) {
                  continue;
                }

                $errors[] = check_plain($entry);
              }
            }
          }
        }
        $args['!errors'] = theme('item_list', array('items' => $errors));
        $msg = 'The API returned %code with the following error messages: !errors';
        $msg .= 'Request: <pre>%request</pre>';
      }
    }

    if (isset($msg)) {
      ampAPIRestClient::log($msg, $args);
    }
  }
}
