<?php 
class views_handler_filter_approval_status_in_operator extends views_handler_filter_in_operator {
  var $value_form_type = 'checkboxes';
  
  function construct() {
    parent::construct();
  }

  function get_value_options() {
    if (isset($this->value_options)) {
      return;
    }
	db_set_active('amp_projects');
	$sql = "SELECT DISTINCT approval_status FROM cached_amp_activity";
	$records = db_query($sql);
	$this->value_options = array();
	db_set_active();
	foreach ($records as $record) {
		if(!empty($record->approval_status))
			$this->value_options[$record->approval_status] = $record->approval_status; 
	}
  }
} 