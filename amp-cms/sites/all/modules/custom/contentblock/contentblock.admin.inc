<?php
/**
 * @file
 * Forms and helper functions for administrative pages.
 */

/**
 * Form constructor for the survey add/edit form.
 * @TODO: Cleanup reference form element and move to contentblock.module.
 *
 * @see contentblock_entity_info().
 */
function contentblock_form($form, &$form_state, $entity) {
  $entity_type = 'contentblock';
  $entity_controller = entity_get_controller($entity_type);

  if (isset($form_state['op']) && arg(0) != 'admin') {
    drupal_set_title(contentblock_entity_ui_get_page_title($form_state['op'], $entity_type, $entity), PASS_THROUGH);
  }

  // $form['additional_settings'] = array(
  //   '#type' => 'vertical_tabs',
  //   '#weight' => 99,
  // );

  // Entity options for administrators
  // $form['options'] = array(
  //   '#type' => 'fieldset',
  //   '#access' => user_access("survey {$entity_type} administer"),
  //   '#title' => t('Publishing options'),
  //   '#collapsible' => TRUE,
  //   '#collapsed' => TRUE,
  //   '#group' => 'additional_settings',
  //   '#attributes' => array(
  //     'class' => array("{$entity_type}-form-options"),
  //   ),
  //   '#weight' => 95,
  // );

  $form['title'] = array(
    '#type' => 'textfield',
    '#title' => t('Title'),
    '#default_value' => $entity->title,
  );

  // Add the buttons.
  $form['actions'] = array('#type' => 'actions');
  $form['actions']['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Save'),
  );

  if ($entity->identifier() && entity_access('delete', $entity_type, $entity)) {
    $form['actions']['delete'] = array(
      '#type' => 'submit',
      '#value' => t('Delete'),
      '#submit' => array('contentblock_entity_form_delete_submit'),
    );
  }

  // Setup the destination and cancel link URL.
  $form_state['destination'] = drupal_get_destination();
  $entity_uri = $entity->uri();
  if ($entity_uri || $entity->machine_name) {
    $form['actions']['cancel'] = array(
      '#type' => 'link',
      '#title' => t('Cancel'),
      '#href' => $entity_uri['path'],
      '#weight' => 100,
    );

    if (!empty($entity->machine_name)) {
      $form['actions']['cancel']['#href'] = 'admin/settings/content-sections';
    }
  }

  if (!empty($_GET['destination'])) {
    $destination_parts = drupal_parse_url($_GET['destination']);
    $form['actions']['cancel']['#href'] = $destination_parts['path'];
    $form['actions']['cancel']['#options']['query'] = $destination_parts['query'];
  }

  // Attach fields, if any, and allow DS to order form items.
  field_attach_form($entity_type, $entity, $form, $form_state, entity_language($entity_type, $entity));

  // Attach validate and submit callbacks.
  $form['#validate'][] = '_contentblock_entity_form_validate';
  $form['#submit'][] = '_contentblock_entity_form_submit';

  if (helpergeneric_cleanup_ui()) {
    if (isset($form['redirect'])) {
      $form['redirect']['#access'] = FALSE;
    }
  }

  return $form;
}

/**
 * Form validation handler for contentblock_form().
 *
 * @see contentblock_form()
 * @see entity_form_field_validate()
 */
function _contentblock_entity_form_validate($form, &$form_state) {
  $entity_type = $form_state['entity_type'];
  $entity = $form_state[$entity_type];
  field_attach_form_validate($entity_type, $entity, $form, $form_state);
}

/**
 * Form API submit callback for contentblock_form().
 */
function _contentblock_entity_form_submit($form, &$form_state) {
  $entity_type = $form_state['entity_type'];

  // Remove unneeded values.
  form_state_values_clean($form_state);
  $entity = entity_ui_form_submit_build_entity($form, $form_state);

  // Set the timestamp fields.
  if (empty($entity->created)) {
    $entity->created = REQUEST_TIME;
  }
  // The changed timestamp is always updated for bookkeeping purposes,
  // for example: revisions, searching, etc.
  $entity->changed = REQUEST_TIME;

  if (!empty($entity->is_new)) {
    $entity->uid = $GLOBALS['user']->uid;
  }
  else {
    // @TODO: Add posibility to change user?!?
  }

  // Save the entity.
  entity_save($entity_type, $entity);

  // Prepare the messages.
  $entity_label = $entity->label();
  if (empty($entity_label) && !empty($entity->machine_name)) {
    $info = contentblock_get_hidden_types($entity->machine_name);
    if (!empty($info['title'])) {
      $entity_label = $info['title'];
    }
  }
  $watchdog_args = array('@type' => $entity->entityType(), '%title' => $entity_label);
  $t_args = array('@type' => contentblock_type_get_name($entity_type), '%title' => $entity_label);
  $entity_uri = entity_uri($entity_type, $entity);
  $entity_link = l(t('view'), $entity_uri['path']);

  if ($entity->identifier()) {
    if (empty($entity->machine_name)) {
      $form_state['redirect'] = entity_access('view', $entity_type, $entity) ? $entity_uri['path'] : '<front>';
    }
    else {
      $form_state['redirect'] = '<front>';
    }
  }

  if (empty($entity->is_new)) {
    watchdog('survey', '@type: updated %title.', $watchdog_args, WATCHDOG_NOTICE, $entity_link);
    drupal_set_message(t('@type %title has been updated.', $t_args));
  }
  else {
    watchdog('survey', '@type: added %title.', $watchdog_args, WATCHDOG_NOTICE, $entity_link);
    drupal_set_message(t('@type %title has been created.', $t_args));
  }

  // Clear the page and block caches.
  cache_clear_all();
}

/**
 * Form submission handler for contentblock_form().
 *
 * Handles the 'Delete' button on the entity form.
 *
 * @see contentblock_form()
 */
function contentblock_entity_form_delete_submit($form, &$form_state) {
  $entity_type = $form_state['entity_type'];
  $entity = $form_state[$entity_type];

  $destination = array();
  if (isset($_GET['destination'])) {
    $destination = drupal_get_destination();
    unset($_GET['destination']);
  }

  $entity_uri = entity_uri($entity_type, $entity);
  $form_state['redirect'] = array($entity_uri['path'] . '/delete', array('query' => $destination));
}

/**
 * Menu callback: Creates overview page for custom contentblock entites at admin/structure/%.
 */
function contentblock_entity_overview_types($entity_type) {
  $entity_info = entity_get_info($entity_type);
  if (empty($entity_info)) {
    return;
  }

  $header = array(t('Name'), array('data' => t('Operations'), 'colspan' => '2'));
  $rows = array();

  if (module_exists('field_ui') && !empty($entity_info['fieldable'])) {
    foreach ($entity_info['bundles'] as $bundle) {
      $url_path = $bundle['admin']['path'];
      if (isset($bundle['admin']['real path'])) {
        $url_path = $bundle['admin']['real path'];
      }

      $row = array();
      $row[] = $bundle['label'];
      $row[] = array('data' => l(t('manage fields'), $url_path . '/fields'));
      $row[] = array('data' => l(t('manage display'), $url_path . '/display'));

      $rows[] = $row;
    }
  }

  $build['table'] = array(
    '#theme' => 'table',
    '#header' => $header,
    '#rows' => $rows,
    '#empty' => t('No entity types available.'),
  );
  return $build;
}
