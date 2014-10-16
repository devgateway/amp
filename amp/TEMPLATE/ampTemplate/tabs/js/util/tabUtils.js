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

	TabUtils.convertJavaFiltersToJS = function(data) {
		// Define some basic defaults needed in the widget filter.
		var blob = {
			Years : {
				startYear : 1998,
				endYear : 2020
			}
		};
		_.each(data.models, function(item, i) {
			switch (item.get('name')) {
			case 'Financing Instrument':
				blob.FinancingInstrumentsList = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Donor Group':
				blob.Donor = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			}
		});

		console.log(blob);
		return blob;
	};

	TabUtils.prototype = {
		constructor : TabUtils
	};

	return TabUtils;
});