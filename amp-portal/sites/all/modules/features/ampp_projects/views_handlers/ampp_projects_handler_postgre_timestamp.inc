<?php
/**
 * 
 */
class ampp_projects_handler_postgre_timestamp extends views_handler_field_date {
	
  function query() {  	
  	$this->ensure_my_table();
  	$this->field_alias = $this->query->add_field(NULL, 'CASE WHEN ('.$this->table_alias.'.'.$this->field.' is NULL OR extract(epoch from '.$this->table_alias.'.'.$this->field.') < 0) THEN 0 ELSE extract(epoch from '.$this->table_alias.'.'.$this->field.') END', $this->table_alias . '_' . $this->field);  	
  }
}
