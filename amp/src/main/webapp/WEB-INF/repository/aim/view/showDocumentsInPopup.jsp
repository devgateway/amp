<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
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

<jsp:include page="/WEB-INF/repository/contentrepository/view/showSelectedDocumentsDM.jsp"/>
</body>
</html>