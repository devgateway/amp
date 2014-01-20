<?php 
/**
 *
 */
class ampp_projects_handler_activity_id extends views_handler_field {

  function render($values) {
    global $language;
      
    $activityId = $values->{$this->field_alias};
    $img =  base_path() . drupal_get_path('theme', 'ampp')."/images/magnifier_icon.png";
    $activityURI = strtr(variable_get('amp_activity_url', '/'), array('%aid%' => $activityId));
    $activityURI = strtr($activityURI, array('%lang_code%' => $language->language));
    $output = '<img class="magnifier" src="' . $img . '" onclick="window.open(\'' . $activityURI . '\', \'activityInfo\',\'' . variable_get('amp_activity_popup_str') . '\')"/>';
    return $output;
  }
}

