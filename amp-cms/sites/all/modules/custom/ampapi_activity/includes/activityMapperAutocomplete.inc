<?php

/**
 * @file
 * Shared classes and interfaces for the API mapper system.
 */

class activityMapperRoleAutocomplete extends activityMapperBase implements activityMapperAutocompleteInterface {
  public function autocomplete($plugin, $data, $type = 'autocomplete') {
    global $language;

    $matches = array();

    $role_id = $this->getRoleId($plugin);
    if (empty($role_id)) {
      return $matches;
    }

    $search_string = '';
    if (in_array($type, array('autocomplete', 'autocompleteids', 'tokeninput'))) {
      $search_string = reset($data);
      $search_string = transliteration_get($search_string, '', $language->language);
    }

    $count = 0;
    $request_data = ampapi_request('get', $plugin['report filter endpoint'], TRUE);
    foreach ($request_data as $entry) {
      if ($count >= 10) {
        break;
      }

      if (!in_array($role_id, $entry->rolesIds)) {
        continue;
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

          if (stripos($entry->acronym, $search_string) !== FALSE) {
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

      $name = $entry->name;
      if (!empty($entry->acronym)) {
        $name .= ' (' . $entry->acronym . ')';
      }

      $matches[] = array(
        'id' => intval($entry->id),
        'name' => filter_xss_admin($name),
      );

      $count++;
    }

    return $matches;
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

class activityMapperLocationAutocomplete extends activityMapperBase implements activityMapperAutocompleteInterface {
  public function autocomplete($plugin, $data, $type = 'autocomplete') {
    $matches = array();

    $search_string = '';
    if (in_array($type, array('autocomplete', 'autocompleteids', 'tokeninput'))) {
      $search_string = reset($data);
      $search_string = transliteration_get($search_string, '', $language->language);
    }

    $count = 0;
    $request_data = ampapi_request('get', $plugin['report filter endpoint'], TRUE);
    foreach ($request_data->children as $entry) {
      if ($count >= 10) {
        break;
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

      $matches[] = array(
        'id' => intval($entry->id),
        'name' => filter_xss_admin($entry->name),
      );

      $count++;
    }

    return $matches;
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

/**
 * Autocomplete helper.
 */
class activityMapperSectorAutocomplete extends activityMapperBase implements activityMapperAutocompleteInterface {
  public function autocomplete($plugin, $data, $type = 'autocomplete') {
    $matches = array();
    $sector_id = $this->getSectorId($plugin);
    if (empty($sector_id)) {
      return $matches;
    }

    $search_string = '';
    if (in_array($type, array('autocomplete', 'autocompleteids', 'tokeninput'))) {
      $search_string = reset($data);
      $search_string = transliteration_get($search_string, '', $language->language);
    }

    $request_data = ampapi_request('get', $plugin['report filter endpoint'] . '/' . $sector_id, TRUE);
    if (empty($request_data->children)) {
      return $matches;
    }

    $count = 0;
    foreach ($request_data->children as $entry) {
      if ($count >= 10) {
        break;
      }

      $prepend_entry = FALSE;
      // Match full codes and codes starting with the provided string.
      if (is_numeric($search_string) && $search_string == $entry->code) {
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

      if (!$prepend_entry) {
        $matches[] = array(
          'id' => intval($entry->id),
          'name' => filter_xss_admin($entry->name),
        );
      }
      else {
        $new_value = array(
          'id' => intval($entry->id),
          'name' => filter_xss_admin($entry->name),
        );
        $matches = array_shift($new_value, $matches);
      }

      $count++;
    }
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
