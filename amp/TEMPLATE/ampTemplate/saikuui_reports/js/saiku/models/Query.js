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
 * Workspace query
 */
var Query = Backbone.Model.extend({

    formatter: Settings.CELLSET_FORMATTER,
    properties: null,

    initialize: function(args, options) {
    	Saiku.logger.log("Query.initialize");
   		this.initFiltersDeferred = $.Deferred();    		
    	
        // Save cube
        _.extend(this, options);
        
        // Bind `this`
        _.bindAll(this, "run");
        
        // Generate a unique query id
        this.uuid = 'xxxxxxxx-xxxx-xxxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, 
            function (c) {
                var r = Math.random() * 16 | 0,
                v = c == 'x' ? r : (r & 0x3 | 0x8);
                return v.toString(16);
            }).toUpperCase();
        
        this.model = _.extend({ name: this.uuid }, SaikuQueryTemplate);
        this.helper = new SaikuQueryHelper(this);
        this.result = new Result({ limit: Settings.RESULT_LIMIT }, { query: this });

        //Start Custom Code for Pagination
       	this.set({page:1});
       	this.set({info: ["generatedHeaders"]});
        // Use this flag to use the saved filters (if any) for the report the only first time is loaded.
        this.firstLoad = true;
        window.currentQuery = this;
        //End Custom Code for Pagination
        
        this.transformSavedFilters();
    },
    
    transformSavedFilters: function() {
    	Saiku.logger.log("Query.transformSavedFilters");
    	var self = this;
        if (this.firstLoad === true) {        	
        	var  auxFilters = self.get('filters');
        	
        	//remove null filters
        	$.each(auxFilters, function(key, value){
        	    if (value === "" || value === null){
        	        delete auxFilters[key];
        	    }
        	});
	       
        	self.set('filters', undefined);	         
	        self.set('filters', auxFilters);	       
        }
    },
    
    parse: function(response) {
    	Saiku.logger.log("Query.parse");
        // Assign id so Backbone knows to PUT instead of POST
        this.id = this.uuid;
        if (response.name) {
            this.id = response.name;
            this.uuid = response.name;
        }
        this.model = _.extend(this.model, response);
        this.model.properties = _.extend({}, Settings.QUERY_PROPERTIES, this.model.properties);
    },
    
    setProperty: function(key, value) {
    	Saiku.logger.log("Query.setProperty");
        this.model.properties[key] = value;
    },

    getProperty: function(key) {
    	Saiku.logger.log("Query.getProperty");
        return this.model.properties[key];
    },

    run_query: function(filters, settings) {
    	Saiku.logger.log("Query.run_query");
        Saiku.logger.log(filters ? JSON.stringify(filters) : '');
   		this.set({page: 1});
    	this.run(null, null, filters, settings);
    },
	//Start Custom Code for Pagination    
    first_page: function() {
    	Saiku.logger.log("Query.first_page");
		if (this.get('max_page_no') > 0) {
			this.set({page:1}) ;
		} else {
			this.set({page:0}) ;
		}
	    this.run(true);
    },
    prev_page: function() {
    	Saiku.logger.log("Query.prev_page");
       	var prev_page = this.get('page') > 1 ? this.get('page') - 1 : this.get('page');    	
    	this.set({page: prev_page});
    	this.run(true);
    },
    next_page: function() {
    	Saiku.logger.log("Query.next_page");
    	this.set({page: Math.min(this.get('page')+1, this.get('max_page_no'))});
    	this.run(true);
    },
    last_page: function() {
    	Saiku.logger.log("Query.last_page");
    	var last_page = this.get('max_page_no');
    	this.set({page: last_page});
    	this.run(true);
    },
    //End Custom Code for Pagination
    run: function(force, mdx, filtersObject, settings) {
    	Saiku.logger.log("Query.run");
    	Saiku.logger.log('END!!!');
    	Saiku.logger.log(new Date().getTime() - window.saiku_time + "ms");
    	
        var filters;
        if (filtersObject) {
        	filters = filtersObject.filters ? filtersObject.filters : filtersObject;
        }
        	
        var self = this;
        // Check for automatic execution
        Saiku.ui.unblock();
        this.workspace.unblock();       

        $(this.workspace.el).find(".workspace_results_info").empty();
        this.workspace.trigger('query:run');
        
        this.result.result = null;
        var validated = false;
        var errorMessage = "Query Validation failed!";

        var exModel = this.helper.model();

    	validated = true;

    	if(!this.workspace.currentQueryModel)
        	this.workspace.currentQueryModel = exModel;

    	/*
    	 * We need filters & settings to be always applied
    	 * See AMP-19159, AMP-19135 and AMP-18826
    	 */
    	//if (this.firstLoad === false) {
    	    if (filters === undefined) {
        		filters = this.get('filters');
        	}
        	if (settings === undefined) {
        		settings = this.get('settings');
        	}
        	
        	var filtersApplied = false;
        	if(filters) {
        		this.set('filters', filters);
        		filtersApplied = true;
        		
        	}	        	
    	

    	var settingsApplied = false;
    	if(settings) {
    		this.set('settings', settings);
    		settingsApplied = true;
    	}
    	
    	if(Saiku.Sorting != undefined && Saiku.Sorting.currentSorting.length > 0) {
    		this.set('sorting', Saiku.Sorting.currentSorting);
    	}

    	exModel = this.workspace.currentQueryModel;
    	exModel.querySettings = {};
    	exModel.queryModel.filters = this.get('filters'); 
    	exModel.queryModel.filtersWithModels = this.get('filtersWithModels');
    	exModel.queryModel.filtersApplied = filtersApplied;
    	if (filtersObject) {
    		exModel.queryModel['include-location-children'] = filtersObject['include-location-children'];
    	}
    	
    	
    	exModel.queryModel.settings = this.get('settings');        	
    	exModel.queryModel.settingsApplied = settingsApplied;
    	if(Settings.PAGINATION) {
    		exModel.queryModel.page = this.get('page');
    	}
    	exModel.queryModel.sorting = this.get('sorting');     
        if (!validated) {
            this.workspace.table.clearOut();
            $(this.workspace.processing).html(errorMessage).show();
            this.workspace.adjust();
            Saiku.i18n.translate();
            return;
        }
        this.firstLoad = false;
        
        Saiku.logger.log("QueryRouter.calculateMD5FromParameters");
        exModel.MD5 = CommonFilterUtils.calculateMD5FromParameters(exModel, this.get('report_id'), Saiku.i18n.locale, this.get('page_timestamp'));
        exModel.querySettings.info = this.get('info');

        // Run it
        this.workspace.table.clearOut();
        //$(this.workspace.processing).html('<span class="processing_image">&nbsp;&nbsp;</span> <span class="i18n">Running query...</span>').show();
        Saiku.ui.block('Running query...');
        this.workspace.adjust();
        this.workspace.trigger('query:fetch');
		Saiku.i18n.translate();
        this.result.save({},{ contentType: "application/json", data: JSON.stringify(exModel), error: function() {
            Saiku.ui.unblock();
            var errorMessage = '<span class="i18n">Error executing query. Please check the server logs or contact your administrator!</span>';
            self.workspace.table.clearOut();
            $(self.workspace.processing).html(errorMessage).show();
            self.workspace.adjust();
            Saiku.i18n.translate();
        } });
    },

    enrich: function() {
    	Saiku.logger.log("Query.enrich");
    },
    
    url: function() {
    	Saiku.logger.log("Query.url");
   		return "/TEMPLATE/ampTemplate/saikuui_reports/mockData/query.json";
    }
});