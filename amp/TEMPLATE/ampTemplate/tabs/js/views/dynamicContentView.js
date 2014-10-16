define([ 'marionette', 'text!views/html/dynamicContentTemplate.html', 'text!views/html/settingsDialogTemplate.html', 'models/settings',
		'business/settings/settingsManager', 'filtersWidget', 'jquery', 'bootstrap' ], function(Marionette, dynamicContentTemplate,
		settingsDialogTemplate, Settings, SettingsManager, FiltersWidget, jQuery) {

	var reportId;
	var reportFilters;
	var containerName;
	var filtersWidget;

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
		initialize : function(data) {
			reportId = data.id;
			reportFilters = data.filters;
		},
		onShow : function() {
			// Due to some incompatibilities with FilterWidget we need
			// to use a DOM element thats already present. So here we
			// change the id for this tabs' filter container.
			containerName = '#filters-container-' + reportId;
			jQuery("#filters-container").attr('id', 'filters-container-' + reportId);

			// Create the FilterWidget instance and register the events
			// for Apply and Cancel button.
			filtersWidget = new FiltersWidget({
				el : containerName,
				draggable : true
			});
			// Register apply and cancel buttons.
			this.listenTo(filtersWidget, 'cancel', function() {
				console.log('filters cancel');
				jQuery(containerName).hide();
			});
			this.listenTo(filtersWidget, 'apply', function(data) {
				console.log('filters apply');
				var filters = filtersWidget.view.serializedFilters;
				// TODO: Call the endpoint with this new filters and
				// redraw the grid.
				console.log(filters);
				jQuery(containerName).hide();
			});
			// Workaround for the problem of widget not being fully loaded with
			// data when the 'loaded' event has been triggered :(
			// So we call showFilters to load all data in the widget and
			// immediately after we hide it.
			filtersWidget.showFilters();
			jQuery(containerName).hide();
		},
		clickFiltersButton : function() {
			console.log('clickFiltersButton');

			var FilterDialogContainerView = Marionette.ItemView.extend({
				template : "<p></p>",
				render : function(model) {
					// Register a deferred to set the default tab's filters in
					// the widget (it doesnt work ok).
					filtersWidget.loaded.then(function() {
						console.log('filter widget loaded');
						// Convert report filters to filterwidget filters.
						var blob = app.TabsApp.tabUtils.convertJavaFiltersToJS(reportFilters);
						filtersWidget.deserialize(blob);
					});

					// Show the dialog and fix the position.
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
		}
	});

	return DynamicContentView;

});