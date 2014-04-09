<?php

/**
 * @file views-export-xls-view-xls.tpl.php
 * Template to display a view as an xls file.
 *
 * - $title : The title of this group of rows.  May be empty.
 * - $rows: An array of row items. Each row is an array of content
 *   keyed by field ID.
 * - $header: an array of haeaders(labels) for fields.
 * - $themed_rows: a array of rows with themed fields.
 * @ingroup views_templates
 */
  set_time_limit(600);
  
  $path = drupal_get_path('module', 'views_export_xls');

  if (!isset($filename) || empty($filename)) {
    $filename = $view->name . '.xls';
  }

  // include xls generatator class
  require_once './' . $path . '/libs/php-excel.class.php';

  $xls = new Excel_XML('UTF-8', false, $view->name);
  $themed_rows = array_merge(array($header), $themed_rows);
  $xls->add3dArray($themed_rows);
  $xls->generateXML($file_name, true);
