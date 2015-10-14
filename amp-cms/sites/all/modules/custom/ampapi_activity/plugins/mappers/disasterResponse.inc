<?php
/**
 * @file
 * disasterResponse plugin.
 */

$plugin = array(
  'title' => t('Disaster Response Marker'),
  'description' => t(''),

  'property name' => 'disaster_response_marker',
  'property info' => array(
    'type' => 'boolean',
  ),

  'report field group' => 'columns',
  'report field key' => 'Funding Status',
  'report filter key' => 'Funding Status',

  'report filter endpoint' => 'filters/fundingStatus',

  'handler' => 'disasterResponseMarkerMapper',
);

class disasterResponseMarkerMapper extends activityMapperBase implements activityMapperOptionsInterface {
  // This hacked mapper is similar to the humanitarianAidMapper mapper.
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
