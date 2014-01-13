<?php
/**
 * 
 */
class ampp_projects_handler_location_ids extends views_handler_field {
	
  function query() {  	
  	$this->ensure_my_table();
  	$this->field_alias = $this->query->add_field(NULL,'array_agg(amp_activity_location.amp_location_id)', $this->table_alias . '_' . $this->field);  	
  }

  function render($values) {
  	
  	$original = $values->{$this->field_alias};  	
  	$output = substr($original, 1, strlen($original)-2);  	
    return $output;
  } 
}
