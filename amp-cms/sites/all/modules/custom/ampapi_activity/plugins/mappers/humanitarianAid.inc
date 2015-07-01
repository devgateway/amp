<?php
/**
 * @file
 * humanitarianAid plugin.
 */

$plugin = array(
  'title' => t('Humanitarian Aid'),
  'description' => t(''),

  'property name' => 'humanitarian_aid',
  'property info' => array(
    'type' => 'boolean',
  ),

  'report field group' => 'columns',
  'report field key' => 'Humanitarian Aid',

  'report filter endpoint' => 'filters/humanitarianAid',

  'handler' => 'humanitarianAidMapper',
);

class humanitarianAidMapper extends activityMapperBase implements activityMapperOptionsInterface {
  public function getMatchedValue($plugin, $lookup_value) {
    if (!isset($lookup_value)) {
      return NULL;
    }

    $returned_data = ampapi_request('get', $plugin['report filter endpoint'], TRUE);

    if (!empty($returned_data->values)) {
      // Yes, the AMP API is very consistent. And (on the Nepal DB at least) the
      // booleans have numeric IDs: 1 == TRUE, 2 == FALSE.
      foreach ($returned_data->values as $value) {
        if ($lookup_value && $value->name == 'Yes') {
          return $value->id;
        }
        else if (!$lookup_value && $value->name == 'No') {
          return $value->id;
        }
      }
    }

    return (bool) $lookup_value;
  }
}
