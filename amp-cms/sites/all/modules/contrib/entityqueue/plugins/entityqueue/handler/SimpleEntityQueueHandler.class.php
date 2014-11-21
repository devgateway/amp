<?php

/**
 * @file
 * Contains Entityqueue handler for simple queues.
 */

/**
 * A simple queue implementation.
 */
class SimpleEntityQueueHandler extends EntityQueueHandlerBase {

  /**
   * {@inheritdoc}
   */
  public function getSubqueueLabel(EntitySubqueue $subqueue) {
    return $this->queue->label;
  }

  /**
   * {@inheritdoc}
   */
  public function canDeleteSubqueue(EntitySubqueue $subqueue) {
    return FALSE;
  }

  /**
   * {@inheritdoc}
   */
  public function loadFromCode() {
    $this->ensureSubqueue();
  }

  /**
   * {@inheritdoc}
   */
  public function insert() {
    $this->ensureSubqueue();
  }

  /**
   * Makes sure that every simple queue has a subqueue.
   */
  protected function ensureSubqueue() {
    global $user;
    static $queues = array();

    if (!isset($queues[$this->queue->name])) {
      $queues[$this->queue->name] = TRUE;

      $transaction = db_transaction();
      $query = new EntityFieldQuery();
      $query
        ->entityCondition('entity_type', 'entityqueue_subqueue')
        ->entityCondition('bundle', $this->queue->name);
      $result = $query->execute();

      // If we don't have a subqueue already, create one now.
      if (empty($result['entityqueue_subqueue'])) {
        $subqueue = entityqueue_subqueue_create();
        $subqueue->queue = $this->queue->name;
        $subqueue->name = $this->queue->name;
        $subqueue->label = $this->getSubqueueLabel($subqueue);
        $subqueue->module = 'entityqueue';
        $subqueue->uid = $user->uid;

        entity_get_controller('entityqueue_subqueue')->save($subqueue, $transaction);
      }
    }
  }

}
