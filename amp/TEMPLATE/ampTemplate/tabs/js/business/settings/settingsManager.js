define([ 'marionette', 'text!views/html/settingsDialogTemplate.html', 'business/translations/translationManager',
		'business/grid/gridManager', 'models/legend', 'jquery', 'jqueryui' ], function(Marionette, settingsDialogTemplate,
		TranslationManager, GridManager, Legend, jQuery) {

	"use strict";
	
	var initialized = false;

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
	
	SettingsManager.initialize = function() {
		var self = this;
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
			width : 'auto',
			position: { my: "center bottom", at: "center center", of: window }
		});
		jQuery(".buttonify").button();
		$('#settings-missing-values-error').hide();
		TranslationManager.searchAndTranslate();
		
		this.initialized = true;

		// Register apply button click.
		jQuery(".settings-container #applySettingsBtn").on("click", function() {
			if($('#currency').val() === null || $('#calendar').val() === null) {
				$('#settings-missing-values-error').show();
				TranslationManager.searchAndTranslate();
				return false;
			}
			
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

            var currentLegendModel = app.TabsApp.dynamicContentRegion.currentView.legends.currentView.model;
            var unitsValue = currentLegendModel.attributes.units;
            var currencyValue = _.findWhere(app.TabsApp.settings.get('0').options, {id: app.TabsApp.appliedSettings["1"]}).value;

			// Update the legend section.
			var legend = new Legend({
				currencyCode : app.TabsApp.appliedSettings["1"],
				currencyValue : currencyValue,
				id : null,
				units : unitsValue
			});
			app.TabsApp.dynamicContentRegion.currentView.legends.currentView.model = legend;
			app.TabsApp.dynamicContentRegion.currentView.legends.currentView.render();

			// Destroy the dialog to unbind the event.
			jQuery(settingsDialog.el).dialog('destroy').remove();
			self.initialized = false;
		});
		
		//Register calendar change.
		jQuery(".settings-container #calendar").on("change", function() {
			var calendarSelect = $('.settings-container #calendar');
			var currencySelect = $('.settings-container #currency');
			var selectedCalendar = $(calendarSelect).val();				
			var calendarCurrencies = _.find(app.TabsApp.settings.attributes, function(item) {return item.id === 'calendarCurrencies'}).options;
			//Note: uniq is used because the list has duplicated currencies.
			var availableCurrenciesForCalendar = _.uniq(_.findWhere(calendarCurrencies, {id: selectedCalendar}).value.split(","));
			var allCurrencies = _.find(app.TabsApp.settings.attributes, function(item) {return item.id === '1'}).options;
			$(currencySelect).empty();
			$.each(availableCurrenciesForCalendar, function(index, object) {   
				$(currencySelect)
			         .append($("<option></option>")
			         .attr("value", object)
			         .text(_.find(allCurrencies, function(item){return item.id === object}).name)); 
			});
			$(currencySelect).val(availableCurrenciesForCalendar[0]);
		});
		jQuery(settingsDialog.el).dialog('close');
		app.TabsApp.settingsDialogView = settingsDialog;
	};

	SettingsManager.openDialog = function() {
		if (this.initialized === false) {
			// By re-initialing we prevent some parameters ghosting.
			this.initialize();
		}
		jQuery(app.TabsApp.settingsDialogView.el).dialog('open');
	};

	return SettingsManager;
});