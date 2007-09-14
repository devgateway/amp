<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>
<logic:notPresent name="noDiv">
<div style='position:relative;display:none;' id='<bean:write name="amountCell" property="column.name"/>-<bean:write name="amountCell" property="ownerId"/>'> 
</logic:notPresent>
<ul>
	<logic:notEmpty name="amountCell" property="currencyCode">
	<li>Source Currency: <bean:write name="amountCell" property="currencyCode"/></li>
	<li>Source Currency Date: <bean:write name="amountCell" property="currencyDate"/></li>
	<li>Source Exchange Rate: <bean:write name="amountCell" property="fromExchangeRate"/></li>
	<li>Target Exchange Rate: <bean:write name="amountCell" property="toExchangeRate"/></li>		
	</logic:notEmpty>
	<logic:notEmpty name="amountCell" property="mergedCells">
	<li>Calculation history:
		<ul>
		<logic:iterate id="mergedCell" name="amountCell" property="mergedCells">
			<li>
			<bean:define id="noDiv" value="true" toScope="request" />
			<bean:define id="viewable" name="mergedCell" type="org.dgfoundation.amp.ar.cell.Cell" scope="page" toScope="request"/>
			<jsp:include page="<%=viewable.getViewerPath()%>"/>	
			</li>
		</logic:iterate>
		</ul>
	</li>
	
	</logic:notEmpty>
</ul>
<logic:notPresent name="noDiv">
</div>
<div align="right" onMouseOver="stm(['Amount Details',document.getElementById('<bean:write name="amountCell" property="column.name"/>-<bean:write name="amountCell" property="ownerId"/>').innerHTML],Style[0])" onMouseOut="htm()">
</logic:notPresent>
<bean:write name="amountCell"/>&nbsp;
<logic:notPresent name="noDiv">
</div>
</logic:notPresent>
