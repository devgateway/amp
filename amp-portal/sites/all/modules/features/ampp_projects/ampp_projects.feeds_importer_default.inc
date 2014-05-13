<?php
/**
 * @file
 * ampp_projects.feeds_importer_default.inc
 */

/**
 * Implements hook_feeds_importer_default().
 */
function ampp_projects_feeds_importer_default() {
  $export = array();

  $feeds_importer = new stdClass;
  $feeds_importer->disabled = FALSE; /* Edit this to true to make a default feeds_importer disabled initially */
  $feeds_importer->api_version = 1;
  $feeds_importer->id = 'top_activities';
  $feeds_importer->config = array(
    'name' => 'Top activities by disbursement import',
    'description' => '',
    'fetcher' => array(
      'plugin_key' => 'FeedsHTTPFetcher',
      'config' => array(
        'auto_detect_feeds' => FALSE,
        'use_pubsubhubbub' => FALSE,
        'designated_hub' => '',
      ),
    ),
    'parser' => array(
      'plugin_key' => 'ExcelParser',
      'config' => array(
        'mapping_mode' => 'rows',
        'sheets' => '',
        'header' => '',
        'fixed' => 'B:F,H:H',
        'iterative' => '3:10000',
      ),
    ),
    'processor' => array(
      'plugin_key' => 'FeedsNodeProcessor',
      'config' => array(
        'content_type' => 'top_activities_by_disbursement',
        'expire' => '-1',
        'author' => 0,
        'mappings' => array(
          0 => array(
            'source' => '[excel-row:x-2]',
            'target' => 'title',
            'unique' => FALSE,
          ),
          1 => array(
            'source' => '[excel-row:x-3]',
            'target' => 'field_primary_sector',
            'unique' => FALSE,
          ),
          2 => array(
            'source' => '[excel-row:x-4]',
            'target' => 'field_donor',
            'unique' => FALSE,
          ),
          3 => array(
            'source' => '[excel-row:x-5]',
            'target' => 'field_da_last_period',
            'unique' => FALSE,
          ),
          4 => array(
            'source' => '[excel-row:x-6]',
            'target' => 'field_dp_last_period',
            'unique' => FALSE,
          ),
          5 => array(
            'source' => '[excel-row:x-8]',
            'target' => 'field_dp_current_period',
            'unique' => FALSE,
          ),
        ),
        'update_existing' => '1',
        'input_format' => 'plain_text',
      ),
    ),
    'content_type' => '',
    'update' => 0,
    'import_period' => '86400',
    'expire_period' => 3600,
    'import_on_create' => 1,
    'process_in_background' => 0,
  );
  $export['top_activities'] = $feeds_importer;

  return $export;
}
