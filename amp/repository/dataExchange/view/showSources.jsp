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



<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event/event-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json-min.js"></script> 
<script type="text/javascript" src="<digi:file src='/TEMPLATE/ampTemplate/js_2/yui/dom/dom-min.js'/>" > </script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datatable/datatable-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/paginator/paginator-min.js"></script>
<!-- 
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/yuiloader-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/yui/event-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/yui/dom-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/datasource-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/datatable-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/button-min.js'/>" > </script>

 -->
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/dataExchange/scripts/logJs/logHelper.js'/>" > </script>


<script language="JavaScript">
	var msgDataError = '<digi:trn>Data error</digi:trn>';
	var msgLoading	 = '<digi:trn>Loading...</digi:trn>';
	
	YAHOO.util.Event.addListener(window, "load", initDynamicTable1);
		function initDynamicTable1() {	
				
		    YAHOO.example.XHR_JSON = new function() {		    			        
		        
		        var lastTimeStamp = new Date().getTime();

		        this.myDataSource = new YAHOO.util.DataSource("/dataExchange/manageSource.do?lastTimeStamp"+lastTimeStamp+"&");
		        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
		        //this.myDataSource.connXhrMode = "queueRequests";
		        this.myDataSource.responseSchema = {
		            resultsList: "SourceSetting",
		            fields: ["ID","Name", "Source", "Workspace"],
		            metaFields: {
		            	totalRecords: "totalRecords" // Access to value in the server response
		        	}    
		        };        
		        
		        var myColumnDefs = [
		            {key:"Name", label:"Name",sortable: true},
	                {key:"Source", label:"Source", sortable: true},
	                {key:"Workspace", label:"Workspace used", sortable:true}
		        ];
		  
		        //var div = document.getElementById('errors');
		
		        var handleSuccess = function(o){
		        	if(o.responseText != undefined){
		        		o.argument.oArgs.liner_element.innerHTML=o.responseText;
		        	}
		        }
		
		        //var handleFailure = function(o){
		        //	if(o.responseText != undefined){
		        //		div.innerHTML = "<li>Transaction id: " + o.tId + "</li>";
		        //		div.innerHTML += "<li>HTTP status: " + o.status + "</li>";
		        //		div.innerHTML += "<li>Status code message: " + o.statusText + "</li>";
		        //	}
		        //}
		        
		        // Create the Paginator 
		        var myPaginator = new YAHOO.widget.Paginator({ 
		        	rowsPerPage:10,
		        	//totalRecords:document.getElementById("totalResults").value,
		        	containers : ["dt-pag-nav","dt-pag-nav2"], 
		        	template : "{CurrentPageReport}&nbsp;<span class='l_sm'><digi:trn>Results:</digi:trn></span>&nbsp;{RowsPerPageDropdown}&nbsp;{FirstPageLink}{PageLinks}{LastPageLink}", 
		        	pageReportTemplate		: "<span class='l_sm'><digi:trn>Showing items</digi:trn></span> <span class='txt_sm_b'>{startIndex} - {endIndex} <digi:trn>of</digi:trn> {totalRecords}</span>", 
		        	rowsPerPageOptions		: [10,25,50,100,{value:999999,text:'<digi:trn jsFriendly="true">All</digi:trn>'}],
		        	firstPageLinkLabel : 	"<digi:trn>first page</digi:trn>", 
		        	previousPageLinkLabel : "<digi:trn>prev</digi:trn>", 
		        	firstPageLinkClass : "yui-pg-first l_sm",
		        	lastPageLinkClass: "yui-pg-last l_sm",
		        	nextPageLinkClass: "yui-pg-next l_sm",
		        	previousPageLinkClass: "yui-pg-previous l_sm",
		        	rowsPerPageDropdownClass:"l_sm",
		        	nextPageLinkLabel		: '<digi:trn jsFriendly="true">next</digi:trn>',
		        	lastPageLinkLabel		: '<digi:trn jsFriendly="true">last page</digi:trn>',
		        	 // use custom page link labels
		            pageLabelBuilder: function (page,paginator) {
		                var curr = paginator.getCurrentPage();
		                if(curr==page){
		                	return "<span class='current-page'>&nbsp;&nbsp;"+page+"&nbsp;&nbsp;</span>|";
		                }
		                else{
		                	return page;
		                }
		                
		            }

		        });
		         
		        var myConfigs = {
		            initialRequest: "sort=name&dir=asc&startIndex=0&results=10", // Initial request for first page of data
		            dynamicData: true, // Enables dynamic server-driven data
		            sortedBy : {key:"Name", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
		            //paginator: new YAHOO.widget.Paginator({ rowsPerPage:10 }) // Enables pagination
		            paginator:myPaginator,
		            MSG_ERROR:msgDataError,
		            MSG_LOADING:msgLoading
		        };
		    	 
		        this.myDataTable = new YAHOO.widget.DataTable("sourcesDiv", myColumnDefs, this.myDataSource, myConfigs);
		        //this.myDataTable.subscribe("rowClickEvent", onRowSelect);
		        this.myDataTable.subscribe("rowClickEvent", function (ev) {
	                var target = YAHOO.util.Event.getTarget(ev);
	                var record = this.getRecord(target);
	                alert(record);
	                document.getElementById("selectFileDiv").style.display = "none";
	        		refreshDetails(record.getData('ID'));
	               
	            });
		        
		        
		        //this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow); 
		        //this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow);
		       
		        
		        this.myDataTable.selectRow(this.myDataTable.getTrEl(0)); 
		        // Programmatically bring focus to the instance so arrow selection works immediately 
		        this.myDataTable.focus(); 
		
		        // Update totalRecords on the fly with value from server
		        this.myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
		           oPayload.totalRecords = oResponse.meta.totalRecords;
		           return oPayload;
		        }
		    };
	    
		}
		
</script>










<script type="text/javascript">
	LogPerExecutionConfig = {
			columnDefs: [	{key:"DbId", sortable:true, formatter:YAHOO.widget.DataTable.formatNumber, hidden: true},
	                   	   {key:"Name", sortable: true},
	                   	{key:"Source", label:"Source", sortable: true},
	                   	{key:"Workspace", label:"Workspace used", sortable:true}
	                   	   ],
	
			responseSchema: {
						resultNode: "SourceSetting",
						fields: [{key:"DbId", parser:YAHOO.util.DataSource.parseNumber},"Name", "Source", "Workspace"]
					}
	
	}
	dataSourceBuilder 	= new DataSourceBuilder("/dataExchange/manageSource.do",
			LogPerExecutionConfig.columnDefs ,LogPerExecutionConfig.responseSchema);
	
	//YAHOO.util.Event.addListener(window, "load", createDataTable );


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
		YAHOO.util.Connect.asyncRequest('POST', '/dataExchange/manageSource.do', getCallbackForSources("detailsDiv"), "selectedSourceId="+sourceId+"&action=showDetails" );
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
		YAHOO.util.Connect.asyncRequest('POST', '/dataExchange/manageSource.do', getCallbackForDelete(sourceId), "selectedSourceId="+sourceId+"&action=delete" );
	}
	
	
</script>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="90%" class="box-border-nopadding">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
		
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

