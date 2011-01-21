<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<bean:define id="listCell" name="viewable"	type="org.dgfoundation.amp.ar.cell.ListCell" scope="request" toScope="page" />
<logic:iterate name="listCell" property="value" id="subCell" scope="page">
	<bean:define id="viewable" name="subCell" 	type="org.dgfoundation.amp.ar.Viewable" scope="page" toScope="request" />
		<jsp:include page="<%=viewable.getViewerPath()%>" />&nbsp;&nbsp;
</logic:iterate>
