<?php
// $Id: update.0063.php,v 1.1 2012/11/30 13:02:27 apetrosyan Exp $

// Prepare an array of modules to be enabled.
_us_revert_feature('ampp_general');
_us_revert_feature('ampp_projects');
_us_revert_feature('ampp_news');
_us_revert_feature('ampp_homepage');

// Enable modules and dependecies
