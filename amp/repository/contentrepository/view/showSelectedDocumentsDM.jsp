<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>

<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<%@include file="addDocumentPanel.jsp" %> 

<%@include file="documentManagerJsHelper.jsp" %>

<%@include file="documentManagerDivHelper.jsp" %>

<logic:notEmpty scope="session" name="<%= org.digijava.module.contentrepository.action.SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>">
	<logic:notEmpty scope="session" name="<%= org.digijava.module.contentrepository.action.SelectDocumentDM.CONTENT_REPOSITORY_HASH_MAP %>" 
			property="${documentsType}">
	<div id="selDocumentsDiv"></div>
	<logic:notEmpty name="showRemoveButton" >
		<logic:equal name="showRemoveButton" value="true">
	&nbsp;&nbsp;&nbsp;&nbsp;<button class="buton" onclick="removeSelectedDocuments()" type="button">Remove</button> 
		</logic:equal>
	</logic:notEmpty>
	<br /> <br />
	<logic:empty scope="request" name="dmWindowTitle"><bean:define toScope="request" id="dmWindowTitle" value=" " /></logic:empty>
		<script type="text/javascript">
			var rights			= null;
			<logic:notEmpty scope="request" name="crRights">
			rights			= {
					versioningRights	: ${versioningRights},
					deleteRights		: ${deleteRights}
			};
			</logic:notEmpty>
			windowController	= newWindow('<bean:write name="dmWindowTitle"/>',false,'selDocumentsDiv');
			windowController.populateWithSelDocs('<%= org.digijava.module.aim.helper.ActivityDocumentsConstants.RELATED_DOCUMENTS%>', rights);
			
		</script>
	
	</logic:notEmpty>

</logic:notEmpty>