define(function() {

	"use strict";

	function TabUtils() {
		if (!(this instanceof TabUtils)) {
			throw new TypeError("TabUtils constructor cannot be called as a function.");
		}
	}

	TabUtils.hideInvisibleTabs = function(tabsCollection) {
		var allTabs = $("#tabs-section ul li");
		_.each(tabsCollection, function(val, i) {
			if (val.get('visible') == false) {
				$(allTabs[i]).hide();
			}
		});
	};

	TabUtils.showInvisibleTab = function(id) {
		this.hideInvisibleTabs(app.TabsApp.tabsCollection.models);
		_.each(app.TabsApp.tabsCollection.models, function(item, i) {
			if (item.get('id') == id) {
				$($("#tabs-section ul li")[i]).show();
				$(app.TabsApp.tabContainer).tabs("option", "active", i);
			}
		});
	};

	TabUtils.prototype = {
		constructor : TabUtils
	};

	return TabUtils;
});