<%@ page pageEncoding="UTF-8" %> 
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/moduleVisibility.tld" prefix="module" %>
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