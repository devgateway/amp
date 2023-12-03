var AMPGisExport = Backbone.View.extend({
	events : {
		'click #export-to-map' : 'export_to_map'
	},
	
	initialize : function(args) {
		Saiku.logger.log("AMPGisExport.initialize");
    	var self = this;
		this.workspace = args.workspace;
		this.initialized = false;

		this.id = _.uniqueId("amp_export_");
		$(this.el).attr({
			id : this.id
		});

		_.bindAll(this, "export_to_map");

    	if(this.workspace.query.get('report_id')){
			this.add_button();
			this.workspace.toolbar.export_to_map = this.export_to_map;
    	}
		
	},
	
	add_button : function() {
		Saiku.logger.log("AMPGisExport.add_button");
		this.export_to_amp_button = $(
				'<a href="#export_to_map" class="export_to_map button i18n" title="Export to Map"></a>');

		var $export_to_map_li = $('<li class="seperator"></li>').append(
				this.export_to_amp_button);
		$(this.workspace.toolbar.el).find("ul").append($export_to_map_li);
	},
	
	export_to_map : function() {
		Saiku.logger.log("AMPGisExport.export_to_map");
		var reportId = + this.workspace.query.get('report_id');
		
		// AMP-18921: workaround to the filters until they will be properly initialized, 
		// that should be done as part of filters widget improvement as a whole
				
		// export to map
		var filterObject = window.currentFilter.serialize();
		$.ajax({
			url : '/rest/data/report/export-to-map/' + reportId,
			dataType: 'json',
			type: 'post',
			contentType: 'application/json',
			data : JSON.stringify({
				filters : filterObject || {},
				settings : window.settingsWidget.toAPIFormat()
			})
		}).done(function(configId) {
			if (configId != null) {
				var url = '/TEMPLATE/ampTemplate/gisModule/dist/index.html#report/' + configId;
				var mapWindow = window.open(url);
           		if (window.focus) {
           			mapWindow.focus();
           		}
			} else {
				alert("no map id could be provided for export");
			}
		}).fail(function() {
			alert( "error" );
		});
	}	
		
});

Saiku.events.bind('session:new', function(session) {
	function new_workspace(args) {
		Saiku.logger.log("AMPGisExport.new_workspace");
		if (typeof args.workspace.export_to_map == "undefined") {
			args.workspace.export_to_map = new AMPGisExport({
				workspace : args.workspace
			});
		}
	}

	function clear_workspace(args) {
		Saiku.logger.log("AMPGisExport.clear_workspace");
		if (typeof args.workspace.export_to_map != "undefined") {
			$(args.workspace.export_to_map.el).hide();
		}
	}
	
	for (var i = 0, len = Saiku.tabs._tabs.length; i < len; i++) {
		var tab = Saiku.tabs._tabs[i];
		if (tab.caption != "Home") {
			new_workspace({
				workspace : tab.content
			});
		}
	}
	;

	Saiku.session.bind("workspace:new", new_workspace);
	Saiku.session.bind("workspace:clear", clear_workspace);
});
