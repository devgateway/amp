<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<%@page import="org.digijava.module.aim.action.SelectFilteredCurrencyRates"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript" />
	function deleteSelection () {
		var myForm			= document.forms['aimSelectFilteredCurrencyRatesForm'];
		myForm.action		= "/aim/selectFilteredRates.do?<%=SelectFilteredCurrencyRates.ACTION_PARAMETER %>=<%=SelectFilteredCurrencyRates.ACTION_DELETE %>";
		
		if ( (myForm.selectedFilteredRates.checked != null &&  myForm.selectedFilteredRates.checked == true) || 
				( myForm.selectedFilteredRates.length != null && myForm.selectedFilteredRates.length > 0 )  ) {
				myForm.submit();		
		}
		else {
			alert ("<digi:trn>You need to select at least one filtered rate</digi:trn>");
			return false;
		}
	}
	
	function addFilteredRate() {
		var myForm			= document.forms['aimSelectFilteredCurrencyRatesForm'];
		myForm.action		= "/aim/selectFilteredRates.do?<%=SelectFilteredCurrencyRates.ACTION_PARAMETER %>=<%=SelectFilteredCurrencyRates.ACTION_ADD %>";
		myForm.submit();
	}
	
	var enterBinder	= new EnterHitBinder('addFilteredRateBtn');

</script>




<digi:instance property="aimSelectFilteredCurrencyRatesForm" />

<bean:define id="myForm" name="aimSelectFilteredCurrencyRatesForm" toScope="page" type="org.digijava.module.aim.form.SelectFilteredCurrencyRatesForm" />

<digi:form action="/selectFilteredRates.do">




<table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left">
<tr><td>
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
</td></tr>
<tr><td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=3 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td colspan="3"><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn >
						Select Filtered Currency rates
						</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td colspan="3" height="16" vAlign="center" width="571"><span class="subtitle-blue">
						<digi:trn>
							Select Filtered Currency Rates
						</digi:trn></span>
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
						<table width="100%" cellspacing="2" cellPadding="2" vAlign="top" align="left">
							<tr>
								<td valign="top">
											<logic:notEmpty name="myForm" property="existingFilteredRates">
												<table border="0px" cellpadding="1px" >
													<tr align="center">
														<th bgcolor="#006699" class="textalb" align="center">
															<digi:trn>
																Selection
															</digi:trn>
														</th>
														<th bgcolor="#006699" class="textalb" align="center">
															<digi:trn>
																To Currency
															</digi:trn>
														</th>
														<th bgcolor="#006699" class="textalb" align="center">
															<digi:trn>
																From Currency
															</digi:trn>
														</th>
													</tr>
													<logic:iterate id="filteredRate" name="myForm" property="existingFilteredRates">
														<tr>
														<td valign="top">	<html:multibox property="selectedFilteredRates"  value="${filteredRate.id}"/> </td>
														<td valign="top">   ${filteredRate.toCurrency.currencyName }  (${filteredRate.toCurrency.currencyCode}) &nbsp; &nbsp; </td>
														<td valign="top">   ${filteredRate.fromCurrency.currencyName }  (${filteredRate.fromCurrency.currencyCode})</td>
														</tr>
													</logic:iterate>
												</table>
												<button type="button" onclick="return deleteSelection();" class="buton" > <digi:trn>Remove Selected Filtered Rates</digi:trn> </button>
											</logic:notEmpty>
								</td>
								<td valign="top">
									<table border="0px"  cellpadding="1px">
													<tr align="center">
														<th bgcolor="#006699" class="textalb" align="center"  colspan="2" >
															<digi:trn>
																Add New Filtered Rate
															</digi:trn>
														</th>
													</tr>
													<tr> 
														<td><digi:trn>To Rate</digi:trn>:</td> 
														<td>
															<html:select property="toCurrencyCode">
																<html:optionsCollection property="existingCurrencies" label="value" value="key" styleClass="inp-text" />
															</html:select>
														</td>
													</tr>
													<tr>
														<td><digi:trn>From Rate</digi:trn>:</td> 
														<td>
															<html:select property="fromCurrencyCode">
																<html:optionsCollection property="existingCurrencies" label="value" value="key"  styleClass="inp-text"/>
															</html:select>
														</td>
													</tr>
										</table>
										<button type="button" onclick="return addFilteredRate();" class="buton" id="addFilteredRateBtn"> <digi:trn>Add</digi:trn> </button>
								</td>
								<td valign="top">
											<table align=center cellPadding=0 cellSpacing=0 width="90%"
												border=0>
												<tr>
													<td><!-- Other Links -->
													<table cellPadding=0 cellSpacing=0 width=100>
														<tr>
															<td bgColor=#c9c9c7 class=box-title><digi:trn
																key="aim:otherLinks">
															Other links
															</digi:trn></td>
															<td background="module/aim/images/corner-r.gif" height="17"
																width=17>&nbsp;</td>
														</tr>
													</table>
													</td>
												</tr>
												<tr>
													<td bgColor="#ffffff" class="box-border">
													<table cellPadding=5 cellSpacing=1 width="100%">
														<tr>
															<td><digi:img src="module/aim/images/arrow-014E86.gif"
																width="15" height="10" /> <c:set var="translation">
																<digi:trn >Click here to go back to admin home page</digi:trn>
															</c:set> <digi:link href="/admin.do"
																title="${translation}">
																<digi:trn>Admin Home</digi:trn>
															</digi:link></td>
														</tr>
														<tr>
															<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif"
																		width="15" height="10" /> 
																	<c:set var="translation">
																		<digi:trn>Click here to go to the Currency Manager</digi:trn>
																	</c:set> 
																	<digi:link href="/currencyManager.do" title="${translation}">
																			<digi:trn>Currency Manager</digi:trn>
																	</digi:link>
															</td>
														</tr>
														<tr>
															<td>
																	<digi:img src="module/aim/images/arrow-014E86.gif"
																		width="15" height="10" /> 
																	<c:set var="translation">
																		<digi:trn>Click here to go to the Currency Rate Manager</digi:trn>
																	</c:set> 
																	<digi:link href="/showCurrencyRates.do~clean=true~timePeriod=1" title="${translation}">
																			<digi:trn>Currency Rate Manager</digi:trn>
																	</digi:link>
															</td>
														</tr>
		
														<!-- end of other links -->
													</table>
													</td>
												</tr>
											</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>



