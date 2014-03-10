<?php
/**
 *
 */
class views_handler_field_status extends views_handler_field {

  function render($values){
    $values->cached_v_status_name = t($values->cached_v_status_name);
    return parent::render($values);
  }
}
