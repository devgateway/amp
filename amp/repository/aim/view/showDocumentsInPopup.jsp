<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<html>
<body>
<br /> <br />
<bean:define toScope="request" id="showRemoveButton" value="true" />
<bean:define toScope="request" id="documentsType" value="<%=org.digijava.module.aim.dbentity.AmpOrganisationDocument.SESSION_NAME %>" />
<bean:define toScope="request" id="versioningRights" value="false" />
<bean:define toScope="request" id="makePublicRights" value="false" />
<bean:define toScope="request" id="showVersionsRights" value="false" />
<bean:define toScope="request" id="deleteRights" value="false" />
<bean:define toScope="request" id="crRights" value="true" />
<bean:define toScope="request" id="showLineBreaks" value="true" />

<jsp:include page="/repository/contentrepository/view/showSelectedDocumentsDM.jsp"/>
</body>
</html>