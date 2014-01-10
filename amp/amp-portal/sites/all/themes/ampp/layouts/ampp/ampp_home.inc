<?php
// $Id: ampp_home.inc,v 1.3 2012/02/06 19:17:30 vamirbekyan Exp $

// Plugin definition
$plugin = array(
  'title' => t('AMPP Homepage Layout'),
  'category' => t('AMPP'),
  'icon' => 'ampp_home.png',
  'theme' => 'ampp_home',
  'theme arguments' => array('id', 'content'),
  //'css' => 'ampp_home.css',
  'regions' => array(
    'slideshow' => t('Slideshow'),
    'search' => t('Search block'),
    'donor_profile' => t('Donor profile block'),
    'map' => t('Map block'),
    'quick_contact' => t('Quick contact'),
    'social_sharing' => t('Social sharing'),
    'news_slideshow' => t('News slideshow'),
  ),
);
