<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>


<bean:define id="groupColumn" name="viewable" type="org.dgfoundation.amp.ar.GroupColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>

<logic:iterate name="groupColumn" property="items" id="column" scope="page">
	<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
	<jsp:include page="<%=viewable.getViewerPath()%>">
		<jsp:param name="inTotalColumn" value="<%=groupColumn.getName().equals(ArConstants.COLUMN_TOTAL)%>"/>
	</jsp:include>	
</logic:iterate>