<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/datatable.css" />
<link rel="stylesheet" type="text/css" href="/repository/xmlpatcher/css/paginator.css" />
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/repository/xmlpatcher/js/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/connection-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/element-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/datasource-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/datatable-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/json-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/yahoo-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/event-min.js"></script> 
<script type="text/javascript" src="/repository/xmlpatcher/js/paginator-min.js"></script> 
<style>
.yui-skin-sam .yui-dt th, .yui-skin-sam .yui-dt th a {
color:#000000;
font-weight:bold;
font-size: 11px;
text-decoration:none;
vertical-align:bottom;
}
.yui-skin-sam th.yui-dt-asc, .yui-skin-sam th.yui-dt-desc {
background:#B8B8B0;
}
.yui-skin-sam .yui-dt th {
background:#B8B8B0;
}
.yui-skin-sam th.yui-dt-asc .yui-dt-liner {
background:transparent url(/repository/aim/images/up.gif) no-repeat scroll right center;
}
.yui-skin-sam th.yui-dt-desc .yui-dt-liner {
background:transparent url(/repository/aim/images/down.gif) no-repeat scroll right center;
}

</style>
<script language="JavaScript">

YAHOO.util.Event.addListener(window, "load", initDynamicTable);
	function initDynamicTable() {
	
    YAHOO.example.XHR_JSON = new function() {
 	
       	         
        this.formatActions = function(elCell, oRecord, oColumn, sData) {
        	elCell.innerHTML = 
        	"<a href=/aim/getWorkspace.do~dest=admin~event=edit~tId=" +oRecord.getData( 'ID' )+" title='<digi:trn>Click here to Edit Workspace</digi:trn>'>" + "<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png'/>" + "</a>&nbsp;&nbsp;&nbsp;&nbsp;"+
        	"<a href=/aim/deleteWorkspace.do~event=delete~tId=" +oRecord.getData( 'ID' )+" title='<digi:trn>Click here to Delete Workspace</digi:trn>'>" + "<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif'/>" + "</a>&nbsp;&nbsp;&nbsp;&nbsp;"+
        	"[<a href=\"JavaScript:openNpdSettingsWindow(" +oRecord.getData( 'ID' )+ ");\">"+"<digi:trn>Npd Settings</digi:trn>"+"</a>]"
        };
        
        this.formatActionsName = function(elCell, oRecord, oColumn, sData) {
        	elCell.innerHTML = 
        	'<a href="JavaScript:showTeamDetails(' +oRecord.getData( 'ID' )+',  \''+oRecord.getData( 'name' )+'\');" title="<digi:trn>Click here to view Details</digi:trn>">' + oRecord.getData( 'name' ) + '</a>'
        };
 
        this.myDataSource = new YAHOO.util.DataSource("/aim/searchWorkspaces.do?");
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        //this.myDataSource.connXhrMode = "queueRequests";
        this.myDataSource.responseSchema = {
            resultsList: "workspaces",
            fields: ["ID","name"],
            metaFields: {
            	totalRecords: "totalRecords" // Access to value in the server response
        	}    
        };
        
        
        var myColumnDefs = [
            {key:"name", label:"<digi:trn>NAME</digi:trn>", sortable:true, width: 250},
            {key:"actions", label:"<digi:trn>ACTIONS</digi:trn>", width: 150, formatter:this.formatActions,className:"ignore"}
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
        // Create the Paginator 
        var myPaginator = new YAHOO.widget.Paginator({ 
        	rowsPerPage:10,
        	containers : ["dt-pag-nav"], 
        	template : "<digi:trn>Results:</digi:trn>{RowsPerPageDropdown}<br/>{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}&nbsp;&nbsp;{CurrentPageReport}", 
        	pageReportTemplate : "<digi:trn>Showing items</digi:trn> {startIndex} - {endIndex} <digi:trn>of</digi:trn> {totalRecords}", 
        	rowsPerPageOptions : [10,25,50,100]
        });   
        var myConfigs = {
            initialRequest: "sort=name&dir=asc&startIndex=0&results=10", // Initial request for first page of data
            dynamicData: true, // Enables dynamic server-driven data
            sortedBy : {key:"name", dir:YAHOO.widget.DataTable.CLASS_ASC}, // Sets UI initial sort arrow
            //paginator: new YAHOO.widget.Paginator({ rowsPerPage:10 }) // Enables pagination
            paginator:myPaginator
        };
    	 
        this.myDataTable = new YAHOO.widget.DataTable("dynamicdata", myColumnDefs, this.myDataSource, myConfigs);
        this.myDataTable.subscribe("rowMouseoverEvent", this.myDataTable.onEventHighlightRow); 
        this.myDataTable.subscribe("rowMouseoutEvent", this.myDataTable.onEventUnhighlightRow);
        this.myDataTable.subscribe("rowClickEvent", this.myDataTable.onEventSelectRow);
 		this.myDataTable.subscribe("rowClickEvent", function (ev) {
				var target = YAHOO.util.Event.getTarget(ev);
				var record = this.getRecord(target);
				showTeamDetails(record.getData('ID'), record.getData('name'));
			});
        
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
<div id="popin" style="display: none">
	<div id="popinContent" class="content">
	</div>
</div>

<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#popin .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	.bd a:hover {
  		background-color:#ecf3fd;
		font-size: 10px; 
		color: #0e69b3; 
		text-decoration: none	  
	}
	.bd a {
	  	color:black;
	  	font-size:10px;
	}
		
</style>
<style type="text/css">
.jcol {
	padding-left: 10px;
}

.jlien {
	text-decoration: none;
}

.tableEven {
	background-color: #dbe5f1;
	font-size: 8pt;
	padding: 2px;
}

.tableOdd {
	background-color: #FFFFFF;
	font-size: 8pt;
	padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}

.notHovered {
	background-color: #FFFFFF;
}
.headTableTr{
    background-color:#B8B8B0; 
    color:#000000;
}

.headTableTd{    
	font-weight:bold;
    font-size: 11px;
    text-decoration:none;
    height: 22px;
}
.clsTableTitleCol {
	-x-system-font:none;
	background-color:#B8B8B0;
	color:#000000;
	cursor:default;
	font-family:"Verdana";
	font-size:7.5pt;
	font-size-adjust:none;
	font-stretch:normal;
	font-style:normal;
	font-variant:normal;
	font-weight:bold;
	line-height:normal;
	text-align:center;
	height: 22px;
}		
.reportsBorderTable {
	border-collapse:collapse;
}
.reportsBorderTD {
	cellpadding: 0px;
	cellspacing: 0px;
	padding: 0px;
	margin: 0px;
	font-family:Arial,Helvetica,sans-serif;
	height: 30px;
	Fixed-width: 100px;
}
</style>
<script type="text/javascript">
<!--

		YAHOOAmp.namespace("YAHOOAmp.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
			width:"450px",
			height:"250px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
	var panelStart;
	var checkAndClose=false;
	var lastFunction="";
   
	    
	function initCurrencyManagerScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			myclose();
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
	}
	
	addLoadEvent(initCurrencyManagerScript);
-->	
</script>
<script language="JavaScript">
    <!--
   
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
       window.location.reload();
    }
    
    
    var responseSuccess = function(o){
		var response = o.responseText; 
		var content = document.getElementById("popinContent");
		content.innerHTML = response;
		showContent();
	}
 
	var responseFailure = function(o){ 
		alert("Connection Failure!"); 
	}  
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	};

	function showContent(){
		var element = document.getElementById("popin");
		element.style.display = "inline";
		if (panelStart < 1){
			myPanel.setBody(element);
		}
		if (panelStart < 2){
			document.getElementById("popin").scrollTop=0;
			myPanel.show();
			panelStart = 2;
		}
		checkErrorAndClose();
	}
	function checkErrorAndClose(){
		if(checkAndClose==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				myclose();
				refreshPage();
			}
			checkAndClose=false;			
		}
	}
	function refreshPage(){
		if(lastFunction==="showDetails"){
			showDetails();
		}
		lastFunction="";
	}

	function myclose(){
		var content = document.getElementById("popinContent");
		content.innerHTML="";
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = '<div style="text-align: center">' + 
		'<img src="/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' + 
		'<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
		showContent();
	}
	function addNewUser()	{
		var msg='\n<digi:trn>AMP View Settings</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="commentUrl" property="context/module/moduleinstance/addUser.do"/>  
		var url = "<%=commentUrl %>";
		YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');
	}

	-->

</script>


<script language="JavaScript">
	var oldKeyword="", oldWorkspaceType="";
	function onDelete() {
		var flag = confirm('<digi:trn key="admin:workSpaceManager.deleteQuestion">Delete this workspace?</digi:trn>');
		return flag;
	}
	function openNpdSettingsWindow(ampTeamId){
		var msg='\n<digi:trn>AMP View Settings</digi:trn>';
		myPanel.cfg.setProperty("width","450px");
		myPanel.cfg.setProperty("height","250px"); 			
		showPanelLoading(msg);
		<digi:context name="commentUrl" property="context/module/moduleinstance/npdSettingsAction.do"/>  
		var url = "<%=commentUrl %>";
		url+='?actionType=viewCurrentSettings&ampTeamId='+ampTeamId
		YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');
	}
	function addActionToURL(actionName){
		var fullURL=document.URL;
		var lastSlash=fullURL.lastIndexOf("/");
		var partialURL=fullURL.substring(0,lastSlash);
		return partialURL+"/"+actionName;
	}
	function getParams(){
		ret = "";
		ret += "keyword="+document.getElementsByName('keyword')[0].value+"&workspaceType="+document.getElementsByName('workspaceType')[0].value;
		return ret;
	}
	
	function resetPage(){
		if(oldKeyword != document.getElementsByName('keyword')[0].value  || oldWorkspaceType != document.getElementsByName('workspaceType')[0].value){
			oldKeyword=document.getElementsByName('keyword')[0].value;
			oldWorkspaceType=document.getElementsByName('workspaceType')[0].value;
			<digi:context name="commentUrl" property="context/module/moduleinstance/workspaceManager.do"/>  
			var url = "<%=commentUrl %>";
			url += "?"+getParams();
			var async=new Asynchronous();
			async.complete=initDynamicTable;
			async.call(url);
		}
	}
</script>
<script langauage="JavaScript" type="text/javascript">
	

	function validateValues(){
		 var errmsg='';
                 var ampTeamId=document.getElementById('ampTeamId').value;
		 var width=document.getElementById('width').value;
		 var height=document.getElementById('height').value;
		 var angle=document.getElementById('angle').value;
                 var pageSize=document.getElementById('pageSize').value;
		 //*** Validate width
		 if(parseInt(width)==(width-0)){		   	
			 if(parseInt(width)<10 || parseInt(width)>1000){
			 	errmsg+='\n<digi:trn>Width must be in range from 10 to 1000</digi:trn>';
			 }
		 }else{
		 	errmsg+='\n<digi:trn>Please enter correct width</digi:trn>';
		 }		 
		 //***Validate height
		 if(parseInt(height)==(height-0)) {
		 	if(parseInt(height)<10 || parseInt(height)>1000){
			 	errmsg+='\n<digi:trn>Height must be in range from 10 to 1000</digi:trn>';
		 	}
		 }else{
		 	errmsg+='\n<digi:trn>Please enter correct height</digi:trn>';
		 }		 
		 //***Validate angle	

			if(angle!=''){
		  		if(parseInt(angle)==(angle-0)) {
		 		if(parseInt(angle)<0 || parseInt(angle)>90){
			 	errmsg+='\n<digi:trn>Angle of inclination must be in range from 0 to 90</digi:trn>';
			}
		 }else{
		 	errmsg+='\n<digi:trn>Please enter correct angle</digi:trn>';
		 }
				} 
		 
		 //***Validate error messages
		 if (errmsg==''){
			saveSettings(ampTeamId,width,height,angle,pageSize);
		 } else{
			alert(errmsg);
			return false;
		 }
	}

    function saveSettings(ampTeamId,width,height,angle,pageSize){
		lastTimeStamp = new Date().getTime();
        <digi:context name="changeSett" property="context/module/moduleinstance/npdSettingsAction.do?actionType=changeSettings"/>
        var params="&ampTeamId="+ampTeamId+"&width="+width+"&height="+height+"&angle="+angle+"&pageSize="+pageSize;
		var url = "${changeSett}"+params+'&timeStamp='+lastTimeStamp;
		checkAndClose=true;
		YAHOOAmp.util.Connect.asyncRequest("POST",url+"?"+params, callback, '');
	}
</script>
<script language="JavaScript">

function updateTableMembers(members){

    var demo       = YAHOO.util.Dom.get('demo'),
        tbl        = demo.getElementsByTagName('table')[0],
        tbody      = tbl.getElementsByTagName('tbody')[0],
        tmp        = document.createElement('div'),
        html       = ["<table id=\"dataTable\"><tbody>"],i,j = 1,l,item;

    if (members && members.length>0) {
        for (i = 0, l = members.length; i < l; ++i) {
            item = members[i];
            html[j++] = '<tr><td width="300" class="reportsBorderTD">';
            html[j++] = '<a href=\'javascript:showUserProfile('+item.ID+')\' title=\'Click to View Member Detais\'>'+item.name+'</a>';
            html[j++] = '</td><td align=\'center\' width="100" class="reportsBorderTD">';
            html[j++] = '<a href=\'JavaScript:memberAction("edit",' +item.ID+')\' title=\'<digi:trn>Click here to Edit Team Member Details</digi:trn>\'>' + '<img vspace=\'2\' border=\'0\' src=\'/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png\'/>' + '</a>'
            html[j++] = '&nbsp;&nbsp;&nbsp;&nbsp;<a href=\'JavaScript:memberAction("delete",' +item.ID+')\'  title=\'<digi:trn>Click here to Delete Team Member</digi:trn>\'>' + '<img vspace=\'2\' border=\'0\' src=\'/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif\'/>' + '</a>'
            html[j++] = '</td></tr>';
        }
        document.getElementById('footerMessage').innerHTML='<em><digi:trn>* Worskpace Manager</digi:trn></em>';
    } else {
        html[j++] = '<tr><td colspan="2"><em><digi:trn>No Member data</digi:trn><em></td></tr>';
        document.getElementById('footerMessage').innerHTML='';
    }
    html[j] = "</tbody></table>";

    tmp.innerHTML = html.join('');

    tbl.replaceChild(tmp.getElementsByTagName('tbody')[0], tbody);
    
       
}
function memberAction(action, id){
	var msg='<digi:trn>Delete Member</digi:trn>';
	if(action==='edit'){
		msg='<digi:trn>Edit Member</digi:trn>'
	}
	myPanel.cfg.setProperty("width","400px");
	myPanel.cfg.setProperty("height","350px"); 
	showPanelLoading(msg);	
	<digi:context name="commentUrl" property="context/module/moduleinstance/getTeamMemberDetailsJSON.do"/>;  
	var url = "<%=commentUrl %>";
	url += "?action="+action+"&id="+id;
	YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');
}
function confirmActionMember(){
	if(validateAction()){
		checkAndClose=true;
		lastFunction="showDetails";
		<digi:context name="commentUrl" property="context/module/moduleinstance/updateTeamMemberJSON.do"/>  
		var url = "<%=commentUrl %>";
		url += "?teamId="+document.getElementsByName('teamId')[0].value+
		"&teamMemberId="+document.getElementsByName('teamMemberId')[0].value+
		"&action="+document.getElementsByName('action')[0].value+
		"&userId="+document.getElementsByName('userId')[0].value+
		"&name="+document.getElementsByName('name')[0].value+
		"&role="+document.getElementsByName('role')[0].value;
		YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');
	}	
}

function validateAction(){
	if(document.getElementsByName('action')[0].value==='edit' && document.getElementsByName('role')[0].selectedIndex==0){
		alert("<digi:trn>Please select the role</digi:trn>");
		return false;
	}
	return true;			
}
function updateTableActivities(members){

    var demo       = YAHOO.util.Dom.get('demo'),
        tbl        = demo.getElementsByTagName('table')[0],
        tbody      = tbl.getElementsByTagName('tbody')[0],
        tmp        = document.createElement('div'),
        html       = ["<table id=\"dataTable\"><tbody>"],i,j = 1,l,item;

    if (members && members.length>0) {
        for (i = 0, l = members.length; i < l; ++i) {
            item = members[i];
            html[j++] = '<tr><td width="300" class="reportsBorderTD">';
            html[j++] = item.name;
            html[j++] = '</td ><td align=\'center\' width="100" class="reportsBorderTD">';
            html[j++] = '<a href=\'JavaScript:removeActivity('+item.ID+')\' onClick=\'return confirmDelete()\' title=\'<digi:trn>Click here to Delete Activity</digi:trn>\'>' + '<img vspace=\'2\' border=\'0\' src=\'/TEMPLATE/ampTemplate/images/deleteIcon.gif\'/>' + '</a>';
            html[j++] = '</td></tr>';
        }
        
    } else {
        html[j++] = '<tr><td colspan="2"><em><digi:trn>No Activities</digi:trn></em></td></tr>';
    }
    document.getElementById('footerMessage').innerHTML='';
    html[j] = "</tbody></table>";

    tmp.innerHTML = html.join('');

    tbl.replaceChild(tmp.getElementsByTagName('tbody')[0], tbody);
    
        
}
function removeActivity(id){
	<digi:context name="commentUrl" property="context/module/moduleinstance/removeTeamActivityJSON.do"/>  
	var url = "<%=commentUrl %>";
	url += "?selActivities="+id+"&teamId="+ document.getElementsByName('teamId')[0].value;
    YAHOO.util.Connect.asyncRequest('GET',url,{

        success : function (res) {
    		document.getElementById('ws_go').disabled=true;
            var activities;
            try {
            	activities = YAHOO.lang.JSON.parse(res.responseText);
                updateTableActivities(activities);
            }
            catch(e) {
                alert("<digi:trn>Error getting activity data</digi:trn>");
            }
            finally {
                setStripsTable("dataTable", "tableEven", "tableOdd");
                setHoveredTable("dataTable", false);
                document.getElementById('ws_go').disabled=false;
            }
        },
        failure : function () {
            alert("<digi:trn>Error getting activity data</digi:trn>");
        }
        
    });
}


function showTeamMembers(id, description){

    YAHOO.util.Connect.asyncRequest('GET','/aim/teamMembersJSON.do?teamId='+id,{

        success : function (res) {
    		document.getElementById('ws_go').disabled=true;
            var members;
            try {
                members = YAHOO.lang.JSON.parse(res.responseText);
                updateTableMembers(members);
            	document.getElementById('teamTitle').innerHTML=document.getElementsByName('teamName')[0].value;
            	document.getElementById('addNew').innerHTML='<a title="Click here to Add Workspace Member" href="JavaScript:assignNewMember()">Add Workspace Member</a>'
            	document.getElementById('lnkRoles').innerHTML='<a title="Click here to View Roles" href=/aim/roles.do>Roles</a>';
            }
            catch(e) {
                alert("<digi:trn>Error getting members data</digi:trn>");
            }
            finally {
                setStripsTable("dataTable", "tableEven", "tableOdd");
                setHoveredTable("dataTable", false);
                document.getElementById('ws_go').disabled=false;
            }
        },
        failure : function () {
            alert("<digi:trn>Error getting members data</digi:trn>");
        }
        
    });
}
function showActivities(id, description){
	

    YAHOO.util.Connect.asyncRequest('GET','/aim/teamActivitiesJSON.do?id='+id,{

        success : function (res) {
    		document.getElementById('ws_go').disabled=true;
            var members;
            try {
                members = YAHOO.lang.JSON.parse(res.responseText);
                updateTableActivities(members);
            	document.getElementById('teamTitle').innerHTML=document.getElementsByName('teamName')[0].value;
            	document.getElementById('addNew').innerHTML='<a title="Click here to Assign Activity" href=\'JavaScript:addActivities('+id+')\'>Assign an activity</a>';
            	document.getElementById('lnkRoles').innerHTML='';
            }
            catch(e) {
                alert("Error getting members data");
            }
            finally {
                setStripsTable("dataTable", "tableEven", "tableOdd");
                setHoveredTable("dataTable", false);
                document.getElementById('ws_go').disabled=false;                            
            }
        },
        failure : function () {
            alert("Error getting members data");
        }
        
    });
}

function assignNewMember(){
	var msg='<digi:trn>Assign Member</digi:trn>';
	myPanel.cfg.setProperty("width","500px");
	myPanel.cfg.setProperty("height","400px"); 
	showPanelLoading(msg);

	<digi:context name="commentUrl" property="context/module/moduleinstance/showAddTeamMemberJSON.do"/>  
	var url = "<%=commentUrl %>";
	url += "?fromPage=1&teamId="+document.getElementsByName('teamId')[0].value;
	

	YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');	
}
function saveAddedMember(){
	if(validateAddedMember()){
		checkAndClose=true;
		lastFunction="showDetails";
		var msg='<digi:trn>Assign Member</digi:trn>';
		<digi:context name="commentUrl" property="context/module/moduleinstance/addTeamMemberJSON.do"/>;
		var url = "<%=commentUrl %>";
		url += "?fromPage=1&teamId="+document.getElementsByName('teamId')[0].value+"&email="+document.getElementsByName('email')[0].value+"&role="+document.getElementsByName('role')[0].value;
		YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');
	}	
}
function validateAddedMember(){
	if(document.getElementsByName('role')[0].selectedIndex==0){
		alert("<digi:trn>Role not entered</digi:trn>");
		return false;
	}
	return true;
}
function addActivities(id){
	var msg='<digi:trn>Assign Activities</digi:trn>';
	myPanel.cfg.setProperty("width","500px");
	myPanel.cfg.setProperty("height","400px"); 
	showPanelLoading(msg);

	<digi:context name="commentUrl" property="context/module/moduleinstance/assignActivityJSON.do"/>  
	var url = "<%=commentUrl %>";
	url += "~id="+id;
	

	YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');

}

function showDetails(){
	id = document.getElementsByName('teamId')[0].value;
	desc = document.getElementsByName('teamName')[0].value;
	if(id===""||desc===""){
		alert("<digi:trn>Select a Team First</digi:trn>");
		return;
	}
	
	value = document.getElementById('showdataWs').options[document.getElementById('showdataWs').selectedIndex].value;
	if(value==0){
		showTeamMembers(document.getElementsByName('teamId')[0].value, document.getElementById('teamTitle').value);			
	}
	else{
		showActivities(document.getElementsByName('teamId')[0].value, document.getElementById('teamTitle').value);		
	}
}
function showTeamDetails(id, description){

	document.getElementsByName('teamId')[0].value=id;
	document.getElementsByName('teamName')[0].value=description;
	
	value = document.getElementById('showdataWs').options[document.getElementById('showdataWs').selectedIndex].value;
	if(value==0){
		showTeamMembers(id, description);			
	}
	else{
		showActivities(id, description);		
	}
}

</script>
<script language=javascript>
function showUserProfile(id){
	var param = "~edit=true~id="+id;
    previewWorkspaceframe('/aim/default/userProfile.do',param);
	
}
function confirmDelete() {
	var flag = confirm("<digi:trn>Are you sure you want to remove the selected activity</digi:trn>");
	if(flag == false)
		return false;
	else 
		return true;
}
</script>
<script language="javascript">
 function validate(){
	var result=false
	if(document.getElementsByName("selectedActivities")!=null){
		var sectors = document.getElementsByName("selectedActivities").length;
		for(var i=0; i< sectors; i++){
			if(document.getElementsByName("selectedActivities")[i].checked){
				result=true;
				break;
			}
		}
	} 
	if(!result){
		alert('<digi:trn>Please, Select an Activity First</digi:trn>')
	}
	return result;
}

function checkall() {
	var selectbox = document.aimAssignActivityForm.checkAll;
	if (document.aimAssignActivityForm.selectedActivities.type=="checkbox"){
		document.aimAssignActivityForm.selectedActivities.checked=selectbox.checked;
	}else{
		var items = document.aimAssignActivityForm.selectedActivities;
		for(i=0;i<items.length;i++){
		 	document.aimAssignActivityForm.selectedActivities[i].checked = selectbox.checked;
		}
	}
  }
function assignActivityList(){
	ret="";
	if(document.getElementsByName("selectedActivities")!=null){
		var sectors = document.getElementsByName("selectedActivities").length;
		for(var i=0; i< sectors; i++){
			if(document.getElementsByName("selectedActivities")[i].checked){
				ret+=document.getElementsByName("selectedActivities")[i].name+"="+document.getElementsByName("selectedActivities")[i].value+"&";
			}
		}
	} 
	if(validate()){
		checkAndClose=true;
		lastFunction="showDetails";
		<digi:context name="commentUrl" property="context/module/moduleinstance/assignActivityJSON.do"/>;  
		var url = "<%=commentUrl %>";
		url+="?"+ret+"&teamId="+document.getElementsByName('teamId')[0].value;
		YAHOOAmp.util.Connect.asyncRequest("POST",url, callback, '');
	}
	else{
		alert('<digi:trn>Validation Error</digi:trn>')
	}
	
}
</script>
<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}


function setHoveredRow(rowId) {

	var rowElement = document.getElementById(rowId);
	if(rowElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        cells      = rowElement.getElementsByTagName('td');

		for(var i = 0, n = cells.length; i < n; ++i) {
			cells[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			cells[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		cells = null;
	}
}

</script> 

<DIV id="TipLayer"	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>

<digi:instance property="aimWorkspaceForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=900>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=850>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn>Click here to goto Admin Home</digi:trn>
						</c:set>
						<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
						<digi:trn>Admin Home</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>Workspace Manager</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height="16" vAlign="center" width="571">
                    	<span class=subtitle-blue><digi:trn>Workspace Manager</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="center" width="571">
						<digi:errors />
					</td>
				</tr>
				<tr><td align="left">
                    <jsp:include page="/repository/aim/view/exportTable.jsp" />
                </td></tr>
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellSpacing="1" border="0">
					<tr><td noWrap width="50%" vAlign="top">
						<table bgColor="#d7eafd" cellPadding="1" cellSpacing="1" width="100%" valign="top">
							<tr bgColor="#ffffff">
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn>Teams</digi:trn>
											<!-- end table title -->
										</td>
										</tr>
										<tr><td>&nbsp;</td></tr>
										
										<digi:form action="/workspaceManager.do?page=1" method="post">
										<tr><td class="box-title" align="left">
											<!-- Table title -->
											<table width="100%" >
												<tr>
						
													<td>
													<digi:trn>keyword</digi:trn>:&nbsp;
										              <html:text property="keyword" style="font-family:verdana;font-size:11px;"/>
													</td>
													
													<td align="center">
														<digi:trn>Type</digi:trn>:&nbsp;
														<html:select property="workspaceType" styleClass="inp-text">
															<html:option value="all"><digi:trn>All</digi:trn></html:option>
															<html:option value="team"><digi:trn>Team</digi:trn></html:option>
															<html:option value="management"><digi:trn>Management</digi:trn></html:option>
															<html:option value="computed"><digi:trn>Computed</digi:trn></html:option>
														</html:select>
													</td>
													<td align="left">
													<c:set var="translation">
										                <digi:trn>Show</digi:trn>
										            </c:set>
										            <input type="button" value="${translation}"  class="dr-menu" style="font-family:verdana;font-size:11px;" onclick="return resetPage()"/>
													</td>
												</tr>
											</table>
											<!-- end table title -->
										</td></tr>
										</digi:form>
										<tr><td>
											<c:set var="translation">
										       <digi:trn>Click here to Add Teams</digi:trn>
										    </c:set>
											<digi:link href="/createWorkspace.do?dest=admin" title="${translation}" >
											<digi:trn>
											Add Team
											</digi:trn>
											</digi:link>
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing="1" cellpadding="0" valign="top" align="left" >
													<logic:empty name="aimWorkspaceForm" property="workspaces">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn>No teams present</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimWorkspaceForm" property="workspaces">
													<tr>
														<td width="70%">
															<div class='yui-skin-sam'>
                                                                <div id="dynamicdata" class="report"></div>
																<div id="dt-pag-nav"></div>
																<div id="errors"></div>
															</div>
														</td>
													</tr>
													</logic:notEmpty>
													<!-- end page logic -->
											</table>
										</td>
										</tr>
									</table>

								</td>
							</tr>
						</table>
					</td>

<!--details-->
					<td  width="50%" vAlign="top">
						<table bgColor="#d7eafd" cellPadding="1" cellSpacing="1" width="100%" valign="top">
							<tr bgColor="#ffffff">
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center" id="teamTitle">
											<digi:trn>Team Name</digi:trn>
										</td>
										</tr>
										<tr><td>&nbsp;</td></tr>
										<tr><td class="box-title" align="left">
											<table width="100%" >
												<tr>
												<td>&nbsp;</td>
													<td align="right">
														<digi:trn>Select</digi:trn>:&nbsp;
													   <select id="showdataWs" class="inp-text"> 
														    <option value="0"><digi:trn>Members</digi:trn></option> 
														    <option value="1"><digi:trn>Activities</digi:trn></option> 
														</select> &nbsp;&nbsp;&nbsp;
											            <input type="button" id="ws_go" value='<digi:trn>Show</digi:trn>' onclick="showDetails()">
													</td>
												</tr>
											</table>
										</td></tr>

										<tr>
										<td>
										<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left" border="0">
										<tr>
										<td id="addNew" align="left">&nbsp;
										</td>
										<td id="lnkRoles" align="right">&nbsp;</td>
										</tr>
										</table>
										</td>
										</tr>
										<tr><td>
										<div>
										<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left" border="0" >
										<tr>
										<td>
											<input type="hidden" name="teamId" value=""/>
											<input type="hidden" name="teamName" value=""/>
											<table  cellspacing="1" cellpadding="2" align="left" width="100%">
											<tr><td>
											<div class="reportHead" style="width: 100%px; height: 22px; max-height: 22px; ">
											<table width="100%" class="reportsBorderTable">																				
										        <tr class="headTableTr">
										            <td align="center" width="300" align="center" class="clsTableTitleCol"><digi:trn>Name</digi:trn></td>
										            <td align="center" width="100" align="center" class="clsTableTitleCol"><digi:trn>Actions</digi:trn></td>
										        </tr>
											</table>
											</div>
											<div id="demo" class="report" style="overflow: auto; width: 100%; height: 309px; max-height: 309px;" class="box-border-nopadding" >																		
												<table class="box-border-nopadding" width="100%" id="dataTable" cellspacing="0" cellpadding="4" valign="top"  align="left">
											    <tbody>
											        <tr><td colspan="2"><em><digi:trn>Select Team to Get Data</digi:trn></em></td></tr>
											    </tbody>
											 	</table>
											</div>
										    <table cellspacing="1" cellpadding="2" align="left" width="100%">
											    <tbody>
											        <tr><td colspan="2" id="footerMessage">&nbsp;
											        </td></tr>
											    </tbody>
											 	</table>
											</td>
											</tr>
											</table>
										</td>
										</tr>
									</table>
									</div>
								</td>
							</tr>
						</table>
						</td>
					</tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td>
</tr>
</table>
