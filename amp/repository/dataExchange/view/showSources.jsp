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
			columnDefs: [	{key:"DbId", sortable:true, formatter:YAHOOAmp.widget.DataTable.formatNumber, hidden: true},
	                   	   {key:"Name", sortable: true},
	                   	{key:"Source", label:"Source", sortable: true},
	                   	{key:"Workspace", label:"Workspace used", sortable:true}
	                   	   ],
	
			responseSchema: {
						resultNode: "SourceSetting",
						fields: [{key:"DbId", parser:YAHOOAmp.util.DataSource.parseNumber},"Name", "Source", "Workspace"]
					}
	
	}
	dataSourceBuilder 	= new DataSourceBuilder("/dataExchange/manageSource.do",
			LogPerExecutionConfig.columnDefs ,LogPerExecutionConfig.responseSchema);
	
	YAHOOAmp.util.Event.addListener(window, "load", createDataTable );


	function createDataTable() {
		var dt	= dataSourceBuilder.createDataTable("sourcesDiv");
		dt.subscribe("rowClickEvent", onRowSelect);
	}

	function onRowSelect(o) {
		var dt 		= 	dataSourceBuilder.dataTable;
		dt.onEventSelectRow(o);
		var id		= dataSourceBuilder.getValueOfFirstColumn();

		document.getElementById("selectFileDiv").style.display = "none";
		refreshDetails(id);
	}
	
	function getCallbackForSources (divIdentifier) {
		var callbackObj	= new Object();
		callbackObj.divEl	= document.getElementById(divIdentifier);
		callbackObj.success	= function (o) {
									this.divEl.innerHTML = o.responseText ;
								};
		callbackObj.failure	= function () {
								this.divEl.innerHTML = "<div align='center'><font color='red'>We are sorry but your request cannot be processed at this time</font></div>";
							};
			

		return callbackObj;
		
	}
	
	function refreshDetails( sourceId ) {
		YAHOOAmp.util.Connect.asyncRequest('POST', '/dataExchange/manageSource.do', getCallbackForSources("detailsDiv"), "selectedSourceId="+sourceId+"&action=showDetails" );
	}

	function executeSource( sourceId, sourceType ) {
		var divEl	= document.getElementById("selectFileDiv");
		document.getElementById("executingSourceId").value	= sourceId;
		if (sourceType == "FILE") {
			divEl.style.display = "block";
		}
		else {
			document.forms['manageSourceForm'].submit();
		}
	}


	function getCallbackForDelete (sourceId) {
		var callbackObj	= new Object();
		callbackObj.sourceId	= sourceId;
		callbackObj.divEl		= document.getElementById("detailsDiv");
		callbackObj.success		= function (o) {
									this.divEl.innerHTML = "" ;
									var dt 		= 	dataSourceBuilder.dataTable;
									var trEl 	= (dt.getSelectedTrEls()[0]);
									dt.deleteRow(trEl);
									
								};
		callbackObj.failure	= function () {
								this.divEl.innerHTML = "<div align='center'><font color='red'>We are sorry but your request cannot be processed at this time</font></div>";
							};
			

		return callbackObj;
		
	}
	
	function deleteSource(sourceId) {
		document.getElementById("detailsDiv").innerHTML = '<digi:trn>Deleting</digi:trn>...';
		YAHOOAmp.util.Connect.asyncRequest('POST', '/dataExchange/manageSource.do', getCallbackForDelete(sourceId), "selectedSourceId="+sourceId+"&action=delete" );
	}
	
	
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
			
			
			<digi:trn>
				Import Manager
			</digi:trn>
		</div>
		
<div style="float: left;width: 60%;" class="yui-skin-sam">
	<div style="width: 500px; margin-left: auto; margin-right: auto;margin-bottom:20px; border: 1px gray solid;text-align: center;">
		<div style="width: 100%; margin-left: auto; margin-right: auto;background-color:#006699; color: white; ">List of sources</div>
		<div id="sourcesDiv" style="margin-left: auto; margin-right: auto;width: auto; height: 400px;overflow: auto; "></div>
		<a href="/dataExchange/createSource.do?htmlView=true"><digi:trn>Create new source</digi:trn></a><br/>
	</div>
</div>
<br />

<div style="float: left; width: 40%;" >
	<div id="detailsDiv"></div>
	<div id="selectFileDiv" style="display: none; margin-top: 100px;">
		<digi:form action="/manageSource.do" enctype="multipart/form-data">
		<table>
			<tr>
				<td style="text-align: center; background: #006699; color: white;"><digi:trn>File Selector</digi:trn></td>
			</tr>
			<tr>
				<td style="text-align: center;">
					<html:hidden property="action" value="execute"/>
						<html:hidden property="executingSourceId" styleId="executingSourceId"/>
						<html:file property="xmlFile"></html:file>
				</td>
			</tr>
			<tr>
				<td style="text-align: center;">
					<html:submit>Execute</html:submit>
				</td>
			</tr>
		</table>	
		</digi:form>
	</div>
</div>

		</td>
	</tr>
</table>

