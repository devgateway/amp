<?php

/**
 * @file
 * Defines the inline entity form controller for Nodes.
 */

class ContentBlockInlineEntityFormController extends EntityInlineEntityFormController {

  /**
  * Overrides EntityInlineEntityFormController::defaultLabels().
   */
  public function defaultLabels() {
    $labels = array(
      'singular' => t('block'),
      'plural' => t('blocks'),
    );
    return $labels;
  }

  /**
   * Overrides EntityInlineEntityFormController::tableFields().
   */
  public function tableFields($bundles) {
    $fields = parent::tableFields($bundles);

    return $fields;
  }

  /**
   * Overrides EntityInlineEntityFormController::entityForm().
   */
  public function entityForm($entity_form, &$form_state) {
    $entity = $entity_form['#entity'];
    $extra_fields = field_info_extra_fields('contentblock', $entity->bundle(), 'form');

    $entity_form['title'] = array(
      '#type' => 'textfield',
      '#title' => t('Title'),
      '#required' => TRUE,
      '#default_value' => $entity->title,
      '#maxlength' => 255,
      // The label might be missing if the Title module has replaced it.
      '#weight' => !empty($extra_fields['title']) ? $extra_fields['title']['weight'] : -5,
    );

    $langcode = entity_language('contentblock', $entity);
    field_attach_form('contentblock', $entity, $entity_form, $form_state, $langcode);

    return $entity_form;
  }
}
