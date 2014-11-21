<?php

/**
 * @file
 * Plugin definition for CTools Export UI integration.
 */
$plugin = array(
  'schema' => 'entityqueue_queue',
  'access' => 'manipulate entityqueues',
  'create access' => 'administer entityqueue',
  'delete access' => 'administer entityqueue',

  // Add our custom operations.
  'allowed operations' => array(
    'subqueues' => array('title' => t('View subqueues')),
    'edit subqueue' => array('title' => t('Edit items')),
  ),

  'menu' => array(
    'menu item' => 'entityqueue',
    'menu title' => 'Entityqueues',
    'menu description' => 'Manage entity queues.',
    // Add entityqueue specific menu callbacks.
    'items' => array(
      'subqueues' => array(
        'path' => 'list/%ctools_export_ui/subqueues',
        'title' => 'Subqueues',
        'page callback' => 'ctools_export_ui_switcher_page',
        'page arguments' => array('entityqueue_export_ui', 'subqueues', 4),
        'load arguments' => array('entityqueue_export_ui'),
        'access callback' => 'entityqueue_queue_access',
        'access arguments' => array('view', 4),
        'type' => MENU_CALLBACK,
      ),
      'add subqueue' => array(
        'path' => 'list/%ctools_export_ui/subqueues/add',
        'title' => 'Add subqueue',
        'page callback' => 'ctools_export_ui_switcher_page',
        'page arguments' => array('entityqueue_export_ui', 'subqueue_add', 4),
        'load arguments' => array('entityqueue_export_ui'),
        'access callback' => 'entityqueue_queue_access',
        'access arguments' => array('create', 4),
        'type' => MENU_LOCAL_ACTION,
      ),
      'edit subqueue callback' => array(
        'path' => 'list/%ctools_export_ui/subqueues/%entityqueue_subqueue',
        'page callback' => 'ctools_export_ui_switcher_page',
        'page arguments' => array('entityqueue_export_ui', 'subqueue_edit', 4, 6),
        'load arguments' => array('entityqueue_export_ui'),
        'access callback' => 'entityqueue_queue_access',
        'access arguments' => array('update', 4),
        'type' => MENU_CALLBACK,
      ),
      'edit subqueue' => array(
        'path' => 'list/%ctools_export_ui/subqueues/%entityqueue_subqueue/edit',
        'title' => 'Edit subqueue',
        'page callback' => 'ctools_export_ui_switcher_page',
        'page arguments' => array('entityqueue_export_ui', 'subqueue_edit', 4, 6),
        'load arguments' => array('entityqueue_export_ui'),
        'access callback' => 'entityqueue_queue_access',
        'access arguments' => array('update', 4),
        'type' => MENU_DEFAULT_LOCAL_TASK,
        'context' => MENU_CONTEXT_PAGE | MENU_CONTEXT_INLINE,
      ),
      'delete subqueue' => array(
        'path' => 'list/%ctools_export_ui/subqueues/%entityqueue_subqueue/delete',
        'title' => 'Delete subqueue',
        'type' => MENU_CALLBACK,
        'page callback' => 'ctools_export_ui_switcher_page',
        'page arguments' => array('entityqueue_export_ui', 'subqueue_delete', 4, 6),
        'load arguments' => array('entityqueue_export_ui'),
        'access callback' => 'entityqueue_access',
        'access arguments' => array('delete', 6, NULL, 'entityqueue_subqueue'),
      ),
    ),
  ),

  // Add our custom page titles.
  'strings' => array(
    'title' => array(
      'subqueues' => t('Subqueues of %title'),
    ),
  ),

  'title singular' => t('queue'),
  'title singular proper' => t('Queue'),
  'title plural' => t('queues'),
  'title plural proper' => t('Queues'),

  'form' => array(
    'settings' => 'entityqueue_export_ui_form',
    'validate' => 'entityqueue_export_ui_form_validate',
  ),

  'handler' => array(
     'class' => 'entityqueue_export_ui',
     'parent' => 'ctools_export_ui',
   ),
);

/**
 * Form to edit the settings of a queue.
 */
function entityqueue_export_ui_form(&$form, &$form_state) {
  global $user;
  if (!user_access('administer entityqueue', $user)) {
    drupal_access_denied();
    drupal_exit();
  }

  _entityqueue_set_breadcrumb();

  ctools_include('plugins');
  $queue = &$form_state['item'];

  // We definitely need a queue label :)
  $form['info']['label']['#required'] = TRUE;

  $handlers = array();
  foreach (ctools_get_plugins('entityqueue', 'handler') as $name => $plugin) {
    $handlers[$name] = $plugin['title'];
  }
  $form['handler'] = array(
    '#title' => t('Handler'),
    '#type' => 'select',
    '#options' => $handlers,
    '#default_value' => !$queue->is_new ? $queue->handler : 'simple',
    '#required' => TRUE,
    '#disabled' => $queue->is_new ? FALSE : TRUE,
  );

  // Get valid target entity types.
  $entity_info = entity_get_info();
  unset($entity_info['entityqueue_subqueue']);

  $entity_types = array();
  foreach ($entity_info as $entity_type => $info) {
    $entity_types[$entity_type] = $info['label'];
  }
  $form['target_type'] = array(
    '#title' => t('Entity type'),
    '#type' => 'select',
    '#options' => $entity_types,
    '#default_value' => $queue->target_type,
    '#required' => TRUE,
    '#disabled' => $queue->is_new ? FALSE : TRUE,
    '#ajax' => array(
      'callback' => 'entityqueue_export_ui_target_type_callback',
      'wrapper' => 'entityqueue-target-bundles',
    ),
  );

  $form['settings'] = array(
    '#type' => 'vertical_tabs',
    '#tree' => TRUE,
  );

  $target_type = !empty($form_state['values']['target_type']) ? $form_state['values']['target_type'] : $queue->target_type;
  $bundles = array();
  foreach ($entity_info[$target_type]['bundles'] as $bundle_name => $bundle_info) {
    $bundles[$bundle_name] = $bundle_info['label'];
  }
  $form['settings']['entityreference_field'] = array(
    '#type' => 'fieldset',
    '#title' => t('Entityreference field settings'),
    '#collapsible' => TRUE,
  );
  $form['settings']['entityreference_field']['target_bundles'] = array(
    '#type' => 'checkboxes',
    '#title' => $target_type == 'node' ? t('Content types') : t('Bundles'),
    '#prefix' => '<div id="entityqueue-target-bundles">',
    '#suffix' => '</div>',
    '#options' => $bundles,
    '#default_value' => !empty($queue->settings['target_bundles']) ? $queue->settings['target_bundles'] : array(),
    '#multiple' => TRUE,
    '#description' => t('The bundles of the entity type that can be referenced. Optional, leave empty for all bundles.')
  );

  $form['settings']['properties'] = array(
    '#type' => 'fieldset',
    '#title' => t('Queue properties'),
    '#collapsible' => TRUE,
  );
  $form['settings']['properties']['min_size'] = array(
    '#type' => 'textfield',
    '#size' => 3,
    '#default_value' => isset($queue->settings['min_size']) ? $queue->settings['min_size'] : 0,
    '#prefix' => '<div class="container-inline form-item">',
    '#field_prefix' => t('Restrict this queue to a minimum of'),
  );
  $form['settings']['properties']['max_size'] = array(
    '#type' => 'textfield',
    '#size' => 3,
    '#default_value' => isset($queue->settings['max_size']) ? $queue->settings['max_size'] : 0,
    '#field_prefix' => t('and a maximum of'),
    '#field_suffix' => t('items.'),
    '#suffix' => '</div>',
  );
  $form['settings']['properties']['act_as_queue'] = array(
    '#type' => 'checkbox',
    '#title' => t('Act as queue'),
    '#default_value' => isset($queue->settings['act_as_queue']) ? 1 : 0,
    '#return_value' => 1,
    '#description' => t('When enabled, adding more then the maximum number of items will remove extra items from the top of the queue.'),
    '#states' => array(
      'invisible' => array(
        ':input[name="settings[properties][max_size]"]' => array('value' => 0),
      ),
    ),
  );
}

/**
 * Selects just the target bundles dropdown to be returned for re-rendering.
 *
 * @return array
 *  A renderable array representing the target bundles dropdown.
 */
function entityqueue_export_ui_target_type_callback($form, $form_state) {
  return $form['settings']['entityreference_field']['target_bundles'];
}

/**
 * Validate callback for the preset edit form.
 */
function entityqueue_export_ui_form_validate(&$form, &$form_state) {
  // Move the queue settings values up a level.
  $queue_settings = element_children($form['settings']);
  foreach ($queue_settings as $form_element) {
    if (isset($form_state['values']['settings'][$form_element]) && is_array($form_state['values']['settings'][$form_element])) {
      $form_state['values']['settings'] += (array) $form_state['values']['settings'][$form_element];
      unset($form_state['values']['settings'][$form_element]);
    }
  }
  unset($form_state['values']['settings']['settings__active_tab']);

  // Clean-up target_bundles values a bit.
  if (isset($form_state['values']['settings'])) {
    $form_state['values']['settings']['target_bundles'] = array_filter($form_state['values']['settings']['target_bundles']);
  }
}
