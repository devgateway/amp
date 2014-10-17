define([ 'jquery', 'jqueryui' ], function(jQuery) {

	"use strict";

	function TabUtils() {
		if (!(this instanceof TabUtils)) {
			throw new TypeError("TabUtils constructor cannot be called as a function.");
		}
	}

	TabUtils.hideInvisibleTabs = function(tabsCollection, animate) {
		var duration = animate != undefined ? animate : 0;
		var allTabs = jQuery("#tabs-section ul li");
		_.each(tabsCollection, function(val, i) {
			if (val.get('visible') == false) {
				jQuery(allTabs[i]).hide(duration);
			}
		});
	};

	TabUtils.showInvisibleTab = function(id, animate) {
		var duration = animate != undefined ? animate : 0;
		this.hideInvisibleTabs(app.TabsApp.tabsCollection.models, 250);
		_.each(app.TabsApp.tabsCollection.models, function(item, i) {
			if (item.get('id') == id) {
				jQuery(jQuery("#tabs-section ul li")[i]).show(duration);
				jQuery(app.TabsApp.tabContainer).tabs("option", "active", i);
			}
		});
	};

	TabUtils.prototype = {
		constructor : TabUtils
	};

	return TabUtils;
});