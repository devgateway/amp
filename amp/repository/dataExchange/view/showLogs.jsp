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

<style type="text/css">
.yui-skin-sam .yui-dt tr.mark,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-desc,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-desc
	{
	background-color: #a33;
	color: #fff;
}
</style>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/yuiloader-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/yui/event-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/yui/dom-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/datasource-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/datatable-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/button-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/logHelper.js'/>" > </script>

<script type="text/javascript">
	LogPerItemConfig = {
			columnDefs: [
						{key:"Number", sortable: true, formatter:YAHOOAmp.widget.DataTable.formatNumber},
						{key:"LogLevel", sortable: true, label: "Log Level", formatter: lpiFormatter},
						{key:"Name", sortable: true},
	                   	   {key:"Date", sortable: true, formatter: YAHOOAmp.widget.DataTable.formatDate},
	                   	   {key:"Time"},
	                   	{key:"Description", hidden: true},
	                   	   ],
	
			responseSchema: {
						resultNode: "LogPerItem",
						fields: [ {key:"Number",parser:YAHOOAmp.util.DataSource.parseNumber},"LogLevel", "Name",
									{key:"Date", parser: YAHOOAmp.util.DataSource.parseDate}, "Time", "Description"]
					}
	
	};
	
	LogPerExecutionConfig = {
			columnDefs: [
			             {key:"DbId", sortable: true, formatter:YAHOOAmp.widget.DataTable.formatNumber},
	                   	   {key:"Date", sortable: true, formatter: YAHOOAmp.widget.DataTable.formatDate},
	                   	   {key:"Time"},
	                   	{key:"ExternalTimestamp", label:"External Timestamp", sortable: true},
	                   	{key:"Description"},
	                   	   ],
	
			responseSchema: {
						resultNode: "LogPerExecution",
						fields: [{key:"DbId", parser:YAHOOAmp.util.DataSource.parseNumber},
									{key:"Date", parser: YAHOOAmp.util.DataSource.parseDate},"Time", "ExternalTimestamp", "Description"]
					}
	
	};


	
	dsBuilderPerExec 	= new DataSourceBuilder("/dataExchange/showLogs.do",
			LogPerExecutionConfig.columnDefs ,LogPerExecutionConfig.responseSchema);
	

	function createDataTablePerExecution() {
		var dt	= dsBuilderPerExec.createDataTable("logsPerExecutionDiv");
		dt.subscribe("rowClickEvent", onExecRowSelect);
	}

	function onExecRowSelect(o) {
		var dt 		= 	dsBuilderPerExec.dataTable;
		dt.onEventSelectRow(o);
		var id		= dsBuilderPerExec.getValueOfFirstColumn();

		refreshLogItemDetails(id);
	}

	function onItemRowSelect(o) {
		var dt		= dsBuilderPerItem.dataTable;
		dt.onEventSelectRow(o);

		var id		= dsBuilderPerItem.getValueOfFirstColumn();
		var pw		= new MyPanelWrapper("Log Details", "/dataExchange/showLogs.do?selectedLogPerItemId="+id );
	}

	function refreshLogItemDetails( id ){ 
		dsBuilderPerItem	= new DataSourceBuilder("/dataExchange/showLogs.do?selectedLogPerExecId="+id,
				LogPerItemConfig.columnDefs ,LogPerItemConfig.responseSchema);
		//dsBuilderPerItem.errorRows	= new Object();
		document.getElementById("logsPerItemDivWrapper").style.display	= "";	
		var dt	= dsBuilderPerItem.createDataTable("logsPerItemDiv");
		dt.subscribe("rowClickEvent", onItemRowSelect );
	}

	function lpiFormatter(cell,rec,col,data) {
		if ( data == "ERROR" ) {
			//dsBuilderPerItem.errorRows[rec.getId()]	= rec;
			cell.innerHTML = "<span style='color: red; font-weight:bold;'>" + data  +"<span>"; 
		}
		else
			cell.innerHTML	= "<span>" + data + "</span>";
			
	}
	
	function refreshColors() {
		YAHOOAmp.util.Dom.removeClass(YAHOOAmp.util.Dom.getElementsByClassName('mark','tr','logsPerItemDiv'), 'mark'); 
		for (var recId in dsBuilderPerItem.errorRows) {
			YAHOOAmp.util.Dom.addClass( dsBuilderPerItem.dataTable.getTrEl(dsBuilderPerItem.errorRows[recId])  ,"mark");
		}
	}
	
	YAHOOAmp.util.Event.addListener(window, "load", createDataTablePerExecution );
</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="90%" class="box-border-nopadding">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
		
		<div style="">
			<c:set var="translation">
				<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
			</c:set>
			<digi:link href="/admin.do" styleClass="comment" title="${translation}" contextPath="/aim">
			<digi:trn key="aim:AmpAdminHome">
			Admin Home
			</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;
			<digi:link href="/manageSource.do?htmlView=true" styleClass="comment">
			<digi:trn>
			Source Manager
			</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;
			
			<digi:trn>
				Source Wizard
			</digi:trn>
		</div>
			<br />
			<div class="yui-skin-sam">
				<div style=" margin-left: auto; margin-right: auto;margin-bottom:20px; border: 1px gray solid;text-align: center; float: left;">
					<div style="width: 100%; margin-left: auto; margin-right: auto;background-color:#006699; color: white; ">
						<digi:trn>Source Executions</digi:trn>
					</div>
					<div id="logsPerExecutionDiv"></div>
				</div>
				<div style="width: 20px; float: left;">&nbsp;</div>
				<div id="logsPerItemDivWrapper" 
					style=" margin-left: auto; margin-right: auto;margin-bottom:20px; border: 1px gray solid;text-align: center;float: left; display: none;">
					<div style="width: 100%; margin-left: auto; margin-right: auto;background-color:#006699; color: white; ">
						<digi:trn>Execution Log</digi:trn>
					</div>
					<div id="logsPerItemDiv"></div>
				</div>
			</div>
			<br />

		</td>
	</tr>
</table>