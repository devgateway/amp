<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ page import="java.util.List"%>

<digi:instance property="crDocumentManagerForm" />

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp"  />

<%@include file="addDocumentPanel.jsp" %>

<digi:errors />

<%@include file="documentManagerJsHelper.jsp" %>

<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="75%"
	class="box-border-nopadding">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top">

			<br />
			<div id="otherDocumentsDiv"></div>
			<br />
		</td>
	</tr>
</table>

 <c:set var="translation">
 	<digi:trn>Public Documents</digi:trn>
 </c:set>
<script type="text/javascript">
	function afterPageLoad(e) {
		windowController	= newWindow('${translation}', false, 'otherDocumentsDiv');
		windowController.populateWithPublicDocs();
	}
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
</script>	

<%@include file="documentManagerDivHelper.jsp" %>