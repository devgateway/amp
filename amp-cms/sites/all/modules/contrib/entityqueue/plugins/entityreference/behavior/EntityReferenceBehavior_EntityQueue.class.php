<?php

/**
 * Defines a Entityreference behavior handler for Entityqueue.
 */
class EntityReferenceBehavior_EntityQueue extends EntityReference_BehaviorHandler_Abstract {

  /**
   * Overrides EntityReference_BehaviorHandler_Abstract::validate().
   */
  public function validate($entity_type, $entity, $field, $instance, $langcode, $items, &$errors) {
    if ($entity_type == 'entityqueue_subqueue') {
      $queue = entityqueue_queue_load($entity->queue);

      $min_size = $queue->settings['min_size'];
      $max_size = $queue->settings['max_size'];
      $act_as_queue = isset($queue->settings['act_as_queue']) ? $queue->settings['act_as_queue'] : 0;

      $empty_target_id = create_function('$value', 'return (!empty($value["target_id"])) ? TRUE : FALSE;');
      $eq_items = array_filter($items, $empty_target_id);

      if (count($eq_items) < $min_size) {
        $errors[$field['field_name']][$langcode][0][] = array(
          'error' => 'entityqueue_min_size',
          'message' => t("The minimum number of items in this queue is @min_size.", array('@min_size' => $min_size)),
        );
      }
      elseif (!$act_as_queue && count($eq_items) > $max_size && $max_size > 0) {
        $errors[$field['field_name']][$langcode][count($items) - 1][] = array(
          'error' => 'entityqueue_max_size',
          'message' => t("The maxinum number of items in this queue is @max_size.", array('@max_size' => $max_size)),
        );
      }
    }
  }

  /**
   * Overrides EntityReference_BehaviorHandler_Abstract::presave().
   */
  public function presave($entity_type, $entity, $field, $instance, $langcode, &$items) {
    if ($entity_type == 'entityqueue_subqueue') {
      $queue = entityqueue_queue_load($entity->queue);

      $max_size = $queue->settings['max_size'];
      $act_as_queue = isset($queue->settings['act_as_queue']) ? $queue->settings['act_as_queue'] : 0;

      if ($act_as_queue) {
        $empty_target_id = create_function('$value', 'return (!empty($value["target_id"])) ? TRUE : FALSE;');
        $eq_items = array_filter($items, $empty_target_id);

        if (count($eq_items) > $max_size && $max_size > 0) {
          // Keep up to $max_size items
          $items = array_slice($eq_items, -$max_size);
        }
      }
    }
  }
}
