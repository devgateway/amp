define([], function() {

	"use strict";

	function SettingsManager() {
		if (!(this instanceof SettingsManager)) {
			throw new TypeError("SettingsManager constructor cannot be called as a function.");
		}
	}

	SettingsManager.prototype = {
		constructor : SettingsManager
	};

	SettingsManager.setAvailableCategories = function(settings, id) {
		$.ajax({
			url : '/rest/data/report/' + id + '/settings',
			async : false
		}).done(function(data) {
			// TODO: add the rest of the options later.
			settings.set('decimalSeparators', data.decimalSeparators);
			settings.set('selectedDecimalPlaces', data.selectedDecimalPlaces);
			settings.set('selectedDecimalSeparator', data.selectedDecimalSeparator);
			settings.set('groupSeparators', data.decimalSeparators);
			settings.set('selectedGroupSeparator', data.selectedGroupSeparator);
			settings.set('selectedUseGroupingSeparator', data.selectedUseGroupingSeparator);
			return settings;
		});
	};

	return SettingsManager;
});