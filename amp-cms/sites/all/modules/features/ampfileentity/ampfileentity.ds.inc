<?php
/**
 * @file
 * ampfileentity.ds.inc
 */

/**
 * Implements hook_ds_view_modes_info().
 */
function ampfileentity_ds_view_modes_info() {
  $export = array();

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'narrow';
  $ds_view_mode->label = 'Narrow';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['narrow'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'narrow_linked';
  $ds_view_mode->label = 'Narrow Linked';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['narrow_linked'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'normal';
  $ds_view_mode->label = 'Normal';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['normal'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'normal_linked';
  $ds_view_mode->label = 'Normal Linked';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['normal_linked'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'original';
  $ds_view_mode->label = 'Original';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['original'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'thumbnail';
  $ds_view_mode->label = 'Thumbnail';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['thumbnail'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'wide';
  $ds_view_mode->label = 'Wide';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['wide'] = $ds_view_mode;

  $ds_view_mode = new stdClass();
  $ds_view_mode->api_version = 1;
  $ds_view_mode->view_mode = 'wide_linked';
  $ds_view_mode->label = 'Wide Linked';
  $ds_view_mode->entities = array(
    'file' => 'file',
  );
  $export['wide_linked'] = $ds_view_mode;

  return $export;
}
