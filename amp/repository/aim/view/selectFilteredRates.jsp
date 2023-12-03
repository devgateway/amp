<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<%@page import="org.digijava.ampModule.aim.action.SelectFilteredCurrencyRates"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>

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

<bean:define id="myForm" name="aimSelectFilteredCurrencyRatesForm" toScope="page" type="org.digijava.ampModule.aim.form.SelectFilteredCurrencyRatesForm" />

<digi:form action="/selectFilteredRates.do">
<h1 class="admintitle">Select Filtered Currency rates</h1>
<table>
	<tr>
		<td>
			<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=1000>
			<table cellPadding=5 cellSpacing=3 width="100%">
				<tr>
					<!-- Start Navigation -->
					<!--<td colspan="3"><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn >
						Select Filtered Currency rates
						</digi:trn>
						</span>
					</td>-->
					<!-- End navigation -->
				</tr>
				<tr>
					<td colspan="2" height=25 align="center" width="100%" bgcolor="#c7d4db">
						<span class=subtitle-blue> 
							<b><digi:trn>
								Select Filtered Currency Rates
							</digi:trn></b>
						</span>
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
						<table width="100%" cellspacing="2" cellPadding="2" align="left">
							<tr>
								<td valign="top" width="100%">
									<table width="100%">
										<tr>
											<td>
												
										<table cellSpacing="1" cellPadding="4" align="left" width="100%" class="inside">
											<thead>
															<tr bgcolor="eeeeee">
																<td align="left" class="inside" width="10px">
																	<b><digi:trn>
																		Selection
																	</digi:trn></b>
																</td>
																<td align="left" class="inside" >
																	<b><digi:trn>
																		To Currency
																	</digi:trn></b>
																</td>
																<td align="left" class="inside">
																	<b><digi:trn>
																		From Currency
																	</digi:trn></b>
																</td>
															</tr>
														</thead>
														<logic:iterate id="filteredRate" name="myForm" property="existingFilteredRates">
															<tr>
																<td align="center" class="inside">	<html:multibox property="selectedFilteredRates"  value="${filteredRate.id}"/> </td>
																<td align="left" class="inside">   ${filteredRate.toCurrency.currencyName }  (${filteredRate.toCurrency.currencyCode}) &nbsp; &nbsp; </td>
																<td align="left" class="inside">   ${filteredRate.fromCurrency.currencyName }  (${filteredRate.fromCurrency.currencyCode})</td>
															</tr>
														</logic:iterate>
												</table>
												
													
											</td>
										</tr>
										<tr>
											<td>
												<c:set var="trnBtn">
													<digi:trn> Remove Selected Filtered Rates </digi:trn>
												</c:set>
												<input class="buttonx" type="button" onclick="return deleteSelection();" value="${trnBtn}" />
											</td>
										</tr>
										<tr>
										  <td><table width="100%">
										<tr>
											<td width="100%">
												<table cellSpacing="1" cellPadding="4" align="left" width="100%" class="inside">
											<thead>
												<tr bgcolor="eeeeee">
													<td align="left" class="inside" width="100%" colspan="2">
														<b><digi:trn>
															Add New Filtered Rate
														</digi:trn></b>
													</td>
												</tr>
											</thead>
											<tr> 
												<td align="left" class="inside"><digi:trn>To Rate</digi:trn>:</td> 
												<td align="center" class="inside">
													<html:select property="toCurrencyCode">
														<html:optionsCollection property="existingCurrencies" label="value" value="key" styleClass="inp-text" />
													</html:select>
												</td>
											</tr>
											<tr>
												<td align="left" class="inside"><digi:trn>From Rate</digi:trn>:</td> 
												<td align="center" class="inside">
													<html:select property="fromCurrencyCode">
														<html:optionsCollection property="existingCurrencies" label="value" value="key"  styleClass="inp-text"/>
													</html:select>
												</td>
											</tr>
										</table>
										
											</td>
										</tr>
										<tr>
											<td width="100%">
												<c:set var="trnAddBtn">
													<digi:trn>Add</digi:trn>
												</c:set>
												<input class="buttonx" type="button" onclick="return addFilteredRate();" value="${trnAddBtn}" id="addFilteredRateBtn"/>	
											</td>
										</tr>
									</table></td>
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
</td>

<!-- other links -->
<td valign="top">
			<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%" >
				<thead>
												<tr>
													<td align="left" width="100%" colspan="2">
													
													<table width="120" cellspacing="0" cellpadding="0">
														<tbody><tr>
															<td bgcolor="#c9c9c7" class="box-title"><b style="font-size: 12px; padding-left: 5px;"><b><digi:trn>
															Other Links
														</digi:trn></b></b>
															</td>
															<td width="17" height="17" background="ampModule/aim/images/corner-r.gif"></td>
														</tr>
													</tbody></table>
													
													
													
													
													
														
													</td>
												</tr>
											</thead>
												
												<tr>
													<td bgColor="#ffffff" class="box-border">
													<table cellPadding=5 cellSpacing=1 width="100%" class="inside">
														<tr>
															<td class="inside"><digi:img src="ampModule/aim/images/arrow-014E86.gif"
																width="15" height="10" /> <c:set var="translation">
																<digi:trn >Click here to go back to admin home page</digi:trn>
															</c:set> <digi:link href="/admin.do"
																title="${translation}">
																<digi:trn>Admin Home</digi:trn>
															</digi:link></td>
														</tr>
														<tr>
															<td class="inside"><digi:img src="ampModule/aim/images/arrow-014E86.gif"
																width="15" height="10" /> 
															<c:set var="translation">
																<digi:trn>Click here to go to the Currency Manager</digi:trn>
															</c:set> 
															<digi:link href="/currencyManager.do" title="${translation}">
																	<digi:trn>Currency Manager</digi:trn>
															</digi:link></td>
														</tr>
														<tr>
															<td class="inside"><digi:img src="ampModule/aim/images/arrow-014E86.gif"
																width="15" height="10" /> 
															<c:set var="translation">
																<digi:trn>Click here to go to the Currency Rate Manager</digi:trn>
															</c:set> 
															<digi:link href="/showCurrencyRates.do~clean=true~timePeriod=1" title="${translation}">
																	<digi:trn>Currency Rate Manager</digi:trn>
															</digi:link></td>
														</tr>
		
														<!-- end of other links -->
													</table>
													</td>
												</tr>
											</table>
		</td>
	</tr>
</table> 
 
 </digi:form>


