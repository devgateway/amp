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
</script>




<digi:instance property="aimSelectFilteredCurrencyRatesForm" />

<bean:define id="myForm" name="aimSelectFilteredCurrencyRatesForm" toScope="page" type="org.digijava.module.aim.form.SelectFilteredCurrencyRatesForm" />

<digi:form action="/selectFilteredRates.do">




<table width="100%" cellspacing="0" cellpadding="0" valign="top" align="left">
<tr><td>
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align="center">
	<tr>
		<td align=left class=r-dotted-lg valign="top" width=750>
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
				<!--<tr>
					<td colspan="3" height="16" vAlign="center" width="571"><span class="subtitle-blue">
						<digi:trn>
							Select Filtered Currency Rates
						</digi:trn></span>
						<digi:errors/>
					</td>
				</tr>-->
				<tr>
					<td noWrap width="100%" vAlign="top">
						<table width="100%" cellspacing="0" cellPadding="0" vAlign="top" align="left">
							<tr>
								
								<td valign="top" width=750>
									<table width=100% cellpadding="0" cellspacing="0" class="inside" style="font-size:12px;">
													<tr align="center">
														<td bgcolor="#c7d4db" height=25 class="inside" align="center"  colspan="2" >
															<digi:trn>
																<b>Add New Filtered Rate</b>															</digi:trn>														</td>
													</tr>
													<tr> 
														<td class="inside" align=right><digi:trn>To Rate</digi:trn>:</td> 
														<td>
															<html:select property="toCurrencyCode">
																<html:optionsCollection property="existingCurrencies" label="value" value="key" styleClass="inp-text" />
															</html:select>														</td>
													</tr>
													<tr>
														<td class="inside" align=right><digi:trn>From Rate</digi:trn>:</td> 
														<td class="inside">
															<html:select property="fromCurrencyCode">
																<html:optionsCollection property="existingCurrencies" label="value" value="key"  styleClass="inp-text"/>
															</html:select>														</td>
													</tr>
													<tr>
													  <td class="inside" colspan="2" align=center><button type="button" onclick="return addFilteredRate();" class="buttonx" > <digi:trn>Add</digi:trn> </button></td>
									  </tr>
										</table>								</td>
								<td valign="top">
											<table align="center" cellpadding="0" cellspacing="0" width="90%" border="0">
												<tr>
													<td><!-- Other Links -->
													<table cellpadding="0" cellspacing="0" width="100" style="font-size:12px;">
														<tr>
															<td bgColor=#c9c9c7 class=box-title><digi:trn
																key="aim:otherLinks">
															<b style="padding-left:5px;">Other links</b>
															</digi:trn></td>
															<td background="module/aim/images/corner-r.gif" height="17"
																width=17>&nbsp;</td>
														</tr>
													</table>													</td>
												</tr>
												<tr>
													<td bgColor="#ffffff" class="box-border">
													<table cellPadding=5 cellspacing="1" width="100%" class="inside">
														<tr>
															<td class="inside"><digi:img src="module/aim/images/arrow-014E86.gif"
																width="15" height="10" /> <c:set var="translation">
																<digi:trn >Click here to go back to admin home page</digi:trn>
															</c:set> <digi:link href="/admin.do"
																title="${translation}">
																<digi:trn>Admin Home</digi:trn>
															</digi:link></td>
														</tr>
														<tr>
															<td class="inside">
																	<digi:img src="module/aim/images/arrow-014E86.gif"
																		width="15" height="10" /> 
																	<c:set var="translation">
																		<digi:trn>Click here to go to the Currency Manager</digi:trn>
																	</c:set> 
																	<digi:link href="/currencyManager.do" title="${translation}">
																			<digi:trn>Currency Manager</digi:trn>
																	</digi:link>															</td>
														</tr>
														<tr>
															<td class="inside">
																	<digi:img src="module/aim/images/arrow-014E86.gif"
																		width="15" height="10" /> 
																	<c:set var="translation">
																		<digi:trn>Click here to go to the Currency Rate Manager</digi:trn>
																	</c:set> 
																	<digi:link href="/showCurrencyRates.do~clean=true~timePeriod=1" title="${translation}">
																			<digi:trn>Currency Rate Manager</digi:trn>
																	</digi:link>															</td>
														</tr>
		
														<!-- end of other links -->
													</table>													</td>
												</tr>
											</table>								</td>
							</tr>
							<tr>
							  
							  <td valign="top">	<logic:notEmpty name="myForm" property="existingFilteredRates">
												<table cellpadding="0" cellspacing="0" class="inside" style="font-size:12px; margin-top:15px;" width=100%>
													<tr align="center">
														<td bgcolor="#c7d4db" class="inside" align="center" height=25 width=50>
															<digi:trn>
																<b>Selection</b>															</digi:trn>														</td>
														<td bgcolor="#c7d4db" class="inside" align="center">
															<digi:trn>
																<b>To Currency</b>															</digi:trn>														</td>
														<td bgcolor="#c7d4db" class="inside" align="center">
															<digi:trn>
																<b><b>From Currency</b></b>															</digi:trn>														</td>
													</tr>
													<logic:iterate id="filteredRate" name="myForm" property="existingFilteredRates">
														<tr>
														<td valign="top" class="inside" align=center>	<html:multibox property="selectedFilteredRates"  value="${filteredRate.id}"/> </td>
														<td valign="top" class="inside">   ${filteredRate.toCurrency.currencyName }  (${filteredRate.toCurrency.currencyCode}) &nbsp; &nbsp; </td>
														<td valign="top" class="inside">   ${filteredRate.fromCurrency.currencyName }  (${filteredRate.fromCurrency.currencyCode})</td>
														</tr>
														<tr>
														  <td colspan="3" valign="top" class="inside" align=center style="padding-top:7px; padding-bottom:7px;"><button type="button" onclick="return deleteSelection();" class="buttonx" > <digi:trn>Remove Selected Filtered Rates</digi:trn> </button></td>
													  </tr>
													</logic:iterate>
												</table>
											</logic:notEmpty>								</td>
							  <td valign="top">&nbsp;</td>
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



