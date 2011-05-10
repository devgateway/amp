<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/js_2/yui/datatable/assets/skins/sam/datatable.css">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css">



<style>
.yui-skin-sam .yui-dt thead th,.yui-skin-sam .yui-dt .yui-dt-data td {
	border-color: #CCC;
	border-style: none solid solid none;
	border-width: medium 1px 1px medium;
}

.yui-skin-sam .yui-dt thead th {
	border-color: #FFF;
	background-color: #C7D4DB;
	color: #000;
	height: 30px;
	text-align: center;
}

.yui-skin-sam .yui-dt th .yui-dt-liner {
	font-size: 12px;
	text-align: center;
	font-weight: bold;
	font-family: Arial, Verdana, Helvetica, sans-serif;
	border-color: #CCC;
}

.yui-skin-sam .yui-dt td .yui-dt-liner {
	font-size: 12px;
	font-family: Arial, Verdana, Helvetica, sans-serif;
}

.yui-skin-sam a.yui-pg-page {
	padding-right: 10px;
	font-size: 11px;
	border-right: 1px solid rgb(208, 208, 208);
}

.yui-skin-sam .yui-pg-pages {
	border: 0px;
	padding-left: 0px;
}

.yui-pg-current-page {
	background-color: #FFFFFF;
	color: rgb(208, 208, 208);
	padding: 0px;
}

.current-page {
	background-color: #FF6000;
	color: #FFFFFF;
	margin-right: 5px;
	font-weight: bold;
}

.yui-pg-last {
	border: 0px
}

.yui-skin-sam span.yui-pg-first,.yui-skin-sam span.yui-pg-previous,.yui-skin-sam span.yui-pg-next,.yui-skin-sam span.yui-pg-last
	{
	display: none;
}

.yui-skin-sam a.yui-pg-first {
	margin-left: 2px;
	padding-right: 7px;
	border-right: 1px solid rgb(208, 208, 208);
}


</style>
<!-- Individual YUI JS files --> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event/event-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script>

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/paginator/paginator-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datatable/datatable-min.js"></script>
<script language="JavaScript">

YAHOO.util.Event.addListener(window, "load", initDynamicTable1);
	function initDynamicTable1() {
	    YAHOO.example.XHR_JSON = new function() {
	 
	        this.myDataSource = new YAHOO.util.DataSource("/aim/searchAvailableUsers.do?");
	        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
	        //this.myDataSource.connXhrMode = "queueRequests";
	        this.myDataSource.responseSchema = {
	            resultsList: "users",
	            fields: ["ID","name","email","organizations","role","chkBox"],
	            metaFields: {
	            	totalRecords: "totalRecords" // Access to value in the server response
	        	}    
	        };        
	        
	        var myColumnDefs = [
	            {key:"name", label:"<digi:trn>NAME</digi:trn>", sortable:true, width: 250},
	            {key:"email", label:"<digi:trn>EMAIL</digi:trn>", sortable:true, width: 230},
	            {key:"organizations", label:"<digi:trn>ORGANIZATIONS</digi:trn>", sortable:false, width: 250},
	            {key:"role", label:"<digi:trn>ROLE</digi:trn>", sortable:false, width: 150},
	            {key:"chkBox", label:"<digi:trn>SELECT</digi:trn>", sortable:false, width: 30}
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
	        	lastPageLinkLabel		: '<digi:trn jsFriendly="true">last page</digi:trn>'
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

<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<script language="JavaScript">
	function addUsersToWorkspace(){
		if(validate()==true){
			//submit form
			<digi:context name="commentUrl" property="context/module/moduleinstance/assignUsersToWorkspace.do"/>;
			document.aimTeamMemberForm.action="${commentUrl}?fromPage=1&addedFrom=showAddFromAdmin";
		    document.aimTeamMemberForm.target="_self";
		    document.aimTeamMemberForm.submit();
		}
	}

	function validate(){
		//if none of the users are checked, we should show an alert
		var checkedUsers = $('input.selectedUsers:checked');
		if(checkedUsers==null || checkedUsers.length==0){
			alert('Please select at least one user to add');
			return false;
		}
		
		//if more then one user is selected to be TL,or team already has a manager, we should show alert
		var selectedRoles=[];
		checkedUsers.each(function(index){ //get all checkboxes,which are checked			
			selectedRoles[selectedRoles.length]=$(this).parents('tr').find('select').val();
		});
		
		var teamHeadRole=document.getElementById('wrkspcManRoleId').value;
		var teamHeadCount=0; //this field is used to cound how many users were indicated as TL.if it's greater then 1,then we should show error
		var workspaceManExist=document.getElementById('wrkspcManager').value;
		if(workspaceManExist=='exists'){
			teamHeadCount++;
		}		
		if(selectedRoles!=null && selectedRoles.length>0){
			for(var i=0;i<selectedRoles.length;i++){
				if(selectedRoles[i].indexOf('_')!= -1){
					selectedRoles[i]=selectedRoles[i].substring(selectedRoles[i].indexOf('_')+1);
				}
				if(selectedRoles[i]== -1){ //check whether all checked users have roles
					alert('Please select role');
					return false;
				}
				if(teamHeadRole==selectedRoles[i]){
					teamHeadCount++;					
				}
			}			
		}
		if(teamHeadCount>1){
			alert('Wokrspace can not have more then one Manager');
			return false;
		}
		
		return true;
	}
</script>

<digi:instance property="aimTeamMemberForm" />

<digi:form action="/addTeamMember.do" method="post">

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<html:hidden name="aimTeamMemberForm" property="teamLeaderExists" styleId="wrkspcManager"/>
<html:hidden name="aimTeamMemberForm" property="workspaceManagerRoleId" styleId="wrkspcManRoleId"/>
<body>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=450>
	<tr></tr>
	<tr>
		<td vAlign="top" width="100%">
			<table width="100%" cellspacing="1" cellpadding="0" valign="top" align="left" >
				<logic:empty name="aimTeamMemberForm" property="allUser">
					<tr bgcolor="#ffffff">
						<td colspan="5" align="center"><b>
							<digi:trn>No users present</digi:trn>
						</b></td>
					</tr>
				</logic:empty>
				<logic:notEmpty name="aimTeamMemberForm" property="allUser">
					<tr>
						<td align=center>
							<div class='yui-skin-sam'>
                            	<div id="dynamicdata" class="report"></div>
                            	<div align="center" style="border: 1px black;"><br><html:button property="" styleClass="buttonx" value="Add Selected Memebers" onclick="addUsersToWorkspace()"></html:button> </div>
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
</digi:form>
</body>


