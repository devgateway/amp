<?php
// $Id: ampp-two-23.inc,v 1.1 2012/01/28 10:37:00 eabrahamyan Exp $

// Plugin definition
$plugin = array(
  'title' => t('AMPP Two-23 Columns Layout'),
  'category' => t('AMPP'),
  'icon' => 'ampp-two-23.png',
  'theme' => 'ampp-two-23',
  'theme arguments' => array('id', 'content'),
  //'css' => 'ampp-two-23.css',
  'regions' => array(
    'header' => t('Header'),
    'col_2' => t('Column 2'),
    'col_3' => t('Column 3'),
    'footer' => t('Footer'),
  ),
);
