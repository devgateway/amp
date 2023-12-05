<!-- Individual YUI CSS files --> 

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/fonts/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/datatable/assets/skins/sam/datatable.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/datatable/assets/skins/sam/paginator.css" />

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/datatable/assets/skins/sam/container.css" />

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/paginator/paginator-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datatable/datatable-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/container/container-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script>

<!-- XML/Java Beautifier -->
<script type="text/javascript" src="/repository/xmlpatcher/js/sh_main.min.js"></script>
<script type="text/javascript" src="/repository/xmlpatcher/js/sh_xml.min.js"></script>
<script type="text/javascript" src="/repository/xmlpatcher/js/sh_java.min.js"></script>
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/sh_style.css" />

<style type="text/css"> 
/* Class for marked rows */
.yui-skin-sam .yui-dt tr.mark,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-desc,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.mark td.yui-dt-desc {
    background-color: #a33;
    color: #fff;
}

.yui-skin-sam .yui-dt tr.ymark,
.yui-skin-sam .yui-dt tr.ymark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.ymark td.yui-dt-desc,
.yui-skin-sam .yui-dt tr.ymark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.ymark td.yui-dt-desc {
    background-color: #FAFF61;
    color: #0;
}

.yui-skin-sam .yui-dt tr.deletedmark,
.yui-skin-sam .yui-dt tr.deletedmark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.deletedmark td.yui-dt-desc,
.yui-skin-sam .yui-dt tr.deletedmark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.deletedmark td.yui-dt-desc {
    text-decoration: line-through;
}


.yui-skin-sam .yui-dt tr.deprecatedmark,
.yui-skin-sam .yui-dt tr.deprecatedmark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.deprecatedmark td.yui-dt-desc,
.yui-skin-sam .yui-dt tr.deprecatedmark td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.deprecatedmark td.yui-dt-desc {
    color: #D6D6D6
}

.yui-skin-sam .yui-dt tr.yui-dt-selected td.yui-dt-asc,
.yui-skin-sam .yui-dt tr.yui-dt-selected td.yui-dt-desc{
    background-color:#426FD9;
}


</style>

<body class="yui-skin-sam">

<div id="popup">

</div>
        <h1 class="admintitle" style="text-align:left;">Database Patches</h1>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center>
	<tr>
		<td align=left class=r-dotted-lg valign="top">

<table cellPadding=5 cellspacing="0" width="100%" border="0" style="margin-bottom:15px;">
			<tr>
				<!-- Start Navigation -->
				<td height=33 bgcolor=#F2F2F2>
				<a href="/aim/admin.do" styleClass="comment"
					title="${translation}">
					<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
				</a>
				&nbsp;&gt;&nbsp; <a href="/xmlpatcher/xmlpatches.do?mode=listPatches"
					class="comment" title="${translation}">
					<digi:trn key="aim:dbpatches">Database Patches</digi:trn>
				</a>&nbsp;&gt;&nbsp; <digi:trn key="aim:dbpatches"><b>Database Patches</b></digi:trn></td>
				<!-- End navigation -->
			</tr>
		</table>
	<table cellpadding="3" cellspacing="1" style="border-color:#999999;border-spacing:2">
<tr><td bgcolor="#C0D6E2" align="center" style="color: #000000; font-size: 12px; font-weight:bold;" width="700px">Discovered Patches</td><td align="center" bgcolor="#C0D6E2" style="color: #000000; font-size: 12px; font-weight:bold;" width="400px">Patch Details</td></tr>
<tr><td rowspan="3" valign="top" width="700px"><div id="dynamicdata"></div> </td><td valign="top"><div id="patchBodyParent" style="height:400px;overflow:auto;"><div id="patchBody"></div></div></td></tr>
<tr><td bgcolor="#C0D6E2" align="center" style="color: #000000; font-size: 12px; font-weight:bold;" width="700px">Patch Logs</td></tr>
<tr><td><div id="patchLogs" style="height:400px;overflow:auto;"></div></td></tr>	
</table>	
		</td>
		</tr>
		</table>
		




</body>


<script type="text/javascript">
YAHOO.namespace("YAHOO.amp");
var popup = new YAHOO.widget.Panel("popup", { visible:false, constraintoviewport:true, draggable:true , height: "300px", width: "550px"} );
popup.render();

var handleFailure = function(o){
	if(o.responseText !== undefined){
		div.innerHTML = "<li>Transaction id: " + o.tId + "</li>";
		div.innerHTML += "<li>HTTP status: " + o.status + "</li>";
		div.innerHTML += "<li>Status code message: " + o.statusText + "</li>";
	}
}

var handleLogBodySuccess = function(o){
	if(o.responseText != undefined){
		popup.setBody('<div style="height:275px;width:545px;overflow:auto">'+o.responseText+'</div>');
	}
	sh_highlightDocument();
	popup.show(); 
}
   
function loadLogBody(patchLogId){
	var callback =
    {
      success:handleLogBodySuccess,
      failure: handleFailure
    }; 
	YAHOO.util.Connect.asyncRequest('GET','/xmlpatcher/xmlpatches.do?mode=logContents&patchLogId='+(patchLogId),callback);
	return true;
}


YAHOO.example.DynamicData = function() {
    var myColumnDefs = [           
                        {key:"patchId", label:"Name", sortable:true},
                        {key:"location",label:"Location",sortable:true},
                        {key:"discovered",label:"Discovered",sortable:true},
                        {key:"state", label:"State", sortable:true},
                        {key:"attempts",label:"Attempts",sortable:true}
                    ];
    


    var stringToDate = function(timestamp) {
        //function parses mysql datetime string and returns javascript Date object
        //input has to be in this format: 2007-06-05 15:26:02
        var regex=/^([0-9]{2,4})-([0-1][0-9])-([0-3][0-9]) (?:([0-2][0-9]):([0-5][0-9]):([0-5][0-9]))?$/;
        var parts=timestamp.replace(regex,"$1 $2 $3 $4 $5 $6").split(' ');
        return new Date(parts[0],parts[1]-1,parts[2],parts[3],parts[4],parts[5]);
      }

 	// Define a custom row formatter function
    var myRowFormatter = function(elTr, oRecord) {
       if (oRecord.getData('state') == 'FAILED') {
            YAHOO.util.Dom.addClass(elTr, 'mark');
        }
       if (oRecord.getData('state') == 'OPEN') {
           YAHOO.util.Dom.addClass(elTr, 'ymark');
       }

       if (oRecord.getData('state') == 'DELETED') {
           YAHOO.util.Dom.addClass(elTr, 'deletedmark');
       }

       if (oRecord.getData('state') == 'DEPRECATED') {
           YAHOO.util.Dom.addClass(elTr, 'deprecatedmark');
       }
        return true;
    }; 
    
    // DataSource instance
    var myDataSource = new YAHOO.util.DataSource("/xmlpatcher/xmlpatchesjson.do?");
    myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
    myDataSource.responseSchema = {
        resultsList: "items",
        fields: ["patchId","location",{key:"discovered",parser:stringToDate},"state","attempts"],
        metaFields: {
            totalRecords: "totalRecords" // Access to value in the server response
        }
    };
    
    // DataTable configuration
    var myConfigs = {
        initialRequest: "sort=discovered&dir=desc&startIndex=0&results=15", // Initial request for first page of data
        dynamicData: true, // Enables dynamic server-driven data
        sortedBy : {key:"discovered", dir:YAHOO.widget.DataTable.CLASS_DESC}, // Sets UI initial sort arrow
        paginator: new YAHOO.widget.Paginator({ rowsPerPage:15 }), // Enables pagination 
       	formatRow: myRowFormatter
    };
    
    // DataTable instance
    var myDataTable = new YAHOO.widget.DataTable("dynamicdata", myColumnDefs, myDataSource, myConfigs);
    // Update totalRecords on the fly with value from server
    myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
        oPayload.totalRecords = oResponse.meta.totalRecords;
        return oPayload;
    }


    
    function loadPatchBody(patchId){
    	var callback =
        {
          success:handlePatchBodySuccess,
          failure: handleFailure
        }; 
    	YAHOO.util.Connect.asyncRequest('GET','/xmlpatcher/xmlpatches.do?mode=patchContents&patchId='+(patchId),callback);
    	return true;
    }


    function loadPatchLogs(patchId) {
 	   var callback =
        {
          success:handlePatchLogsSuccess,
          failure: handleFailure
        }; 
 		YAHOO.util.Connect.asyncRequest('GET','/xmlpatcher/xmlpatches.do?mode=listPatchLogs&patchId='+(patchId),callback);
 	return true;
 	}

    
    
    var handlePatchBodySuccess = function(o){
    	if(o.responseText != undefined){
    		var element2 = document.getElementById("patchBody");
    		element2.innerHTML=o.responseText;
    	}
    	sh_highlightDocument();
    }

    var handlePatchLogsSuccess = function(o){
    	if(o.responseText != undefined){
    		var element2 = document.getElementById("patchLogs");
    		element2.innerHTML=o.responseText;
    	}
    }

 

	var rowClickEvent = function(oArgs) {
		 myDataTable.onEventSelectRow(oArgs);
		 var rec = this.getRecord(oArgs.target);
		 loadPatchBody(rec.getData("patchId"));
		 loadPatchLogs(rec.getData("patchId"))
	}
    
    // Subscribe to events for row selection
    myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow);
    myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow);
    myDataTable.subscribe("rowClickEvent", rowClickEvent);

    
    return {
        ds: myDataSource,
        dt: myDataTable
    };
        
}();
</script>
