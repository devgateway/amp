<?php

/**
 * @file: Revert Features.
 * @desc: This script can be used to revert features and clear all the cache.
 */

// Revert all features and clear system caches.
_us_features__revert_all();
drupal_flush_all_caches();
