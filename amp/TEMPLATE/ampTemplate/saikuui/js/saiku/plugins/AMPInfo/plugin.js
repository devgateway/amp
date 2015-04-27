var AMPInfo = Backbone.View.extend({

    initialize: function(args) {
    	if(!Settings.AMP_REPORT_API_BRIDGE) return; 

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
		var content = this.render_info(filters, settings);
		$(this.el).html(content);
		$("#amp_info_filters").accordion({	
			collapsible : true,
			active : false,
			animate : "linear" 
		});
        $(this.el).show();
    },
    
    render_info: function(filters, settings) {
    	var content = "<div id='amp_notification' class='amp_notification'><span class='i18n'>{0}</span></div>" 
    		+ "<div id='amp_info_filters'>";
    	content += "<h3><span class='i18n'>Applied filters</span></h3>";
    	content += "<div id='amp_info_filters_block'>";
    	var processed_items = {};
    	if(filters.columnFilterRules !== undefined) {
    		_.each(filters.columnFilterRules, function(v, k){
    			var key = k;
    			var values = extract_values(v[0]);
    			processed_items[k] = values;
    		});
    	}
    	if(filters.columnDateFilterRules !== undefined) {
    		_.each(filters.columnDateFilterRules, function(v, k){
    			var key = k;
    			var values = extract_values(v[0]);
    			processed_items[k] = values;
    		});
    	}
    	_.each(processed_items, function(v,k) {
        	content += "<div>";
            content += "<b>" + k + "</b>:<br>";
            _.each(v, function(item, i) {
            	content += "<div class='round-filter'>" + item + "</div>";
            });
        	content += "<hr></div>";
    	});
    	content += "</div>";
    	content += "</div>";
    	if(settings){
        	content += "<div id='amp_info_settings'><span class='i18n'>Currency</span>: " +  settings["1"];
        	content += "</div>";
    	}
    	content = content.replace("{0}", this.build_notification(settings));
    	return content;
    },
    
    build_notification: function(settings) {
    	var notification = "";
    	if(settings){
    		switch(settings["3"]) {
        	case 0.001: notification = "Amounts in Thousands";
        	break;
        	case 0.000001 : notification = "Amounts in Millions";
        	break;
        	}
    	}
    	return notification;
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

