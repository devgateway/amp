<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<%@page import="org.digijava.module.aim.util.CurrencyUtil"%>

<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%><digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
<digi:instance property="aimCurrencyRateForm" />



<jsp:include page="scripts/newCalendar.jsp"  />

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp"  />
</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>


<script language="JavaScript">

function validate() {
	if (isEmpty(document.aimCurrencyRateFormPop.updateCRateCode.value) == true) {
		alert('<digi:trn jsFriendly="true" key="aim:currencyCodenotEntered">Currency code not entered</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateCode.focus();
		return false;
	}
	if (document.aimCurrencyRateFormPop.updateCRateCode.value == 'USD') {
		alert('<digi:trn jsFriendly="true" key="aim:selectDifferentCurrency">All exchange rates are saved in terms of USD. Please select a different currency.</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateCode.focus();
		return false;
	}

	if (isEmpty(document.aimCurrencyRateFormPop.updateCRateDate.value) == true) {
		alert('<digi:trn jsFriendly="true" key="aim:exchangeRateDateNotEntered">Exchange rate date not entered</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateDate.focus();
		return false;
	}
	if (isEmpty(document.aimCurrencyRateFormPop.updateCRateAmount.value) == true) {
		alert('<digi:trn jsFriendly="true" key="aim:exchangeRateNotEntered">Exchange rate not entered</digi:trn>');
		document.aimCurrencyRateFormPop.updateCRateAmount.focus();
		return false;
	}
	
	if (checkAmountUsingSymbols(document.aimCurrencyRateFormPop.updateCRateAmount.value,'<%=FormatHelper.getGroupSymbol()%>','<%=FormatHelper.getDecimalSymbol()%>') == false) 
		{
			alert('<digi:trn jsFriendly="true" key="aim:invalidExchangeRateEntered">Invalid exchange rate entered</digi:trn>');
			document.aimCurrencyRateFormPop.updateCRateAmount.focus();
			return false;
		}
	
	return true;
}

function saveRate() {
	var valid = validate();
	if (valid == true) {
		document.aimCurrencyRateFormPop.target = window.opener.name;
		document.aimCurrencyRateFormPop.submit();
		window.close();
	}
	return valid;
}

function load() {
	document.aimCurrencyRateFormPop.updateCRateCode.focus();
}

function unload() {
	window.opener.document.aimCurrencyRateFormPop.currUrl.value = "";
}

function closePopup() {
	window.close();
}

</script>


<digi:form action="/saveCurrencyRate.do" name="aimCurrencyRateFormPop" type="aimCurrencyRateForm">

<html:hidden name="aimCurrencyRateFormPop" property="doAction" value="saveRate"/>

<input type="hidden" name="selectedDate">

<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
	<tr>
		<td align=left valign="top">
			<br>
			<br>
			<br>
			<table cellpadding="0" cellspacing="0" width="100%" class="inside" style="background-color: white; border-width: 1px;">
				<tr style="background-color: #F2F2F2;">
					<td vAlign="center" width="100%" align ="center" height="20" style="font-size: 12px;">
						<digi:trn key="aim:currencyRateEditor">Currency Rate Editor</digi:trn>
					</td>
				</tr>
				<tr>
					<td>
						<br>
						<br>
						<br>
						<br>
					</td>
				</tr>
				<tr>
					<td align="center">
						<table border="0" cellpadding="2" cellspacing="1" width="100%">
							<tr>
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:currencyCode">Currency Code</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<html:select property="updateCRateCode" styleClass="inp-text">
										<html:optionsCollection property="currencyCodes"
										value="currencyCode" label="currencyCode" />&nbsp;&nbsp;&nbsp;
									</html:select>
								</td>
							</tr>
							<tr>
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="admin:currencyRates:editPopup:ExchangeRateDescr">Exchange rate (value of 1 <gs:value name="<%=GlobalSettingsConstants.BASE_CURRENCY %>" /> in selected currency)</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
								<table cellpadding="0" cellspacing="0">
								<tr>
								<td>

                                    <c:set var="formatTip">
                                        <digi:trn key="aim:decimalforma">Format has to be: </digi:trn>
                                        <%=FormatHelper.formatNumber(FormatHelper.parseDouble("1"+FormatHelper.getDecimalSymbol()+"5"))%>
                                    </c:set>

								    <c:set var="codeBase"><%= CurrencyUtil.BASE_CODE %></c:set>
									<c:if test="${aimCurrencyRateFormPop.updateCRateCode==codeBase}">
									<html:text title="${formatTip}" property="updateCRateAmount" disabled="true" styleClass="amt" size="10"/>
									</c:if>
									<c:if test="${aimCurrencyRateFormPop.updateCRateCode!=codeBase}">
									<html:text title="${formatTip}" property="updateCRateAmount" styleClass="amt" size="10"/>
									</c:if>

								</td>
								<td>
								    <p style="padding-left: 5px;">
                                        <digi:trn key="aim:currencyformat">Format: <%=org.digijava.module.aim.util.FeaturesUtil.getGlobalSettingValue(org.digijava.module.aim.helper.GlobalSettingsConstants.NUMBER_FORMAT) %> </digi:trn><br/>
                                        <digi:trn key="aim:groupSymbol">Group Symbol: "<%=FormatHelper.getGroupSymbol()%>"</digi:trn><br/>
                                        <digi:trn key="aim:decimalSymbol">Decimal symbol: "<%=FormatHelper.getDecimalSymbol()%>"</digi:trn>
                                    </p>
								</td>
								</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:exchangeRateDate">Exchange rate date</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<table cellpadding="0" cellspacing="0">
										<tr>
											<td>
												<html:text property="updateCRateDate" size="10" styleClass="inp-text" readonly="true" styleId="updateCRateDate"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
								 				<a id="date1" href='javascript:pickDateById("date1","updateCRateDate")'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
												</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							
							<tr>
								<td colspan="2">
									<table width="100%" cellpadding="3" cellspacing="3" border="0">
										<tr>
											<td align="right">
                                              <c:set var="trnSaveBtn">
                                                <digi:trn key="aim:btnSave">Save</digi:trn>
                                              </c:set>
                                              <input type="button" value="${trnSaveBtn}" onclick="saveRate()" class="buttonx">
											</td>
											<td align="left">
                                              <c:set var="trnCloseBtn">
                                                <digi:trn key="aim:btnClose">Close</digi:trn>
                                              </c:set>
                                              <input type="button" value="${trnCloseBtn}" onclick="closePopup()" class="buttonx">
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;
						  <digi:trn>All fields marked with an </digi:trn><FONT color=red><B><BIG>*</BIG></B></FONT><digi:trn> are required. </digi:trn>
       
					</td>
				</tr>
			</table>
			<br>
			<br>
		</td>
	</tr>
</table>
</digi:form>
