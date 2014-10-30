define([ 'models/filter', 'collections/filters', 'jquery' ], function(Filter, Filters, jQuery) {

	"use strict";

	function FilterUtils() {
		if (!(this instanceof FilterUtils)) {
			throw new TypeError("FilterUtils constructor cannot be called as a function.");
		}
	}

	FilterUtils.extractFilters = function(content) {
		var filters = new Filters();
		var filtersJson = content.get('reportMetadata').get('reportSpec').get('filters').get('filterRules');
		jQuery(filtersJson.keys()).each(function(i, item) {
			var subElement = filtersJson.get(item);
			if (subElement instanceof Backbone.Collection) {
				if (item.indexOf('ElementType = ENTITY') > -1) {
					var name = item.substring(item.indexOf('[') + 1, item.indexOf(']'));
					var element = subElement.models[0];
					var content = [];
					if (element.get('value') != null) {
						var item = {};
						item.id = element.get('value');
						item.name = element.get('value');
						content.push(item);
					} else if (element.get('values') != null) {
						_.each(element.get('values').models, function(item_, i) {
							var item = {};
							item.id = item_.get('value');
							item.name = item_.get('value');
							content.push(item);
						});
					}

					var auxFilter = new Filter({
						name : name,
						values : content
					});
					filters.add(auxFilter);
				} else if (item.indexOf('ElementType = DATE') > -1) {

				} else if (item.indexOf('ElementType = YEAR') > -1) {

				}
			}
		});
		return filters;
	};

	// TODO: after we are sure tab's default filters are EXACTLY the same than
	// widget filters we can simplify these 2 methods into just one.
	FilterUtils.updateFiltersRegion = function(filtersFromWidget) {
		// First we cleanup current values.
		app.TabsApp.filters.models = [];
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();

		if (filtersFromWidget.columnFilters != undefined) {
			for ( var propertyName in filtersFromWidget.columnFilters) {
				var auxProperty = filtersFromWidget.columnFilters[propertyName];
				var content = [];
				_.each(auxProperty, function(item, i) {
					var auxItem = {};
					auxItem.id = item.get('id');
					auxItem.name = item.get('name');
					content.push(auxItem);
				});
				var filter = new Filter({
					name : propertyName,
					values : content
				});
				app.TabsApp.filters.models.push(filter);
			}
		}
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();
	};

	FilterUtils.prototype = {
		constructor : FilterUtils
	};

	return FilterUtils;
});