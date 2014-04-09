<?php
/**
 * @file
 * ampp_general.strongarm.inc
 */

/**
 * Implements hook_strongarm().
 */
function ampp_general_strongarm() {
  $export = array();

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_403';
  $strongarm->value = 'You are not authorized to access this page.';
  $export['customerror_403'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_403_php';
  $strongarm->value = 0;
  $export['customerror_403_php'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_403_theme';
  $strongarm->value = 'ampp';
  $export['customerror_403_theme'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_403_title';
  $strongarm->value = 'Access denied';
  $export['customerror_403_title'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_404';
  $strongarm->value = 'Requested page not found';
  $export['customerror_404'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_404_php';
  $strongarm->value = 0;
  $export['customerror_404_php'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_404_theme';
  $strongarm->value = 'ampp';
  $export['customerror_404_theme'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_404_title';
  $strongarm->value = 'Page not found';
  $export['customerror_404_title'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'customerror_redirect';
  $strongarm->value = '';
  $export['customerror_redirect'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'nodequeue_autocomplete_limit';
  $strongarm->value = '10';
  $export['nodequeue_autocomplete_limit'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'nodequeue_links';
  $strongarm->value = FALSE;
  $export['nodequeue_links'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'nodequeue_tab_name';
  $strongarm->value = 'Nodequeue';
  $export['nodequeue_tab_name'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'nodequeue_use_tab';
  $strongarm->value = 1;
  $export['nodequeue_use_tab'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'nodequeue_view_per_queue';
  $strongarm->value = 0;
  $export['nodequeue_view_per_queue'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'page_manager_node_view_disabled';
  $strongarm->value = FALSE;
  $export['page_manager_node_view_disabled'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'panelizer_defaults_node_page';
  $strongarm->value = array(
    'status' => 1,
    'default' => 1,
    'choice' => 0,
  );
  $export['panelizer_defaults_node_page'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'panelizer_node:page_allowed_layouts';
  $strongarm->value = 'O:22:"panels_allowed_layouts":4:{s:9:"allow_new";b:1;s:11:"module_name";s:19:"panelizer_node:page";s:23:"allowed_layout_settings";a:14:{s:8:"flexible";b:0;s:14:"twocol_stacked";b:0;s:13:"twocol_bricks";b:0;s:6:"twocol";b:0;s:25:"threecol_33_34_33_stacked";b:0;s:17:"threecol_33_34_33";b:0;s:25:"threecol_25_50_25_stacked";b:0;s:17:"threecol_25_50_25";b:0;s:6:"onecol";b:0;s:9:"ampp_home";b:1;s:8:"ampp_one";b:1;s:10:"ampp_three";b:1;s:8:"ampp_two";b:1;s:11:"ampp-two-23";b:1;}s:10:"form_state";N;}';
  $export['panelizer_node:page_allowed_layouts'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'panelizer_node:page_allowed_types';
  $strongarm->value = array(
    'custom-custom' => 'custom-custom',
    'block-locale-language' => 0,
    'block-menu-menu-amp-links' => 0,
    'block-menu-devel' => 0,
    'block-menu-menu-footer-links' => 0,
    'block-menu-menu-header-links' => 0,
    'block-node-syndicate' => 0,
    'block-node-recent' => 0,
    'block-shortcut-shortcuts' => 0,
    'block-shorten-shorten' => 0,
    'block-shorten-shorten_short' => 0,
    'block-system-powered-by' => 0,
    'block-system-help' => 0,
    'block-system-navigation' => 0,
    'block-system-management' => 0,
    'block-system-user-menu' => 0,
    'block-system-main-menu' => 0,
    'block-user-login' => 0,
    'block-user-new' => 0,
    'block-user-online' => 0,
    'block-devel-execute_php' => 0,
    'block-devel-switch_user' => 0,
    'entity_field-node:body' => 'entity_field-node:body',
    'entity_field-node:field_image' => 0,
    'entity_field-node:field_slideshow_image' => 0,
    'entity_field-node:field_link' => 0,
    'entity_field-node:field_slideshow_image_toggle' => 0,
    'entity_field-node:field_show_title_in_slideshow' => 0,
    'entity_field-node:field_files' => 0,
    'entity_field-node:field_images' => 0,
    'entity_field-node:field_da_last_period' => 0,
    'entity_field-node:field_donor' => 0,
    'entity_field-node:field_dp_current_period' => 0,
    'entity_field-node:field_dp_last_period' => 0,
    'entity_field-node:field_primary_sector' => 0,
    'entity_field-node:field_total_planned_disbursement' => 0,
    'token-array:keys' => 'token-array:keys',
    'token-array:value' => 'token-array:value',
    'token-node:source' => 0,
    'token-node:log' => 0,
    'token-node:content-type' => 0,
    'token-node:feed-source' => 0,
    'token-node:nid' => 0,
    'token-node:vid' => 0,
    'token-node:title' => 0,
    'token-node:body' => 0,
    'token-node:summary' => 0,
    'token-node:language' => 0,
    'token-node:url' => 0,
    'token-node:edit-url' => 0,
    'token-node:created' => 0,
    'token-node:changed' => 0,
    'token-node:author' => 0,
    'token-node:short-url' => 0,
    'token-node:menu-link' => 0,
    'token-node:is-new' => 0,
    'token-node:status' => 0,
    'token-node:promote' => 0,
    'token-node:sticky' => 0,
    'token-node:revision' => 0,
    'token-node:field-slideshow-image-toggle' => 0,
    'token-node:field-show-title-in-slideshow' => 0,
    'token-node:field-da-last-period' => 0,
    'token-node:field-donor' => 0,
    'token-node:field-dp-current-period' => 0,
    'token-node:field-dp-last-period' => 0,
    'token-node:field-primary-sector' => 0,
    'token-node:field-total-planned-disbursement' => 0,
    'token-node:field_image' => 0,
    'token-node:field_slideshow_image' => 0,
    'token-node:field_files' => 0,
    'token-node:field_images' => 0,
    'token-node:field_link' => 0,
    'token-node:field_slideshow_image_toggle' => 0,
    'token-node:field_show_title_in_slideshow' => 0,
    'token-node:field_da_last_period' => 0,
    'token-node:field_donor' => 0,
    'token-node:field_dp_current_period' => 0,
    'token-node:field_dp_last_period' => 0,
    'token-node:field_primary_sector' => 0,
    'token-node:field_total_planned_disbursement' => 0,
    'token-content-type:name' => 0,
    'token-content-type:machine-name' => 0,
    'token-content-type:description' => 0,
    'token-content-type:node-count' => 0,
    'token-content-type:edit-url' => 0,
    'token-term:edit-url' => 0,
    'token-term:root' => 0,
    'token-term:tid' => 0,
    'token-term:name' => 0,
    'token-term:description' => 0,
    'token-term:node-count' => 0,
    'token-term:url' => 0,
    'token-term:vocabulary' => 0,
    'token-term:parent' => 0,
    'token-term:weight' => 0,
    'token-vocabulary:machine-name' => 0,
    'token-vocabulary:edit-url' => 0,
    'token-vocabulary:vid' => 0,
    'token-vocabulary:name' => 0,
    'token-vocabulary:description' => 0,
    'token-vocabulary:node-count' => 0,
    'token-vocabulary:term-count' => 0,
    'token-file:extension' => 0,
    'token-file:size-raw' => 0,
    'token-file:fid' => 0,
    'token-file:name' => 0,
    'token-file:path' => 0,
    'token-file:mime' => 0,
    'token-file:size' => 0,
    'token-file:url' => 0,
    'token-file:timestamp' => 0,
    'token-file:owner' => 0,
    'token-user:cancel-url' => 0,
    'token-user:one-time-login-url' => 0,
    'token-user:roles' => 0,
    'token-user:role-names' => 0,
    'token-user:role-ids' => 0,
    'token-user:piwik-role-names' => 0,
    'token-user:piwik-role-ids' => 0,
    'token-user:uid' => 0,
    'token-user:name' => 0,
    'token-user:mail' => 0,
    'token-user:url' => 0,
    'token-user:edit-url' => 0,
    'token-user:last-login' => 0,
    'token-user:created' => 0,
    'token-current-user:ip-address' => 0,
    'token-menu-link:mlid' => 0,
    'token-menu-link:title' => 0,
    'token-menu-link:url' => 0,
    'token-menu-link:parent' => 0,
    'token-menu-link:root' => 0,
    'token-menu-link:menu' => 0,
    'token-menu-link:edit-url' => 0,
    'token-current-page:title' => 0,
    'token-current-page:url' => 0,
    'token-current-page:page-number' => 0,
    'token-current-page:query' => 0,
    'token-url:path' => 0,
    'token-url:relative' => 0,
    'token-url:absolute' => 0,
    'token-url:brief' => 0,
    'token-url:unaliased' => 0,
    'token-url:args' => 0,
    'token-array:first' => 0,
    'token-array:last' => 0,
    'token-array:count' => 0,
    'token-array:reversed' => 0,
    'token-array:join' => 0,
    'token-random:number' => 0,
    'token-random:hash' => 0,
    'token-date-field-value:date' => 0,
    'token-date-field-value:to-date' => 0,
    'token-excel-file:path' => 0,
    'token-excel-file:url' => 0,
    'token-excel-sheet:id' => 0,
    'token-excel-sheet:name' => 0,
    'token-excel-sheet:cell-1-1-formatted' => 0,
    'token-excel-sheet:cell-1-1' => 0,
    'token-excel-row:row' => 0,
    'token-excel-row:range-id' => 0,
    'token-excel-row:column-offset' => 0,
    'token-excel-row:column-1-formatted' => 0,
    'token-excel-row:column-1' => 0,
    'token-excel-row:x-1-formatted' => 0,
    'token-excel-row:x-1' => 0,
    'token-excel-column:row-1-formatted' => 0,
    'token-excel-column:row-1' => 0,
    'token-excel-column:y-1-formatted' => 0,
    'token-excel-column:y-1' => 0,
    'token-excel-column:column' => 0,
    'token-excel-column:column-alpha' => 0,
    'token-excel-column:row-offset' => 0,
    'token-site:name' => 0,
    'token-site:slogan' => 0,
    'token-site:mail' => 0,
    'token-site:url' => 0,
    'token-site:url-brief' => 0,
    'token-site:login-url' => 0,
    'token-site:current-user' => 0,
    'token-site:current-date' => 0,
    'token-date:short' => 0,
    'token-date:medium' => 0,
    'token-date:long' => 0,
    'token-date:custom' => 0,
    'token-date:since' => 0,
    'token-date:raw' => 0,
    'token-date:ampp_cal' => 0,
    'token-variable:piwik_site_id' => 0,
    'token-variable:site_name' => 0,
    'token-variable:site_mail' => 0,
    'token-variable:site_slogan' => 0,
    'token-variable:anonymous' => 0,
    'token-variable:feed_description' => 0,
    'token-variable:maintenance_mode_message' => 0,
    'token-variable:user_registration_help' => 0,
    'token-variable:user_picture_dimensions' => 0,
    'token-variable:user_picture_guidelines' => 0,
    'token-menu:name' => 0,
    'token-menu:machine-name' => 0,
    'token-menu:description' => 0,
    'token-menu:menu-link-count' => 0,
    'token-menu:edit-url' => 0,
    'entity_form_field-node:body' => 0,
    'entity_form_field-node:field_image' => 0,
    'entity_form_field-node:field_slideshow_image' => 0,
    'entity_form_field-node:field_link' => 0,
    'entity_form_field-node:field_slideshow_image_toggle' => 0,
    'entity_form_field-node:field_show_title_in_slideshow' => 0,
    'entity_form_field-node:field_files' => 0,
    'entity_form_field-node:field_images' => 0,
    'entity_form_field-node:field_da_last_period' => 0,
    'entity_form_field-node:field_donor' => 0,
    'entity_form_field-node:field_dp_current_period' => 0,
    'entity_form_field-node:field_dp_last_period' => 0,
    'entity_form_field-node:field_primary_sector' => 0,
    'entity_form_field-node:field_total_planned_disbursement' => 0,
    'views_panes-homepage_slideshow-panel_pane_5' => 'views_panes-homepage_slideshow-panel_pane_5',
    'views_panes-projects_search_result-panel_pane_1' => 0,
    'views-homepage_slideshow' => 'views-homepage_slideshow',
    'views-slideshows' => 'views-slideshows',
    'views-top_activities_by_disbursment' => 'views-top_activities_by_disbursment',
    'views-feeds_log' => 0,
    'views-news' => 0,
    'views-projects_search_result' => 0,
    'feedback-feedback' => 'feedback-feedback',
    'query-query' => 'query-query',
    'news_list-news_list' => 'news_list-news_list',
    'recent_news-recent_news' => 'recent_news-recent_news',
    'top_activities_by_disbursement-top_activities_by_disbursement' => 'top_activities_by_disbursement-top_activities_by_disbursement',
    'data_quality-data_quality' => 'data_quality-data_quality',
    'projects_search_result-projects_search_result' => 'projects_search_result-projects_search_result',
    'projects_search_form-projects_search_form' => 'projects_search_form-projects_search_form',
    'search_result_totals-search_result_totals' => 'search_result_totals-search_result_totals',
    'projects_as_of-projects_as_of' => 'projects_as_of-projects_as_of',
    'commitments_report-commitments_report' => 'commitments_report-commitments_report',
    'ext_source-ext_source' => 0,
    'page_logo-page_logo' => 0,
    'page_title-page_title' => 0,
    'page_primary_links-page_primary_links' => 0,
    'page_secondary_links-page_secondary_links' => 0,
    'page_site_name-page_site_name' => 0,
    'page_breadcrumb-page_breadcrumb' => 0,
    'page_feed_icons-page_feed_icons' => 0,
    'page_help-page_help' => 0,
    'page_tabs-page_tabs' => 0,
    'page_messages-page_messages' => 0,
    'page_slogan-page_slogan' => 0,
    'node_form_path-node_form_path' => 0,
    'node_form_author-node_form_author' => 0,
    'node_form_menu-node_form_menu' => 0,
    'node_form_publishing-node_form_publishing' => 0,
    'node_form_title-node_form_title' => 0,
    'node_form_log-node_form_log' => 0,
    'node_form_buttons-node_form_buttons' => 0,
    'vocabulary_terms-vocabulary_terms' => 0,
    'node-node' => 0,
    'term_description-term_description' => 0,
    'term_list-term_list' => 0,
    'node_body-node_body' => 0,
    'node_type_desc-node_type_desc' => 0,
    'node_content-node_content' => 0,
    'node_terms-node_terms' => 0,
    'node_author-node_author' => 0,
    'node_links-node_links' => 0,
    'node_attachments-node_attachments' => 0,
    'node_updated-node_updated' => 0,
    'node_created-node_created' => 0,
    'node_title-node_title' => 0,
    'user_profile-user_profile' => 0,
    'user_picture-user_picture' => 0,
    'user_signature-user_signature' => 0,
    'form-form' => 0,
    'views_exposed-views_exposed' => 0,
    'views_footer-views_footer' => 0,
    'views_attachments-views_attachments' => 0,
    'views_empty-views_empty' => 0,
    'views_pager-views_pager' => 0,
    'views_view-views_view' => 0,
    'views_feed-views_feed' => 0,
    'views_row-views_row' => 0,
    'views_header-views_header' => 0,
  );
  $export['panelizer_node:page_allowed_types'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_blog_pattern';
  $strongarm->value = 'blogs/[user:name]';
  $export['pathauto_blog_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_forum_pattern';
  $strongarm->value = '[term:vocabulary]/[term:name]';
  $export['pathauto_forum_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_donor_profile_pattern';
  $strongarm->value = '';
  $export['pathauto_node_donor_profile_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_event_pattern';
  $strongarm->value = 'event/[node:title]';
  $export['pathauto_node_event_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_homepage_slideshow_pattern';
  $strongarm->value = '';
  $export['pathauto_node_homepage_slideshow_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_imported_item_pattern';
  $strongarm->value = 'news/[node:title]';
  $export['pathauto_node_imported_item_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_map_pattern';
  $strongarm->value = '';
  $export['pathauto_node_map_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_news_pattern';
  $strongarm->value = 'news/[node:title]';
  $export['pathauto_node_news_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_page_en_pattern';
  $strongarm->value = '';
  $export['pathauto_node_page_en_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_page_pattern';
  $strongarm->value = '[node:title]';
  $export['pathauto_node_page_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_page_und_pattern';
  $strongarm->value = '';
  $export['pathauto_node_page_und_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_node_pattern';
  $strongarm->value = 'content/[node:title]';
  $export['pathauto_node_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_punctuation_hyphen';
  $strongarm->value = 1;
  $export['pathauto_punctuation_hyphen'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_taxonomy_term_pattern';
  $strongarm->value = '[term:vocabulary]/[term:name]';
  $export['pathauto_taxonomy_term_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'pathauto_user_pattern';
  $strongarm->value = 'users/[user:name]';
  $export['pathauto_user_pattern'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'path_alias_whitelist';
  $strongarm->value = array(
    'node' => TRUE,
    'taxonomy' => TRUE,
    'user' => TRUE,
  );
  $export['path_alias_whitelist'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_cache';
  $strongarm->value = 0;
  $export['piwik_cache'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_codesnippet_after';
  $strongarm->value = '';
  $export['piwik_codesnippet_after'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_codesnippet_before';
  $strongarm->value = '';
  $export['piwik_codesnippet_before'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_custom';
  $strongarm->value = '0';
  $export['piwik_custom'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_custom_var';
  $strongarm->value = array(
    'slots' => array(
      1 => array(
        'slot' => 1,
        'name' => '',
        'value' => '',
        'scope' => 'visit',
      ),
      2 => array(
        'slot' => 2,
        'name' => '',
        'value' => '',
        'scope' => 'visit',
      ),
      3 => array(
        'slot' => 3,
        'name' => '',
        'value' => '',
        'scope' => 'visit',
      ),
      4 => array(
        'slot' => 4,
        'name' => '',
        'value' => '',
        'scope' => 'visit',
      ),
      5 => array(
        'slot' => 5,
        'name' => '',
        'value' => '',
        'scope' => 'visit',
      ),
    ),
  );
  $export['piwik_custom_var'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_domain_mode';
  $strongarm->value = '0';
  $export['piwik_domain_mode'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_js_scope';
  $strongarm->value = 'footer';
  $export['piwik_js_scope'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_pages';
  $strongarm->value = 'admin
admin/*
batch
node/add*
node/*/*
user/*/*';
  $export['piwik_pages'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_page_title_hierarchy';
  $strongarm->value = 1;
  $export['piwik_page_title_hierarchy'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_page_title_hierarchy_exclude_home';
  $strongarm->value = 1;
  $export['piwik_page_title_hierarchy_exclude_home'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_privacy_donottrack';
  $strongarm->value = 1;
  $export['piwik_privacy_donottrack'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_roles';
  $strongarm->value = array(
    1 => 0,
    2 => 0,
    3 => 0,
  );
  $export['piwik_roles'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_site_search';
  $strongarm->value = FALSE;
  $export['piwik_site_search'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_track';
  $strongarm->value = 1;
  $export['piwik_track'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_trackfiles_extensions';
  $strongarm->value = '7z|aac|arc|arj|asf|asx|avi|bin|csv|doc|exe|flv|gif|gz|gzip|hqx|jar|jpe?g|js|mp(2|3|4|e?g)|mov(ie)?|msi|msp|pdf|phps|png|ppt|qtm?|ra(m|r)?|sea|sit|tar|tgz|torrent|txt|wav|wma|wmv|wpd|xls|xml|z|zip';
  $export['piwik_trackfiles_extensions'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_translation_set';
  $strongarm->value = 0;
  $export['piwik_translation_set'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_url_http';
  $strongarm->value = 'http://stats.ampsite.net/';
  $export['piwik_url_http'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_url_https';
  $strongarm->value = 'https://stats.ampsite.net/';
  $export['piwik_url_https'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_visibility_pages';
  $strongarm->value = '0';
  $export['piwik_visibility_pages'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'piwik_visibility_roles';
  $strongarm->value = '1';
  $export['piwik_visibility_roles'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_cache_clear_all';
  $strongarm->value = 1;
  $export['shorten_cache_clear_all'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_cache_duration';
  $strongarm->value = '1814400';
  $export['shorten_cache_duration'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_cache_fail_duration';
  $strongarm->value = '1800';
  $export['shorten_cache_fail_duration'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_generate_token';
  $strongarm->value = 1;
  $export['shorten_generate_token'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_invisible_services';
  $strongarm->value = array(
    'cli.gs' => 'cli.gs',
    'Metamark' => 'Metamark',
    'migre.me' => 'migre.me',
    'PeekURL' => 'PeekURL',
    'qr.cx' => 'qr.cx',
    'redir.ec' => 'redir.ec',
    'ri.ms' => 'ri.ms',
    'is.gd' => 0,
    'TinyURL' => 0,
  );
  $export['shorten_invisible_services'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_method';
  $strongarm->value = 'curl';
  $export['shorten_method'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_service';
  $strongarm->value = 'TinyURL';
  $export['shorten_service'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_service_backup';
  $strongarm->value = 'is.gd';
  $export['shorten_service_backup'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_show_service';
  $strongarm->value = 0;
  $export['shorten_show_service'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_timeout';
  $strongarm->value = '3';
  $export['shorten_timeout'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_use_alias';
  $strongarm->value = 1;
  $export['shorten_use_alias'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'shorten_www';
  $strongarm->value = 1;
  $export['shorten_www'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'site_403';
  $strongarm->value = 'customerror/403';
  $export['site_403'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'site_404';
  $strongarm->value = 'customerror/404';
  $export['site_404'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'theme_default';
  $strongarm->value = 'ampp';
  $export['theme_default'] = $strongarm;

  $strongarm = new stdClass;
  $strongarm->disabled = FALSE; /* Edit this to true to make a default strongarm disabled initially */
  $strongarm->api_version = 1;
  $strongarm->name = 'update_scripts_directory';
  $strongarm->value = 'sites/all/modules/custom/update_scripts';
  $export['update_scripts_directory'] = $strongarm;

  return $export;
}
