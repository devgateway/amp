<?php


class views_handler_filter_amp_activity_id extends views_handler_filter {
 
  /**
   * Determine if a filter can be exposed.
   */
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
  function can_expose() { 
    return FALSE; 
  }


  /**
   *
   */
  function query() {
	
	$snippet  = "cached_amp_activity.amp_team_id is not null";
	$this->query->add_where_expression(0, $snippet);
	
	$snippet  = "cached_amp_activity.approval_status IN ('approved', 'startedapproved')";
    $snippet .= " AND cached_amp_activity.draft <> true"; 
    $snippet .= " AND cached_amp_activity.amp_team_id IN (select amp_team_id from amp_team where parent_team_id IN (select amp_team_id from amp_team where access_type = 'Management'))"; 
    $snippet .= " OR (";
    $snippet .= "cached_amp_activity.approval_status IN ('approved', 'startedapproved')";
    $snippet .= " AND cached_amp_activity.draft <> true";
    $snippet .= " AND cached_amp_activity.amp_activity_id IN (select distinct(activity) from amp_org_role where organisation IN (select amp_org_id from amp_team_orgs where amp_team_id IN (select amp_team_id from amp_team where parent_team_id in (select amp_team_id from amp_team where access_type='Management')))))";
	  
	$this->query->add_where_expression(0, $snippet);
  }
}