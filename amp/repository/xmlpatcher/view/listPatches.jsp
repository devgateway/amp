<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<%@ page import="java.util.Map,java.util.List,java.util.ArrayList"%>

<head>
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/datatable.css" />
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/paginator.css" />

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datatable-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/event-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/paginator-min.js"></script> 

<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/rowexpand.css"/>
<script type="text/javascript" src="/repository/xmlpatcher/js/rowexpand.js"></script>

<style type="text/css">
/* custom styles for this example */
.yui-skin-sam .yui-dt-body { cursor:pointer; } /* when rows are selectable */
#single { margin-top:2em; }
</style>




        <script type="text/javascript" src="<digi:file src="script/yui/container_core-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/connection-min.js"/>"></script>
        
        <!-- Source File -->
        <script type="text/javascript" src="<digi:file src="script/yui/menu-amp-min.js"/>"></script>
        <script type="text/javascript" src="<digi:file src="script/yui/container-min.js"/>"></script> 
        <script type="text/javascript" src="<digi:file src="script/yui/tabview-min.js"/>"></script> 

        <!-- Core + Skin CSS -->
        <digi:ref href="css/menu.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/tabview.css" type="text/css" rel="stylesheet" />
        <digi:ref href="css/container.css" type="text/css" rel="stylesheet" />

        <!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="/repository/xmlpatcher/js/sh_main.min.js"></script>
<script type="text/javascript" src="/repository/xmlpatcher/js/sh_xml.min.js"></script>
<script type="text/javascript" src="/repository/xmlpatcher/js/sh_java.min.js"></script>
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/sh_style.css" />

<script type="text/javascript">

YAHOO.util.Event.addListener(window, "load", function() {
	
    YAHOO.example.XHR_JSON = new function() {
        this.formatUrl = function(elCell, oRecord, oColumn, sData) {
            elCell.innerHTML = "<a style='text-decoration: underline' onclick=loadPatchBody(" +"'" +oRecord.getData("ID") + "'"+")>" + sData + "</a>";
        };

        var expansionFormatter  = function(el, oRecord, oColumn, oData) {
            var cell_element    = el.parentNode;

            //Set trigger
            if( oData ){ //Row is closed
                YAHOO.util.Dom.addClass( cell_element,
                    "yui-dt-expandablerow-trigger" );
            }

        };

     

        
        this.myDataSource = new YAHOO.util.DataSource("/xmlpatcher/xmlpatchesjson.do");
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        this.myDataSource.connXhrMode = "queueRequests";
        this.myDataSource.responseSchema = {
            resultsList: "items",
            fields: ["ID","farm","Location",{key:"Discovered"},{key:"State",parser:"number"}],
            metaFields: {
            	totalRecords: "totalRecords" // Access to value in the server response
        	}    
        };
        
        
        var myColumnDefs = [
			{key:"ID",label:"",formatter:expansionFormatter},                        
            {key:"ID", label:"Name", sortable:true, formatter:this.formatUrl},
            {key:"Location"},
            {key:"Discovered"},
            {key:"State", label:"State", formatter:YAHOO.widget.DataTable.formatNumber, sortable:true}
        ];


        var div = document.getElementById('errors');

        var handleSuccess = function(o){
        	if(o.responseText != undefined){
        		o.argument.oArgs.liner_element.innerHTML=o.responseText;
        	}
        }

        var handleFailure = function(o){
        	if(o.responseText !== undefined){
        		div.innerHTML = "<li>Transaction id: " + o.tId + "</li>";
        		div.innerHTML += "<li>HTTP status: " + o.status + "</li>";
        		div.innerHTML += "<li>Status code message: " + o.statusText + "</li>";
        	}
        }

     

        var expansionTemplate = function( oArgs ){
        	   var callback =
               {
                 success:handleSuccess,
                 failure: handleFailure,
                 argument: { oArgs:oArgs}
               }; 
        	YAHOO.util.Connect.asyncRequest('GET','/xmlpatcher/xmlpatches.do?mode=listPatchLogs&patchId='+(oArgs.data.getData().ID),callback);
        	return true;
        }

        // DataTable configuration
        var myConfigs = {
            initialRequest: "sort=ID&dir=asc&startIndex=0&results=10", // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"ID", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            paginator: new YAHOO.widget.Paginator({ rowsPerPage:10 }), // Enables pagination
            rowExpansionTemplate:expansionTemplate 
        };
            
        this.myDataTable = new YAHOO.widget.DataTable("json", myColumnDefs,
                this.myDataSource, myConfigs);

        // Subscribe to events for row selection
       this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow);
       this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow);
        
       //Subscribe to a click event to bind to
       this.myDataTable.subscribe( 'cellClickEvent',this.myDataTable.onEventToggleRowExpansion );
      
       // Update totalRecords on the fly with value from server
       this.myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
           oPayload.totalRecords = oResponse.meta.totalRecords;
           return oPayload;
       }

    };

    
});

</script>
</head>

<body class="yui-skin-sam">

<script type="text/javascript">
var myPanel2 = new YAHOO.widget.Panel("myPanel2", {
    fixedcenter: true,
    maxwidth:"700px",
    constraintoviewport: true,
    underlay:"shadow",
    close:true,
    visible:false,
    modal:false,
    draggable:true} );

myPanel2.setBody("");
myPanel2.render(document.body);

function showPatch() {
		var element2 = document.getElementById("patchContent");
		element2.style.display 	= "inline";
		myPanel2.setBody(element2);
	myPanel2.show();
}

function showLog() {
	var element2 = document.getElementById("logContent");
	element2.style.display 	= "inline";
	myPanel2.setBody(element2);
	myPanel2.show();
}

var handleFailure = function(o){
	if(o.responseText !== undefined){
		div.innerHTML = "<li>Transaction id: " + o.tId + "</li>";
		div.innerHTML += "<li>HTTP status: " + o.status + "</li>";
		div.innerHTML += "<li>Status code message: " + o.statusText + "</li>";
	}
}

var handlePatchBodySuccess = function(o){
	if(o.responseText != undefined){
		var element2 = document.getElementById("patchContent");
		element2.innerHTML=o.responseText;
	}
	sh_highlightDocument();
	showPatch();
}

function loadPatchBody( patchId ){
	var callback =
    {
      success:handlePatchBodySuccess,
      failure: handleFailure
    }; 
	YAHOO.util.Connect.asyncRequest('GET','/xmlpatcher/xmlpatches.do?mode=patchContents&patchId='+(patchId),callback);
	return true;
}

var handleLogBodySuccess = function(o){
	if(o.responseText != undefined){
		var element2 = document.getElementById("logContent");
		element2.innerHTML=o.responseText;
	}
	sh_highlightDocument();
	showLog();
}

function loadLogBody( patchLogId ){
	var callback =
    {
      success:handleLogBodySuccess,
      failure: handleFailure
    }; 
	YAHOO.util.Connect.asyncRequest('GET','/xmlpatcher/xmlpatches.do?mode=logContents&patchLogId='+(patchLogId),callback);
	return true;
}
</script>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=760px>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750px>
		<table cellPadding=5 cellSpacing=0 width="100%" border=0>
			<tr>
				<td height=33><span class=crumb> <c:set
					var="clickToViewAdmin">
					<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
				</c:set> <a href="/aim/admin.do" styleClass="comment"
					title="${clickToViewAdmin}">
					<digi:trn key="aim:AmpAdminHome"> Admin Home </digi:trn>
				</a> &nbsp;&gt;&nbsp; <digi:trn key="aim:patchListViewer">Database Patches</digi:trn>
				</span></td>
			</tr>
			<tr>
				<td height=16 vAlign="center">
				<div id="json"></div>

				<div id="errors"></div>

				</td>
			</tr>

		</table>
		</td>
	</tr>
</table>

<div id="patchContent" style="display: none">
</div>

<div id="logContent" style="display: none">
</div>

<a href="/aim/ListAppliedPatches.do">Old Applied Patches List</a>

</body> 