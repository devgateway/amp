<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<bean:define id="costs" name="costs" scope="request" toScope="page" type="java.util.Collection"/>
<bean:define id="mode" name="mode" scope="request" toScope="page" type="java.lang.String"/>
<script type="text/javascript">

function openEUActivityDetails(euActivityId) {
			<digi:context name="ActivityDetailsURL" property="context/module/moduleinstance/viewEUActivityDetails.do?euActivityId=" />
			openURLinWindow("<%=ActivityDetailsURL%>"+euActivityId,500, 350);
}
</script>
<logic:equal name="mode" value="preview">
	<table width="100%" bgcolor="#ffffff">
		<tr>
			<td>
				<table class="box-border-nopadding" style="font-size:11px;">
					<tr class="textnegru" bgcolor="#f4f4f2" align="right">
					
</logic:equal>
<logic:notEqual name="mode" value="preview">
	<table width="90%" bgcolor="#f4f4f2">
		<tr class="textalb" bgcolor="#006699" align="right">
</logic:notEqual>
	<field:display name="Costing Activity Name" feature="Costing">
		<td align="left"><b><digi:trn key="aim:viewcostssummary:name" > Name</digi:trn></b></td></field:display>
		<field:display name="Costing Total Cost" feature="Costing">
		<td><b><digi:trn key="aim:viewcostssummary:totalCost">Total Cost</digi:trn></b></td>
		</field:display>
		<field:display name="Costing Total Contribution" feature="Costing">
		<td><b><digi:trn key="aim:viewcostssummary:totalContribution">Total Contribution</digi:trn></b></td>
		</field:display>
	</tr>
	<%Double grandCost = new Double(0);
	Double grandContribution = new Double(0);%>

	<logic:iterate name="costs" id="cost"
		type="org.digijava.module.aim.dbentity.EUActivity" indexId="idx">
		<bean:define id="euActivity" name="cost"
			type="org.digijava.module.aim.dbentity.EUActivity" scope="page"
			toScope="request" />

		<c:set target="${euActivity}" property="desktopCurrencyId"
			value="<%= org.digijava.module.aim.util.CurrencyUtil.getDefaultCurrency().getCurrencyCode()%>" />

		<%grandCost = new Double(euActivity.getTotalCostConverted()+grandCost.doubleValue());%>
		<%grandContribution = new Double(euActivity
						.getTotalContributionsConverted()+grandContribution.doubleValue());%>

		<tr bgcolor="#FFFFFF" align="right">
			<td align="left"><b>

			<logic:equal name="mode" value="form">
			<c:set var="translation"><digi:trn key="aim:viewcostsummary:remove">Remove</digi:trn>
			</c:set>

			<a style="cursor:pointer;color:#006699"
				title="Click to edit the activity"
				onClick='editEUActivity(<bean:write name="idx"/>)'> <bean:write
				name="euActivity" property="name" /> </a> &nbsp;&nbsp; </b> [<a
				style="cursor:pointer;color:#006699"
				title="Click to remove the activity"
				onClick='deleteEUActivity(<bean:write name="idx"/>)'>${translation}</a>]
			</logic:equal>
			<logic:equal name="mode" value="view">
			<a style="cursor:pointer;color:#006699"
				title="Click to view the activity"
				onClick='openEUActivityDetails(<bean:write name="euActivity" property="id"/>)'> <bean:write
				name="euActivity" property="name" /> </a> &nbsp;&nbsp; </b>
			</logic:equal>
			<field:display name="Costing Activity Name" feature="Costing">
			<logic:equal name="mode" value="preview">
				<bean:write name="euActivity" property="name"/>
			</logic:equal>
			</field:display>
				</td>
				<field:display name="Costing Total Cost" feature="Costing">
			<td><bean:write name="euActivity" property="totalCostConverted"
				format="###,###,###.##" /></td>
				</field:display>
				<field:display name="Costing Total Contribution" feature="Costing">
			<td><bean:write name="euActivity"
				property="totalContributionsConverted" format="###,###,###.##" /></td>
	</field:display>

		</tr>
		<tr bgcolor="#FFFFFF">
			<td colspan="3">
			<table>
			<field:display name="Costing Inputs" feature="Costing">
			<logic:notEmpty name="euActivity" property="inputs">
			<tr><td align="right"><i><digi:trn key="aim:viewcostssummary:inputs">Inputs:</digi:trn></i></td><td width="400"><bean:write name="euActivity" property="inputs" /></td></tr>
			</logic:notEmpty>
			</field:display>
			<field:display name="Costing Assumptions" feature="Costing">
			<logic:notEmpty name="euActivity" property="assumptions">
			<tr><td align="right"><i><digi:trn key="aim:viewcostssummary:assumptions">Assumptions:</digi:trn></i></td><td width="400"><bean:write name="euActivity" property="assumptions" /></td></tr>
			</logic:notEmpty>
			</field:display>
			<field:display name="Costing Progress" feature="Costing">
			<logic:notEmpty name="euActivity" property="progress">
			<tr><td align="right"><i><digi:trn key="aim:viewcostssummary:progress">Progress:</digi:trn></i></td><td width="400"><bean:write name="euActivity" property="progress" /></td></tr>
			</logic:notEmpty>
			</field:display>
			<field:display name="Costing Due Date" feature="Costing">
			<logic:notEmpty name="euActivity" property="dueDate">
			<tr><td align="right"><i><digi:trn key="aim:viewcostssummary:dueDate">Due Date:</digi:trn></i></td><td width="400"><bean:write name="euActivity" property="dueDate" format="MM/dd/yyyy" /></td></tr>
			</logic:notEmpty>
			</field:display>
			</table>
			</td>
		</tr>
	</logic:iterate>
	<tr bgcolor="#FFFFFF">
		<td>&nbsp;</td>
		<td colspan="2">
		<hr />
		</td>
	</tr>
	
<c:if test="${!empty costs}">
	<tr bgcolor="#FFFFFF">
		<td align="right"><b><digi:trn key="aim:viewcostssummary:totals">Totals:</digi:trn></b></td>
		<field:display name="Grand Total Cost" feature="Costing">
		<td align="right"><fmt:formatNumber var="grandCostFormatted"
			pattern="###,###,###.##" value="<%=grandCost%>" />
		${grandCostFormatted}</td>
		</field:display>
		<field:display name="Grand Total Cost" feature="Costing">
		<td align="right"><fmt:formatNumber var="grandContributionFormatted"
			pattern="###,###,###.##" value="<%=grandContribution%>" />
		${grandContributionFormatted}</td>
		</field:display>
	</tr>
	<field:display name="Costing Contribution Gap" feature="Costing">
		<tr bgcolor="#FFFFFF">
			<td align="right"><b><digi:trn key="aim:viewcostssummary:contributionGap">Contribution Gap:</digi:trn></b></td>
			<td align="right"><b> <fmt:formatNumber var="contributionGap"
				pattern="###,###,###.##" value="<%=new Double(grandCost.doubleValue()-grandContribution.doubleValue())%>" />
			${contributionGap} </b></td>
			<td align="right">&nbsp;</td>
		</tr>
	</field:display>
</c:if>
	<logic:equal name="mode" value="preview">
			</table>
			</td>
			</tr>
	</logic:equal>
</table>
