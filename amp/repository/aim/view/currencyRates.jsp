<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/aim" prefix="aim" %>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript" type="text/javascript">
	<jsp:include page="scripts/calendar.js.jsp" flush="true" />
</script>
<jsp:include page="scripts/newCalendar.jsp" flush="true" />
<jsp:include page="addCurrencyRate.jsp" flush="true" />
<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}

.jcol{												
padding-left:10px;												 
}
.jlien{
	text-decoration:none;
}
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

.notHovered {
	background-color:#FFFFFF;
}
.mapagination{
padding:3px;border:1px solid #999999; width:10px; height:10px; float:left;
}

.clsTableTitleCol {
	-x-system-font:none;
	background-color:#B8B8B0;
	color:#000000;
	cursor:default;
	font-family:"Verdana";
	font-size:7.5pt;
	font-size-adjust:none;
	font-stretch:normal;
	font-style:normal;
	font-variant:normal;
	font-weight:bold;
	line-height:normal;
	text-align:center;
}		
.reportsBorderTable {
	border-collapse:collapse;
}
.reportsBorderTD {
	cellpadding: 0px;
	cellspacing: 0px;
	padding: 0px;
	margin: 0px;
	font-family:Arial,Helvetica,sans-serif;
}
		
-->
</style>

<script language="JavaScript">

function addExchangeRate() {
	myAddExchangeRate();
}

function loadExchangeRate() {
	openNewWindow(500, 300);
	<digi:context name="loadExchangeRate" property="context/module/moduleinstance/loadCurrencyRates.do" />
  	document.aimCurrencyRateForm.action = "<%= loadExchangeRate %>";
	document.aimCurrencyRateForm.target = popupPointer.name;
	document.aimCurrencyRateForm.submit();
}

function updateRates()
{
	if(confirm('<digi:trn key="aim:updateCurrencyFromUploadedFile">Do you want to update the Currency rates from the uploaded file ?</digi:trn>'))
	{
		<digi:context name="updateRates" property="context/module/moduleinstance/saveCurrencyRate.do~doAction=file"/>
		document.aimCurrencyRateForm.action = "<%= updateRates %>";
		document.aimCurrencyRateForm.target = "_self";
		document.aimCurrencyRateForm.submit();
	}
	else
		return false;
}

function editExchangeRate(date,code) {
	myEditExchangeRate(date, code);
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
	<digi:context name="showCurrRates" property="context/module/moduleinstance/showCurrencyRates.do" />
	document.aimCurrencyRateForm.action = "<%=showCurrRates %>";
	document.aimCurrencyRateForm.target = "_self";
	document.aimCurrencyRateForm.submit();
}

function selectFile() {
	document.aimCurrencyRateForm.ratesFile.value = document.aimCurrencyRateForm.file.value;
	document.aimCurrencyRateForm.doAction.value = "loadRates";
	document.aimCurrencyRateForm.target = "_self";
	document.aimCurrencyRateForm.submit();
}

function fnSubmit() {
	<digi:context name="showCurrRates" property="context/module/moduleinstance/showCurrencyRates.do" />
	document.aimCurrencyRateForm.action = "<%=showCurrRates %>";
	document.aimCurrencyRateForm.target = "_self";
	document.aimCurrencyRateForm.submit();
}

</script>

<script type="text/javascript">
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
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
<c:set var="baseCurrencyGS" value="<%= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.BASE_CURRENCY) %>" scope="request" />
<digi:errors/>
<digi:instance property="aimCurrencyRateForm" />

<digi:form action="/showCurrencyRates.do" enctype="multipart/form-data">

<input type="hidden" name="selectedDate">
<html:hidden property="updateCRateId"/>
<html:hidden property="doAction"/>
<html:hidden property="ratesFile"/>
<input type="hidden" name="currUrl" value="">

<table width="100%" cellspacing=0 cellpadding=0 valign="top" align="left">
<tr><td>
<!-- AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
</td></tr>
<tr><td>
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td  width=14>&nbsp;</td>
		<td align=left  vAlign=top width=750>
			<table cellPadding=5 cellSpacing=3 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td><span class=crumb>
						<digi:link href="/admin.do" styleClass="comment" title="Click here to goto Admin Home">
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:currencyRateManager">Currency Rate Manager</digi:trn>
						</span>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:exchangeRatesfor1USDollars">
							Exchange Rates for 1 US Dollars(USD)
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width="75%" vAlign="top">
						<table width="100%" cellspacing="2" cellPadding="2" vAlign="top" align="left">
							<tr><td>
								<!-- Filters -->
								<table cellPadding=1 cellSpacing=1 align="left"  width="700" height = "70">
								<tr><td >
								<table cellPadding=0 cellSpacing=2 align="left" width="700" border=0>
									<tr>
										<td  vAlign="center">
											<b><digi:trn key="aim:filterBy">Filter By</digi:trn>:</b>
										</td>
										<td  vAlign="center">
											<digi:trn key="aim:currencyCode">Currency Code</digi:trn>
										</td>
										<td  vAlign="center">
											<html:select property="filterByCurrCode" styleClass="inp-text">
												<html:option value="">--<digi:trn key="aim:all">All</digi:trn>--</html:option>
												<html:optionsCollection name="aimCurrencyRateForm" property="currencyCodes"
												value="currencyCode" label="currencyCode" />&nbsp;&nbsp;&nbsp;
											</html:select>
										</td>
										<td  vAlign="center">
											<digi:trn>Base Code</digi:trn>
										</td>
										<td  vAlign="center">
											<html:select property="filterByBaseCode" styleClass="inp-text">
<!--												<html:option value=""><digi:trn>${baseCurrencyGS}</digi:trn></html:option>-->
												<logic:iterate id="codebase" name="aimCurrencyRateForm" property="currencyCodes">																																															
													<html:option value="${codebase.currencyCode}"><digi:trn>${codebase.currencyCode}</digi:trn></html:option>	
												</logic:iterate>	
											</html:select>
										</td>
										<td vAlign="center">
											<digi:trn key="aim:ratesOfDate">Rates of date</digi:trn>
										</td>
										<%--
										<td  vAlign="center">
											<digi:trn key="aim:dateFrom">From</digi:trn>
										</td>
										--%>
										<td  vAlign="center">
											<table cellPadding=0 cellSpacing=0>
												<tr>
													<td>
														<html:text property="filterByDateFrom" size="10" readonly="true"
														styleId="filterByDateFrom" styleClass="inp-text"/>
													</td>
													<td align="left" vAlign="center">&nbsp;
														<a id="date1" href='javascript:pickDate("date1",document.aimCurrencyRateForm.filterByDateFrom)'>
															<img src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif" alt="Click to View Calendar" border=0>
														</a>
													</td>
												</tr>
											</table>
										</td>
										<td  vAlign="center">
                                        &nbsp;&nbsp;&nbsp;&nbsp;
											<%--<digi:trn key="aim:andPrev7Days">and previous</digi:trn>--%>
											<html:select property="timePeriod" styleClass="inp-text">
												<logic:iterate id="period" name="aimCurrencyRateForm" property="timePeriods" >
													<c:set var="translation">
														<digi:trn key="aim:timeperiods${period.label}">${period.label}</digi:trn>
													</c:set>						
													<html:option value="${period.value}">${translation}</html:option>
												</logic:iterate>
											</html:select>
										</td>
										
										<td  vAlign="center">
                                          <c:set var="trnGoBtn">
                                            <digi:trn key="aim:goBtn"> Go </digi:trn>
                                          </c:set>
                                          <html:submit onclick="javascrip:fnSubmit()" value="${trnGoBtn}"  styleClass="dr-menu"/>
										</td>
									</tr>
								</table>
								</td></tr>
                                <tr>
                                  <td >
                                    <table cellPadding=0 cellSpacing=2 align="left" border=0 vAlign="center">
                                      <tr>
                                        <td  vAlign="left" align="center">
                                          <FONT color=red>*</FONT>
                                          <a title="<digi:trn key="aim:LocationoftheFile">URI Location of the document to be attached</digi:trn>"><digi:trn key="aim:file">File</digi:trn>
										  </a>
                                        </td>
                                        <td vAlign="middle" align="center">
										  &nbsp;&nbsp;&nbsp;<img src= "/TEMPLATE/ampTemplate/imagesSource/common/help.gif" border="0" title="<digi:trn key="aim:currencyFormatHint"> The file should have 3 columns: the first column contains currency codes (ex. CAD, or ETB), the second column contains rates (per 1 US dollar), and the 3rd column contains the dates (in format  dd-mm-yyyy).</digi:trn>"/>
										</td>                                   
                                        
                                        <td  vAlign="left" align="left">
                                        <!-- <html:file name="aimCurrencyRateForm" property="currRateFile" size="50" styleClass="dr-menu"/> -->
                                        <c:set var="trnUpdateValues">
                                            <digi:trn key="aim:UpdateValues">Update Values</digi:trn>
                                        </c:set>
 
                                        <a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
										 	<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
												<input id="currRateFile" name="currRateFile" type="file" class="file"/>
												<input type="button" value="${trnUpdateValues}" class="button" onclick="return updateRates()">
											</div>
                                        </a>
                                       
                                        </td>
                                       </tr>
                                    </table>
                                  </td>
                                  
                                </tr>
                                </table>
							</td>
                            </tr>
							<tr>
                            <td bgcolor="#ffffff" valign="top" align="left">
								<!-- Exchange rates table -->
								<table width="600" class="reportsBorderTable">
								<thead class="fixedHeader">
									<tr>
										<td align="center" width="23" class="clsTableTitleCol">
											<input type="checkbox" name="checkAll" onclick="checkall()">
										</td>
										<td align="center" width="45" class="clsTableTitleCol">
											<b><digi:trn key="aim:currCode">Code</digi:trn></b>
										</td>
										<td align="center" width="150" class="clsTableTitleCol">
											<b><digi:trn key="aim:currencyName">Currency Name</digi:trn></b>
										</td>
										<td align="center" width="55" class="clsTableTitleCol">
											<b><digi:trn> Source Currency Code</digi:trn></b>
										</td>
										<td align="center" width="150" class="clsTableTitleCol">
											<b><digi:trn>Source Currency Name</digi:trn></b>
										</td>
										<td align="center" width="85" class="clsTableTitleCol">
											<b><digi:trn key="aim:exchangeRateDate">Date</digi:trn></b>
										</td>
										<td align="center" width="65" class="clsTableTitleCol">
											<b><digi:trn key="aim:exchangeRate">Rate</digi:trn></b>
										</td>
									</tr>
									</thead>
								</table>
								<div id="demo" style="overflow: auto; width: 600px; height: 309px; max-height: 309px;" class="box-border-nopadding">
								<table id="dataTable" class="reportsBorderTable" width="100%" cellspacing="0" cellpadding="1" style="visibility: visible;">
									<c:if test="${empty aimCurrencyRateForm.currencyRates}">
									<tr bgcolor="#f4f4f2">
										<td colspan="5" align="center" class="reportsBorderTD">
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
										<td align="center" width="23" class="reportsBorderTD">
											<html:multibox property="selectedRates">
												<c:out value="${cRates.id}"/>
											</html:multibox>
										</td>
										<td align="left" width="45" class="reportsBorderTD">
                                          <digi:trn>${cRates.currencyCode}</digi:trn>
										</td>
										<td align="left" width="150" class="reportsBorderTD">
											<c:out value="${cRates.currencyName}"/>
										</td>
										<td align="center" width="55" class="reportsBorderTD">
                                         	${cRates.fromCurrencyCode}
										</td>
										<td align="center" width="150" class="reportsBorderTD">
											<c:out value="${cRates.fromCurrencyName}"/>
										</td>
										<td align="center" width="85" class="reportsBorderTD">
											<a href="javascript:editExchangeRate('<c:out value="${cRates.exchangeRateDate}"/>','<c:out value="${cRates.currencyCode}"/>')">
											<c:out value="${cRates.exchangeRateDate}"/>
											</a>
										</td>
										<td align="right" nowrap="nowrap" width="60" class="reportsBorderTD">
											<aim:formatNumber  maxFractionDigits="10" value="${cRates.exchangeRate}"> </aim:formatNumber>
										</td>
									</tr>
									</c:forEach>
									</c:if>
								</table>
								</div>
								</div>
										 </td>
									</tr>
								<!-- ========= TABLE AJOUT LEFT PB============= -->
								</table>
							</td></tr>
							<tr>
                              <td>
								<div style ="float:left; width:340px;">
	                                <c:set var="trnDelBtn">
	                                  <digi:trn key="aim:deleteSelectedRates">Delete Selected Rates</digi:trn>
	                                </c:set>
	                                <input type="button" value="${trnDelBtn}" class="dr-menu" onclick="deleteRates()">
								</div>
								<div style ="float:left;">
									<table cellPadding=0 cellSpacing=2 align="left" border=0 vAlign="center">
									<tr>
										<td  vAlign="left" width="170" align="center">
											<digi:trn key="aim:numRecordsPerPage">Number of records per page</digi:trn>:
										</td>
										<td  vAlign="left" width="50" align="center">
											<html:text property="numResultsPerPage" size="3" styleClass="inp-text"/>
										</td>
										<td vAlign="left" align="center">
                                          <c:set var="trnViewBtn">
                                            <digi:trn key="aim:viewBtn"> View </digi:trn>
                                          </c:set>
                                          <html:submit value="${trnViewBtn}" styleClass="dr-menu"/>
										</td>
									</tr>
								    </table>
								</div>
                              </td>
                            </tr>
							<c:if test="${!empty aimCurrencyRateForm.pages}">
							<tr><td>
								<!-- Pagination -->
								<table width="460" cellSpacing="1" cellPadding="2" vAlign="top" align="left">

									<tr>
										<td >
											
											<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
											<c:forEach var="currPage" items="${aimCurrencyRateForm.pages}">
												<c:if test="${currPage == aimCurrencyRateForm.currentPage}">
													<div  class = "mapagination" style ="padding:3px;border:2px solid #000000; color:#FF0000;">
													    <c:out value="${currPage}"/></div>
												</c:if>
												<c:if test="${currPage != aimCurrencyRateForm.currentPage}">
													<c:set target="${urlParams}" property="page">
													
														<c:out value="${currPage}"/>
													
													</c:set>
													
													<div class = "mapagination">
														<digi:link href="/showCurrencyRates.do" name="urlParams">
														<c:out value="${currPage}"/></digi:link>
													</div>
												</c:if>
												
											</c:forEach>
											<bean:size name="aimCurrencyRateForm" property="pages" id="totpages"/>
											<!-- 	<u><c:out value="${aimCurrencyRateForm.currentPage}"/></u> --> 
										<div style = "padding:3px;border:1px solid #999999; width:100px; height:10px; float:left;">
											<digi:trn key="aim:of">of</digi:trn>
											<u><c:out value="${totpages}"/></u>
											<digi:trn key="aim:page">Pages</digi:trn>
										</div>
										</td>
									</tr>
									<tr>
										<td>

										</td>
									</tr>
								</table>
							</td></tr>
							</c:if>

							<!--here-->

							<c:if test="${empty aimCurrencyRateForm.pages}">
							<tr><td>
								<!-- Pagination -->
								<table width="460" cellSpacing="1" cellPadding="2" vAlign="top" align="left">
									<tr>
										<td>
											 1 <digi:trn key="aim:of">of</digi:trn> 1
										</td>
									</tr>
								</table>
							</td></tr>
							</c:if>
							<!--end end-->
							<tr><td>
								<table width="600" cellSpacing="3" cellPadding="1" vAlign="top" align="left">
									<tr>
										<td align="left">
											<!-- <a href="javascript:addExchangeRate()">
                                            <digi:trn key="aim:AddNewExchangeRate">Add new exchange rate</digi:trn>
											</a>
											-->
										</td>
										<td align="right">
										<c:if test="${aimCurrencyRateForm.lastRateUpdate != null}">
											<digi:trn key="aim:LastUpdate">Last Update: </digi:trn>
											<c:out value="${aimCurrencyRateForm.lastRateUpdate}"/>
										</c:if>
										</td>
									</tr>
								</table>
							</td></tr>
				
						</table>
						</td>
						<td valign="top">
							<!--  =================LINKLINKLINKLINK=================== -->
								<table align=center cellPadding=0 cellSpacing=0 width="130" border=0 style ="  margin-top:136px; _margin-top:90px;">
										<tr>
											<td bgColor=#c9c9c7 class=box-title height="20">
												<digi:trn key="aim:Links">
												Links
												</digi:trn>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table cellPadding=5 cellSpacing=1 width="100%">
													<tr>
														<td>
															<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" 	width="15" height="10"/>
														</td>
														<td>
															<digi:link module="aim"  href="/../um/addUser.do">
																<a href="javascript:addExchangeRate()">
						                                            <digi:trn key="aim:AddNewExchangeRate">Add new exchange rate</digi:trn>
																</a>
															</digi:link>
														</td>
													</tr>																								
													<tr>
														<td>
															<digi:img src="/TEMPLATE/ampTemplate/imagesSource/arrows/arrow-014E86.gif" 	width="15" height="10"/></td>
														<td>
															<digi:link module="aim"  href="/admin.do">
															<digi:trn key="aim:AmpAdminHome">
															Admin Home
															</digi:trn>
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
</td></tr>
</table>

</digi:form>

<script type="text/javascript">
	initFileUploads();
</script>
<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
	setHoveredRow("rowHighlight");
</script>
