<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>
<logic:notPresent name="debug">
<div align="right">
</logic:notPresent>
<logic:present name="debug">
<div align="center" onMouseOver="stm(['AmountCell Properties',document.getElementById('cellId-<bean:write name="amountCell" property="objectId"/>').innerHTML],Style[1])" onMouseOut="htm()">
</logic:present>
<bean:write name="amountCell"/>&nbsp;
<logic:present name="debug">
</div>
</logic:present>
<logic:notPresent name="debug">
</div>
</logic:notPresent>
<logic:present name="debug">

<div style='position:relative;display:none;' id='cellId-<bean:write name="amountCell" property="objectId"/>'> 
<ul>
<li>Object Id=<bean:write name="amountCell" property="hashCode"/></li>
<li>From Rate=<bean:write name="amountCell" property="fromExchangeRate"/></li>
<li>To Rate=<bean:write name="amountCell" property="toExchangeRate"/></li>
<li>Percentage=<bean:write name="amountCell" property="percentage"/>%</li>
<li>Percentage Columns=<bean:write name="amountCell" property="columnPercent"/></li>
<li>Percentage Ref Value=<bean:write name="amountCell" property="columnCellValue"/></li>
<li>Currency Code=<bean:write name="amountCell" property="currencyCode"/></li>
<li>Currency Date=<bean:write name="amountCell" property="currencyDate"/></li>
<li>Activity Owner Id=<bean:write name="amountCell" property="ownerId"/></li>
<li>Source Column=<bean:write name="amountCell" property="column.name"/></li>
<logic:present name="amountCell" property="metaData">
<li>Meta Information=<bean:write name="amountCell" property="metaData"/></li>
</logic:present>
</ul>
</div>
<logic:notEmpty name="amountCell" property="mergedCells">
=(<i>
<bean:write name="amountCell" property="wrappedAmount"/><i> &nbsp;
		<logic:iterate id="mergedCell" name="amountCell" property="mergedCells">
			<bean:define id="noDiv" value="true" toScope="request" />
			<bean:define id="viewable" name="mergedCell" type="org.dgfoundation.amp.ar.cell.Cell" scope="page" toScope="request"/>
			<jsp:include page="AmountCell.jsp"/>+
		</logic:iterate>
0)
</i>

</logic:notEmpty>
</logic:present>
