<%@ page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<bean:define id="textCell" name="viewable" type="org.dgfoundation.amp.ar.cell.TextCell" scope="request" toScope="page" />
<bean:define id="caller" name="caller" scope="request" toScope="page" />
<logic:present name="starFlag" scope="request">
	<bean:define id="starFlagLocal" name="starFlag" scope="request" toScope="page" />
</logic:present>
<div class="desktop_project_name_sel" style="max-width:250px; word-wrap:break-word;">
	<logic:present name="starFlag" scope="request">
		<logic:equal name="starFlagLocal" value="true">*</logic:equal>
		<bean:define id="starFlag" value="" scope="page" toScope="request" />
	</logic:present>
	<logic:empty name="textCell" property="shortTextVersion">...</logic:empty>
		<bean:write name="textCell" property="shortTextVersion" filter="false"/>&nbsp;


<logic:notEqual name="caller" property="class.name" value="org.dgfoundation.amp.ar.cell.ListCell">
<logic:equal name="textCell" property="hasLongVersion" value="true">
	<div class="desktop_project_name_sel" style="position:relative;display:none;" id='<bean:write name="textCell" property="column.name"/>-<bean:write name="textCell" property="ownerId"/>'> 
		<bean:write name="textCell" filter="false"/>
	</div>
	
<div align="center" onMouseOver="stm(['<bean:write name="textCell" property="column.name"/> Full Text',document.getElementById('<bean:write name="textCell" property="column.name"/>-<bean:write name="textCell" property="ownerId"/>').innerHTML],Style[1])" onMouseOut="htm()">
<font style="font-family: Arial,Verdana,Helvetica,sans-serif;">[Full text]</font> 
</logic:equal>
</logic:notEqual>
</div>

