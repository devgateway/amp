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
        var properties = this.workspace.query.model.properties ?
            this.workspace.query.model.properties : Settings.QUERY_PROPERTIES;

        // Set properties appropriately
        if (properties['saiku.olap.query.nonempty'] === true) {
            $(this.el).find('.non_empty').addClass('on');
        }
        if (properties['saiku.olap.query.automatic_execution'] === true) {
            $(this.el).find('.auto').addClass('on');
        }
        
        if (properties['saiku.olap.query.drillthrough'] !== true) {
            $(this.el).find('.drillthrough, .drillthrough_export').addClass('disabled_toolbar');
        }

        if (properties['org.saiku.query.explain'] !== true) {
            $(this.el).find('.explain_query').addClass('disabled_toolbar');
        }

        if (properties['org.saiku.connection.scenario'] !== true) {
            $(this.el).find('.query_scenario').addClass('disabled_toolbar');
        } else {
            $(this.el).find('.query_scenario').removeClass('disabled_toolbar');
            $(this.el).find('.drillthrough, .drillthrough_export').addClass('disabled_toolbar');
        }
        if (properties['saiku.olap.query.limit'] == 'true' || properties['saiku.olap.query.filter'] == true) {
            $(this.workspace.el).find('.fields_list_header').addClass('limit');
        }

        if (this.workspace.query.getProperty('saiku.olap.result.formatter') !== "undefined" && this.workspace.query.getProperty('saiku.olap.result.formatter') == "flattened") {
            if (! $(this.el).find('.group_parents').hasClass('on')) {
                $(this.el).find('.group_parents').addClass('on');
            }
        }
        if ($(this.workspace.el).find( ".workspace_results tbody.ui-selectable" ).length > 0) {
            $(this.el).find('.zoom_mode').addClass('on');
        }

        $(this.el).find(".spark_bar, .spark_line").removeClass('on');
        $(this.el).find('a.edit').removeClass('disabled_toolbar');

        if (Settings.MODE == 'VIEW' || this.workspace.isReadOnly) {
            $(this.el).find('a.edit').hide();
            $(this.el).find('a.save').hide();
        } else {
            if (this.workspace.viewState == 'view') {
                $(this.el).find('a.edit').removeClass('on');
            } else {
                $(this.el).find('a.edit').addClass('on');
            }
            $(this.el).find('a.edit').show('normal');
        }
    },
    
    new_query: function(event) {
        this.workspace.switch_view_state('edit');
        this.workspace.new_query();
        
        return false;
    },

    edit_query: function(event) {
        $(event.target).toggleClass('on');

        if ($(event.target).hasClass('on')) {
            this.workspace.switch_view_state('edit');
        } else {
            this.workspace.switch_view_state('view');
        }
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
            (new OpenDialog()).render().open();
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
        // Change property
        var newState = !this.workspace.query.getProperty('saiku.olap.query.automatic_execution');
        this.workspace.query.setProperty('saiku.olap.query.automatic_execution', newState);

        if (newState) {
            $(event.target).addClass('on');    
        } else {
            $(event.target).removeClass('on');    
        }
    },
    
    toggle_fields: function(event) {
        var self = this;
        if (event) {
            $(this.el).find('.toggle_fields').toggleClass('on');
        }
        if (!$(this.el).find('.toggle_fields').hasClass('on')) {
            this.toggle_fields_action('hide');
        } else {
            this.toggle_fields_action('show');
        }
        
    },

    toggle_fields_action: function(action, dontAnimate) {
        var self = this;
        if ( action == 'show' && $('.workspace_editor').is(':visible')) {
            return;
        } else if ( action == 'hide' && $('.workspace_editor').is(':hidden')) {
            return;
        }
        if (dontAnimate) {
            $('.workspace_editor').css('height','');
            if ($('.workspace_editor').is(':hidden')) {
                $('.workspace_editor').show();
            } else {
                $('.workspace_editor').hide();
            }
            return; 
        }
        
        if (action == 'hide') {
            $(this.workspace.el).find('.workspace_editor').hide();
        } else {
            $(this.workspace.el).find('.workspace_editor').show();
        }

        // avoid scrollbar on the right

        /*
        var wf = $('.workspace_editor').height();
        if ( action == 'hide') {
            var wr = $('.workspace_results').height();
            $('.workspace_results').height(wr - wf);
        }
        $(this.workspace.el).find('.workspace_editor').slideToggle({
            queue: false,
            progress: function() {
                self.workspace.adjust();
            },
            complete: function() {
                if ($('.workspace_editor').is(':hidden')) {
                    $('.workspace_editor').height(wf);
                } else {
                    $('.workspace_editor').css('height','');                    
                }
                
                self.workspace.adjust();
            }
        });

        */
    },


    
    toggle_sidebar: function() {
        this.workspace.toggle_sidebar();
    },
    
    group_parents: function(event) {
        $(event.target).toggleClass('on');
        if ($(event.target).hasClass('on')) {
            this.workspace.query.setProperty('saiku.olap.result.formatter', 'flattened');
        } else {
            this.workspace.query.setProperty('saiku.olap.result.formatter', 'flat');
        }
        this.workspace.query.run();
    },

    non_empty: function(event) {
        // Change property
        var nonEmpty = !this.workspace.query.getProperty('saiku.olap.query.nonempty');
        this.workspace.query.helper.nonEmpty(nonEmpty);

        this.workspace.query.setProperty('saiku.olap.query.nonempty', nonEmpty);
    
        // Toggle state of button
        $(event.target).toggleClass('on');
        
        // Run query
        this.workspace.query.run();
    },
    
    swap_axis: function(event) {
        // Swap axes
        $(this.workspace.el).find('.workspace_results table').html('');
        this.workspace.query.helper.swapAxes();
        this.workspace.sync_query();
        this.workspace.query.run(true);
    },
    

    check_modes: function(source) {
        if (typeof source === "undefined" || source == null)
            return;
        
        if ($(this.workspace.el).find( ".workspace_results tbody.ui-selectable" ).length > 0) {
            $(this.workspace.el).find( ".workspace_results tbody" ).selectable( "destroy" );
        }
        if (!$(source).hasClass('on')) {
            $(this.workspace.el).find("td.data").removeClass('cellhighlight').unbind('click');
            $(this.workspace.el).find(".table_mode").removeClass('on');

            this.workspace.query.run();
        } else {
            if ($(source).hasClass('drillthrough_export')) {
                $(this.workspace.el).find("td.data").addClass('cellhighlight').unbind('click').click(this.clicked_cell_drillthrough_export);
                $(this.workspace.el).find(".query_scenario, .drillthrough, .zoom_mode").removeClass('on');
            } else if ($(source).hasClass('drillthrough')) {
                $(this.workspace.el).find("td.data").addClass('cellhighlight').unbind('click').click(this.clicked_cell_drillthrough);
                $(this.workspace.el).find(".query_scenario, .drillthrough_export, .zoom_mode").removeClass('on');
            } else if ($(source).hasClass('query_scenario')) {
                this.workspace.query.scenario.activate();
                $(this.workspace.el).find(".drillthrough, .drillthrough_export, .zoom_mode").removeClass('on');
            } else if ($(source).hasClass('zoom_mode')) {
                
                var self = this;
                $(self.workspace.el).find( ".workspace_results tbody" ).selectable({ filter: "td", stop: function( event, ui ) {
                    var positions = [];
                    $(self.workspace.el).find( ".workspace_results" ).find('td.ui-selected div').each(function(index, element) {
                        var p = $(element).attr('rel');
                        if (p) {
                            positions.push(p);
                        }
                    });
                    $(self.workspace.el).find( ".workspace_results" ).find('.ui-selected').removeClass('.ui-selected');

                    positions = _.uniq(positions);
                    if (positions.length > 0) {
                        self.workspace.query.action.put("/zoomin", { success: self.workspace.sync_query,
                                data: { selections : JSON.stringify(positions) }
                            });
                    }
                } });
                $(this.workspace.el).find(".drillthrough, .drillthrough_export").removeClass('on');
            }
        }

                
    },
    query_scenario: function(event) {
       $(event.target).toggleClass('on');
        this.check_modes($(event.target));        

    },
    zoom_mode: function(event) {
       $(event.target).toggleClass('on');
        this.check_modes($(event.target));        

    },

    drillthrough: function(event) {
       $(event.target).toggleClass('on');
        this.check_modes($(event.target));        
    },
    
    display_drillthrough: function(model, response) {
        this.workspace.table.render({ data: response });
        Saiku.ui.unblock();
    },

    export_drillthrough: function(event) {
        $(event.target).toggleClass('on');
        this.check_modes($(event.target));        
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
        this.workspace.query.parse(response);
        /*
        $columns = $(this.workspace.drop_zones.el).find('.columns')
            .children()
            .detach();
        $rows = $(this.workspace.drop_zones.el).find('.rows')
            .children()
            .detach();
            
        $(this.workspace.drop_zones.el).find('.columns').append($rows);
        $(this.workspace.drop_zones.el).find('.rows').append($columns);
        var rowLimit = $(this.workspace).find('fields_list.ROWS .limit').hasClass('on') | false;
        var colLimit = $(this.workspace).find('fields_list.COLUMNS .limit').hasClass('on') | false;
        $(this.workspace).find('fields_list.ROWS .limit').removeClass('on');
        $(this.workspace).find('fields_list.COLUMNS .limit').removeClass('on');
        if (rowLimit) {
            $(this.workspace).find('fields_list.COLUMNS .limit').addClass('on');
        }
        if (colLimit) {
            $(this.workspace).find('fields_list.ROWS .limit').addClass('on');
        }
        */
        this.workspace.unblock();
        this.workspace.sync_query();
        Saiku.ui.unblock();
    },
    
    show_mdx: function(event) {
        //this.workspace.query.enrich();

        (new MDXModal({ mdx: this.workspace.query.model.mdx })).render().open();
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
        var self = this;
        $(this.workspace.el).find('.workspace_fields').addClass('hide');
        $(this.el).find('.auto, .query_scenario, .buckets, .non_empty, .swap_axis, .mdx, .switch_to_mdx, .zoom_mode').parent().hide();

        if ($(this.workspace.el).find( ".workspace_results tbody.ui-selectable" ).length > 0) {
            $(this.workspace.el).find( ".workspace_results tbody" ).selectable( "destroy" );
        }


        $(this.el).find('.run').attr('href','#run_mdx');
        $(this.el).find('.run, .save, .open, .new, .edit').removeClass('disabled_toolbar');
        
        if (this.workspace.dimension_list) {
            $(this.workspace.el).find('.sidebar_inner ul li a')
                .css({fontWeight: "normal"}).parent('li').removeClass('ui-draggable ui-draggable-disabled ui-state-disabled');
        }
        this.activate_buttons({ workspace: this.workspace });
        $(this.workspace.toolbar.el)
                .find('.run')
                .removeClass('disabled_toolbar');

        $(this.workspace.table.el).empty();
        this.workspace.adjust();
        this.post_mdx_transform();

    },



    post_mdx_transform: function() {
        var self = this;

        if (this.workspace.query.model.type !== "MDX") {
            //this.workspace.query.enrich();
            this.workspace.query.model.queryModel = {};
            this.workspace.query.model.type = "MDX";
            this.workspace.query.setProperty('saiku.olap.result.formatter', 'flat');
            self.workspace.query.helper.model().parameters = {};

        }
        var mdx = this.workspace.query.model.mdx;

        if (self.editor) {
            self.editor.setValue(mdx,0);
            self.editor.clearSelection();
            self.editor.focus();
        }
        $(self.el).find('.group_parents').removeClass('on');
        
        if (Settings.ALLOW_PARAMETERS) {

            var parameterDetector = function() {
                var mdx = self.editor.getValue();
                var parameters = [];
                if (mdx) {
                    for (var i = 0, len = mdx.length; i < (len-1); i++ ) {
                        if (mdx[i] === "$" && mdx[i+1] === "{") {
                            var param = "";
                            var closed = false;
                            for(i = i + 2; i < len; i++) {
                                if (mdx[i] !== '}') {
                                    param += mdx[i];
                                } else {
                                    closed = true;
                                    i++;
                                    break;
                                }
                            }
                            if (closed && param && param.length > 0) {
                                parameters.push(param);
                            }
                        }
                    }
                }
                var qParams = self.workspace.query.helper.model().parameters;
                var newParams = {};
                _.each(parameters, function(p) {
                    if (!qParams[p]) {
                        newParams[p] = "";
                    } else {
                        newParams[p] = qParams[p];
                    }

                });
                self.workspace.query.helper.model().parameters = newParams;
                self.workspace.update_parameters();

                
            };

            var lazyDetector = function() { _.delay(parameterDetector, 1000); };
            self.editor.getSession().off('change', lazyDetector);
            self.editor.getSession().on('change', lazyDetector);
            self.workspace.update_parameters();
        }

    },

    run_mdx: function(event) {
        //var mdx = $(this.workspace.el).find(".mdx_input").val();
        if ($(this.workspace.el).find(".mdx_input").height() > 100) {
            $(this.workspace.el).find(".mdx_input").height(100);
        }
        return;
    },

    explain_query: function(event) {
        var self = this;
        var explained = function(model, args) {

            var explainPlan = "<textarea style='width: " + ($("body").width() - 165) + "px;height:" + ($("body").height() - 175) + "px;'>";
            if (args != null && args.error != null) {
                explainPlan += args.error;
            } else if (args.cellset && args.cellset.length === 0) {
                explainPlan += "Empty explain plan!";
            } else {
                explainPlan += args.cellset[1][0].value;
            }
            explainPlan += "</textarea>";

            Saiku.ui.unblock();
            var html =  explainPlan;
            var html = '<div id="fancy_results" class="workspace_results" style="overflow:visible"><table>' 
                 "<tr><th clas='row_header'>Explain Plan</th></tr>"
                 "<tr><td>" + explainPlan + "</td></tr>"
                 '</table></div>';

            $.fancybox(html
                ,
                {
                'autoDimensions'    : false,
                'autoScale'         : false,
                'height'            :  ($("body").height() - 100),
                'width'             :  ($("body").width() - 100),
                'transitionIn'      : 'none',
                'transitionOut'     : 'none'
                }
            );
        };

        self.workspace.query.action.get("/explain", { success: explained } );

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