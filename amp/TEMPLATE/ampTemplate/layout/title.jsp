<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%
	String title=(String)((org.apache.struts.tiles.ComponentContext) request.getAttribute("org.apache.struts.taglib.tiles.CompContext")).getAttribute("title");
	String key=(title.replaceAll(" ",""));
%>
	
<title>			
	<logic:present name="extraTitle" scope="request">
		<bean:define id="extTitle" name="extraTitle" scope="request" type="java.lang.String" />
		<c:set var="key">aim:pagetitle:<%=key%><%=extTitle%></c:set>
		<digi:trn neverShowLinks ="true" >Aid Management Platform </digi:trn>
		<digi:trn neverShowLinks ="true"><%=title%></digi:trn> ${extTitle}
	</logic:present>
	<logic:notPresent name="extraTitle" scope="request">
		<c:set var="key">aim:pagetitle:<%=key%></c:set>
		<digi:trn neverShowLinks = "true">Aid Management Platform </digi:trn>
		<digi:trn key="${key}" neverShowLinks ="true">
			<%=title%>
		</digi:trn>
	</logic:notPresent>
</title>