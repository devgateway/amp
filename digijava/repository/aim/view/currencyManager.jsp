<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/currency.js"/>"></script>
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
	document.aimCurrencyForm.action = "<%= add %>~doAction=showCurrencies~closeFlag=false";
	document.aimCurrencyForm.target = popupPointer.name;
	document.aimCurrencyForm.submit();
}

function editCurrency(code) {
	openNewWindow(450, 230);
	<digi:context name="add" property="context/module/moduleinstance/updateCurrency.do" />
	document.aimCurrencyForm.action = "<%= add %>~doAction=showCurrencies~currencyCode="+code;
	document.aimCurrencyForm.target = popupPointer.name;
	document.aimCurrencyForm.submit();
}

function submit() {
	document.aimCurrencyForm.target = "_self";
	document.aimCurrencyForm.submit();		  
}

</script>

<digi:errors/>
<digi:instance property="aimCurrencyForm" />

<digi:form action="/currencyManager.do">

<html:hidden property="page"/>

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
						<digi:trn key="aim:currencyManager">
						Currency Manager
						</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:currencyManager">
							Currency Manager
						</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
						<table width="100%" cellspacing="2" cellPadding="2" vAlign="top" align="left">
							<tr><td>
								<!-- Filters to be put here -->


								<table cellPadding=1 cellSpacing=1 align="left" bgcolor="#dddddd" width="330">
								<tr><td bgcolor="#f4f4f2">
								<table cellPadding=0 cellSpacing=2 align="left" border=0 width="330">
									<tr>
										<td bgcolor="#f4f4f2" align="left" width="100">
											<b><digi:trn key="aim:filterBy">Filter By</digi:trn>:</b>
										</td>
										<td bgcolor="#f4f4f2" align="right" width="80">
											<digi:trn key="aim:currencies">Currencies</digi:trn>
										</td>
										<td bgcolor="#f4f4f2" align="left" width="100">
											<html:select property="filterByCurrency" styleClass="inp-text">
												<html:option value="">-- All --</html:option>
												<html:option value="A">Active Currencies</html:option>
												<html:option value="N">Inactive Currencies</html:option>
											</html:select>
										</td>
										<td bgcolor="#f4f4f2" align="right" width="50">
											<html:button  styleClass="dr-menu" property="submitButton"  onclick="submit()">
												<digi:trn key="btn:go">Go</digi:trn> 
											</html:button>
											
										</td>	
									</tr>
								</table>
								
								</td></tr>
								<tr><td bgcolor="#f4f4f2">
								<table cellPadding=0 cellSpacing=2 align="left" border=0 vAlign="center">
									<tr>
										<td bgcolor="#f4f4f2" vAlign="center" width="170" align="left">
											<digi:trn key="aim:numRecordsPerPage">Number of records per page</digi:trn>:
										</td>
										<td bgcolor="#f4f4f2" vAlign="center" width="50" align="left">
											<html:text property="numRecords" size="3" styleClass="inp-text"/>
										</td>											
										<td bgcolor="#f4f4f2" vAlign="center" align="left">
											<html:button  styleClass="dr-menu" property="submitButton"  onclick="submit()">
												<digi:trn key="btn:view">View</digi:trn> 
											</html:button>
										</td>	
									</tr>
								</table>								
								</td></tr>
								
								</table>
							</td></tr>
							<tr><td bgcolor="#ffffff" valign="top" align="left">
								<!-- Currency list table -->
								<table cellSpacing="1" cellPadding="4" vAlign="top" align="left" bgcolor="#aaaaaa" width="450">
									<tr bgcolor="eeeeee">
										<td colspan="2" align="center" width="40" onMouseOver="this.className='colHeaderOver'"
										onMouseOut="this.className='colHeaderLink'">
											<b><digi:trn key="aim:currCode">Code</digi:trn></b>
										</td>
										<td align="center" width="200" onMouseOver="this.className='colHeaderOver'"
										onMouseOut="this.className='colHeaderLink'">
											<b><digi:trn key="aim:currencyName">Currency Name</digi:trn></b>
										</td>
										<%--
										<td align="center" width="150" onMouseOver="this.className='colHeaderOver'"
										onMouseOut="this.className='colHeaderLink'">
											<b><digi:trn key="aim:countryName">Country</digi:trn></b>
										</td>
										--%>
									</tr>
									<c:if test="${empty aimCurrencyForm.currency}">
									<tr bgcolor="#f4f4f2">
										<td colspan="4" align="center">
											<digi:trn key="aim:noCurrencies">No currencies present</digi:trn>
										</td>
									</tr>
									</c:if>
									<% int index = 0; %>
									<c:if test="${!empty aimCurrencyForm.currency}">
									<c:forEach var="curr" items="${aimCurrencyForm.currency}">
									<%
									if (index%2 == 0) { %>
									<tr class="rowNormal">
									<% } else { %>
									<tr class="rowAlternate">
									<% } 
									index++;%>
										<td align="left" bgcolor="#ffffff" width="3">
											<c:if test="${curr.activeFlag == 1}">
												<bean:define id="translation">
													<digi:trn key="aim:clickHereToMakeTheCurrencyInactive">
														Click here to make the currency inactive
													</digi:trn>
												</bean:define>
												<a href="javascript:makeInactive('<c:out value="${curr.currencyCode}"/>')"
												title="<%=translation%>">
												<digi:img src="module/aim/images/bullet_green.gif" border="0"/></a>
											</c:if>
											<c:if test="${curr.activeFlag != 1}">
												<bean:define id="translation">
													<digi:trn key="aim:clickHereToMakeTheCurrencyActive">
														Click here to make the currency Active
													</digi:trn>
												</bean:define>											
												<a href="javascript:makeActive('<c:out value="${curr.currencyCode}"/>')"
												title="<%=translation%>">
												<digi:img src="module/aim/images/bullet_grey.gif" border="0"/></a>											
											</c:if>											
										</td>									
										<td align="left">
											<a href="javascript:editCurrency('<c:out value="${curr.currencyCode}"/>')">
											<c:out value="${curr.currencyCode}"/></a>
										</td>
										<td align="left">
											<a href="javascript:editCurrency('<c:out value="${curr.currencyCode}"/>')">
											<c:out value="${curr.currencyName}"/></a>
										</td>
										<%--
										<td align="left">
											<c:out value="${curr.countryName}"/>
										</td>
										--%>
									</tr>
									</c:forEach>
									</c:if>									
								</table>
							</td></tr>
							<tr><td>
								<!-- Pagination -->
								<table width="460" cellSpacing="1" cellPadding="0" vAlign="top">
									<tr>
										<td>&nbsp;</td>
										<td align="right" width="184">
											<table cellSpacing="1" cellPadding="2" vAlign="top" align="left">
												<tr>
													<td align="left" bgcolor="#ffffff" width="3">
														<digi:img src="module/aim/images/bullet_green.gif"/>
													</td>
													<td align="left" bgcolor="#ffffff">
														<digi:trn key="aim:denotesAnActiveCurrency">
															Denotes an active currency</digi:trn>
													</td>										
												</tr>										
											</table>
										</td>
									</tr>								
									<tr>
										<td width="200" align="left">
											<c:if test="${!empty aimCurrencyForm.pages}">
											<bean:size name="aimCurrencyForm" property="pages" id="totpages"/>
											Page <u><c:out value="${aimCurrencyForm.currentPage}"/></u> of 
											<u><c:out value="${totpages}"/></u>
											</c:if>
										</td>
										<td align="right" width="184">
											<table cellSpacing="1" cellPadding="2" vAlign="top" align="left">
												<tr>
													<td align="left" bgcolor="#ffffff" width="3">
														<digi:img src="module/aim/images/bullet_grey.gif"/>
													</td>
													<td align="left" bgcolor="#ffffff">
														<digi:trn key="aim:denotesAnInActiveCurrency">
															Denotes an inactive currency</digi:trn>
													</td>										
												</tr>										
											</table>
										</td>										
									</tr>
									<c:if test="${!empty aimCurrencyForm.pages}">
									<tr>
										<td colspan="2" align="left">
											Pages :
											<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
											<c:forEach var="currPage" items="${aimCurrencyForm.pages}">
												<c:if test="${currPage == aimCurrencyForm.currentPage}">
													<c:out value="${currPage}"/>
												</c:if>
												<c:if test="${currPage != aimCurrencyForm.currentPage}">
													<c:set target="${urlParams}" property="page">
														<c:out value="${currPage}"/>
													</c:set>																							
													<digi:link href="/currencyManager.do" name="urlParams">
													<c:out value="${currPage}"/></digi:link> 
												</c:if>												
												| 
											</c:forEach>
										</td>
									</tr>
									</c:if>
								</table>
							</td></tr>
							<tr><td>
								<!-- Other links -->
								<table cellSpacing="1" cellPadding="2" vAlign="top" align="left">
									<tr>
										<td align="left" bgcolor="#ffffff">
											<a href="javascript:addNewCurrency()">
											<digi:trn key="aim:addNewCurrency">
											Add a new currency</digi:trn></a>
										</td>										
									</tr>										
								</table>								
							</td></tr>
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
