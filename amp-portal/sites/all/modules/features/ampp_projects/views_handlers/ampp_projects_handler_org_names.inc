<?php
/**
 * 
 */
class ampp_projects_handler_org_names extends views_handler_field {
  
  const  DELIMETER_SIGN = '||';
	
  function query() {  	
    global $language;
    
  	$this->ensure_my_table();
  	$translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', amp_organisation.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";  
  	$this->field_alias = $this->query->add_field(NULL, "array_to_string(array_agg(" . $translate_field . "), '".self::DELIMETER_SIGN."')", $this->table_alias . '_' . $this->field);  	
  }

  function render($values) {
  	
  	$output_array = explode(self::DELIMETER_SIGN, $values->{$this->field_alias});
  	$output_array_filtered = array();
  	foreach ($output_array as $key=>$value){
  		if(!empty($value) and $value != "NULL"){
  			if(!in_array($value, $output_array_filtered)){
  				$output_array_filtered[] = $value; 
  			}
  		}
  	} 
  	$output = implode(", <br>", $output_array_filtered);
    return $output;
  } 
}
