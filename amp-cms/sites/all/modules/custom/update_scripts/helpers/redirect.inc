<?php

/**
 * @file
 * Update Scripts helper functions for the Redirect module.
 */

/**
 * Save a new URL redirect.
 */
function _us_redirect__create($info) {
  // Merge default values.
  $redirect = new stdClass();
  redirect_object_prepare($redirect, array(
    'source' => isset($info['source']) ? urldecode($info['source']) : '',
    'source_options' => isset($info['source_options']) ? drupal_get_query_array($info['source_options']) : array(),
    'redirect' => isset($info['redirect']) ? urldecode($info['redirect']) : '',
    'redirect_options' => isset($info['redirect_options']) ? drupal_get_query_array($info['redirect_options']) : array(),
    'status_code' => isset($info['status_code']) ? urldecode($info['status_code']) : 301,
    'language' => isset($info['language']) ? urldecode($info['language']) : LANGUAGE_NONE,
  ));

  // Save the URL redirect.
  redirect_save($redirect);
}
