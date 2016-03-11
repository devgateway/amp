/*  
 *   Copyright 2012 OSBI Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
 
/**
 * The workspace toolbar, and associated actions
 */

var EnabledGisModel = Backbone.Model.extend({
	
});

var EnabledGisCollection = Backbone.Collection.extend({
	model : EnabledGisModel,
	url : '/rest/common/fm',
	fetchData : function() {
		var params = {
			"detail-modules" : [ "GIS" ]
		};
		this.fetch({
			type : 'POST',
			async : false,
			processData : false,
			mimeType : 'application/json',
			traditional : true,
			headers : {
				'Content-Type' : 'application/json',
				'Cache-Control' : 'no-cache'
			},
			data : JSON.stringify(params),
			error : function(collection, response) {
				//console.error('error loading enabled modules.');
			},
			success : function(collection, response) {
			}
		});
	}
});

var enabledGisFM = new EnabledGisCollection();
enabledGisFM.fetchData();

var WorkspaceToolbar = Backbone.View.extend({
    enabled: false,
    events: {
        'click a': 'call'
    },
    
    initialize: function(args) {
        // Keep track of parent workspace
        this.workspace = args.workspace;
        
        // Maintain `this` in callbacks
        _.bindAll(this, "call", "reflect_properties", "run_query",
            "swap_axes_on_dropzones", "display_drillthrough","clicked_cell_drillthrough_export",
            "clicked_cell_drillthrough","activate_buttons", "switch_to_mdx","post_mdx_transform", "toggle_fields_action",
            "export_amp_xls", "export_amp_xls_plain", "export_amp_csv", "export_amp_pdf", "calculate_url");
        
        // Redraw the toolbar to reflect properties
        this.workspace.bind('properties:loaded', this.reflect_properties);
        
        // Fire off workspace event
        this.workspace.trigger('workspace:toolbar:render', { 
            workspace: this.workspace
        });
        
        // Activate buttons when a new query is created or run
        this.workspace.bind('query:new', this.activate_buttons);
        this.workspace.bind('query:result', this.activate_buttons);
        
        Saiku.Sorting.initialize(this);
        
    },
    
    activate_buttons: function(args) {
        if (args != null && args.data && args.data.cellset && args.data.cellset.length > 0 ) {
        	
            $(args.workspace.toolbar.el).find('.button')
                .removeClass('disabled_toolbar');       

            $(args.workspace.el).find("td.data").removeClass('cellhighlight').unbind('click');
            $(args.workspace.el).find(".table_mode").removeClass('on');

        } else {
            $(args.workspace.toolbar.el).find('.button')
                .addClass('disabled_toolbar').removeClass('on');
            $(args.workspace.el).find('.fields_list .disabled_toolbar').removeClass('disabled_toolbar');
            $(args.workspace.toolbar.el)
                .find('.new, .open, .save, .edit, .run,.auto, .non_empty,.toggle_fields,.toggle_sidebar,.switch_to_mdx, .mdx')
                .removeClass('disabled_toolbar');
        }
        
        var arrButtons = $(args.workspace.toolbar.el)
        .find('.new, .open, .save, .run, .swap_axis, .zoom_mode, .query_scenario, .edit, .auto, .non_empty,.toggle_fields,.toggle_sidebar,.switch_to_mdx, .mdx, .group_parents, .drillthrough, .drillthrough_export');
        _.each(arrButtons, function(button) {
        	//Hide Parent
        	$(button.parentElement).hide();
        });
        $(this.workspace.el).find('.workspace_fields').addClass('hide');
        
        if (this.workspace.query.result.hasRun() && 
        		this.workspace.query.result.result.page.pageArea === null) {
        	$(this.el).find('a.export_xls').addClass('disabled_toolbar');            	
        	$(this.el).find('a.export_csv').addClass('disabled_toolbar');
        	$(this.el).find('a.export_pdf').addClass('disabled_toolbar');
        	$(this.el).find('a.export_to_map').addClass('disabled_toolbar');
        	$(this.el).find('a.fullscreen').addClass('disabled_toolbar');
        	$(this.el).find('a.export_dual_currency').addClass('disabled_toolbar');
        }

    	if (this.is_gis_enabled()) {
        	$(this.el).find('a.export_to_map').removeClass('disabled_toolbar');
        } else {
        	$(this.el).find('a.export_to_map').addClass('disabled_toolbar');
        }
    	
        this.reflect_properties();

    },

    template: function() {
        var template = $("#template-workspace-toolbar").html() || "";
        return _.template(template)();
    },
    
    render: function() {
        $(this.el).html(this.template());
        
        return this; 
    },
    
    call: function(event) {
        // Determine callback
        event.preventDefault();
        var callback = event.target.hash.replace('#', '');
        
        // Attempt to call callback
        if (! $(event.target).hasClass('disabled_toolbar') && this[callback]) {
            this[callback](event);
        }
        
        return false;
    },

    reflect_properties: function() {

    },
    
    new_query: function(event) {

    },

    edit_query: function(event) {

    },

    save_query: function(event) {
        var self = this;
        if (this.workspace.query) {
            if (typeof this.editor != "undefined") {
                var mdx = this.editor.getValue();
                this.workspace.query.model.mdx = mdx;
            }
            (new SaveQuery({ query: this.workspace.query })).render().open();
        }
    },

    open_query: function(event) {

    },

    
    run_query: function(event) {
        this.workspace.query.run(true);        
    },

    //Start Custom Code for Pagination
    first_page: function(event) {
        this.workspace.query.first_page();
    },

    prev_page: function(event) {
        this.workspace.query.prev_page();
    },

    next_page: function(event) {
        this.workspace.query.next_page();
    },

    last_page: function(event) {
        this.workspace.query.last_page();
    },
    //End Custom Code for Pagination
         
    automatic_execution: function(event) {

    },
    
    toggle_fields: function(event) {

    },

    toggle_fields_action: function(action, dontAnimate) {

    },
    
    toggle_sidebar: function() {
        this.workspace.toggle_sidebar();
    },
    
    group_parents: function(event) {

    },

    non_empty: function(event) {

    },
    
    swap_axis: function(event) {

    },    

    check_modes: function(source) {
            
    },
    
    query_scenario: function(event) {       

    },
    
    zoom_mode: function(event) {

    },

    drillthrough: function(event) {
     
    },
    
    display_drillthrough: function(model, response) {

    },

    export_drillthrough: function(event) {
      
    },

    clicked_cell_drillthrough_export: function(event) {
        $target = $(event.target).hasClass('data') ?
            $(event.target).find('div') : $(event.target);
        var pos = $target.attr('rel');     
        (new DrillthroughModal({
            workspace: this.workspace,
            maxrows: 10000,
            title: "Drill-Through to CSV",
            action: "export",
            position: pos,
            query: this.workspace.query
        })).open();
   
    },

    clicked_cell_drillthrough: function(event) {
        $target = $(event.target).hasClass('data') ?
            $(event.target).find('div') : $(event.target);
        var pos = $target.attr('rel');
        (new DrillthroughModal({
            workspace: this.workspace,
            maxrows: 200,
            title: "Drill-Through",
            action: "table",
            success: this.display_drillthrough,
            position: pos,
            query: this.workspace.query
        })).open();
   
    },

    swap_axes_on_dropzones: function(model, response) {
        
    },
    
    show_mdx: function(event) {

    },
    
    export_xls: function(event) {
        window.location = Settings.REST_URL +
            this.workspace.query.url() + "/export/xls/" + this.workspace.query.getProperty('saiku.olap.result.formatter');
    },

    export_amp_xls: function(event) {
    	var auxQuery = this.workspace.currentQueryModel;
    	auxQuery.xls_type = 'styled';
	    $.postDownload("/rest/data/saikureport/export/xls/" + this.calculate_url(),
	    		{query: JSON.stringify(auxQuery)}, "post");
    },

    export_amp_xls_plain: function(event) {
    	var auxQuery = this.workspace.currentQueryModel;
    	auxQuery.xls_type = 'plain';
	    $.postDownload("/rest/data/saikureport/export/xls/" + this.calculate_url(),
	    		{query: JSON.stringify(auxQuery)}, "post");

    },
    export_csv: function(event) {
        window.location = Settings.REST_URL +
            this.workspace.query.url() + "/export/csv";
    },

    export_amp_csv: function(event) {
	    $.postDownload("/rest/data/saikureport/export/csv/" + this.calculate_url(),
	    	{query: JSON.stringify(this.workspace.currentQueryModel)}, "post");

    },

    export_pdf: function(event) {
        window.location = Settings.REST_URL +
            this.workspace.query.url() + "/export/pdf/" + this.workspace.query.getProperty('saiku.olap.result.formatter');
    },

    export_amp_pdf: function(event) {
	    $.postDownload("/rest/data/saikureport/export/pdf/" +  this.calculate_url(),
	    	{query: JSON.stringify(this.workspace.currentQueryModel)}, "post");
    },

    export_amp_dual_currency: function(){
        var that = this;
        if(!this.deflatedCurrenciesPromise){
            this.deflatedCurrenciesPromise = $.get("/rest/amp/settings")
                .then(function(settings){
                    return settings.filter(function(setting){
                        return setting.id == "1";
                    })[0]
                })
                .then(function(setting){
                    return setting.options.filter(function(currency){
                        return currency.id != setting.defaultId
                    })
                })
            ;
        }
        this.deflatedCurrenciesPromise.then(function(currencies){
            var $container = $('#deflated-currencies-container');
            var $select = $container.find('#amp_deflated_currency');
            var $export = $container.find(".btn.export");
            var $cancel = $container.find(".btn.cancel");
            var $close = $container.find(".close.cancel");
            if($select.is(":empty")){
                $select.append(currencies.map(function(currency){
                    return $("<option></option>")
                        .text(currency.name + " (" + currency.id + ")")
                        .attr("value", currency.id);
                }));
                $cancel.add($export).add($close).click(function(){
                    $container.hide();
                });
                $export.click(function(){
                    var payload = $.extend(true, {}, that.workspace.currentQueryModel);
                    payload.xls_type = 'styled';
                    payload.queryModel.secondCurrency = $select.val();
                    $.postDownload("/rest/data/saikureport/export/xls/" + that.calculate_url(),
                      {query: JSON.stringify(payload)}, "post");
                });
            }
            $container.show();
        });
    },

    switch_to_mdx: function(event) {

    },

    post_mdx_transform: function() {

    },

    run_mdx: function(event) {
        return;
    },

    explain_query: function(event) {
        return false;
    },
    
    calculate_url : function() {
    	var runUrl='';
    	var reportIdentification; //this may be the actual ID or the report token if we are running the report without saving
    	if(this.workspace.query.get('report_id')){
    		reportIdentification=this.workspace.query.get('report_id');
    	}else{
    		runUrl='run/';
    		reportIdentification=this.workspace.query.get('report_token');
    	}
    	
    	// AMP-22070 temporary parameter, will be removed when Mondrian will not be used
    	var niReportAttr = '';
    	if(Settings.NIREPORT) {
    		niReportAttr = '?nireport=true';
    	}
    	
    	return runUrl + reportIdentification + niReportAttr;
    },
    
    is_gis_enabled : function() {
    // Map icon is enabled if FM "GIS" is enabled or the report type is not pledge
    	return enabledGisFM && 
    	enabledGisFM.models[0].get('error') == undefined && 
    	enabledGisFM.models[0].get('GIS') !== undefined &&
    	this.workspace.query.attributes.report_type != 5;
    }

});

$.postDownload = function (path, params, method) {
    method = method || "post";
    var form = document.getElementById("exportForm");
    if(!form) {
        form = document.createElement("form");
        form.id = "exportForm";
    }
    form.setAttribute("method", method);
    form.setAttribute("action", path);
    form.setAttribute("accept-charset","UTF-8");

    for(var key in params) {
        if(params.hasOwnProperty(key)) {
        	if(params[key]) {
	        	var query = document.getElementById("export_query");
	        	if(!query) {
	        		query = document.createElement("input");
	                form.appendChild(query);
	        	}
	        	query.setAttribute("type", "hidden");
	        	query.setAttribute("name", key);
	        	query.setAttribute("value", params[key]);
        	}
         }
    }
    document.body.appendChild(form);
    form.submit();
    // Due to AMP-20337 we need to destroy this form after using it.
    $(form).empty();
    $(form).remove();
}