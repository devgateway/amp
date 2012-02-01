<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>

<digi:instance property="crSelectDocumentForm" />

<bean:define id="myForm" name="crSelectDocumentForm" toScope="request" type="org.digijava.module.contentrepository.form.SelectDocumentForm" />
<bean:define id="teamForm" name="myForm" property="teamInformationBeanDM" toScope="page" />
<bean:define id="isTeamLeader" name="teamForm" property="isTeamLeader" />
<bean:define id="meTeamMember" name="teamForm" property="meTeamMember" />
<bean:define id="tMembers" name="teamForm" property="myTeamMembers" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
	
<%@include file="documentManagerJsHelper.jsp" %>

<script type="text/javascript">
	function afterPageLoad(e) {		
		var showTheFollowingDocuments 	= 'ALL';
		<c:if test="${myForm.showTheFollowingDocuments!=null}">
			showTheFollowingDocuments		= '${myForm.showTheFollowingDocuments}';
		</c:if>
	    var select = '<digi:trn key="contentrepository:SelectDocumentsTitle">Select Documents</digi:trn>';
	    var windowHandler;
	    if(showTheFollowingDocuments=='PUBLIC'){
	    	windowHandler	= newWindow( select,false,'otherDocumentsDiv');
	    }else{
	    	windowHandler	= newWindow( select,true,'otherDocumentsDiv');	    	
	    }		
		
		if (showTheFollowingDocuments == 'PUBLIC') {
			windowHandler.populateWithPublicDocs();
		}
	}
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
	
	var enterBinder	= new EnterHitBinder('addDocFromRepBtn');
</script>	

<br />
<div id="otherDocumentsDiv">&nbsp;</div>
<input type="hidden" name="type" id="typeId"/>
<html:button  styleClass="dr-menu" property="submitButton" onclick="saveSelectedDocuments()" styleId="addDocFromRepBtn">
	<digi:trn>Submit this</digi:trn>
</html:button>
<br /><br />
&nbsp;&nbsp;
<c:if test="${myForm.showTheFollowingDocuments=='ALL'}">
	<c:set var="translation">
			<digi:trn>Click here to open a new document window</digi:trn>
	</c:set>
	<a title="${translation}" style="cursor:pointer; text-decoration:underline; color: blue; font-size: x-small" onClick="newWindow('<digi:trn>Select Documents</digi:trn>', true, 'otherDocumentsDiv')" /> 
		<digi:trn>New window</digi:trn>
	</a>
</c:if>

<%@include file="documentManagerDivHelper.jsp" %>
	