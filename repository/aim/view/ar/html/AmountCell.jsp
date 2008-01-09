<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<bean:define id="amountCell" name="viewable" type="org.dgfoundation.amp.ar.cell.AmountCell" scope="request" toScope="page"/>
<logic:notPresent name="debug">
<div align="right">
</logic:notPresent>
<bean:write name="amountCell"/>&nbsp;
<logic:notPresent name="debug">
</div>
</logic:notPresent>
<logic:present name="debug">
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
