<?php
// $Id: update.0023.php,v 1.1 2012/01/25 04:12:59 vamirbekyan Exp $

_us_install_features(array('ampp_news'));
_us_revert_features(array('ampp_general', 'ampp_content_types', 'ampp_homepage', 'ampp_news'));
