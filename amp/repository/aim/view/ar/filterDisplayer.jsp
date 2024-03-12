<%@ page pageEncoding="UTF-8"%>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi"%>

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