<?php
/**
 * @file
 * Class for the Panelizer taxonomy term entity plugin.
 */

/**
 * Panelizer Entity taxonomy term plugin class.
 *
 * Handles term specific functionality for Panelizer.
 */
class PanelizerEntityTaxonomyTerm extends PanelizerEntityDefault {

  public function entity_access($op, $entity) {
    // This must be implemented by the extending class.
    if ($op == 'update' || $op == 'delete') {
      return taxonomy_term_edit_access($entity);
    }

    if ($op == 'view') {
      return TRUE;
    }

    return FALSE;
  }

  /**
   * Implement the save function for the entity.
   */
  public function entity_save($entity) {
    taxonomy_term_save($entity);
  }

  /**
   * Implement the save function for the entity.
   */
  public function entity_allows_revisions($entity) {
    return array(FALSE, FALSE);

  }

  public function settings_form(&$form, &$form_state) {
    parent::settings_form($form, $form_state);

    $warn = FALSE;
    foreach ($this->plugin['bundles'] as $info) {
      if (!empty($info['status'])) {
        $warn = TRUE;
        break;
      }
    }

    if ($warn) {
      $task = page_manager_get_task('term_view');
      if (!empty($task['disabled'])) {
        drupal_set_message('The taxonomy term template page is currently not enabled in page manager. You must enable this for Panelizer to be able to panelize taxonomy terms.', 'warning');
      }

      $handler = page_manager_load_task_handler($task, '', 'term_view_panelizer');
      if (!empty($handler->disabled)) {
        drupal_set_message('The panelizer variant on the taxonomy term template page is currently not enabled in page manager. You must enable this for Panelizer to be able to panelize taxonomy terms.', 'warning');
      }
    }
  }

  public function entity_identifier($entity) {
    return t('This taxonomy term');
  }

  public function entity_bundle_label() {
    return t('Taxonomy vocabulary');
  }

  function get_default_display() {
    // For now we just go with the empty display.
    // @todo come up with a better default display.
    return parent::get_default_display();
  }

  /**
   * Implements a delegated hook_page_manager_handlers().
   *
   * This makes sure that all panelized entities have the proper entry
   * in page manager for rendering.
   */
  public function hook_default_page_manager_handlers(&$handlers) {
    page_manager_get_task('term_view');

    $handler = new stdClass;
    $handler->disabled = FALSE; /* Edit this to true to make a default handler disabled initially */
    $handler->api_version = 1;
    $handler->name = 'term_view_panelizer';
    $handler->task = 'term_view';
    $handler->subtask = '';
    $handler->handler = 'panelizer_node';
    $handler->weight = -100;
    $handler->conf = array(
      'title' => t('Term panelizer'),
      'context' => page_manager_term_view_get_type() == 'multiple' ? 'argument_terms_1' : 'argument_term_1',
      'access' => array(),
    );
    $handlers['term_view_panelizer'] = $handler;

    return $handlers;
  }

}
