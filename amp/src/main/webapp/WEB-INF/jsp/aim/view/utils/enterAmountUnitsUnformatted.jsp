<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://digijava.org/GlobalSettings" prefix="gs" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="1" onTrueEvalBody="true">
	<logic:present scope="request" name="amount_prefix"><%= request.getAttribute("amount_prefix") %></logic:present>
	<digi:trn key="aim:pleaseEnterTheAmountInThousands">Please enter amount in thousands (000)</digi:trn>
	<logic:present scope="request" name="amount_suffix"><%= request.getAttribute("amount_suffix") %></logic:present>
</gs:test>

<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="2" onTrueEvalBody="true">
	<logic:present scope="request" name="amount_prefix"><%= request.getAttribute("amount_prefix") %></logic:present>
	<digi:trn key="aim:pleaseEnterTheAmountInMillions">Please enter amount in millions (000 000)</digi:trn>
	<logic:present scope="request" name="amount_suffix"><%= request.getAttribute("amount_suffix") %></logic:present>
</gs:test>
