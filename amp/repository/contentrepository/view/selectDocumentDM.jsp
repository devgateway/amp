<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>

<digi:instance property="crSelectDocumentForm" />

<%@include file="addDocumentPanel.jsp" %>

<bean:define id="myForm" name="crSelectDocumentForm" toScope="page"
	type="org.digijava.module.contentrepository.form.SelectDocumentForm" />

<bean:define id="teamForm" name="myForm" property="teamInformationBeanDM" toScope="page" />
	
<bean:define id="isTeamLeader" name="teamForm" property="isTeamLeader" />
<bean:define id="meTeamMember" name="teamForm" property="meTeamMember" />

<bean:define id="tMembers" name="teamForm" property="myTeamMembers" />

	
<%@include file="documentManagerJsHelper.jsp" %>

<script type="text/javascript">
	function afterPageLoad(e) {
	    var select = '<digi:trn key="contentrepository:SelectDocumentsTitle">Select Documents</digi:trn>';
		newWindow( select,true,'otherDocumentsDiv');
	}
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
</script>	

<br />
<div id="otherDocumentsDiv">&nbsp;</div>
<html:button  styleClass="buton" property="submitButton" onclick="saveSelectedDocuments()" >
	<digi:trn key="btn:submitDocumentsFromRepository">Submit this</digi:trn>
</html:button>
<br /><br />
&nbsp;&nbsp;
	<c:set var="translation">
			<digi:trn key="contentrepository:newWindowExplanation">Click here to open a new document window</digi:trn>
	</c:set>
<a title="${translation}" style="cursor:pointer; text-decoration:underline; color: blue; font-size: x-small" onClick="newWindow('<digi:trn key="contentrepository:SelectDocumentsTitle">Select Documents</digi:trn>', true, 'otherDocumentsDiv')" /> 
	<digi:trn key="contentrepository:newWindow">New window</digi:trn>
</a>

<%@include file="documentManagerDivHelper.jsp" %>
	