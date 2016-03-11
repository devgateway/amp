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
        // Use this flag to use the saved filters (if any) for the report the only first time is loaded.
        this.firstLoad = true;
        window.currentQuery = this;
        //End Custom Code for Pagination
        
        this.transformSavedFilters();
    },    
    
    transformSavedFilters: function() {
    	var self = this;
        if (this.firstLoad === true) {
        	// Get original filters from reports specs.
        	var auxFilters = self.get('filters');
	        // Cleanup filters property on this Query.
        	self.set('filters', undefined);
	        // TODO: Review this 2-steps process completely.
	        var extractedFiltersFromSpecs = FilterUtils.extractFilters(auxFilters);
	        var blob = FilterUtils.convertJavaFiltersToJS(extractedFiltersFromSpecs);
	        //console.log(blob);
	        // Set these filters reformatted. 
	        self.set('filters', blob);	       
        }
    },
    
    // AMP-18921: workaround to the filters until they will be properly initialized, 
	// that should be done as part of filters widget improvement as a whole
    initFilters_old: function() {
    	var self = this;
        if (window.currentFilter !== undefined && window.currentFilter.converted !== true) {
        	window.currentFilter.converted = true;
            window.currentFilter.loaded.done(function() {
	            //console.log(this.get('filters'));
	            var auxFilters = self.get('filters');
	            self.set('filters', undefined);
	            // TODO: Review this 2-steps process completely.
	            var extractedFiltersFromSpecs = FilterUtils.extractFilters(auxFilters);
	            //console.log(extractedFiltersFromSpecs);
	            var blob = FilterUtils.convertJavaFiltersToJS(extractedFiltersFromSpecs);
	            //console.log(blob);
	                                
	            window.currentFilter.deserialize(blob, {
	            	silent : true
	            });
	            var serealized = window.currentFilter.serialize();
	            //console.log(JSON.stringify(serealized));
	            self.set('filters', serealized);
	            //console.log(JSON.stringify(self.get('filters')));
	            // TODO: Check if the filter widget is really ready (in older versions the parse of dates where processed too late).
	            self.initFiltersDeferred.resolve();
            });                                                                     
        }
    },
    
    parse: function(response) {
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
            this.model.properties[key] = value;
    },

    getProperty: function(key) {
        return this.model.properties[key];
    },

    run_query: function(filters, settings) {
   		this.set({page: 1});
    	this.run(null, null, filters, settings);
    },
	//Start Custom Code for Pagination    
    first_page: function() {
	if (this.get('max_page_no') > 0) {
		this.set({page:1}) ;
	} else {
		this.set({page:0}) ;
	}
    	this.run(true);
    },
    prev_page: function() {
       	var prev_page = this.get('page') > 1 ? this.get('page') - 1 : this.get('page');    	
    	this.set({page: prev_page});
    	this.run(true);
    },
    next_page: function() {
    	this.set({page: Math.min(this.get('page')+1, this.get('max_page_no'))});
    	this.run(true);
    },
    last_page: function() {
    	var last_page = this.get('max_page_no');
    	this.set({page: last_page});
    	this.run(true);
    },
    //End Custom Code for Pagination
    run: function(force, mdx, filters, settings) {
    	console.log('END!!!');
    	console.log(new Date().getTime() - window.saiku_time + "ms");

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
        		//this.set('filtersWithModels', window.currentFilter.serializeToModels());
        	}	        	
    	//}

    	var settingsApplied = false;
    	if(settings) {
    		this.set('settings', settings);
    		settingsApplied = true;
    	}
    	
    	if(Saiku.Sorting != undefined && Saiku.Sorting.currentSorting.length > 0) {
    		this.set('sorting', Saiku.Sorting.currentSorting);
    	}

    	exModel = this.workspace.currentQueryModel;
    	//if (this.firstLoad === false) {
    		exModel.queryModel.filters = this.get('filters');
    		exModel.queryModel.filtersWithModels = this.get('filtersWithModels');
    		exModel.queryModel.filtersApplied = filtersApplied;
    	//}
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

        // Run it
        this.workspace.table.clearOut();
        $(this.workspace.processing).html('<span class="processing_image">&nbsp;&nbsp;</span> <span class="i18n">Running query...</span>').show();
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
        
    },
    
    url: function() {
   		return "/TEMPLATE/ampTemplate/saikuui_nireports/mockData/query.json";
    }
});
