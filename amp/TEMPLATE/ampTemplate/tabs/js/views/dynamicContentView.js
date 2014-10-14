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
			var FilterDialogContainerView = Marionette.ItemView.extend({
				template : "<b>nada</b>",
				render : function(model) {
					// alert(this.$el);
					this.$el.append("<div id='filters-dialog-container'>nada4</div>");
				}
			});
			var filterDialog = new FilterDialogContainerView();
			filterDialog.render();
			jQuery(filterDialog.el).dialog({
				modal : true,
				title : 'Filters',
				width : '900',
				height : '550'
			});

			app.TabsApp.filtersWidget = new FiltersWidget({
				el : jQuery('#filters-dialog-container'),
				draggable : true,
				translator : null
			});
			app.TabsApp.filtersWidget.loaded.then(function() {
				// debugger
				var self = this;
				/*
				 * self.state.register(self, 'filters', { get : function() {
				 * return self.serialize(); }, set : function(state) { return
				 * self.deserialize(state); }, empty : null });
				 */
			});
			app.TabsApp.filtersWidget.showFilters();
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