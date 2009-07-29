<%@ page pageEncoding="UTF-8"%>
<%@ page import="org.dgfoundation.amp.ar.ArConstants" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>

<bean:define id="<%=ArConstants.REPORTS_FILTER%>" name="reportsFilter" scope="session" toScope="page"/>

<logic:present name="reportsFilter">
<table>
<logic:present name="reportsFilter" property="ampCurrencyCode">
<tr><td align="right">
<digi:trn key="reportsFilter:currency">Currency Code</digi:trn>:
</td><td>
<bean:write name="reportsFilter" property="ampCurrencyCode"/>
</td>
</logic:present>
</table>
</logic:present>