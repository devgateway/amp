<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/calendar.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function addExchangeRate() {
	openNewWindow(420, 180);
	<digi:context name="addExchangeRate" property="context/module/moduleinstance/showAddExchangeRates.do~reset=true" />
  	document.aimCurrencyRateForm.action = "<%= addExchangeRate %>~doAction=showRates";
	document.aimCurrencyRateForm.target = popupPointer.name;
	document.aimCurrencyRateForm.submit();
} 	

function loadExchangeRate() {
	openNewWindow(500, 300);
	<digi:context name="loadExchangeRate" property="context/module/moduleinstance/loadCurrencyRates.do" />
  	document.aimCurrencyRateForm.action = "<%= loadExchangeRate %>";
	document.aimCurrencyRateForm.target = popupPointer.name;
	document.aimCurrencyRateForm.submit();
} 	

function editExchangeRate(date,code) {
	openNewWindow(420, 180);
	<digi:context name="addExchangeRate" property="context/module/moduleinstance/showAddExchangeRates.do~reset=true" />
  	document.aimCurrencyRateForm.action = "<%= addExchangeRate %>~doAction=showRates~updateCRateCode="+code+"~updateCRateDate="+date+"~reset=false";
	document.aimCurrencyRateForm.target = popupPointer.name;
	document.aimCurrencyRateForm.submit();
}

function checkall() {
	var selectbox = document.aimCurrencyRateForm.checkAll;
	var items = document.aimCurrencyRateForm.selectedRates;
	if (document.aimCurrencyRateForm.selectedRates.checked == true || 
						 document.aimCurrencyRateForm.selectedRates.checked == false) {
			  document.aimCurrencyRateForm.selectedRates.checked = selectbox.checked;
	} else {
		for(i=0; i<items.length; i++){
			document.aimCurrencyRateForm.selectedRates[i].checked = selectbox.checked;
		}
	}
}

function deleteRates() {
	document.aimCurrencyRateForm.doAction.value = "delete";
	document.aimCurrencyRateForm.target = "_self";
	document.aimCurrencyRateForm.submit();
}

function selectFile() {
	document.aimCurrencyRateForm.ratesFile.value = document.aimCurrencyRateForm.file.value;
	document.aimCurrencyRateForm.doAction.value = "loadRates";
	document.aimCurrencyRateForm.target = "_self";
	document.aimCurrencyRateForm.submit();
}

</script>

<digi:errors/>
<digi:instance property="aimCurrencyRateForm" />

<digi:form action="/showCurrencyRates.do" enctype="multipart/form-data">

<input type="hidden" name="selectedDate">
<html:hidden property="updateCRateId"/>
<html:hidden property="doAction"/>
<html:hidden property="ratesFile"/>

<table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left">
<tr><td>
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
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
					<td><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:link href="/currencyManager.do" styleClass="comment" title="Click here to view currency manager">
						<digi:trn key="aim:currencyManager">
						Currency Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:exchangeRates">
						Exchange Rates
						</digi:trn>	
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:exchangeRatesInUSDollars">
							Exchange Rates in US Dollars(USD)
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
						<table width="100%" cellspacing="2" cellPadding="2" vAlign="top" align="left">
							<tr><td>
								<!-- Filters -->

								<table cellPadding=1 cellSpacing=1 align="left" bgcolor="#dddddd" width="600">
								<tr><td bgcolor="#f4f4f2">
								<table cellPadding=0 cellSpacing=2 align="left" width="600" border=0>
									<tr>
										<td bgcolor="#f4f4f2" vAlign="center">
											<b><digi:trn key="aim:filterBy">Filter By</digi:trn>:</b>
										</td>
										<td bgcolor="#f4f4f2" vAlign="center">
											<digi:trn key="aim:currencyCode">Currency Code</digi:trn>
										</td>
										<td bgcolor="#f4f4f2" vAlign="center">
											<html:select property="filterByCurrCode" styleClass="inp-text">
												<html:option value="">-- All --</html:option>
												<html:optionsCollection name="aimCurrencyRateForm" property="currencyCodes" 
												value="currencyCode" label="currencyCode" />&nbsp;&nbsp;&nbsp;
											</html:select>
										</td>
										<td bgcolor="#f4f4f2" vAlign="center">
											<digi:trn key="aim:ratesOfDate">Rates of date</digi:trn>
										</td>
										<%--
										<td bgcolor="#f4f4f2" vAlign="center">
											<digi:trn key="aim:dateFrom">From</digi:trn>
										</td>
										--%>
										<td bgcolor="#f4f4f2" vAlign="center">
											<table cellPadding=0 cellSpacing=0>
												<tr>
													<td>
														<html:text property="filterByDateFrom" size="10" 
														styleId="filterByDateFrom" styleClass="inp-text"/>
													</td>
													<td align="left" vAlign="center">&nbsp;
														<a href="javascript:calendar('filterByDateFrom')">
														<img src="../ampTemplate/images/show-calendar.gif" border=0></a>
													</td>
												</tr>
											</table>
										</td>
										<td bgcolor="#f4f4f2" vAlign="center">
											<digi:trn key="aim:andPrev7Days">and previous 7 days</digi:trn>
										</td>
										<td bgcolor="#f4f4f2" vAlign="center">
											<html:submit value=" Go " styleClass="buton"/>
										</td>	
									</tr>
								</table>								
								</td></tr>
								<tr><td bgcolor="#f4f4f2">
								<table cellPadding=0 cellSpacing=2 align="left" border=0 vAlign="center">
									<tr>
										<td bgcolor="#f4f4f2" vAlign="left" width="170" align="center">
											<digi:trn key="aim:numRecordsPerPage">Number of records per page</digi:trn>:
										</td>
										<td bgcolor="#f4f4f2" vAlign="left" width="50" align="center">
											<html:text property="numResultsPerPage" size="3" styleClass="inp-text"/>
										</td>											
										<td bgcolor="#f4f4f2" vAlign="left" align="center">
											<html:submit value=" View " styleClass="buton"/>
										</td>	
									</tr>
								</table>								
								</td></tr>
								
								</table>
								

							</td></tr>
							<tr><td bgcolor="#ffffff" valign="top" align="left">
								<!-- Exchange rates table -->
								<table cellSpacing="1" cellPadding="2" vAlign="top" align="left" bgcolor="#aaaaaa" width="450">
									<tr bgcolor="eeeeee">
										<td align="center" width="3">
											<input type="checkbox" name="checkAll" onclick="checkall()">
										</td>									
										<td align="center" width="40" onMouseOver="this.className='colHeaderOver'"
										onMouseOut="this.className='colHeaderLink'">
											<b><digi:trn key="aim:currCode">Code</digi:trn></b>
										</td>
										<td align="center" width="200" onMouseOver="this.className='colHeaderOver'"
										onMouseOut="this.className='colHeaderLink'">
											<b><digi:trn key="aim:currencyName">Currency Name</digi:trn></b>
										</td>
										<td align="center" width="80" onMouseOver="this.className='colHeaderOver'"
										onMouseOut="this.className='colHeaderLink'">
											<b><digi:trn key="aim:exchangeRateDate">Date</digi:trn></b>
										</td>
										<td align="center" onMouseOver="this.className='colHeaderOver'"
										onMouseOut="this.className='colHeaderLink'">
											<b><digi:trn key="aim:exchangeRate">Rate</digi:trn></b>
										</td>
									</tr>
									<c:if test="${empty aimCurrencyRateForm.currencyRates}">
									<tr bgcolor="#f4f4f2">
										<td colspan="5" align="center">
											<digi:trn key="aim:noCurrencyRates">No currency rates</digi:trn>
										</td>
									</tr>
									</c:if>
									<% int index = 0; %>
									<c:if test="${!empty aimCurrencyRateForm.currencyRates}">
									<c:forEach var="cRates" items="${aimCurrencyRateForm.currencyRates}">
									<%
									if (index%2 == 0) { %>
									<tr class="rowNormal">
									<% } else { %>
									<tr class="rowAlternate">
									<% } 
									index++;%>
										<td align="center" width="3">
											<html:multibox property="selectedRates">
												<c:out value="${cRates.id}"/>
											</html:multibox>
										</td>									
										<td align="left">
											<c:out value="${cRates.currencyCode}"/>
										</td>
										<td align="left">
											<c:out value="${cRates.currencyName}"/>
										</td>
										<td align="center">
											<a href="javascript:editExchangeRate('<c:out value="${cRates.exchangeRateDate}"/>','<c:out value="${cRates.currencyCode}"/>')">
											<c:out value="${cRates.exchangeRateDate}"/>
											</a>
										</td>
										<td align="right">
											<c:out value="${cRates.exchangeRate}"/>
										</td>										
									</tr>
									</c:forEach>
									</c:if>									
								</table>
							</td></tr>
							<tr><td>
								<input type="button" value="Delete Selected Rates" class="buton" onclick="deleteRates()">
							</td></tr>
							<c:if test="${!empty aimCurrencyRateForm.pages}">
							<tr><td>
								<!-- Pagination -->
								<table width="460" cellSpacing="1" cellPadding="2" vAlign="top" align="left">
									<tr>
										<td>
											<bean:size name="aimCurrencyRateForm" property="pages" id="totpages"/>
											Page <u><c:out value="${aimCurrencyRateForm.currentPage}"/></u> of 
											<u><c:out value="${totpages}"/></u>
										</td>
									</tr>
									<tr>
										<td>
											Pages :
											<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
											<c:forEach var="currPage" items="${aimCurrencyRateForm.pages}">
												<c:if test="${currPage == aimCurrencyRateForm.currentPage}">
													<c:out value="${currPage}"/>
												</c:if>
												<c:if test="${currPage != aimCurrencyRateForm.currentPage}">
													<c:set target="${urlParams}" property="page">
														<c:out value="${currPage}"/>
													</c:set>																							
													<digi:link href="/showCurrencyRates.do" name="urlParams">
													<c:out value="${currPage}"/></digi:link> 
												</c:if>												
												| 
											</c:forEach>
										</td>
									</tr>
								</table>
							</td></tr>
							</c:if>
							<tr><td>
								<table width="450" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
									<tr>
										<td align="left">
											<a href="javascript:addExchangeRate()">
											Add new exchange rate</a>
										</td>
									</tr>
								</table>
							</td></tr>
							<%--
							<tr><td bgcolor="#ffffff">
								<table width="450" cellSpacing="1" cellPadding="3" vAlign="top" align="left" bgcolor="#aaaaaa">
									<tr bgcolor="#eeeeee">
										<td align="center" colspan="2">
											<b>
											Load currency rates from a CSV file</b>
										</td>										
									</tr>								
									<tr bgcolor="#ffffff">
										<td align="center">
											<table width="350" cellSpacing="3" cellPadding="1" vAlign="top" align="center">
												<tr>
													<td align="left" width="320">
														<html:file name="aimCurrencyRateForm" property="ratesFile" 
														size="40" styleClass="dr-menu"/>
														<input type="file" name="file" size="50" class="dr-menu">
													</td>
													<td align="left">
														<input type="button" value="Load" class="buton" onclick="selectFile()">
													</td>										
												</tr>
											</table>										
										</td>										
									</tr>
								</table>
							</td></tr>							
							--%>
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
