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
 * The query toolbar, and associated actions
 */
var QueryToolbar = Backbone.View.extend({  

    events: {
        'click .options a.button': 'call',
        'click .renderer a.button' : 'switch_render_button'
    },

    chart: {},

    render_mode: "table",
    spark_mode: null,


    initialize: function(args) {
    	Saiku.logger.log("QueryToolbar.initialize");
        // Keep track of parent workspace
        this.workspace = args.workspace;
        
        // Maintain `this` in callbacks
        _.bindAll(this, "call","activate_buttons", "spark_bar", "spark_line", "render_row_viz", "run_row_viz", "switch_render_button");
        
        this.render_mode = "table";
        this.spark_mode = null;

        // Activate buttons when a new query is created or run
        this.workspace.bind('query:new', this.activate_buttons);
        this.workspace.bind('query:result', this.activate_buttons);
        this.workspace.bind('table:rendered', this.run_row_viz);
        
    },
    
    activate_buttons: function(args) {     
    	Saiku.logger.log("QueryToolbar.activate_buttons");
    },

    template: function() {
    	Saiku.logger.log("QueryToolbar.template");
        var template = $("#template-query-toolbar").html() || "";
        return _.template(template)();
    },
    
    render: function() {
    	Saiku.logger.log("QueryToolbar.render");
        $(this.el).html(this.template());
        $(this.el).hide();
        return this; 
    },
    
    switch_render_button: function(event) {
    	Saiku.logger.log("QueryToolbar.switch_render_button");
    },
    
    switch_render: function(render_type) {
    	Saiku.logger.log("QueryToolbar.switch_render");
        return false;
    },

    call: function(event) {
    	Saiku.logger.log("QueryToolbar.call");
        event.preventDefault();
        return false;
    },

    spark_bar: function() {
       
    },

    spark_line: function() {
        
    },

    run_row_viz: function(args) {
        
    },

    render_row_viz: function(type) {
        
    }
});
