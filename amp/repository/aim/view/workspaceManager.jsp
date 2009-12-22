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
            //elCell.innerHTML = "<a href=/um/viewEditUser.do~id=" +sData+">" +"<img vspace='2' border='0' src='/repository/message/view/images/edit.gif'/>" + "</a>";
            //elCell.innerHTML +="&nbsp;&nbsp;<a onclick='return banUser();' title='Ban User' href=/um/viewEditUser.do~id=" +sData+"~ban=true>" +"<img vspace='2' border='0' src='/TEMPLATE/ampTemplate/images/deleteIcon.gif'/>" + "</a>";
        	elCell.innerHTML = 
        	"[<a href=/aim/teamMembers.do~teamId=" +oRecord.getData( 'ID' )+" title='Click here to view Members'>" + "<digi:trn>Members</digi:trn>" + "</a>]&nbsp;&nbsp;&nbsp;&nbsp;"+
        	"[<a href=/aim/teamActivities.do~id=" +oRecord.getData( 'ID' )+" title='Click here to view Activities'>" + "<digi:trn>Activities</digi:trn>" + "</a>]&nbsp;&nbsp;&nbsp;&nbsp;"+
        	"[<a href=/aim/getWorkspace.do~dest=admin~event=edit~tId=" +oRecord.getData( 'ID' )+" title='Click here to Edit Workspace'>" + "<digi:trn>Edit</digi:trn>" + "</a>]&nbsp;&nbsp;&nbsp;&nbsp;"+
        	"[<a href=/aim/deleteWorkspace.do~event=delete~tId=" +oRecord.getData( 'ID' )+" title='Click here to Delete Workspace'>" + "<digi:trn>Delete</digi:trn>" + "</a>]&nbsp;&nbsp;&nbsp;&nbsp;"+
        	"[<a href=\"JavaScript:openNpdSettingsWindow(" +oRecord.getData( 'ID' )+ ");\">"+"<digi:trn>Npd Settings</digi:trn>"+"</a>]"
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
            //{key:"ID", label:"ID"},
            {key:"name", label:"<digi:trn>NAME</digi:trn>", sortable:true, width: 200},
            //{key:"email", label:"<digi:trn>EMAIL</digi:trn>", sortable:true, width: 150},
            //{key:"workspaces", label:"<digi:trn>WORKSPACES</digi:trn>", width: 260},
            {key:"actions", label:"<digi:trn>ACTION</digi:trn>", width: 350, formatter:this.formatActions}
            //{key:"actions", label:"ACTION", formatter:this.formatActions}
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
        	template : "{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}&nbsp;&nbsp;{CurrentPageReport}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<digi:trn>Results:</digi:trn>{RowsPerPageDropdown}", 
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
   
	    
	function initCurrencyManagerScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		myPanel.render(document.body);
		panelStart = 0;
		
	}
	
	addLoadEvent(initCurrencyManagerScript);
-->	
</script>
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

<DIV id="TipLayer"	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
	
<digi:instance property="aimWorkspaceForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
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
				<tr>
					<td noWrap width="100%" vAlign="top">
					<table width="100%" cellspacing="1" cellSpacing="1" border="0">
					<tr><td noWrap width="650" vAlign="top">
						<table bgColor="#d7eafd" cellPadding="1" cellSpacing="1" width="100%" valign="top">
							<tr bgColor="#ffffff">
								<td vAlign="top" width="100%">
									<table width="100%" cellspacing="1" cellpadding="1" valign="top" align="left">
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn>Teams</digi:trn>
											<!-- end table title -->
										</td></tr>
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
														<digi:trn>Workspace Type</digi:trn>:&nbsp;
														<html:select property="workspaceType" styleClass="inp-text">
															<html:option value="all"><digi:trn>All</digi:trn></html:option>
															<html:option value="team"><digi:trn>Team</digi:trn></html:option>
															<html:option value="management"><digi:trn>Management</digi:trn></html:option>
															<html:option value="computed"><digi:trn>Computed</digi:trn></html:option>
														</html:select>
													</td>
													<td align="right">
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
										<tr><td>&nbsp;</td></tr>
										<tr><td>
											<table width="100%" cellspacing="1" cellpadding="4" valign="top" align="left" >
													<logic:empty name="aimWorkspaceForm" property="workspaces">
													<tr bgcolor="#ffffff">
														<td colspan="5" align="center"><b>
															<digi:trn>No teams present</digi:trn>
														</b></td>
													</tr>
													</logic:empty>
													<logic:notEmpty name="aimWorkspaceForm" property="workspaces">
													<tr>
														<td width="100%">
															<div class='yui-skin-sam'>
																<div id="dynamicdata"></div>
																<div id="dt-pag-nav"></div>
																<div id="errors"></div>
															</div>
														</td>
													</tr>
													</logic:notEmpty>
													<!-- end page logic -->
											</table>
										</td></tr>
									</table>

								</td>
							</tr>
						</table>
					</td>

					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
												</c:set>
												<digi:link href="/createWorkspace.do?dest=admin" title="${translation}" >
												<digi:trn key="aim:addTeam">
												Add Teams
												</digi:trn>
												</digi:link>
											</td>
										</tr>
																				
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<c:set var="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</c:set>
												<digi:link href="/admin.do" title="${translation}" >
												<digi:trn key="aim:AmpAdminHome">
												Admin Home
												</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
