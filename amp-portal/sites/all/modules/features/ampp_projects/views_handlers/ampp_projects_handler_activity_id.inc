<?php 
/**
 *
 */
class ampp_projects_handler_activity_id extends views_handler_field {

  function render($values) {     
    $activityId = $values->{$this->field_alias};
    $img =  base_path() . drupal_get_path('theme', 'ampp')."/images/magnifier_icon.png";
    $activitiURI = strtr(variable_get('amp_activity_url', '/'), array('%aid%' =>$activityId));
    $output = '<img class="magnifier" src="'.$img.'" onclick="window.open(\''.$activitiURI.'\', \'activityInfo\',\''.variable_get('amp_activity_popup_str').'\')"/>';
    return $output;
  }
}

