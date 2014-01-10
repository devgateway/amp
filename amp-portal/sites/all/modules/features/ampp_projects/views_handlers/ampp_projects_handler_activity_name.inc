<?php 
/**
 *
 */
class ampp_projects_handler_activity_name extends views_handler_field {

  function render($values) {     
    $activityId = $values->amp_activity_id;
    $activityName = $values->{$this->field_alias};
    $activityURI = strtr(variable_get('amp_activity_url', '/'), array('%aid%' => $activityId));
    $output = '<a href="#" onclick="window.open(\'' . $activityURI . '\', \'activityInfo\',\'' . variable_get('amp_activity_popup_str') . '\'); return false;">' . $activityName . '</a>';
    return $output;
  }
}

