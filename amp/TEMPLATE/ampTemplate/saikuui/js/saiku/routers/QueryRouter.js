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
        'report/open/:report_id': 'open_report'
    },
    new_query: function() {
    	Saiku.tabs.add(new Workspace());
    },
    
    open_query: function(query_name) {
        Settings.ACTION = "OPEN_QUERY";
        var options = {};
        var dataType = "text";
        if (!Settings.BIPLUGIN5 && Settings.BIPLUGIN) {
            var file = (Settings.GET.SOLUTION ? (Settings.GET.SOLUTION + "/") : "")
                        + (Settings.GET.PATH && Settings.GET.PATH != "/" ? (Settings.GET.PATH + "/") : "")
                        + (Settings.GET.ACTION || "");
            options = {
                file: file
            };
        } else {
            options = {
                file: query_name
            }
        }

        var params = _.extend({ 
                file: options.file
            }, Settings.PARAMS);

        var dialog = {
            populate: function(repository) {
                if (repository && repository.length > 0) {
                    var f = repository[0];
                    var query = new Query(params,{ name: options.file });
                    Saiku.tabs.add(new Workspace({ query: query, item: repository[0] }));

                } else {
                    Saiku.tabs.add(new Workspace());
                }
                Settings.INITIAL_QUERY = false;
            }
        };

        var repositoryFile = new Repository({}, { dialog: dialog }).fetch({ async: false, data: { path: options.file }});

        


        

    },
    open_report_old: function(report_id) {
        $.getJSON(Settings.AMP_PATH + "/" + report_id, function( data ) {
        	if(data.errorMessage)
    		{
        		//TODO: Replace with friendlier message
        		alert("Error opening report: " + data.errorMessage);
        		window.close();
    		}
			var query = new SavedQuery({file:'amp_source_file'});
			var xmlTemplate = XmlTemplates.templateMDX;
			xmlTemplate = xmlTemplate.replace("__MDX__", "");
			xmlTemplate = xmlTemplate.replace("__CUBE__", data.reportMetadata.cube);
			xmlTemplate = xmlTemplate.replace("__NAME__", data.reportMetadata.queryName);
			xmlTemplate = xmlTemplate.replace("__CONNECTION__", data.reportMetadata.connection);
            xmlTemplate = xmlTemplate.replace("__CATALOG__", data.reportMetadata.catalog);
			xmlTemplate = xmlTemplate.replace("__SCHEMA__", data.reportMetadata.schema);

			var model = Backbone.Model.extend({
				defaults: {
					file: data.reportMetadata.name,
					report_id: report_id
				},
				initialize: function(){
					console.log("model created");
				}
			});
			query.move_query_to_workspace(new model(), xmlTemplate, true);
        });
    	
    	
    },    
    open_report: function(report_id) {
        $.getJSON(Settings.AMP_PATH + "/" + report_id, function( data ) {
        	if(data.errorMessage)
    		{
        		//TODO: Replace with friendlier message
        		alert("Error opening report: " + data.errorMessage);
        		window.close();
    		}
			var query = new SavedQuery({file:'amp_source_file'});

        	templateQuery.name = data.reportMetadata.queryName;
        	templateQuery.mdx = "WITH\r\nSET [~ROWS] AS\r\n     {[Activity Texts.AMP ID].[AMP ID].Members}\r\nSELECT\r\nNON EMPTY {[Measures].[Actual Commitments]} ON COLUMNS,\r\nNON EMPTY [~ROWS] ON ROWS\r\nFROM [Donor Funding]";
			templateQuery.cube.uniqueName = data.reportMetadata.uniqueName;
			templateQuery.cube.name = data.reportMetadata.cube;
			templateQuery.cube.connection = data.reportMetadata.connection;
			templateQuery.cube.catalog = data.reportMetadata.catalog;
			templateQuery.cube.schema = data.reportMetadata.schema;
			templateQuery.reportSpec = data.reportSpec;
			Settings.RESULTS_PER_PAGE = data.reportMetadata.recordsPerPage;
			Settings.NUMBER_FORMAT_OPTIONS = Settings.Util.extractSettings(data.reportMetadata.settings);
			
			var model = Backbone.Model.extend({
				defaults: {
					file: data.reportMetadata.name,
					report_id: report_id,
					filters: data.reportMetadata.reportSpec.filters,
					settings: data.reportMetadata.reportSpec.settings,
					hierarchies : data.reportMetadata.reportSpec.hierarchies,
					columns : data.reportMetadata.reportSpec.columns,
					report_type : data.reportMetadata.reportSpec.reportType
				},
				initialize: function(){
					//console.log("model created");
				}
			});
			query.move_query_to_workspace_json(new model(), templateQuery);
        });
    	
    	
    },
    open_query_repository: function( ) {
        Toolbar.prototype.open_query( );
    }
});

Saiku.routers.push(new QueryRouter());



var templateQuery = {
   "queryModel":{

   },
   "cube":{
      "uniqueName":"[amp].[AMP].[AMP].[Donor Funding]",
      "name":"Donor Funding",
      "connection":"amp",
      "catalog":"AMP",
      "schema":"AMP",
      "caption":null,
      "visible":false
   },
   "mdx":"WITH\r\nSET [~ROWS] AS\r\n     {[Activity Texts.AMP ID].[AMP ID].Members}\r\nSELECT\r\nNON EMPTY {[Measures].[Actual Commitments]} ON COLUMNS,\r\nNON EMPTY [~ROWS] ON ROWS\r\nFROM [Donor Funding]",
   "name":"39D9EDF2-EC0A-F127-BB94-278217742C5F",
   "parameters":{

   },
   "plugins":{

   },
   "properties":{
      "saiku.olap.query.automatic_execution":true,
      "saiku.olap.query.nonempty":true,
      "saiku.olap.query.nonempty.rows":true,
      "saiku.olap.query.nonempty.columns":true,
      "saiku.ui.render.mode":"table",
      "saiku.olap.query.filter":true,
      "saiku.olap.result.formatter":"flat",
      "org.saiku.query.explain":true,
      "org.saiku.connection.scenario":false,
      "saiku.olap.query.drillthrough":true
   },
   "metadata":{

   },
   "queryType":"OLAP",
   "type":"QUERYMODEL"
};

var XmlTemplates = [];
XmlTemplates['templateMDX'] = '<?xml version="1.0" encoding="UTF-8"?> \
	<Query name="__NAME__" type="MDX" connection="__CONNECTION__" cube="__CUBE__" catalog="__CATALOG__" schema="__SCHEMA__"> \
	  <MDX>__MDX__</MDX> \
	  <Totals /> \
	  <Properties> \
	    <Property name="saiku.olap.query.nonempty" value="true" /> \
	    <Property name="saiku.olap.query.nonempty.rows" value="true" /> \
	    <Property name="org.saiku.query.explain" value="false" /> \
	    <Property name="org.saiku.connection.scenario" value="false" /> \
	    <Property name="saiku.ui.render.mode" value="table" /> \
	    <Property name="saiku.olap.query.nonempty.columns" value="true" /> \
	    <Property name="saiku.olap.query.drillthrough" value="true" /> \
	    <Property name="saiku.olap.query.automatic_execution" value="true" /> \
	  </Properties> \
	</Query> ';

