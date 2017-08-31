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
 * Class which handles table rendering of resultsets
 */
var Table = Backbone.View.extend({
    className: 'table_wrapper',
    events: {
        'click th.row' : 'clicked_cell',
        /*'click th.col' : 'clicked_cell',*/
        'click th.col' : 'clicked_header_cell',
        'click th.row_header' : 'clicked_header_cell'
    },

    initialize: function(args) {
    	Saiku.logger.log("Table.initialize");
        this.workspace = args.workspace;
        this.renderer = new SaikuTableRenderer();

        // Bind table rendering to query result event
        _.bindAll(this, "render", "process_data");
        this.workspace.bind('query:result', this.render);
        this.id = _.uniqueId("table_");
        $(this.el).attr('id', this.id);
    },
    
    clicked_header_cell : function(event) {
    	Saiku.logger.log("Table.clicked_header_cell");
    	// For clicks on report's header cells.
    	if(Saiku.Sorting != undefined) {
    		Saiku.Sorting.processClickOnHeader(event);
    	}
    },
    
    clicked_cell: function(event) {
    	Saiku.logger.log("Table.cell");
    	// Keep this function for processing clicks on any report's cell.
    },

    render: function(args, block) {
    	Saiku.logger.log("Table.render");
        if (typeof args == "undefined" || typeof args.data == "undefined" || 
            ($(this.workspace.el).is(':visible') && !$(this.el).is(':visible'))) {
            return;
        }
        if (args.data != null && args.data.error != null) {
            return;
        }        
        // Check to see if there is data
        if (args.data == null || (args.data.height !== "undefined" && args.data.height == 0)) {
            return;
        }
        this.clearOut();
        //$(this.el).html('<span class="processing_image">&nbsp;&nbsp;</span> <span class="i18n rendering_data">Rendering data...</span>');

        // Render the table without blocking the UI thread
        _.delay(this.process_data, 2, args.data);
    },

    clearOut: function() {
    	Saiku.logger.log("Table.clearOut");
        // Do some clearing in the renderer
        this.renderer.clear();
        $(this.workspace.el).find( ".workspace_results" ).unbind('scroll');
        var element = document.getElementById(this.id);
        if(element) {
            var table = element.firstChild;
            if (table) {
                element.removeChild(table);
            }
        }
    },

    process_data: function(data) {
    	Saiku.logger.log("Table.process_data");
        this.workspace.processing.hide();
        this.workspace.adjust();
        // Append the table
        this.clearOut();

    	if (data.page.pageArea !== null && !data.isEmpty ) {
    		$(this.el).html('<div class="results_container"></div>');
    	} else {
    		$(this.el).html("<br><div class='i18n no_results'>The specified filtered report does not hold any data. Either pick a different filter criteria or use another report.</div><br><div class='results_container'></div>");
    	}
        
        data.workspace = this.workspace;
        var contents = this.renderer.render(data, { 
            htmlObject: $(this.el).find('.results_container')
        });
        this.post_process();
    },

    post_process: function() {
    	Saiku.logger.log("Table.post_process");
        if (this.workspace.query.get('type') == 'QM' && Settings.MODE != "view") {
            $(this.el).addClass('headerhighlight');
        } else {
            $(this.el).removeClass('headerhighlight');
        }
        
        $(this.el).find(".i18n").i18n(Saiku.i18n.po_file);
        this.workspace.trigger('table:rendered', this);
    }
});
