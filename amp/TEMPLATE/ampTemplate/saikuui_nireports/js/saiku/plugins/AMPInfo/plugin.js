var AMPInfo = Backbone.View.extend({

    initialize: function(args) {
    	this.workspace = args.workspace;
        
        this.id = _.uniqueId("amp_info_");
        $(this.el).attr({ id: this.id });
        
        _.bindAll(this, "render", "receive_info", "process_info", "render_info");
        this.workspace.bind('query:result', this.receive_info);
        
        $(this.workspace.el).find('.workspace_results')
            .prepend($(this.el).hide())
    },
    
    render: function() {
		var filters = this.workspace.query.get('filters');
		var settings = window.currentSettings;
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
				$("#amp_info_filters_block").html(filtersToHtml(modelFilters));
				Saiku.i18n.translate();
			}
		});
        $(this.el).show();
    },
    
    render_info: function(settings) {
    	//TODO: Move all these html into a template + view.
    	var content = "<div id='amp_notification' class='amp_notification'><span class='i18n'>{0}</span></div>" 
    		+ "<div id='amp_info_filters'>";
    	content += "<h3><span class='i18n'>Applied filters</span></h3>";
    	content += "<div id='amp_info_filters_block'></div>";
    	content += "</div>";
    	if(settings){
        	content += "<div id='amp_info_settings'><h4><span class='i18n'>Currency</span>: " +  settings["1"];
        	content += "</h4></div>";
    	}
    	content = content.replace("{0}", this.build_notification());
    	return content;
    },
    
    build_notification: function() {
			switch(this.workspace.query.get('raw_settings').unitsOption) {
				case "AMOUNTS_OPTION_THOUSANDS": return "Amounts are in thousands (000)";
				case "AMOUNTS_OPTION_MILLIONS": return "Amounts are in millions (000 000)";
			}
    	return "";
    },
    
    receive_info: function(args) {
        return _.delay(this.process_info, 0, args);
    },
    
    process_info: function(args) {
        
        $(this.el).empty();
        this.render();
    },

    error: function(args) {
        $(this.el).text(safe_tags_replace(args.data.error));
    }
});
var extract_values = function(object_value) {
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
	console.log(filters);
	//TODO: Move all these html into a template + view.
	var html = "";
	if (filters.columnFilters != undefined) {
		for ( var propertyName in filters.columnFilters) {
			var auxProperty = filters.columnFilters[propertyName];
			var content = [];
			_.each(auxProperty, function(item, i) {
				var auxItem = {};
				if(item.get !== undefined) {
					auxItem.id = item.get('id');
					auxItem.name = item.get('name');
					if (item.get('name') === "true" || item.get('name') === "false") {
						//auxItem.trnName = TranslationManager.getTranslated(item.get('name'));
						auxItem.trnName = item.get('name');
					 }
					else {
						auxItem.trnName = item.get('name');
					}
					content.push(auxItem);
				} else {
					console.error(JSON.stringify(auxItem) + " not mapped, we need to check why is not a model.");
				}
			});
			/*var name = TranslationManager.getTranslated(auxProperty.filterName) || TranslationManager.getTranslated(propertyName)*/
			var trnName = auxProperty.filterName || propertyName;
			html += "<div class='round-filter-group'><b class='i18n'>" + trnName + "</b><br>" + filterContentToHtml(content) + "</div>";
		}
	}
	if (filters.otherFilters != undefined) {
		for ( var propertyName in filters.otherFilters) {
			var dateContent = filters.otherFilters[propertyName];
			if (dateContent != undefined
					&& dateContent.start != undefined) {
				var filter = {
					trnName : propertyName, /*TranslationManager.getTranslated(propertyName),*/
					name : propertyName,
					values : [ {
						id : dateContent.start,
						name : dateContent.start,
						trnName : dateContent.start //doesn't need translation for now
					},
					{
						id : dateContent.end,
						name : dateContent.end,
						trnName : dateContent.end //doesn't need translation for now
						
					}]
				};
				html += "<div class='round-filter-group'><b class='i18n'>" + filter.trnName + "</b><br>" + filterContentToHtml(filter.values) + "</div>";
			}
		}
	}
	return html;
}

var filterContentToHtml = function(content) {
	var html = "";
	_.each(content, function(item) {
		html += "<div class='round-filter'>" + item.trnName + "</div>";
	});
	return html;
}

/**
 * Start Plugin
 */ 
 Saiku.events.bind('session:new', function(session) {

        function new_workspace(args) {
            if (typeof args.workspace.stats == "undefined"
            	|| typeof args.workspace.amp_info == "undefined") {
                args.workspace.amp_info = new AMPInfo({ workspace: args.workspace });
            }
        }

        function clear_workspace(args) {
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

