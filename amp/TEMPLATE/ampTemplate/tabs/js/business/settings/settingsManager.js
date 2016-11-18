define(['marionette', 'translationManager',
		'business/grid/gridManager', 'models/legend', 'jquery','settings'], function(Marionette,
		TranslationManager, GridManager, Legend, jQuery, Settings) {

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
		app.TabsApp.settingsWidget = new Settings.SettingsWidget({
			el : '#settings-popup',
			draggable : true,
			caller : 'TABS',
			isPopup : true,
			definitionUrl : '/rest/settings-definitions/gis'
		});

		app.TabsApp.listenTo(app.TabsApp.settingsWidget, 'close', function() {
			$('#settings-popup').hide();
		});

		app.TabsApp.listenTo(app.TabsApp.settingsWidget, 'applySettings', function() {
			GridManager.filter(app.TabsApp.currentTab.get('id'), app.TabsApp.serializedFilters, app.TabsApp.settingsWidget.toAPIFormat());
			$('#settings-popup').hide();
		});
		
		app.TabsApp.generalSettings = new Settings.GeneralSettings();
		app.TabsApp.generalSettings.load()
	};

	SettingsManager.openDialog = function() {		
		$('#settings-popup').show();
		app.TabsApp.settingsWidget.show();
	};

	return SettingsManager;
});