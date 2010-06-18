<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<link rel="stylesheet" type="text/css" href="<digi:file src='module/dataExchange/scripts/logJs/css/fonts-min.css'/>">
<link rel="stylesheet" type="text/css" href="<digi:file src='module/dataExchange/scripts/logJs/css/datatable.css'/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/yuiloader-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/yui/event-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/yui/dom-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/datasource-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/datatable-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/button-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/logHelper.js'/>" > </script>

<script type="text/javascript">
	LogPerExecutionConfig = {
			columnDefs: [
	                   	   {key:"Date", sortable: true, formatter: YAHOOAmp.widget.DataTable.formatDate},
	                   	{key:"ExternalTimestamp", label:"External Timestamp", sortable: true},
	                   	{key:"Description"},
	                   	   ],
	
			responseSchema: {
						resultNode: "LogPerExecution",
						fields: [{key:"Date", parser: YAHOOAmp.util.DataSource.parseDate}, "ExternalTimestamp", "Description"]
					}
	
	}
	dataSourceBuilder 	= new DataSourceBuilder("/dataExchange/showLogs.do",
			LogPerExecutionConfig.columnDefs ,LogPerExecutionConfig.responseSchema);
	
	YAHOOAmp.util.Event.addListener(window, "load", function(){ dataSourceBuilder.createDataTable("logsPerExecutionDiv") } );
</script>
IT WORKS !!!!
<div class="yui-skin-sam">
<div id="logsPerExecutionDiv"></div>
</div>