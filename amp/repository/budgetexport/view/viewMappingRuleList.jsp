<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="beMapingForm"/>
	
<digi:form action="/showMapingRuleList" method="post">
	<html:hidden name="beMapingForm" property="id"/>
	<table style="border-collapse:collapse;" border="1">
		<logic:present name="beMapingForm" property="rules">
			<logic:notEmpty name="beMapingForm" property="rules">
				<tr>
					<td>Name</td>
					<td>AMP Column</td>
					<td>Has header</td>
					<td colspan="3">&nbsp;</td>
					<td>Total/Mapped</td>
					<td>CSV items</td>
				</tr>
					<logic:iterate name="beMapingForm" property="rules" id="rule">
						<tr>
							<td>
								<bean:write name="rule" property="name"/>
							</td>
							<td>
								<bean:write name="rule" property="ampColumn.columnName"/>
							</td>
							<td>
								<bean:write name="rule" property="header"/>
							</td>
							<td>
								<a href="/budgetexport/addEditDeleteMapRule.do?action=edit&id=<bean:write name="rule" property="id"/>&curProjectId=<bean:write name="beMapingForm" property="id"/>">edit</a>
							</td>
							<td>
								<a href="/budgetexport/addEditDeleteMapRule.do?action=delete&id=<bean:write name="rule" property="id"/>&curProjectId=<bean:write name="beMapingForm" property="id"/>">delete</a>
							</td>
							<td>
								<a href="/budgetexport/mapActions.do?action=view&ruleId=<bean:write name="rule" property="id"/>&projectId=<bean:write name="beMapingForm" property="id"/>">Manage map</a>
							</td>
							<td>
								(<bean:write name="rule" property="totalAmpEntityCount"/>/<bean:write name="rule" property="mappedAmpEntityCount"/>)
							</td>
							<td>
								<bean:write name="rule" property="csvItemCount"/>
							</td>
						</tr>
					</logic:iterate>
			</logic:notEmpty>
		</logic:present>
		<tr>
			<td colspan="8" align="right">
				<a href="/budgetexport/addEditDeleteMapRule.do?action=add&curProjectId=<bean:write name="beMapingForm" property="id"/>">Add new rule</a>
			</td>		
		</tr>
	</table>
     </div>		
</digi:form>