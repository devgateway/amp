<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<bean:define id="textCell" name="viewable" type="org.dgfoundation.amp.ar.cell.TextCell" scope="request" toScope="page" />
<bean:define id="caller" name="caller" scope="request" toScope="page" />
<div align="left"><bean:write name="textCell" property="shortTextVersion" filter="false"/>&nbsp;</div>

<logic:notEqual name="caller" property="class.name" value="org.dgfoundation.amp.ar.cell.ListCell">
<logic:equal name="textCell" property="hasLongVersion" value="true">
<div style='position:relative;display:none;' id='<bean:write name="textCell" property="column.name"/>-<bean:write name="textCell" property="ownerId"/>'> 
	<bean:write name="textCell" filter="false"/>
</div>
<div align="left">
......
</div>
<div align="center" onMouseOver="stm(['<bean:write name="textCell" property="column.name"/> Full Text',document.getElementById('<bean:write name="textCell" property="column.name"/>-<bean:write name="textCell" property="ownerId"/>').innerHTML],Style[1])" onMouseOut="htm()">[<u>full text</u>]
</logic:equal>
</logic:notEqual>

