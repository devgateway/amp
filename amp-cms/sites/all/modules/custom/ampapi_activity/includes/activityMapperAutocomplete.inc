<?php

/**
 * @file
 * Shared classes and interfaces for the API mapper system.
 */

class activityMapperAutocompleteBase extends activityMapperBase {
  public function getMatchedValues($data, $type, $entries, &$matches) {
    global $language;

    $search_string = '';
    if (in_array($type, array('autocomplete', 'autocompleteids', 'tokeninput'))) {
      $search_string = reset($data);
      $search_string = transliteration_get($search_string, '', $language->language);
    }

    foreach ($entries as $entry) {
      if (count($matches) >= 10) {
        break;
      }

      // Match full codes and codes starting with the provided string.
      $prepend_entry = FALSE;
      if (isset($entry->code) && is_numeric($search_string) && $search_string == $entry->code) {
        $prepend_entry = TRUE;
      }

      switch ($type) {
        case 'tokeninput':
        case 'autocomplete':
          $skip = TRUE;

          $entry_name = transliteration_get($entry->name, '', $language->language);
          if (stripos($entry_name, $search_string) !== FALSE) {
            $skip = FALSE;
          }

          if (stripos($entry->name, $search_string) !== FALSE) {
            $skip = FALSE;
          }

          if (isset($entry->acronym) && stripos($entry->acronym, $search_string) !== FALSE) {
            $skip = FALSE;
          }

          if ($skip) {
            continue 2;
          }
          break;
        case 'autocompleteids':
          if ($entry->name != $search_string) {
            continue 2;
          }
          break;
        case 'tokeninputids':
          if (!in_array($entry->id, $data)) {
            continue 2;
          }
          break;

        default:
          continue 2;
          break;
      }

      // Include acronym in name, if any.
      $name = $entry->name;
      if (isset($entry->acronym)) {
        $name .= ' (' . $entry->acronym . ')';
      }

      // Include Location Administrative Level in name, if any.
      // if (isset($entry->level)) {
      //   $name = $entry->level . ' - ' . $name;
      // }

      if (!$prepend_entry) {
        $matches[] = array(
          'id' => intval($entry->id),
          'name' => filter_xss_admin($name),
        );
      }
      else {
        $new_value = array(
          'id' => intval($entry->id),
          'name' => filter_xss_admin($name),
        );
        $matches = array_shift($new_value, $matches);
      }
    }

    if (count($matches) >= 10) {
      return;
    }

    // Search sub-levels.
    foreach ($entries as $entry) {
      if (empty($entry->children)) {
        continue;
      }
      $this->getMatchedValues($data, $type, $entry->children, $matches);
    }
  }

  public function getAutocompleteValue($plugin, $data, $type) {
    $matches = $this->autocomplete($plugin, $data, $type);
    if (empty($matches)) {
      return NULL;
    }

    if ($type == 'autocompleteids') {
      $entry = reset($matches);
      if (isset($entry['id'])) {
        return array(intval($entry['id']));
      }
    }

    return $matches;
  }
}

class activityMapperRoleAutocomplete extends activityMapperAutocompleteBase implements activityMapperAutocompleteInterface {
  public function autocomplete($plugin, $data, $type = 'autocomplete') {
    $matches = array();

    $role_id = $this->getRoleId($plugin);
    if (empty($role_id)) {
      return $matches;
    }

    $request_data = ampapi_request('get', $plugin['report filter endpoint'], TRUE);
    $this->getMatchedValues($data, $type, $request_data, $matches);

    return $matches;
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
}

class activityMapperLocationAutocomplete extends activityMapperAutocompleteBase implements activityMapperAutocompleteInterface {
  public function autocomplete($plugin, $data, $type = 'autocomplete') {
    $matches = array();

    $request_data = ampapi_request('get', $plugin['report filter endpoint'], TRUE);
    $this->getMatchedValues($data, $type, array($request_data), $matches);

    return $matches;
  }
}

class activityMapperSectorAutocomplete extends activityMapperAutocompleteBase implements activityMapperAutocompleteInterface {
  public function autocomplete($plugin, $data, $type = 'autocomplete') {
    $matches = array();
    $sector_id = $this->getSectorId($plugin);
    if (empty($sector_id)) {
      return $matches;
    }

    $request_data = ampapi_request('get', $plugin['report filter endpoint'] . '/' . $sector_id, TRUE);
    $this->getMatchedValues($data, $type, array($request_data), $matches);

    return $matches;
  }

  // @TODO: Request ID from the API.
  public function getSectorId($plugin) {
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
