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
						content = element.get('value');
					} else if (element.get('values') != null) {
						_.each(element.get('values').models, function(item, i) {
							content.push(item.get('value'));
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

	FilterUtils.prototype = {
		constructor : FilterUtils
	};

	return FilterUtils;
});