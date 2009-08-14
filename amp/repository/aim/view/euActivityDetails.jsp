<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<bean:define id="euActivity" name="euActivity"
	type="org.digijava.module.aim.dbentity.EUActivity" scope="request"
	toScope="page" />
<table width="100%" border="0" cellspacing="2" cellpadding="2"
	align="center" class="box-border-nopadding">
	<tr>
		<td colspan="4" bgcolor="#006699" class="textalb" align="center">
		View Activity</td>
	</tr>
	
<tr><td align="center">
<table width="90%" border="0" cellspacing="2" cellpadding="2"
	align="center">


	<tr>
		<td align="left"><b>
		<font size="+1">
		<bean:write name="euActivity" property="name" />
		</font>
		</b>
	</tr>
	<tr>
		<td><b>Activity ID:</b>&nbsp;<bean:write name="euActivity" property="textId" /></td>
	</tr>
	<tr>
		<td><b>Inputs:</b>&nbsp;<bean:write name="euActivity" property="inputs" /></td>
	</tr>
	<tr>
	<td align="center">
		<table width="100%"  class="box-border-nopadding">
		<tr>
		<td bgcolor="#006699" class="textalb" align="center"><b>Total Cost</b></td>
		</tr>
		<tr>
			<td>Amount:&nbsp;<bean:write name="euActivity" property="totalCost" />&nbsp;<bean:write
				name="euActivity" property="totalCostCurrency.currencyCode" /></td>
		</tr>

		</table>
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
	<td align="center">
		<table width="100%"  class="box-border-nopadding">
		<tr>
		<td bgcolor="#006699" class="textalb" align="center"><b>Contributions</b></td>
		</tr>
		<logic:iterate name="euActivity" property="contributions" id="contribution">
		<tr><td class="box-border-nopadding">
				Funding Org:&nbsp;<bean:write name="contribution" property="donor.name" /><br/>
				Financing Instrument:&nbsp;<bean:write name="contribution" property="financingTypeCategVal.value"/><br/>
				Financing Type:&nbsp;<bean:write name="contribution" property="financingInstr.value"/><br/>				
				Amount:&nbsp;<bean:write name="contribution" property="amount" />&nbsp;<bean:write
						name="contribution" property="amountCurrency.currencyCode" />
		</td></tr>
		</logic:iterate>

		</table>
	</td>
	</tr>
	<tr><td><b>Assumptions:</b>&nbsp;<bean:write name="euActivity" property="assumptions" /></td></tr>
	<tr><td><b>Progress:</b>&nbsp;<bean:write name="euActivity" property="progress" /></td></tr>
	<tr><td><b>Due Date:</b>&nbsp;<bean:write name="euActivity" property="dueDate" format="MM/dd/yyyy" /></td></tr>	
</table>
</td>
</tr>
<tr><td align="center">
		<input type="button" class="dr-menu" value="Close" onclick="window.close()"/>&nbsp;&nbsp;
		<input type="button" class="dr-menu" value="Print" onclick="window.print()"/>		
</td></tr>
</table>
