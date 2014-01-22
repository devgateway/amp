<?php

/*
 * define plugin
 */
$plugin = array(
  'single' => TRUE,
  'title' => t('Search Result Totals'),
  'description' => t('Search Result Totals'),
  'category' => array(t('AMPP Widgets')),
);


/**
 *
 */
function ampp_projects_search_result_totals_content_type_render($subtype, $conf, $panel_args, $context) {
	global $amp_db_exists;
	$block = new stdClass();
	$block->content = '';
	
	$amount_commitment = get_total_amount(0);
    $amount_disbursement = get_total_amount(1);
    
	if (module_exists('format_number')) {
		$amount_commitment = empty($amount_commitment) ? "0" : format_number($amount_commitment, 0);
	    $amount_disbursement = empty($amount_disbursement) ? "0" : format_number($amount_disbursement, 0);
	} else {
	    $amount_commitment = empty($amount_commitment) ? "0" : number_format(intval($amount_commitment));
	    $amount_disbursement = empty($amount_disbursement) ? "0" : number_format(intval($amount_disbursement));
	}
	$html = '<div class="total-amounts view-projects-search-result">';
	$html .= '<table class="views-table commitment-disbursement"><thead><tr>';
	$html .= '<th class="views-field"></th>';
	$html .= '<th  class="views-field">'.t('Commitment amount').' ('.variable_get('currency_code').")".'</th>';
	$html .= '<th class="views-field">'.t('Disbursement amount').' ('.variable_get('currency_code').")".'</th>';
	$html .= '</tr></thead>';

	$html .= '<td>'.t("Total").':'.'</td>';
	$html .= '<td class="views-field commitment">' . $amount_commitment . '</td>';
	$html .= '<td class="views-field disbursement">' . $amount_disbursement . '</td>';
	$html .= '</tr></thead>';

	$html .= '</table>';
	$html .= '</div>';

    $block->content = $html;

	return $block;
}

