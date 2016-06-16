<?php
/**
 * @file
 * nationalPlanObjective plugin.
 */

$plugin = array(
  'title' => t('National Plan Objective'),
  'description' => t(''),

  'property name' => 'national_plan_objective',
  'property info' => array(
    'type' => 'text',
  ),

  'report field group' => 'columns',
  'report field key' => 'National Plan Objective',
  // Changed in 2.12.
  // @TODO: Make things simpler.
  'report filter key' => 'National Planning Objectives Level 1',

  'report filter endpoint' => 'filters/programs',
  'report sub-filter filter-id' => 'National Plan Objective',
  'report filter endpoint extra' => 'filters/programs',

  'handler' => 'nationalPlanObjectiveMapper',
);

class nationalPlanObjectiveMapper extends activityMapperSubFilterAutocomplete {
}
