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
        'query/open': 'open_query_repository',
        'report/:report_id': 'open_report'
    },
    
    open_query: function(query_name) {
        Settings.ACTION = "OPEN_QUERY";
        var options = {};
        var dataType = "text";
        if (Settings.BIPLUGIN) {
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
        var query = new SavedQuery(options);
        query.fetch({ success: query.move_query_to_workspace, dataType: dataType});
    },

    open_query_repository: function( ) {
        Toolbar.prototype.open_query( );
    },
    
    open_report: function(report_id) {
            $.getJSON(Settings.AMP_PATH + "/" + report_id, function( data ) {
				var query = new SavedQuery({file:'amp_source_file'});
				var xmlTemplate = '<?xml version="1.0" encoding="UTF-8"?> \
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
				xmlTemplate = xmlTemplate.replace("__MDX__", data.mdx);
				xmlTemplate = xmlTemplate.replace("__CATALOG__", data.reportMetadata.catalog);
				xmlTemplate = xmlTemplate.replace("__CUBE__", data.reportMetadata.cube);
				xmlTemplate = xmlTemplate.replace("__NAME__", data.reportMetadata.queryName);
				xmlTemplate = xmlTemplate.replace("__CONNECTION__", data.reportMetadata.connection);
				xmlTemplate = xmlTemplate.replace("__SCHEMA__", data.reportMetadata.schema);

				var model = Backbone.Model.extend({
					defaults: {
						file: data.reportMetadata.name
					},
					initialize: function(){
						console.log("model created");
					}
				});

				query.move_query_to_workspace(new model(), xmlTemplate);
            });
    }

});

Saiku.routers.push(new QueryRouter());
