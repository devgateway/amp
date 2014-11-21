<?php

/**
 * @file
 * Definition of EntityReference_SelectionHandler_EntityQueue.
 */

/**
 * Defines a Entityreference selection handler for Entityqueue.
 */
class EntityReference_SelectionHandler_EntityQueue extends EntityReference_SelectionHandler_Generic {

  /**
   * Overrides EntityReference_SelectionHandler_Generic::getInstance().
   */
  public static function getInstance($field, $instance = NULL, $entity_type = NULL, $entity = NULL) {
    return new EntityReference_SelectionHandler_EntityQueue($field, $instance, $entity_type, $entity);
  }

  /**
   * Constructs the EntityQueue selection handler.
   */
  protected function __construct($field, $instance = NULL, $entity_type = NULL, $entity = NULL) {
    parent::__construct($field, $instance, $entity_type, $entity);

    $queue_name = NULL;
    if (!empty($entity->queue)) {
      $queue_name = $entity->queue;
    }
    elseif (!empty($instance['bundle'])) {
      $queue_name = $instance['bundle'];
    }
    if (!empty($queue_name)) {
      $this->queue = entityqueue_queue_load($queue_name);
    }

    // Override the entityreference settings with our own.
    $this->field['settings']['handler_settings']['target_bundles'] = NULL;
  }

  /**
   * Overrides EntityReference_SelectionHandler_Generic::buildEntityFieldQuery().
   */
  public function buildEntityFieldQuery($match = NULL, $match_operator = 'CONTAINS') {
    $handler = EntityReference_SelectionHandler_Generic::getInstance($this->field, $this->instance, $this->entity_type, $this->entity);
    $query = $handler->buildEntityFieldQuery($match, $match_operator);

    if (!empty($this->queue->settings['target_bundles'])) {
      $query->entityCondition('bundle', $this->queue->settings['target_bundles'], 'IN');
    }

    return $query;
  }
}
