<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>



<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">

function makeInactive(code) {
	<digi:context name="changeStatus" property="context/module/moduleinstance/changeCurrencyStatus.do" />
	document.aimCurrencyForm.action = "<%= changeStatus %>~currCode="+code+"~status=0";
	document.aimCurrencyForm.target = "_self";
	document.aimCurrencyForm.submit();
}

function makeActive(code) {
	<digi:context name="changeStatus" property="context/module/moduleinstance/changeCurrencyStatus.do" />
	document.aimCurrencyForm.action = "<%= changeStatus %>~currCode="+code+"~status=1";
	document.aimCurrencyForm.target = "_self";
	document.aimCurrencyForm.submit();
}

function addNewCurrency() {
	openNewWindow(450, 230);
	<digi:context name="add" property="context/module/moduleinstance/updateCurrency.do" />
	document.aimCurrencyForm.action = "<%= add %>~doAction=new~closeFlag=false";
	document.aimCurrencyForm.target = popupPointer.name;
	document.aimCurrencyForm.submit();
}

function editCurrency(code) {
	openNewWindow(450, 230);
	<digi:context name="add" property="context/module/moduleinstance/updateCurrency.do" />
	document.aimCurrencyForm.action = "<%= add %>~closeFlag=false~doAction=show~currencyCode="+code;
	document.aimCurrencyForm.target = popupPointer.name;
	document.aimCurrencyForm.submit();
}

function deleteCurrency(code) {
		  var flag = validate();
		  if(flag)
		  {
			<digi:context name="deleteCurrency" property="context/module/moduleinstance/deleteCurrency.do" />
			document.aimCurrencyForm.action = "<%= deleteCurrency %>~id="+code;

			document.aimCurrencyForm.target = "_self";
			document.aimCurrencyForm.submit();
		  }
}
function validate(){


			return(confirm('<digi:trn jsFriendly="true">Do you want to delete this Currency?</digi:trn>'));

}
function applyFilter() {
	
	if(document.getElementsByName('numRecords')[0].value>0)
	{   <digi:context name="manager" property="context/module/moduleinstance/currencyManager.do" />
    document.aimCurrencyForm.action = "${manager}";
	document.aimCurrencyForm.target = "_self";
	document.aimCurrencyForm.submit();
}
	else alert("<digi:trn>Number of records per page should be greater than 0</digi:trn>");
}

function sortSubmit(val){
	
	var sval = document.aimCurrencyForm.sort.value;
	var soval = document.aimCurrencyForm.sortOrder.value;

	if ( val == sval ) {
		if (soval == "asc")
			document.aimCurrencyForm.sortOrder.value = "desc";
		else if (soval == "desc")
			document.aimCurrencyForm.sortOrder.value = "asc";
	}
	else
		document.aimCurrencyForm.sortOrder.value = "asc";
	
	document.aimCurrencyForm.sort.value=val;
	
	<digi:context name="sorting" property="context/module/moduleinstance/currencyManager.do" />
	document.aimCurrencyForm.action = "<%= sorting %>";
	document.aimCurrencyForm.target = "_self";
	document.aimCurrencyForm.submit();
}
function submitPages(){
	if(document.getElementsByName('numRecords')[0].value>0){
	    <digi:context name="manager" property="context/module/moduleinstance/currencyManager.do" />
	    document.aimCurrencyForm.action = "${manager}";
		document.aimCurrencyForm.target = "_self";
		document.aimCurrencyForm.submit();
	}
	else{
		alert("<digi:trn>Number of records per page should be greater than 0</digi:trn>");
	}
		
}

var enterBinder	= new EnterHitBinder('currencyFilterBtn');

function exportXSL(){
    <digi:context name="exportUrl" property="context/module/moduleinstance/exportCurrencyManager2XSL.do"/>;
    document.aimCurrencyForm.action="${exportUrl}";
    document.aimCurrencyForm.target="_blank";
    document.aimCurrencyForm.submit();
}


</script>



<h1 class="admintitle"><digi:trn>Currency manager</digi:trn></h1>
<digi:instance property="aimCurrencyForm" />
<digi:form action="/currencyManager.do">

<html:hidden property="sort" />
<html:hidden property="sortOrder" />
	<table width="1000" cellspacing="0" cellpadding="0" valign="top"
		align="center">
		<tr>
			<td>
				<!--  AMP Admin Logo --> <jsp:include page="teamPagesHeader.jsp" />
				<!-- End of Logo --></td>
		</tr>
		<tr>
			<td>
				<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000
					align=center>
					<tr>
						<td align=left class=r-dotted-lg valign="top" width=750>
							<table cellPadding=5 cellSpacing=3 width="100%">
								<tr>
									<!-- Start Navigation -->
									<td colspan="2"><span class=crumb> <digi:link
												href="/admin.do" styleClass="comment"
												title="Click here to goto Admin Home">
												<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
											</digi:link>&nbsp;&gt;&nbsp; <digi:trn key="aim:currencyManager">
						Currency Manager
						</digi:trn> </span>
										</td>
									<!-- End navigation -->
								</tr>
								
								<tr>
									<td align="left">
										<jsp:include
									page="/repository/aim/view/adminXSLExportToolbar.jsp" />
									</td>
									<td rowspan="4" valign=top>
										<table align="center" cellpadding="0" cellspacing="0"
											width="90%" border="0">
											<tr>
												<td style="border-bottom:1px solid #ccc;">
													<!-- Other Links -->
													<table cellpadding="0" cellspacing="0" width="120">
														<tr>
															<td bgColor=#c9c9c7 class=box-title>
																	<b style="font-size: 12px; padding-left: 5px;"><digi:trn
																	key="aim:otherLinks">Other
																		links</digi:trn></b>
															</td>
															<td background="module/aim/images/corner-r.gif"
																height="17" width=17></td>
														</tr>
													</table></td>
											</tr>
											<tr>
												<td bgColor="#ffffff" class="box-border">
													<table cellPadding=5 cellspacing="1" width="100%"
														class="inside">
														<tr>
															<td class="inside"><digi:img
																	src="module/aim/images/arrow-014E86.gif" width="15"
																	height="10" /> <c:set var="translation">
																	<digi:trn>Click here to go back to admin home page</digi:trn>
																</c:set> <digi:link href="/admin.do" title="${translation}">
																	<digi:trn>Admin Home</digi:trn>
																</digi:link>
															</td>
														</tr>
														<tr>
															<td class="inside"><digi:img
																	src="module/aim/images/arrow-014E86.gif" width="15"
																	height="10" /> <c:set var="translation">
																	<digi:trn>Click here to go to the Currency Rate Manager</digi:trn>
																</c:set> <digi:link
																	href="/showCurrencyRates.do~clean=true~timePeriod=1"
																	title="${translation}">
																	<digi:trn>Currency Rate Manager</digi:trn>
																</digi:link>
															</td>
														</tr>
														<tr>
															<td class="inside"><digi:img
																	src="module/aim/images/arrow-014E86.gif" width="15"
																	height="10" /> <c:set var="translation">
																	<digi:trn>Click here to go to Select Filteres Currency Rates</digi:trn>
																</c:set> <digi:link href="/selectFilteredRates.do"
																	title="${translation}">
																	<digi:trn>Select Filtered Rates</digi:trn>
																</digi:link>
															</td>
														</tr>
														<!-- end of other links -->
													</table></td>
											</tr>
									</table></td>
								</tr>

								<html:hidden property="page" />
								<html:hidden property="currencyCode" />
								<html:hidden property="currencyName" />
								<html:hidden property="countryId" />
								<html:hidden property="order" />
								<tr>
									<td noWrap width=75% vAlign="top">
										<table width="100%" cellspacing="2" cellPadding="2"
											vAlign="top" align="left">
											<tr>
												<td>
													<!-- Filters to be put here -->
													<table cellpadding="1" cellspacing="1" align="left"
														bgcolor="#dddddd" width="100%">
														<tr>
															<td bgcolor="#f4f4f2">
																<table cellpadding="0" cellSpacing=2 align="left"
																	border="0" style="font-size: 12px;">
																	<tr>
																		<td bgcolor="#f4f4f2" align="left"
																			style="padding-right: 10px;"><b><digi:trn
																					key="aim:filterBy">Filter By</digi:trn>:</b>
																		</td>
																		<td bgcolor="#f4f4f2" style="padding-right: 10px;"><digi:trn
																				key="aim:currencies">Currencies</digi:trn>
																		</td>
																		<td bgcolor="#f4f4f2" style="padding-right: 10px;"><html:select
																				property="filterByCurrency" styleClass="inp-text">
																				<html:option value="">--<digi:trn
																						key="aim:all">All</digi:trn>--</html:option>
																				<html:option value="A">
																					<digi:trn key="aim:activecurrencies">Active Currencies</digi:trn>
																				</html:option>
																				<html:option value="N">
																					<digi:trn key="aim:inactivecurrencies">Inactive Currencies</digi:trn>
																				</html:option>
																			</html:select>
																		</td>
																		<td bgcolor="#f4f4f2"><c:set var="trnGoBtn">
																				<digi:trn key="aim:goBtn"> Go </digi:trn>
																			</c:set> <input type="button" value="${trnGoBtn}"
																			class="buttonx" onclick="applyFilter()" id="currencyFilterBtn" /></td>
																	</tr>
																</table></td>
														</tr>
														<tr>
															<td bgcolor="#f4f4f2">
																<table cellpadding="0" cellSpacing=2 align="left"
																	border="0" vAlign="center" style="font-size: 12px;">
																	<tr>
																		<td bgcolor="#f4f4f2" vAlign="center" align="left"
																			style="padding-right: 10px;"><digi:trn
																				key="aim:numRecordsPerPage">Number of records per page</digi:trn>:
																		</td>
																		<td bgcolor="#f4f4f2" vAlign="center" width="50"
																			align="left"><html:text property="numRecords"
																				size="3" styleClass="inp-text" />
																		</td>

																		<td bgcolor="#f4f4f2" vAlign="center" align="left"><c:set
																				var="trnViewBtn">
																				<digi:trn key="aim:trnViewBtn"> View </digi:trn>
																			</c:set> <input type="button" value="${trnViewBtn}"
																			class="buttonx" onclick="submitPages()" />
																		</td>
																	</tr>
																</table></td>
														</tr>
													</table></td>
											</tr>
											<logic:equal name="aimCurrencyForm" property="cantDelete"
												value="true">
												<tr>
													<td bgcolor="#ffffff" valign="top" align="left"><b>
															<font color="red"> <digi:trn
																	key="aim:cannotDeleteCurrencyMsg2">Cannot Delete the currency since it is already in use!</digi:trn>
														</font> </b></td>
												</tr>
											</logic:equal>
											<tr>
												<td bgcolor="#ffffff" valign="top" align="left"
													class="report">
													<!-- Currency list table --> <!--  to export table we are adding class "report" to its container -->
													<table cellSpacing="1" cellPadding="4" vAlign="top"
														align="left" width="100%" class="inside">
														<thead>
															<tr bgcolor="eeeeee">
																<td width="5%" class="inside"></td>
																<td align="left" class="inside" width="20%"
																	style="cursor: pointer;" onclick="sortSubmit('code')"
																	onMouseOver="this.className='colHeaderOver'"
																	onMouseOut="this.className='colHeaderLink'"><b><digi:trn
																			key="aim:currCode">Code</digi:trn>
																</b>
																<c:if test="${aimCurrencyForm.sort=='code' && aimCurrencyForm.sortOrder=='asc'}">
																		<img src="/repository/aim/images/up.gif" alt="up" />
																	</c:if>
																<c:if test="${aimCurrencyForm.sort=='code' && aimCurrencyForm.sortOrder=='desc'}">
																		<img src="/repository/aim/images/down.gif" alt="down" />
																	</c:if>
																</td>
																<td align="left" class="inside" width="35%"
																	style="cursor: pointer;" onclick="sortSubmit('cname')"
																	onMouseOver="this.className='colHeaderOver'"
																	onMouseOut="this.className='colHeaderLink'"><b><digi:trn
																			key="aim:currencyName">Currency Name</digi:trn>
																</b>
																<c:if test="${aimCurrencyForm.sort=='cname' && aimCurrencyForm.sortOrder=='asc'}">
																		<img src="/repository/aim/images/up.gif" alt="up" />
																	</c:if>
																<c:if test="${aimCurrencyForm.sort=='cname' && aimCurrencyForm.sortOrder=='desc'}">
																		<img src="/repository/aim/images/down.gif" alt="down" />
																	</c:if>
																</td>
																<td colspan="2" align="left" class="inside"
																	style="cursor: pointer;"
																	onMouseOver="this.className='colHeaderOver'"
																	onclick="sortSubmit('country')"
																	onMouseOut="this.className='colHeaderLink'"><b><digi:trn
																			key="aim:countryName">Administrative Level 0</digi:trn>
																</b>
																<c:if test="${aimCurrencyForm.sort=='country' && aimCurrencyForm.sortOrder=='asc'}">
																		<img src="/repository/aim/images/up.gif" alt="up" />
																	</c:if>
																<c:if test="${aimCurrencyForm.sort=='country' && aimCurrencyForm.sortOrder=='desc'}">
																		<img src="/repository/aim/images/down.gif" alt="down" />
																	</c:if>
																</td>
															</tr>
														</thead>
														<!--  to export table we are adding class "yui-dt-data" to its tbody-->
														<tbody class="yui-dt-data">
															<c:if test="${empty aimCurrencyForm.currency}">
																<tr bgcolor="#f4f4f2">
																	<td colspan="5" align="center" class="inside"><digi:trn
																			key="aim:noCurrencies">No currencies present</digi:trn>
																	</td>
																</tr>
															</c:if>
															<% int index = 0; %>
															<c:if test="${!empty aimCurrencyForm.currency}">
																<c:forEach var="curr"
																	items="${aimCurrencyForm.currency}">
																	<%if (index%2 == 0) { %>
																	<tr class="rowNormal">
																		<%  } else { %>
																	
																	<tr class="rowAlternate">
																		<%}index++;%>
																		<td align="left" width="3" class="inside">
																			<c:if test="${curr.activeFlag == 1}">
																				<c:set var="translation">
																					<digi:trn>Click here to make the currency inactive</digi:trn>
																				</c:set>
																				<a	href="javascript:makeInactive('${curr.currencyCode}')" title="${translation}">
																					<digi:img src="module/aim/images/bullet_green.gif" border="0" />
																				</a>
																				<span class="invisible-item"><digi:trn>Active currencies</digi:trn>
																				</span>
																			</c:if>
																			<c:if test="${curr.activeFlag != 1}">
																				<c:set var="translation">
																					<digi:trn>Click here to make the currency Active</digi:trn>
																				</c:set>
																				<a href="javascript:makeActive('${curr.currencyCode}')" title="${translation}">
																					<digi:img src="module/aim/images/bullet_grey.gif" border="0" />
																				</a>
																				<span class="invisible-item"><digi:trn>Inactive currencies</digi:trn></span>
																			</c:if>
																		</td>
																		<td align="left" class="inside">
																			<a href="javascript:editCurrency('${curr.currencyCode}')">${curr.currencyCode}</a>
																		</td>
																		<td align="left" class="inside"><a
																			href="javascript:editCurrency('${curr.currencyCode}')">
																				${curr.currencyName}</a></td>
																		<td align="left" class="inside"><a
																			href="javascript:editCurrency('${curr.currencyCode}')">
																				<c:if test="${curr.countryLocation.id!=null}">
																					<digi:trn>${curr.countryLocation.name}</digi:trn>
																				</c:if> </a></td>
																		<!--  tds marked with class "ignore" will be ignored during export -->
																		<td align="right" class="inside ignore"><a
																			href="javascript:deleteCurrency('${curr.currencyCode}')">
																				<digi:img src="../ampTemplate/images/deleteIcon.gif" border="0" alt="Delete this Currency" /> </a>
																		</td>
																	</tr>
																</c:forEach>
															</c:if>
														</tbody>
													</table></td>
											</tr>
											<tr>
												<td>
													<!-- Pagination -->
													<hr />
													<table width="100%" cellSpacing="1" cellPadding="0"
														vAlign="top">
														<tr>
															<td>
																<table cellSpacing="1" cellPadding="2" vAlign="top"
																	align="left">
																	<tr>
																		<td align="left" bgcolor="#ffffff" width="3"><digi:img
																				src="module/aim/images/bullet_green.gif" />
																		</td>
																		<td align="left" bgcolor="#ffffff"
																			style="font-size: 12px;"><digi:trn
																				key="aim:denotesAnActiveCurrency">
															Denotes an active currency</digi:trn>
																		</td>
																		<td align="left" bgcolor="#ffffff"><table
																				cellspacing="1" cellpadding="2" valign="top"
																				align="left">
																				<tr>
																					<td align="left" bgcolor="#ffffff" width="3"><digi:img
																							src="module/aim/images/bullet_grey.gif" />
																					</td>
																					<td align="left" bgcolor="#ffffff"
																						style="font-size: 12px;"><digi:trn
																							key="aim:denotesAnInActiveCurrency"> Denotes an inactive currency</digi:trn>
																					</td>
																				</tr>
																			</table>
																		</td>
																	</tr>
																</table>
															</td>
														</tr>
														<c:if test="${!empty aimCurrencyForm.pages}">
															<tr>
																<td align="left" style="font-size: 12px;">
																	<hr /> <digi:trn>Pages:</digi:trn>
																	<c:if
																		test="${aimCurrencyForm.currentPage > 1}">
																		<jsp:useBean id="urlParamsFirst" type="java.util.Map"
																			class="java.util.HashMap" />
																		<c:set target="${urlParamsFirst}" property="page"
																			value="1" />
																		<c:set target="${urlParamsFirst}" property="order"
																			value="${aimCurrencyForm.order}" />
																		<c:set var="translation">
																			<digi:trn key="aim:firstpage">First Page</digi:trn>
																		</c:set>
																		<digi:link href="/currencyManager.do"
																			style="text-decoration=none" name="urlParamsFirst"
																			title="${translation}">
&lt;&lt;												</digi:link>
																		<jsp:useBean id="urlParamsPrevious"
																			type="java.util.Map" class="java.util.HashMap" />
																		<c:set target="${urlParamsPrevious}" property="page"
																			value="${aimCurrencyForm.currentPage -1}" />
																		<c:set target="${urlParamsPrevious}" property="order"
																			value="${aimCurrencyForm.order}" />
																		<c:set var="translation">
																			<digi:trn key="aim:previouspage">Previous Page</digi:trn>
																		</c:set>
																		<digi:link href="/currencyManager.do"
																			name="urlParamsPrevious" style="text-decoration=none"
																			title="${translation}">
&lt;												</digi:link>
																	</c:if> <c:set var="length"
																		value="${aimCurrencyForm.pagesToShow}"></c:set> <c:set
																		var="start" value="${aimCurrencyForm.offset}" /> <logic:iterate
																		name="aimCurrencyForm" property="pages" id="pages"
																		type="java.lang.Integer" offset="${start}"
																		length="${length}">
																		<jsp:useBean id="urlParams1" type="java.util.Map"
																			class="java.util.HashMap" />
																		<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
																		<c:set target="${urlParams1}" property="orgSelReset"
																			value="false" />
																		<c:set target="${urlParams1}" property="order"
																			value="${aimCurrencyForm.order}" />
																		<c:if test="${aimCurrencyForm.currentPage == pages}">
																			<font color="#FF0000"><%=pages%></font>
																		</c:if>
																		<c:if test="${aimCurrencyForm.currentPage != pages}">
																			<c:set var="translation">
																				<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
																			</c:set>
																			<digi:link href="/currencyManager.do"
																				name="urlParams1" title="${translation}">
																				<%=pages%>
																			</digi:link>
																		</c:if>
												|&nbsp;
											</logic:iterate> <c:if
																		test="${aimCurrencyForm.currentPage != aimCurrencyForm.pagesSize}">
																		<jsp:useBean id="urlParamsNext" type="java.util.Map"
																			class="java.util.HashMap" />
																		<c:set target="${urlParamsNext}" property="page"
																			value="${aimCurrencyForm.currentPage+1}" />
																		<c:set target="${urlParamsNext}"
																			property="orgSelReset" value="false" />
																		<c:set target="${urlParamsNext}" property="order"
																			value="${aimCurrencyForm.order}" />
																		<c:set var="translation">
																			<digi:trn key="aim:nextpage">Next Page</digi:trn>
																		</c:set>
																		<digi:link href="/currencyManager.do"
																			style="text-decoration=none" name="urlParamsNext"
																			title="${translation}">&gt;</digi:link>
																		<jsp:useBean id="urlParamsLast" type="java.util.Map"
																			class="java.util.HashMap" />
																		<c:if
																			test="${aimCurrencyForm.pagesSize > aimCurrencyForm.pagesToShow}">
																			<c:set target="${urlParamsLast}" property="page"
																				value="${aimCurrencyForm.pagesSize-1}" />
																		</c:if>
																		<c:if
																			test="${aimCurrencyForm.pagesSize < aimCurrencyForm.pagesToShow}">
																			<c:set target="${urlParamsLast}" property="page"
																				value="${aimCurrencyForm.pagesSize}" />
																		</c:if>
																		<c:set target="${urlParamsLast}"
																			property="orgSelReset" value="false" />
																		<c:set target="${urlParamsLast}" property="order"
																			value="${aimCurrencyForm.order}" />
																		<c:set var="translation">
																			<digi:trn key="aim:lastpage">Last Page</digi:trn>
																		</c:set>
																		<digi:link href="/currencyManager.do"
																			style="text-decoration=none" name="urlParamsLast"
																			title="${translation}">&gt;&gt;												
														</digi:link>&nbsp;&nbsp;
											</c:if> <c:out value="${aimCurrencyForm.currentPage}"></c:out> <digi:trn
																		key="aim:of">of</digi:trn> <c:out
																		value="${aimCurrencyForm.pagesSize}"></c:out>
																</td>
															</tr>
														</c:if>
													</table></td>
											</tr>
											<tr>
												<td align=center>
													 <!-- Other links -->
													<table cellSpacing="1" cellPadding="2" vAlign="top">
														<tr>
															<td align="left" bgcolor="#ffffff"
																style="font-size: 12px;"><a
																href="javascript:addNewCurrency()"> <digi:trn
																		key="aim:addNewCurrency">
											Add a new currency</digi:trn>
															</a>
															</td>
														</tr>
													</table></td>
											</tr>
										</table></td>
								</tr>
							</table></td>
					</tr>
				</table></td>
		</tr>
	</table>
</digi:form>



