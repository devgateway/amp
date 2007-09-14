<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>
<logic:notPresent name="noDiv">
<div align="right" onMouseOver="stm(['Amount Details',document.getElementById('<bean:write name="amountCell" property="column.name"/>-<bean:write name="amountCell" property="ownerId"/>').innerHTML],Style[0])" onMouseOut="htm()">
</logic:notPresent>
<bean:write name="amountCell"/>&nbsp;
<logic:notPresent name="noDiv">
</div>
</logic:notPresent>
