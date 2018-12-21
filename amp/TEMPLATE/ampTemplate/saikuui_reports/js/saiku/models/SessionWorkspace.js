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
 * Object which handles sessionworkspace and stores connections and cubes
 * @param username
 * @param password
 * @returns {Session}
 */
var SessionWorkspace = Backbone.Model.extend({
	

	defaults : {
		"queryModel" : {},
		"name" : "D1CE2041-5F65-64C6-8C21-2D15741ACB3A",
		"parameters" : {},
		"plugins" : {},
		"metadata" : {},
		"queryType" : "OLAP",
		"type" : "QUERYMODEL"
	},
	
	// IMPORTANT: Care with this file, some changes can cause stop loading Saiku.
    initialize: function(args, options) {
    	Saiku.logger.log("SessionWorkspace.initialize");
        this.process_datasources(this, this.defaults);
        Backbone.history.start();
    	
        // Attach a custom event bus to this model
        _.extend(this, Backbone.Events);
        _.bindAll(this, "process_datasources");
        this.initialized = false;
        this.first = true;
    },

    refresh: function() {
    	Saiku.logger.log("SessionWorkspace.refresh");
        this.fetch({success:this.process_datasources},{});
    },
        
    destroy: function() {
    	Saiku.logger.log("SessionWorkspace.destroy");
        return false;
    },
    
    process_datasources: function(model, response) {
    	Saiku.logger.log("SessionWorkspace.process_datasources");    	
    	// Show UI
        $(Saiku.toolbar.el).prependTo($("#header"));
        $("#header").show();
        // Add initial tab
        Saiku.tabs.render();
        //Saiku.splash.render();
        /*if (!Settings.INITIAL_QUERY) {
        	Saiku.tabs.add(new SplashScreen(), false);
        }
        if (!Settings.INITIAL_QUERY) {
        	Saiku.tabs.add(new Workspace());
        }*/
        // Notify the rest of the application that login was successful
        Saiku.events.trigger('session:new', {
        	session: this
        });
    }
});
