<?php
class views_handler_filter_amp_team_id extends views_handler_filter {
 
 function option_definition() {
   $options = parent::option_definition();

    $options['operator'] = array('default' => '');
    $options['value'] = array('default' => '');
    $options['group'] = array('default' => '0');
    $options['exposed'] = array('default' => FALSE);
    $options['expose'] = array(
      'contains' => array(
        'operator_id' => array('default' => FALSE),
        'label' => array('default' => '', 'translatable' => TRUE),
        'use_operator' => array('default' => 0),
        'operator' => array('default' => ''),
        'identifier' => array('default' => ''),
        'required' => array('default' => 0),
        'remember' => array('default' => 0),
        'multiple' => array('default' => 0),
      ),
    );

    return $options;
  }	
	
  /**
   * Determine if a filter can be exposed.
   */
  function can_expose() { return FALSE; }
  
  function query() {
    $amp_managment_query = db_select('amp_team', 'amp_team')->fields('amp_team', array('amp_team_id'))->condition('access_type', 'Management', '=');
    $amp_team_sub_query = db_select('amp_team', 'amp_team')->fields('amp_team', array('amp_team_id'))->condition('parent_team_id', $amp_managment_query, 'IN');
    $amp_managment_sub_query = db_select('amp_team', 'amp_team')
  							 ->fields('amp_team', array('amp_team_id'))
  							 ->condition('parent_team_id', $amp_managment_query, 'IN');
  		
    $this->query->add_where($this->options['group'], 'cached_amp_activity.amp_team_id', $amp_managment_sub_query, 'IN');
  }
}