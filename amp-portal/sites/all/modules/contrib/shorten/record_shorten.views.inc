<?php

/**
 * @file
 *   Provide views data and handlers for the Record Shortened URLs module.
 */

/**
 * Implements hook_views_data().
 */
function record_shorten_views_data() {
  // Basic table information.
  $data['record_shorten']['table']['group']  = t('Shortened URLs');

  // Advertise this table as a possible base table.
  $data['record_shorten']['table']['base'] = array(
    'field' => 'sid',
    'title' => t('Shortened URLs'),
    'help' => t('Listings of URLs shortened by the Shorten URLs module.'),
    'weight' => 10,
  );
  $data['users']['table']['join'] = array(
    'record_shorten' => array(
      'left_field' => 'uid',
      'field' => 'uid',
      'type' => 'LEFT',
    ),
  );

  $data['record_shorten']['sid'] = array(
    'title' => t('Shorten ID'),
    'help' => t('The ID of the recorded URL.'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_numeric',
    ),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
    'argument' => array(
      'handler' => 'views_handler_argument_numeric',
    ),
  );

  $data['record_shorten']['original'] = array(
    'title' => t('Original URL'),
    'help' => t('The original, long, unshortened URL.'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_string',
    ),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
    'argument' => array(
      'handler' => 'views_handler_argument_string',
    ),
  );

  $data['record_shorten']['short'] = array(
    'title' => t('Shortened URL'),
    'help' => t('The new, computed, shortened URL.'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_string',
    ),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
    'argument' => array(
      'handler' => 'views_handler_argument_string',
    ),
  );

  $data['record_shorten']['service'] = array(
    'title' => t('Service'),
    'help' => t('The service used to shorten the URL.'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array(
      'handler' => 'record_shorten_views_handler_filter_string_service',
    ),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
    'argument' => array(
      'handler' => 'views_handler_argument_string',
    ),
  );

  $data['record_shorten']['uid'] = array(
    'title' => t('User ID'),
    'help' => t('The User ID of the user who created the shortened URL.'),
    'field' => array(
      'handler' => 'views_handler_field',
      'click sortable' => TRUE,
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_numeric',
    ),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
    'argument' => array(
      'handler' => 'views_handler_argument_numeric',
    ),
  );

  $data['record_shorten']['hostname'] = array(
    'title' => t('IP Address'),
    'help' => t('The IP address of the user who created the shortened URL.'),
    'field' => array(
      'handler' => 'record_shorten_views_handler_field_hostname',
      'click sortable' => FALSE,
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_string',
    ),
  );

  $data['record_shorten']['created'] = array(
    'title' => t('Created time'),
    'help' => t('The time the shortened URL was created.'),
    'field' => array(
      'handler' => 'views_handler_field_date',
      'click sortable' => TRUE,
    ),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
    'filter' => array(
      'handler' => 'views_handler_filter_date',
    ),
  );

  return $data;
}

/**
 * Implements hook_views_handlers().
 */
function record_shorten_views_handlers() {
  return array(
    'info' => array(
      'path' => drupal_get_path('module', 'record_shorten'),
    ),
    'handlers' => array(
      'record_shorten_views_handler_filter_string_service' => array(
        'parent' => 'views_handler_filter_many_to_one',
      ),
      'record_shorten_views_handler_field_hostname' => array(
        'parent' => 'views_handler_field',
      ),
    ),
  );
}
