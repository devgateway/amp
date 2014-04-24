<?php

/**
 * @file
 * Update Scripts helper functions for the core Taxonomy module.
 */

function _us_taxonomy_vocabulary_save($vocabulary) {
  $vocabulary = is_object($vocabulary) ? $vocabulary : (object) $vocabulary;
  taxonomy_vocabulary_save($vocabulary);
}


function _us_taxonomy_vocabulary_delete($vid) {
  taxonomy_vocabulary_delete($vid);
}

