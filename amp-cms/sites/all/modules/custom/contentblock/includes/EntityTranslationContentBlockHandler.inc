<?php

/**
 * @file
 * ContentBlock translation handler for the entity translation module.
 *
 * @TODO: Add translation status support.
 */


/**
 * ContentBlock translation handler.
 *
 * Overrides default behaviours for ContentBlock properties.
 */
class EntityTranslationContentBlockHandler extends EntityTranslationDefaultHandler {
  public function __construct($entity_type, $entity_info, $entity) {
    parent::__construct('contentblock', $entity_info, $entity);
  }

  /**
   * @see EntityTranslationDefaultHandler::getAccess()
   */
  public function getAccess($op) {
    if ($op == 'update') {
      $op = 'edit';
    }
    return contentblock_access($op, $this->entity);
  }

  /**
   * @see EntityTranslationDefaultHandler::getTranslationAccess()
   */
  public function getTranslationAccess($langcode) {
    return contentblock_access('administer') || contentblock_access('edit', $this->entity);
  }

  /**
   * Convert the translation update status fieldset into a vartical tab.
   */
  public function entityForm(&$form, &$form_state) {
    parent::entityForm($form, $form_state);

    // Move the translation fieldset to a vertical tab.
    if (isset($form['translation'])) {
      $form['translation']['status']['#access'] = FALSE;
      $form['translation']['name']['#access'] = FALSE;
      $form['translation']['created']['#access'] = FALSE;
    }
  }

  // /**
  //  * @see EntityTranslationDefaultHandler::entityFormLanguageWidgetSubmit()
  //  */
  // function entityFormLanguageWidgetSubmit($form, &$form_state) {
  //   $this->updateFormLanguage($form_state);
  // }

  // /**
  //  * @see EntityTranslationDefaultHandler::entityFormSubmit()
  //  */
  // public function entityFormSubmit($form, &$form_state) {
  //   if (!isset($form_state['values']['translation'])) {
  //     // Always publish the original values when we have no translations.
  //     $form_state['values']['translation'] = array('status' => TRUE);
  //   }
  //   $values = &$form_state['values']['translation'];

  //   if (!$this->isTranslationForm()) {
  //     // Inherit entity authoring information for the original values.
  //     $values['name'] = $form_state['values']['name'];
  //     if (!empty($form_state['values']['date'])) {
  //       $values['created'] = $form_state['values']['date'];
  //     }
  //   }

  //   parent::entityFormSubmit($form, $form_state);
  // }

  /**
   * @see EntityTranslationDefaultHandler::getStatus()
   */
  protected function getStatus() {
    // return (boolean) $this->entity->status;
    return TRUE;
  }
}
