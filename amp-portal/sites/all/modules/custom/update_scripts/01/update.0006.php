<?php
// $Id: update.0006.php,v 1.1 2012/01/06 04:22:55 vamirbekyan Exp $

// Enable modules and dependecies
_us_install_modules(array('panelizer', 'image_resize_filter', 'i18n_node', 'conditional_fields', 'insert', 'job_scheduler'));

_us_revert_features(array('ampp_general', 'ampp_content_types', 'ampp_homepage'));
