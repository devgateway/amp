define([ 'jquery', 'jqueryui', 'jqgrid' ], function(jQuery) {

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

	TabUtils.shortenTabNames = function(tabs) {
		var maxChars = 20;
		_.each(tabs, function(item, i) {
			var name = item.get('name');
			// Ignore "more tabs" tab.
			if (name.length > maxChars && item.get('id') != -1) {
				item.set('shortName', name.substring(0, maxChars) + '...');
			} else {
				item.set('shortName', name);
			}
		});
	};

	/**
	 * Resize the grid when the container grows/shrinks.
	 * 
	 * @param id
	 * @param originalWidth
	 * @param grow
	 */
	TabUtils.resizePanel = function(id, originalWidth, grow) {
		try {
			var newWidth = 0;
			if (grow) {
				newWidth = jQuery("#tabs-" + id).width();
			} else {
				newWidth = originalWidth;
			}
			console.log(newWidth);
			jQuery("#tab_grid_" + id).jqGrid().setGridWidth(newWidth, true);
		} catch (err) {
			console.error(err);
		}
	};

	TabUtils.prototype = {
		constructor : TabUtils
	};

	return TabUtils;
});