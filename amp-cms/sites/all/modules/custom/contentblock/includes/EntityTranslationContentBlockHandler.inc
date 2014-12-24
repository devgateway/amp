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

  /**
   * @see EntityTranslationDefaultHandler::entityFormLanguageWidgetSubmit()
   */
  function entityFormLanguageWidgetSubmit($form, &$form_state) {
    $this->updateFormLanguage($form_state);

    $is_translation = $this->isTranslationForm();
    if ($is_translation) {
      return;
    }
    $form_langcode = $this->getFormLanguage();

    foreach (field_info_instances($this->entityType, $this->bundle) as $instance) {
      $field_name = $instance['field_name'];
      if (!isset($form[$field_name]['#language'])) {
        continue;
      }

      $field = field_info_field($field_name);
      if (!$field['translatable']) {
        continue;
      }

      $previous_langcode = $form[$field_name]['#language'];
      if ($previous_langcode == $form_langcode) {
        continue;
      }

      // Handle a possible language change: new language values are inserted,
      // previous ones are deleted.
      if (isset($form_state['values'][$field_name][$previous_langcode])) {
        $form_state['values'][$field_name][$form_langcode] = $form_state['values'][$field_name][$previous_langcode];
        $form_state['values'][$field_name][$previous_langcode] = array();
      }
      if (isset($form_state['input'][$field_name][$previous_langcode])) {
        $form_state['input'][$field_name][$form_langcode] = $form_state['input'][$field_name][$previous_langcode];
        $form_state['input'][$field_name][$previous_langcode] = array();
      }
    }
  }
}
