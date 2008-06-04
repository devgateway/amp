<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="org.digijava.module.contentrepository.action.SelectDocumentDM"%>
<%@ page import="org.digijava.module.aim.helper.ActivityDocumentsConstants"%>

<%@include file="addDocumentPanel.jsp" %> 

<%@include file="documentManagerJsHelper.jsp" %>

<%@include file="documentManagerDivHelper.jsp" %>
<% String documentsType = (String)pageContext.findAttribute("documentsType"); %>
<logic:notEmpty scope="session" name="<%= org.digijava.module.contentrepository.action.SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>">
	<logic:notEmpty name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" scope="session"
			property="<%=documentsType %>">
	<bean:define name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" 
			property="<%=documentsType %>"  id="relDocs" scope="session" toScope="page"/>
	</logic:notEmpty>
	
	<logic:notEmpty name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" scope="session"
			property="<%=ActivityDocumentsConstants.TEMPORARY_DOCUMENTS %>">
	<bean:define name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" 
			property="<%=ActivityDocumentsConstants.TEMPORARY_DOCUMENTS %>"  id="tempDocs" scope="session" toScope="page"/>
	</logic:notEmpty>

	<logic:empty name="windowName">
		<bean:define id="windowName" toScope="page">
			<digi:trn key="cr:selectedDocs:defaultWindowName">Selection</digi:trn>
		</bean:define>
	</logic:empty>
	<c:if test="${ (!empty relDocs) || (!empty tempDocs) }" >

		<div id="selDocumentsDiv"></div>
		<logic:notEmpty name="showRemoveButton" >
			<logic:equal name="showRemoveButton" value="true">
		&nbsp;&nbsp;&nbsp;&nbsp;
			<html:button  styleClass="dr-menu" property="submitButton" onclick="removeSelectedDocuments()">
				<digi:trn key="btn:remove">Remove</digi:trn>
			</html:button>
			</logic:equal>
		</logic:notEmpty>
		<br /> <br />
		<logic:empty scope="request" name="dmWindowTitle"><bean:define toScope="request" id="dmWindowTitle" value=" " /></logic:empty>
			<script type="text/javascript">
				var rights			= null;
				<logic:notEmpty scope="request" name="crRights">
				rights			= {
						showVersionsRights	: ${showVersionsRights},
						versioningRights	: ${versioningRights},
						makePublicRights	: ${makePublicRights},
						deleteRights		: ${deleteRights}
				};
				</logic:notEmpty>
				windowController	= newWindow('<bean:write name="dmWindowTitle"/>',false,'selDocumentsDiv');
				windowController.setTitle('${windowName}');
				windowController.populateWithSelDocs('<%=documentsType%>', rights);
				
			</script>
	</c:if>
</logic:notEmpty>