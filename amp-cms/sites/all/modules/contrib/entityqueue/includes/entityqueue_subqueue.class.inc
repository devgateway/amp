<?php

/**
 * @file
 * Defines the class for entity subqueues.
 */

/**
 * Main class for subqueue entities.
 */
class EntitySubqueue extends Entity {

  /**
   * Constructs a EntitySubqueue object.
   */
  public function __construct($values = array()) {
    parent::__construct($values, 'entityqueue_subqueue');
  }
}
