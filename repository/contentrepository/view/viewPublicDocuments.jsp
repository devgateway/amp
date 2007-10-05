<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>

<digi:instance property="crDocumentManagerForm" />

<%@include file="addDocumentPanel.jsp" %>

<%@include file="documentManagerJsHelper.jsp" %>

<%@include file="documentManagerDivHelper.jsp" %>

<br />
<div id="otherDocumentsDiv"></div>
<br />

<script type="text/javascript">
	windowController	= newWindow( 'Public Documents', false, 'otherDocumentsDiv');
	windowController.populateWithPublicDocs();
</script>	
