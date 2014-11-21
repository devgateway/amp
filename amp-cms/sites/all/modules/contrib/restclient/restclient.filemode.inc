<?php

/**
 * @file
 * Defines the restclient testing feature.
 */

/**
 * Function to fetch a response from a file.
 *
 * @param $url
 *  The url path to fetch a response from.
 *
 * @param $variables
 *  Variable to be used with the "rest" call
 *
 * @return
 *  Return an response loaded from the file.
 */
function restclient_filemode_fetch_response($url, $variables) {
  $file_name = _restclient_filemode_file_name($url, $variables);
  //Fetch the file if it exist.
  $return = '';
  $realpath = _restclient_get_file_location();
  //Check if the restclient_testing file exist, if not create it.
  if (!file_exists($realpath)) {
    //Create it
    if (!drupal_mkdir($realpath)) {
      watchdog('restclient', 'Failed to create restclient_testing folder.', array(), WATCHDOG_ERROR);
      return;
    }
  }

  $file_url = $realpath . '/' . $file_name;
  //Check if the file exist?

  if (file_exists($file_url)) {
    //Fetch the file.
    $data = file_get_contents($file_url);
    $return = unserialize($data);
  }
  else {
    watchdog('restclient', 'Corresponding file does not exist for !url', array('!url' => $url), WATCHDOG_ERROR);
    return;
  }
  return $return;
}

/**
 * Function to save the response of the curl call to a file.
 *
 * @param $response
 *   response from the service call
 *
 * @param $url
 *   url of the service call.
 *
 * @param $variables
 *   $type of function being called, GET, PUT etc.
 *
 */
function restclient_filemode_save_response($response, $url, $variables) {
  $file_name = _restclient_filemode_file_name($url, $variables);
  //Fetch the file if it exist.
  $realpath = _restclient_get_file_location();

  //Check if the restclient_testing file exist, if not create it.
  if (!file_exists($realpath)) {
    //Create it
    if (!drupal_mkdir($realpath)) {
      watchdog('restclient', 'Failed to create restclient_testing folder.', array(), WATCHDOG_ERROR);
      return;
    }
  }

  $file_url = $realpath . '/' . $file_name;
  //Check if the file exist if so overrite it, if not create it.
  $serialized = serialize($response);

  //File save data and replace if the file already exist.
  if (!file_unmanaged_save_data($serialized, $file_url, FILE_EXISTS_REPLACE)) {
    watchdog('restclient', 'Failed to save the contents of the file.', array(), WATCHDOG_ERROR);
  }
  return;
}

/**
 * restclient file name for the url call.
 *
 * @param $resource_path
 *   path of the service all
 *
 * @param $type
 *   type of function called, GET, PUT etc
 *
 * @return string
 *   md5 hashed file name with the type at the end.
 */
function _restclient_filemode_file_name($resource_path, $header) {
  //Get the hook
  $optional = '';
  $data =  module_invoke_all('filemode_signature_alter');
  foreach ($data as $argument) {
    if (is_array($header[$argument])) {
      $optional .= serialize($header[$argument]);
    }
    else {
      $optional .= $header[$argument];
    }
  }
  $path = $resource_path . '.' . $optional;
  return md5($path);
}


function _restclient_get_file_location() {
  $path = variable_get('restclient_filepath', 'private://restclient_testing');
  //Check if theres is private, public, or tmp in the file path.
  if ($wrapper = file_stream_wrapper_get_instance_by_uri($path)) {
    $realpath = $wrapper->realpath();
  }
  return $realpath;
}
