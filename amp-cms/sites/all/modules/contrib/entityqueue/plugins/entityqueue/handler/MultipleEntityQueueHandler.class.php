<?php

/**
 * @file
 * Contains Entityqueue handler for simple queues with multiple subqueues.
 */

/**
 * A multiple subqueue queue implementation.
 */
class MultipleEntityQueueHandler extends SimpleEntityQueueHandler {

  /**
   * {@inheritdoc}
   */
  public function subqueueForm(EntitySubqueue $subqueue, &$form_state) {
    $values = isset($form_state['values']) ? $form_state['values'] : (array) $subqueue;

    $form = array();
    $form['label'] = array(
      '#type' => 'textfield',
      '#title' => t('Subqueue label'),
      '#required' => TRUE,
      '#default_value' => isset($values['label']) ? $values['label'] : '',
    );

    $form['name'] = array(
      '#type' => 'machine_name',
      '#title' => t('Subqueue name'),
      '#required' => TRUE,
      '#default_value' => isset($values['name']) ? $values['name'] : '',
      '#machine_name' => array(
        'exists' => 'entityqueue_subqueue_load',
        'source' => array('label'),
      ),
      '#disabled' => (isset($subqueue->subqueue_id)),
    );
    return $form;
  }

  /**
   * {@inheritdoc}
   */
  public function loadFromCode() {}

  /**
   * {@inheritdoc}
   */
  public function insert() {}

  /**
   * {@inheritdoc}
   */
  public function canDeleteSubqueue(EntitySubqueue $subqueue) {
    return TRUE;
  }

}
