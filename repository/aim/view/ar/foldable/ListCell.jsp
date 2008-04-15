<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<bean:define id="listCell" name="viewable"
	type="org.dgfoundation.amp.ar.cell.ListCell" scope="request"
	toScope="page" />
<div style='position:relative;display:none;' id='<bean:write name="listCell" property="column.name"/>-<bean:write name="listCell" property="ownerId"/>'> 
<ul>
<logic:iterate name="listCell" property="value" id="subCell" scope="page">
	<bean:define id="viewable" name="subCell"
		type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request" />
	<bean:define id="caller" name="listCell"
		type="org.dgfoundation.amp.ar.cell.ListCell" scope="page" toScope="request" />	
		<li><jsp:include page="<%=viewable.getViewerPath()%>" /></li>
</logic:iterate>
</ul>
</div>
<div align="center" onMouseOver="stm(['<bean:write name="listCell" property="column.name"/> List',document.getElementById('<bean:write name="listCell" property="column.name"/>-<bean:write name="listCell" property="ownerId"/>').innerHTML],Style[0])" onMouseOut="htm()">[<u>list...</u>]
</div>