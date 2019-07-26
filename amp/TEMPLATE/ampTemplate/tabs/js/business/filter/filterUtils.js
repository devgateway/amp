define([ 'models/filter', 'collections/filters', 'translationManager', 'jquery' ], function(Filter, Filters, TranslationManager, jQuery) {

	"use strict";

	function FilterUtils() {
		if (!(this instanceof FilterUtils)) {
			throw new TypeError("FilterUtils constructor cannot be called as a function.");
		}
	}
	
	FilterUtils.extractSorters = function(content, columns, measures, hierarchies) {
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
		} else {
			sorting.sord = "asc";
			sorting.sidx = "";
			_.each(columns.models, function(column) {
				if (_.find(hierarchies.models, function (item) {return item.get('entityName').trim() === column.get('entityName');}) === undefined) {
					sorting.sidx += ( sorting.sidx == "" ? "" : ", " ) + column.get('entityName');
				}
			});

		}
		// Added for AMP-22511: We need to cleanup the sorters if they are defined over a column no longer in the report.
		if (sorting.sidx !== undefined) {
			var sidxColumns = sorting.sidx.split(',');
			// Iterate saved columns for sorting.
			_.each(sidxColumns, function(item) {
				// Look for this column on colums and measures.
				if (_.find(columns.models, function (item2) {return item.trim() === item2.get('entityName')}) === undefined 
						&& _.find(measures.models, function (item2) {return item.trim() === item2.get('entityName')}) === undefined) {
					
					sorting.sidx = undefined;
					sorting.sord = undefined;
				}
			});
		}
		return sorting;
	}

	//TODO: move to CommonFilterUtils.js and merge with the same function on Saiku.
	FilterUtils.extractFilters = function(filtersFromWidgetWithNames) {
		if(app.TabsApp.filters == undefined){
			app.TabsApp.filters = new Filters();
		}
		
		app.TabsApp.filters.models = [];
		
		if (filtersFromWidgetWithNames.filters !== undefined) {
			for ( var propertyName in filtersFromWidgetWithNames.filters) {
				var auxProperty = filtersFromWidgetWithNames.filters[propertyName];	
				if(auxProperty.modelType === 'DATE-RANGE-VALUES' || auxProperty.modelType === 'YEAR-SINGLE-VALUE'){
					var dateFilter = FilterUtils._extractDateFilter(propertyName, auxProperty);
					if(dateFilter){
						app.TabsApp.filters.add(dateFilter);
					}
				}else{
					_.each(auxProperty.serializedToModels, function(item, i) {
						var content = [];
						if (item.length > 0) {
							var names = [];
							_.each(item, function(item2) {
								names.push(item2.name);
							});
							var translatedNames = TranslationManager.getTranslated(names);
							_.each(item, function(item2, j) {
								// Items.
								content.push({id: 0, name: item2.name, trnName: translatedNames[j]});
							});
							// Group title.
							var name = TranslationManager.getTranslated(item[0].levelName.replace(/-/g, " ")) ||
								item[0].levelName.replace(/-/g, " ");
							var filter = new Filter({
								trnName : name,
								name: item[0].levelName,
								values : content
							});
							app.TabsApp.filters.add(filter);
						}
					});	
				}				

			}
		}		
		
		return app.TabsApp.filters;
	};

	// TODO: after we are sure tab's default filters are EXACTLY the same than
	// widget filters we can simplify these 2 methods into just one.
	FilterUtils.updateFiltersRegion = function(filtersFromWidgetWithNames) {		
		app.TabsApp.filters.models = [];
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();
		FilterUtils.extractFilters(filtersFromWidgetWithNames);
		app.TabsApp.dynamicContentRegion.currentView.filters.currentView.render();
	};
	
	FilterUtils._extractDateFilter = function(propertyName, auxProperty){
		var filter;
		if (auxProperty !== undefined) {			
			var filterObject = {
					trnName : TranslationManager.getTranslated(propertyName.replace(/-/g, " ")),
					name : propertyName,
					values: []
			};
			if (auxProperty.modelType === 'DATE-RANGE-VALUES') {
				auxProperty.start =  auxProperty.start || "" ;
				auxProperty.end =  auxProperty.end || "" ;	
				var startDatePrefix = (auxProperty.start.length > 0 && auxProperty.end.length === 0) ? "From" : "";
				var endDatePrefix = (auxProperty.start.length === 0 && auxProperty.end.length > 0) ? "Until" : "";
				var trnName = "";
				
				if(auxProperty.start.length > 0){
					trnName = TranslationManager.getTranslated(startDatePrefix) + " " + app.TabsApp.filtersWidget.formatDate(auxProperty.start);					
				}				
				
				if(auxProperty.end.length > 0){
					if (auxProperty.start.length > 0) {
						trnName += " - ";
					}
					trnName += TranslationManager.getTranslated(endDatePrefix) + " " + app.TabsApp.filtersWidget.formatDate(auxProperty.end);				
				}
				
				filterObject.values.push({
					id : auxProperty.start + auxProperty.end ,
					name : auxProperty.start + auxProperty.end,
					trnName : trnName
				});
			} else if (auxProperty.modelType === 'YEAR-SINGLE-VALUE') {
				auxProperty.year = auxProperty.year || '';
				filterObject.values.push({
					id : auxProperty.year,
					name : auxProperty.year,
					trnName : auxProperty.year
				});
				filterObject.trnName = auxProperty.displayName;
			}
			filter = new Filter(filterObject);			
		}
		return filter;
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
