<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org/digi" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>

<digi:instance property="crDocumentManagerForm" />

<jsp:include page="/WEB-INF/jsp/aim/view/teamPagesHeader.jsp"  />

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
