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

<%@include file="documentManagerDivHelper.jsp" %>

<br />
<div id="otherDocumentsDiv"></div>
<button onclick="saveSelectedDocuments()" type="button">Submit</button> 
<br /><br />
&nbsp;&nbsp;
<a style="cursor:pointer; text-decoration:underline; color: blue; font-size: x-small" onClick="newWindow('Select Documents', true, 'otherDocumentsDiv')" /> 
	Click here to add a new window
</a>

<script type="text/javascript">
	newWindow( 'Select Documents',true,'otherDocumentsDiv');
</script>	
	
	