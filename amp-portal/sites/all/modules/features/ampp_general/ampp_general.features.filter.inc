<?php
/**
 * @file
 * ampp_general.features.filter.inc
 */

/**
 * Implements hook_filter_default_formats().
 */
function ampp_general_filter_default_formats() {
  $formats = array();

  // Exported format: Plain text
  $formats['plain_text'] = array(
    'format' => 'plain_text',
    'name' => 'Plain text',
    'cache' => '1',
    'status' => '1',
    'weight' => '10',
    'filters' => array(
      'image_resize_filter' => array(
        'weight' => '-50',
        'status' => '1',
        'settings' => array(
          'link' => 0,
          'link_class' => '',
          'link_rel' => '',
          'image_locations' => array(
            'local' => 'local',
            'remote' => 0,
          ),
        ),
      ),
      'filter_htmlcorrector' => array(
        'weight' => '-48',
        'status' => '1',
        'settings' => array(),
      ),
      'filter_url' => array(
        'weight' => '-45',
        'status' => '1',
        'settings' => array(
          'filter_url_length' => '72',
        ),
      ),
      'filter_autop' => array(
        'weight' => '-44',
        'status' => '1',
        'settings' => array(),
      ),
    ),
  );

  return $formats;
}
