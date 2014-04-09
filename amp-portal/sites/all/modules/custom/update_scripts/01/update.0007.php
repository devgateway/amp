<?php
// $Id: update.0007.php,v 1.2 2012/01/10 19:56:09 vamirbekyan Exp $

_us_install_features(array('ampp_roles_and_permissions'));
_us_revert_features(array('ampp_general', 'ampp_content_types', 'ampp_homepage', 'ampp_roles_and_permissions'));
