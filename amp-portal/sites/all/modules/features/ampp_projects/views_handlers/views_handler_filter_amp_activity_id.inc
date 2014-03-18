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
   * Override the default query.
   * @TODO: Add descriptions, split into several filters!
   */
  function query() {
    $this->query->set_group_operator('AND', 0);

    $group = 0;
    $this->query->set_where_group('AND', $group);
    $snippets[$group][] = "cached_amp_activity.approval_status IN ('approved', 'startedapproved')";
    $snippets[$group][] = "cached_amp_activity.draft <> TRUE";
    $snippets[$group][] = "cached_amp_activity.amp_team_id IS NOT NULL";

    $group = 1;
    $this->query->set_where_group('OR', $group);

    // Get Activities which have one parent of type "Management"? I guess!?!
    $case_one_sub = "SELECT amp_team_id FROM amp_team WHERE access_type = 'Management'";
    $case_one = "SELECT amp_team_id FROM amp_team WHERE parent_team_id IN ($case_one_sub)";
    $snippets[$group][] = "( cached_amp_activity.amp_team_id IN ($case_one) )";

    // It sure does something... but I don't know what to name it.
    $case_two_sub = "SELECT amp_org_id FROM amp_team_orgs WHERE amp_team_id IN ($case_one)";
    $case_two = "SELECT DISTINCT(activity) FROM amp_org_role WHERE organisation IN ($case_two_sub)";
    $snippets[$group][] = "( cached_amp_activity.amp_activity_id IN ($case_two) )";

    foreach ($snippets as $group => $expressions) {
      foreach ($expressions as $expression) {
        $this->query->add_where_expression($group, $expression);
      }
    }
  }
}
