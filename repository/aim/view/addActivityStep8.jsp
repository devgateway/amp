<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/yahoo-dom-event.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/container-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/connection-min.js"/>" ></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/panel/dragdrop-min.js"/>" ></script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#popin .content { 
	    overflow:auto; 
	    height:300px; 
	    background-color:fff; 
	    padding:10px; 
	} 
	
</style>

<div id="popin" style="display: none">
	<div id="popinContent" class="content">
	</div>
</div>

<script type="text/javascript">
<!--

		YAHOOAmp.namespace("YAHOOAmp.amp");

		var myPanel = new YAHOOAmp.widget.Panel("newpopins", {
			width:"650px",
			fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true
		    });
	var panelStart=0;
	var checkAndClose=false;
	function initContactInfoScript() {
		var msg='\n<digi:trn>Add Contact Information</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		
		myPanel.render(document.body);
	}
   
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
       window.location.reload();
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
		var content = document.getElementById("popinContent");
	    //response = response.split("<!")[0];
		content.innerHTML = response;
	    //content.style.visibility = "visible";
		
		showContent();
	}
 
	var responseFailure = function(o){ 
	// Access the response object's properties in the 
	// same manner as listed in responseSuccess( ). 
	// Please see the Failure Case section and 
	// Communication Error sub-section for more details on the 
	// response object's properties.
		//alert("Connection Failure!"); 
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
	
	function myclose(){		
		myPanel.hide();	
		panelStart=1;
	
	}
	function closeWindow() {
		myclose();
	}

	function saveContact(){		
		if(validateInfo()){
			//ajax check for duplicate email
			checkForduplicateEmail();
		}
	}

	function checkForduplicateEmail(){ //checks whether such email already exists in db
		var email=document.getElementById('email').value;
		var url=addActionToURL('activityContactInfo.do?toDo=checkDulicateEmail&email='+email);			
		var async=new Asynchronous();
		async.complete=showErrorOrSaveContact;
		async.call(url);
	}

	function showErrorOrSaveContact(status, statusText, responseText, responseXML){
		var root=responseXML.getElementsByTagName('CONTACTS')[0].childNodes[0];
		var contEmail=root.getAttribute('email');		
		if(contEmail=='exists'){
			alert('Contact with the given email already exists');			
			return false;
		}
		//if emails doesn't exist, save contact.
		myclose();
		addContact();
	}

	function addActionToURL(actionName){
	  var fullURL=document.URL;
	  var lastSlash=fullURL.lastIndexOf("/");
	  var partialURL=fullURL.substring(0,lastSlash);
	  return partialURL+"/"+actionName;
	}

	function validateInfo(){
		if(document.getElementById('name').value==null || document.getElementById('name').value==''){
			alert('Please Enter Name');
			return false;
		}
		if(document.getElementById('lastname').value==null || document.getElementById('lastname').value==''){
			alert('Please Enter lastname');
			return false;
		}
		if(document.getElementById('email').value==null || document.getElementById('email').value==''){
			alert('Please Enter email');
			return false;
		}else if(document.getElementById('email').value.indexOf('@')==-1){
			alert('Please Enter Correct Email');
			return false;
		}
		if (checkNumber('phone')==false){
			return false;
		}
		if(checkNumber('fax')==false){
			return false;
		}		
		return true;
	}

	function checkNumber(phoneOrFaxId){
	 var phoneOrFax=document.getElementById(phoneOrFaxId);
	 var number=phoneOrFax.value;
	 var validChars= "0123456789()+ ";
	 for (var i = 0;  i < number.length;  i++) {
	  var ch = number.charAt(i);
	  if (validChars.indexOf(ch)==-1){
	   alert('enter correct number');
	   phoneOrFax.value=number.substring(0,i);
	   return false;	   
	  }
	 }	 
	 return true;
	}
	
	function checkErrorAndClose(){
		if(checkAndClose==true){
			if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
				myclose();
				addContact();
			}
			checkAndClose=false;
		}
	}

	function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = "<div style='text-align: center'>" + "Loading..." + 
			"... <br /> <img src='/repository/aim/view/images/images_dhtmlsuite/ajax-loader-darkblue.gif' border='0' height='17px'/></div>";		
		showContent();
	}
	
	function AddContactButton(contactType){		
		var msg='\n<digi:trn>Add Contact Info</digi:trn>';
		showPanelLoading(msg);
		<digi:context name="contactUrl" property="context/module/moduleinstance/activityContactInfo.do" />
		var url = "<%=contactUrl %>?toDo=" + "add&contType="+contactType;
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}

	function editContact(contactType,contactId){
		var msg='\n<digi:trn>Edit Contact Info</digi:trn>';
		showPanelLoading(msg);		
		var partialUrl=addActionToURL('activityContactInfo.do');
        var url=partialUrl+'?toDo=edit&tempId='+contactId+'&contType='+contactType;
        YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}

	function deleteContact(contactType,contactId){	
		if(confirmDelete()){			
			<digi:context name="addCont" property="context/activityContactInfo.do?toDo=delete"/>;
	        document.aimEditActivityForm.action = "<%= addCont %>&tempId="+contactId;
	        document.aimEditActivityForm.target = "_self";
	        document.aimEditActivityForm.submit();
		}		
	}
	function confirmDelete(){
		var msg='<digi:trn>Do you want to remove contact ?</digi:trn>'
		return confirm(msg);
	}

	function addActionToURL(actionName){
        var fullURL=document.URL;
        var lastSlash=fullURL.lastIndexOf("/");
        var partialURL=fullURL.substring(0,lastSlash);
        return partialURL+"/"+actionName;
    }

	function addContact()
    {        
        <digi:context name="addCont" property="context/activityContactInfo.do?toDo=save"/>;
        document.contactForm.action = "<%= addCont %>";
        document.contactForm.target = "_self";
        document.contactForm.submit();
    }

    function searchContact(){    	
		var flg=checkEmpty();
		if(flg){
			var keyword=document.getElementById('keyword').value;
			<digi:context name="searchCont" property="context/activityContactInfo.do?toDo=search" />
			var url = "<%= searchCont %>&keyword="+keyword;
			YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);				 
			return true;
		}
		return false;
    }

    function checkEmpty() {
		var flag=true;
		var keyword=document.getElementById('keyword');
		if(trim(keyword.value) == "")
		{
			alert("Please Enter a Keyword....");
			flag=false;
		}		
		return flag;
	}

    function addSelectedContacts()
    {        
        <digi:context name="addSelCont" property="context/activityContactInfo.do?toDo=addSelectedConts"/>;
        document.contactForm.action = "<%= addSelCont %>";
        document.contactForm.target = "_self";
        document.contactForm.submit();
    }

    function changePrimaryState(contactType){
        
        if(contactType=='donor'){
        	var donors= $("input[@id^='donors_']");
        	var resetDonors=resetPrimary(donors);
        	if(resetDonors==true){
        		document.getElementById('don').value=true;
        	}else{
        		document.getElementById('don').value=false;
        	}    
        }else if(contactType=='mofed'){
        	var mofed= $("input[@id^='mofed_']");
        	var resetMofed=resetPrimary(mofed);
        	if(resetMofed==true){
        		document.getElementById('mof').value=true;
        	}else{
        		document.getElementById('mof').value.value=false;
        	}
        }else if(contactType=='proj'){
        	var projCoord= $("input[@id^='proj_']");
        	var resetProjCoord=resetPrimary(projCoord);
        	if(resetProjCoord==true){
        		document.getElementById('proj').value=true;
        	}else{
        		document.getElementById('proj').value=false;
        	}    
        }else if(contactType=='secMin'){
        	var secMin= $("input[@id^='secMin_']");
        	var resetSecMin=resetPrimary(secMin);
        	if(resetSecMin==true){
        		document.getElementById('secMin').value=true;
        	}else{
        		document.getElementById('secMin').value=false;
        	}	
        }
    }

    function resetPrimary(contList){
        var retValue=true;
    	for(var i=0;i<contList.length;i++){
    		if(contList[i].checked){
    			retValue=false;
    			break;
    		}
    	}
    	return retValue;
    }
	-->

</script>

<script language="JavaScript">
<!--
 function validateForm() { 
  return true;
 }

function resetAll()
{
 <digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
 document.aimEditActivityForm.action = "<%= resetAll %>";
 document.aimEditActivityForm.target = "_self";
 document.aimEditActivityForm.submit();
 return true;
}
-->
</script>
<digi:instance property="aimEditActivityForm" />
<digi:form action="/addActivity.do" method="post" onsubmit="changePrimaryState()">
<html:hidden property="step" />
<html:hidden property="editAct" />
<html:hidden name="aimEditActivityForm" property="contactInformation"/>
<html:hidden name="aimEditActivityForm" styleId="contType"  property="contactInformation.contactType"/>
<html:hidden name="aimEditActivityForm" styleId="keywordSearch"  property="contactInformation.keyword"/>
<html:hidden styleId="don" value="${aimEditActivityForm.contactInformation.resetDonorIds}" property="contactInformation.resetDonorIds"/>
<html:hidden styleId="mof" property="contactInformation.resetMofedIds" value="${aimEditActivityForm.contactInformation.resetMofedIds}"/>
<html:hidden styleId="proj" property="contactInformation.resetProjCoordIds" value="${aimEditActivityForm.contactInformation.resetProjCoordIds}"/>
<html:hidden styleId="secMin" property="contactInformation.resetSecMinIds" value="${aimEditActivityForm.contactInformation.resetSecMinIds}"/>
<c:set var="stepNm">
  ${aimEditActivityForm.stepNumberOnPage}
 </c:set>
 <digi:errors/>


<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">

<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="100%" vAlign="top" align="center" border="0">
	<tr>
		<td class="r-dotted-lg" width="10">&nbsp;</td>
		<td align="left" vAlign="top" class="r-dotted-lg">
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td>
								<span class=crumb>
								<c:if test="${aimEditActivityForm.pageId == 0}">
									<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home ">
										<digi:trn key="aim:AmpAdminHome">
											Admin Home
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								<c:if test="${aimEditActivityForm.pageId == 1}">
                                    <c:set var="message">
										<digi:trn key="aim:documentNotSaved">WARNING : The document has not been saved. Please press OK to continue or Cancel to save the document.</digi:trn>
									</c:set>
									<c:set var="quote">'</c:set>
									<c:set var="escapedQuote">\'</c:set>
									<c:set var="msg">
									${fn:replace(message,quote,escapedQuote)}
									</c:set>
									<digi:link href="/viewMyDesktop.do" styleClass="comment" onclick="return quitRnot1('${msg}')" title="Click here to view MyDesktop ">
										<digi:trn key="aim:portfolio">
											Portfolio
										</digi:trn>
									</digi:link>&nbsp;&gt;&nbsp;
								</c:if>
								          
		                           <c:forEach var="step" items="${aimEditActivityForm.steps}" end="${stepNm-1}" varStatus="index">
		                               <c:set property="translation" var="trans">
		                                   <digi:trn key="aim:clickToViewAddActivityStep${step.stepActualNumber}">
		                                       Click here to goto Add Activity Step ${step.stepActualNumber}
		                                   </digi:trn>
		                               </c:set>
		                               
		                               <c:if test="${!index.last}">                                   
		                                   <c:if test="${index.first}">
		                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
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
		                                       <digi:link href="/addActivity.do?step=${step.stepNumber}&edit=true" styleClass="comment" title="${trans}">
		                                           <digi:trn key="aim:addActivityStep${step.stepActualNumber}">Step ${step.stepActualNumber}</digi:trn>
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
		                                       <digi:trn key="aim:addActivityStep${step.stepActualNumber}"> Step ${step.stepActualNumber}</digi:trn>
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
									<digi:trn key="aim:addNewActivity">
										Add New Activity
									</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">
										Edit Activity
									</digi:trn>:
										<bean:write name="aimEditActivityForm" property="identification.title"/>
								</c:if>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top">
						<tr><td width="75%" vAlign="top">
							<table cellPadding=0 cellSpacing=0 width="100%">
								<tr>
									<td width="100%">
										<table cellPadding=0 cellSpacing=0 width="100%" border=0>
											<tr>
												<td width="13" height="20" background="module/aim/images/left-side.gif">
												</td>
												<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
													<digi:trn>
														Step</digi:trn> ${stepNm} <digi:trn>of  </digi:trn>${fn:length(aimEditActivityForm.steps)}:
	                                                <digi:trn key="aim:activity:ContactInformation">Contact Information</digi:trn>
												</td>
												<td width="13" height="20" background="module/aim/images/right-side.gif">
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr><td width="100%" bgcolor="#f4f4f2">
									<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
										<tr>
											<td bgColor="#f4f4f2" align="center" vAlign="top">												
												<!-- contents -->
												<table width="95%" bgcolor="#f4f4f2">													
													
													<feature:display name="Donor Contact Information" module="Contact Information">
														<tr>
															<td>
																<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<a title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>Donor Contact Information</digi:trn></b>
																</a>	
															</td>
														</tr>
														<tr>
															<td>
																<table style="width: 100%;height: 100%;vertical-align: top;">
																	<c:if test="${not empty aimEditActivityForm.contactInformation.donorContacts}">
																		<tr bgcolor="#cccccc"">
																			<td><strong><digi:trn>Firstname</digi:trn></strong></td>
																			<td><strong><digi:trn>Lastname</digi:trn></strong></td>
																			<td><strong><digi:trn>Email</digi:trn></strong></td>
																			<td><strong><digi:trn>Organisation</digi:trn></strong></td>
																			<td><strong><digi:trn>Phone</digi:trn></strong></td>
																			<td><strong><digi:trn>Primary</digi:trn></strong></td>
																			<td><strong><digi:trn>Actions</digi:trn></strong></td>
																		</tr>
																		<c:forEach var="donorContact" items="${aimEditActivityForm.contactInformation.donorContacts}" varStatus="stat">
																			<c:set var="background">
																				<c:if test="${stat.index%2==0}">#FFFFFF</c:if>
																				<c:if test="${stat.index%2==1}">#d7eafd</c:if>
																			</c:set>
																			<tr bgcolor="${background}">
																				<td>${donorContact.contact.name}</td>
																				<td>${donorContact.contact.lastname}</td>
																				<td>${donorContact.contact.email}</td>
																				<td>${donorContact.contact.organisationName}</td>
																				<td>${donorContact.contact.phone}</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primaryDonorContIds" styleId="donors_${stat.index}" value="${donorContact.contact.temporaryId}" onchange="changePrimaryState('donor')"/>																					
																				</td>
																				<td>
																					<a href="javascript:editContact('DONOR_CONT',${donorContact.contact.temporaryId})"><img src="/repository/message/view/images/edit.gif" border="0" /></a>
																					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams}" property="tempId">
																						<bean:write name="donorContact" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams}" property="contType" value="DONOR_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams"><img src="/repository/message/view/images/trash_12.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff"><html:button property="submitButton" styleClass="dr-menu" onclick="AddContactButton('DONOR_CONT')">Add Contact</html:button></td>
																	</tr>																	
																</table>												
															</td>
														</tr>
														<tr style="height:20px"><td/></tr>
													</feature:display>													
													<feature:display name="Government Contact Information" module="Contact Information">
														<tr>
															<td>
																<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width="15">
																<a title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>MOFED Contact Information</digi:trn></b>
																</a>	
															</td>
														</tr>
														<tr>
															<td>
																<table style="width: 100%;height: 100%;vertical-align: top;">
																	<c:if test="${not empty aimEditActivityForm.contactInformation.mofedContacts}">
																		<tr bgcolor="#cccccc"">
																			<td><strong><digi:trn>Firstname</digi:trn></strong></td>
																			<td><strong><digi:trn>Lastname</digi:trn></strong></td>
																			<td><strong><digi:trn>Email</digi:trn></strong></td>
																			<td><strong><digi:trn>Organisation</digi:trn></strong></td>
																			<td><strong><digi:trn>Phone</digi:trn></strong></td>
																			<td><strong><digi:trn>Primary</digi:trn></strong></td>
																			<td><strong><digi:trn>Actions</digi:trn></strong></td>
																		</tr>
																		<c:forEach var="mofedContact" items="${aimEditActivityForm.contactInformation.mofedContacts}" varStatus="stat">
																			<c:set var="background">
																				<c:if test="${stat.index%2==0}">#FFFFFF</c:if>
																				<c:if test="${stat.index%2==1}">#d7eafd</c:if>
																			</c:set>
																			<tr bgcolor="${background}">
																				<td>${mofedContact.contact.name}</td>
																				<td>${mofedContact.contact.lastname}</td>
																				<td>${mofedContact.contact.email}</td>
																				<td>${mofedContact.contact.organisationName}</td>
																				<td>${mofedContact.contact.phone}</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primaryMofedContIds" styleId="mofed_${stat.index}" value="${mofedContact.contact.temporaryId}" onchange="changePrimaryState('mofed')"/>
																				</td>
																				<td>
																					<a href="javascript:editContact('MOFED_CONT',${mofedContact.contact.temporaryId})"><img src="/repository/message/view/images/edit.gif" border="0" /></a>
																					<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams1}" property="tempId">
																						<bean:write name="mofedContact" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams1}" property="contType" value="MOFED_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams1"><img src="/repository/message/view/images/trash_12.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff"><html:button property="submitButton" styleClass="dr-menu" onclick="AddContactButton('MOFED_CONT')">Add Contact</html:button></td>
																	</tr>
																</table>												
															</td>
														</tr>
														<tr style="height:20px"><td/></tr>
													</feature:display>
													<feature:display name="Project Coordinator Contact Information" module="Contact Information">
														<tr>
															<td>
																<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<a title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>Project Coordinator Contact Information</digi:trn></b>
																</a>	
															</td>
														</tr>
														<tr>
															<td>
																<table style="width: 100%;height: 100%;vertical-align: top;">
																	<c:if test="${not empty aimEditActivityForm.contactInformation.projCoordinatorContacts}">
																		<tr bgcolor="#cccccc"">
																			<td><strong><digi:trn>Firstname</digi:trn></strong></td>
																			<td><strong><digi:trn>Lastname</digi:trn></strong></td>
																			<td><strong><digi:trn>Email</digi:trn></strong></td>
																			<td><strong><digi:trn>Organisation</digi:trn></strong></td>
																			<td><strong><digi:trn>Phone</digi:trn></strong></td>
																			<td><strong><digi:trn>Primary</digi:trn></strong></td>
																			<td><strong><digi:trn>Actions</digi:trn></strong></td>
																		</tr>
																		<c:forEach var="projCoordinator" items="${aimEditActivityForm.contactInformation.projCoordinatorContacts}" varStatus="stat">
																			<c:set var="background">
																				<c:if test="${stat.index%2==0}">#FFFFFF</c:if>
																				<c:if test="${stat.index%2==1}">#d7eafd</c:if>
																			</c:set>
																			<tr bgcolor="${background}">
																				<td>${projCoordinator.contact.name}</td>
																				<td>${projCoordinator.contact.lastname}</td>
																				<td>${projCoordinator.contact.email}</td>
																				<td>${projCoordinator.contact.organisationName}</td>
																				<td>${projCoordinator.contact.phone}</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primaryProjCoordContIds" styleId="proj_${stat.index}" value="${projCoordinator.contact.temporaryId}" onchange="changePrimaryState('proj')"/>
																				</td>
																				<td>
																					<a href="javascript:editContact('PROJ_COORDINATOR_CONT',${projCoordinator.contact.temporaryId})"><img src="/repository/message/view/images/edit.gif" border="0" /></a>
																					<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams2}" property="tempId">
																						<bean:write name="projCoordinator" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams2}" property="contType" value="PROJ_COORDINATOR_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams2"><img src="/repository/message/view/images/trash_12.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff"><html:button property="submitButton" styleClass="dr-menu" onclick="AddContactButton('PROJ_COORDINATOR_CONT')">Add Contact</html:button></td>
																	</tr>
																</table>												
															</td>
														</tr>
														<tr style="height:20px"><td/></tr>
													</feature:display>
													<feature:display name="Sector Ministry Contact Information" module="Contact Information">
														<tr>
															<td>
																<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
																<a title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>Sector Ministry Contact Information</digi:trn></b>
																</a>	
															</td>
														</tr>
														<tr>
															<td>
																<table style="width: 100%;height: 100%;vertical-align: top;">
																	<c:if test="${not empty aimEditActivityForm.contactInformation.sectorMinistryContacts}">
																		<tr bgcolor="#cccccc"">
																			<td><strong><digi:trn>Firstname</digi:trn></strong></td>
																			<td><strong><digi:trn>Lastname</digi:trn></strong></td>
																			<td><strong><digi:trn>Email</digi:trn></strong></td>
																			<td><strong><digi:trn>Organisation</digi:trn></strong></td>
																			<td><strong><digi:trn>Phone</digi:trn></strong></td>
																			<td><strong><digi:trn>Primary</digi:trn></strong></td>
																			<td><strong><digi:trn>Actions</digi:trn></strong></td>
																		</tr>
																		<c:forEach var="sectorMinistry" items="${aimEditActivityForm.contactInformation.sectorMinistryContacts}" varStatus="stat">
																			<c:set var="background">
																				<c:if test="${stat.index%2==0}">#FFFFFF</c:if>
																				<c:if test="${stat.index%2==1}">#d7eafd</c:if>
																			</c:set>
																			<tr bgcolor="${background}">
																				<td>${sectorMinistry.contact.name}</td>
																				<td>${sectorMinistry.contact.lastname}</td>
																				<td>${sectorMinistry.contact.email}</td>
																				<td>${sectorMinistry.contact.organisationName}</td>
																				<td>${sectorMinistry.contact.phone}</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primarySecMinContIds" styleId="secMin_${stat.index}" value="${sectorMinistry.contact.temporaryId}" onchange="changePrimaryState('secMin')"/>
																				</td>
																				<td>
																					<a href="javascript:editContact('SECTOR_MINISTRY_CONT',${sectorMinistry.contact.temporaryId})"><img src="/repository/message/view/images/edit.gif" border="0" /></a>
																					<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams3}" property="tempId">
																						<bean:write name="sectorMinistry" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams3}" property="contType" value="SECTOR_MINISTRY_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams3"><img src="/repository/message/view/images/trash_12.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff"><html:button property="submitButton" styleClass="dr-menu" onclick="AddContactButton('SECTOR_MINISTRY_CONT')">Add Contact</html:button></td>
																	</tr>
																</table>												
															</td>
														</tr>
													</feature:display>													
													<tr><td bgColor="#f4f4f2">
														&nbsp;
													</td></tr>		
												</table>
												<!-- end contents -->
											</td>
										</tr>
									</table>
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
				<tr><td>
					&nbsp;
				</td></tr>
			</table>
		</td>
		<td width="10">&nbsp;</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>