<%@ page session="true" contentType="text/html; charset=ISO-8859-1" %>
<%@ taglib uri="http://www.tonbeller.com/jpivot" prefix="jp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<jsp:include page="PrepareQuery.jsp"/>

<jp:mondrianQuery 
	id="query01" 
	dataSource="ampDS"  
	catalogUri="/WEB-INF/queries/AMP.xml"
	dynResolver="org.digijava.module.mondrian.query.SchemaManager"
	
>
	<%=session.getAttribute("querystring")%>
</jp:mondrianQuery>

<c:set var="title01" scope="session"></c:set>
