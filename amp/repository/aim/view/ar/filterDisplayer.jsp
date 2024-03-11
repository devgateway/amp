<%@ page pageEncoding="UTF-8"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>
<bean:define id="reportsFilter" name="reportCD" property="filter" toScope="session" />

<logic:present name="reportsFilter">
	<table>
		<logic:present name="reportsFilter" property="ampCurrencyCode">
			<tr>
				<td align="right">
					<digi:trn key="reportsFilter:currency">Currency Code</digi:trn>:
				</td>
				<td>
					<bean:write name="reportsFilter" property="ampCurrencyCode"/>
				</td>
			</tr>
		</logic:present>
	</table>
</logic:present>