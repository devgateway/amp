<?php

/**
 * @file
 * Hooks provided by Entityqueue.
 */

/**
 * @addtogroup hooks
 * @{
 */

/**
 * This hook is needed to allow modules to provide their own queues.
 *
 * If you do this, CTools will look for default queues in
 * <modulename>.entityqueue_default.inc
 *
 * @see hook_entityqueue_default_queues()
 *
 * @return array
 *   An associative array containing the entityqueue version.
 */
function hook_entityqueue_api($module = NULL, $api = NULL) {
  if ($module == 'entityqueue' && $api == 'entityqueue_default') {
    return array('version' => '1');
  }
}

/**
 * This hook allows modules to provide their own queues.
 *
 * @return array
 *   An associative array containing EntityQueue objects.
 */
function hook_entityqueue_default_queues() {
  $queues = array();

  $queue = new EntityQueue();
  $queue->disabled = FALSE; /* Edit this to true to make a default queue disabled initially */
  $queue->api_version = 1;
  $queue->name = 'featured_articles';
  $queue->label = 'Featured articles';
  $queue->handler = 'simple';
  // if multilingual
  $queue->language = 'en';
  $queue->target_type = 'node';
  $queue->settings = array(
    'target_bundles' => array(),
    'min_size' => 0,
    'max_size' => 0,
    'subqueue_label' => '',
    'act_as_queue' => 1,
  );
  $queues['featured_articles'] = $queue;

  return $queues;
}

/**
 * @} End of "addtogroup hooks".
 */
