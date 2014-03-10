<?php
/**
 *
 */
class ampp_projects_handler_activity_name extends views_handler_field {

  function query() {
    global $language;

    $this->ensure_my_table();
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpActivityVersion', 'name', cached_amp_activity.amp_activity_id, 'amp_activity_version', 'name', 'amp_activity_id', '" . $language->language . "')";
    $this->field_alias = $this->query->add_field(NULL, $translate_field, $this->table_alias . '_' . $this->field);
  }

  function render($values) {
    global $language;

    $activityId = $values->amp_activity_id;
    $activityName = $values->{$this->field_alias};
    $activityURI = strtr(variable_get('amp_activity_url', '/'), array('%aid%' => $activityId));
    $activityURI = strtr($activityURI, array('%lang_code%' => $language->language));
    $output = '<a href="#" onclick="window.open(\'' . $activityURI . '\', \'activityInfo\',\'' . variable_get('amp_activity_popup_str') . '\'); return false;">' . $activityName . '</a>';
    return $output;
  }
}

