<?php 


/**
 * @file
 * Top activities by disbursement
 *
 */

$plugin = array(
    'single' => TRUE,
    'title' => t('Top activities by disbursement'),
    'description' => t('Top activities by disbursement'),
    'category' => array(t('AMPP Widgets')),
    'defaults' => array(
        'rows_to_show' => 10,
        'top_text' => '',
        'bottom_text' => '',
        'heading_title' => 'Project title',
        'heading_primary_sector' => 'Primary sector',
        'heading_donor' => 'Donor',
        'heading_dp_last_period' => 'DP last period',
        'heading_da_last_period' => 'DA last period',
        'heading_dp_current_period' => 'DP current period',
        'default_sort' => 'field_total_planned_disbursement',
        'default_order' => 'desc',
    ),
    'render callback' => 'ampp_projects_tabd_render',
);


/**
 *
 */
function ampp_projects_tabd_render($subtype, $conf, $args, $context) {

	$block = new stdClass();
	
	$_SESSION['top_activities_by_disbursement_conf'] = serialize($conf);
	
	$view = views_get_view('top_activities_by_disbursment');
	$tmpview = $view->clone_view();
	$tmpview->set_display('block_1');
	
	$block->title = t('Top activities by disbursement');
	$block->content = (isset($conf['top_text']) && !empty($conf['top_text'])) ? '<div class="top-text">' . $conf['top_text'] . '</div>' : '';	
	$block->content .= $tmpview->preview('block_1', $args);
	$block->content .= (isset($conf['bottom_text']) && !empty($conf['bottom_text'])) ? '<div class="bottom-text">' . $conf['bottom_text'] . '</div>' : '';
	return $block;
}


/**
 *
 */
function ampp_projects_top_activities_by_disbursement_content_type_edit_form($form, &$form_state) {
	$conf = $form_state['conf'];

	$form['rows_to_show'] = array(
	    '#type' => 'textfield',
	    '#title' => t('Number of projects to show.'),
	    '#size' => 40, 
	    '#description' => t('Number of projects to show.'),
	    '#default_value' => isset($conf['rows_to_show']) ? $conf['rows_to_show'] : $form_state['plugin']['defaults']['rows_to_show'],
	    '#required' => TRUE,
	);
	
    foreach ($form_state['plugin']['defaults'] as $k => $v) {
        if (substr($k, 0, 8) == 'heading_') {
            $form[$k] = array(
                '#type' => 'textfield',
                '#title' => t('Heading for the "' . strtoupper(substr($k, 8, 1)) . str_replace('_', ' ', substr($k, 9)) . '" column'),
                '#size' => 40, 
                '#default_value' => isset($conf[$k]) ? $conf[$k] : $form_state['plugin']['defaults'][$k],
                '#required' => FALSE,
            );
        }
	}
	
	$form['rows_to_show'] = array(
        '#type' => 'textfield',
        '#title' => t('Number of projects to show.'),
        '#size' => 40, 
        '#description' => t('Number of projects to show.'),
        '#default_value' => isset($conf['rows_to_show']) ? $conf['rows_to_show'] : $form_state['plugin']['defaults']['rows_to_show'],
        '#required' => TRUE,
    );
	
	$options = array (
	   'title' => t('Title'),
	   'field_primary_sector_value' => 'Primary sector',
       'field_donor_value' => 'Donor',
       'field_dp_last_period_value' => 'DP last period',
       'field_da_last_period_value' => 'DA last period',
       'field_dp_current_period_value' => 'DP current period',
       'field_total_planned_disbursement_value' => 'Total planned disbursement',
	);
	
    $form['default_sort'] = array(
        '#type' => 'select',
        '#title' => t('Default sorting column.'),
        '#options' => $options, 
        '#default_value' => isset($conf['default_sort']) ? $conf['default_sort'] : $form_state['plugin']['defaults']['default_sort'],
        '#required' => TRUE,
    );
	
	$form['default_order'] = array(
        '#type' => 'select',
        '#title' => t('Default order.'),
        '#options' => array('asc' => t('Ascending'), 'desc' => t('Descending')), 
        '#default_value' => isset($conf['default_order']) ? $conf['default_order'] : $form_state['plugin']['defaults']['default_order'],
        '#required' => TRUE,
    );
	
	$form['top_text'] = array(
        '#type' => 'textarea',
        '#title' => t('Text above the table'),
        //'#description' => t('A list of HTML tags that can be used.') . $allowed_html, 
        '#default_value' => isset($conf['top_text']) ? $conf['top_text'] : $form_state['plugin']['defaults']['top_text'],
        '#required' => FALSE,
    );
    
    $form['bottom_text'] = array(
        '#type' => 'textarea',
        '#title' => t('Text below the table'),
        //'#description' => t('A list of HTML tags that can be used.') . $allowed_html,
        '#default_value' => isset($conf['bottom_text']) ? $conf['bottom_text'] : $form_state['plugin']['defaults']['bottom_text'],
        '#required' => FALSE,
    );

	return $form;	
}


/**
 * handles edit for submit
 */
function ampp_projects_top_activities_by_disbursement_content_type_edit_form_submit(&$form, &$form_state) { 
    foreach (array_keys($form_state['plugin']['defaults']) as $key) {
        $form_state['conf'][$key] = $form_state['values'][$key];
    }
}
