<?php

/**
 * @file
 * Projects search form
 *
 * @TODO: Remove the hardcoded IDs for form elements.
 */

include_once('projects_search_form.helpers.inc');

$plugin = array(
  'single' => TRUE,
  'title' => t('Projects Search Form'),
  'description' => t('Provides a search form for AMP Activities (projects).'),
  'category' => array(t('AMPP Widgets'), -9),
  'defaults' => array(
    'search_path' => 'search',
    'period_flag' => FALSE,
    'activity_status_flag' => FALSE,
    'budget_flag' => FALSE,
    'country_level_projects_flag' => FALSE,
    'preview_amount_flag' => FALSE,

    'sectors' => array(
      'show' => TRUE,
      'label' => 'Sectors',
      'popup' => FALSE,
    ),
    'sectors_primary' => array(
      'show' => FALSE,
      'label' => 'Primary Sectors',
      'popup' => FALSE,
    ),
    'sectors_secondary' => array(
      'show' => FALSE,
      'label' => 'Secondary Sectors',
      'popup' => FALSE,
    ),
    'organisations_dn' => array(
      'show' => TRUE,
      'label' => 'Donors',
      'popup' => FALSE,
    ),
    'organisations_ba' => array(
      'show' => FALSE,
      'label' => 'Beneficiary Agency',
      'popup' => FALSE, // @TODO: Add callback in ampp_search_modalform_callback().
    ),
    'programs' => array(
      'show' => FALSE,
      'label' => 'Programs',
      'popup' => FALSE,
    ),
    'locations' => array(
      'show' => TRUE,
      'label' => 'Locations',
      'popup' => FALSE,
    ),
    'keywords' => array(
      'show' => TRUE,
      'label' => 'Keywords',
      // 'popup' => FALSE,
    ),
  ),

  'render callback' => 'projects_search_form_block_render'
);

/**
 * Run-time rendering of the body of the block.
 */
function projects_search_form_block_render($subtype, $conf, $args, $context) {
  global $amp_db_exists;

  // Merge in plugin defaults, prevents not defined warnings on existing panels.
  $plugin_definition = ctools_get_plugins('ctools', 'content_types', $subtype);
  $conf = drupal_array_merge_deep($plugin_definition['defaults'], $conf);

  $block = new stdClass();
  $block->title = t('Projects search');
  $block->content = t('Could not connect to the activities database. Please try again later. If the problem persists contact the site administrator.');

  if (!$amp_db_exists) {
    return $block;
  }

  $block->content = drupal_get_form('projects_search_form', $conf);

  // Load the plugin specific JavaScript.
  drupal_add_js($plugin_definition['path'] . '/projects_search_form.js');

  // @TODO: Use libraries API.
  $module_path = drupal_get_path('module', 'ampp_projects');
  drupal_add_js($module_path . '/tokeninput/src/jquery.tokeninput.js');

  return $block;
}

/**
 * Returns a search form for AMP Activities (projects).
 */
function projects_search_form($form, &$form_state, $conf) {
  // By default we don't include the popup specific JS.
  $include_popup_javascript = FALSE;

  // Pass the pane configuration to the submit handler.
  $form_state['conf'] = $conf;

  // Get the 'popup icon' HTML.
  $theme_path = path_to_theme();
  $img_vars = array('path' => $theme_path . '/images/list.png');
  $popup_icon = theme('image', $img_vars);

  // @TODO: Remove this hack!
  unset($form['#attributes']['id']);
  $form['#attributes']['id'][] = 'ampp-search-form';

  foreach ($conf as $field_name => $field_info) {
    if (!is_array($field_info)) {
      continue;
    }

    if (empty($field_info['show'])) {
      continue;
    }

    // Default value for autocomplete fields is added using the token libary.
    $form[$field_name] = array(
      '#type' => 'textfield',
      '#title' => t($field_info['label']),
      '#description' => '',
      '#size' => 30,
      '#maxlength' => 64,
    );

    // Cleanup style.
    $form[$field_name]['#prefix'] = '<div class="clearfix">';
    $form[$field_name]['#suffix'] = '</div>';

    // Setup autocomplete fields using the token library.
    if ($field_name != 'keywords') {
      $form[$field_name]['#pre_render'] = array(
        'projects_search_form_pre_render_autocomplete_fields',
      );
    }

    // Setup the dialog popup behavior.
    if (!empty($field_info['popup'])) {
      $include_popup_javascript = TRUE;

      $popup_args = array(
        'html' => TRUE,
        'query' => array('format' => 'simple'),
        'attributes' => array(
          'class' => 'modal-selection',
          'rel' => 'edit-' . drupal_html_class($field_name),
          'title' => t($field_info['label'] . ' selection')
        )
      );

      $popup_url = 'ampp_search/modalform/' . $field_name;

      $form[$field_name]['#field_suffix'] = ' <span class="modal-list-more">';
      $form[$field_name]['#field_suffix'] .= l($popup_icon, $popup_url, $popup_args);
      $form[$field_name]['#field_suffix'] .= '</span>';
    }

    // Add description text to autocomplete fields.
    // @TODO: Use filter names to generate descriptions.
    switch ($field_name) {
      case 'sectors':
        $form[$field_name]['#description'] = t('Start typing a sector name then choose from the list.');
        break;

      case 'sectors_primary':
        $form[$field_name]['#description'] = t('Start typing a primary sector name then choose from the list.');
        break;

      case 'sectors_secondary':
        $form[$field_name]['#description'] = t('Start typing a secondary sector name then choose from the list.');
        break;

      case 'programs':
        $form[$field_name]['#description'] = t('Start typing a program name then choose from the list.');
        break;

      case 'locations':
        $form[$field_name]['#description'] = t('Start typing a location name then choose from the list.');
        break;

      case 'organisations_dn':
        $form[$field_name]['#description'] = t('Start typing a donor name then choose from the list.');
        break;

      case 'organisations_ba':
        $form[$field_name]['#description'] = t('Start typing a beneficiary agency name then choose from the list.');
        break;

      case 'keywords':
        $form[$field_name]['#description'] = t('Search in project titles.');

        // The keywords field needs to be after the period fields.
        $form[$field_name]['#weight'] = 2;

        // Add the default value.
        if (isset($_GET[$field_name])) {
          $form[$field_name]['#default_value'] = check_plain($_GET[$field_name]);
        }
        break;
    }
  }

  if (!empty($conf['period_flag'])) {
    // Get the input date format.
    $localized_date_formats = locale_get_localized_date_format(array($GLOBALS['language']->language));
    $format = $localized_date_formats['date_format_ampp_cal'];

    $period_start_default = '';
    if (isset($_GET['period_start'])) {
      $period_start_default = date_format($_GET['period_start'], $format);
    }

    $period_end_default = '';
    if (isset($_GET['period_end'])) {
      $period_end_default = date_format($_GET['period_end'], $format);
    }

    $form['period'] = array(
      '#type' => 'item',
      '#title' => t('Period from'),
      '#weight' => 1,
    );

    $form['period']['period_start'] = array(
      '#type' => 'date_popup',
      '#description' => t('Records with no value are ignored.'),
      '#default_value' => $period_start_default,
      '#date_type' => DATE_UNIX,
      '#date_timezone' => date_default_timezone(),
      '#date_format' => $format,
      '#date_year_range' => _amp_activity_activities_period_range_limits(),
    );
    $form['period']['period_end'] = array(
      '#type' => 'date_popup',
      '#default_value' => $period_end_default,
      '#date_type' => DATE_UNIX,
      '#date_timezone' => date_default_timezone(),
      '#date_format' => $format,
      '#date_year_range' => _amp_activity_activities_period_range_limits(),
    );

    $form['period']['period_start']['#prefix'] = '<div class="clearfix">';
    $form['period']['period_end']['#suffix'] = '</div>';
  }

  if (!empty($conf['activity_status_flag'])) {
    $options = array(
      '' => t('Show all projects'),
    ) + _amp_activity_get_activity_status_options();

    // Get the activity status from URL and sanatzie the value.
    $status_default = 'all';
    if (isset($_GET['status'])) {
      $status_default = (int) $_GET['status'];
    }

    $form['status'] = array(
      '#type' => 'select',
      '#title' => t('Status Filter'),
      '#options' => $options,
      '#default_value' => $status_default,
      '#attributes' => array(
        'class' => array('clearfix'),
      ),
      '#weight' => 3,
    );
  }

  if (!empty($conf['budget_flag'])) {
    $options = array(
      'all' => t('Show all projects'),
      'on' => t('On-budget only'),
      'off' => t('Off-budget only')
    );

    // Get the budget status from URL and sanatzie the value.
    $budget_default = 'all';
    if (isset($_GET['budget'])) {
      switch ($_GET['budget']) {
        case 'on':
          $budget_default = 'on';
          break;
        case 'off':
          $budget_default = 'off';
          break;
      }
    }

    $form['budget'] = array(
      '#type' => 'radios',
      '#title' => t('Show on-budget projects'),
      // '#description' => t('NOTE: There are some activities with no declared budget.'),
      '#options' => $options,
      '#default_value' => $budget_default,
      '#attributes' => array(
        'class' => array('clearfix'),
      ),
      '#weight' => 3,
    );
  }

  if (!empty($conf['country_level_projects_flag'])) {
    $clp_default = FALSE;
    if (!empty($_GET['country_level_projects'])) {
      $clp_default = TRUE;
    }

    $form['country_level_projects'] = array(
      '#type' => 'checkbox',
      '#title' => t('Include higher geographical levels'),
      '#weight' => 4,
      '#default_value' => $clp_default,
    );
  }

  if (!empty($conf['preview_amount_flag'])) {
    $form['preview_amount'] = array(
      '#type' => 'hidden',
      '#value' => TRUE,
      '#weight' => 3,
    );
  }

  $form['actions'] = array(
    '#type' => 'actions',
    '#weight' => 50,
  );

  $form['actions']['submit'] = array(
    '#type' => 'submit',
    '#value' => t('Show'),
  );

  $form['actions']['throbber'] = array(
    '#prefix' => '<span class="search-form-throbber">',
    '#markup' =>  t('calculating...'),
    '#suffix' => '</span>',
  );

  $form['actions']['form_info'] = array(
    '#prefix' => '<span class="search-form-info">',
    '#markup' =>  '',
    '#suffix' => '</span>',
  );

  // Add the autocompmlete and forminfo endpoint to JS.
  $form_js_settings = array();
  $form_js_settings['autocomplete_endpoint'] = url('ampp_search/autocomplete');
  $form_js_settings['forminfo_endpoint'] = url('ampp_search/forminfo');
  $form_js_settings['modalform_values_endpoint'] = url('ampp_search/modalform_values');

  drupal_add_js(array('projectsSearchForm' => $form_js_settings), 'setting');

  if ($include_popup_javascript) {
    drupal_add_library('system', 'ui.dialog');
  }

  return $form;
}

/**
 * Submit callback for projects_search_form().
 */
function projects_search_form_submit($form, &$form_state) {
  $conf = $form_state['conf'];
  $submit_path = $conf['search_path'];

  // Get a list of arguments for the search view.
  $arguments = _ampp_get_search_arguments(FALSE);
  $arguments = array_filter($arguments);

  // Filter out fields that are not enabled, makes the URL shorter.
  foreach ($conf as $field_name => $field_info) {
    if (!is_array($field_info)) {
      continue;
    }

    if (!empty($conf[$field_name]['show'])) {
      continue;
    }

    if (isset($arguments[$field_name]) && $arguments[$field_name] == '') {
      continue;
    }

    switch ($field_name) {
      case 'period_flag':
        unset($arguments['period_start']);
        unset($arguments['period_end']);
        break;

      default:
        unset($arguments[$field_name]);
        break;
    }
  }

  if (!empty($_GET['country_level_projects'])) {
    $arguments['country_level_projects'] = TRUE;
  }

  drupal_goto($submit_path, array('query' => $arguments));
}

/**
 * Setup the jQuery Token Input library for autocomplete fields.
 */
function projects_search_form_pre_render_autocomplete_fields($element) {
  $field_name = $element['#name'];

  // We need at least an empty array in order to intialize the token widget.
  $form_js_settings = array();
  $form_js_settings['autocomplete'][$field_name] = array();

  // @TODO: Remove this mapping.
  $field_values_callback_map = array(
    'sectors' => 'ampp_get_sectors_from_url',
    'sectors_primary' => 'ampp_get_primarysectors_from_url',
    'sectors_secondary' => 'ampp_get_secondarysectors_from_url',
    'programs' => 'ampp_get_programs_from_url',
    'locations' => 'ampp_get_locations_from_url',
    'organisations_dn' => 'ampp_get_donors_from_url',
    'organisations_ba' => 'ampp_get_agencies_from_url',
  );

  $autocomplete_info[$field_name] = NULL;
  // Check if we have a values callback.
  if (array_key_exists($field_name, $field_values_callback_map)) {
    $callback = $field_values_callback_map[$field_name];

    if (function_exists($callback)) {
      // Get the values from URL.
      $values = $callback();

      // Add the default values to javascript settings.
      if (!empty($values)) {
        $form_js_settings['autocomplete'][$field_name] = $values;
      }
    }
  }

  drupal_add_js(array('projectsSearchForm' => $form_js_settings), 'setting');

  return $element;
}

/**
 * Provides the pane settings form.
 */
function ampp_projects_projects_search_form_content_type_edit_form($form, &$form_state) {
  $plugin_defaults = $form_state['plugin']['defaults'];
  $conf = $form_state['conf'];

  $form['note_at_least_one'] = array(
    '#type' => 'item',
    '#markup' => '<div><b>At least one control should be selected.</b></div>',
    '#weight' => 0,
  );

  // Hide the override_title description text.
  unset($form['override_title_markup']);

  $form['search_path'] = array(
    '#type' => 'textfield',
    '#title' => t('Search path'),
    '#description' => t('This should point to a page that contains the "Projects Search Result" block. Filter values will be submitted to that page.'),
    '#default_value' => $conf['search_path'],
    '#required' => TRUE,
  );

  $form['period_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show period range filters'),
    '#description' => t('Displays a pair of date fields that can be used to filter projects by their implementation period.'),
    '#default_value' => $conf['period_flag'],
  );

  $form['activity_status_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Display status filter.'),
    '#description' => t('Displays an activity status filter.'),
    '#default_value' => $conf['activity_status_flag'],
  );

  $form['budget_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show On/Off-budget checkbox'),
    '#description' => t('Displays a checkbox that can be used to filter project by their budget status.'),
    '#default_value' => $conf['budget_flag'],
  );

  $form['country_level_projects_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Include higher geographical levels flag'),
    '#description' => t('Include higher geographical levels flag'),
    '#default_value' => $conf['country_level_projects_flag'],
  );

  $form['preview_amount_flag'] = array(
    '#type' => 'checkbox',
    '#title' => t('Pre-calculate amount(s)'),
    '#description' => t('Calculates the Commitment amount and shows the information next to submit button.'),
    '#default_value' => $conf['preview_amount_flag'],
  );

  // Create the columns fieldset.
  $form['filters'] = array(
    '#type' => 'fieldset',
    '#title' => t('Filters visibility and labels'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );

  foreach ($plugin_defaults as $field_name => $field_info) {
    if (!is_array($field_info)) {
      continue;
    }

    $form['filters'][$field_name] = array(
      '#tree' => TRUE,
    );

    $form['filters'][$field_name]['show'] = array(
      '#type' => 'checkbox',
      '#title' => t('Show the ' . $field_info['label'] . ' filter'),
      '#default_value' => $plugin_defaults[$field_name]['show'],
    );

    $form['filters'][$field_name]['label'] = array(
      '#type' => 'textfield',
      '#title' => t('Label'),
      '#size' => 30,
      '#default_value' => $plugin_defaults[$field_name]['label'],
      '#dependency' => array(
        'edit-' . drupal_html_class($field_name) . '-show' => array(1),
      ),
    );

    // Display the popup option only if the filter supports it.
    if (isset($field_info['popup'])) {
      $form['filters'][$field_name]['popup'] = array(
        '#type' => 'checkbox',
        '#title' => t('Show filter popup icon'),
        '#description' => t('Adds a small icon next to the field. When clicked a window will popup with all the available filter options.'),
        '#default_value' => $plugin_defaults[$field_name]['popup'],
        '#dependency' => array(
          'edit-' . drupal_html_class($field_name) . '-show' => array(1),
        ),
      );
    }
  }

  return $form;
}

/**
 * Submit callback for ampp_projects_projects_search_form_content_type_edit_form().
 */
function ampp_projects_projects_search_form_content_type_edit_form_submit(&$form, &$form_state) {
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}
