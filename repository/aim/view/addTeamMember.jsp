<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/fonts-min.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/datatable.css" />
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/paginator.css" />
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/connection-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datatable-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/event-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/paginator-min.js"></script> 
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
	            {key:"name", label:"<digi:trn>NAME</digi:trn>", sortable:true, width: 150},
	            {key:"email", label:"<digi:trn>EMAIL</digi:trn>", sortable:true, width: 150},
	            {key:"organizations", label:"<digi:trn>ORGANIZATIONS</digi:trn>", sortable:false, width: 150},
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

<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>

<script language="JavaScript">
	function addUsersToWorkspace(){
		if(validate()==true){
			//submit form
			<digi:context name="commentUrl" property="context/module/moduleinstance/assignUsersToWorkspace.do"/>;
			document.aimTeamMemberForm.action="${commentUrl}?fromPage=1&addedFrom=addedFromUser";
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
<html:hidden name="aimTeamMemberForm" property="teamLeaderExists" styleId="wrkspcManager"/>
<html:hidden name="aimTeamMemberForm" property="workspaceManagerRoleId" styleId="wrkspcManRoleId"/>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<body>
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="450">
	<tr></tr>
	<tr>
		<td vAlign="top" width="100%">
			<table width="100%" cellspacing="1" cellpadding="0" valign="top" align="center" >
				<logic:empty name="aimTeamMemberForm" property="allUser">
					<tr bgcolor="#ffffff">
						<td colspan="5" align="center"><b>
							<digi:trn>No users present</digi:trn>
						</b></td>
					</tr>
				</logic:empty>
				<logic:notEmpty name="aimTeamMemberForm" property="allUser">
					<tr>						
						<td bgcolor="#f4f4f2">
							<div class='yui-skin-sam'>
                            	<div id="dynamicdata" class="report" align="center"></div>
                            	<div align="center" style="border: 1px black;"><br><html:button property="" value="Add Selected Memebers" onclick="addUsersToWorkspace()"></html:button> </div>
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