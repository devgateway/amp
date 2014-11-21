<?php

/**
 * @file
 * The controller for the EntityQueue entity containing CRUD operations.
 */

/**
 * The controller class for subqueues.
 */
class EntitySubqueueEntityController extends EntityAPIController {

  /**
   * Creates a default subqueue.
   *
   * @param array $values
   *   An array of values to set, keyed by property name.
   *
   * @return
   *   An EntitySubqueue object with all default fields initialized.
   */
  public function create(array $values = array()) {
    $values += array(
      'queue' => NULL,
      'name' => NULL,
      'label' => '',
      'language' => language_default()->language,
      'module' => '',
      'uid' => '',
      'data' => array(),
    );

    return parent::create($values);
  }
}
