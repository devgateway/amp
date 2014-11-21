<?php

/**
 * @file
 * Views integration for Entityqueue.
 */

/**
 * Implements hook_views_data_alter().
 *
 * @see entityreference_field_views_data_views_data_alter()
 */
function entityqueue_views_data_alter(&$data) {
  // Build an array of entityreference fields created by Entityqueue.
  $eq_fields = array();
  foreach (entityqueue_queue_load_multiple() as $queue) {
    $eq_fields[] = _entityqueue_get_target_field_name($queue->target_type);
  }
  $eq_fields = array_unique($eq_fields);

  foreach ($eq_fields as $field_name) {
    if ($field = field_info_field($field_name)) {
      $field_table = _field_sql_storage_tablename($field);

      foreach ($field['bundles'] as $entity_type => $bundles) {
        $target_entity_info = entity_get_info($field['settings']['target_type']);
        if (isset($target_entity_info['base table'])) {
          $entity_info = entity_get_info($entity_type);

          $target_base_table = $target_entity_info['base table'];
          $relationship =& $data[$target_base_table]['entityqueue_relationship'];
          $relationship['relationship'] = array(
            'handler' => 'entityqueue_handler_relationship_entityqueue',
            'field_name' => $field['field_name'],
            'field table' => $field_table,
            'field field' => $field['field_name'] . '_target_id',
            'base' => $entity_info['base table'],
            'base field' => $entity_info['entity keys']['id'],
            'label' => t('@target Queue', array(
              '@target' => $target_entity_info['label'],
            )),
            'group' => t('Entityqueue'),
            'title' => t('@target Queue', array(
              '@target' => $target_entity_info['label'],
            )),
            'help' => t('Create a relationship from @target to an entityqueue.', array(
              '@target' => $target_entity_info['label'],
            )),
          );

          if (isset($data[$field_table]['table']['join']['entityqueue_subqueue']['extra'])) {
            $relationship['relationship']['join_extra'] = $data[$field_table]['table']['join']['entityqueue_subqueue']['extra'];
          }

          $relationship['sort'] = array(
            'handler' => 'entityqueue_handler_sort_position',
            'group' => t('Entityqueue'),
            'title' => t('@target Queue Position', array(
              '@target' => $target_entity_info['label'],
            )),
            'label' => t('@target Queue Position', array(
              '@target' => $target_entity_info['label'],
            )),
            'help' => t('Position of item in the @target queue.', array(
              '@target' => $target_entity_info['label'],
            )),
            'field table' => $field_table,
            'field_name' => $field_name,
            'real field' => 'delta',
          );
        }
      }

      // Remove data that was automatically added by Views for our entity target
      // fields.
      unset($data[$field_table]);
    }
  }

  $data['entityqueue_subqueue']['queue'] = array(
    'title' => t('Queue Name'),
    'label' => t('Queue Name'),
    'group' => t('Entityqueue'),
    'help' => t('The entityqueue machine name.'),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
  );

  $data['entityqueue_subqueue']['name'] = array(
    'title' => t('Subqueue Name'),
    'label' => t('Subqueue Name'),
    'group' => t('Entityqueue'),
    'help' => t('The subqueue machine name.'),
    'sort' => array(
      'handler' => 'views_handler_sort',
    ),
  );
}
