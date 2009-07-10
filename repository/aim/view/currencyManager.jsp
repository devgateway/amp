<%@ page pageEncoding="UTF-8" %>
<%@ page import="org.digijava.module.aim.form.CurrencyForm"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/currency.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<jsp:include page="currencyManagerPopin.jsp" flush="true" />

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


			return(confirm('<digi:trn key="Do you want to delete this Currency?">Do you want to delete this Currency?</digi:trn>'));

}
function applyFilter() {
    <digi:context name="manager" property="context/module/moduleinstance/currencyManager.do" />
    document.aimCurrencyForm.action = "${manager}";
	document.aimCurrencyForm.target = "_self";
	document.aimCurrencyForm.submit();
}

function sortSubmit(value){
	<digi:context name="sorting" property="context/module/moduleinstance/currencyManager.do" />
	document.aimCurrencyForm.action = "<%= sorting %>~order="+value;
	document.aimCurrencyForm.target = "_self";
	document.aimCurrencyForm.submit();
}

function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}

function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}


function setHoveredRow(rowId) {

	var rowElement = document.getElementById(rowId);
	if(rowElement){
    	var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        cells      = rowElement.getElementsByTagName('td');

		for(var i = 0, n = cells.length; i < n; ++i) {
			cells[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			cells[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		cells = null;
	}
}
</script>

<style type="text/css">
.jcol {
	padding-left: 10px;
}

.jlien {
	text-decoration: none;
}

.tableEven {
	background-color: #dbe5f1;
	font-size: 8pt;
	padding: 2px;
}

.tableOdd {
	background-color: #FFFFFF;
	font-size: 8pt;
	padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}

.notHovered {
	background-color: #FFFFFF;
}
</style>

<digi:instance property="aimCurrencyForm" />

<digi:form action="/currencyManager.do">

<html:hidden property="page"/>
<html:hidden property="currencyCode"/>
<html:hidden property="currencyName"/>
<html:hidden property="countryId"/>
<html:hidden property="order"/>



<table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left">
<tr><td>
<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td align=left vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height="33"><span class=crumb>
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
						<digi:errors/>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
						<table width="100%" cellspacing="0" cellPadding="0" vAlign="top" >
							<tr><td>
								<!-- Filters to be put here -->
								<table cellPadding=1 cellSpacing=1 align="left" width="330">
								<tr>
									<td height="30">
										<table cellPadding=0 cellSpacing=2 align="left" border=0 width="330">
											<tr height="25">
												<td align="left" >
													<digi:trn key="aim:filterBy">Filter By</digi:trn>:
													<html:select property="filterByCurrency" styleClass="inp-text" onchange="document.aimCurrencyForm.submit()">
														<html:option value="">-<digi:trn key="aim:all">All</digi:trn>-</html:option>
														<html:option value="A"><digi:trn key="aim:activecurrencies">Active Currencies</digi:trn></html:option>
														<html:option value="N"><digi:trn key="aim:inactivecurrencies">Inactive Currencies</digi:trn></html:option>
													</html:select>
												</td>
												<td align="right" width="50">
		                                          <c:set var="trnGoBtn">
		                                            <digi:trn key="aim:goBtn"> Go </digi:trn>
		                                          </c:set>
		                                          <input type="button" value="${trnGoBtn}" class="dr-menu" onclick="applyFilter()"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								

								</table>
							</td>
							</tr>
							<logic:equal name="aimCurrencyForm" property="cantDelete" value="true">
								<tr>
									<td bgcolor="#ffffff" valign="top" align="left">
										<b><font color="red">
											<digi:trn key="aim:cannotDeleteCurrencyMsg2">Cannot Delete the currency since it is already in use!</digi:trn>
										</font></b>
									</td>
								</tr>
							</logic:equal>
							<tr><td bgcolor="#ffffff" valign="top" align="left">
								<!-- Currency list table -->
								<table cellSpacing="0" cellPadding="0" vAlign="top" align="left" width="85%">
									<tr>
										<td>
											<table width="100%" height="30" cellpadding="0" cellspacing="0">
												<tr style="background-color: #999999; color: #000000;" align="center">
													<td width="5%" > </td>
													<td align="left" width="15%" style="cursor:pointer;" onclick="sortSubmit(1)" >
														<b><digi:trn key="aim:currCode">Code</digi:trn></b>
													</td>
													<td align="left" width="40%"  style="cursor:pointer;" onclick="sortSubmit(2)" >
														<b><digi:trn key="aim:currencyName">Currency Name</digi:trn></b>
													</td>
													<td colspan="2" width="25%" align="center" style="cursor:pointer;" onclick="sortSubmit(3)" >
														<b><digi:trn key="aim:countryName">Country</digi:trn></b>
													</td>
													<td width="15%" > </td>
												</tr>
											</table>
										</td>
									</tr>
									<c:if test="${empty aimCurrencyForm.currency}">
									<tr>
										<td colspan="4" align="center">
											<digi:trn key="aim:noCurrencies">No currencies present</digi:trn>
										</td>
									</tr>
									</c:if>
									<c:if test="${!empty aimCurrencyForm.currency}">
									<tr>
										<td>
											<div style="overflow: auto; width: 100%; height: 220px; max-height: 220px;">
												<table width="100%" cellspacing="0" cellpadding="0" id="dataTable">
													<c:forEach var="curr" items="${aimCurrencyForm.currency}">
														<tr height="30">
														<td align="center" width="5%">
															<c:if test="${curr.activeFlag == 1}">
																<c:set var="translation">
																	<digi:trn key="aim:clickHereToMakeTheCurrencyInactive">
																		Click here to make the currency inactive
																	</digi:trn>
																</c:set>
																<a href="javascript:makeInactive('${curr.currencyCode}')"
																title="${translation}">
																<digi:img src="module/aim/images/bullet_green.gif" border="0"/></a>
															</c:if>
															<c:if test="${curr.activeFlag != 1}">
																<c:set var="translation">
																	<digi:trn key="aim:clickHereToMakeTheCurrencyActive">
																		Click here to make the currency Active
																	</digi:trn>
																</c:set>
																<a href="javascript:makeActive('${curr.currencyCode}')"
																title="${translation}">
																<digi:img src="module/aim/images/bullet_grey.gif" border="0"/></a>
															</c:if>
														</td>
														<td align="left" width="15%">
			                                              <a href="javascript:editCurrency('${curr.currencyCode}')">
			                                                <digi:trn key='aim:currency:${fn:replace(curr.currencyCode, " ", "_")}'>${curr.currencyCode}</digi:trn>
			                                              </a>
														</td>
														<td align="left" width="40%">
															<a href="javascript:editCurrency('${curr.currencyCode}')">
															${curr.currencyName}</a>
														</td>
														<td align="center" width="25%">
			                                              <a href="javascript:editCurrency('${curr.currencyCode}')">
			                                                <c:if test="${curr.countryLocation.id!=null}">
			                                                <digi:trn>${curr.countryLocation.name}</digi:trn>
			                                                </c:if>
			                                              </a>
			                                            </td>
														<td align="center" width="15%">
															<a href="javascript:deleteCurrency('${curr.currencyCode}')">
													 		<digi:img src="../ampTemplate/images/trash_16.gif" border="0" alt="Delete this Currency"/>
															</a>
														</td>
													</tr>
													</c:forEach>
												</table>
											</div>
										</td>
									</tr>
									</c:if>
								</table>
							</td>
							</tr>
							<tr>
								<td>
									<table>
									<tr>
									<td width="50%">
										<!-- Pagination -->
										
										<table style="padding:5px;">
										<c:if test="${!empty aimCurrencyForm.pages}">
											<tr bgcolor="#ffffff" id="rowHighlight">
													<c:if test="${aimCurrencyForm.currentPage > 1}">
														<jsp:useBean id="urlParamsFirst" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsFirst}" property="page" value="1"/>
														<c:set target="${urlParamsFirst}" property="order" value="${aimCurrencyForm.order}"/>
														<c:set var="translation">
															<digi:trn key="aim:firstpage">First Page</digi:trn>
														</c:set>
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
															<digi:link href="/currencyManager.do"  style="text-decoration=none"  name="urlParamsFirst" title="${translation}">
																&lt;&lt;
															</digi:link>
														</td>
														<jsp:useBean id="urlParamsPrevious" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsPrevious}" property="page" value="${aimCurrencyForm.currentPage -1}"/>
														<c:set target="${urlParamsPrevious}" property="order" value="${aimCurrencyForm.order}"/>
														<c:set var="translation">
															<digi:trn key="aim:previouspage">Previous Page</digi:trn>
														</c:set>
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
															<digi:link  href="/currencyManager.do" name="urlParamsPrevious" style="text-decoration=none" title="${translation}" >
																&lt;
															</digi:link>
														</td>
													</c:if>
													
													<c:set var="length" value="${aimCurrencyForm.pagesToShow}"></c:set>
													<c:set var="start" value="${aimCurrencyForm.offset}"/>
													<logic:iterate name="aimCurrencyForm" property="pages" id="pages" type="java.lang.Integer" offset="${start}" length="${length}">	
														<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams1}" property="page"><%=pages%></c:set>
														<c:set target="${urlParams1}" property="orgSelReset" value="false"/>
														<c:set target="${urlParams1}" property="order" value="${aimCurrencyForm.order}"/>
														<c:if test="${aimCurrencyForm.currentPage == pages}">
															<td style="padding:3px;border:2px solid #000000; " nowrap="nowrap" >
																<font color="#FF0000"><%=pages%></font>
															</td>
														</c:if>
														<c:if test="${aimCurrencyForm.currentPage != pages}">
															<c:set var="translation">
																<digi:trn key="aim:clickToViewNextPage">Click here to go to Next Page</digi:trn>
															</c:set>
															<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
																<digi:link href="/currencyManager.do" name="urlParams1" title="${translation}" >
																	<%=pages%>
																</digi:link>
															</td>
														</c:if>
													</logic:iterate>
													<c:if test="${aimCurrencyForm.currentPage != aimCurrencyForm.pagesSize}">
														<jsp:useBean id="urlParamsNext" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParamsNext}" property="page" value="${aimCurrencyForm.currentPage+1}"/>
														<c:set target="${urlParamsNext}" property="orgSelReset" value="false"/>
														<c:set target="${urlParamsNext}" property="order" value="${aimCurrencyForm.order}"/>
														<c:set var="translation">
															<digi:trn key="aim:nextpage">Next Page</digi:trn>
														</c:set>
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
															<digi:link href="/currencyManager.do"  style="text-decoration=none" name="urlParamsNext" title="${translation}"  >
																&gt;
															</digi:link>
														</td>
														<jsp:useBean id="urlParamsLast" type="java.util.Map" class="java.util.HashMap"/>
														<c:if test="${aimCurrencyForm.pagesSize > aimCurrencyForm.pagesToShow}">
															<c:set target="${urlParamsLast}" property="page" value="${aimCurrencyForm.pagesSize-1}"/>
														</c:if>
														<c:if test="${aimCurrencyForm.pagesSize < aimCurrencyForm.pagesToShow}">
															<c:set target="${urlParamsLast}" property="page" value="${aimCurrencyForm.pagesSize}"/>
														</c:if>
														<c:set target="${urlParamsLast}" property="orgSelReset" value="false"/>
														<c:set target="${urlParamsLast}" property="order" value="${aimCurrencyForm.order}"/>
														<c:set var="translation">
															<digi:trn key="aim:lastpage">Last Page</digi:trn>
														</c:set>
														<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
															<digi:link href="/currencyManager.do"  style="text-decoration=none" name="urlParamsLast" title="${translation}"  >
																&gt;&gt; 
															</digi:link>
														</td>
													</c:if>
													<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
														<digi:trn key="aim:of">of</digi:trn> 
														<c:out value="${aimCurrencyForm.pagesSize}"></c:out>
														<digi:trn key="aim:pages">Pages</digi:trn>&nbsp;
													</td>
													<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
													</td>
													<% CurrencyForm aimCurrencyForm = (CurrencyForm) pageContext.getAttribute("aimCurrencyForm");%>
													<td style="padding:3px;border:1px solid #999999;" nowrap="nowrap">
														<c:out value="<%=aimCurrencyForm.getAllCurrencies().size()%>"></c:out>
														<digi:trn key="aim:records">Records</digi:trn>
													</td>
												</tr>
											</c:if>
										</table>
									
								</td>
								<td width="25%" align="center">
									<digi:trn key="aim:results">Results:</digi:trn>&nbsp;
									<html:select property="numRecords" styleClass="inp-text" onchange="document.aimCurrencyForm.submit()">
										<html:option value="10">10</html:option>
										<html:option value="20">20</html:option>
										<html:option value="50">50</html:option>
										<html:option value="-1">
											<digi:trn key="aim:resultsAll">-All-</digi:trn>
										</html:option>
									</html:select>
								</td>			
								<td align="left" bgcolor="#ffffff">
									<c:set var="addCurrencyBtn">
                                      <digi:trn key="aim:addCurrencyBtn">Add a new currency</digi:trn>
                                    </c:set>
                                    <input type="button" value="${addCurrencyBtn}" class="dr-menu" onclick="javascript:addNewCurrency()"/>									
								</td>
							</tr>
						</table>
							</td>
							</tr>
							<tr>
						        <td>
									<table>
						             	<tr>
						                 	<td colspan="2">
						                 		<strong><digi:trn key="aim:IconReference">Icons Reference</digi:trn></strong>
						       				</td>
						       			</tr>
						     			<tr>
						           			<td width="15" height="20" align="center">
												<img src= "module/aim/images/bullet_grey.gif" vspace="2" border="0" align="absmiddle" />
											</td>
											<td nowrap="nowrap">
						               			<digi:trn key="aim:clickToInactiveCurrency">Click on this icon to activate a Currency (Currently inactive)&nbsp;</digi:trn>
						               			<br />
						       				</td>
						       			</tr>
						        		<tr>
						           			<td width="15" height="20" align="center">
												<img src= "module/aim/images/bullet_green.gif" vspace="2" border="0" align="absmiddle" />
						               		</td>
											<td nowrap="nowrap">
											<digi:trn key="aim:clickToActiveCurrency">Click on this icon to deactivate a Currency (Currently active)&nbsp;</digi:trn>
						               			<br />
						       				</td>
						       			</tr>
										<tr>
						           			<td width="15" height="20" align="center">
												<img src= "../ampTemplate/images/trash_16.gif" vspace="2" border="0" align="absmiddle" />
						               		</td>
											<td nowrap="nowrap">
											<digi:trn key="aim:clickToDeleteCurrency">Click on this icon to delete a Currency&nbsp;</digi:trn>
						               			<br />
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
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script> 
</digi:form>



