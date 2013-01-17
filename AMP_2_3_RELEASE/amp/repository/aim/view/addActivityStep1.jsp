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

<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop-min.js'/>" >.</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>" >.</script>
<script type="text/javascript" src="<digi:file src="script/ajaxconnection/connection-min.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>
<!-- Stylesheet of AMP -->
        <digi:ref href="css/new_styles.css" type="text/css" rel="stylesheet" />
<!--  -->

<script type="text/javascript">
<!--

		YAHOO.namespace("YAHOO.amp");

		var myPanel = new YAHOO.widget.Panel("newmyComment", {
			width:"600px",
			fixedcenter: true,
		    constraintoviewport: true,
		    underlay:"none",
		    close:true,
		    visible:false,
		    modal:true,
		    draggable:true
		    });
	
	function initStep1Scripts() {
		var msg='\n<digi:trn key="aim:addeditComment">Add/Edit Comment</digi:trn>';
		myPanel.setHeader(msg);
		myPanel.setBody("");
		myPanel.beforeHideEvent.subscribe(function() {
			delCommentContent=true;
			showContent();
		}); 
		myPanel.render(document.body);
	}
	//this is called from editActivityMenu.jsp
	//window.onload=initStep1Scripts;
-->	
</script>
<style type="text/css">
	.mask {
	  -moz-opacity: 0.8;
	  opacity:.80;
	  filter: alpha(opacity=80);
	  background-color:#2f2f2f;
	}
	
	#myComment .content { 
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
		showContent(response);
	}
	var delCommentContent=false;	 
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

	var specialResponseSuccess = function(o){
		myclose();
   	}
	var specialCallback =
	{
			success:specialResponseSuccess, 
			failure:responseFailure 
	}
	
	function commentWin(commentwinHeader,commentId){
		delCommentContent=false;
		<digi:context name="commentUrl" property="context/module/moduleinstance/viewComment.do" />
		var url = "<%=commentUrl %>?comment=" + commentId + "&edit=" + "true";
		myPanel.setHeader('\n' + commentwinHeader); //set header to popin
		YAHOOAmp.util.Connect.asyncRequest("POST", url, callback);
	}

	function showContent(content){
		if(delCommentContent==true){
			myPanel.setBody("");
		}else{
			showComment(content);
		}
	}
	function showComment(content) {
		myPanel.setBody(content);
		myPanel.show();
	}	
	function saveComment(){
		var postString		= generateFields("");
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/viewComment.do", specialCallback, postString);
	}
	 function editDelete() {
		var postString		= generateFields("edit=true");
		YAHOOAmp.util.Connect.asyncRequest("POST", "/aim/viewComment.do", callback, postString);
	}

	 
	function message(val1,val2) {
		var flag = true;
		val2 = val2 - 1;
		<digi:context name="url" property="context/module/moduleinstance/viewComment.do?edit=true" />
		if (val1 == "delete") {
			if(!confirm("Are you sure about deleting this comment?"))
				flag = false;
		}
		if (flag == true) {
			document.getElementById('actionFlag').value = val1;
			document.getElementById('ampCommentId').value = val2;
			editDelete();
		}
	}
	function generateFields(action){
		var ret;
		ret=action+"&comments.actionFlag="+document.getElementById('actionFlag').value+"&comments.ampCommentId="+document.getElementById('ampCommentId').value+"&comments.commentText="+document.getElementById('commentText').value;
		return ret;
	}
	function myclear(){
		document.getElementById('commentText').value="";
	}
	function myclose(){
		myPanel.hide();	
	}
	function mycheck() {
		var str = document.getElementById('commentText').value;
		str = trim(str);
		document.getElementById('commentText').value = str;
		if (str.length == 0 || str == null) {
			alert("Please enter your comment.");
			document.getElementById('commentText').focus();
			return false;
		}
		else{
			saveComment();
		}
		myclose();
	}
	
	function trim ( inputStringTrim ) {
		fixedTrim = "";
		lastCh = " ";
		for (x=0; x < inputStringTrim.length; x++) {
			ch = inputStringTrim.charAt(x);
			if ((ch != " ") || (lastCh != " ")) { fixedTrim += ch; }
				lastCh = ch;
		}
		if (fixedTrim.charAt(fixedTrim.length - 1) == " ") {
			fixedTrim = fixedTrim.substring(0, fixedTrim.length - 1); }
		return fixedTrim;
	}
	
-->
</script>
<script language="JavaScript">
<!--
	
function checkSelOrgs() {
	<c:set var="translation">
		<digi:trn key="aim:chooseOrganizationToRemove">Please choose an organization to remove</digi:trn>
	</c:set>
	
	if (document.getElementsByName('identification.selOrgs').checked != null) { // only one org. added
		if (document.getElementsByName('identification.selOrgs').checked == false) {
			alert("${translation}");
			return false;
		}
	} else { // many org. present
		var length = document.getElementsByName('identification.selOrgs').length;
		var flag = 0;
		for (i = 0;i < length;i ++) {
			if (document.getElementsByName('identification.selOrgs')[i].checked == true) {
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

function selectOrganisation() {
	openNewWindow(600, 400);
	<digi:context name="selectOrganization" property="context/module/moduleinstance/selectOrganization.do?orgSelReset=true&edit=true&step=1" />
	document.aimEditActivityForm.action = "<%=selectOrganization%>";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function edit(key,fieldName) {
    <digi:context name="nextSetp" property="context/module/moduleinstance/addActivity.do"/>
    document.aimEditActivityForm.action = "<%= nextSetp %>?fieldName="+fieldName;
    document.aimEditActivityForm.target = "_self"

  document.aimEditActivityForm.editKey.value = key;
  document.aimEditActivityForm.step.value = "1.1";
  document.aimEditActivityForm.submit();

}


function removeSelOrganisations() {
	var flag = checkSelOrgs();
	if (flag == false) return false;
	<digi:context name="remOrgs" property="context/module/moduleinstance/removeSelOrganisations.do?edit=true" />
	document.aimEditActivityForm.action = "<%= remOrgs %>";
	document.aimEditActivityForm.target = "_self"
	document.aimEditActivityForm.submit();
	return true;
}

function goNext() {
    if(!validateForm()){
      return false;
    }
    <digi:context name="nextSetp" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= nextSetp %>";
    document.aimEditActivityForm.target = "_self"
    document.aimEditActivityForm.submit();
    return true;
}

function resetAll(){
	<digi:context name="resetAll" property="context/module/moduleinstance/resetAll.do?edit=true" />
	document.aimEditActivityForm.action = "<%= resetAll %>";
	document.aimEditActivityForm.target = "_self";
	document.aimEditActivityForm.submit();
	return true;
}

function validateForm() {
  /*<field:display name="Project Title" feature="Identification">
  if (trim(document.aimEditActivityForm.title.value) == "") {
    <c:set var="translation">
    <digi:trn key="aim:pleaseEnterTitle">Please enter title</digi:trn>
    </c:set>
    alert("${translation}");
    document.aimEditActivityForm.title.focus();
    return false;
  }
  </field:display>
  
  var stId=document.getElementsByName("statusId");
  var draftStatus=document.getElementById("draftFlag");
  if(draftStatus!=null && draftStatus.value!="true"){
  	<field:display name="Status" feature="Planning">
    if(stId==null || stId[0]==null || stId[0].value==0){
      <c:set var="message">
      <digi:trn key="aim:pleaseSelectStatus">Please select status!</digi:trn>
      </c:set>
      alert("${message}");
      return false;
    }
    </field:display>
  }*/
  /*	if (document.aimEditActivityForm.status.value == "-1") {
    alert("Please select status");
    document.aimEditActivityForm.status.focus();
    return false;
  }*/
  document.aimEditActivityForm.step.value="2";
  <digi:context name="commentUrl" property="context/module/moduleinstance/addActivity.do?edit=true" />
  url = "<%=commentUrl %>?comment=" + "ccd" + "&edit=" + "true";
  document.aimEditActivityForm.action = url;
  return true;
}

function goNextStep(){
  if(validateForm()){
    <digi:context name="nextStepUrl" property="context/module/moduleinstance/addActivity.do?edit=true" />
    document.aimEditActivityForm.action = "<%= nextStepUrl %>";
    document.aimEditActivityForm.target = "_self";
    document.aimEditActivityForm.submit();
  }
}

function reviseCloseDate() {
	openNewWindow(600, 150);
	<digi:context name="rev" property="context/module/moduleinstance/reviseCompDate.do?edit=true" />
	document.aimEditActivityForm.action = "<%= rev %>";
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();
}

function popupwin() {
	var statId=document.getElementsByName('identification.statusId')[0].value;
	openNewWindow(600, 550);	
	document.aimEditActivityForm.action = "/getSatusDescription.do?catEditKey="+statId;
	document.aimEditActivityForm.target = popupPointer.name;
	document.aimEditActivityForm.submit();	
}



-->
</script>

<jsp:include page="scripts/newCalendar.jsp"  />

<digi:instance property="aimEditActivityForm" />

<digi:form action="/addActivity.do" method="post">

<html:hidden property="step"/>
<html:hidden property="editKey"/>
<html:hidden property="editAct"/>

<input type="hidden" name="edit" value="true">

<input type="hidden" name="selectedDate" value="">
<html:hidden property="reset" />
<table width="100%" cellPadding="0" cellSpacing="0" vAlign="top" align="left" border="0">
<tr><td width="100%" vAlign="top" align="left">
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%" vAlign="top" align="center" border="0">
	<tr>
		<td class=r-dotted-lg width="10">&nbsp;</td>
		<td align=left valign="top" class=r-dotted-lg>
			<table width="98%" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td><jsp:include page="t.jsp"/>
								<span class=crumb>
								
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
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivityStep1">Edit Activity - Step 1</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addActivityStep1">Add Activity - Step 1</digi:trn>
								</c:if>
								</span>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr><td>
					<table width="100%" cellSpacing="1" cellPadding="1" vAlign="top">
						<tr>
							<td height=16 valign="center" width="100%"><span class=subtitle-blue>
								<c:if test="${aimEditActivityForm.editAct == false}">
									<digi:trn key="aim:addNewActivity">Add New Activity</digi:trn>
								</c:if>
								<c:if test="${aimEditActivityForm.editAct == true}">
									<digi:trn key="aim:editActivity">Edit Activity</digi:trn>
:
										<bean:write name="aimEditActivityForm" property="identification.title"/>
								</c:if>
							</td>
						</tr>
					</table>
				</td></tr>
				<tr> <td>
					<digi:errors/>
				</td></tr>
			    <logic:iterate id="element" name="aimEditActivityForm" property="messages">
                    <tr><td>
                        <digi:trn key="${element.key}">
                            <font color="#FF0000"><bean:write name="element" property="value"/></font>
                        </digi:trn>
                    </td></tr>
                </logic:iterate>
				<tr><td>
					<table width="100%" cellSpacing="5" cellPadding="3" vAlign="top" border="0">
						<tr><td width="75%" vAlign="top">
						<table cellpadding="0" cellspacing="0" width="100%" border="0">
							<tr>
								<td width="100%">
									<table cellpadding="0" cellspacing="0" width="100%" border="0">
										<tr>
											<td width="13" height="20" background="module/aim/images/left-side.gif">
											</td>
											<td vAlign="center" align ="center" class="textalb" height="20" bgcolor="#006699">
												 <digi:trn>Step</digi:trn> 1 <digi:trn>of  </digi:trn> ${fn:length(aimEditActivityForm.steps)}:
                                                                                                           <digi:trn key="IdentificationAndPlanning"> 
                                                                                                               Identification | Planning </digi:trn>
											</td>
											<td width="13" height="20" background="module/aim/images/right-side.gif">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgcolor="#f4f4f2" width="100%">
							<table width="100%" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#006699">
							<tr><td bgColor=#f4f4f2 align="center" vAlign="top">
								<!-- contents -->

								<table width="95%" bgcolor="#f4f4f2" border="0">
									<feature:display name="Identification" module="Project ID and Planning">
									&nbsp;
									</feature:display>
									<tr><td>
										<IMG alt=Link height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<b><digi:trn key="aim:identification">Identification</digi:trn></b>

									</td></tr>
									<tr><td>&nbsp;</td></tr>
									
									<tr>
										<td>
											<table width="100%" bgcolor="#FFFFFF" cellPadding=5 cellspacing="1">
												<jsp:include page="addActivityStep1Identification.jsp"/>
												<feature:display name="Identification" module="Project ID and Planning">
													<field:display name="Organizations and Project ID" feature="Identification">
														<jsp:include page="addActivityStep1OrgAndProjects.jsp"/>
													</field:display>
												</feature:display>
											</table>
										</td>
									</tr>
									

									<tr><td>&nbsp;
										
									</td></tr>
									<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>
									<feature:display name="Planning" module="Project ID and Planning">
										<jsp:include page="addActivityStep1Planning.jsp"/>
									</feature:display>
										
									<logic:notEmpty name="aimEditActivityForm" property="customFields">
										<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>
										
										<feature:display name="Step1" module="Custom Fields">
											<bean:define id="stepNumber" toScope="request" value="1"/>
											<jsp:include page="addActivityCustomFields.jsp"/>
											<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>
										</feature:display>									
									</logic:notEmpty>
									
			
<!--
									<tr><td bgColor=#f4f4f2 align="center">
										<table cellPadding=3>
											<tr>
												<td>
													<%--<html:submit value="Next >>" styleClass="dr-menu" onclick="return validateForm()"/>--%>
													<html:submit value="Next >>" styleClass="dr-menu" onclick="goNextStep()"/>
												</td>
												<td>
													<html:reset value="Reset" styleClass="dr-menu" onclick="return resetAll()"/>
												</td>
											</tr>
										</table>
									</td></tr>
 -->


								</table>

								<!-- end contents -->
							</td></tr>
							</table>
							</td></tr>
						</table>
						</td>
						<td width="25%" vAlign="top" align="right">
						<!-- edit activity form menu -->
							<jsp:include page="editActivityMenu.jsp"  />
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



