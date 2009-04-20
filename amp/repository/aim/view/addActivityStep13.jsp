<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronousSendNotNull.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/dhtml-suite-for-applications.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/jquery-latest.pack.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/jquery.disable.text.select.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>

<!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
<!--  -->


<div id="myContract" style="display: none">
	<div id="myContractContent" class="content">
		<jsp:include page="/aim/editIPAContract.do?new=true" />
	</div>
</div>

<div id="popin2" style="display: none">
	<div id="popinContent2" class="content">
	</div>
</div>

<script type="text/javascript">
<!--
		YAHOOAmp.namespace("YAHOOAmp.amptab");
		YAHOOAmp.amptab.init = function() {
		    		var tabView = new YAHOOAmp.widget.TabView('tabview_container');
		};
	    var myPanel5 = new YAHOOAmp.widget.Panel("newmyContract", {
			width:"800px",
		    fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"] 
		    }
		     );
		var myPanel2 = new YAHOOAmp.widget.Panel("newpopins", {
			width:"300px",
			height:"200px",
			fixedcenter: true,
		    constraintoviewport: false,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true,
		    context: ["showbtn", "tl", "bl"]
		    });
		
	//myPanel1.beforeHideEvent.subscribe(YAHOOAmp.amptab.handleClose);
	var panelStart, panelStart2;
	var checkAndClose2=false;	    
	function initScripts() {
		var msgP5='\n<digi:trn key="aim:addeditContracting">Add/Edit Contracting</digi:trn>';
		myPanel5.setHeader(msgP5);
		myPanel5.setBody("");
		myPanel5.beforeHideEvent.subscribe(function() {
			panelStart=1;
		});		
		
		myPanel5.render(document.body);
		panelStart = 0;
		
		var msgP2='\n<digi:trn key="aim:selectOrganization">Select Organization</digi:trn>';
		myPanel2.setHeader(msgP2);
		myPanel2.setBody("");
		myPanel2.beforeHideEvent.subscribe(function() {
			panelStart2=1;
		});		
		myPanel2.render(document.body);
		panelStart2 = 0;
		
	}
	
	function showSave() {
		var element5 = document.getElementById("myContract");
		element5.style.display = "inline";
		if (panelStart < 1){
			myPanel5.setBody(element5);
		}
		if (panelStart < 2){
			document.getElementById("myContractContent").scrollTop=0;
			myPanel5.show();
			panelStart = 2;
		}
	}
	function hideAddContract() {
		panelStart = 1;
		myPanel5.hide();
	}
	
	function resetFilter(){
		if (aimReportsFilterPickerForm.text)
			aimReportsFilterPickerForm.text.value="";

		if (aimReportsFilterPickerForm.selectedDonorGroups)
			aimReportsFilterPickerForm.selectedDonorGroups.selectedIndex=-1;
			
		if (aimReportsFilterPickerForm.governmentApprovalProcedures){
			aimReportsFilterPickerForm.governmentApprovalProcedures[0].checked=false;
			aimReportsFilterPickerForm.governmentApprovalProcedures[1].checked=false;
		}
	}

	window.onload=initScripts();
	
	<logic:present parameter="displayAdd" >
			var current2 = window.onload;
			window.onload = function() {
	            current2.apply(current2);
				showSave();
        	};
	</logic:present>
-->	
</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myContract .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	#popin2 .content { 
	    overflow:auto; 
	    height:455px; 
	    background-color:fff; 
	    padding:10px; 
	} 
</style>



<script language="JavaScript">
    <!--
    
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
         window.location.reload();
    }
    
    
    function callUrl(indexId){
	    var async=new Asynchronous();
	    async.complete=mapCallBack;
	    async.call("/aim/editIPAContract.do?deleteEU&indexId="+indexId);
    }
    
    
    
    function addIPAContract() {
        openNewWindow(900, 600);
        <digi:context name="editIPAContract" property="context/module/moduleinstance/editIPAContract.do?new" />
        document.aimEditActivityForm.action = "<%= editIPAContract %>";
        document.aimEditActivityForm.target = popupPointer.name;
        document.aimEditActivityForm.submit();
    }
    
    var responseSuccess = function(o){ 
	/* Please see the Success Case section for more
	 * details on the response object's properties.
	 * o.tId
	 * o.status
	 * o.statusText
	 * o.getResponseHeader[ ]
	 * o.getAllResponseHeaders
	 * o.responseText
	 * o.responseXML
	 * o.argument
	 */
		var response = o.responseText; 
		var content = document.getElementById("myContractContent");
	    //response = response.split("<!")[0];
		content.innerHTML = response;
	    //content.style.visibility = "visible";
		showSave();
	}
		 
	var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		alert("Connection Failure!"); 
	}  
	var callback = 
	{ 
		success:responseSuccess, 
		failure:responseFailure 
	};
    
	function addContract(){
        var postString		= "new=true";
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
	}
    
    function editContract(indexId) {
        var postString		= "editEU=true&indexId="+indexId;
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/editIPAContract.do", callback, postString);
    }
    
    function deleteContract(indexId) {
        
        <digi:context name="editIPAContract" property="context/module/moduleinstance/deleteIPAContract.do?indexId=" />
        document.aimEditActivityForm.action = "<%=editIPAContract%>"+indexId;
        document.aimEditActivityForm.target = "_self";
        document.aimEditActivityForm.submit();
    }
    
    
  
    function validateForm() {
        return true;
    }

	var responseSuccess2 = function(o){
		/* Please see the Success Case section for more
		 * details on the response object's properties.
		 * o.tId
		 * o.status
		 * o.statusText
		 * o.getResponseHeader[ ]
		 * o.getAllResponseHeaders
		 * o.responseText
		 * o.responseXML
		 * o.argument
		 */
			var response = o.responseText; 
			var content = document.getElementById("popinContent2");
		    //response = response.split("<!")[0];
			content.innerHTML = response;
		    //content.style.visibility = "visible";
			
			showContent2();
		}
	 
		var responseFailure2 = function(o){ 
		// Access the response object's properties in the 
		// same manner as listed in responseSuccess( ). 
		// Please see the Failure Case section and 
		// Communication Error sub-section for more details on the 
		// response object's properties.
			//alert("Connection Failure!"); 
		}  
	
	var callback2 =
	{ 
		success:responseSuccess2, 
		failure:responseFailure2 
	};
/*	function showContent2(){
		var element = document.getElementById("popin2");
		element.style.display = "inline";
		myPanel2.setBody(element);
		document.getElementById("popin2").scrollTop=0;
		myPanel2.show();
	}*/
	function showContent2(){
		var element = document.getElementById("popin2");
		element.style.display = "inline";
		if (panelStart2 < 1){
			myPanel2.setBody(element);
		}
		if (panelStart2 < 2){
			document.getElementById("popin2").scrollTop=0;
			myPanel2.show();
			panelStart2 = 2;
		}
		checkErrorAndClose2();
	}
	function checkErrorAndClose2(){
		if(checkAndClose2==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				myclose2();
				refreshPage2();
			}
			checkAndClose2=false;			
		}
	}
	function refreshPage2(){
		orgsAdded();
		//document.aimEditActivityForm.step.value = "13";
		//document.aimEditActivityForm.action = "/aim/addActivity.do?edit=true";
		//document.aimEditActivityForm.target = "_self";
		//document.aimEditActivityForm.submit();
	}

	function myclose2(){
		var content = document.getElementById("popinContent2");
		content.innerHTML="";
		panelStart2=1;
		myPanel2.hide();
	
	}

	function closeWindow() {
		myclose2();
	}
	function showPanelLoading(msg){
		myPanel2.setHeader(msg);		
		var content = document.getElementById("popinContent2");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent2();
	}
	
	function selectOrg(params1, params2, params3, params4) {
		myPanel2.cfg.setProperty("width","600px");
		myPanel2.cfg.setProperty("height","500px");
		var msg='\n<digi:trn key="aim:selectOrg">Select Organization</digi:trn>';
		showPanelLoading(msg);
		YAHOOAmp.util.Connect.asyncRequest("POST", params1, callback2);
	}

	function checkNumeric(objName,comma,period,hyphen)
	{
		var numberfield = objName;
		if (chkNumeric(objName,comma,period,hyphen) == false)
		{
			numberfield.select();
			numberfield.focus();
			return false;
		}
		else
		{
			return true;
		}
	}

	function chkNumeric(objName,comma,period,hyphen)
	{
// only allow 0-9 be entered, plus any values passed
// (can be in any order, and don't have to be comma, period, or hyphen)
// if all numbers allow commas, periods, hyphens or whatever,
// just hard code it here and take out the passed parameters
		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = objName;
		var allValid = true;
		var decPoints = 0;
		var allNum = "";

		for (i = 0;  i < checkStr.value.length;  i++)
		{
			ch = checkStr.value.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
			if (ch == checkOK.charAt(j))
			break;
			if (j == checkOK.length)
			{
				allValid = false;
				break;
			}
			if (ch != ",")
			allNum += ch;
		}
		if (!allValid)
		{
			alertsay = "Please enter only numbers in the \"Number of results per page\"."
			alert(alertsay);
			return (false);
		}
	}



	function validate() {
		<c:set var="translation">
			<digi:trn key="aim:chooseOrganizationToAdd">Please choose an organization to add</digi:trn>
		</c:set>
		if(document.aimSelectOrganizationForm.selectedOrganisationFromPages.value != "-1") return true;

		if (document.aimSelectOrganizationForm.selOrganisations.checked != null) { // only one
			if (document.aimSelectOrganizationForm.selOrganisations.checked == false) {
				alert("${translation}");
				return false;
			}
		}
		else { // many
			var length = document.aimSelectOrganizationForm.selOrganisations.length;
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimSelectOrganizationForm.selOrganisations[i].checked == true) {
					flag = 1;
					break;
				}
			}

			if (flag == 0) {
				alert("${translation}");
				return false;
			}
		}
		return true;
	}
	
	
	function setOrganization(id) {
		<digi:context name="selOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
	    //document.aimSelectOrganizationForm.action = "<%= selOrg %>&id="+id;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    //document.aimSelectOrganizationForm.submit();
	    checkAndClose2=true;
		var urlParams="<%=selOrg%>";
		var params="edit=true&orgSelReset=false&subAction=organizationSelected&id="+id;
		YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback2);
		    
		//return true;
	}
	
	function selectOrganization() {
		var flag = validate();
		if (flag == false)
			return false;

		
		<digi:context name="selOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
	    //document.aimSelectOrganizationForm.action = "<%= selOrg %>";
		//document.aimSelectOrganizationForm.target = window.opener.name;
		document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=-1;
	    //document.aimSelectOrganizationForm.submit();
	    checkAndClose2=true;
		var url = "<%=selOrg %>"
		var params = "?edit=true&orgSelReset=false&subAction=organizationSelected"+getParams();    
		YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback2);
		
		return true;
	}

	function resetForm() {
		document.aimSelectOrganizationForm.ampOrgTypeId.value=-1;
		document.aimSelectOrganizationForm.keyword.value="";
		document.aimSelectOrganizationForm.tempNumResults.value=10;
	
	}




	function selectOrganizationPages(page) {
	   <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do" />
	   //var val = "<%=searchOrg%>";
	   //val = val + page;
	   //document.aimSelectOrganizationForm.action = val;

	   document.aimSelectOrganizationForm.selectedOrganisationFromPages.value=page;
	   var urlParams="<%=searchOrg%>";
	   var params="edit=true&orgSelReset=false&subAction=selectPage&page="+page;
	   YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+"?"+params, callback2);
	   //document.aimSelectOrganizationForm.submit();
	   //return true;
	}	
	function getParams(){
		ret="";
		ret+="&selectedOrganisationFromPages="+document.getElementsByName('selectedOrganisationFromPages')[0].value+
		"&keyword="+document.getElementsByName('keyword')[0].value+
		"&ampOrgTypeId="+document.getElementsByName('ampOrgTypeId')[0].value;
		//else if (type==3){//add sectors chosen from the list
		if(document.getElementsByName("selOrganisations")!=null){
			var sectors = document.getElementsByName("selOrganisations").length;
			for(var i=0; i< sectors; i++){
				if(document.getElementsByName("selOrganisations")[i].checked){
					ret+="&"+document.getElementsByName("selOrganisations")[i].name+"="+document.getElementsByName("selOrganisations")[i].value;
				}
			}
		}
	
		return ret;
	}
	function searchOrganization() {
		if(checkNumeric(document.aimSelectOrganizationForm.tempNumResults	,'','','')==true)
		{
			if (document.aimSelectOrganizationForm.tempNumResults.value == 0) {
				  alert ("Invalid value at 'Number of results per page'");
				  document.aimSelectOrganizationForm.tempNumResults.focus();
				  //return false;
			} else {
				 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
			    //document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
			    //document.aimSelectOrganizationForm.submit();
			    var url = "<%=searchOrg %>"
				var params = "?edit=true&subAction=search"+getParams();    
			    YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback2);
				//return true;
			}
		}
		else return false;
	}




	function searchAlpha(val) {
		if (document.aimSelectOrganizationForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimEditActivityForm.tempNumResults.focus();
			  //return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
			 //url = "<%= searchOrg %>?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";
		     //document.aimSelectOrganizationForm.action = url;
		     //document.aimSelectOrganizationForm.submit();
		     var url = "<%=searchOrg %>"
			 var params = "?alpha=" + val + "&orgSelReset=false&edit=true&subAction=search";    
			 YAHOOAmp.util.Connect.asyncRequest("POST", url+params, callback2);
		     
			 //return true;
		}
	}
		
	function searchAlphaAll(val) {
		if (document.aimSelectOrganizationForm.tempNumResults.value == 0) {
			  alert ("Invalid value at 'Number of results per page'");
			  document.aimSelectOrganizationForm.tempNumResults.focus();
			  //return false;
		} else {
			 <digi:context name="searchOrg" property="context/module/moduleinstance/selectOrganizationComponent.do"/>
			  //  document.aimSelectOrganizationForm.action = "<%= searchOrg %>";
		      var aux= document.aimSelectOrganizationForm.tempNumResults.value;
		      document.aimSelectOrganizationForm.tempNumResults.value=1000000;
		     //document.aimSelectOrganizationForm.submit();

			   var urlParams="<%=searchOrg%>";
			   var params="?edit=true&subAction=search&tempNumResults=1000000";
			   YAHOOAmp.util.Connect.asyncRequest("POST", urlParams+params, callback2);
			      document.aimSelectOrganizationForm.tempNumResults.value=aux;		      
			  //return true;
		}
	}


	
	-->
</script>

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">

<html:hidden property="step"/>

<html:hidden property="editAct"/>
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
</c:set>



<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border=0>
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td><jsp:include page="t.jsp"/>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<c:set var="translation">
										<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
									</c:set>
									<digi:link href="/admin.do" styleClass="comment" title="${translation}">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">
									<c:set var="translation">
										<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop </digi:trn>
									</c:set>
									<c:set var="message">
										<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>
									<c:set var="quote">'</c:set>
									<c:set var="escapedQuote">\'</c:set>
									<c:set var="msg">
										${fn:replace(message,quote,escapedQuote)}
									</c:set>

									<digi:link href="/viewMyDesktop.do" styleClass="comment"  onclick="return quitRnot1('${msg}')" title="${translation}">

										<digi:trn key="aim:portfolio">Portfolio</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>

                                <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
                                   <c:set property="translation" var="trans">
                                       <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
                                           Click here to goto Add Activity Step ${step.stepActualNumber}
                                       </digi:trn>
                                   </c:set>
                                   <c:set var="link">
										<c:if test="${step.stepNumber==9}">
										  /editSurveyList.do?edit=true
										</c:if>
                                        <c:if test="${step.stepNumber!=9}">
                                        
                                            /addActivity.do?step=${step.stepNumber}&edit=true
                                            
                                        </c:if>
                                   </c:set>
                                   <c:if test="${!index.last}">
                                       <c:if test="${index.first}">
                                           <digi:link href=" ${link}" styleClass="comment" title="${trans}">
                                               <c:if test="${aimEditActivityForm.editAct == true}">
                                                   <digi:trn key="aim:editActivityStep1">
                                                       Edit Activity - Step 1
                                                   </digi:trn>
                                               </c:if>
                                               <c:if test="${aimEditActivityForm.editAct == false}">
                                                   <digi:trn key="aim:addActivityStep1">
                                                       Add Activity - Step 1
                                                   </digi:trn>
                                               </c:if>
                                           </digi:link>
                                           &nbsp;&gt;&nbsp;
                                       </c:if>
                                       <c:if test="${!index.first}">
                                           <digi:link href="${link}" styleClass="comment" title="${trans}">
                                               <digi:trn key="aim:addActivityStep${step.stepActualNumber}">
    	                                           Step ${step.stepActualNumber}
	                                           </digi:trn>
                                           </digi:link>
                                           &nbsp;&gt;&nbsp;
                                       </c:if>
                                   </c:if>
                                   <c:if test="${index.last}">
                                       <c:if test="${index.first}">
                                           <c:if test="${aimEditActivityForm.editAct == true}">
                                               <digi:trn key="aim:editActivityStep1">
                                                   Edit Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                           <c:if test="${aimEditActivityForm.editAct == false}">
                                               <digi:trn key="aim:addActivityStep1">
                                                   Add Activity - Step 1
                                               </digi:trn>
                                           </c:if>
                                       </c:if>
                                       <c:if test="${!index.first}">
                                           <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
                                       </c:if>
                                   </c:if>
                               </c:forEach>
							   </span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 vAlign=center width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>
									<bean:write name="aimEditActivityForm" property="identification.title"/>
								</c:if>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<digi:errors/>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border=0>
						<tr><td width="75%" vAlign="top">
						<table cellPadding=0 cellSpacing=0 width="100%" border=0>
							<tr>
								<td width="100%">
									<table cellPadding=0 cellSpacing=0 width="100%" border=0>
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
                                                                                             <digi:trn>
													Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>
                                                                                            ${fn:length(aimEditActivityForm.steps)}:
												 <digi:trn key="aim:stepContracting">IPA Contracting</digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td>
							<field:display name="Add IPA Contract" feature="Contracting">
								<div style="width:99.5%;height:12px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
						            <span style="cursor:pointer;float:left;">
						                <a class="settingsLink" onClick="addContract();">
						                	<digi:trn key="aim:addIPAContract">Add IPA Contract</digi:trn>
						                </a>
					                </span>
				                </div> 
							</field:display>
                            </td></tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
								<!-- contents -->
								 <logic:notEmpty name="aimEditActivityForm" property="contracts.contracts">
				                      <table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
					                      <c:forEach items="${aimEditActivityForm.contracts.contracts}" var="contract" varStatus="idx">
						                      <tr><td bgColor=#f4f4f2 align="center" vAlign="top">
					                          <table width="100%" border="0" cellspacing="2" cellpadding="2" align="left" class="box-border-nopadding">
					                               <field:display name="Contract Name" feature="Contracting">
					                               <tr>
					                                   <td align="left" width="25%">
					                                       <b><digi:trn key="aim:IPA:popup:name">Contract name:</digi:trn></b>
					                                   </td>
					                                   <td>
					                                        ${contract.contractName}
				                                       </td>
					                               </tr>
					                               </field:display>
				                                  <field:display name="Contract Description" feature="Contracting">
					                                  <tr>
					                                      <td align="left">
					                                          <b><digi:trn key="aim:IPA:popup:description">Description:</digi:trn></b>
					                                      </td>
					                                      <td>
					                                         ${contract.description}
					                                      </td>
					                                  </tr>
				                                  </field:display>
				                                  <field:display name="Contracting Activity Category" feature="Contracting">
					                                <tr>
				                                         <td align="left">
				                                              <b><digi:trn key="aim:IPA:popup:actCat">Activity Category:</digi:trn></b>
				                                          </td>
				                                          <td>
				                                                <c:if test ="${not empty contract.activityCategory}">
				                                                  ${contract.activityCategory.value}
				                                              </c:if>
				                                              
				                                          </td>
					                              	</tr>
					                              </field:display>
					                              <field:display name="Contracting Type" feature="Contracting">
				                                      <tr>
				                                          <td align="left">
				                                              <b><digi:trn key="aim:IPA:popup:type">type</digi:trn>:</b>
				                                          </td>
				                                          <td>
			                                                <c:if test ="${not empty contract.type}">
			                                                  ${contract.type.value}
			                                                </c:if>
				                                          </td>
				                                      </tr>
					                              </field:display>
			                                       <field:display name="Contracting Start of Tendering" feature="Contracting">
				                                      <tr>
				                                          <td align="left">
				                                              <b><digi:trn key="aim:IPA:popup:startOfTendering">Start of Tendering:</digi:trn></b>
				                                          </td>
				                                          <td>
				                                              ${contract.formattedStartOfTendering}
				                                         </td>
				                                          
				                                      </tr>
			                                       </field:display>
			                                     <field:display name="Signature of Contract" feature="Contracting">
			                                     	<tr>
			                                           <td align="left">
			                                                <b><digi:trn key="aim:IPA:popup:signatureOfContract">Signature of Contract:</digi:trn></b>
			                                            </td>
			                                            <td>
			                                                 ${contract.formattedSignatureOfContract}
			                                            </td>
			                                        </tr>	
		                                         </field:display>
			                                     <field:display name="Contract Validity Date" feature="Contracting">
			                                        <tr>
			                                            <td align="left">
			                                                <b><digi:trn key="aim:IPA:popup:contractValidityDate">Contract Validity Date</digi:trn>:</b>
			                                            </td>
			                                            <td>
			                                                 ${contract.formattedContractValidity}
			                                            </td>
			                                            
			                                        </tr>	
		                                         </field:display>
		                                        <field:display name="Contract Organization" feature="Contracting">
			                                         <tr>
			                                            <td align="left" valign="top">
			                                                <b><digi:trn key="aim:IPA:popup:contractOrg">Contract Organization:</digi:trn></b>
			                                            </td>
			                                            <td>
															<c:forEach items="${contract.organizations}" var="org">
																<c:out value="${org.name}"/><br/>
															</c:forEach>			                                                
			                                            </td>
			                                        </tr>	
		                                         </field:display>
		                                        <field:display name="Contracting Contractor Name" feature="Contracting">
			                                         <tr>
			                                            <td align="left">
			                                                 <b><digi:trn key="aim:IPA:newPopup:contractingContractorName">Contractor Name</digi:trn>:</b>
			                                             </td>
			                                             <td>
			                                                      ${contract.contractingOrganizationText}
			                                                 
			                                             </td>
			                                             
			                                         </tr>	
		                                          </field:display>
		                                         <field:display name="Contract Completion" feature="Contracting">
			                                         <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:IPA:popup:contractCompletion">Contract Completion:</digi:trn></b>
			                                             </td>
			                                             <td>
			                                                  ${contract.formattedContractCompletion}
			                                             </td>
			                                         </tr>
		                                          </field:display>
		                                         <field:display name="Contracting Status" feature="Contracting">
		                                             <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:IPA:popup:status">Status:</digi:trn></b>
			                                             </td>
			                                             <td>
			                                                 <c:if test ="${not empty contract.status}">
			                                                     ${contract.status.value}
			                                                 </c:if>
			                                             </td>
			                                         </tr>
												  </field:display>
		                                         <field:display name="Contracting Total Amount" feature="Contracting">
			                                         <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:ipa:popup:totalAmount">Total Amount</digi:trn>:</b>
			                                             </td>
			                                             <td>
			                                                 ${contract.totalAmount}
			                                                 ${contract.totalAmountCurrency} 
			                                             </td>
			                                         </tr>
		                                         </field:display>
		                                         <field:display name="Contract Total Value" feature="Contracting">
			                                         <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:ipa:popup:contractTotalValue">Contract Total Value</digi:trn>:</b>
			                                             </td>
			                                             <td>
			                                                 ${contract.contractTotalValue}
			                                                 ${contract.totalAmountCurrency} 
			                                             </td>
			                                         </tr>
		                                         </field:display>
		                                         <field:display name="Total EC Contribution" feature="Contracting">
			                                         <tr>
			                                             <td align="left" colspan="2">
			                                                 <b><digi:trn key="aim:IPA:popup:totalECContribution">Total EC Contribution:</digi:trn></b>
			                                             </td>
			                                         </tr>
		                                         </field:display>
		                                         <field:display name="Contracting IB" feature="Contracting">
			                                         <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:ipa:popup:ib">IB</digi:trn>:</b>
			                                             </td>
			                                             <td>
			                                                  ${contract.totalECContribIBAmount}
			                                                 ${contract.totalAmountCurrency} 
			                                             </td>
			                                         </tr>
		                                         </field:display>
		                                         <field:display name="Contracting INV" feature="Contracting">
			                                         <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:ipa:popup:inv">INV</digi:trn>:</b>
			                                             </td>
			                                             <td>
			                                                 ${contract.totalECContribINVAmount}
			                                                ${contract.totalAmountCurrency}
			                                             </td>
			                                         </tr>   
		                                         </field:display>
		                                         <field:display name="Contracting Total National Contribution" feature="Contracting">
			                                         <tr>
			                                             <td align="left" colspan="2">
			                                                 <b><digi:trn key="aim:IPA:popup:totalNationalContribution">Total National Contribution:</digi:trn></b>
			                                             </td>
			                                         </tr>
		                                         </field:display>
		                                         <field:display name="Contracting Central Amount" feature="Contracting">
			                                         <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:ipa:popup:central">Central</digi:trn>:</b>
			                                             </td>
			                                             <td>
			                                                 ${contract.totalNationalContribCentralAmount}
			                                                 ${contract.totalAmountCurrency} 
			                                             </td>
			                                         </tr>
		                                         </field:display>
		                                         <field:display name="Contracting Regional Amount" feature="Contracting">
			                                         <tr>
			                                             <td align="left">
			                                                 <b><digi:trn key="aim:ipa:popup:regional">Regional</digi:trn>:</b>
			                                             </td>
			                                             <td>
			                                                 ${contract.totalNationalContribRegionalAmount} 
				                                               ${contract.totalAmountCurrency}
			                                              </td>
			                                          </tr>
		                                           </field:display>
		                                          <field:display name="Contracting IFIs" feature="Contracting">
			                                          <tr>
			                                              <td align="left">
			                                                  <b><digi:trn key="aim:ipa:popup:ifis">IFIs</digi:trn>:</b>
			                                              </td>
			                                              <td>
			                                                  ${contract.totalNationalContribIFIAmount}
			                                                 ${contract.totalAmountCurrency}
			                                              </td>
			                                          </tr>
		                                           </field:display>
		                                          <field:display name="Total Private Contribution" feature="Contracting">
			                                          <tr>
			                                              <td align="left" colspan="2">
			                                                  <b><digi:trn key="aim:IPA:popup:totalPrivateContribution">Total Private Contribution:</digi:trn></b>
			                                              </td>
			                                          </tr>
			                                          <tr>
			                                              <td align="left">
			                                                  <b><digi:trn key="aim:ipa:popup:ib">IB</digi:trn>:</b>
			                                              </td>
			                                              <td>
			                                                  ${contract.totalPrivateContribAmount}
			                                                  ${contract.totalAmountCurrency}
			                                              </td>
			                                          </tr>
		                                           </field:display>
						                                           
			                                      <field:display name="Total Disbursements of Contract" feature="Contracting">
			                                          <tr>
			                                              <td align="left">
			                                                  <b><digi:trn key="aim:IPA:popup:totalDisbursements">Total Disbursements</digi:trn>:</b>
			                                              </td>
			                                              <td>
			              									${contract.totalDisbursements} &nbsp;
			              									<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
			              										&nbsp; ${contract.totalAmountCurrency}
			              									</logic:empty>
			              									<logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
			              										&nbsp; ${contract.dibusrsementsGlobalCurrency}
			              									</logic:notEmpty>
			                                              </td>
			                                          </tr>
			                                      </field:display>    
			                                      <field:display name="Contract Execution Rate" feature="Contracting">
			                                          <tr>
			                                              <td align="left">
			                                                  <b><digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:</b>
			                                              </td>
			                                              <td>
			              										&nbsp; ${contract.executionRate}
			                                              </td>
			                                          </tr>
			                                      </field:display>
			                                      <field:display name="Total Funding Disbursements of Contract" feature="Contracting">
			                                          <tr>
			                                              <td align="left">
			                                                  <b><digi:trn key="aim:IPA:popup:totalFundingDisbursements">Total Funding Disbursements</digi:trn>:</b>
			                                              </td>
			                                              <td>
			              									${contract.fundingTotalDisbursements} &nbsp;
			              									<logic:empty name="contract" property="dibusrsementsGlobalCurrency">
			              										&nbsp; ${contract.totalAmountCurrency}
			              									</logic:empty>
			              									<logic:notEmpty name="contract" property="dibusrsementsGlobalCurrency">
			              										&nbsp; ${contract.dibusrsementsGlobalCurrency}
			              									</logic:notEmpty>
			                                              </td>
			                                          </tr>
			                                      </field:display>
			                                      <field:display name="Contract Funding Execution Rate" feature="Contracting">
			                                          <tr>
			                                              <td align="left">
			                                                  <b><digi:trn key="aim:IPA:popup:contractExecutionRate">Contract Execution Rate</digi:trn>:</b>
			                                              </td>
			                                              <td>
			              										&nbsp; ${contract.fundingExecutionRate}
			                                              </td>
			                                          </tr>
			                                      </field:display>    
		                                          <field:display name="Contracting Disbursements" feature="Contracting">
			                                          <tr>
			                                              <td colspan="2">
			                                                  <b><digi:trn key="aim:IPA:popup:disbursements">Disbursements:</digi:trn></b>
			                                              </td>
			                                          </tr>
			                                          <tr>
			                                              <td>&nbsp;
			                                              </td>
			                                              <td>
		                                                      <logic:notEmpty name="contract" property="disbursements">
		                                                           <table width="100%">
																    <tr>
																		<th><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></th>
																		
																	</tr>
			                                                           <c:forEach  items="${contract.disbursements}" var="disbursement" >
			                                                               <tr>
			                                                                   <td align="center" valign="top">
			                                                                       <c:if test="${disbursement.adjustmentType==0}">
		                                                                             <digi:trn key="aim:actual">Actual</digi:trn>
			                                                                       </c:if>
			                                                                       <c:if test="${disbursement.adjustmentType==1}">
		                                                                             <digi:trn key="aim:planned">Planned</digi:trn>
			                                                                       </c:if>
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${disbursement.amount}
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                      ${disbursement.currency.currencyName} 
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${disbursement.disbDate}
			                                                                   </td>
			                                                               </tr>
			                                                           </c:forEach>
		                                                           </table>
		                                                       </logic:notEmpty>						
			                                               </td>		
			                                           </tr>
		                                            </field:display>	
		                                            <field:display name="Contracting Funding Disbursements" feature="Contracting">
			                                          <tr>
			                                              <td colspan="2">
			                                                  <b><digi:trn key="aim:IPA:popup:fundingDisbursements">Funding Disbursements:</digi:trn></b>
			                                              </td>
			                                          </tr>
			                                          <tr>
			                                              <td>&nbsp;
			                                              </td>
			                                              <td>
		                                                      <logic:notEmpty name="aimEditActivityForm" property="funding">
		                                                           <table width="100%">
																    <tr>
																		<th><field:display name="Adjustment Type Disbursement" feature="Disbursement"><digi:trn key="aim:adjustmentTyeDisbursement">Adjustment Type Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Amount Disbursement" feature="Disbursement"><digi:trn key="aim:amountDisbursement">Amount Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Currency Disbursement" feature="Disbursement"><digi:trn key="aim:currencyDisbursement">Currency Disbursement</digi:trn></field:display></th>
																		<th><field:display name="Date Disbursement" feature="Disbursement"><digi:trn key="aim:dateDisbursement">Date Disbursement</digi:trn></field:display></th>
																		
																	</tr>
			                                                           <c:forEach  items="${aimEditActivityForm.funding.fundingDetails}" var="fundingDetail" >
			                                                           		<logic:equal name="contract" property="contractName" value="${fundingDetail.contract.contractName}">
			                                                           		<c:if test="${fundingDetail.transactionType == 1}">
			                                                               <tr>
			                                                                   <td align="center" valign="top">
			                                                                       <c:if test="${fundingDetail.adjustmentType==0}">
		                                                                             <digi:trn key="aim:actual">Actual</digi:trn>
			                                                                       </c:if>
			                                                                       <c:if test="${fundingDetail.adjustmentType==1}">
		                                                                             <digi:trn key="aim:planned">Planned</digi:trn>
			                                                                       </c:if>
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${fundingDetail.transactionAmount}
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                      ${fundingDetail.currencyCode} 
			                                                                   </td>
			                                                                   <td align="center" valign="top">
			                                                                       ${fundingDetail.transactionDate}
			                                                                   </td>
			                                                               </tr>
			                                                               </c:if>
			                                                               </logic:equal>
			                                                           </c:forEach>
		                                                           </table>
		                                                       </logic:notEmpty>						
			                                               </td>		
			                                           </tr>
		                                            </field:display>
		                                            <tr><td>  
			                                            <field:display name="Edit Contract" feature="Contracting">
			                                            		<a style="cursor:pointer;color:#006699; text-decoration: underline" title="Click to edit the contract" onClick='editContract(${idx.count})'><b><digi:trn key="aim:editThisItem">Edit this item</b></digi:trn></a> 
			                                            </field:display>
			                                           <field:display name="Delete Contract" feature="Contracting">
			                                  				&nbsp;&nbsp;&nbsp; 
			                                  				<a style="cursor:pointer;color:#006699;text-decoration: underline" title="Click to remove the contract" onClick='callUrl(${idx.count})'><b><digi:trn key="aim:deleteThisItem">Delete this item</digi:trn></b></a>
			                                           </field:display>
		                                          	</td></tr>
		                                       </table>
		                                      </td></tr>
		                                   </c:forEach>
	                                   </table>
	                               </logic:notEmpty>
								  <!-- end contents -->
								</td></tr>
                                <tr><td>
                                    &nbsp;
                                </td></tr>
						</table>
						</td>
						<td width="25%" vAlign="top" align="right">
							<!-- edit activity form menu -->
								<jsp:include page="editActivityMenu.jsp" flush="true" />
							<!-- end of activity form menu -->
						</td></tr>
					</table>
				</td></tr>
				<tr><td>&nbsp;</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>
