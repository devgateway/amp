<?php

/**
 * @file
 * Entityqueue handler for simple queues.
 */

$plugin = array(
  'title' => t('Simple queue'),
  'class' => 'SimpleEntityQueueHandler',
  // This plugin has only one subqueue per queue.
  'queue type' => 'single',
  'weight' => -100,
);
