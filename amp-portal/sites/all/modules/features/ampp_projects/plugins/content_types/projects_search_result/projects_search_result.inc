<?php

/**
 * @file
 * Projects Search Result
 *
 * @TODO: Create an URL alias only when requested.
 */

$plugin = array(
  'single' => TRUE,
  'title' => t('Projects Search Result'),
  'description' => t('Projects Search Result'),
  'category' => array(t('AMPP Widgets')),
  'defaults' => array(
    'show_info_column' => TRUE,
    'show_status_column' => FALSE,
    'show_export_links' => FALSE,

    // @TODO: Extract default settings from the view fields list.
    'name' => array(
      'show' => TRUE,
      'label' => t('Project title'),
    ),
    'organisation_names' => array(
      'show' => TRUE,
      'label' => t('Donors'),
    ),
    'organisation_names_1' => array(
      'show' => TRUE,
      'label' => t('Beneficiary Agencies'),
    ),
    'sector_names' => array(
      'show' => TRUE,
      'label' => t('Sectors'),
    ),
    'sector_names_1' => array(
      'show' => FALSE,
      'label' => t('Primary sectors'),
    ),
    'sector_names_2' => array(
      'show' => FALSE,
      'label' => t('Secondary sectors'),
    ),
    'program_names' => array(
      'show' => FALSE,
      'label' => t('Programs'),
    ),
    'location_names' => array(
      'show' => TRUE,
      'label' => t('Locations'),
    ),
    'actual_start_date' => array(
      'show' => TRUE,
      'label' => t('Start date'),
    ),
    'actual_completion_date' => array(
      'show' => TRUE,
      'label' => t('End date'),
    ),
    'commitment_amount' => array(
      'show' => TRUE,
      'label' => t('Commitment amount'),
    ),
    'disbursement_amount' => array(
      'show' => TRUE,
      'label' => t('Commitment amount'),
    ),
  ),
  'render callback' => 'ampp_projects_projects_search_result_render'
);

/**
 * Run-time rendering of the body of the block.
 */
function ampp_projects_projects_search_result_render($subtype, $conf, $panel_args, $context) {
  // Define the pane block.
  $block = new stdClass();
  $block->title = t('Projects search');
  $block->content = '';

  // Merge in plugin defaults, prevents not defined warnings on existing panels.
  $plugin_definition = ctools_get_plugins('ctools', 'content_types', $subtype);
  $conf = drupal_array_merge_deep($plugin_definition['defaults'], $conf);

  // @TODO: Remove or find a different to pas information to views_pre_render.
  $_SESSION['projects_search_result_conf'] = serialize($conf);

  // Get the view arguments.
  $args = _ampp_get_search_arguments(TRUE);

  $view = views_get_view('projects_search_result');
  $view->set_display('panel_pane_1');
  $view->set_arguments($args);
  $view->pre_execute();
  $view->execute();

  // Render XLS export links.
  if (!empty($conf['show_export_links'])) {
    $url_query_args = _ampp_get_search_arguments(FALSE);
    $url_query_args = array_filter($url_query_args);

    $url_query_args['show_status_column'] = $conf['show_status_column'];
    if (!empty($_GET['country_level_projects'])) {
      $url_query_args['country_level_projects'] = TRUE;
    }

    $url_args = array(
      'attributes' => array(
        'class' => array(
          'export-link',
          'with-icon',
        ),
      ),
      'query' => $url_query_args,
    );
    $block->content .= '<div class="export-link-wrapper">';
    $block->content .= l(t('Export in XLS format'), 'searchexport/searchresult.xls', $url_args);

    if (module_exists('shorten') && variable_get('ampp_generate_short_urls', TRUE)) {
      $short_url_args = array(
        'absolute' => TRUE,
        'query' => $url_query_args,
      );
      $short_url = shorten_url(url('searchexport/searchresult.xls', $short_url_args));

      $block->content .= '<br ><a class="export-link short" href="http://' . $short_url . '"><b>' . t('Permanent link') . '</b></a>';
    }
    $block->content .= '</div>';
  }

  // optionally hide the first column.
  if (empty($conf['show_info_column'])) {
    unset($view->field['amp_activity_id']);
  }

  // optionally show the Status column.
  if (empty($conf['show_status_column'])) {
    unset($view->field['status_name']);
  }

  if (!empty($view->total_rows)) {
    $block->content .= $view->preview('panel_pane_1');
  }
  else {
    // Display no results text message.
    $block->content .= t(variable_get('amp_no_search_results_text', 'No results found'));
  }

  return $block;
}

/**
 * @TODO: Find another way to style the pages that contain this content_type.
 */
function ampp_projects_preprocess_html(&$variables) {
  $variables['classes_array'][] = "page-search";
}

/**
 * Create the settings form.
 */
function ampp_projects_projects_search_result_content_type_edit_form($form, &$form_state) {
  ctools_include('dependent');

  $conf = $form_state['conf'];

  $form['note_show_columns'] = array(
    '#type' => 'item',
    '#markup' => '<div><b>' . t('Show columns') . '</b></div>',
    '#description' => t('At least one column should be selected.'),
    '#weight' => 0,
  );

  // Hide the override_title description text.
  unset($form['override_title_markup']);

  $form['show_info_column'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show the first column'),
    '#default_value' => $conf['show_info_column'],
  );

  $form['show_status_column'] = array(
    '#type' => 'checkbox',
    '#title' => t('Show the Status column'),
    '#default_value' => $conf['show_status_column'],
  );

  $form['show_export_links'] = array(
    '#type' => 'checkbox',
    '#title' => t('Enable Export To XLS option'),
    '#default_value' => $conf['show_export_links'],
  );

  // Create the columns fieldset.
  $form['columns'] = array(
    '#type' => 'fieldset',
    '#title' => t('Column visibility and labels'),
    '#collapsible' => TRUE,
    '#collapsed' => TRUE,
  );

  foreach ($conf as $field_name => $field_info) {
    if (!is_array($field_info)) {
      continue;
    }

    $form['columns'][$field_name] = array(
      '#tree' => TRUE,
    );

    $form['columns'][$field_name]['show'] = array(
      '#type' => 'checkbox',
      '#title' => t('Show ' . $field_info['label'] . ' column'),
      '#default_value' => $field_info['show'],
    );

    $form['columns'][$field_name]['label'] = array(
      '#type' => 'textfield',
      '#title' => t('Label'),
      '#size' => 30,
      '#default_value' => $field_info['label'],
      '#dependency' => array(
        'edit-' . drupal_html_class($field_name) . '-show' => array(1),
      ),
    );
  }

  return $form;
}

/**
 * Update custom content_type configuration.
 */
function ampp_projects_projects_search_result_content_type_edit_form_submit(&$form, &$form_state) {
  foreach (array_keys($form_state['plugin']['defaults']) as $key) {
    $form_state['conf'][$key] = $form_state['values'][$key];
  }
}
