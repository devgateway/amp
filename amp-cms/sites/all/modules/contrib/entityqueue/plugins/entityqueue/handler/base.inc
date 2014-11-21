<?php

/**
 * @file
 * Contains Entityqueue handler interface and base class.
 */

/**
 * Abstraction of the handling logic of a queue.
 */
interface EntityQueueHandlerInterface {

  /**
   * Factory function: creates a new instance of this handler for a queue.
   *
   * @param EntityQueue $queue
   *   An EntityQueue object.
   *
   * @return EntityQueueHandlerInterface
   */
  public static function getInstance(EntityQueue $queue);

  /**
   * Generates a settings form for this handler.
   */
  public function settingsForm();

  /**
   * Generates an add/edit subqueue form for this handler.
   */
  public function subqueueForm(EntitySubqueue $subqueue, &$form_state);

  /**
   * Returns the entity type label of a queue target type.
   */
  public function getTargetTypeLabel();

  /**
   * Returns the label of the queue's handler.
   */
  public function getHandlerLabel();

  /**
   * Returns the label of a given subqueue.
   */
  public function getSubqueueLabel(EntitySubqueue $subqueue);

  /**
   * Returns TRUE if subqueue can be deleted, otherwise returns FALSE.
   */
  public function canDeleteSubqueue(EntitySubqueue $subqueue);

  /**
   * Act on creating a queue.
   */
  public function create();

  /**
   * Act on loading a queue.
   */
  public function load();

  /**
   * Act on loading a queue that is defined only in code.
   */
  public function loadFromCode();

  /**
   * Act before a queue is saved.
   */
  public function preSave();

  /**
   * Act after a new queue is saved.
   */
  public function insert();

  /**
   * Act after a queue is updated.
   */
  public function update();

  /**
   * Act before deleting a queue.
   */
  public function preDelete();

  /**
   * Act after deleting a queue.
   */
  public function postDelete();
}

/**
 * An abstract implementation of EntityQueueHandlerInterface.
 */
abstract class EntityQueueHandlerBase implements EntityQueueHandlerInterface {

  /**
   * The handler plugin definition.
   *
   * @var array
   */
  protected $plugin;

  /**
   * The EntityQueue object.
   *
   * @var EntityQueue
   */
  protected $queue;

  public static function getInstance(EntityQueue $queue) {
    $entity_type = $queue->target_type;

    // Check if the entity type does exist and has a base table.
    $entity_info = entity_get_info($entity_type);
    if (empty($entity_info['base table'])) {
      return BrokenEntityQueueHandler::getInstance($queue);
    }

    // Check if the queue handler exists.
    ctools_include('plugins');
    $plugin = ctools_get_plugins('entityqueue', 'handler', $queue->handler);
    if (empty($plugin)) {
      return BrokenEntityQueueHandler::getInstance($queue);
    }

    return new $plugin['class']($queue);
  }

  protected function __construct(EntityQueue $queue) {
    $this->queue = $queue;

    ctools_include('plugins');
    $plugin = ctools_get_plugins('entityqueue', 'handler', $this->queue->handler);
    $this->plugin = $plugin;
  }

  public function settingsForm() {
    return array();
  }

  public function subqueueForm(EntitySubqueue $subqueue, &$form_state) {
    return array();
  }

  public function getTargetTypeLabel() {
    $entity_info = entity_get_info($this->queue->target_type);
    if (!empty($entity_info['base table'])) {
      return $entity_info['label'];
    }

    return '<em>' . t('Missing entity type (@type)', array('@type' => $this->queue->target_type)) . '</em>';
  }

  public function getHandlerLabel() {
    return $this->plugin['title'];
  }

  public function getSubqueueLabel(EntitySubqueue $subqueue) {}

  public function canDeleteSubqueue(EntitySubqueue $subqueue) {
    return TRUE;
  }

  public function create() {}

  public function load() {}

  public function loadFromCode() {}

  public function preSave() {}

  public function insert() {}

  public function update() {}

  public function preDelete() {}

  public function postDelete() {}
}

/**
 * A NULL implementation of EntityQueueHandlerInterface.
 */
class BrokenEntityQueueHandler extends EntityQueueHandlerBase {

  /**
   * {@inheritdoc}
   */
  public static function getInstance(EntityQueue $queue) {
    return new BrokenEntityQueueHandler($queue);
  }

  /**
   * {@inheritdoc}
   */
  public function settingsForm() {
    $form['handler'] = array(
      '#markup' => t('The selected handler is broken.'),
    );
    return $form;
  }

  public function subqueueForm(EntitySubqueue $subqueue, &$form_state) {
    $form['handler'] = array(
      '#markup' => t('The selected handler is broken.'),
    );
    return $form;
  }

  /**
   * {@inheritdoc}
   */
  public function getHandlerLabel() {
    return '<em>' . t('Broken/missing handler') . '</em>';
  }

  /**
   * {@inheritdoc}
   */
  public function getSubqueueLabel(EntitySubqueue $subqueue) {
    return '';
  }

}
