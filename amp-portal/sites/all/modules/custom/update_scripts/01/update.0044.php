<?php
// $Id: update.0044.php,v 1.1 2012/03/04 20:46:37 lananikyan Exp $
_us_disable_module('admin_menu_toolbar');
_us_disable_module('admin_menu');
_us_install_module('toolbar');
_us_install_module('shortcut');

_us_revert_features(array('ampp_roles_and_permissions'));