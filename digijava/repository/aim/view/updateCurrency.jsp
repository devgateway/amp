<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<digi:instance property="aimCurrencyForm" />

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function validate() {
	if (isEmpty(document.aimCurrencyForm.currencyCode.value) == true) {
		alert("Currency code not entered");
		document.aimCurrencyForm.currencyCode.focus();
		return false;
	}
	if (isEmpty(document.aimCurrencyForm.currencyName.value) == true) {
		alert("Currency name not entered");
		document.aimCurrencyForm.currencyName.focus();
		return false;
	}
	if (isEmpty(document.aimCurrencyForm.countryName.value) == true) {
		alert("Country name not entered");
		document.aimCurrencyForm.countryName.focus();
		return false;
	}
	if (document.aimCurrencyForm.id.value == -1) {
		if (isEmpty(document.aimCurrencyForm.exchangeRateDate.value) == true) {
			alert("Exchange rate date not entered");
			document.aimCurrencyForm.exchangeRateDate.focus();
			return false;
		}			  
		if (isEmpty(document.aimCurrencyForm.exchangeRate.value) == true) {
			alert("Exchange rate not entered");
			document.aimCurrencyForm.exchangeRate.focus();
			return false;
		}			  		
		if (checkAmount(document.aimCurrencyForm.exchangeRate.value) == false) {
			alert("Invalid exchange rate entered");
			document.aimCurrencyForm.exchangeRate.focus();					  
			return false;
		}		
	}
	return true;
}

function saveCurrency() {
	var valid = validate();
	if (valid != false) {
		document.aimCurrencyForm.target = window.opener.name;
		document.aimCurrencyForm.submit();
		window.close();			  
	}
	return valid;

}

function closePopup() {
	<digi:context name="back" property="context/module/moduleinstance/currencyManager.do" />
	document.aimCurrencyForm.action = "<%= back %>";
	document.aimCurrencyForm.target = window.opener.name;
	document.aimCurrencyForm.submit();
	window.close();
}

function load() {
	document.aimCurrencyForm.currencyCode.focus();
}

function unload() {
}

</script>


<digi:form action="/saveCurrency.do">

<input type="hidden" name="selectedDate">
<html:hidden property="doAction" value="updateCurrency"/>
<html:hidden property="id"/>

<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
	<tr>
		<td align=left vAlign=top>
			<table bgcolor=#aaaaaa cellPadding=0 cellSpacing=0 width="100%" class=box-border-nopadding>
				<tr bgcolor="#aaaaaa">
					<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
						<digi:trn key="aim:currencyRateEditor">Currency Editor</digi:trn>
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
									<html:text property="currencyCode" styleClass="inp-text" size="7"/>
								</td>								
							</tr>
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:currencyName">Currency Name</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<html:text property="currencyName" styleClass="inp-text" size="20"/>
								</td>								
							</tr>
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:countryName">Country Name</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<html:select property="countryName" styleClass="inp-text">
										<html:option value="">Select a country</html:option>
										<html:optionsCollection name="aimCurrencyForm" property="countries" 
										value="name" label="name" />&nbsp;&nbsp;&nbsp;
									</html:select>								
								</td>								
							</tr>
							<c:if test="${aimCurrencyForm.id == -1}">
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:exchangeRateDate">Exchange rate date</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<table cellPadding=0 cellSpacing=0>
										<tr>
											<td>
												<html:text property="exchangeRateDate" size="10" 
												styleClass="inp-text" readonly="true" styleId="exchangeRateDate"/>
											</td>
											<td align="left" vAlign="center">&nbsp;
												<a href="javascript:calendar('exchangeRateDate')">
												<img src="../ampTemplate/images/show-calendar.gif" border=0></a>
											</td>
										</tr>
									</table>
								</td>								
							</tr>
							<tr bgcolor="#f4f4f2">
								<td align="right" valign="middle" width="50%">
									<FONT color=red>*</FONT>
									<digi:trn key="aim:exchangeRateInUSD">Exchange rate (in USD)</digi:trn>&nbsp;
								</td>
								<td align="left" valign="middle">
									<html:text property="exchangeRate" styleClass="amt" size="7"/>
									<FONT color=red>
									<digi:trn key="aim:USD">USD</digi:trn></FONT>
								</td>								
							</tr>							
							</c:if>
							<tr bgcolor="#ffffff">
								<td colspan="2">
									<table width="100%" cellpadding="3" cellspacing="3" border="0">
										<tr>
											<td align="right">
												<input type="button" value="Save" onclick="return saveCurrency()" class="dr-menu">
											</td>
											<td align="left">
												<input type="button" value="Close" onclick="return closePopup()" class="dr-menu">
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
