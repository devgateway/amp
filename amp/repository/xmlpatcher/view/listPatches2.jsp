<!-- Individual YUI CSS files --> 

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/datatable.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/paginator.css" />

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/container.css" />

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/connection-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/paginator-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datatable-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/container-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/dragdrop-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/animation-min.js"></script> 

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


</style>

<body class="yui-skin-sam">

<div id="popup">

</div>

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 >
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top>

<table cellPadding=5 cellSpacing=0 width="100%" border=0>
			<tr>
				<!-- Start Navigation -->
				<td height=33>
				<a href="/aim/admin.do" styleClass="comment"
					title="${translation}">
					<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
				</a>
				&nbsp;&gt;&nbsp; <a href="/xmlpatcher/xmlpatches.do?mode=listPatches"
					class="comment" title="${translation}">
					<digi:trn key="aim:dbpatches">Database Patches</digi:trn>
				</a>&nbsp;&gt;&nbsp; <digi:trn key="aim:dbpatches">Database Patches</digi:trn></td>
				<!-- End navigation -->
			</tr>
		</table>
	<table cellpadding="3" cellspacing="1" style="border-color:#999999;border-spacing:2">
<tr><td bgcolor="#05528b" align="center" style="color: #ffffff; font-size: larger;" width="700px">Discovered Patches</td><td align="center" bgcolor="#05528b" style="color: #ffffff; font-size: larger;" width="400px">Patch Details</td></tr>
<tr><td rowspan="3" valign="top" width="700px"><div id="dynamicdata"></div> </td><td valign="top"><div id="patchBody"  style="height:400px;overflow:auto;"></div> </td></tr>
<tr><td bgcolor="#05528b" align="center" style="color: #ffffff; font-size: larger;" width="700px">Patch Logs</td></tr>
<tr><td><div id="patchLogs" style="height:400px;overflow:auto;"></div></td></tr>	
</table>	
		</td>
		</tr>
		</table>
		




</body>


<script type="text/javascript">

popup = new YAHOO.widget.Panel("popup", { visible:false, constraintoviewport:true, draggable:true , height: "300px", width: "550px"} );
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
