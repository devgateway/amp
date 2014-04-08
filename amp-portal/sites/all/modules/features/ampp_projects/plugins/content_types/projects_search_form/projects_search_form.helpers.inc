<?php

/**
 * @file
 * Contains temporary helper function for the projects_search_form plugin.
 *
 * @TODO: Cleanup and use more generic functions.
 */

/**
 * Helper function for projects_search_form_pre_render_autocomplete_fields().
 */
function ampp_get_sectors_from_url() {
  $sectors = array();

  $sectors_ids = isset($_GET['sectors']) ? $_GET['sectors'] : 'all';
  if ($sectors_ids != 'all' and !empty($sectors_ids)) {
    db_set_active(AMP_DATABASE);
    $items = array();

    global $language;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

    $query = db_select('amp_sector', 'sector');
    $query->fields('sector', array('amp_sector_id'));
    $query->addExpression($translate_field, 'name');
    $query->condition('sector.amp_sector_id', explode(',', $sectors_ids), 'IN');

    $result = $query->execute();
    foreach ($result as $record) {
      $sectors[] = array(
        'id' => $record->amp_sector_id,
        'name' => addslashes(check_plain($record->name))
      );
    }
    db_set_active();
  }

  return $sectors;
}

/**
 * Helper function for projects_search_form_pre_render_autocomplete_fields().
 */
function ampp_get_primarysectors_from_url() {
  $sectors = array();
  $sectors_ids = isset($_GET['sectors_primary']) ? $_GET['sectors_primary'] : 'all';
  if ($sectors_ids != 'all' and !empty($sectors_ids)) {
    db_set_active(AMP_DATABASE);
    $items = array();

    global $language;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

    $query = db_select('amp_sector', 'sector');
    $query->fields('sector', array('amp_sector_id'));
    $query->addExpression($translate_field, 'name');
    $query->condition('sector.amp_sector_id', explode(',', $sectors_ids), 'IN');

    $result = $query->execute();
    foreach ($result as $record) {
      $sectors[] = array(
        'id' => $record->amp_sector_id,
        'name' => addslashes(check_plain($record->name))
      );
    }
    db_set_active();
  }

  return $sectors;
}

/**
 * Helper function for projects_search_form_pre_render_autocomplete_fields().
 */
function ampp_get_secondarysectors_from_url() {
  $sectors = array();
  $sectors_ids = isset($_GET['sectors_secondary']) ? $_GET['sectors_secondary'] : 'all';
  if ($sectors_ids != 'all' and !empty($sectors_ids)) {
    db_set_active(AMP_DATABASE);
    $items = array();

    global $language;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

    $query = db_select('amp_sector', 'sector');
    $query->fields('sector', array('amp_sector_id'));
    $query->addExpression($translate_field, 'name');
    $query->condition('sector.amp_sector_id', explode(',', $sectors_ids), 'IN');

    $result = $query->execute();
    foreach ($result as $record) {
      $sectors[] = array(
        'id' => $record->amp_sector_id,
        'name' => addslashes(check_plain($record->name))
      );
    }
    db_set_active();
  }

  return $sectors;
}

/**
 * Helper function for projects_search_form_pre_render_autocomplete_fields().
 */
function ampp_get_programs_from_url() {
  $programs = array();
  $programs_ids = isset($_GET['programs']) ? $_GET['programs'] : 'all';
  if ($programs_ids != 'all' and !empty($programs_ids)) {
    db_set_active(AMP_DATABASE);
    $items = array();

    $query = db_select('amp_theme', 'program');
    $query->fields('program', array('amp_theme_id', 'name'));
    if (variable_get('amp_limit_programs_to_activities', 1)) {
      $query->join('amp_activity_program', 'activity', 'activity.amp_program_id = program.amp_theme_id');
    }
    $query->where('getprogramsettingid(program.amp_theme_id) = ' . variable_get('amp_programs_level', 2));
    $query->condition('program.amp_theme_id', explode(',', $programs_ids), 'IN');

    $result = $query->distinct()->execute()->fetchAll();
    foreach ($result as $record) {
      $programs[] = array(
        'id' => $record->amp_theme_id,
        'name' => addslashes(check_plain($record->name))
      );
    }
    db_set_active();
  }

  return $programs;
}

/**
 * Helper function for projects_search_form_pre_render_autocomplete_fields().
 */
function ampp_get_locations_from_url() {
  $locations = array();
  $locations_ids = isset($_GET['locations']) ? $_GET['locations'] : 'all';
  if ($locations_ids != 'all' and !empty($locations_ids)) {

    db_set_active(AMP_DATABASE);
    $items = array();

    global $language;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpCategoryValueLocations', 'name', location.id, 'amp_category_value_location', 'location_name', 'id', '" . $language->language . "')";

    $query = db_select('amp_category_value_location', 'location');
    $query->fields('location', array('id'));
    $query->addExpression($translate_field, 'location_name');
    $query->condition('location.id', explode(',', $locations_ids), 'IN');

    $result = $query->execute();
    foreach ($result as $record) {
      $parents = get_location_path($record->id);
      if (!empty($parents)) {
        $name = $parents . ' -> ' . $record->location_name;
      }
      else {
        $name = $record->location_name;
      }
      $locations[] = array('id' => $record->id, 'name' => check_plain($name));
    }
    db_set_active();
  }

  return $locations;
}

/**
 * Helper function for projects_search_form_pre_render_autocomplete_fields().
 */
function ampp_get_donors_from_url() {
  $donors = array();
  $donors_ids = isset($_GET['organisations_dn']) ? $_GET['organisations_dn'] : 'all';

  db_set_active(AMP_DATABASE);
  $items = array();
  if ($donors_ids != 'all' && !empty($donors_ids)) {

    global $language;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', donor.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

    $query = db_select('amp_organisation', 'donor');
    $query->distinct();
    $query->fields('donor', array('amp_org_id', 'acronym'));
    $query->addExpression($translate_field, 'name');
    if (variable_get('amp_show_only_donors', 0)) {
      $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = donor.amp_org_id');
      $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
    }
    $query->condition('donor.amp_org_id', explode(',', $donors_ids), 'IN');
    $or_statement = db_or()->condition('donor.deleted', 'true', '<>')
      ->condition('donor.deleted', NULL, 'is');
    $query->condition($or_statement);

    $result = $query->execute();
    foreach ($result as $record) {
      $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
      $donors[] = array('id' => $record->amp_org_id, 'name' => $name);
    }

  }

  db_set_active();

  return $donors;
}

/**
 * Helper function for projects_search_form_pre_render_autocomplete_fields().
 */
function ampp_get_agencies_from_url() {
  $agencies = array();
  $agencies_ids = isset($_GET['organisations_ba']) ? $_GET['organisations_ba'] : 'all';

  db_set_active(AMP_DATABASE);
  $items = array();
  if ($agencies_ids != 'all' && !empty($agencies_ids)) {

    global $language;
    $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', agency.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

    $query = db_select('amp_organisation', 'agency');
    $query->distinct();
    $query->fields('agency', array('amp_org_id', 'acronym'));
    $query->addExpression($translate_field, 'name');
    $query->condition('agency.amp_org_id', explode(',', $agencies_ids), 'IN');
    $or_statement = db_or()->condition('agency.deleted', 'true', '<>')
      ->condition('agency.deleted', NULL, 'is');
    $query->condition($or_statement);

    $result = $query->execute();
    foreach ($result as $record) {
      $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
      $agencies[] = array('id' => $record->amp_org_id, 'name' => $name);
    }

  }

  db_set_active();

  return $agencies;
}

/**
 * Autocomplete callback handler for sectors search
 */
function ampp_search_autocomplete_sectors() {
  $string = $_POST['string'];
  $items = array();
  db_set_active(AMP_DATABASE);

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

  $query = db_select('amp_sector', 'sector');
  $query->fields('sector', array('amp_sector_id'));
  $query->addExpression($translate_field, 'name');
  $query->condition('cc.name', 'Primary', '=');
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->addJoin('LEFT', 'amp_sector_scheme', 'ss', 'sector.amp_sec_scheme_id = ss.amp_sec_scheme_id');
  $query->addJoin('LEFT', 'amp_classification_config', 'cc', 'cc.classification_id = ss.amp_sec_scheme_id');

  $result = $query->execute();
  foreach ($result as $record) {
    $items[] = array(
      'id' => $record->amp_sector_id,
      'name' => check_plain($record->name)
    );
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for primarysectors search
 */
function ampp_search_autocomplete_sectors_primary() {
  $string = $_POST['string'];
  $items = array();
  db_set_active(AMP_DATABASE);

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

  $query = db_select('amp_sector', 'sector');
  $query->fields('sector', array('amp_sector_id'));
  $query->addExpression($translate_field, 'name');
  $query->condition('cc.name', 'Primary', '=');
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->addJoin('LEFT', 'amp_sector_scheme', 'ss', 'sector.amp_sec_scheme_id = ss.amp_sec_scheme_id');
  $query->addJoin('LEFT', 'amp_classification_config', 'cc', 'cc.classification_id = ss.amp_sec_scheme_id');

  $result = $query->execute();
  foreach ($result as $record) {
    $items[] = array(
      'id' => $record->amp_sector_id,
      'name' => check_plain($record->name)
    );
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for secondarysectors search
 */
function ampp_search_autocomplete_sectors_secondary() {
  $string = $_POST['string'];
  $items = array();
  db_set_active(AMP_DATABASE);

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpSector', 'name', sector.amp_sector_id, 'amp_sector', 'name', 'amp_sector_id', '" . $language->language . "')";

  $query = db_select('amp_sector', 'sector');
  $query->fields('sector', array('amp_sector_id'));
  $query->addExpression($translate_field, 'name');
  $query->condition('cc.name', 'Secondary', '=');
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->addJoin('LEFT', 'amp_sector_scheme', 'ss', 'sector.amp_sec_scheme_id = ss.amp_sec_scheme_id');
  $query->addJoin('LEFT', 'amp_classification_config', 'cc', 'cc.classification_id = ss.amp_sec_scheme_id');

  $result = $query->execute();
  foreach ($result as $record) {
    $items[] = array(
      'id' => $record->amp_sector_id,
      'name' => check_plain($record->name)
    );
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for programs search
 */
function ampp_search_autocomplete_programs() {
  $string = $_POST['string'];
  $items = array();
  db_set_active(AMP_DATABASE);

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpTheme', 'name', program.amp_theme_id, 'amp_theme', 'name', 'amp_theme_id', '" . $language->language . "')";

  $query = db_select('amp_theme', 'program');
  $query->fields('program', array('amp_theme_id'));
  $query->addExpression($translate_field, 'name');

  $root_pid = variable_get('amp_programs_root', 0);
  if ($root_pid) {
    // we need to get all parent IDs to limit the search to those
    $rootline = hs_select_programs_rootline($root_pid);
    if (!empty($rootline)) {
      $query->condition('program.parent_theme_id', $rootline, 'IN');
    }
  }
  else {
    $query->where('getprogramsettingid(program.amp_theme_id) = ' . variable_get('amp_programs_level', 2));
  }
  if (variable_get('amp_limit_programs_to_activities', 1)) {
    $query->join('amp_activity_program', 'activity', 'activity.amp_program_id = program.amp_theme_id');
  }
  $query->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->orderBy($translate_field, 'ASC');

  $result = $query->distinct()->execute()->fetchAll();
  foreach ($result as $record) {
    $items[] = array(
      'id' => $record->amp_theme_id,
      'name' => check_plain($record->name)
    );
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for donors search
 */
function ampp_search_autocomplete_organisations_dn() {
  $string = $_POST['string'];
  $items = array();
  db_set_active(AMP_DATABASE);

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', orgs.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  $query = db_select('amp_organisation', 'orgs');
  $query->fields('orgs', array('amp_org_id', 'acronym'));
  $query->addExpression($translate_field, 'name');

  $or_statement = db_or()
    ->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')')
    ->where('unaccent(orgs.acronym) ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->condition($or_statement);

  if (variable_get('amp_show_only_donors', 0)) {
    $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = orgs.amp_org_id');
    $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
  }

  $or_statement = db_or()
    ->condition('orgs.deleted', 'true', '<>')
    ->condition('orgs.deleted', NULL, 'is');
  $query->condition($or_statement);

  $query->groupBy('orgs.amp_org_id');
  $query->groupBy($translate_field);
  $query->groupBy('orgs.acronym');

  $result = $query->execute();

  foreach ($result as $record) {
    $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
    $items[] = array('id' => $record->amp_org_id, 'name' => $name);
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for agencies search
 */
function ampp_search_autocomplete_organisations_ba() {
  $string = $_POST['string'];
  $items = array();
  db_set_active(AMP_DATABASE);

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', agency.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  // Get the role ID for the "Beneficiary Agency" role.
  // SELECT amp_role_id FROM amp_role WHERE role_code = 'BA';
  $ba_role_id = db_select('amp_role', 'ar')
    ->condition('ar.role_code', 'BA', '=')
    ->fields('ar', array('amp_role_id'))
    ->execute()
    ->fetchField();

  // Get the list of organizations with the "Beneficiary Agency" role.
  // SELECT DISTINCT amp_org_id, name,role FROM amp_organisation orgs LEFT JOIN amp_org_role aor ON aor.organisation = orgs.amp_org_id WHERE aor.role = 5;
  $query = db_select('amp_organisation', 'agency');
  $query->leftJoin('amp_org_role', 'aor', 'aor.organisation = agency.amp_org_id');
  $query->distinct();
  $query->fields('agency', array('amp_org_id', 'acronym'));
  $query->fields('aor', array('role'));
  $query->addExpression($translate_field, 'name');
  $query->condition('aor.role', $ba_role_id, '=');

  // Search for the provided string.
  $query_or = db_or();
  $query_or->where('unaccent(' . $translate_field . ') ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query_or->where('unaccent(agency.acronym) ILIKE unaccent(\'%' . db_like($string) . '%\')');
  $query->condition($query_or);

  $result = $query->execute();

  foreach ($result as $record) {
    $name = !empty($record->acronym) ? check_plain($record->name) . ' (' . check_plain($record->acronym) . ')' : check_plain($record->name);
    $items[] = array('id' => $record->amp_org_id, 'name' => $name);
  }
  db_set_active();

  return $items;
}

/**
 * Autocomplete callback handler for region search
 */
function ampp_search_autocomplete_locations() {
  $string = $_POST['string'];
  db_set_active(AMP_DATABASE);

  // We retrieve the country setting for the AMP installation
  $query = db_select('amp_global_settings', 'setting');
  $query->fields('setting', array('settingsvalue'));
  $query->condition('setting.settingsname', 'Default Country', '=');
  $defaultCountry = $query->execute()->fetchCol();
  if (!empty($defaultCountry)) {
    $defaultCountry = current($defaultCountry);
  }

  $items = array();

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpCategoryValueLocations', 'name', loc.id, 'amp_category_value_location', 'location_name', 'id', '" . $language->language . "')";

  if (empty($defaultCountry)) {
    $query = "SELECT id, " . $translate_field . " as location_name FROM {amp_category_value_location} as loc WHERE unaccent(" . $translate_field . ") ILIKE unaccent(:string)";
    $result = db_query($query, array(':string' => '%' . $string . '%'));
  }
  else {
    // Include only subnational locations if the AMP installation has a Default Country set
    $query = "SELECT id, " . $translate_field . " as location_name FROM {amp_category_value_location} as loc WHERE unaccent(" . $translate_field . ") ILIKE unaccent(:string) AND parent_location IS NOT NULL";
    $result = db_query($query, array(':string' => '%' . $string . '%'));
  }

  foreach ($result as $record) {
    $parents = get_location_path($record->id);
    if (!empty($parents)) {
      $name = $parents . ' -> ' . $record->location_name;
    }
    else {
      $name = $record->location_name;
    }
    $items[] = array('id' => $record->id, 'name' => check_plain($name));
  }
  db_set_active();

  return $items;
}

/**
 * Helper function for ampp_search_modalform_callback().
 */
function ampp_search_ajax_sectors_form() {
  $form = array();
  $form['sectors'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
      'module' => 'hs_select_sectors',
      'params' => array(
        'id' => 0,
        'pid' => 0,
        'separator' => '__',
      ),
      'save_lineage' => 1,
      'enforce_deepest' => 0,
      'entity_count' => 0,
      'require_entity' => 0,
      'level_labels' => array('status' => 0),
      'dropbox' => array('status' => 1, 'limit' => 0),
      'editability' => array('status' => 1, 'max_levels' => 2),
      'resizable' => 1,
      'render_flat_select' => 0
    ),
  );

  return $form;
}

/**
 * Helper function for ampp_search_modalform_callback().
 */
function ampp_search_ajax_primarysectors_form() {
  $form = array();
  $form['sectors_primary'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
      'module' => 'hs_select_primarysectors',
      'params' => array(
        'id' => 0,
        'pid' => 0,
        'separator' => '__',
      ),
      'save_lineage' => 1,
      'enforce_deepest' => 0,
      'entity_count' => 0,
      'require_entity' => 0,
      'level_labels' => array('status' => 0),
      'dropbox' => array('status' => 1, 'limit' => 0),
      'editability' => array('status' => 1, 'max_levels' => 2),
      'resizable' => 1,
      'render_flat_select' => 0
    ),
  );

  return $form;
}

/**
 * Helper function for ampp_search_modalform_callback().
 */
function ampp_search_ajax_secondarysectors_form() {
  $form = array();
  $form['sectors_secondary'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
      'module' => 'hs_select_secondarysectors',
      'params' => array(
        'id' => 0,
        'pid' => 0,
        'separator' => '__',
      ),
      'save_lineage' => 1,
      'enforce_deepest' => 0,
      'entity_count' => 0,
      'require_entity' => 0,
      'level_labels' => array('status' => 0),
      'dropbox' => array('status' => 1, 'limit' => 0),
      'editability' => array('status' => 1, 'max_levels' => 2),
      'resizable' => 1,
      'render_flat_select' => 0
    ),
  );

  return $form;
}

/**
 * Helper function for ampp_search_modalform_callback().
 */
function ampp_search_ajax_programs_form() {
  $form = array();
  $form['programs'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
      'module' => 'hs_select_programs',
      'params' => array(
        'id' => 0,
        'pid' => variable_get('amp_programs_root', 0),
        'separator' => '__',
      ),
      'save_lineage' => 1,
      'enforce_deepest' => 0,
      'entity_count' => 0,
      'require_entity' => 0,
      'level_labels' => array('status' => 0),
      'dropbox' => array('status' => 1, 'limit' => 0),
      'editability' => array('status' => 1, 'max_levels' => 2),
      'resizable' => 1,
      'render_flat_select' => 0
    ),
  );

  return $form;
}

/**
 * Helper function for ampp_search_modalform_callback().
 */
function ampp_search_ajax_donors_form() {
  $form = array();
  $hierarchy = array();
  $hierarchy = ampp_projects_get_donors();

  $form['organisations_dn'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 10,
    '#config' => array(
      'module' => 'hs_smallhierarchy',
      'params' => array(
        'hierarchy' => $hierarchy,
        'separator' => '_',
      ),
      'save_lineage' => 1,
      'enforce_deepest' => 0,
      'entity_count' => 0,
      'require_entity' => 0,
      'level_labels' => array('status' => 0),
      'dropbox' => array('status' => 1, 'limit' => 0),
      'editability' => array('status' => 1, 'max_levels' => 1),
      'resizable' => 0,
      'render_flat_select' => 0
    ),
  );

  return $form;
}

/**
 * Helper function for ampp_search_modalform_callback().
 */
function ampp_search_ajax_locations_form() {
  $form = array();
  $form['locations'] = array(
    '#type' => 'hierarchical_select',
    '#size' => 5,
    '#config' => array(
      'module' => 'hs_select_locations',
      'params' => array(
        'id' => 0,
        'pid' => 0,
        'separator' => '__',
      ),
      'save_lineage' => 1,
      'enforce_deepest' => 0,
      'entity_count' => 0,
      'require_entity' => 0,
      'level_labels' => array('status' => 0),
      'dropbox' => array('status' => 1, 'limit' => 0),
      'editability' => array('status' => 1, 'max_levels' => 2),
      'resizable' => 1,
      'render_flat_select' => 0
    ),
  );

  return $form;
}

/**
 *
 */
function get_location_path($childLocationId, $depth = 999) {
  // we need to show the full hierarchy but not country
  global $language;

  $allParentIds = array();

  db_set_active(AMP_DATABASE);

  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpCategoryValueLocations', 'parent_name', names.id, 'amp_category_value_location', 'location_name', 'id', '" . $language->language . "')";

  $query = db_select('amp_category_value_location', 'loc');
  $query->fields('loc', array('parent_location', 'parent_category_value'));
  $query->addExpression($translate_field, 'parent_name');
  $query->join('amp_category_value_location', 'names', 'names.id = loc.parent_location');
  $query->join('amp_category_value', 'cats', 'cats.id = names.parent_category_value');
  $query->condition('loc.id', $childLocationId, '=');
  $query->condition('cats.category_value', 'Country', '!=');
  $query->isNotNull('loc.parent_location');

  $result = $query->execute();
  $parents = '';
  foreach ($result as $record) {
    if ($depth != 0) {
      $parents = get_location_path($record->parent_location, $depth - 1) . '->' . $record->parent_name;
    }
    $parents = $record->parent_name;
  }

  db_set_active();

  return $parents;
}

/**
 * Helper function for ampp_search_get_modalform_values().
 *
 * @TODO: Remove!!!
 */
function ampp_projects_get_donor($id) {
  db_set_active('amp_projects');

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', orgs.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  $query = db_select('amp_organisation', 'orgs')
    ->fields('orgs', array('amp_org_id', 'acronym'));
  $query->addExpression($translate_field, 'name');
  if (variable_get('amp_show_only_donors', 0)) {
    $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = orgs.amp_org_id');
    $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
  }
  $query->condition('orgs.amp_org_id', intval($id), '=');
  $or_statement = db_or()
    ->condition('orgs.deleted', 'true', '<>')
    ->condition('orgs.deleted', NULL, 'is');
  $query->condition($or_statement);

  $result = $query->execute();
  $info = array();
  foreach ($result as $record) {
    $info[] = array('id' => $record->amp_org_id, 'name' => $record->name);
  }
  db_set_active();

  return $info;
}

/**
 * Helper function for ampp_search_ajax_donors_form().
 *
 * @TODO: Remove!!!
 */
function ampp_projects_get_donors() {
  db_set_active('amp_projects');

  global $language;
  $translate_field = "translate_field('org.digijava.module.aim.dbentity.AmpOrganisation', 'name', orgs.amp_org_id, 'amp_organisation', 'name', 'amp_org_id', '" . $language->language . "')";

  $query = db_select('amp_organisation', 'orgs');
  $query->fields('orgs', array('amp_org_id', 'acronym'));
  $query->addExpression($translate_field, 'name');
  if (variable_get('amp_show_only_donors', 0)) {
    $query->innerJoin('amp_funding', 'f', 'f.amp_donor_org_id = orgs.amp_org_id');
    $query->innerJoin('amp_activity', 'a', 'a.amp_activity_id = f.amp_activity_id');
  }
  $or_statement = db_or()->condition('orgs.deleted', 'true', '<>')
    ->condition('orgs.deleted', NULL, 'is');
  $query->condition($or_statement);
  $query->orderBy('name', 'ASC');

  $result = $query->execute();

  $hierarchy = array();
  foreach ($result as $record) {
    $hierarchy[$record->amp_org_id] = array('label' => $record->name);
  }

  db_set_active();

  return $hierarchy;
}
