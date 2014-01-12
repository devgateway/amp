<?php
// $Id: update.0022.php,v 1.1 2012/01/24 00:02:13 vamirbekyan Exp $

_us_uninstall_features(array('ampp_feeds_news'));
_us_revert_features(array('ampp_general', 'ampp_content_types', 'ampp_homepage'));
