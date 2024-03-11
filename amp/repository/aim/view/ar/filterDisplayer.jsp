<%@ page pageEncoding="UTF-8"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>

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