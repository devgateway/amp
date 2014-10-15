define([ 'marionette', 'text!views/html/dynamicContentTemplate.html', 'text!views/html/settingsDialogTemplate.html', 'models/settings',
		'business/settings/settingsManager', 'filtersWidget', 'jquery', 'bootstrap' ], function(Marionette, dynamicContentTemplate,
		settingsDialogTemplate, Settings, SettingsManager, FiltersWidget, jQuery) {

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
			jQuery("#filters-container").attr('id', 'filters-container-' + reportId);
			var FilterDialogContainerView = Marionette.ItemView.extend({
				template : "<p></p>",
				render : function(model) {
					var containerName = '#filters-container-' + reportId;
					var filtersWidget = new FiltersWidget({
						el : containerName,
						draggable : true,
						translator : null
					});
					filtersWidget.showFilters();
					jQuery(containerName).show();
					jQuery(containerName).css('position', 'absolute');
					jQuery(containerName).css('top', 10);
				}
			});
			var filterDialog = new FilterDialogContainerView();
			filterDialog.render();
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
			jQuery(settingsDialog.el).dialog({
				modal : true,
				title : 'Settings',
				width : 500
			});
			jQuery(".buttonify").button();
		},
		setId : function(id) {
			reportId = id;
		}
	});

	return DynamicContentView;

});