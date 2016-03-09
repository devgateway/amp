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
 * Router for opening query when session is initialized
 */
var QueryRouter = Backbone.Router.extend({
    routes: {
        'query/open/*query_name': 'open_query',
        'query/new_query': 'new_query',
        'query/open': 'open_query_repository',
        'report/open/:report_id': 'open_report',
        'report/run/:report_token': 'run_report'
    },
    new_query: function() {
    	Saiku.tabs.add(new Workspace());
    },
    run_report: function(report_token) {
    	$.getJSON(Settings.AMP_PATH + "/run/" + report_token, process_spec);	
    },    
    open_report: function(report_id) {
        $.getJSON(Settings.AMP_PATH + "/" + report_id, process_spec);
    },
    open_query: function(query_name) {
        console.error('code removed');
    },
    open_query_repository: function( ) {
        Toolbar.prototype.open_query( );
    }
});

Saiku.routers.push(new QueryRouter());


var process_spec = function(data) {
	if (data.errorMessage)
	{
		//TODO: Replace with friendlier message
		alert("Error opening report: " + data.errorMessage);
		window.close();
		return;
	}
	var query = new SavedQuery({file:'amp_source_file'});

	templateQuery.reportSpec = data.reportSpec;
	Settings.RESULTS_PER_PAGE = data.reportMetadata.recordsPerPage;
	Settings.NUMBER_FORMAT_OPTIONS = Settings.Util.extractSettings(data.reportMetadata.settings);
	var report_type = data.reportMetadata.reportType;
	var report_fieldname = (report_type === "IN_MEMORY") ? "report_token" : "report_id";
	var report_identifier = data.reportMetadata.reportIdentifier;
	var defaults = {
		file: data.reportMetadata.name,
		filters: data.reportMetadata.reportSpec.filters,
		settings: data.reportMetadata.reportSpec.settings,
		hierarchies : data.reportMetadata.reportSpec.hierarchies,
		columns : data.reportMetadata.reportSpec.columns,
		report_type : data.reportMetadata.reportSpec.reportType
	};
	defaults[report_fieldname] = report_identifier;
	var model = Backbone.Model.extend({
		defaults: defaults,
		initialize: function(){
			//console.log("model created");
		}
	});
	query.move_query_to_workspace_json(new model(), templateQuery);
};

var templateQuery = {
   "queryModel":{

   },
   "name":"39D9EDF2-EC0A-F127-BB94-278217742C5F",
   "parameters":{

   },
   "plugins":{

   },   
   "metadata":{

   }
};