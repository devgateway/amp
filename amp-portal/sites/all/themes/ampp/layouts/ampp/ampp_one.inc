<?php
// $Id: ampp_one.inc,v 1.2 2012/01/03 19:56:12 vamirbekyan Exp $

// Plugin definition
$plugin = array(
  'title' => t('AMPP One Column Layout'),
  'category' => t('AMPP'),
  'icon' => 'ampp_one.png',
  'theme' => 'ampp_one',
  'theme arguments' => array('id', 'content'),
  //'css' => 'ampp_one.css',
  'regions' => array(
    'header' => t('Header'),
    'col_1' => t('Column 1'),
    'footer' => t('Footer'),
  ),
);
