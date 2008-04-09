<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@page import="org.digijava.module.aim.helper.FormatHelper"%>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="aimCurrencyRateForm" />
<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function validate() {
	if (isEmpty(document.aimCurrencyRateForm.updateCRateCode.value) == true) {
		alert("Currency code not entered");
		document.aimCurrencyRateForm.updateCRateCode.focus();
		return false;
	}
	if (document.aimCurrencyRateForm.updateCRateCode.value == 'USD') {
		alert("All exchange rates are saved in terms of USD. Please select a different currency.");
		document.aimCurrencyRateForm.updateCRateCode.focus();
		return false;
	}

	if (isEmpty(document.aimCurrencyRateForm.updateCRateDate.value) == true) {
		alert("Exchange rate date not entered");
		document.aimCurrencyRateForm.updateCRateDate.focus();
		return false;
	}
	if (isEmpty(document.aimCurrencyRateForm.updateCRateAmount.value) == true) {
		alert("Exchange rate not entered");
		document.aimCurrencyRateForm.updateCRateAmount.focus();
		return false;
	}
	
	if (checkAmountUsingSymbols(document.aimCurrencyRateForm.updateCRateAmount.value,'<%=FormatHelper.getGroupSymbol()%>','<%=FormatHelper.getDecimalSymbol()%>') == false) 
		{
			alert("Invalid exchange rate entered");
			document.aimCurrencyRateForm.updateCRateAmount.focus();
			return false;
		}
	
	return true;
}

function saveRate() {
	var valid = validate();
	if (valid == true) {
		document.aimCurrencyRateForm.target = window.opener.name;
		document.aimCurrencyRateForm.submit();
		window.close();
	}
	return valid;
}

function load() {
	document.aimCurrencyRateForm.updateCRateCode.focus();
}

function unload() {
	window.opener.document.aimCurrencyRateForm.currUrl.value = "";
}

function closePopup() {
	window.close();
}

</script>


<digi:form action="/saveCurrencyRate.do">

<html:hidden name="aimCurrencyRateForm" property="doAction" value="saveRate"/>

<input type="hidden" name="selectedDate">

<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
	<tr>
		<td align=left vAlign=top>
			<table bgcolor=#aaaaaa cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
				<tr bgcolor="#aaaaaa">
					<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
						<digi:trn key="aim:currencyRateEditor">Currency Rate Editor</digi:trn>
					</td></tr>
				<tr>
					<td align="center">
						<table border="0" cellpadding="2" cellspacing="1" width="100%">
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:currencyCode">Currency Code</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<html:select property="updateCRateCode" styleClass="inp-text">
										<html:optionsCollection name="aimCurrencyRateForm" property="currencyCodes"
										value="currencyCode" label="currencyCode" />&nbsp;&nbsp;&nbsp;
									</html:select>
								</td>
							</tr>
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:exchangeRateDate">Exchange rate date</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<table cellPadding=0 cellSpacing=0>
										<tr>
											<td>
												<html:text property="updateCRateDate" size="10" styleClass="inp-text" readonly="true" styleId="updateCRateDate"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
								 				<a id="date1" href='javascript:pickDateCurrency("date1",document.aimCurrencyRateForm.updateCRateDate)'>
													<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border=0>
												</a>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="admin:currencyRates:editPopup:ExchangeRateDescr">Exchange rate (value of 1 USD in selected currency)</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
								<c:set var="formatTip">
														<digi:trn key="aim:decimalforma">Format has to be: </digi:trn> <%=FormatHelper.formatNumber(FormatHelper.parseDouble("1"+FormatHelper.getDecimalSymbol()+"5"))%>
								</c:set>
								
									<html:text title="${formatTip}" property="updateCRateAmount" styleClass="amt" size="7"/>
									<!-- 
									<FONT color=red>
									<digi:trn key="aim:USD">USD</digi:trn></FONT> -->
								</td>
							</tr>
							<tr bgcolor="#ffffff">
								<td colspan="2">
									<table width="100%" cellpadding="3" cellspacing="3" border="0">
										<tr>
											<td align="right">
                                              <c:set var="trnSaveBtn">
                                                <digi:trn key="aim:btnSave">Save</digi:trn>
                                              </c:set>
                                              <input type="button" value="${trnSaveBtn}" onclick="saveRate()" style="dr-menu">
											</td>
											<td align="left">
                                              <c:set var="trnCloseBtn">
                                                <digi:trn key="aim:btnClose">Close</digi:trn>
                                              </c:set>
                                              <input type="button" value="${trnCloseBtn}" onclick="closePopup()" style="dr-menu">
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
						<digi:trn key="um:allMarkedRequiredField">All fields marked with an <FONT color=red><B><BIG>*</BIG>
						</B></FONT> are required.</digi:trn>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>
