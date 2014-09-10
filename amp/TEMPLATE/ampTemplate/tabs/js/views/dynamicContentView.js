define([ 'marionette', 'text!views/html/dynamicContentTemplate.html' ], function(Marionette, dynamicContentTemplate) {

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
				template : "#settings-dialog-template"
			});
			var settingsDialog = new SettingDialogContainerView();
			settingsDialog.render();
			$(settingsDialog.el).dialog({
				modal : true,
				title : 'Settings'
			});
		}
	});

	return DynamicContentView;

});