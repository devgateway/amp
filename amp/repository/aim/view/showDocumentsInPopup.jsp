<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/resources/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/resources/tld/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module" %>
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