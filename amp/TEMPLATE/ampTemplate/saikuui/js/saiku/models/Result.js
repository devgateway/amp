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
 * Holds the resultset for a query, and notifies plugins when resultset updated
 */
var Result = Backbone.Model.extend({

    result: null,
    firstRun: false,
    
    initialize: function(args, options) {
        // Keep reference to query
        this.query = options.query;
    },
    
    parse: function(response) {
        // Show the UI if hidden
        this.query.workspace.unblock();
        this.query.workspace.processing.hide();
        this.result = response;
        if (!response.error) {
            this.query.model = _.extend({}, response.query);
        }
        this.firstRun = true;
		//Start Custom Code for Pagination
        this.query.set({'total_rows': response.height});
        this.updatePagination();
        //End Custom Code for Pagination

        this.query.workspace.trigger('query:result', {
            workspace: this.query.workspace,
            data: response
        });

    },

    hasRun: function() {
        return this.firstRun;
    },
    
    lastresult: function() {
            return this.result;
    },
    
    url: function() {
    	debugger;
    	if(this.query.get('report_id')){
	    	return encodeURI("../../../rest/data/saikureport/" + this.query.get('report_id'));
    	}
    	else if(this.query.get('report_token'))
    	{
    		return encodeURI("../../../rest/data/saikureport/run/" + this.query.get('report_token'));
    	}    	
        return "api/query/execute";
    },

    updatePagination: function() {
    	//Start Custom Code for Pagination
    	if(Settings.PAGINATION) {
    		result_type = "paginatedresult";
    		var totalRows =  this.query.get('total_rows');
    		var page = this.query.get('page');
    		var maxPageNo = 0;
    		if (Settings.RESULTS_PER_PAGE > 0) {
    			maxPageNo = (((totalRows + Settings.RESULTS_PER_PAGE - 1) / Settings.RESULTS_PER_PAGE) >> 0) - 1;
    		}
    		this.query.set({'max_page_no': maxPageNo});
    		if (maxPageNo < page) {
    			page = maxPageNo;
    			this.query.set('page', page);
    		}
        	var start = page * Settings.RESULTS_PER_PAGE;
        	var end = start  + Settings.RESULTS_PER_PAGE;
        	this.set({'start': start});
        	this.set({'end': end});
    	}
    }
});
