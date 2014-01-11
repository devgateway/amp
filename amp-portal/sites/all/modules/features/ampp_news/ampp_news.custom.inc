<?php

/*
 * implementation hook_ctools_plugin_directory
 */
function ampp_news_ctools_plugin_directory($owner, $plugin_type) {
	if($owner == 'ctools' && $plugin_type == 'content_types') {
		return 'plugins/content_types';
	}
}