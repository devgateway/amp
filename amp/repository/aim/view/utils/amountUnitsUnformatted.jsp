<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="1" onTrueEvalBody="true">
	<logic:present scope="request" name="amount_prefix"><%= request.getAttribute("amount_prefix") %></logic:present>
	<digi:trn key="aim:amountsinthousands">Amounts in thousands (000)</digi:trn>
	<logic:present scope="request" name="amount_suffix"><%= request.getAttribute("amount_suffix") %></logic:present>
</gs:test>

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="2" onTrueEvalBody="true">
	<logic:present scope="request" name="amount_prefix"><%= request.getAttribute("amount_prefix") %></logic:present>
	<digi:trn key="aim:amountsinmillions">Amounts in millions (000 000)</digi:trn>
	<logic:present scope="request" name="amount_suffix"><%= request.getAttribute("amount_suffix") %></logic:present>
</gs:test>
