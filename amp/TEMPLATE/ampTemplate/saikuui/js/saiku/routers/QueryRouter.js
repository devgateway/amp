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

var XmlTemplates = {};
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
	</Query> ',
XmlTemplates['templateQuery'] = '<?xml version="1.0" encoding="UTF-8"?> \
	<Query name="__NAME__" type="QM" connection="__CONNECTION__" cube="__CUBE__" catalog="__CATALOG__" schema="__SCHEMA__"> \
    <QueryModel> \
      <Axes> \
        <Axis location="ROWS" nonEmpty="true"> \
			<Dimensions> \
				__ROWS__ \
			</Dimensions> \
        </Axis> \
        <Axis location="COLUMNS" nonEmpty="true"> \
			<Dimensions> \
				__COLUMNS__ \
			</Dimensions> \
        </Axis> \
        <Axis location="FILTER" nonEmpty="false" /> \
      </Axes> \
    </QueryModel> \
    <MDX></MDX> \
    <Totals /> \
    <Properties> \
      <Property name="saiku.ui.render.mode" value="table" /> \
      <Property name="org.saiku.query.explain" value="true" /> \
      <Property name="saiku.olap.query.nonempty.columns" value="true" /> \
      <Property name="saiku.olap.query.nonempty.rows" value="true" /> \
      <Property name="org.saiku.connection.scenario" value="false" /> \
      <Property name="saiku.olap.query.automatic_execution" value="true" /> \
      <Property name="saiku.olap.query.drillthrough" value="true" /> \
      <Property name="saiku.olap.query.filter" value="false" /> \
      <Property name="saiku.olap.query.limit" value="false" /> \
      <Property name="saiku.olap.query.nonempty" value="true" /> \
    </Properties> \
      </Query>';

var dimensionXml = '<Dimension name="__ENTITYNAME__" hierarchizeMode="PRE" hierarchyConsistent="true"> \
	<Inclusions> \
		__INCLUSIONS__ \
	</Inclusions> \
	<Exclusions /> \
	</Dimension>';

var selectionXml = '<Selection dimension="__ENTITYNAME__" type="__TYPE__" node="__NAME__" operator="__OPERATOR__" />';

//var measureXml = '<Selection dimension="Measures" type="member" node="__MEASURE__" operator="MEMBER" />';

var DimensionMap = {
		"Project Title": {
			entityName: "Project Title",
			dimensionName: "[Project Title].[Project Title]",
		},
		"Region":{
			entityName: "Location",
			dimensionName: "[Location.Locations].[Country Name]",
		},
		"Donor Group":{
			entityName: "Donor Agency",
			dimensionName: "[Donor Agency].[Organization Group Name]"
		},
		"Zone":{
			entityName: "Location",
			dimensionName: "[Location.Locations].[Zone Name]"
		},
		"District":{
			entityName: "Location",
			dimensionName: "[Location.Locations].[District Name]"
		},
		"Primary Sector":{
			entityName: "Primary Sector",
			dimensionName: "[Primary Sector].[Primary Sector]"
		},
		"Primary Sector":{
			entityName: "Primary Sector",
			dimensionName: "[Primary Sector].[Primary Sector Sub-sector]"
		},
		"Executing Agency":{
			entityName: "Executing Agency",
			dimensionName: "[Executing Agency].[Organization Name]"
		},
		"Responsible Agency":{
			entityName: "Responsible  Agency",
			dimensionName: "[Responsible Organization].[Organization Name]"
		},
		"AMP ID":{
			entityName: "AMP ID",
			dimensionName: "[AMP ID].[AMP ID]"
		}
};

var MeasureMap = {
		"Actual Commitments":"[Measures].[Actual Commitments]",
		"Planned Commitments":"[Measures].[Planned Commitments]",
		"Actual Disbursements":"[Measures].[Actual Disbursements]",
		"Planned Disbursements":"[Measures].[Planned Disbursements]",
};

/**
 * Router for opening query when session is initialized
 */
var QueryRouter = Backbone.Router.extend({
    routes: {
        'query/open/*query_name': 'open_query',
        'query/open': 'open_query_repository',
        'report/mdx/:report_id': 'open_report_mdx',
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
    
    open_report_mdx: function(report_id) {
            $.getJSON(Settings.AMP_PATH + "/" + report_id, function( data ) {
				var query = new SavedQuery({file:'amp_source_file'});
				var xmlTemplate = XmlTemplates.templateMDX;
				xmlTemplate = xmlTemplate.replace("__MDX__", data.mdx);
				xmlTemplate = xmlTemplate.replace("__CUBE__", data.reportMetadata.cube);
				xmlTemplate = xmlTemplate.replace("__NAME__", data.reportMetadata.queryName);
				xmlTemplate = xmlTemplate.replace("__CONNECTION__", data.reportMetadata.connection);
                xmlTemplate = xmlTemplate.replace("__CATALOG__", data.reportMetadata.catalog);
				xmlTemplate = xmlTemplate.replace("__SCHEMA__", data.reportMetadata.schema);

				var model = Backbone.Model.extend({
					defaults: {
						file: data.reportMetadata.name
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
			var query = new SavedQuery({file:'amp_source_file'});
			var xmlTemplate = XmlTemplates.templateQuery;
			var columns = _.map(data.reportMetadata.reportSpec.columns, function(v) {
    			return DimensionMap[v.entityName];
        	});
			var rowsXML = _.map(_.groupBy(_.compact(columns), 'entityName'), function(value, key) {
				var currDimensionXml = dimensionXml.replace(/__ENTITYNAME__/g, key);
				var measuresXML = _.map(value, function(v){
					var currSelectionXml = selectionXml.replace(/__ENTITYNAME__/g, v.entityName);
					currSelectionXml = currSelectionXml.replace(/__NAME__/g, v.dimensionName);
					currSelectionXml = currSelectionXml.replace(/__OPERATOR__/g, "MEMBERS");
					currSelectionXml = currSelectionXml.replace(/__TYPE__/g, "level");
					return currSelectionXml;
				});
				return currDimensionXml.replace(/__INCLUSIONS__/g, measuresXML.join(''));
			});
			var measureDimensionXml = dimensionXml.replace(/__ENTITYNAME__/g, "Measures");
			var measuresXML = _.map(data.reportMetadata.reportSpec.measures, function(v){
				if( MeasureMap[v.entityName]) {
					var currSelectionXml = selectionXml.replace(/__ENTITYNAME__/g, "Measures");
					currSelectionXml = currSelectionXml.replace(/__NAME__/g, MeasureMap[v.entityName]);
					currSelectionXml = currSelectionXml.replace(/__OPERATOR__/g, "MEMBER");
					currSelectionXml = currSelectionXml.replace(/__TYPE__/g, "member");
					return currSelectionXml;
				}
			});
			measuresXML = _.compact(measuresXML);
			
			var columnsXml = measureDimensionXml.replace(/__INCLUSIONS__/g, measuresXML.join(''));
			xmlTemplate = xmlTemplate.replace(/__ROWS__/g, rowsXML.join(''));
			xmlTemplate = xmlTemplate.replace(/__COLUMNS__/g, columnsXml);
			xmlTemplate = xmlTemplate.replace("__MDX__", data.mdx);
			xmlTemplate = xmlTemplate.replace("__CUBE__", data.reportMetadata.cube);
			xmlTemplate = xmlTemplate.replace("__NAME__", data.reportMetadata.queryName);
			xmlTemplate = xmlTemplate.replace("__CONNECTION__", data.reportMetadata.connection);
            xmlTemplate = xmlTemplate.replace("__CATALOG__", data.reportMetadata.catalog);
			xmlTemplate = xmlTemplate.replace("__SCHEMA__", data.reportMetadata.schema);

			var model = Backbone.Model.extend({
				defaults: {
					file: data.reportMetadata.name
				},
				initialize: function(){
					console.log("model created");
				}
			});
			query.move_query_to_workspace(new model(), xmlTemplate, true);
        });
}

    
});

Saiku.routers.push(new QueryRouter());
