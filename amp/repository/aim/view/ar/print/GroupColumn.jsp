<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<bean:define id="groupColumn" name="viewable" type="org.dgfoundation.amp.ar.GroupColumn" scope="request" toScope="page"/>
<bean:define id="ownerId" name="ownerId" type="java.lang.Long" scope="request" toScope="page"/>
<bean:define id="isGroup" name="isGroup" type="java.lang.String" scope="request" toScope="page"/>

<% if(groupColumn.getVisibleCellCount(ownerId)!=0){ %>


<logic:iterate name="groupColumn" property="items" id="column" scope="page" type="org.dgfoundation.amp.ar.CellColumn">
<% if(column.getVisibleCellCount(ownerId)!=0){ %>
			<span style="font-style: italic;font-size: 9px;">
			
			<c:set var="key">
				aim:reportBuilder:<%=column.getName()%>
			</c:set>
	 		<digi:trn key="${key}">
	 				<%=column.getName()%>
			 </digi:trn>
			 :</span>
			<bean:define id="isGroup"  type="java.lang.String" value="true" toScope="request"/>
			<bean:define id="viewable" name="column" type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request"/>
			<jsp:include  page="<%=viewable.getViewerPath()%>"/><br>	
				
<%}%>		
</logic:iterate>
<%}
  
else{%>
	&nbsp;	
<%}%>