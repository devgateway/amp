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
<%@ taglib uri="/taglib/aim"prefix="aim"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/addActivity.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/asynchronous.js"/>"></script>

<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<script type="text/javascript" src='<digi:file src="module/aim/scripts/panel/connection-min.js"/>' ></script>



<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/container.css'/>"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src="script/jquery.js"/>"></script>
<jsp:include page="/repository/aim/view/components/contactScripts.jsp" flush="true" />
	
	

<style type="text/css">
	
	#popin .content { 
	    overflow:auto; 
	    height:455px;
	    background-color:fff; 
	    padding:10px; 
	}  
</style>

<!--
<div id="popin" style="display: none">
	<div id="popinContent" class="content">
	</div>
</div>
<div id="contactPopin" style="display: none">
    <div id="popinContactContent" class="content">
    </div>
</div>
-->



<script type="text/javascript">
<!--


	function addLoadEvent(func) {
	  var oldonload = window.onload;
	  if (typeof window.onload != 'function') {
	    window.onload = func;
	  } else {
	    window.onload = function() {
	      if (oldonload) {
	        oldonload();
	      }
	      func();
	    }
	  }
	}



//    addLoadEvent(initScripts);



		YAHOO.namespace("YAHOO.amp");

		var myPanel = new YAHOO.widget.Panel("newpopins", {
			width:"600px",
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
	function initOrgScript() {
		var msg='\n<digi:trn>Add Organization(s)</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			panelStart=1;
		}); 
		
		myPanel.render(document.body);
                panelStrat=0;
	}

	var myPanelContact = new YAHOO.widget.Panel("newpopins2", { 
        x:250,
        y:100,
        minWidth:"400px",
        fixedcenter: false,
        constraintoviewport: false,
        underlay:"none",
        close:true,
        visible:false,
        modal:true,
        draggable:true,
        context: ["showbtn", "tl", "bl"]
    });
    var panelStartContact=0;
    var checkAndCloseContact=false;

      function initContactInfoScript() {
        var msg='\n<digi:trn >Add Contact Information</digi:trn>';
        myPanelContact.setHeader(msg);
        myPanelContact.setBody("");
        myPanelContact.beforeHideEvent.subscribe(function() {
            panelStartContact=1;
        });

        myPanelContact.render(document.body);
        panelStartContact=0;
    }
   
    //DO NOT REMOVE THIS FUNCTION --- AGAIN!!!!
    function mapCallBack(status, statusText, responseText, responseXML){
       window.location.reload();
    }
<%-- dublicate functions in contactScripts.jsp
      var responseSuccess1 = function(o){
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
        var content = document.getElementById("popinContactContent");
        //response = response.split("<!")[0];
        content.innerHTML = response;
        //content.style.visibility = "visible";
        showContactContent();
    }

    var responseFailure1 = function(o){
        alert("Connection Failure!");
    }
    
    var callback1 =
        {
        success:responseSuccess1,
        failure:responseFailure1
    };
--%>

    function refreshPage(){
        document.aimEditActivityForm.submit();
    }

    function showPanelLoading(msg){
		myPanel.setHeader(msg);		
		var content = document.getElementById("popinContent");
		content.innerHTML = '<div style="text-align: center">' + 
		'<img src="/TEMPLATE/ampTemplate/imagesSource/loaders/ajax-loader-darkblue.gif" border="0" height="17px"/>&nbsp;&nbsp;' + 
		'<digi:trn>Loading, please wait ...</digi:trn><br/><br/></div>';
		showContent();
	}

    function checkErrorAndClose(){
		if(checkAndClose==true){
			 if(document.getElementsByName("someError")[0]==null || document.getElementsByName("someError")[0].value=="false"){
             	var callbackFunction='';
                if(document.aimSelectOrganizationForm!=null&&document.aimSelectOrganizationForm.callbackFunction!=null)
                	callbackFunction=document.aimSelectOrganizationForm.callbackFunction.value;
                if( callbackFunction.trim()!=''){
                	eval(callbackFunction);
                }else{
                    refreshPage();
                }
                myclose();
             }
             checkAndClose=false;                    
		}
	}

    function addOrganizations2Contact(){
        <digi:context name="addCont" property="context/addAmpContactInfo.do?action=addOrganizations"/>;
        checkAndClose=true;
        var url="${addCont}";
        var parameters=getContactParams();
        YAHOO.util.Connect.asyncRequest("POST", url, callback1,parameters);
    }

    function removeContactOrgs(){
        var params=getContactParams();
        if(document.getElementsByName("selContactOrgs")!=null){
            var orgs = document.getElementsByName("selContactOrgs").length;
            for(var i=0; i<  orgs; i++){
                if(document.getElementsByName("selContactOrgs")[i].checked){
                    params+="&"+document.getElementsByName("selContactOrgs")[i].name+"="+document.getElementsByName("selContactOrgs")[i].value;
                }
            }
        }
        else{
            var msg="<digi:trn jsFriendly="true">Please select organization(s) to remove</digi:trn>"
            alert(msg);
        }
         <digi:context name="addCont" property="context/addAmpContactInfo.do?action=removeOrganizations"/>;
         var url="${addCont}";         
         YAHOO.util.Connect.asyncRequest("POST", url, callback1,params);

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

    function changePrimaryState(contactType){
        if(contactType=='donor'){
        	var donors= $("input[id^='donors_']");
        	var resetDonors=resetPrimary(donors);
        	if(resetDonors==true){
        		document.getElementById('don').value=true;
        	}else{
        		document.getElementById('don').value=false;
        	}    
        }else if(contactType=='mofed'){
        	var mofed= $("input[id^='mofed_']");
        	var resetMofed=resetPrimary(mofed);
        	if(resetMofed==true){
        		document.getElementById('mof').value=true;
        	}else{
        		document.getElementById('mof').value.value=false;
        	}
        }else if(contactType=='proj'){
        	var projCoord= $("input[id^='proj_']");
        	var resetProjCoord=resetPrimary(projCoord);
        	if(resetProjCoord==true){
        		document.getElementById('proj').value=true;
        	}else{
        		document.getElementById('proj').value=false;
        	}    
        }else if(contactType=='secMin'){
        	var secMin= $("input[id^='secMin_']");
        	var resetSecMin=resetPrimary(secMin);
        	if(resetSecMin==true){
        		document.getElementById('secMin').value=true;
        	}else{
        		document.getElementById('secMin').value=false;
        	}	
        }else if(contactType=='implExecuting'){
        	var impl= $("input[id^='implExecuting']");
        	var resetEmplEx=resetPrimary(impl);
        	if(resetEmplEx==true){
        		document.getElementById('implExecuting').value=true;
        	}else{
        		document.getElementById('implExecuting').value=false;
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

    function selectContact(params1) {
         YAHOO.util.Connect.asyncRequest("POST", params1, callback1);
     }

//    addLoadEvent(delBody);
	-->

</script>
 <jsp:include page="/repository/aim/view/addOrganizationPopin.jsp" flush="true" />

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
<%-- <html:hidden name="aimEditActivityForm" styleId="keywordSearch"  property="contactInformation.keyword"/>--%>
<html:hidden styleId="don" value="${aimEditActivityForm.contactInformation.resetDonorIds}" property="contactInformation.resetDonorIds"/>
<html:hidden styleId="mof" property="contactInformation.resetMofedIds" value="${aimEditActivityForm.contactInformation.resetMofedIds}"/>
<html:hidden styleId="proj" property="contactInformation.resetProjCoordIds" value="${aimEditActivityForm.contactInformation.resetProjCoordIds}"/>
<html:hidden styleId="secMin" property="contactInformation.resetSecMinIds" value="${aimEditActivityForm.contactInformation.resetSecMinIds}"/>
<html:hidden styleId="implExecuting" property="contactInformation.resetImplExecutingIds" value="${aimEditActivityForm.contactInformation.resetImplExecutingIds}"/>


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

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" vAlign="top" align="center" border=0>
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left vAlign=top class=r-dotted-lg>
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
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">





<!-- contents -->
												<table width="100%" bgcolor="#f4f4f2">													
													
													<feature:display name="Donor Contact Information" module="Contact Information">
														<tr>
															<td class="separator1" title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>Donor Contact Information</digi:trn></b>
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
																				<td>
																					<c:forEach var="email" items="${donorContact.contact.properties}">
																						<c:if test="${email.name=='contact email'}">
																							<div>${email.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td>
																					 <ul>
                                                                                        <c:if test="${not empty donorContact.contact.organisationName}">
                                                                                     		<li>${donorContact.contact.organisationName}</li>
                                                                                        </c:if>
                                                                                        <c:if test="${not empty donorContact.contact.organizationContacts}">
                                                                                        	<c:forEach var="orgCont" items="${donorContact.contact.organizationContacts}">
	                                                                                        	<li>${orgCont.organisation.name}</li>
	                                                                                        </c:forEach>
                                                                                        </c:if>
                                                                                     </ul>
																				</td>
																				<td>
																					<c:forEach var="phone" items="${donorContact.contact.properties}">
																						<c:if test="${phone.name=='contact phone'}">
																							<div>${phone.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primaryDonorContIds" styleId="donors_${stat.index}" value="${donorContact.contact.temporaryId}" onchange="changePrimaryState('donor')"/>																					
																				</td>
																				<td>	
																					<c:set var="ampContactId">
		                                            									<c:choose>
		                                                									<c:when test="${empty donorContact.contact.id||donorContact.contact.id==0}">
		                                                    									${donorContact.contact.temporaryId}
		                                                									</c:when>
		                                                									<c:otherwise>
		                                                    									${donorContact.contact.id}
		                                                									</c:otherwise>
		                                            									</c:choose>
		                                        									 </c:set>
																					 <aim:editContactLink collection="donorContacts" form="${aimEditActivityForm.contactInformation}" contactId="${ampContactId}" contactType="${donorContact.contactType}" addOrgBtn="visible">
						                                            					<img alt="edit" src= "/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0"/>
						                                        					</aim:editContactLink>
						                                        					<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams}" property="tempId">
																						<bean:write name="donorContact" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams}" property="contType" value="DONOR_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams"><img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>																		
																		<td colspan="7" bgcolor="#ffffff">
																			<aim:addContactButton collection="donorContacts" form="${aimEditActivityForm.contactInformation}" contactType="DONOR_CONT" addOrgBtn="visible"><digi:trn>Add contact</digi:trn></aim:addContactButton>						
																		</td>
																	</tr>																	
																</table>												
															</td>
														</tr>
														<tr style="height:20px"><td/></tr>
													</feature:display>													
													<feature:display name="Government Contact Information" module="Contact Information">
														<tr>
															<td class="separator1" title="<digi:trn>The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>MOFED Contact Information</digi:trn></b>
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
																				<td>
																					<c:forEach var="email" items="${mofedContact.contact.properties}">
																						<c:if test="${email.name=='contact email'}">
																							<div>${email.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td>
																					<ul>
                                                                                        <c:if test="${not empty mofedContact.contact.organisationName}">
                                                                                     		<li>${mofedContact.contact.organisationName}</li>
                                                                                        </c:if>
                                                                                        <c:if test="${not empty mofedContact.contact.organizationContacts}">
                                                                                        	<c:forEach var="orgCont" items="${mofedContact.contact.organizationContacts}">
	                                                                                        	<li>${orgCont.organisation.name}</li>
	                                                                                        </c:forEach>
                                                                                        </c:if>
                                                                                     </ul>
                                                                                </td>
																				<td>
																					<c:forEach var="phone" items="${mofedContact.contact.properties}">
																						<c:if test="${phone.name=='contact phone'}">
																							<div>${phone.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primaryMofedContIds" styleId="mofed_${stat.index}" value="${mofedContact.contact.temporaryId}" onchange="changePrimaryState('mofed')"/>
																				</td>
																				<td>
																					<c:set var="ampContactId">
		                                            									<c:choose>
		                                                									<c:when test="${empty mofedContact.contact.id||mofedContact.contact.id==0}">
		                                                    									${mofedContact.contact.temporaryId}
		                                                									</c:when>
		                                                									<c:otherwise>
		                                                    									${mofedContact.contact.id}
		                                                									</c:otherwise>
		                                            									</c:choose>
		                                        									 </c:set>
																					 <aim:editContactLink collection="mofedContacts" form="${aimEditActivityForm.contactInformation}" contactId="${ampContactId}" contactType="${mofedContact.contactType}" addOrgBtn="visible">
						                                            					<img alt="edit" src= "/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0"/>
						                                        					</aim:editContactLink>
						                                        					<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams1}" property="tempId">
																						<bean:write name="mofedContact" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams1}" property="contType" value="MOFED_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams1"><img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff">
																			<aim:addContactButton collection="mofedContacts" form="${aimEditActivityForm.contactInformation}" contactType="MOFED_CONT" addOrgBtn="visible"><digi:trn>Add contact</digi:trn></aim:addContactButton>
																		</td>
																	</tr>
																</table>												
															</td>
														</tr>
														<tr style="height:20px"><td/></tr>
													</feature:display>
													<feature:display name="Project Coordinator Contact Information" module="Contact Information">
														<tr>
															<td class="separator1" title="<digi:trn>The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>Project Coordinator Contact Information</digi:trn></b>
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
																				<td>
																					<c:forEach var="email" items="${projCoordinator.contact.properties}">
																						<c:if test="${email.name=='contact email'}">
																							<div>${email.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td>
																					<ul>
                                                                                        <c:if test="${not empty projCoordinator.contact.organisationName}">
                                                                                     		<li>${projCoordinator.contact.organisationName}</li>
                                                                                        </c:if>
                                                                                        <c:if test="${not empty projCoordinator.contact.organizationContacts}">
                                                                                        	<c:forEach var="orgCont" items="${projCoordinator.contact.organizationContacts}">
	                                                                                        	<li>${orgCont.organisation.name}</li>
	                                                                                        </c:forEach>
                                                                                        </c:if>
                                                                                     </ul>
																				</td>
																				<td>
																					<c:forEach var="phone" items="${projCoordinator.contact.properties}">
																						<c:if test="${phone.name=='contact phone'}">
																							<div>${phone.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primaryProjCoordContIds" styleId="proj_${stat.index}" value="${projCoordinator.contact.temporaryId}" onchange="changePrimaryState('proj')"/>
																				</td>
																				<td>
																					<c:set var="ampContactId">
		                                            									<c:choose>
		                                                									<c:when test="${empty projCoordinator.contact.id||projCoordinator.contact.id==0}">
		                                                    									${projCoordinator.contact.temporaryId}
		                                                									</c:when>
		                                                									<c:otherwise>
		                                                    									${projCoordinator.contact.id}
		                                                									</c:otherwise>
		                                            									</c:choose>
		                                        									 </c:set>
																					 <aim:editContactLink collection="projCoordinatorContacts" form="${aimEditActivityForm.contactInformation}" contactId="${ampContactId}" contactType="${projCoordinator.contactType}" addOrgBtn="visible">
						                                            					<img alt="edit" src= "/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0"/>
						                                        					</aim:editContactLink>
						                                        					<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams2}" property="tempId">
																						<bean:write name="projCoordinator" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams2}" property="contType" value="PROJ_COORDINATOR_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams2"><img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff">
																			<aim:addContactButton collection="projCoordinatorContacts" form="${aimEditActivityForm.contactInformation}" contactType="PROJ_COORDINATOR_CONT" addOrgBtn="visible"><digi:trn>Add contact</digi:trn></aim:addContactButton>				
																		</td>
																	</tr>
																</table>												
															</td>
														</tr>
														<tr style="height:20px"><td/></tr>
													</feature:display>
													<feature:display name="Sector Ministry Contact Information" module="Contact Information">
														<tr>
															<td class="separator1" title="<digi:trn>The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>Sector Ministry Contact Information</digi:trn></b>
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
																				<td>
																					<c:forEach var="email" items="${sectorMinistry.contact.properties}">
																						<c:if test="${email.name=='contact email'}">
																							<div>${email.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td>
																					<ul>
                                                                                        <c:if test="${not empty sectorMinistry.contact.organisationName}">
                                                                                     		<li>${sectorMinistry.contact.organisationName}</li>
                                                                                        </c:if>
                                                                                        <c:if test="${not empty sectorMinistry.contact.organizationContacts}">
                                                                                        	<c:forEach var="orgCont" items="${sectorMinistry.contact.organizationContacts}">
	                                                                                        	<li>${orgCont.organisation.name}</li>
	                                                                                        </c:forEach>
                                                                                        </c:if>
                                                                                     </ul>
																				</td>
																				<td>
																					<c:forEach var="phone" items="${sectorMinistry.contact.properties}">
																						<c:if test="${phone.name=='contact phone'}">
																							<div>${phone.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primarySecMinContIds" styleId="secMin_${stat.index}" value="${sectorMinistry.contact.temporaryId}" onchange="changePrimaryState('secMin')"/>
																				</td>
																				<td>
																					<c:set var="ampContactId">
		                                            									<c:choose>
		                                                									<c:when test="${empty sectorMinistry.contact.id||sectorMinistry.contact.id==0}">
		                                                    									${sectorMinistry.contact.temporaryId}
		                                                									</c:when>
		                                                									<c:otherwise>
		                                                    									${sectorMinistry.contact.id}
		                                                									</c:otherwise>
		                                            									</c:choose>
		                                        									 </c:set>
																					 <aim:editContactLink collection="sectorMinistryContacts" form="${aimEditActivityForm.contactInformation}" contactId="${ampContactId}" contactType="${sectorMinistry.contactType}" addOrgBtn="visible">
						                                            					<img alt="edit" src= "/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0"/>
						                                        					</aim:editContactLink>
																					<jsp:useBean id="urlParams3" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams3}" property="tempId">
																						<bean:write name="sectorMinistry" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams3}" property="contType" value="SECTOR_MINISTRY_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams3"><img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff">
																			<aim:addContactButton collection="sectorMinistryContacts" form="${aimEditActivityForm.contactInformation}" contactType="SECTOR_MINISTRY_CONT" addOrgBtn="visible"><digi:trn>Add contact</digi:trn></aim:addContactButton>																			
																		</td>
																	</tr>																	
																</table>
															</td>
														</tr>
														<tr style="height:20px"><td/></tr>
													</feature:display>
													<!-- Implementing/Executing agencies -->
													<feature:display name="Implementing/Executing Agency Contact Information" module="Contact Information">
														<tr>
															<td class="separator1" title="<digi:trn>The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
																<b><digi:trn>Implementing/Executing Agency Contact Information</digi:trn></b>
															</td>
														</tr>
														<tr>
															<td>
																<table style="width: 100%;height: 100%;vertical-align: top;">
																	<c:if test="${not empty aimEditActivityForm.contactInformation.implExecutingAgencyContacts}">
																		<tr bgcolor="#cccccc"">
																			<td><strong><digi:trn>Firstname</digi:trn></strong></td>
																			<td><strong><digi:trn>Lastname</digi:trn></strong></td>
																			<td><strong><digi:trn>Email</digi:trn></strong></td>
																			<td><strong><digi:trn>Organisation</digi:trn></strong></td>
																			<td><strong><digi:trn>Phone</digi:trn></strong></td>
																			<td><strong><digi:trn>Primary</digi:trn></strong></td>
																			<td><strong><digi:trn>Actions</digi:trn></strong></td>
																		</tr>
																		<c:forEach var="implExecAgency" items="${aimEditActivityForm.contactInformation.implExecutingAgencyContacts}" varStatus="stat">
																			<c:set var="background">
																				<c:if test="${stat.index%2==0}">#FFFFFF</c:if>
																				<c:if test="${stat.index%2==1}">#d7eafd</c:if>
																			</c:set>
																			<tr bgcolor="${background}">
																				<td>${implExecAgency.contact.name}</td>
																				<td>${implExecAgency.contact.lastname}</td>
																				<td>
																					<c:forEach var="email" items="${implExecAgency.contact.properties}">
																						<c:if test="${email.name=='contact email'}">
																							<div>${email.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td>
																					<ul>
                                                                                        <c:if test="${not empty implExecAgency.contact.organisationName}">
                                                                                     		<li>${implExecAgency.contact.organisationName}</li>
                                                                                        </c:if>
                                                                                        <c:if test="${not empty implExecAgency.contact.organizationContacts}">
                                                                                        	<c:forEach var="orgCont" items="${implExecAgency.contact.organizationContacts}">
	                                                                                        	<li>${orgCont.organisation.name}</li>
	                                                                                        </c:forEach>
                                                                                        </c:if>
                                                                                     </ul>
																				</td>
																				<td>
																					<c:forEach var="phone" items="${implExecAgency.contact.properties}">
																						<c:if test="${phone.name=='contact phone'}">
																							<div>${phone.value}</div>
																						</c:if>
																					</c:forEach>
																				</td>
																				<td align="left">
																					<html:multibox name="aimEditActivityForm" property="contactInformation.primaryImplExecutingContIds" styleId="implExecuting_${stat.index}" value="${implExecAgency.contact.temporaryId}" onchange="changePrimaryState('implExecuting')"/>
																				</td>
																				<td>
																					<c:set var="ampContactId">
		                                            									<c:choose>
		                                                									<c:when test="${empty implExecAgency.contact.id||implExecAgency.contact.id==0}">
		                                                    									${implExecAgency.contact.temporaryId}
		                                                									</c:when>
		                                                									<c:otherwise>
		                                                    									${implExecAgency.contact.id}
		                                                									</c:otherwise>
		                                            									</c:choose>
		                                        									 </c:set>
																					 <aim:editContactLink collection="implExecutingAgencyContacts" form="${aimEditActivityForm.contactInformation}" contactId="${ampContactId}" contactType="${implExecAgency.contactType}" addOrgBtn="visible">
						                                            					<img alt="edit" src= "/TEMPLATE/ampTemplate/imagesSource/common/application_edit.png" border="0"/>
						                                        					</aim:editContactLink>
						                                        					<jsp:useBean id="urlParams4" type="java.util.Map" class="java.util.HashMap"/>
																					<c:set target="${urlParams4}" property="tempId">
																						<bean:write name="implExecAgency" property="contact.temporaryId"/>
																					</c:set>
																					<c:set target="${urlParams4}" property="contType" value="IMPL_EXEC_AGENCY_CONT"/>
																					<digi:link href="/activityContactInfo.do?toDo=delete" name="urlParams4"><img src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0" /></digi:link>
																				</td>
																			</tr>
																		</c:forEach>
																	</c:if>
																	<tr>
																		<td colspan="7" bgcolor="#ffffff">
																			<aim:addContactButton collection="implExecutingAgencyContacts" form="${aimEditActivityForm.contactInformation}" contactType="IMPL_EXEC_AGENCY_CONT" addOrgBtn="visible"><digi:trn>Add contact</digi:trn></aim:addContactButton>			
																		</td>
																	</tr>
																</table>												
															</td>
														</tr>
													</feature:display>													
													<tr><td bgColor="#f4f4f2">&nbsp;
														
													</td></tr>		
												</table>
												<!-- end contents -->


















								
								
								<%--
								
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2">
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<a title="<digi:trn key="aim:DetailsofContactPerson">The first name, last name and e-mail of the person in charge of the project at the funding agency</digi:trn>">
										<b><digi:trn key="aim:contactInformation">Contact Information</digi:trn></b>
										</a>
									</td></tr>
									<tr><td>
										<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
											<tr>
												<td width="50%" valign="top">
													<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
														<tr>
															<td width="20%"></td>
															<td width="15%" align="right"></td>
															<td width="65%"></td>
														</tr>														
														<feature:display name="Donor Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:donorFundingContactInformation">
																Donor funding contact information</digi:trn>
															</td>
															<field:display name="Donor First Name" feature="Donor Contact Information">
															<td align="right">
																<b>
																<digi:trn key="aim:firstName">
																	First Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntFirstName" styleClass="inp-text"/>
															</td>
															</field:display>
														</tr>
														<field:display name="Donor Last Name" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:lastName">
																	Last Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Email" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:email">
																	Email
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Title" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:title">
																	Title
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Organization" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:organization">
																	Organization
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Phone Number" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:phoneNumber">
																	Phone Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntPhoneNumber" styleClass="inp-text" styleId="donorPhone" onkeyup="checkNumber('donorPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Donor Fax Number" feature="Donor Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:faxNumber">
																	Fax Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.dnrCntFaxNumber" styleClass="inp-text" styleId="dnrCntFax" onkeyup="checkNumber('dnrCntFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
														<tr>
															<td>&nbsp;</td><td></td><td></td>
														</tr>
														<feature:display name="Government Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:mofedContactInformation">
																MOFED contact information</digi:trn>
															</td>
															<field:display name="Government First Name" feature="Government Contact Information">
															<td align="right">
																<b>
																<digi:trn key="aim:firstName">
																	First Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntFirstName" styleClass="inp-text"/>
															</td>
															</field:display>
														</tr>
														<field:display name="Government Last Name" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:lastName">
																	Last Name
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Email" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:email">
																	Email
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Title" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:title">
																	Title
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Organization" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:organization">
																	Organization
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Phone Number" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:phoneNumber">
																	Phone Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntPhoneNumber" styleClass="inp-text" styleId="mofedPhone" onkeyup="checkNumber('mofedPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Government Fax Number" feature="Government Contact Information">
														<tr>
															<td>
															</td>
															<td align="right">
																<b>
																<digi:trn key="aim:faxNumber">
																	Fax Number
																</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.mfdCntFaxNumber" styleClass="inp-text" styleId="mfdCntFax" onkeyup="checkNumber('mfdCntFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
													</table>
												</td>
												<td width="50%" valign="top">
													<table width="100%" cellSpacing=2 cellPadding=2 vAlign=top align=left class="box-border-nopadding" border=0>
														<tr>
															<td width="20%"></td>
															<td width="15%" align="right"></td>
															<td width="65%"></td>
														</tr>
														<feature:display name="Project Coordinator Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:projectCoordinator">Project Coordinator Contact Information</digi:trn>
															</td>
															<field:display name="Project Coordinator First Name" feature="Project Coordinator Contact Information">
																<td align="right">
																	<b><digi:trn key="aim:projectCoordinator:firstName">First Name</digi:trn></b>
																</td>
																<td>
																	<html:text property="contactInfo.prjCoFirstName" styleClass="inp-text"/>
																</td>
															</field:display>
														</tr>
														<field:display name="Project Coordinator Last Name" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:lastName">Last Name</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Email" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:email">Email</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Title" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:title">Title</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Organization" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:organization">Organization</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Phone Number" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:phoneNumber">Phone Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoPhoneNumber" styleClass="inp-text" styleId="prjCoPhone" onkeyup="checkNumber('prjCoPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Project Coordinator Fax Number" feature="Project Coordinator Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:projectCoordinator:faxNumber">Fax Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.prjCoFaxNumber" styleClass="inp-text" styleId="prjCoFax" onkeyup="checkNumber('prjCoFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
														<tr>
															<td>&nbsp;</td><td></td><td></td>
														</tr>
														<feature:display name="Sector Ministry Contact Information" module="Contact Information">
														<tr>
															<td align="right">
																<digi:trn key="aim:sectorMinistryCnt">Sector Ministry Contact Information</digi:trn>
															</td>
															<field:display name="Sector Ministry Contact First Name" feature="Sector Ministry Contact Information">
																<td align="right">
																	<b><digi:trn key="aim:sectorMinistryCnt:firstName">First Name</digi:trn></b>
																</td>
																<td>
																	<html:text property="contactInfo.secMiCntFirstName" styleClass="inp-text"/>
																</td>
															</field:display>
														</tr>
														<field:display name="Sector Ministry Contact Last Name" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:lastName">Last Name</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntLastName" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Email" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:email">Email</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntEmail" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Title" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:title">Title</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntTitle" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Organization" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:organization">Organization</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntOrganization" styleClass="inp-text"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Phone Number" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:phoneNumber">Phone Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntPhoneNumber" styleClass="inp-text" styleId="MiCntPhone" onkeyup="checkNumber('MiCntPhone')"/>
															</td>
														</tr>
														</field:display>
														<field:display name="Sector Ministry Contact Fax Number" feature="Sector Ministry Contact Information">
														<tr>
															<td></td>
															<td align="right">
																<b><digi:trn key="aim:sectorMinistryCnt:faxNumber">Fax Number</digi:trn></b>
															</td>
															<td>
																<html:text property="contactInfo.secMiCntFaxNumber" styleClass="inp-text" styleId="MiCntFax" onkeyup="checkNumber('MiCntFax')"/>
															</td>
														</tr>
														</field:display>
														</feature:display>
													</table>
												</td>
											</tr>											
										</table>
									</td></tr>
									<tr><td bgColor=#f4f4f2>
										&nbsp;
									</td></tr>

								</table>
								<!-- end contents -->
								
								--%>
								
								
								
								
							</td></tr>
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

<script language="javascript">
	
			initContactScript();
			initContactInfoScript();
	</script>