<?php
// $Id: ampp_three.inc,v 1.2 2012/01/03 19:56:55 vamirbekyan Exp $

// Plugin definition
$plugin = array(
  'title' => t('AMPP Three Columns Layout'),
  'category' => t('AMPP'),
  'icon' => 'ampp_three.png',
  'theme' => 'ampp_three',
  'theme arguments' => array('id', 'content'),
  //'css' => 'ampp_three.css',
  'regions' => array(
    'header' => t('Header'),
    'col_1' => t('Column 1'),
    'col_2' => t('Column 2'),
    'col_3' => t('Column 3'),
    'footer' => t('Footer'),
  ),
);
