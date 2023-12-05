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
    	Saiku.logger.log("Result.initialize");
        // Keep reference to query
        this.query = options.query;
    },
    
    parse: function(response) {
    	Saiku.logger.log("Result.parse");
        // Show the UI if hidden
        this.query.workspace.unblock();
        if (this.query.workspace.processing) {
            this.query.workspace.processing.hide();
        }
        this.result = response;
        if (!response.error) {
            this.query.model = _.extend({}, response.query);
        }
        this.firstRun = true;
		//Start Custom Code for Pagination
        this.query.set({'total_rows': response.page.totalRecords});
        this.query.set({'page': response.page.currentPageNumber});
        this.query.set({'max_page_no': response.page.totalPageCount});
        
        this.updatePagination();
        //End Custom Code for Pagination

        this.query.workspace.trigger('query:result', {
            workspace: this.query.workspace,
            data: response
        });

    },

    hasRun: function() {
    	Saiku.logger.log("Result.hasRun");
        return this.firstRun;
    },
    
    lastresult: function() {
    	Saiku.logger.log("Result.lastresult");
        return this.result;
    },
    
    url: function() {
    	Saiku.logger.log("Result.url");
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
    	Saiku.logger.log("Result.updatePagination");
    	//Start Custom Code for Pagination
    	if(this.query.get('max_page_no') === 0) {
    		this.query.set('page', 0);
    	}
    	/*this.set({'start': this.query.get('page')});
	    	this.set({'end': this.query.get('max_page_no')});*/
    }
});
