<?php

/**
 * @file
 * Defines the class for entity activity.
 */

/**
 * Main class for activity entities.
 */
class ActivityEntity extends Entity {
  // Define some default values.
  public $id = NULL;
  public $title = '';
  public $external_url = NULL;

  /**
   * URI method for entity_class_uri().
   */
  protected function defaultUri() {
    if (!isset($this->external_url)) {
      return NULL;
    }

    return array(
      'path' => helpergeneric_detect_amp_baseurl() . $this->external_url,
    );
  }
}
