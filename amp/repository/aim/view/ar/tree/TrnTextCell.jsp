<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>

<bean:define id="trnTextCell" name="viewable" type="org.dgfoundation.amp.ar.cell.TrnTextCell" scope="request" toScope="page" />
<bean:define id="caller" name="caller" scope="request" toScope="page" />
<div align="left"><%=trnTextCell.getValue().toString()%></div>

<logic:notEqual name="caller" property="class.name" value="org.dgfoundation.amp.ar.cell.ListCell">
<logic:equal name="trnTextCell" property="hasLongVersion" value="true">
<div style='position:relative;display:none;' id='<bean:write name="trnTextCell" property="column.name"/>-<bean:write name="trnTextCell" property="ownerId"/>'> 
	<bean:write name="trnTextCell" filter="false"/>
</div>
<div align="left">
......
</div>
<div align="center" onMouseOver="stm(['<bean:write name="trnTextCell" property="column.name"/> Full Text',document.getElementById('<bean:write name="trnTextCell" property="column.name"/>-<bean:write name="trnTextCell" property="ownerId"/>').innerHTML],Style[1])" onMouseOut="htm()">[<u>full text</u>]
</logic:equal>
</logic:notEqual>

