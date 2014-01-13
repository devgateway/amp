<?php

/**
 * @file
 * Administrative functionality for Feedback module.
 */

/**
 * Build a (sortable) form containing a checkbox for each feedback entry.
 */
function feedback_admin_view_form($form, &$form_state) {
  $status_headings = array(
    FEEDBACK_OPEN => t('Open feedback messages'),
    FEEDBACK_PROCESSED => t('Processed feedback messages'),
  );
  $form['#feedback_header'] = array(
    array(),
    array('data' => t('Location'), 'field' => 'f.location_masked', 'sort' => 'asc'),
    array('data' => t('Date'), 'field' => 'f.timestamp'),
    array('data' => t('User'), 'field' => 'u.name'),
    t('Message'),
    t('Delete'),
  );
  // Hack to prevent pager_query() from issuing PHP notices.
  if (!isset($_GET['page'])) {
    $_GET['page'] = '';
  }
  if (count(explode(',', $_GET['page'])) < 2) {
    $_GET['page'] .= ',0';
  }

  $form['feedback-messages'] = array('#tree' => TRUE);
  $query = db_select('feedback', 'f')->extend('PagerDefault')->extend('TableSort');
  $query->join('users', 'u', 'f.uid = u.uid');
  $query->fields('f')
    ->limit(50);
  foreach (array(FEEDBACK_OPEN, FEEDBACK_PROCESSED) as $status) {
    $status_query = clone $query;
    $fids = $status_query->element($status)
      ->condition('f.status', $status)
      ->execute()->fetchCol();
    $form['feedback-messages'][$status] = array(
      '#type' => 'fieldset',
      '#title' => $status_headings[$status],
      '#collapsible' => TRUE,
      '#collapsed' => $status,
      '#attributes' => array('class' => array('feedback-messages')),
    );
    if (!empty($fids)) {
      $entries = feedback_load_multiple($fids);
      foreach ($entries as $fid => $entry) {
        $form['feedback-messages'][$status][$fid] = array(
          '#type' => 'checkbox',
          '#return_value' => FEEDBACK_PROCESSED,
          '#default_value' => FALSE,
        );
        $form['feedback-messages'][$status][$fid]['location'] = array('#markup' => l(truncate_utf8($entry->location, 32, FALSE, TRUE), $entry->url));
        $form['feedback-messages'][$status][$fid]['date'] = array('#markup' => format_date($entry->timestamp, 'small'));
        $form['feedback-messages'][$status][$fid]['user'] = array('#markup' => format_username($entry));
        $form['feedback-messages'][$status][$fid]['message'] = feedback_format_message($entry);
        $form['feedback-messages'][$status][$fid]['delete'] = array('#type' => 'link', '#title' => t('delete'), '#href' => "admin/reports/feedback/$fid/delete");
      }
    }
  }
  $form['submit'] = array('#type' => 'submit', '#value' => t('Submit'));
  return $form;
}

/**
 * Output a sortable table containing all feedback entries.
 */
function theme_feedback_admin_view_form($variables) {
  $form = $variables['form'];
  $output = '';
  foreach (element_children($form['feedback-messages']) as $status) {
    $item = &$form['feedback-messages'][$status];
    if (!isset($item['#type']) || $item['#type'] != 'fieldset') {
      continue;
    }
    // Build the table.
    $rows = array();
    foreach (element_children($item) as $element_entry) {
      $entry = &$item[$element_entry];
      // Render the data first.
      $rows[] = array(
        0,
        drupal_render($entry['location']),
        drupal_render($entry['date']),
        drupal_render($entry['user']),
        drupal_render($entry['message']),
        drupal_render($entry['delete']),
      );
      // Render the checkbox.
      $rows[count($rows) - 1][0] = drupal_render($entry);
    }
    if (empty($rows)) {
      $rows[] = array(array('data' => t('No feedback entries available.'), 'colspan' => 6));
    }
    // Inject the table.
    $item['messages'] = array(
      '#markup' => theme('table', array('header' => $form['#feedback_header'], 'rows' => $rows)),
      '#suffix' => theme('pager', array('element' => $status)),
      '#weight' => -1,
    );
    // Render the fieldset.
    $output .= drupal_render($item);
  }
  // Render internal FAPI and potential extra form elements.
  $output .= drupal_render_children($form);
  return $output;
}

/**
 * Form submit callback for admin view form.
 */
function feedback_admin_view_form_submit($form, &$form_state) {
  $update = array();
  // Determine feedback entries to update.
  foreach ($form_state['values']['feedback-messages'] as $status => $values) {
    $values = array_filter($values);
    if (!empty($values)) {
      $entries = feedback_load_multiple(array_keys($values));
      foreach ($entries as $fid => $entry) {
        $entry->status = ($status == FEEDBACK_OPEN ? FEEDBACK_PROCESSED : FEEDBACK_OPEN);
        feedback_save($entry);
      }
    }
  }
}

/**
 * Form builder; The general feedback settings form.
 *
 * @ingroup forms
 */
function feedback_admin_settings_form($form, &$form_state) {
  $form['feedback_excluded_paths'] = array(
    '#type' => 'textarea',
    '#title' => t('Paths to exclude from feedback display'),
    '#default_value' => variable_get('feedback_excluded_paths', 'admin/reports/feedback'),
    '#description' => t("Specify pages by using their paths. Enter one path per line. The '*' character is a wildcard. Example paths are %blog for the blog page and %blog-wildcard for every personal blog. %front is the front page.", array('%blog' => 'blog', '%blog-wildcard' => 'blog/*', '%front' => '<front>')),
  );
  return system_settings_form($form);
}

/**
 * Generate an array for rendering the given entry.
 *
 * @param $entry
 *   A feedback entry object.
 * @param $view_mode
 *   View mode, e.g. 'full'.
 * @param $langcode
 *   (optional) A language code to use for rendering. Defaults to the global
 *   content language of the current request.
 *
 * @return
 *   An array as expected by drupal_render().
 */
function feedback_view($entry, $view_mode = 'full', $langcode = NULL) {
  if (!isset($langcode)) {
    $langcode = $GLOBALS['language_content']->language;
  }

  // Retrieve all entry fields and attach to $entry->content.
  feedback_build_content($entry, $view_mode, $langcode);

  $build = $entry->content;
  // We don't need duplicate rendering info in entry->content.
  unset($entry->content);

  $build += array(
    '#theme' => 'feedback_entry',
    '#entry' => $entry,
    '#view_mode' => $view_mode,
    '#language' => $langcode,
  );

  // Allow modules to modify the structured entry.
  $type = 'feedback';
  drupal_alter(array('feedback_view', 'entity_view'), $build, $type);

  return $build;
}

/**
 * Builds a structured array representing the feedback message content.
 *
 * @param $entry
 *   A feedback entry object.
 * @param $view_mode
 *   View mode, e.g. 'full'.
 * @param $langcode
 *   (optional) A language code to use for rendering. Defaults to the global
 *   content language of the current request.
 */
function feedback_build_content($entry, $view_mode = 'full', $langcode = NULL) {
  if (!isset($langcode)) {
    $langcode = $GLOBALS['language_content']->language;
  }

  // Build fields content.
  field_attach_prepare_view('feedback', array($entry->fid => $entry), $view_mode);
  entity_prepare_view('feedback', array($entry->fid => $entry));
  $entry->content = field_attach_view('feedback', $entry, $view_mode, $langcode);

  module_invoke_all('feedback_view', $entry, $view_mode, $langcode);
  module_invoke_all('entity_view', $entry, 'feedback', $view_mode, $langcode);
}

/**
 * Process variables for feedback-entry.tpl.php.
 *
 * The $variables array contains the following arguments:
 * - $entry
 *
 * @see feedback-entry.tpl.php
 */
function template_preprocess_feedback_entry(&$variables) {
  foreach (element_children($variables['elements']) as $key) {
    $variables['content'][$key] = $variables['elements'][$key];
  }

  $entry = $variables['elements']['#entry'];

  // Preprocess fields.
  field_attach_preprocess('feedback', $entry, $variables['elements'], $variables);

  $variables['location'] = l($entry->location, $entry->url);
  $variables['date'] = format_date($entry->timestamp, 'small');
  $variables['account'] = format_username($entry);
  $variables['message'] = feedback_format_message($entry);
}

/**
 * Form constructor for the feedback delete confirmation form.
 *
 * @param $entry
 *   The feedback entry to delete.
 *
 * @see feedback_delete_confirm_submit()
 * @ingroup forms
 */
function feedback_delete_confirm($form, &$form_state, $entry) {
  $form['fid'] = array('#type' => 'value', '#value' => $entry->fid);
  $output = confirm_form($form,
    t('Are you sure you want to delete the feedback entry?'),
    'admin/reports/feedback',
    NULL,
    t('Delete'));
  return $output;
}

/**
 * Process feedback_delete_confirm() form submissions.
 */
function feedback_delete_confirm_submit($form, &$form_state) {
  feedback_delete($form_state['values']['fid']);
  drupal_set_message(t('The feedback entry was deleted.'));

  $form_state['redirect'] = 'admin/reports/feedback';
}
