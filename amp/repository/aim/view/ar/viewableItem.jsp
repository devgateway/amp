<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>

<bean:define id="viewable" name="viewable" type="org.dgfoundation.amp.ar.Viewable" scope="request" toScope="page"/>
<jsp:include page="<%=viewable.getViewerPath()%>"/>