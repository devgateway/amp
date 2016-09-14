define([ 'models/filter', 'collections/filters', 'business/translations/translationManager', 'jquery' ], function(Filter, Filters, TranslationManager, jQuery) {

	"use strict";

	function FilterUtils() {
		if (!(this instanceof FilterUtils)) {
			throw new TypeError("FilterUtils constructor cannot be called as a function.");
		}
	}
	
	FilterUtils.extractSorters = function(content, columns, measures) {
		var sorting = {};
		if (content !== null && content.models !== null) {
			sorting.sord = content.models[0].get('ascending') ? "asc" : "desc";
			var auxTuple = content.models[0].get('hierPath');
			if (_.isUndefined(auxTuple) === false) {
				for (var k in auxTuple.models) {
					var value = auxTuple.models[k].get('value');
					sorting.sidx = value;
				}
			}
		}
		// Added for AMP-22511: We need to cleanup the sorters if they are defined over a column no longer in the report.
		if (sorting.sidx !== undefined) {
			var sidxColumns = sorting.sidx.split(',');
			// Iterate saved columns for sorting.
			_.each(sidxColumns, function(item) {
				// Look for this column on colums and measures.
				if (_.find(columns.models, function (item2) {return item.trim() === item2.get('entityName')}) === undefined 
						&& _.find(measures.models, function (item2) {return item.trim() === item2.get('entityName')}) === undefined) {
					console.log("Reset sorting.");
					sorting.sidx = undefined;
					sorting.sord = undefined;
				}
			});
		}
		return sorting;
	}

	//TODO: move to CommonFilterUtils.js and merge with the same function on Saiku.
	FilterUtils.extractFilters = function(content) {
		var filters = new Filters();
		var filtersColumnsJson = content.get('columnFilterRules');
		jQuery(filtersColumnsJson.keys()).each(function(i, item) {
			var subElement = filtersColumnsJson.get(item);
			if (subElement instanceof Backbone.Collection) {

				var element = subElement.models[0];
				var content = [];
				if (element.get('value') !== null) {
					var auxItem = {};
					auxItem.id = element.get('value');
					auxItem.name = element.get('valueToName').attributes[element.get('value')];
					content.push(auxItem);
				} else if (element.get('valueToName') !== null) {
					// This should be .models but the way the endpoint returns
					// the data breaks backbone.
					var foundValueToName = false;
					_.each(element.get('valueToName').attributes, function(item_, i) {
						// Need to do this because of how js parses these data
						// and adds an extra element.
						if (i !== undefined && item_ !== undefined) {
							var item = {};
							item.id = i;
							item.name = item_;
							if (subElement.name === 'DATE') {
								item.dateIntervalType = (element.get('max') === item.id ? 'max' : 'min');
							}
							content.push(item);
							foundValueToName = true;
						}
					});
					if (!foundValueToName) {
						// This is a special case for usually for boolean filters with YES and/or NO answer.						
						_.each(element.get('values').models, function(item_, i) {
							if (i !== undefined && item_ !== undefined) {
								var item = {};
								item[i] = item_.get('value');
								content = item;
							}
						});
					}
				}
				
				//translate filter values
				_.each(content,function(item, i) {
					//for now only true or false were asked to be translated. 
					//Avoid doing a ajax call for all values if we only need 2.
					if (item.name === "true" || item.name === "false") {
						item.trnName = TranslationManager.getTranslated(item.name);
					 }
					else {
						if (item instanceof Array) {
							item.trnName = item.name;							
						} else {
							// This is a special case for boolean filters like Humanitarian Aid that have a different structure so we cant translate anything, just have 0/1.
							// This forces us to translate 0/1 into Yes/No elsewhere because we cant do "1".trnName = 'Yes'
						}
					}
				});
				var auxFilter = new Filter({
					trnName : TranslationManager.getTranslated(item),
					name: item,
					values : content
				});
				filters.add(auxFilter);
			}
		});

		var filtersDateColumnsJson = content.get('columnDateFilterRules');
		jQuery(filtersDateColumnsJson.keys()).each(function(i, item) {
			var subElement = filtersDateColumnsJson.get(item);
			if (subElement instanceof Backbone.Collection) {
				var element = subElement.models[0];
				var content = [];	
				if (element.get('value') !== null) {
					var auxItem = {};
					auxItem.id = element.get('value');
					auxItem.name = element.get('valueToName').attributes[element.get('value')];
					content.push(auxItem);
				} else if (element.get('valueToName') !== null) {
					// This should be .models but the way the endpoint returns
					// the data breaks backbone.
					_.each(element.get('valueToName').attributes, function(j, item2) {
						// Need to do this because of how js parses these data
						// and adds an extra element.
						if (j !== undefined) {
							var item_ = {};
							item_.id = i;
							item_.name = j;
							content.push(item_);
						}
					});
				}
				var auxFilter = new Filter({
					trnName : TranslationManager.getTranslated(item),
					name: item,
					values : content
				});
				filters.add(auxFilter);
			}
		});
		
		//Process filters that dont come inside the previous categories (ie: computed year).
		if (content.get('computedYear')) {
			var values = [];
			values.push({
				id : content.get('computedYear'),
				name : content.get('computedYear'),
				trnName : content.get('computedYear')
			});
			var auxFilter = new Filter({
				trnName : TranslationManager.getTranslated('Computed Year'),
				name: 'computedYear',
				values : values
			}); 
			filters.add(auxFilter);
		}
		
		return filters;
	};

	// TODO: after we are sure tab's default filters are EXACTLY the same than
	// widget filters we can simplify these 2 methods into just one.
	FilterUtils.updateFiltersRegion = function(filtersFromWidgetWithNames) {
		app.TabsApp.filters.models = [];
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();
		
		if (filtersFromWidgetWithNames.columnFilters !== undefined) {
			for ( var propertyName in filtersFromWidgetWithNames.columnFilters) {
				var auxProperty = filtersFromWidgetWithNames.columnFilters[propertyName];				
				_.each(auxProperty.serializedToModels, function(item, i) {
					var content = [];
					if (item.length > 0) {
						_.each(item, function(item2) {
							content.push({id: 0, name: item2.name, trnName: TranslationManager.getTranslated(item2.name)});
						});
						var name = TranslationManager.getTranslated(item[0].levelName) || item[0].levelName;
						var filter = new Filter({
							trnName : name,
							name: item[0].levelName,
							values : content
						});
						app.TabsApp.filters.models.push(filter);
					}
				});				
			}
		}
		if (filtersFromWidgetWithNames.otherFilters !== undefined) {
			for ( var propertyName in filtersFromWidgetWithNames.otherFilters) {
				var dateContent = filtersFromWidgetWithNames.otherFilters[propertyName];
				if (dateContent !== undefined) {
					var filterObject = {
							trnName : TranslationManager.getTranslated(propertyName),
							name : propertyName,
							values: []
					};
					if (dateContent.modelType === 'DATE-RANGE-VALUES') {
						dateContent.start =  dateContent.start || "" ;
						dateContent.end =  dateContent.end || "" ;	
	
						var startDatePrefix = (dateContent.start.length > 0 && dateContent.end.length === 0) ? "from " : "";
						var endDatePrefix = (dateContent.start.length === 0 && dateContent.end.length > 0) ? "until " : "";
						if(dateContent.start.length > 0){
							filterObject.values.push({
								id : dateContent.start,
								name : dateContent.start,
								trnName : TranslationManager.getTranslated(startDatePrefix) + app.TabsApp.filtersWidget.formatDate(dateContent.start)
							});
						}
						if(dateContent.end.length > 0){
							filterObject.values.push({
								id : dateContent.end,
								name : dateContent.end,
								trnName : TranslationManager.getTranslated(endDatePrefix) + app.TabsApp.filtersWidget.formatDate(dateContent.end)
	
							});	
						}															 
					} else if (dateContent.modelType === 'YEAR-SINGLE-VALUE') {
						dateContent.year = dateContent.year || '';
						filterObject.values.push({
							id : dateContent.year,
							name : dateContent.year,
							trnName : dateContent.year
						});
						filterObject.trnName = dateContent.displayName;
					}
					var filter = new Filter(filterObject);
					app.TabsApp.filters.models.push(filter);
				}
			}
		}
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();
	};

	//copypasted from years-filter-model.js
	FilterUtils._dateConvert = function(input){
		var output = null;
		if(input){
			if(input.indexOf('/')>-1){
				input = input.split('/');
				output = input[2] + '-' + input[1] + '-' + input[0];
			} else if(input.indexOf('-')>-1){
				input = input.split('-');
				output = input[2] + '/' + input[1] + '/' + input[0];
			}
		}
		return output;
	};	
	  
	FilterUtils.pushDateLimit = function(_name, value){
	if(app.TabsApp.filters.models[_name] === undefined) {
		var filter = new Filter({
			name : _name,
			values : [ {
				id : value,
				name : value
			} ]
		});
		app.TabsApp.filters.models.push(filter);
	} else {
		app.TabsApp.filters.models[_name].id = value;
		app.TabsApp.filters.models[_name].name = value;
	}
	
  };
	  
	FilterUtils.widgetFiltersToJavaFilters = function(originalFilters) {
		return originalFilters;
	};
	
	FilterUtils.julianToDate = function(julian) {
		var X = parseFloat(julian) + 0.5;
		var Z = Math.floor(X); // Get day without time
		var F = X - Z; // Get time
		var Y = Math.floor((Z - 1867216.25) / 36524.25);
		var A = Z + 1 + Y - Math.floor(Y / 4);
		var B = A + 1524;
		var C = Math.floor((B - 122.1) / 365.25);
		var D = Math.floor(365.25 * C);
		var G = Math.floor((B - D) / 30.6001);
		// must get number less than or equal to 12)
		var month = (G < 13.5) ? (G - 1) : (G - 13);
		// if Month is January or February, or the rest of year
		var year = (month < 2.5) ? (C - 4715) : (C - 4716);
		month -= 1; // Handle JavaScript month format
		var UT = B - D - Math.floor(30.6001 * G) + F;
		var day = Math.floor(UT);
		// Determine time
		UT -= Math.floor(UT);
		UT *= 24;
		var hour = Math.floor(UT);
		UT -= Math.floor(UT);
		UT *= 60;
		var minute = Math.floor(UT);
		UT -= Math.floor(UT);
		UT *= 60;
		var second = Math.round(UT);
		return new Date(year, month, day);
		//return new Date(Date.UTC(year, month, day, hour, minute, second));
	};

	FilterUtils.prototype = {
		constructor : FilterUtils
	};

	return FilterUtils;
});
