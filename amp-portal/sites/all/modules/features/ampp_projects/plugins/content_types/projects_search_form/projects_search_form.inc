<?php

/**
 * @file
 * Projects search form
 *
 */
$plugin = array(
    'single' => TRUE,
    'title' => t('Projects Search Form'),
    'description' => t('Projects Search Form'),
    'category' => array(t('AMPP Widgets'), -9),
    'defaults' => array(
      'search_path' => 'search',
      'budget_flag' => 0,
      'country_level_projects_flag' => 0,
      'sectors_flag' => 1,
      'primary_sectors_flag' => 0,
      'secondary_sectors_flag' => 0,
      'programs_flag' => 0,
      'donors_flag' => 1,
      'agencies_flag' => 1,
      'locations_flag' => 1,
      'period_flag' => 0,
      'keywords_flag' => 1,
      'sectors_popup' => 0,
      'primary_sectors_popup' => 0,
      'secondary_sectors_popup' => 0,
      'programs_popup' => 0,
      'donors_popup' => 0,
      'agencies_popup' => 0,
      'locations_popup' => 0,
      'sectors_label' => 'Sectors',
      'primary_sectors_label' => 'Primary Sectors',
      'secondary_sectors_label' => 'Secondary Sectors',
      'programs_label' => 'Programs',
      'donors_label' => 'Donors',
      'agencies_label' => 'Agency',
      'locations_label' => 'Locations',
      'keywords_label' => 'Keywords',
      'preview_amount' => 0,
    ),
    'render callback' => 'ampp_projects_projects_search_form_render'
);

/**
 * Run-time rendering of the body of the block.
 *
 * @param $subtype
 * @param $conf
 *   Configuration as done at admin time.
 * @param $args
 * @param $context
 *   Context - in this case we don't have any.
 *
 * @return
 *   An object with at least title and content members.
 */
function ampp_projects_projects_search_form_render($subtype, $conf, $args, $context) {
    module_load_include('inc', 'node', 'node.pages');

  // Merge in plugin defaults, prevents not defined warnings on existing panels.
  $plugin_definition = ctools_get_plugins('ctools', 'content_types', $subtype);
  $conf += $plugin_definition['defaults'];
  
  $block = new stdClass();
  $block->title = t('Projects search');
  $block->content = drupal_get_form('ampp_projects_projects_search_form_form', $conf);
  return $block;
}


/**
 *
 */
function project_search_form_uidatepicker($form, $form_state) {
  drupal_add_library('system', 'ui.datepicker');
  drupal_add_js("(function ($) { $('.datepicker').datepicker(); })(jQuery);", array('type' => 'inline', 'scope' => 'footer', 'weight' => 5));
  return $form;
}

/**
 * Returns an edit form for the projects search
 */
function ampp_projects_projects_search_form_form($form, &$form_state, $conf) {

  global $language;

  $theme_path = path_to_theme();
  $img_vars = array('path' => $theme_path.'/images/list.png');
  $icon = theme('image',$img_vars);

  $form_state['conf'] = $conf;

  unset($form['#attributes']['id']);
  $form['#attributes']['id'][] = 'ampp-search-form';

  if ($conf["sectors_flag"] == 1) {
    $form['sector'] = array(
      '#type' => 'textfield',
      '#title' => (isset($conf['sectors_label'])) ? t($conf['sectors_label']) : t('Sectors'),
      '#size' => 30,
      '#maxlength' => 64,
      '#description' => t('Start typing sector name then choose from list. Multiple entries possible. Logical OR is used between multiple values.')
    );
    if (!isset($conf['sectors_popup']) || $conf['sectors_popup'] == 0) $form['sector']['#suffix'] = '<span class="modal-list-more"> ' . l($icon, 'ampp_search/ajax-sectores', array('html' => TRUE, 'query' => array('format' => 'simple'), 'attributes'=> array('class' => 'modal-selection', 'rel' => 'edit-sector', 'title' => t($conf['sectors_label'] . ' selection')))) . '</span>';
  }

  if ($conf["primary_sectors_flag"] == 1) {
    $form['primary_sector'] = array(
      '#type' => 'textfield',
      '#title' => (isset($conf['primary_sectors_label'])) ? t($conf['primary_sectors_label']) : t('Primary Sectors'),
      '#size' => 30,
      '#maxlength' => 64,
      '#description' => t('Start typing primary sector name then choose from list. Multiple entries possible. Logical OR is used between multiple values.')
   );
     if (!isset($conf['primary_sectors_popup']) || $conf['primary_sectors_popup'] == 0) $form['primary_sector']['#suffix'] = '<span class="modal-list-more"> ' . l($icon, 'ampp_search/ajax-primarysectors', array('html' => TRUE, 'query' => array('format' => 'simple'), 'attributes'=> array('class' => 'modal-selection', 'rel' => 'edit-primary-sector', 'title' => t($conf['primary_sectors_label'] . ' selection')))) . '</span>';
  }

  if ($conf["secondary_sectors_flag"] == 1) {
    $form['secondary_sector'] = array(
      '#type' => 'textfield',
      '#title' => (isset($conf['secondary_sectors_label'])) ? t($conf['secondary_sectors_label']) : t('Primary Sectors'),
      '#size' => 30,
      '#maxlength' => 64,
      '#description' => t('Start typing secondary sector name then choose from list. Multiple entries possible. Logical OR is used between multiple values.')
   );
     if (!isset($conf['secondary_sectors_popup']) || $conf['secondary_sectors_popup'] == 0) $form['secondary_sector']['#suffix'] = '<span class="modal-list-more"> ' . l($icon, 'ampp_search/ajax-secondarysectors', array('html' => TRUE, 'query' => array('format' => 'simple'), 'attributes'=> array('class' => 'modal-selection', 'rel' => 'edit-secondary-sector', 'title' => t($conf['secondary_sectors_label'] . ' selection')))) . '</span>';
  }

  if ($conf["programs_flag"] == 1) {
    $form['program'] = array(
      '#type' => 'textfield',
      '#title' => (isset($conf['programs_label'])) ? t($conf['programs_label']) : t('Programs'),
      '#size' => 30,
      '#maxlength' => 64,
      '#description' => t('Start typing program name then choose from list. Multiple entries possible. Logical OR is used between multiple values.')
    );
    if (!isset($conf['programs_popup']) || $conf['programs_popup'] == 0) $form['program']['#suffix'] = '<span class="modal-list-more"> ' . l($icon, 'ampp_search/ajax-programs', array('html' => TRUE, 'query' => array('format' => 'simple'), 'attributes'=> array('class' => 'modal-selection', 'rel' => 'edit-program', 'title' => t($conf['programs_label'] . ' selection')))) . '</span>';
  }

  if ($conf["donors_flag"] == 1) {
    $form['donor'] = array(
      '#type' => 'textfield',
      '#title' => (isset($conf['donors_label'])) ? t($conf['donors_label']) : t('Donors'),
      '#size' => 30,
      '#maxlength' => 64,
      '#description' => t('Start typing donor name then choose from list. Multiple entries possible. Logical OR is used between multiple values.')
   );
     if (!isset($conf['donors_popup']) || $conf['donors_popup'] == 0) $form['donors_flag']['#suffix'] = '<span class="modal-list-more"> ' . l($icon, 'ampp_search/ajax-donors', array('html' => TRUE, 'query' => array('format' => 'simple'), 'attributes'=> array('class' => 'modal-selection', 'rel' => 'edit-donor', 'title' => t($conf['donors_label'] . ' selection')))) . '</span>';
  }

  if ($conf["agencies_flag"] == 1) {
    $form['agency'] = array(
        '#type' => 'textfield',
        '#title' => (isset($conf['agencies_label'])) ? t($conf['agencies_label']) : t('Agencies'),
        '#size' => 30,
        '#maxlength' => 64,
        '#description' => t('Start typing agency name then choose from list. Multiple entries possible. Logical OR is used between multiple values.')
    );
    if (!isset($conf['agencies_popup']) || $conf['agencies_popup'] == 0) $form['agencies_flag']['#suffix'] = '<span class="modal-list-more"> ' . l($icon, 'ampp_search/ajax-agencies', array('html' => TRUE, 'query' => array('format' => 'simple'), 'attributes'=> array('class' => 'modal-selection', 'rel' => 'edit-agency', 'title' => t($conf['agencies_label'] . ' selection')))) . '</span>';
  }

  if ($conf["locations_flag"] == 1) {
    $form['region'] = array(
      '#type' => 'textfield',
      '#title' => (isset($conf['locations_label'])) ? t($conf['locations_label']) : t('Locations'),
      '#size' => 30,
      '#maxlength' => 64,
      '#description' => t('Start typing region then choose from list. Multiple entries possible. Logical OR is used between multiple values.')
    );
    if (!isset($conf['locations_popup']) || $conf['locations_popup'] == 0) $form['locations_flag']['#suffix'] = '<span class="modal-list-more"> ' . l($icon, 'ampp_search/ajax-regions', array('html' => TRUE, 'query' => array('format' => 'simple'), 'attributes'=> array('class' => 'modal-selection', 'rel' => 'edit-region', 'title' => t($conf['locations_label'] . ' selection')))) . '</span>';
  }

  if ($conf["period_flag"] == 1) {
    $lang_arr = locale_get_localized_date_format(array($language->language));
    $years_range_arr = activities_period_range();
    $years_past = (date('Y') > $years_range_arr[0]) ? date('Y') - $years_range_arr[0] : 0;
    $years_future = ($years_range_arr[1] > date('Y')) ? $years_range_arr[1] - date('Y') : 0;

    $form['period'] = array(
      '#type' => 'item',
      '#title' => t('Period from'),
      'period_start' => array(
        '#type' => 'date_popup',
        '#title' => '',
        '#default_value' => '',
        '#date_type' => DATE_UNIX,
        '#date_timezone' => date_default_timezone(),
        '#date_format' => $lang_arr['date_format_ampp_cal'],
        '#date_year_range' => '-' . $years_past . ':+' . $years_future,
        '#description' => t('Records with no value are ignored.'),
      ),
      'period_end' => array(
        '#type' => 'date_popup',
        '#title' => '',
        '#default_value' => '',
        '#date_type' => DATE_UNIX,
        '#date_timezone' => date_default_timezone(),
        '#date_format' => $lang_arr['date_format_ampp_cal'],
        '#date_year_range' => '-' . $years_past . ':+' . $years_future,
      ),
    );
  }

  if ($conf["keywords_flag"] == 1) {
    $form['keyword'] = array(
      '#type' => 'textfield',
      '#title' => (isset($conf['keywords_label'])) ? t($conf['keywords_label']) : t('Keywords'),
      '#size' => 30,
      '#description' => t('Search in project titles'),
    );
  }

  if ($conf["budget_flag"] == 1) {
    $form['budget'] = array(
      '#type' => 'radios',
      '#title' => t('Show on-budget projects'),
      '#options' => array('all' => t('Show both on and off budget projects'), OnOffBudgetIds::$onBudgetProjectsId => t('On-budget only'), OnOffBudgetIds::$offBudgetProjectsId => t('Off-budget only')),
      '#default_value' => 'all',
    );
  }

  if ($conf["country_level_projects_flag"] == 1) {
    $form['country_level_projects'] = array(
      '#type' => 'checkbox',
      '#title' => t('Include higher geographical levels'),
      //'#value' => 1,
    );
  }

  if ($conf["preview_amount"] == 1) {
    $form['preview_amount'] = array(
      '#type' => 'hidden',
      '#value' => $conf["preview_amount"],
      '#attributes' => array('id' => array('preview_amount')),
    );
  }

  global $amp_db_exists;

  if ($amp_db_exists) {
    $form['submit'] = array('#type' => 'submit', '#value' => t('Show'));
    $form['results_count'] = array('#markup' => t('projects to show is') . '&nbsp;' . '<div id="search_result_count">' . ampp_search_result_count(false, isset($conf['preview_amount']) ? $conf['preview_amount'] : 0) . '</div><span id="loading-count">' . t('loading') . '...</span>');
  } else {
    $form['results_count'] = array('#markup' => '<div class="messages error"><ul><li>' . t('AMP database is not configured properly.') . '</li></ul><span><img id="close" src="/sites/all/themes/ampp/images/cancel_icon.gif"></span></div>');
  }

  // Fill search form textfields.
  // Form should be filled only on search result page
  $js_string  = '';
  $arr = ampp_get_sectors_from_url();
  if (is_array($arr)) $js_string .= 'var selectedSectors = new Array('.implode(",", $arr).');' . "\n";
  $arr = ampp_get_primarysectors_from_url();
  if (is_array($arr)) $js_string .= 'var selectedPrimarySectors = new Array('.implode(",", $arr).');' . "\n";
  $arr = ampp_get_secondarysectors_from_url();
  if (is_array($arr)) $js_string .= 'var selectedSecondarySectors = new Array('.implode(",", $arr).');' . "\n";
  $arr = ampp_get_programs_from_url();
  if (is_array($arr)) $js_string .= 'var selectedPrograms = new Array('.implode(",", $arr).');' . "\n";
  $arr = ampp_get_donors_from_url();
  if (is_array($arr)) $js_string .= 'var selectedDonors = new Array('.implode(",", $arr).');' . "\n";
  $arr = ampp_get_agencies_from_url();
  if (is_array($arr)) $js_string .= 'var selectedAgencies = new Array('.implode(",", $arr).');' . "\n";
  $arr = ampp_get_locations_from_url();
  if (is_array($arr)) $js_string .= 'var selectedLocations = new Array('.implode(",", $arr).');' . "\n";
  $arr = ampp_get_period_from_url();
  if (is_array($arr)) {
    if (trim($arr[0]) != 'all')
      $js_string .= "var selectedPeriodStart = '" . format_date(strtotime($arr[0]), 'ampp_cal') . "'\n";
    if (trim($arr[1]) != 'all')
      $js_string .= "var selectedPeriodEnd = '" . format_date(strtotime($arr[1]), 'ampp_cal') . "'\n";
  }

  $arr = ampp_get_keywords_from_url();
  if (is_array($arr)) {
    $js_string .= 'var selectedKeywords = new Array("'. implode(",", $arr) .'");' . "\n";
  }

  if ($conf["budget_flag"] == 1) {
    $arr = ampp_get_onoffbudget_from_url();
    if(is_array($arr)) {
      $i = 0;
      while ($i < count($arr) ) {
    switch($arr[$i]) {
      case OnOffBudgetIds::$offBudgetProjectsId:
      $js_string .= "var selectedBudget = ".OnOffBudgetIds::$offBudgetProjectsId."; \n";
      break;
      case OnOffBudgetIds::$onBudgetProjectsId:
      $js_string .= "var selectedBudget = ".OnOffBudgetIds::$onBudgetProjectsId."; \n";
      break;
    }
    $i++;
      }
    } else {
      $js_string .= "var selectedOffBudget = 'all'; \n";
    }
  }
  if ($conf["country_level_projects_flag"] == 1) {
    $regionalprojects = ampp_get_regionalprojects_from_url();
    if($regionalprojects == 1) {
      $js_string .= "var selectedRegionalProjects = 1; \n";
    }
  }

  drupal_add_js($js_string,array('type' => 'inline', 'scope' => 'footer'));

  return $form;
}


/*
 *
 */
function ampp_projects_projects_search_form_form_submit($form, &$form_state) {
  $conf = $form_state['conf'];
  $submit_path = $conf['search_path'];

  //  print_r($form_state['values']);
  //  exit;

  $queryStrArr = Array();

  $sectors = empty($form_state['values']['sector']) ? 'all' : $form_state['values']['sector'];
  $primarysectors = empty($form_state['values']['primary_sector']) ? 'all' : $form_state['values']['primary_sector'];
  $secondarysectors = empty($form_state['values']['secondary_sector']) ? 'all' : $form_state['values']['secondary_sector'];
  $programs = empty($form_state['values']['program']) ? 'all' : $form_state['values']['program'];
  $donors =  empty($form_state['values']['donor']) ? 'all' : $form_state['values']['donor'];
  $agencies =  empty($form_state['values']['agency']) ? 'all' : $form_state['values']['agency'];
  $regions = empty($form_state['values']['region']) ? 'all' : $form_state['values']['region'];
  $period_start = empty($form_state['values']['period_start']) ? 'all' : $form_state['values']['period_start'];
  $period_end = empty($form_state['values']['period_end']) ? 'all' : $form_state['values']['period_end'];
  $keyword = empty($form_state['values']['keyword']) ? 'all' : $form_state['values']['keyword'];

  $budgetfilter = "";
  $budgetfilterArr = Array();
  if(!empty($form_state['values']['budget'])) {
    array_push($budgetfilterArr,$form_state['values']['budget']);
  }

  $budgetfilter = implode( ",", $budgetfilterArr );

  if(trim($budgetfilter) == "")  {
      $budgetfilter = "all";
  }

  $countrylevelprojectsfilter = (!empty($form_state['values']['country_level_projects'])) ? $form_state['values']['country_level_projects'] : "all" ;

  if($conf['donors_flag'] == 1) {
    $queryStrArr['donors'] = $donors;
  }
  if($conf['agencies_flag'] == 1) {
    $queryStrArr['agencies'] = $agencies;
  }
  if($conf['sectors_flag'] == 1) {
    $queryStrArr['sectors'] = $sectors;
  }
  if($conf['primary_sectors_flag'] == 1) {
    $queryStrArr['primarysectors'] = $primarysectors;
  }
  if($conf['secondary_sectors_flag'] == 1) {
    $queryStrArr['secondarysectors'] = $secondarysectors;
  }
  if($conf['programs_flag'] == 1) {
    $queryStrArr['programs'] = $programs;
  }
  if($conf['locations_flag'] == 1) {
    $queryStrArr['locations'] = $regions;
  }
  if($conf['period_flag'] == 1) {
    $queryStrArr['period_start'] = $period_start;
    $queryStrArr['period_end'] = $period_end;
  }
  if($conf['keywords_flag'] == 1) {
    $queryStrArr['keywords'] = $keyword;
  }
  if($conf['budget_flag'] == 1) {
    $queryStrArr['budget'] = $budgetfilter;
  }
  if($conf['country_level_projects_flag'] == 1) {
    $queryStrArr['showregionalprojects'] = $countrylevelprojectsfilter;
  }

  drupal_goto($submit_path, array('query' => $queryStrArr));
}


/*
 *
 */
function ampp_projects_projects_search_form_content_type_edit_form($form, &$form_state) {
  $conf = $form_state['conf'];

  $form['note_at_least_one'] = array(
        '#type' => 'item',
        '#markup' => '<div><b>At least one control should be selected.</b></div>',
        '#weight' => 0,
  );

  // hide($form['override_title']);
  // hide($form['override_title_text']);
  // hide($form['override_title_markup']);

  $form['search_path'] = array(
    '#type' => 'textfield',
    '#title' => t('Search result path'),
    '#size' => 60,
    '#description' => t('Search result path'),
    '#default_value' => isset($conf['search_path']) ? $conf['search_path'] : $form_state['plugin']['defaults']['search_path'],
    '#required' => TRUE,
  );

  $form['sectors_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Sectors search field'),
    '#description' => t('Show sectors search field'),
    '#default_value' => isset($conf['sectors_flag']) ? intval($conf['sectors_flag']) : $form_state['plugin']['defaults']['sectors_flag'],
    '#id' => 'sectors_flag',
  );
  $form['sectors_popup'] = array(
    '#type' => 'checkbox',
    '#title' => t('Disable Sectors popup select'),
    '#default_value' => isset($conf['sectors_popup']) ? intval($conf['sectors_popup']) : $form_state['plugin']['defaults']['sectors_popup'],
    '#dependency' => array('sectors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );
  $form['sectors_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Sectors label'),
    '#default_value' => isset($conf['sectors_label']) ? t($conf['sectors_label']) : t($form_state['plugin']['defaults']['sectors_label']),
    '#dependency' => array('sectors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );

  $form['primary_sectors_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Primary Sectors search field'),
    '#description' => t('Show primary sectors search field'),
    '#default_value' => isset($conf['primary_sectors_flag']) ? intval($conf['primary_sectors_flag']) : $form_state['plugin']['defaults']['primary_sectors_flag'],
    '#id' => 'primary_sectors_flag',
  );
  $form['primary_sectors_popup'] = array(
    '#type' => 'checkbox',
    '#title' => t('Disable Primary Sectors popup select'),
    '#default_value' => isset($conf['primary_sectors_popup']) ? intval($conf['primary_sectors_popup']) : $form_state['plugin']['defaults']['primary_sectors_popup'],
    '#dependency' => array('primary_sectors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );
  $form['primary_sectors_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Primary Sectors label'),
    '#default_value' => isset($conf['primary_sectors_label']) ? t($conf['primary_sectors_label']) : t($form_state['plugin']['defaults']['primary_sectors_label']),
    '#dependency' => array('primary_sectors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );

  $form['secondary_sectors_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Secondary Sectors search field'),
    '#description' => t('Show secondary sectors search field'),
    '#default_value' => isset($conf['secondary_sectors_flag']) ? intval($conf['secondary_sectors_flag']) : $form_state['plugin']['defaults']['secondary_sectors_flag'],
    '#id' => 'secondary_sectors_flag',
  );
  $form['secondary_sectors_popup'] = array(
    '#type' => 'checkbox',
    '#title' => t('Disable Secondary Sectors popup select'),
    '#default_value' => isset($conf['secondary_sectors_popup']) ? intval($conf['secondary_sectors_popup']) : $form_state['plugin']['defaults']['secondary_sectors_popup'],
    '#dependency' => array('secondary_sectors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );
  $form['secondary_sectors_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Secondary Sectors label'),
    '#default_value' => isset($conf['secondary_sectors_label']) ? t($conf['secondary_sectors_label']) : t($form_state['plugin']['defaults']['secondary_sectors_label']),
    '#dependency' => array('secondary_sectors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );

  $form['programs_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Programs search field'),
    '#description' => t('Show programs search field'),
    '#default_value' => isset($conf['programs_flag']) ? intval($conf['programs_flag']) : $form_state['plugin']['defaults']['programs_flag'],
    '#id' => 'programs_flag',
  );
  $form['programs_popup'] = array(
    '#type' => 'checkbox',
    '#title' => t('Disable Programs popup select'),
    '#default_value' => isset($conf['programs_popup']) ? intval($conf['programs_popup']) : $form_state['plugin']['defaults']['programs_popup'],
    '#dependency' => array('programs_flag' => array(1)),
    '#dependency_type' => 'disable',
  );
  $form['programs_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Programs label'),
    '#default_value' => isset($conf['programs_label']) ? t($conf['programs_label']) : t($form_state['plugin']['defaults']['programs_label']),
    '#dependency' => array('programs_flag' => array(1)),
    '#dependency_type' => 'disable',
  );

  $form['donors_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Donors search field'),
    '#description' => t('Show donors search field'),
    '#default_value' => isset($conf['donors_flag']) ? intval($conf['donors_flag']) : $form_state['plugin']['defaults']['donors_flag'],
    '#id' => 'donors_flag',
  );
  $form['donors_popup'] = array(
    '#type' => 'checkbox',
    '#title' => t('Disable Donors popup select'),
    '#default_value' => isset($conf['donors_popup']) ? intval($conf['donors_popup']) : $form_state['plugin']['defaults']['donors_popup'],
    '#dependency' => array('donors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );
  $form['donors_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Donors label'),
    '#default_value' => isset($conf['donors_label']) ? t($conf['donors_label']) : t($form_state['plugin']['defaults']['donors_label']),
    '#dependency' => array('donors_flag' => array(1)),
    '#dependency_type' => 'disable',
  );

  $form['agencies_flag'] = array(
      '#type' => 'checkbox',
      '#title' => t('Show Agencies search field'),
      '#description' => t('Show agencies search field'),
      '#default_value' => isset($conf['agencies_flag']) ? intval($conf['agencies_flag']) : $form_state['plugin']['defaults']['agencies_flag'],
      '#id' => 'agencies_flag',
  );
  $form['agencies_popup'] = array(
      '#type' => 'checkbox',
      '#title' => t('Disable Agencies popup select'),
      '#default_value' => isset($conf['agencies_popup']) ? intval($conf['agencies_popup']) : $form_state['plugin']['defaults']['agencies_popup'],
      '#dependency' => array('agencies_flag' => array(1)),
      '#dependency_type' => 'disable',
  );
  $form['agencies_label'] = array(
      '#type' => 'textfield',
      '#title' => t('Agencies label'),
      '#default_value' => isset($conf['agencies_label']) ? t($conf['agencies_label']) : t($form_state['plugin']['defaults']['agencies_label']),
      '#dependency' => array('agencies_flag' => array(1)),
      '#dependency_type' => 'disable',
  );

  $form['locations_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Locations search field'),
    '#description' => t('Show locations search field'),
    '#default_value' => isset($conf['locations_flag']) ? intval($conf['locations_flag']) : $form_state['plugin']['defaults']['locations_flag'],
    '#id' => 'locations_flag',
  );
  $form['locations_popup'] = array(
    '#type' => 'checkbox',
    '#title' => t('Disable Locations popup select'),
    '#default_value' => isset($conf['locations_popup']) ? intval($conf['locations_popup']) : $form_state['plugin']['defaults']['locations_popup'],
    '#dependency' => array('locations_flag' => array(1)),
    '#dependency_type' => 'disable',
  );
  $form['locations_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Locations label'),
    '#default_value' => isset($conf['locations_label']) ? t($conf['locations_label']) : t($form_state['plugin']['defaults']['locations_label']),
    '#dependency' => array('locations_flag' => array(1)),
    '#dependency_type' => 'disable',
  );

  $form['period_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Period range search fields'),
    '#description' => t('Show Start and End year select controls'),
    '#default_value' => isset($conf['period_flag']) ? intval($conf['period_flag']) : $form_state['plugin']['defaults']['period_flag'],
    '#id' => 'period_flag',
  );

  $form['keywords_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show Keywords search field'),
    '#description' => t('Show keywords search field'),
    '#default_value' => isset($conf['keywords_flag']) ? intval($conf['keywords_flag']) : $form_state['plugin']['defaults']['keywords_flag'],
    '#id' => 'keywords_flag',
  );
  $form['keywords_label'] = array(
    '#type' => 'textfield',
    '#title' => t('Keywords label'),
    '#default_value' => isset($conf['keywords_label']) ? t($conf['keywords_label']) : t($form_state['plugin']['defaults']['keywords_label']),
    '#dependency' => array('keywords_flag' => array(1)),
    '#dependency_type' => 'disable',
  );

  $form['budget_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show On-budget/Off-budget projects flags'),
    '#description' => t('Show on-budget projects flag'),
    '#default_value' => isset($conf['budget_flag']) ? intval($conf['budget_flag']) : $form_state['plugin']['defaults']['budget_flag'],
  );

  $form['country_level_projects_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Include higher geographical levels flag'),
    '#description' => t('Include higher geographical levels flag'),
    '#default_value' => isset($conf['country_level_projects_flag']) ? intval($conf['country_level_projects_flag']) : $form_state['plugin']['defaults']['country_level_projects_flag'],
  );

  $form['preview_amount'] = array(
    '#type' => 'checkbox',
    '#title' => t('Pre-calculate amount(s)'),
    '#description' => t('Pre-calculate Commitment amount and show next to "project to show" number'),
    '#default_value' => isset($conf['preview_amount']) ? intval($conf['preview_amount']) : $form_state['plugin']['defaults']['preview_amount'],
  );

  return $form;
}


/*
 * handles edit for submit
 */
function ampp_projects_projects_search_form_content_type_edit_form_submit(&$form, &$form_state) {
   foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}
