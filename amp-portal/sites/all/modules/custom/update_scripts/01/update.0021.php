<?php
// $Id: update.0021.php,v 1.2 2012/01/22 05:58:08 vamirbekyan Exp $

_us_uninstall_modules(array('feeds_news'));
_us_uninstall_features(array('feeds_news'));

_us_install_features(array('ampp_feeds_news'));
_us_revert_features(array('ampp_general', 'ampp_content_types', 'ampp_feeds_news'));
