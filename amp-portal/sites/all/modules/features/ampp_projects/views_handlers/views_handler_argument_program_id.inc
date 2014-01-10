<?php
/**
 * Filter to handle sector Id filters
 */
class views_handler_argument_program_id extends views_handler_argument_numeric {
  
  function query($group_by = FALSE)  {
    
    $this->ensure_my_table();
    
    if (!empty($this->options['break_phrase'])) {
      views_break_phrase($this->argument, $this);
    }
    else {
      $this->value = array($this->argument);
    }
    watchdog('ampp_projects', 'PROGRAMS:' . $this->argument);
    $this->addParentProgramsIds();
    
    $placeholder = $this->placeholder();
    $null_check = empty($this->options['not']) ? '' : "OR $this->table_alias.$this->real_field IS NULL";
    
    if (count($this->value) > 1) {
      $operator = empty($this->options['not']) ? 'IN' : 'NOT IN';
      $this->query->add_where_expression(0, "$this->table_alias.$this->real_field $operator($placeholder) $null_check", array($placeholder => $this->value));
    }
    else {
      $operator = empty($this->options['not']) ? '=' : '!=';
      $this->query->add_where_expression(0, "$this->table_alias.$this->real_field $operator $placeholder $null_check", array($placeholder => $this->argument));
    }
  }
  
  private function addParentProgramsIds(){
    foreach ($this->value as $id){
      $this->value = array_merge($this->value, getChildProgramsIds($id));
    }
  } 
  
}