<?php


/**
 * Field handler for entity boolean fields.
 */
class ampapi_activity_views_handler_field_boolean extends entity_views_handler_field_boolean {

  /**
   * Render a single field value.
   */
  public function render_single_value($value, $values) {
    // "No information" - means an empty value, don't ask.
    if (!is_bool($value) && ($value === "" || $value === 'No information')) {
      return t('N/A');
    }
    else {
      return parent::render_single_value($value, $values);
    }
  }

  /**
   * Overridden value fetcher.
   *
   * Here is where we decide what value we want to show.
   */
  public function get_value($values, $field = NULL) {
    $received_value = EntityFieldHandlerHelper::get_value($this, $values, $field);
    // Sometimes it's called second time, in that case we just check if it's a bool or not,
    // and if it's not, we judge according to the string value.
    if (!is_bool($received_value)) {
      if ($received_value == 'No') {
        return FALSE;
      }
      elseif ($received_value == 'Yes') {
        return TRUE;
      }
    }
    return $received_value;
  }
}
