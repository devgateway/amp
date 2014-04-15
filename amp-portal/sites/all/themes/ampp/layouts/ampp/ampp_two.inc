<?php
// $Id: ampp_two.inc,v 1.2 2012/01/03 19:56:52 vamirbekyan Exp $

// Plugin definition
$plugin = array(
  'title' => t('AMPP Two Columns Layout'),
  'category' => t('AMPP'),
  'icon' => 'ampp_two.png',
  'theme' => 'ampp_two',
  'theme arguments' => array('id', 'content'),
  //'css' => 'ampp_two.css',
  'regions' => array(
    'header' => t('Header'),
    'col_1' => t('Column 1'),
    'col_2' => t('Column 2'),
    'footer' => t('Footer'),
  ),
);
