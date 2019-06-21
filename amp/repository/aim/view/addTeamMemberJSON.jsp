<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ page import="org.digijava.kernel.request.TLSUtils" %>

<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/js_2/yui/datatable/assets/skins/sam/datatable.css">
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css">

<!-- Individual YUI CSS files -->
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/assets/skins/sam/autocomplete.css"> 

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/autocomplete-min.js"></script>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<style>
<!--
.ui-autocomplete {
	font-size:12px;
	border: 1px solid silver;
	max-height: 150px;
	overflow-y: scroll;
	background: white;
}
.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}


#userAutoComp  ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}


#userAutoComp  div {
	padding: 0px;
	margin: 0px; 
}

#userAutoComp  {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#userAutoComp {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
    font-size: 12px;
}


#userAutoComp {
    font-size: 12px;
}

#userAutoComp {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

span.extContactDropdownEmail {
	color:grey;
}


-->
</style>



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
	padding-right: 10px;
	border-right: 1px solid rgb(208, 208, 208);
}

.current-page {
	background-color: #FF6000;
	color: #FFFFFF;
	margin-right: 5px;
	font-weight: bold;
	padding-right: 10px;
	border-right: 1px solid rgb(208, 208, 208);
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

<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/common/TranslationManager.js"></script>

<script language="JavaScript">

YAHOO.util.Event.addListener(window, "load", initDynamicTable1);
	function initDynamicTable1() {
	    YAHOO.example.XHR_JSON = new function() {
	        var timestamp=new Date().getTime();
	        this.myDataSource = new YAHOO.util.DataSource("/aim/searchAvailableUsers.do?timestamp="+timestamp+"&");
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
	            {key:"role", label:"<digi:trn>ROLE</digi:trn>", sortable:false, minWidth: 150},
	            {key:"chkBox", label:"<digi:trn>SELECT</digi:trn>", sortable:false, minWidth: 30}
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

            var isRtl = <%=TLSUtils.getCurrentLocale().getLeftToRight() == false%>;
            var language = '<%=TLSUtils.getCurrentLocale().getCode()%>';
            var region = '<%=TLSUtils.getCurrentLocale().getRegion()%>';

            function convertNumbers() {
                $('.number-to-convert').each(function() {
                    this.innerText = TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, this.innerText);
                });
            }
	        // Create the Paginator  yui-pg-current-page yui-pg-page
	        var myPaginator = new YAHOO.widget.Paginator({ 
	        	rowsPerPage:10,
	        	containers : ["dt-pag-nav"], 
	        	template : "{CurrentPageReport}&nbsp;<span class='l_sm'><digi:trn>Results:</digi:trn></span>&nbsp;<span>{RowsPerPageDropdown}&nbsp;{FirstPageLink}{PageLinks}{LastPageLink}</span>",
	        	pageReportTemplate		: "<span class='l_sm'><digi:trn>Showing items</digi:trn></span> <span class='txt_sm_b number-to-convert'>{startRecord} - {endRecord} <digi:trn>of</digi:trn> {totalRecords}</span>",
                rowsPerPageOptions		: [
                    {value:10, text:'<digi:easternArabicNumber>10</digi:easternArabicNumber>'},
                    {value:25, text:'<digi:easternArabicNumber>25</digi:easternArabicNumber>'},
                    {value:50, text:'<digi:easternArabicNumber>50</digi:easternArabicNumber>'},
                    {value:100, text:'<digi:easternArabicNumber>100</digi:easternArabicNumber>'},
                    {value:999999,text:'<digi:trn jsFriendly="true">All</digi:trn>'}
                ],
	        	firstPageLinkLabel : 	"<digi:trn>first page</digi:trn>", 
	        	previousPageLinkLabel : "<digi:trn>prev</digi:trn>", 
	        	firstPageLinkClass : "yui-pg-first l_sm",
	        	lastPageLinkClass: "yui-pg-last l_sm",
	        	nextPageLinkClass: "yui-pg-next l_sm",
	        	previousPageLinkClass: "yui-pg-previous l_sm",
	        	rowsPerPageDropdownClass:"l_sm",
	        	nextPageLinkLabel		: '<digi:trn jsFriendly="true">next</digi:trn>',
	        	lastPageLinkLabel		: '<digi:trn jsFriendly="true">last page</digi:trn>',
                pageLabelBuilder: function (page, paginator) {
					return TranslationManager.convertNumbersToEasternArabicIfNeeded(isRtl, language, region, "" + page);
                }
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
	 		
	 		if(this.myDataTable.getRecordSet().getLength() > 0) { 
	            this.myDataTable.selectRow(this.myDataTable.getTrEl(0)); 
	 		}
	        // Programmatically bring focus to the instance so arrow selection works immediately 
	        this.myDataTable.focus(); 
	        
	        this.myDataTable.subscribe('postRenderEvent',function(oArgs){
	        	var children=document.getElementById("selectedUsersListDiv").childNodes;
	        	if(children!=null){
	        		for(var i=0;i<children.length;i++){
	        			var child=children[i];
	        			if(child.tagName.toLowerCase()=='input'){
	        				$('#chk_'+child.value).attr('checked','checked');
	        			}
	        		}
	        	}
	        	children=document.getElementById("selectedUsersRoleListDiv").childNodes;
	        	if(children!=null){
	        		for(var i=0;i<children.length;i++){
	        			var child=children[i];
	        			if(child.tagName.toLowerCase()=='input'){
	        				var currUserId=child.value.split("_")[0];
	    					$('#role_'+currUserId).val(child.value);
	        			}
	        		}
	        	}
	        	
				/*$('.selectedUsersList').each(function(index) {
					$('#chk_'+$(this).val()).attr('checked','checked');
				});
				$('.selectedUsersRoleList').each(function(index) {
					var currUserId=$(this).val().split("_")[0];
					$('#role_'+currUserId).val($(this).val());
				});*/
				
	        });

            this.myDataTable.subscribe('postRenderEvent', convertNumbers);

	        // Update totalRecords on the fly with value from server
	        this.myDataTable.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
	           oPayload.totalRecords = oResponse.meta.totalRecords;
	           return oPayload;
	       }
	       
	    };
    
	}

        function searchUsers() {
		aimTeamMemberForm.action="${contextPath}/aim/showAddTeamMemberJSON.do?fromPage="+document.getElementById('wrkspcFromPage').value
                    +"&teamId="+document.getElementById('wrkspcTeamId').value;
		aimTeamMemberForm.target = "_self";
		aimTeamMemberForm.submit();
	}
        function pickUsersForTeam(checkbox){
        	var div=document.getElementById("selectedUsersListDiv");
        	if(checkbox.checked){
        		var selectedUser=document.createElement("input");
        		selectedUser.setAttribute("type","hidden");
        		selectedUser.setAttribute("value",checkbox.value);
        		selectedUser.setAttribute("class", 'selectedUsersList');
        		selectedUser.setAttribute("id", 'userId_'+checkbox.value);
        		div.appendChild(selectedUser);
        	}
        	else{
        		var selectedUser=document.getElementById('userId_'+checkbox.value);
        		div.removeChild(selectedUser);
        	}
        }
       
	function updateUserRole(select) {
		var div = document.getElementById("selectedUsersRoleListDiv");
		var selectedUser = document.getElementById('hidden_' + select.id);
		var currentValue = select.value;
		if(selectedUser!=null){
			div.removeChild(selectedUser);
		}
		if( currentValue!=-1){
			var selectedUser = document.createElement("input");
			selectedUser.setAttribute("type", "hidden");
			selectedUser.setAttribute("value", currentValue);
			selectedUser.setAttribute("class", 'selectedUsersRoleList');
			selectedUser.setAttribute("name", 'userIdsWithRoles');
			selectedUser.setAttribute("id", 'hidden_' + select.id);
			div.appendChild(selectedUser);
		}
	}

	// don't remove or change this line!!!
	document.getElementsByTagName('body')[0].className = 'yui-skin-sam';
</script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<script language="JavaScript">
	function addUsersToWorkspace(){
		if(validate()==true){
			//submit form
			var addFromWhere=document.getElementById("addFromWhere").value;
			<digi:context name="commentUrl" property="context/module/moduleinstance/assignUsersToWorkspace.do"/>;
			document.aimTeamMemberForm.action="${commentUrl}?added=true&addedFrom="+addFromWhere;
		    document.aimTeamMemberForm.target="_self";
		    document.aimTeamMemberForm.submit();
		}
	}

	
	function validate() {
		//if none of the users are checked, we should show an alert
		var checkedUsers = document.getElementById("selectedUsersListDiv").childNodes;
		var checkedRoles = document.getElementById("selectedUsersRoleListDiv").childNodes;
		if (checkedUsers == null || checkedUsers.length == 0) {
			alert('Please select at least one user to add');
			return false;
		}
		//if more then one user is selected to be TL,or team already has a manager, we should show alert
		var teamHeadRole = document.getElementById('wrkspcManRoleId').value;
		var teamHeadCount = 0; //this field is used to count how many users were indicated as TL.if it's greater then 1,then we should show error
		var workspaceManExist = document.getElementById('wrkspcManager').value;
		if (workspaceManExist == 'exists') {
			teamHeadCount++;
		}

		for ( var i = 0; i < checkedUsers.length; i++) {
			var child = checkedUsers[i];
			if (child.tagName.toLowerCase() == 'input') {
				var role = document.getElementById("hidden_role_" + child.value);
				if (role == null) {
					alert('<digi:trn jsFriendly="true">Please select role</digi:trn>');
					return false;
					
				}
				var roleId = role.value.split("_")[1];
				if (roleId == teamHeadRole) {
					teamHeadCount++;
					if (teamHeadCount > 1) {
						alert('<digi:trn jsFriendly="true">Workspace can not have more than one Manager</digi:trn>');
						return false;
					}

				}
			}
		}

		if (checkedUsers.length != checkedRoles.length) {
			alert('<digi:trn jsFriendly="true">Role is set, but corresponding user is not selected.Please check</digi:trn>');
			return false;
		}

		return true;
	}
	function clearSearchResults(){
		 document.getElementById("userInput").value="";
		 searchUsers();
	}
</script>

<digi:instance property="aimTeamMemberForm" />


<digi:form action="/addTeamMember.do" method="post">
<div style="dispalay:none" id="selectedUsersListDiv"></div>
<div style="dispalay:none" id="selectedUsersRoleListDiv"></div>

<jsp:include page="teamPagesHeader.jsp" flush="true" />
<html:hidden name="aimTeamMemberForm" property="teamLeaderExists" styleId="wrkspcManager"/>
<html:hidden name="aimTeamMemberForm" property="workspaceManagerRoleId" styleId="wrkspcManRoleId"/>
<html:hidden name="aimTeamMemberForm" property="fromPage" styleId="wrkspcFromPage"/>
<html:hidden name="aimTeamMemberForm" property="teamId" styleId="wrkspcTeamId" />
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
				<bean:define id="addSelectedMembers"><digi:trn>Add Selected Members</digi:trn></bean:define>
						<tr>
							<td>
								<div>
									<div class="left-float">
									<digi:trn>Search User</digi:trn>:
										<html:text property="fullname" styleId="userInput"
											styleClass="inputx"
											style="width:320px; Font-size: 10pt; height:22px;" />
										<div id="userAutoComp"></div>
									</div>
									<div class="left-float">
										<input type="button" value="<digi:trn>Clear</digi:trn>"
											class="buttonx_sm" onClick="clearSearchResults()"/>
									</div>
								</div></td>
						</tr>
						<logic:notEmpty name="aimTeamMemberForm" property="allUser">
                                   
					<tr>
						<td align=center>
							<div class='yui-skin-sam'>
                            	<div id="dynamicdata" class="report"></div>
                            	<div align="center" style="border: 1px black;"><br><html:button property="" styleClass="buttonx" value="${addSelectedMembers}" onclick="addUsersToWorkspace()"></html:button> </div>
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
    <script type="text/javascript">
        var userDataSource = new YAHOO.widget.DS_XHR("/aim/searchFutureTeamMembers.do", ["\n", ";"]);
        userDataSource.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
        userDataSource.queryMatchContains = true;
        userDataSource.scriptQueryParam  = "srchStr";
        var userAutoComp = new YAHOO.widget.AutoComplete("userInput","userAutoComp", userDataSource);
        userAutoComp.queryDelay = 0.5;
        $("#userInput").css("position", "static");
       
		var myHandler = function(sType, aArgs) {
					
						searchUsers();
					};
		userAutoComp.itemSelectEvent.subscribe(myHandler);
		</script>
</body>


