<?php

/**
 * @file
 * Defines the base class for entity queues.
 */

/**
 * Base class for entity queues.
 */
class EntityQueue {

  /**
   * @var string $name
   */
  public $name = '';

  /**
   * @var string $label
   */
  public $label = '';

  /**
   * @var string $language
   */
  public $language = '';

  /**
   * @var string $handler
   */
  public $handler = '';

  /**
   * @var string $target_type
   */
  public $target_type = 'node';

  /**
   * @var array $settings
   */
  public $settings = array(
    'target_bundles' => array(),
    'min_size' => 0,
    'max_size' => 0,
    'subqueue_label' => '',
  );

  /**
   * @var int $export_type
   */
  public $export_type = EXPORT_IN_CODE;

  /**
   * @var bool $is_new
   */
  public $is_new = FALSE;

  /**
   * Constructs a new EntityQueue object, without saving it to the database.
   *
   * @param array $values
   */
  public function __construct($values = array()) {
    $values = (array) $values;
    // Set initial values.
    foreach ($values as $key => $value) {
      $this->$key = $value;
    }
  }

  /**
   * Returns the label of this queue.
   *
   * @return string
   */
  public function label() {
    return $this->label;
  }

}
