<?php

/**
 * Field handler for entity properties.
 */
class ampapi_activity_views_handler_field_with_currency extends entity_views_handler_field_text {
  /**
   * Get this field's label taken into account particular heading rules.
   */
  function label() {
    if (!isset($this->options['label'])) {
      return '';
    }
    if (isset($this->query->query->metaData['report_currency'])) {
      $currency = $this->query->query->metaData['report_currency'];
      return $this->options['label'] .  ' (' . check_plain($currency) . ')';
    }
    return $this->options['label'];
  }
}
