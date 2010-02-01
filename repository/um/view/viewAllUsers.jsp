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

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-dom-event.js"></script> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/datatable-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/yahoo-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/event-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/paginator-min.js"></script> 

<script type="text/javascript" src="/TEMPLATE/ampTemplate/script/yui/connection-min.js"></script> 

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
        	elCell.innerHTML = "<a href=JavaScript:showUserProfile('"+oRecord.getData( 'email' )+"')  title='<digi:trn>Edit User</digi:trn>'>" + sData + "</a>";
        };
 
        this.myDataSource = new YAHOO.util.DataSource("/um/userSearch.do?");
        this.myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSON;
        //this.myDataSource.connXhrMode = "queueRequests";
        this.myDataSource.responseSchema = {
            resultsList: "users",
            fields: ["ID","name","email","workspaces","actions"],
            metaFields: {
            	totalRecords: "totalRecords" // Access to value in the server response
        	}    
        };
        
        
        var myColumnDefs = [
            //{key:"ID", label:"ID"},
            {key:"name", label:"<digi:trn>NAME</digi:trn>", sortable:true, formatter:this.formatActions, width: 150},
            {key:"email", label:"<digi:trn>EMAIL</digi:trn>", sortable:true, width: 150},
            {key:"workspaces", label:"<digi:trn>WORKSPACES</digi:trn>", width: 260},
            {key:"actions", label:"<digi:trn>ACTION</digi:trn>", width: 40, className: 'ignore'}
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

		YAHOO.namespace("YAHOO.amp");

		var myPanel = new YAHOO.widget.Panel("newpopins", {
			width:"700px",
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
	var userRegistered=false;    
	    
	function initCurrencyManagerScript() {
		var msg='\n<digi:trn>Select Indicator</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
			if(userRegistered){
				initDynamicTable();
				userRegistered=false;
			}
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
		document.aimCurrencyForm.submit();
	}

	function myclose(){
		var content = document.getElementById("popinContent");
		content.innerHTML="";
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
		if(userRegistered){
			initDynamicTable();
			userRegistered=false;
		}
	}
	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = '<div style="text-align: center">' + 
		'<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' + 
		'<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
		showContent();
	}
	function addNewUser()	{
		var msg='\n<digi:trn>Add New User</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="commentUrl" property="context/um/addUser.do"/>  
		var url = "<%=commentUrl %>";
		YAHOO.util.Connect.asyncRequest("POST",url, callback, '');
	}

	-->

</script>
<script language="JavaScript">


	function getParams(){
        var ret=""
        ret="orgType="+document.getElementsByName('orgType')[0].value+
        	"&orgGrp="+document.getElementsByName('orgGrp')[0].value+
        	"&actionFlag="+document.getElementsByName('actionFlag')[0].value+
        	"&firstNames="+document.getElementsByName('firstNames')[0].value+
        	"&lastName="+document.getElementsByName('lastName')[0].value+
        	"&email="+document.getElementsByName('email')[0].value+
        	"&emailConfirmation="+document.getElementsByName('emailConfirmation')[0].value+
        	"&password="+document.getElementsByName('password')[0].value+
        	"&passwordConfirmation="+document.getElementsByName('passwordConfirmation')[0].value+
        	"&selectedCountryResidence="+document.getElementsByName('selectedCountryResidence')[0].value+
        	"&mailingAddress="+document.getElementsByName('mailingAddress')[0].value+
        	"&selectedOrgType="+document.getElementsByName('selectedOrgType')[0].value+
        	"&selectedOrgGroup="+document.getElementsByName('selectedOrgGroup')[0].value+
        	"&organizationName="+document.getElementsByName('organizationName')[0].value+
        	"&selectedOrganizationId="+document.getElementsByName('selectedOrganizationId')[0].value+
        	"&selectedLanguage="+document.getElementsByName('selectedLanguage')[0].value;
    	if(document.getElementsByName('sendEmail')[0].checked){
    		ret+="&sendEmail=true";
        }
    	else{
    		ret+="&sendEmail=false";
        }
    	if(document.getElementsByName('addWorkspace')[0].checked){
    		ret+="&addWorkspace=true";
        }
    	else{
    		ret+="&addWorkspace=false";
        }
    	return ret;
		
	}
	function optionChanged(flag) {
		if (flag == 'otype') {
			var index1  = document.umAddUserForm.selectedOrgType.selectedIndex;
			var val1    = document.umAddUserForm.selectedOrgType.options[index1].value;
			var orgType = document.umAddUserForm.orgType.value;
			if ( val1 != "-1") {
				if (val1 != orgType) {
					document.umAddUserForm.orgType.value = val1;
					document.umAddUserForm.actionFlag.value = "typeSelected";
					<digi:context name="selectType" property="context/module/moduleinstance/addUser.do" />
		   			document.umAddUserForm.action = "<%= selectType %>";
					document.umAddUserForm.target = "_self";
					//document.umAddUserForm.submit();
					var url = "<%=selectType %>";
					YAHOO.util.Connect.asyncRequest("POST",url +"?"+ getParams(), callback, '');
				}
				return false;
			}
			else
				return false;
		}
		if (flag == 'ogroup') {
			var index2  = document.umAddUserForm.selectedOrgGroup.selectedIndex;
			var val2    = document.umAddUserForm.selectedOrgGroup.options[index2].value;
			var orgGrp = document.umAddUserForm.orgGrp.value;
			if ( val2 != "-1") {
				if (val2 != orgGrp) {
					document.umAddUserForm.orgGrp.value = val2;
					document.umAddUserForm.actionFlag.value = "groupSelected";
					<digi:context name="selectGrp" property="context/module/moduleinstance/addUser.do" />
		   			document.umAddUserForm.action = "<%= selectGrp %>";
					document.umAddUserForm.target = "_self";
					//document.umAddUserForm.submit();
					var url = "<%=selectGrp %>";
					YAHOO.util.Connect.asyncRequest("POST",url +"?"+ getParams(), callback, '');
					
				}
				return false;
			}
			else
				return false;
		}
	}

	function cancel()
	{
		document.umAddUserForm.action = "/um/viewAllUsers.do";
		document.umAddUserForm.target = "_self";
		document.umAddUserForm.submit();
		return false;
	}
	function isVoid(name){
        if (name == "" || name == null || !isNaN(name) || name.charAt(0) == ' '){
        	return true;
        }		
		return false;		
	}
	function validate(){
        name = document.umAddUserForm.firstNames.value;
        lastname = document.umAddUserForm.lastName.value;
        password = document.umAddUserForm.password.value;
        passwordConfirmation = document.umAddUserForm.passwordConfirmation.value;
        selectedOrgType = document.umAddUserForm.selectedOrgType.value;
        selectedOrgGroup = document.umAddUserForm.selectedOrgGroup.value;
        selectedOrganizationId = document.umAddUserForm.selectedOrganizationId.value;
        
        if (isVoid(name))
        {
			<c:set var="translation">
			<digi:trn key="erroruregistration.FirstNameBlank">First Name is Blank</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if (isVoid(lastname))
        {
			<c:set var="translation">
			<digi:trn key="error.registration.LastNameBlank">LastName is Blank</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(validateEmail()==false)
            return false
        if (isVoid(password)||isVoid(passwordConfirmation))
        {
			<c:set var="translation">
			<digi:trn key="error.registration.passwordBlank">Password field is Blank</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(password != passwordConfirmation){
			<c:set var="translation">
			<digi:trn key="error.registration.NoPasswordMatch">Passwords in both fields must be the same</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrgType=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.enterorganizationother">Please enter Organization Type</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrgGroup=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.NoOrgGroup">Please Select Organization Group</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }
        if(selectedOrganizationId=="-1"){
			<c:set var="translation">
			<digi:trn key="error.registration.NoOrganization">Please Select Organization</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
        }

        return true;
	}
	function validateEmail() {
	    var reg = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
		var address = document.umAddUserForm.email.value;
		var address2 = document.umAddUserForm.emailConfirmation.value;
		if(reg.test(address) == false||reg.test(address2) == false) {
			<c:set var="translation">
			<digi:trn key="error.registration.noemail">you must enter Valid email please check in</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		if(address != address2){
			<c:set var="translation">
			<digi:trn key="error.registration.noemailmatch">Emails in both fields must be the same</digi:trn>
    		</c:set>
			alert("${translation}");
        	return false;
		}
		return true;
	}

	function init_addUserForm() {
		var u = document.getElementsByName('email')[0];
		u.setAttribute("autocomplete", "off");
		
		var u = document.getElementsByName('emailConfirmation')[0];
		u.setAttribute("autocomplete", "off");
		
		var u = document.getElementsByName('password')[0];
		u.setAttribute("autocomplete", "off");
		
		var u = document.getElementsByName('passwordConfirmation')[0];
		u.setAttribute("autocomplete", "off");
		
	}
	//YAHOO.util.Event.addListener(window, "load", init_addUserForm) ;
	function registerNewUser(){
      if(validate()){
        userRegistered = true;
		<digi:context name="addNewUser" property="context/module/moduleinstance/registerUser.do" />
		var url = "<%=addNewUser %>";
		YAHOO.util.Connect.asyncRequest("POST",url +"?"+ getParams(), callback, '');
         
      }
    }
	
</script>
<script language="JavaScript">
	function deleteWS(id){
		document.umAddUserForm.teamMemberId.value=id;
		document.umAddUserForm.actionFlag.value = "deleteWS";
		<digi:context name="selectType" property="context/module/moduleinstance/addWorkSpaceUser.do" />
		var url = "<%= selectType %>";
		url += "?"+getParamsWS()+"&teamMemberId="+id+"&actionFlag=deleteWS";
		YAHOO.util.Connect.asyncRequest("POST",url, callback, '');
	}
	function cancelAddWorkSpace()
	{
        ret="firstNames="+
    	"&lastName="+
    	"&email="+
    	"&emailConfirmation="+
    	"&password="+
    	"&passwordConfirmation="+
		"&role=-1"+
		"&teamId=-1"+
		"&addWorkspace=false"+
		"&selectedOrgType=-1"+
		"&selectedOrgGroup=-1"+
		"&selectedOrganizationIde=-1"+
		"&sendEmail=-1"+
		"&teamId=-1";
		
		<digi:context name="commentUrl" property="context/um/addUser.do"/>  
		var url = "<%=commentUrl %>";
		url += "?"+ret;
		YAHOO.util.Connect.asyncRequest("POST",url, callback, '');
	}
	function validateWS()
	{
		if(document.umAddUserForm.teamId.value=="-1"){
			<c:set var="translation">
			<digi:trn key="aim:chooseTeam">Please choose workspace</digi:trn>
    		</c:set>
			
			alert("${translation}");
			document.umAddUserForm.teamId.focus();
			return false;
		}	
		if(document.umAddUserForm.role.value=="-1"){
			<c:set var="translation">
			<digi:trn key="aim:chooseRole">Please choose role</digi:trn>
    		</c:set>
			
			alert("${translation}");
			document.umAddUserForm.role.focus();
			return false;
		}	
		return true;
	}
	function getParamsWS(){
        var ret=""
        ret="firstNames="+document.getElementsByName('firstNames')[0].value+
        	"&lastName="+document.getElementsByName('lastName')[0].value+
        	"&email="+document.getElementsByName('email')[0].value+
        	"&emailConfirmation="+document.getElementsByName('emailConfirmation')[0].value+
        	"&password="+document.getElementsByName('password')[0].value+
        	"&passwordConfirmation="+document.getElementsByName('passwordConfirmation')[0].value+
        	"&selectedCountryResidence="+document.getElementsByName('selectedCountryResidence')[0].value+
        	//"&mailingAddress="+document.getElementsByName('mailingAddress')[0].value+
        	"&selectedOrgType="+document.getElementsByName('selectedOrgType')[0].value+
        	"&selectedOrgGroup="+document.getElementsByName('selectedOrgGroup')[0].value+
        	"&sendEmail="+document.getElementsByName('sendEmail')[0].value+
        	"&addWorkspace="+document.getElementsByName('addWorkspace')[0].value+
        	"&organizationName="+document.getElementsByName('organizationName')[0].value+
        	"&selectedOrganizationId="+document.getElementsByName('selectedOrganizationId')[0].value+
        	"&selectedLanguage="+document.getElementsByName('selectedLanguage')[0].value+
        	"&teamId="+document.getElementsByName('teamId')[0].value+
        	"&role="+document.getElementsByName('role')[0].value;
    	return ret;
		
	}
	
	function addWorkSpace(){
		if(validateWS()){
			//var msg='\n<digi:trn>Add New User</digi:trn>';
			//showPanelLoading(msg);
			<digi:context name="commentUrl" property="context/um/addWorkSpaceUser.do"/>  
			var url = "<%=commentUrl %>";
			YAHOO.util.Connect.asyncRequest("POST",url+"?"+getParamsWS(), callback, '');
		}
	}
</script>

<c:set var="translationBan">
	<digi:trn key="um:confirmBanMsg">Do you really want to ban the user ?</digi:trn>
</c:set>

<c:set var="translationUnban">
	<digi:trn key="um:confirmUnbanMsg">Do you really want to remove the ban ?</digi:trn>
</c:set>

<script language="JavaScript">
function banUser(txt) {
  var ban=confirm("${translationBan}");
  return ban;
  }
  
 function unbanUser(txt) {
  var ban=confirm("${translationUnban}");
  return ban;
  }
  
  
  function searchAlpha(val) {
		     document.umViewAllUsersForm.action = "/um/viewAllUsers.do?currentAlpha="+val;
		     document.umViewAllUsersForm.submit();
			 return true;		
	}

</script>


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=850>
    <tr>
      		<td align=left vAlign=top>
		        <table cellPadding=5 cellSpacing=0 width="100%">
		          	<tr>
		            <!-- Start Navigation -->
			            <td height=33>
			              <span class=crumb>
			                <c:set var="translation">
			                  <digi:trn key="um:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
			                </c:set>
			                <digi:link module="aim" href="/admin.do" styleClass="comment" title="${translation}" >
			                  <digi:trn key="um:AmpAdminHome">
			                  Admin Home
			                  </digi:trn>
			                </digi:link>&nbsp;&gt;&nbsp;
			
			                <digi:trn key="um:users">
			                Users
			                </digi:trn>
			              </span>
			            </td>
		            <!-- End navigation -->
		          	</tr>
		         	<tr>
			            <td>
			              <digi:errors/> 
			              <span class=subtitle-blue>
			                <digi:trn key="um:viewAllUsers:ListOfUsers">
			                List of users
			                </digi:trn>
			              </span>
			            </td>
		          	</tr>
                      <tr>
                        <td>
                            <jsp:include page="/repository/aim/view/exportTable.jsp" />
                        </td>
                    </tr>
                    <digi:instance property="umViewAllUsersForm" />
                    <digi:context name="digiContext" property="context" />

                    <digi:form action="/viewAllUsers.do" method="post">
		        	<tr style="width:50%;">
			          	<c:choose>
			          		<c:when test="${umViewAllUsersForm.showBanned}">
			          			<td width="350">
					              <digi:trn key="um:viewAllUsers:filter">
					              	Filter by:
					              </digi:trn>
					              <html:select property="type" style="font-family:verdana;font-size:11px;" disabled="true" onchange="document.umViewAllUsersForm.submit()">
					                <c:set var="translation">
					                  <digi:trn key="um:viewAllUsers:all">
					                  -All-
					                  </digi:trn>
					                </c:set>
					                <html:option value="-1">${translation}</html:option>
					              </html:select>
				              </td>
			          		</c:when>
			          		<c:otherwise>
					            <td width="350px;" >
						        	<digi:trn key="um:viewAllUsers:filter">
						              	Filter by:
						            </digi:trn>
						            <html:select property="type" style="font-family:verdana;font-size:11px; margin-right:70px;" onchange="document.umViewAllUsersForm.submit()">
						                <c:set var="translation">
						                 	<digi:trn key="um:viewAllUsers:all">
						                 	 -All-
						                  	</digi:trn>
						                </c:set>
						                <html:option value="-1">${translation}</html:option>
										<c:set var="translation">
						                  <digi:trn key="um:viewAllUsers:regisetred">
						                  Registered
						                  </digi:trn>
						                </c:set>
						                <html:option value="0">${translation}</html:option>
										<c:set var="translation">
						                  <digi:trn key="um:viewAllUsers:workspaceMembers">
						                  Workspace members
						                  </digi:trn>
						                </c:set>
						                <html:option value="1">${translation}</html:option>	                
						            </html:select>
					           	</td>  
				            </c:otherwise>
			            </c:choose>
			            <!--  <td width="300px" style="background-color:#99FF66">  -->
			            <c:if test="${not empty umViewAllUsersForm.currentAlpha}">
					    	<c:if test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
						    	<c:if test="${umViewAllUsersForm.currentAlpha!=''}">														    	
							    	<c:set var="trnViewAllLink">
										<digi:trn key="aim:clickToViewAllSearchPages">Click here to view all search pages</digi:trn>
									</c:set>
									<a href="javascript:searchAlpha('viewAll')" title="${trnViewAllLink}">
										<digi:trn key="aim:viewAllLink"><!-- viewAll  --></digi:trn></a>
								</c:if>
							</c:if>
					    </c:if>
						<td width="300px;" >
			            	<digi:trn>Go to:</digi:trn>
							<html:select property="currentAlpha" style="font-family:verdana;font-size:11px; margin-right:100px;" onchange="document.umViewAllUsersForm.submit()">
								<html:option value="viewAll">-All-</html:option>
								<c:if test="${not empty umViewAllUsersForm.alphaPages}">
								<logic:iterate name="umViewAllUsersForm" property="alphaPages" id="alphaPages" type="java.lang.String">
									<c:if test="${alphaPages != null}">
										<html:option value="<%=alphaPages %>"><%=alphaPages %></html:option>
									</c:if>
								</logic:iterate>
								</c:if>
							</html:select>
						</td>
						<td width="850px;" >
				   		    <digi:trn key="um:viewAllUsers:keyword">
					        Keyword:
					        </digi:trn>
					        <html:text property="keyword" style="font-family:verdana;font-size:11px; width:130px; text-align:left;"/>
							<c:set var="translation">
								<digi:trn key="um:viewAllUsers:showButton">
									 Show
								</digi:trn>
							</c:set>
							<input type="submit" value="${translation}"  class="dr-menu" style="font-family:verdana;font-size:11px;" />
					    </td>
			            <td>
			
						</td>
		        	</tr>
		        	<tr>
		            <td noWrap width=850 vAlign="top" colspan="7">
		            	<table width="100%" cellspacing=1 cellSpacing=1>
							<tr>
								<td vAlign="top"> 
									<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%"  >
										<tr >
											<td vAlign="top" width="100%">
											</td>
										</tr>
										<tr >
											<td valign="top">
												<table align=center bgColor=#ffffff cellPadding=0 cellSpacing=0  border=0 >
													<tr>
														<td bgColor=#ffffff >
															<table border=0 cellPadding=0 cellSpacing=0  width="100%">
																<tr bgColor=#ffffff>
																	<!-- header -->
																	<td height="" align="center" colspan="5"><B>
																	<!-- <digi:trn key="um:users"></digi:trn> -->
		                                                              </b>
																	</td>
																	<!-- end header -->
																</tr>		
		 														<tr>
																	<td width="100%">
																		<div class='yui-skin-sam'>
																			<div id="dynamicdata" class="report"></div>
																			<div id="dt-pag-nav"></div>
																			<div id="errors"></div>
																		</div>
																	</td>
																</tr>
																<tr>
															
																</tr>
																<logic:notEmpty name="umViewAllUsersForm" property="alphaPages">
																	<tr>
																		<td align="center" colspan="4">
																	
																		</td>
																	</tr>
																</logic:notEmpty>									
																<logic:notEmpty name="umViewAllUsersForm" property="alphaPages">
																	<tr>
																		<td bgColor=#ffffff>
																			<c:if test="${not empty umViewAllUsersForm.currentAlpha}">
																				<c:if test="${umViewAllUsersForm.currentAlpha!='viewAll'}">
																			   		<c:if test="${umViewAllUsersForm.currentAlpha!=''}">														    	
																			    		<digi:trn key="um:UserMan:alphaFilterNote">
																							Go to -All- to see all existing Users.
																						</digi:trn>
																					</c:if>
																				</c:if>
																			</c:if>										
																		</td>
																	</tr>
																</logic:notEmpty>	
							                         		</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
							    </td>
								<td noWrap width="180" vAlign="top">
									<table align=center cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td bgColor=#c9c9c7 class=box-title height="20">
												<digi:trn key="aim:Links">
												Links
												</digi:trn>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table cellPadding=5 cellSpacing=1 width="100%">
													<tr>
														<td>
															<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" 	width="15" height="10"/>
														</td>
														<td>
															<a href="javascript:addNewUser();">
										 						<digi:trn key="aim:addNewUser">
																	Add new user
																</digi:trn>
															</a>
														</td>
													</tr>																								
													<tr>
														<td>
															<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" 	width="15" height="10"/></td>
														<td>
															<digi:link module="aim"  href="/admin.do">
															<digi:trn key="aim:AmpAdminHome">
															Admin Home
															</digi:trn>
															</digi:link>
														</td>
													</tr>
													<tr>
														<td>
															<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" 	width="15" height="10"/></td>
														<td>
															<digi:link  module="aim" href="/workspaceManager.do~page=1">
															<digi:trn key="aim:WorkspaceManager">
															Workspace Manager
															</digi:trn>
															</digi:link>
														</td>
													</tr>
													<c:choose>
														<c:when test="${umViewAllUsersForm.showBanned}">
															<tr>
																<td>
																	<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=false">
																	<digi:trn key="aim:ViewActiveUsers">
																	View Active Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:when>
														<c:otherwise>
															<tr>
																<td>
																	<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" 	width="15" height="10"/></td>
																<td>
																	<digi:link  module="aim" href="/../um/viewAllUsers.do~showBanned=true">
																	<digi:trn>
																	View Inactive Users
																	</digi:trn>
																	</digi:link>
																</td>
															</tr>
														</c:otherwise>
													</c:choose>
													<!-- end of other links -->
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
</digi:form>



