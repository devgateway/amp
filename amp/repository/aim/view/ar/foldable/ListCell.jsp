<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<bean:define id="listCell" name="viewable"
	type="org.dgfoundation.amp.ar.cell.ListCell" scope="request"
	toScope="page" />

<%if(listCell.size() > 3) {%>
<div style='position:relative;display:none;' id='<bean:write name="listCell" property="column.name"/>-<bean:write name="listCell" property="ownerId"/>'> 
<%}%>

<logic:iterate name="listCell" property="value" id="subCell"
	scope="page">
	<bean:define id="viewable" name="subCell"
		type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request" />
	<bean:define id="caller" name="listCell"
		type="org.dgfoundation.amp.ar.cell.ListCell" scope="page" toScope="request" />	
		<jsp:include page="<%=viewable.getViewerPath()%>" />
</logic:iterate>
<%if(listCell.size() > 3) {%>
</div>


<c:set var="key">
	aim:reportbuilder:<bean:write name="listCell" property="column.name"/>
</c:set>

<div align="center" onMouseOver="stm(['<digi:trn jsFriendly="true" key="${key}"><bean:write name="listCell" property="column.name"/></digi:trn>',document.getElementById('<bean:write name="listCell" property="column.name"/>-<bean:write name="listCell" property="ownerId"/>').innerHTML],Style[1])" onMouseOut="htm()">[<u><digi:trn key="aim:reportbuilder:list">list...</digi:trn></u>]
</div>
<%} %>