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
				app.TabsApp.tabsCollection.models[i].set('isOtherTabNowVisible', false);
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
				app.TabsApp.tabsCollection.models[i].set('isOtherTabNowVisible', true);
			}
		});
	};

	/**
	 * This ugly method will solve the known problem of latest jqueryui tabs +
	 * <base> tag by changing the <a href> when it points to a local "#"
	 * address.
	 * 
	 * @param selector
	 * @param options
	 */
	TabUtils.createTabs = function(selector, options) {
		jQuery(selector).find("ul a").each(
				function() {
					var href = jQuery(this).attr("href");
					var newHref = window.location.protocol + '//' + window.location.hostname + (location.port ? ':' + location.port : '')
							+ window.location.pathname + href;
					if (href.indexOf("#") == 0) {
						jQuery(this).attr("href", newHref);
					}
				});
		jQuery(selector).tabs(options);
	};

	TabUtils.prototype = {
		constructor : TabUtils
	};

	return TabUtils;
});