<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<bean:define id="textCell" name="viewable" type="org.dgfoundation.amp.ar.cell.TextCell" scope="request" toScope="page" />
<bean:define id="caller" name="caller" scope="request" toScope="page" />
<logic:present name="starFlag" scope="request">
	<bean:define id="starFlagLocal" name="starFlag" scope="request" toScope="page" />
</logic:present>

<logic:notEqual name="textCell"  property="translationKey" value="0">
<div align="left" style="padding-left:<%=request.getAttribute("pading")%>" 
title="${textCell.fullTextVersion}">
	<%if (textCell.getShortTextVersion().length() > 59){ %>
		<logic:present name="starFlag" scope="request">
			<logic:equal name="starFlagLocal" value="true">*</logic:equal>
			<bean:define id="starFlag" value="" scope="page" toScope="request" />
		</logic:present>
		<%=textCell.getShortTextVersion().substring(0,59)%>...
	<%}else{ %>
		<logic:present name="starFlag" scope="request">
			<logic:equal name="starFlagLocal" value="true">*</logic:equal>
			<bean:define id="starFlag" value="" scope="page" toScope="request" />
		</logic:present>
		<bean:write name="textCell" property="shortTextVersion" filter="false"/>
	<%}%>
</div>
</logic:notEqual>


<logic:notEqual name="caller" property="class.name" value="org.dgfoundation.amp.ar.cell.ListCell">
<logic:equal name="textCell" property="hasLongVersion" value="true">
<div style='position:relative;display:none;' id='<bean:write name="textCell" property="column.name"/>-<bean:write name="textCell" property="ownerId"/>'> 
	${textCell.fullTextVersion}
</div>
<div align="center" onMouseOver="stm(['<bean:write name="textCell" property="column.name"/> <digi:trn>Full Text</digi:trn>',document.getElementById('<bean:write name="textCell" property="column.name"/>-<bean:write name="textCell" property="ownerId"/>').innerHTML],Style[1])" onMouseOut="htm()">[<u><digi:trn>full text</digi:trn></u>]
</div>
</logic:equal>
</logic:notEqual>
