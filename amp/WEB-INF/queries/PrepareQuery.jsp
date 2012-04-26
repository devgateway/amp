<%@page import="org.digijava.module.mondrian.query.MondrianQuery"%>

<%
	MondrianQuery mq = new MondrianQuery();
	mq.createQuery(request);	
%>