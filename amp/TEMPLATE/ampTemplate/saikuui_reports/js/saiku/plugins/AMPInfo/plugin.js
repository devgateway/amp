var AMPInfo = Backbone.View.extend({

    initialize: function(args) {
    	Saiku.logger.log("AMPInfo.initialize");
    	this.workspace = args.workspace;
        
        this.id = _.uniqueId("amp_info_");
        $(this.el).attr({ id: this.id });
        
        _.bindAll(this, "render", "receive_info", "process_info", "render_info");
        this.workspace.bind('query:result', this.receive_info);
        
        $(this.workspace.el).find('.workspace_results')
            .prepend($(this.el).hide())
    },
    
    render: function() {
    	Saiku.logger.log("AMPInfo.render");
		var filters = this.workspace.query.get('filters');
		var settings = window.settingsWidget.toAPIFormat();
		var content = this.render_info(settings);
		$(this.el).html(content);
		Saiku.i18n.translate();
		$("#amp_info_filters").accordion({	
			collapsible : true,
			active : false,
			animate : "linear",
			activate: function (event, ui) {
				$("#amp_info_filters_block").empty();
				var modelFilters = window.currentFilter.serializeToModels();				
				$("#amp_info_filters_block").html(filtersToHtml(modelFilters.filters || {}));
				Saiku.i18n.translate();
			}
		});
        $(this.el).show();
    },
    
    render_info: function(settings) {    	
    	Saiku.logger.log("AMPInfo.render_info");
    	//TODO: Move all these html into a template + view.
    	var content = "<div id='amp_notification' class='amp_notification'><span class='i18n'>{0}</span></div>" 
    		+ "<div id='amp_info_filters' style='display: block !important;'>";
    	content += "<h3><span class='i18n'>Applied filters</span></h3>";
    	content += "<div id='amp_info_filters_block'></div>";
    	content += "</div>";
    	var notification = this.build_notification();
    	if(settings) {
    		if (this.workspace.query.attributes.original_currency) {
    			content += "<div id='amp_info_settings'><h5><span class='i18n'>&nbsp;</h5></div>";
    		} else {
	    		var currencyCode = settings[window.settingsWidget.Constants.CURRENCY_ID];
	    		var currencyValue = window.settingsWidget.definitions.findCurrencyById(currencyCode).value;
	        	content += "<div id='amp_info_settings'><h5><span class='i18n'>Currency</span>: " + currencyValue;
	        	content += "</h5></div>";
    		}
            notification = this.build_notificationFromSettings(settings);
    	}
    	content = content.replace("{0}", notification);
    	return content;
    },
    
    build_notification: function() {
    	Saiku.logger.log("AMPInfo.build_notification");
			switch(this.workspace.query.get('raw_settings').unitsOption) {
				case "AMOUNTS_OPTION_THOUSANDS": return "Amounts are in thousands (000)";
				case "AMOUNTS_OPTION_MILLIONS": return "Amounts are in millions (000 000)";
			}
    	return "";
    },

    build_notificationFromSettings: function(settings) {
    	Saiku.logger.log("AMPInfo.build_notificationFromSettings");
    	var amountFormat = settings[window.settingsWidget.Constants.AMOUNT_FORMAT_ID];
        	if (amountFormat) {
                switch (amountFormat[window.settingsWidget.Constants.AMOUNT_UNIT_ID]) {
                    case window.settingsWidget.Constants.AMOUNTS_OPTION_THOUSANDS:
                        return "Amounts are in thousands (000)";
                    case window.settingsWidget.Constants.AMOUNTS_OPTION_MILLIONS:
                        return "Amounts are in millions (000 000)";
                }
            }
    	return "";
    },
    
    receive_info: function(args) {
    	Saiku.logger.log("AMPInfo.receive_info");
        return _.delay(this.process_info, 0, args);
    },
    
    process_info: function(args) {
    	Saiku.logger.log("AMPInfo.process_info");
        $(this.el).empty();
        this.render();
    },

    error: function(args) {
    	Saiku.logger.log("AMPInfo.error");
        $(this.el).text(safe_tags_replace(args.data.error));
    }
});

var extract_values = function(object_value) {
	Saiku.logger.log("AMPInfo.extract_values");
	var values = [];
	switch(object_value.filterType) {
    	case "VALUES":
    		_.each(object_value.values, function(item) {
    			values.push(object_value.valueToName[item]);
    		});
    		break;
    	case "RANGE":
    		values.push(object_value.valueToName[object_value.min]);
    		values.push(object_value.valueToName[object_value.max]);
    		break;
    	case "SINGLE_VALUE":
			values.push(object_value.valueToName[object_value.value]);
    		break;
	}
	return values;
}

var filtersToHtml = function(filters) {
	Saiku.logger.log("AMPInfo.filtersToHtml");
	//TODO: Move all these html into a template + view.
	var html = "";
	if (filters != undefined) {
		for ( var propertyName in filters) {
			var auxProperty = filters[propertyName];
			var content = [];
			if(auxProperty.modelType === 'YEAR-SINGLE-VALUE' || auxProperty.modelType === 'DATE-RANGE-VALUES'){
				var filter = createDateFilterObject(filters, propertyName);
				if(filter && filter.values.length > 0){
					html += "<div class='round-filter-group'><b class='i18n'>" + filter.trnName.replace(/-/g, " ") + "</b><br>" + filterContentToHtml(filter.values) + "</div>";						
				}
				
			} else {
				_.each(auxProperty, function(item, i) {
					var auxItem = {};				
					if(item.get !== undefined) {
						auxItem.id = item.get('id');
						auxItem.name = item.get('name');
						
						if (item.get('name') === "true" || item.get('name') === "false") {						
							auxItem.trnName = TranslationManager.getTranslated(item.get('name'));						
						 }
						else {
							auxItem.trnName = item.get('name');
						}
						content.push(auxItem);
					} else {
						console.error(JSON.stringify(auxItem) + " not mapped, we need to check why is not a model.");
					}
				});
				
				var trnName = auxProperty.filterName || propertyName;
				html += "<div class='round-filter-group'><b class='i18n'>" + trnName.replace(/-/g, " ") + "</b><br>" + filterContentToHtml(content) + "</div>";
			}	
			
		}
	}
	
	
	return html;
}

var filterContentToHtml = function(content) {
	Saiku.logger.log("AMPInfo.filterContentToHtml");
	var html = "";
	_.each(content, function(item) {
		html += "<div class='round-filter'>" + item.trnName + "</div>";
	});
	return html;
}

var createDateFilterObject= function(filters, propertyName){	
	var auxProperty = filters[propertyName];
	var filter;	
	if (auxProperty != undefined) {
		filter = {
				trnName : TranslationManager.getTranslated(propertyName), 
				name : propertyName,
				values:[]
		};	
		if (auxProperty.modelType === 'DATE-RANGE-VALUES') {
			auxProperty.start = auxProperty.start || "";
			auxProperty.end = auxProperty.end || "";

			var startDatePrefix = TranslationManager.getTranslated((auxProperty.start.length > 0 && auxProperty.end.length === 0) ? "From" : "");
			var endDatePrefix = TranslationManager.getTranslated((auxProperty.start.length === 0 && auxProperty.end.length > 0) ? "Until" : "");
			var trnName = "";
			
			if(auxProperty.start.length > 0 ){
				trnName = startDatePrefix + " " + window.currentFilter.formatDate(auxProperty.start);				
			}			
						
			if(auxProperty.end.length > 0){
				if(auxProperty.start.length > 0){
					trnName += " - ";
				}
				trnName += endDatePrefix + " " + window.currentFilter.formatDate(auxProperty.end);					
			}	
			
			filter.values.push({
				id : auxProperty.end + auxProperty.start ,
				name : auxProperty.end + auxProperty.start,
				trnName : trnName 					
			});	
			
			
		} else if (auxProperty.modelType === 'YEAR-SINGLE-VALUE') {
			if(auxProperty.year){				
				filter.values.push({
					id : auxProperty.year,
					name : auxProperty.year,
					trnName : auxProperty.year
				});
			}			
			filter.trnName = auxProperty.displayName;
		}
	}
	return filter;
};

/**
 * Start Plugin
 */ 
 Saiku.events.bind('session:new', function(session) {

        function new_workspace(args) {
        	Saiku.logger.log("AMPInfo.new_workspace");
            if (typeof args.workspace.stats == "undefined"
            	|| typeof args.workspace.amp_info == "undefined") {
                args.workspace.amp_info = new AMPInfo({ workspace: args.workspace });
            }
        }

        function clear_workspace(args) {
        	Saiku.logger.log("AMPInfo.clear_workspace");
            if (typeof args.workspace.amp_info != "undefined" && $(args.workspace.amp_info.el).is(':visible')) {
                $(args.workspace.amp_info.el).parents().find('.workspace_results table').show();
                $(args.workspace.amp_info.el).hide();
            }
        }

        
        // Attach stats to existing tabs
        for(var i = 0, len = Saiku.tabs._tabs.length; i < len; i++) {
            var tab = Saiku.tabs._tabs[i];
            if(tab.caption != "Home") {
                new_workspace({
                    workspace: tab.content
                });
            }
        };

        // Attach stats to future tabs
        Saiku.session.bind("workspace:new", new_workspace);
        Saiku.session.bind("workspace:clear", clear_workspace);
    });

