<?php

/**
 * @file
 * Defines the class for entity question.
 */

/**
 * Main class for question entities.
 */
class ContentBlockEntity extends Entity {
  // Define some default values.
  public $cbid = NULL;
  public $title = '';
  public $language = LANGUAGE_NONE;
  public $type = '';
  public $machine_name = NULL;
  public $uid = NULL;
  public $status = TRUE;
  public $created = NULL;
  public $changed = NULL;
  public $style = '';
  public $icon = '';

  /**
   * URI method for entity_class_uri().
   */
  protected function defaultUri() {
    if (!isset($this->cbid)) {
      return NULL;
    }

    return array(
      'path' => CONTENTBLOCK_UI_PATH . '/' . $this->identifier(),
    );
  }
}
