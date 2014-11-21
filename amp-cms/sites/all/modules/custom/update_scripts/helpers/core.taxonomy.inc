<?php

/**
 * @file
 * Update Scripts helper functions for the core Taxonomy module.
 */

/**
 * Create a new vocabulary.
 */
function _us_taxonomy__vocabulary_save($vocabulary) {
  $vocabulary = is_object($vocabulary) ? $vocabulary : (object) $vocabulary;
  return taxonomy_vocabulary_save($vocabulary);
}

/**
 * Delete a vocabulary.
 */
function _us_taxonomy__vocabulary_delete($vid) {
  return taxonomy_vocabulary_delete($vid);
}

/**
 * Create a new taxonomy term.
 */
function _us_taxonomy__term_save($term) {
  $term = is_object($term) ? $term : (object) $term;
  return taxonomy_term_save($term);
}
