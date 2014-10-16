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
			},
			Donor : []
		};
		_.each(data.models, function(item, i) {
			switch (item.get('name')) {
			case 'Financing Instrument':
				blob.FinancingInstrumentsList = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Contracting Agency':
				blob['Contracting Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Executing Agency':
				blob['Executing Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Implementing Agency':
				blob['Implementing Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Beneficiary Agency':
				blob['Beneficiary Agency'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Responsible Organization':
				blob['Responsible Organization'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Type Of Assistance':
				blob['TypeOfAssistanceList'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Status':
				blob['ActivityApprovalStatus'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'National Planning Objectives':
				blob['National Plan Objective'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Primary Program':
				blob['Primary Program'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Approval Status':
				blob['ActivityStatusList'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Secondary Program':
				blob['Secondary Program'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'On/Off/Treasury Budget':
				blob['ActivityBudgetList'] = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				break;
			case 'Donor Type':
				var auxDonorTypes = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				blob.Donor = _.union(blob.Donor, auxDonorTypes);
				break;
			case 'Donor Group':
				var auxDonors = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				blob.Donor = _.union(blob.Donor, auxDonors);
				break;
			case 'Donor Agency':
				var auxDonorAgencies = _.map(item.get('values'), function(item) {
					return parseInt(item);
				});
				blob.Donor = _.union(blob.Donor, auxDonorAgencies);
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