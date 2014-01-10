<?php
// $Id: update.0029.php,v 1.1 2012/02/02 22:03:06 vamirbekyan Exp $


_us_install_features(array('ampp_slides'));
_us_revert_features(array('ampp_general', 'ampp_content_types', 'ampp_homepage', 'ampp_news', 'ampp_slides'));