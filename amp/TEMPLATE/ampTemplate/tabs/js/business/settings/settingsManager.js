define([ 'marionette', 'text!views/html/settingsDialogTemplate.html', 'business/translations/translationManager',
		'business/grid/gridManager', 'models/legend', 'jquery', 'jqueryui' ], function(Marionette, settingsDialogTemplate,
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

	SettingsManager.setAvailableCategories = function(settings, id) {
		$.ajax({
			url : '/rest/data/report/' + id + '/settings',
			async : false
		}).done(function(data) {
			return settings;
		});
	};

	SettingsManager.openDialog = function() {
		var SettingDialogContainerView = Marionette.ItemView.extend({
			template : _.template(settingsDialogTemplate)
		});
		var settings = app.TabsApp.settings;
		// TODO: Replace some of the default values with the ones
		// for this tab.
		if (app.TabsApp.appliedSettings != null) {
			settings.set('selectedCurrency', app.TabsApp.appliedSettings["1"]);
		}
		if (app.TabsApp.appliedSettings != null) {
			settings.set('selectedCalendar', app.TabsApp.appliedSettings["2"]);
		}
		// SettingsManager.setAvailableCategories(settings, reportId);
		var settingsDialog = new SettingDialogContainerView({
			model : settings
		});
		settingsDialog.render();
		jQuery(settingsDialog.el).dialog({
			modal : true,
			title : TranslationManager.getTranslated("Settings"),
			width : 300
		});
		jQuery(".buttonify").button();
		TranslationManager.searchAndTranslate();

		// Register apply button click.
		jQuery(".settings-container #applySettingsBtn").on("click", function() {
			jQuery('#calendar').removeAttr('selected');
			jQuery('#currency').removeAttr('selected');
			jQuery(settingsDialog.el).dialog('close');

			/* settingsDialog.model.get('currency'),settingsDialog.model.get('calendar') */

			// Save selected settings for later.
			app.TabsApp.appliedSettings = {
				"1" : jQuery("#currency").val(),
				"2" : jQuery("#calendar").val()
			};
			GridManager.filter(app.TabsApp.currentTab.get('id'), app.TabsApp.serializedFilters, app.TabsApp.appliedSettings);

			// Update the legend section.
			var legend = new Legend({
				currencyCode : app.TabsApp.appliedSettings["1"],
				id : null
			});
			app.TabsApp.dynamicContentRegion.currentView.legends.currentView.model = legend;
			app.TabsApp.dynamicContentRegion.currentView.legends.currentView.render();

			// Destroy the dialog to unbind the event.
			jQuery(settingsDialog.el).dialog('destroy');
		});
	};

	return SettingsManager;
});