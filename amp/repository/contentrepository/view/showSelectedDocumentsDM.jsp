<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ page import="org.digijava.ampModule.contentrepository.action.SelectDocumentDM"%>
<%@ page import="org.digijava.ampModule.aim.helper.ActivityDocumentsConstants"%>

<%@include file="addDocumentPanel.jsp" %>
<logic:notEmpty name="checkBoxToHide" scope="request">
	<html:hidden property="checkBoxToHide" value="true" styleId="checkBoxToHide"/>
</logic:notEmpty> 
<logic:empty name="checkBoxToHide" scope="request">
	<html:hidden property="checkBoxToHide" value="false" styleId="checkBoxToHide"/>
</logic:empty> 

<%@include file="documentManagerJsHelper.jsp" %>

<%@include file="documentManagerDivHelper.jsp" %>
<% String documentsType = (String)pageContext.findAttribute("documentsType"); %>
<logic:notEmpty scope="session" name="<%= org.digijava.ampModule.contentrepository.action.SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>">

	<logic:notEmpty name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" scope="session" property="<%=documentsType %>">		
		<bean:define name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" property="<%=documentsType %>"  id="relDocs" scope="session" toScope="page"/>
		<c:set var="removeFrom">'<%=documentsType %>'</c:set>
	</logic:notEmpty>
	
	<logic:notEmpty name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" scope="session" property="<%=documentsType %>">		
		<bean:define name="<%= SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" property="<%=documentsType %>"  id="relDocs" scope="session" toScope="page"/>
	</logic:notEmpty>
	
	
	<c:if test="${viewAllRights==null}">
		<c:set var="viewAllRights" value="false"/>
	</c:if>

	<logic:empty name="windowName">
		<c:set var="windowName" target="request">&nbsp;</c:set>
	</logic:empty>	
	
	
		<div id="selDocumentsDiv"></div>
		<field:display name="Remove Documents Button" feature="Related Documents">
		<logic:notEmpty name="showRemoveButton" >
			<logic:equal name="showRemoveButton" value="true">
		<c:if test="${showLineBreaks}">&nbsp;&nbsp;&nbsp;&nbsp;</c:if>
			<html:button  styleClass="buttonx_sm" property="submitButton" onclick="removeSelectedDocuments(${removeFrom})">
				<digi:trn key="btn:remove">Remove</digi:trn>
			</html:button>
			</logic:equal>
		</logic:notEmpty>
		</field:display>
		<c:if test="${showLineBreaks}"><br /> <br /></c:if>
		
		
			<script type="text/javascript">
				var rights			= null;
				<logic:notEmpty scope="request" name="crRights">
				rights			= {
						showVersionsRights	: ${showVersionsRights},
						versioningRights	: ${versioningRights},
						makePublicRights	: ${makePublicRights},
						deleteRights		: ${deleteRights},
						viewAllRights		: ${viewAllRights}
				};
				</logic:notEmpty>
//				alert('showselecteddocumentsdm.jsp');
				windowController	= newWindow('${windowName}',false,'selDocumentsDiv');
				// windowController.setTitle('${windowName}');
				windowController.populateWithSelDocs('<%=documentsType%>', rights);
				
			</script>
	
</logic:notEmpty>