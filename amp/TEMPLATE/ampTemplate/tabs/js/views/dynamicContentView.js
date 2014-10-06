define([ 'marionette', 'text!views/html/dynamicContentTemplate.html', 'text!views/html/settingsDialogTemplate.html', 'models/settings',
		'business/settings/settingsManager' ], function(Marionette, dynamicContentTemplate, settingsDialogTemplate, Settings,
		SettingsManager) {

	var reportId;

	var DynamicContentView = Marionette.LayoutView.extend({
		template : _.template(dynamicContentTemplate),
		regions : {
			filters : '#dynamic-filters-region',
			legends : '#dynamic-legends-region',
			results : '#dynamic-results-region'
		},
		events : {
			'click #filters-button' : "clickFiltersButton",
			'click #settings-button' : "clickSettingsButton"
		},
		clickFiltersButton : function() {
			console.log('clickFiltersButton');
			var FilterDialogContainerView = Marionette.ItemView.extend({
				template : "#filters-dialog-template"
			});
			var filterDialog = new FilterDialogContainerView();
			filterDialog.render();
			$(filterDialog.el).dialog({
				modal : true,
				title : 'Filters'
			});
		},
		clickSettingsButton : function() {
			console.log('clickSettingsButton');
			var SettingDialogContainerView = Marionette.ItemView.extend({
				template : _.template(settingsDialogTemplate)
			});
			// TODO: Replace some of the default values with the ones
			// for this tab.
			var settings = new Settings();
			SettingsManager.setAvailableCategories(settings, reportId);
			var settingsDialog = new SettingDialogContainerView({
				model : settings
			});
			settingsDialog.render();
			$(settingsDialog.el).dialog({
				modal : true,
				title : 'Settings',
				width : 500
			});
		},
		setId : function(id) {
			reportId = id;
		}
	});

	return DynamicContentView;

});