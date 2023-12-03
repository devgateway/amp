define(['marionette', 'translationManager',
		'business/grid/gridManager', 'models/legend', 'jquery'], function(Marionette,
		TranslationManager, GridManager, Legend, jQuery) {

	"use strict";
	
	function SettingsManager() {
		if (!(this instanceof SettingsManager)) {
			throw new TypeError("SettingsManager constructor cannot be called as a function.");
		}
	}

	SettingsManager.prototype = {
		constructor : SettingsManager
	};

	SettingsManager.initialize = function() {
		app.TabsApp.settingsWidget = new AMPSettings.SettingsWidget({
			el : '#settings-popup',
			draggable : true,
			caller : 'TABS',
			isPopup : true,
			definitionUrl : '/rest/settings-definitions/tabs'
		});

		app.TabsApp.listenTo(app.TabsApp.settingsWidget, 'close', function() {
			$('#settings-popup').hide();
		});

		app.TabsApp.listenTo(app.TabsApp.settingsWidget, 'applySettings', function() {
			app.TabsApp.serializedFilters = app.TabsApp.filtersWidget.serialize() || {};
			GridManager.filter(app.TabsApp.currentTab.get('id'), app.TabsApp.serializedFilters.filters, app.TabsApp.settingsWidget.toAPIFormat());
			SettingsManager.updateLegend(app.TabsApp.settingsWidget.toAPIFormat());
			$('#settings-popup').hide();
		});
		
		app.TabsApp.generalSettings = new AMPSettings.GeneralSettings();
		app.TabsApp.generalSettings.load()
	};

	SettingsManager.openDialog = function() {		
		$('#settings-popup').show();
		app.TabsApp.settingsWidget.show();
	};
	
	SettingsManager.updateLegend = function(appliedSettings){
		// Update the legend section.
		var currentLegendModel = app.TabsApp.dynamicContentRegion.currentView.legends.currentView.model;
        var unitsValue = currentLegendModel.attributes.units;
		var currencyCode = appliedSettings[app.TabsApp.settingsWidget.Constants.CURRENCY_ID];
		var currencyValue = app.TabsApp.settingsWidget.definitions.findCurrencyById(currencyCode).value;
		var legend = new Legend({
			currencyCode : currencyCode,
			currencyValue : currencyValue,
			id : null,
			units : unitsValue
		});
		app.TabsApp.dynamicContentRegion.currentView.legends.currentView.model = legend;
		app.TabsApp.dynamicContentRegion.currentView.legends.currentView.render();
	};

	return SettingsManager;
});