var AMPGisExport = Backbone.View.extend({
	events : {
		'click #export-to-map' : 'exportToMap'
	},
	
	initialize : function(args) {
    	var self = this;
		this.workspace = args.workspace;
		this.initialized = false;

		this.id = _.uniqueId("amp_export_");
		$(this.el).attr({
			id : this.id
		});

		_.bindAll(this, "exportToMap");
		
		$("#export-to-map").on("click", this.exportToMap);
		
	},
	
	exportToMap : function() {
		var reportId = + this.workspace.query.get('report_id');
		$.ajax({
			url : '/rest/data/report/export-to-map/' + reportId,
			dataType: 'json',
			type: 'post',
			contentType: 'application/json',
			data : JSON.stringify({
				filters : window.currentFilter.serialize(),
				settings : window.currentSettings
			})
		}).done(function(configId) {
			if (configId != null) {
				var url = '/TEMPLATE/ampTemplate/gisModule/dist/index.html#report/' + configId;
				event.preventDefault();
           		event.stopPropagation();
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
		if (typeof args.workspace.amp_export == "undefined") {
			args.workspace.amp_export = new AMPGisExport({
				workspace : args.workspace
			});
		}
	}

	function clear_workspace(args) {
		if (typeof args.workspace.amp_export != "undefined") {
			$(args.workspace.amp_export.el).hide();
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
