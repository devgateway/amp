<?php

/**
 * @file
 * Shared classes and interfaces for the API mapper system.
 *
 * @TODO: Reduce complexity!!
 */

class activityMapperAutocompleteBase extends activityMapperBase implements activityMapperAutocompleteInterface {
  public function autocomplete($plugin, $data, $type = 'autocomplete') {
    $info = $this->getAutocompleteInfo($plugin, $data, $type);

    if (empty($info)) {
      return array();
    }

    $returned_data = ampapi_request('get', $info['endpoint'], TRUE);

    // Because we like to be consistent.
    switch ($plugin['report filter endpoint']) {
      case 'filters/locations':
        // Include top level (the country name) in the autocomplete suggestions.
        $returned_data = array($returned_data);
        break;
      case 'filters/sectors':
        // Do not include top level (Primary/Secondary) in the autocomplete suggestions.
        $returned_data = $returned_data->children;
        break;
    }
    $returned_data = $this->filterData($info, $returned_data);
    return $this->getMatchedValues($info, $returned_data);
  }

  public function getAutocompleteInfo($plugin, $data, $type) {
    $info = array();

    if (in_array($type, array('autocomplete', 'tokeninput'))) {
      $info['string'] = reset($data);
    }
    else {
      $info['values'] = $data;
    }

    $info['plugin'] = $plugin;
    $info['type'] = $type;

    $info['endpoint'] = $plugin['report filter endpoint'];

    return $info;
  }

  /**
   * Public function that filters out the data according the certain values in $info.
   *
   * @param array $info
   *   Info array.
   * @param  array $data
   *   Array of data that needs to be filtered.
   * @return array
   *   Filtered data.
   */
  public function filterData($info, $data) {
    return $data;
  }

  public function getMatchedValues(array $info, array $entries, $top_level = TRUE) {
    global $language;
    $matches = array();

    $search_string = '';
    if (in_array($info['type'], array('autocomplete', 'tokeninput'))) {
      $search_string = transliteration_get($info['string'], '', $language->language);
    }

    foreach ($entries as $entry) {
      if (count($matches) >= 10) {
        break;
      }

      // Match full codes and codes starting with the provided string.
      $prepend_entry = FALSE;
      if ($info['plugin']['report filter endpoint'] == 'filters/sectors') {
        if (isset($entry->code) && is_numeric($search_string) && $search_string == $entry->code) {
          $prepend_entry = TRUE;
        }
      }


      switch ($info['type']) {
        case 'tokeninput':
        case 'autocomplete':
          $skip = TRUE;

          $entry_name = transliteration_get($entry->name, '', $language->language);
          if (stripos($entry_name, $search_string) !== FALSE) {
            $skip = FALSE;
          }

          if ($info['plugin']['report filter endpoint'] == 'filters/orgs') {
            if (isset($entry->acronym) && stripos($entry->acronym, $search_string) !== FALSE) {
              $skip = FALSE;
            }
          }

          // Filter out donors without funding.
          // @TODO: Refactor.
          if ($info['plugin']['property name'] == 'donor_agency') {
            if (!$skip && !empty($entry->hasFundings)) {
              $skip = FALSE;
            }
            else {
              $skip = TRUE;
            }
          }

          if ($skip) {
            continue 2;
          }
          break;
        case 'autocompleteids':
          // Special case for autocomplete powered view filters.
          // Not using the jQuery TokenInput library.
          if ($entry->name != $search_string) {
            continue 2;
          }
          break;
        case 'tokeninputids':
          if (!in_array($entry->id, $info['values'])) {
            continue 2;
          }
          break;

        default:
          continue 2;
          break;
      }

      // Include parent Location Administrative Levels in name, if any.
      $name = '';
      if ($info['plugin']['report filter endpoint'] == 'filters/locations' && isset($info['parents'])) {
        foreach ($info['parents'] as $parent) {
          if (empty($parent->top_level)) {
            $name .= filter_xss_admin($parent->name) . ' > ';
          }
        }
      }

      // Include acronym in name, if any.
      $name .= filter_xss_admin($entry->name);
      if (isset($entry->acronym)) {
        $name .= ' (' . filter_xss_admin($entry->acronym) . ')';
      }

      $new_value = array(
        'id' => intval($entry->id),
        'name' => $name,
      );

      if (!$prepend_entry) {
        $matches[] = $new_value;
      }
      else {
        $matches = array_shift($new_value, $matches);
      }
    }

    // Search sub-levels, if any.
    foreach ($entries as $entry) {
      if (count($matches) >= 10) {
        break;
      }

      if (!empty($entry->children)) {
        // We need to change the info only for the current children.
        $temp_info = $info;
        if ($top_level) {
          $entry->top_level = TRUE;
        }
        $temp_info['parents'][] = $entry;

        $matches = array_merge($matches, $this->getMatchedValues($temp_info, $entry->children, FALSE));
      }
    }

    return $matches;
  }

  public function getAutocompleteValue($plugin, $data, $type) {
    $matches = $this->autocomplete($plugin, $data, $type);
    if (empty($matches)) {
      return NULL;
    }

    // Special case for autocomplete powered view filters.
    // Not using the jQuery TokenInput library.
    if ($type == 'autocompleteids') {
      $entry = reset($matches);
      if (isset($entry['id'])) {
        return array(intval($entry['id']));
      }
    }

    return $matches;
  }
}

class activityMapperRoleAutocomplete extends activityMapperAutocompleteBase {
  public function getAutocompleteInfo($plugin, $data, $type) {
    $info = parent::getAutocompleteInfo($plugin, $data, $type);

    $role_id = $this->getRoleId($plugin);
    if (empty($role_id)) {
      return NULL;
    }

    $info['role_id'] = $role_id;

    return $info;
  }

  // @TODO: Request ID from the API.
  public function getRoleId($plugin) {
    static $roles_data;

    if (!isset($roles_data)) {
      $roles_data = ampapi_request('get', $plugin['report filter endpoint extra'], TRUE);
    }

    if (empty($roles_data)) {
      return FALSE;
    }

    // @HACK: Temporary!
    if ($plugin['report field key'] == 'Donor Agency') {
      $plugin['report field key'] = 'Donor'; // Yes, we have a consistent API.
    }

    foreach ($roles_data as $info) {
      if ($info->name == $plugin['report field key']) {
        return $info->id;
      }
    }
  }

    /**
   * {@inheritdoc}
   */
  public function filterData($info, $data) {
    // Apply global / parent filter initially.
    $filtered_data = parent::filterData($info, $data);
    // Now, for filters/orgs, we want to apply sub_filter_id filtering.
    if ($info['endpoint'] == 'filters/orgs' && isset($info['role_id'])) {
      foreach ($filtered_data as $fd_key => $row) {
        if (!in_array($info['role_id'], $row->rolesIds)) {
          unset($filtered_data[$fd_key]);
        }
      }
    }
    return $filtered_data;
  }
}

class activityMapperSectorAutocomplete extends activityMapperAutocompleteBase {
  public function getAutocompleteInfo($plugin, $data, $type) {
    $info = parent::getAutocompleteInfo($plugin, $data, $type);

    $sector_type_id = $this->getSectorTypeId($plugin);
    if (empty($sector_type_id)) {
      return NULL;
    }

    $info['sector_id'] = $sector_type_id;
    $info['endpoint'] = $plugin['report filter endpoint'] . '/' . $sector_type_id;

    return $info;
  }

  public function getSectorTypeId($plugin) {
    static $sectors_data;

    if (!isset($sectors_data)) {
      $sectors_data = ampapi_request('get', $plugin['report filter endpoint'], TRUE);
    }

    if (empty($sectors_data)) {
      return FALSE;
    }

    foreach ($sectors_data as $info) {
      if ($info->name == $plugin['filter sector type']) {
        return $info->id;
      }
    }
  }
}
