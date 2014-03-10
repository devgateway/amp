<?php
/**
 *
 */
class ampp_projects_handler_location_names extends views_handler_field {

  const  DELIMETER_SIGN = '||';

  function query() {
    global $language;

    $this->ensure_my_table();
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpCategoryValueLocations', 'name', amp_category_value_location.id, 'amp_category_value_location', 'region', 'id', '" . $language->language . "')";
    $this->field_alias = $this->query->add_field(NULL, "array_to_string(array_agg(" . $translate_field . "), '".self::DELIMETER_SIGN."')", $this->table_alias . '_' . $this->field);
  }

  function render($values) {

    $output_array = explode(self::DELIMETER_SIGN, $values->{$this->field_alias});
    $output_array_filtered = array();
    $output = "";
    foreach ($output_array as $key=>$value){
      if(!empty($value) and $value != "NULL"){
        if(!in_array($value, $output_array_filtered)){
          $output_array_filtered[] = $value;
        }
      }
    }
    if(count($output_array_filtered) <= 5){
      $output = implode(", <br>", $output_array_filtered);
    }
    else{
      foreach ($output_array_filtered as $n=>$value){
        if($n==5){
          $output .= "<span class='more-locations' id='more-".$values->amp_activity_id."'>".t("more...")."</span>";
          $output .= "<span class='more-locations' style='display: none' id='less-".$values->amp_activity_id."'>".t("less...")."</span>";
          $output .= "<div class='hidden-locations' id='hidden-".$values->amp_activity_id."'>";
        }
        $output .= $value;
        if($n+1 != count($output_array_filtered)){
          $output .= ", <br>";
        }
      }
      $output .= "</div>";
    }
    return $output;
  }
}
