<?php

/**
 * Filters out Activities that are not public.
 *
 * @TODO: Add descriptions, split into several filters!
 */
class amp_activity_handler_filter_activity_filter_public extends views_handler_filter {
  /**
   * Hide the administrative summary.
   */
  function admin_summary() {
    return NULL;
  }

  /**
   * This is not a configurable filter.
   */
  function can_expose() {
    return FALSE;
  }

  /**
   * Create a custom filter query.
   */
  function query() {
    // This is a hackish attempt to optimize the query.
    $use_statically_cached_ids = TRUE;
    if ($use_statically_cached_ids) {
      // Get an array of 'public' activities.
      $items = amp_activity_get_amp_activities();

      $placeholder = ':db_filter_public_placeholder';
      $this->query->add_where_expression(0, "$this->table.$this->real_field IN ($placeholder)", array($placeholder => $items));

      return;
    }

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
