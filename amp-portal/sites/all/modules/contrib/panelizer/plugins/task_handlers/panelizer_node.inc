<?php
/**
 * @file
 *
 * This is the task handler plugin to handle an entity view.
 */

// Plugin definition
$plugin = array(
  // is a 'context' handler type, meaning it supports the API of the
  // context handlers provided by ctools context plugins.
  'handler type' => 'context',
  // may NOT be added up front.
  'visible' => FALSE,
  // Administrative fields.
  'title' => t('Panelizer'),
  'admin summary' => 'panelizer_panelizer_task_admin_summary',
  'operations' => array(),
  // Callback to render the data.
  'render' => 'panelizer_panelizer_task_render',
  // Callback to return addressable data
  'addressable callback' => 'panelizer_panelizer_task_get_addressable',

  'test' => 'panelizer_panelizer_task_test',
);

/**
 * Callback to provide administrative summary of the task handler.
 */
function panelizer_panelizer_task_admin_summary($handler, $task, $subtask, $page, $show_title = TRUE) {
  $output = '';

  $output .= '<div class="clear-block">';
  if ($show_title) {
    // Get the operations
    $operations = page_manager_get_operations($page);

    // Get operations for just this handler.
    $operations = $operations['handlers']['children'][$handler->name]['children']['actions']['children'];
    $args = array('handlers', $handler->name, 'actions');
    $rendered_operations = page_manager_render_operations($page, $operations, array(), array('class' => array('actions')), 'actions', $args);

    $output .= '<div class="handler-title clear-block">';
    $output .= '<div class="actions handler-actions">' . $rendered_operations['actions'] . '</div>';
    $output .= '<span class="title-label">' . t('Panelizer') . '</span>';
    $output .= '</div>';
  }

  $access = t('This variant will be selected if the entity being viewed is panelized. This variant must be enabled and selected for panelizer to work!');

  $rows[] = array(
    array(
      'class' => array('page-summary-label'),
      'data' => t('Selection rule'),
    ),
    array(
      'class' => array('page-summary-data'),
      'data' => $access,
    ),
    array('class' => array('page-summary-operation'), ''),
  );

  $output .= theme('table', array('header' => array(), 'rows' => $rows, 'attributes' => array('class' => array('page-manager-handler-summary'))));
  $output .= '</div>';

  return $output;
}

/**
 * Render a entity that has been panelized.
 */
function panelizer_panelizer_task_render($handler, $base_contexts, $args, $test = TRUE) {
  // Get the context this is viewing; figure out what entity it is and load
  // the right plugin.
  ctools_include('context');
  $context = reset($base_contexts);
  if (!$context) {
    return FALSE;
  }

  if (empty($context->data->panelizer)) {
    return FALSE;
  }

  $panelizer = $context->data->panelizer;
  // One of these two will always be set.
  $entity_type = !empty($panelizer->entity_type) ? $panelizer->entity_type : $panelizer->panelizer_type;

  $address = implode('::', array('page_manager', $handler->task, $handler->subtask, $handler->name, implode('..', $args)));

  if ($entity_handler = panelizer_entity_plugin_get_handler($entity_type)) {
    return $entity_handler->render_entity($context->data, $args, $address);
  }
}

/**
 * Determine if the panelizer task handler should fire.
 *
 * This returns true if the configured entity is panelized and has
 * a display.
 */
function panelizer_panelizer_task_test($handler, $base_contexts) {
  if (empty($base_contexts)) {
    return;
  }

  $context = reset($base_contexts);
  if (empty($context->data)) {
    return;
  }

  $entity = &$context->data;
  return !empty($entity->panelizer->display);
}

function panelizer_panelizer_task_get_addressable($task, $subtask_name, $handler, $address, $contexts, $arguments, $type) {
  ctools_include('plugins', 'panels');
  if (empty($contexts)) {
    return;
  }

  $context = reset($contexts);
  if (empty($context->data)) {
    return;
  }

  // Extract the entity from the context so we can load the panelizer.
  $entity = &$context->data;

  if (empty($entity->panelizer) || empty($entity->panelizer->display)) {
    return;
  }

  // Load the display
  $display = $entity->panelizer->display;
  // One of these two will always be set.
  $entity_type = !empty($panelizer->entity_type) ? $panelizer->entity_type : $panelizer->panelizer_type;
  $handler = panelizer_entity_plugin_get_handler($entity_type);
  if (!$handler) {
    return;
  }

  list($entity_id, $revision_id, $bundle) = entity_extract_ids($entity_type, $entity);

  $display->context = $handler->get_contexts($panelizer, $entity);
  $display->args = $arguments;
  $display->css_id = $panelizer->css_id;

  $display->cache_key = implode(':', array('panelizer', $entity_type, $entity_id));

  $renderer = panels_get_renderer($panelizer->pipeline, $display);
  if ($type == 'content') {
    $renderer->prepare();

    $pid = array_shift($address);
    if (!empty($renderer->prepared['panes'][$pid])) {
      return $renderer->render_pane($renderer->prepared['panes'][$pid]);
    }
  }
}
