<?php
/**
 * @file
 * Definition of the taxonomy term plugin.
 */

$plugin = array(
  'handler' => 'PanelizerEntityTaxonomyTerm',
  'entity path' => 'taxonomy/term/%taxonomy_term',
  'hooks' => array(
    'menu' => TRUE,
    'admin_paths' => TRUE,
    'permission' => TRUE,
    'panelizer_defaults' => TRUE,
    'default_page_manager_handlers' => TRUE,
  ),
);
