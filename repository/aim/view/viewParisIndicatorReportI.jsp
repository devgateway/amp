<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-nested" prefix="nested" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fmt" prefix="fmt" %>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

<%@ page import="java.util.*" %>
<%@ page import="org.digijava.module.aim.dbentity.*" %>
<%@ page import="org.digijava.module.aim.helper.*" %>
<%@ page import="org.digijava.module.categorymanager.util.*" %>
<%@ page import="org.digijava.module.categorymanager.dbentity.*" %>

<style>
<!--
.toolbar{
	width: 70px;
	background: #addadd; 
	background-color: #addadd; 
	padding: 3px 3px 3px 3px; 
	position: relative; 
	top: 10px; 
	left: 10px;
	bottom: 100px;
		
}
.toolbartable{
	border-color: #FFFFFF;
	border-width: 2px;
	border-bottom-width: 2px; 
	border-right-width: 2px;"
	border-left-width: 2px;
	border-style: solid;
}
-->
</style>

<style type="text/css">
#tabs {
	font-family: Arial,Helvetica,sans-serif;
	font-size: 8pt;
	clear: both;
	text-align: center;
}

#tabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#tabs li { 
	 float: left;
}

#tabs a, #tabs span { 
	font-size: 8pt;
}

#tabs ul li a { 
	background:#222E5D url(/TEMPLATE/ampTemplate/images/tableftcorner.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;
}

#tabs ul li a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcorner.gif) right top no-repeat;
	padding: 4px 10px 4px 10px;
}

#tabs ul li span a { 
	background:#3754A1 url(/TEMPLATE/ampTemplate/images/tableftcornerunsel.gif) no-repeat scroll left top;
	color:#FFFFFF;
	float:left;
	margin:0pt 0px 0pt 0pt;
	position:relative;
	text-decoration:none;
	top:0pt;
}

#tabs ul li span a div { 
	background: url(/TEMPLATE/ampTemplate/images/tabrightcornerunsel.gif) right top no-repeat;
	padding: 4px 5px 4px 5px;
}

#tabs a:hover {
    background: #455786 url(/TEMPLATE/ampTemplate/images/tableftcornerhover.gif) left top no-repeat;  
}

#tabs a:hover span {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}
#tabs a:hover div {
    background: url(/TEMPLATE/ampTemplate/images/tabrightcornerhover.gif) right top no-repeat;  
}

#tabs a.active {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 4px 10px 4px 10px;
	text-decoration: none;
	color: #333;
}

#tabs a.active:hover {
	position: relative;
	top: 0;
	margin: 0 2px 0 0;
	float: left;
	background: #FFF3B3;
	padding: 6px 10px 6px 10px;
	text-decoration: none;
	color: #333;
}


#subtabs ul {
	display: inline;
	list-style-type: none;
	margin: 0;
	padding: 0;
}

#subtabs li {
	float: left;
	padding: 0px 4px 0px 4px;
}

#subtabs a, #subtabs span { 
	font-size: 8pt; 
}

#subtabs a {
}

#subtabs ul li span {
	text-decoration: none;
}

#subtabs ul li div span {
	text-decoration: none;
}

#subtabs {
	text-align: center;
	font-family:Arial,Helvetica,sans-serif;
	font-size: 8pt;
	padding: 2px 4px 2px 4px;
	background-color:#CCDBFF;
}

#main {
	clear:both;
	text-align: left;
	border-top: 2px solid #222E5D;
	border-left: 1px solid #666;
	border-right: 1px solid #666;
	padding: 2px 4px 2px 4px;
}
html>body #main {
	width:742px;
}

#mainEmpty {
	border-top: 2px solid #222E5D;
	width: 750px;
	clear:both;
}
html>body #mainEmpty {
	clear:both;
	width:752px;
}

</style>

<style type="text/css">
	.td_top1 {
		border-top-style:solid; 
		border-top-color:#455786;
		border-top-width: 1px;
		border-right-style:solid; 
		border-right-color:#455786;
		border-right-width: 1px; 
		border-left-style:solid; 
		border-left-color:#455786;
		border-left-width: 1px;
	}
	
	.td_bottom1 {
		border-bottom-style:solid; 
		border-bottom-color:#455786;
		border-bottom-width: 1px;
		border-right-style:solid; 
		border-right-color:#455786;
		border-right-width: 1px; 
		border-left-style:solid; 
		border-left-color:#455786;
		border-left-width: 1px;
	}
	
	.td_right_left1 {
		border-right-style:solid; 
		border-right-color:#455786;
		border-right-width: 1px; 
		border-left-style:solid; 
		border-left-color:#455786;
		border-left-width: 1px;
	}
</style>

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>

<script language="JavaScript">
<!--
	function clearFilter()
	{
		//alert('clearFilter');
		<digi:context name="clearVal" property="context/module/moduleinstance/parisIndicatorReport.do" />
		//alert(document.getElementsByName('indicatorId')[0]);
		var param = document.getElementsByName('indicatorId')[0].value;
		document.getElementsByName('filterFlag')[0].value = "true";
		document.aimParisIndicatorReportForm.action = "<%= clearVal %>?indcId="+param;
		document.aimParisIndicatorReportForm.target = "_self";
		//document.aimParisIndicatorReportForm.submit();
		//document.getElementsByName('aimParisIndicatorReportForm')[0].submit();
	}

	function openPrinter(){
		var val = document.getElementsByName('indicatorId')[0].value;
		window.open('/aim/parisIndicatorReport.do~indcId='+val+'~reset=false~print=true','mywindow','toolbar=no,location=no,directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
		document.getElementsByName('print')[0].value = 'false';
	}

	function popup_pdf(val) {
		//openResisableWindow(1000,1000);
		<digi:context name="pdf" property="context/module/moduleinstance/parisIndicatorReportPDFXLSCSV.do?docType=pdf" />
		document.aimParisIndicatorReportForm.action = "<%= pdf %>&pid="+val;
		//document.aimParisIndicatorReportForm.target = popupPointer.name;
		document.aimParisIndicatorReportForm.submit();
	}

	function popup_csv() {
		openResisableWindow(800, 600);
		<digi:context name="csv" property="context/module/moduleinstance/parisIndicatorReportPDFXLSCSV.do?docType=csv" />
		document.aimParisIndicatorReportForm.action = "<%= csv %>&pid="+val;
		document.aimParisIndicatorReportForm.target = popupPointer.name;
		document.aimParisIndicatorReportForm.submit();
	}

	function popup_xls(val) {
		//openResisableWindow(800, 600);
		<digi:context name="xls" property="context/module/moduleinstance/parisIndicatorReportPDFXLSCSV.do?docType=xls" />
		document.aimParisIndicatorReportForm.action = "<%= xls %>&pid="+val;
		//document.aimParisIndicatorReportForm.target = popupPointer.name;
		document.aimParisIndicatorReportForm.submit();
	}

	function popup_warn() {
		alert("Year Range selected should NOT be Greater than 3 Years.");
	}

	function chkYear(val) {
		var stYr = document.getElementsByName('startYear')[0].value;
		var clYr = document.getElementsByName('closeYear')[0].value;
		if (clYr < stYr) {
			if (val == 'start')
				alert("Start-year can not be greater than Close-year.");
			else
				alert("Close-year can not be less than Start-year.");
			return false;
		}
	}
-->
</script>

<digi:errors/>

<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> 
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn> 
</c:set>

<script language="JavaScript">
function toggleSettings(){
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings} &gt;&gt;";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings} &lt;&lt;";
	}
}
</script>

<!--<digi:form action="/parisIndicatorReport.do" >-->
<!--	<html:hidden property="filterFlag" />-->
<!--	<html:hidden property="indicatorId" />-->

<digi:instance property="aimParisIndicatorReportForm" />

<logic:equal name="aimParisIndicatorReportForm" property="print" value="false">
	<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />
	<link rel="stylesheet" href="<digi:file src="/repository/parisindicator/view/css/pi_styles.css"/>">
</logic:equal>

<div id="myFilter2" style="display: none;" >
	<jsp:include page="/aim/parisIndicatorPopupAction.do" />
</div>
<div id="myFilter" style="display: none;" >
	<jsp:include page="viewParisIndicatorPopupFilter.jsp" />
</div>

<jsp:include page="/repository/aim/view/viewParisIndicatorPopupScripts.jsp"/>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<jsp:include page="/repository/aim/view/saveReports/dynamicSaveReportsAndFilters.jsp" />
<bean:define id="formPI" name="aimParisIndicatorReportForm" type="org.digijava.module.aim.form.ParisIndicatorReportForm"/>

<table border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF" >
	<tr>
		<td width="1000" align="left" valign="top" BORDER="1" style="padding-left:5px;">
			<table width="100%"  border="0" cellpadding="5" cellspacing="0">
				<tr><td>&nbsp;</td></tr>
				<logic:equal name="aimParisIndicatorReportForm" property="print" value="false">
				<tr>
					<td>
						<div style="margin-left:5px;margin-right:5px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif;">
							<span style="cursor:pointer;font-style: italic;float:right;" onClick="toggleSettings();" id="displaySettingsButton">${showCurrSettings} &lt;&lt;</span>
							<span style="cursor: pointer; float: left;">
								<a class="settingsLink" onClick="showFilter(); " >
				                	<digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn>
				                </a>
							</span>
							<br>
							<div style="display:none;background-color:#FFFFCC;padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
					           	<table cellpadding="0" cellspacing="0" border="0" width="100%">
					           		<tr>
					           			<td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">					
											<table border="0" cellspacing="0" cellpadding="0" >
												<tr height=20>
													<td>
														<table>
															<td>
																<strong><digi:trn key="rep:pop:SelectedRangeStartYear">Start Year:</digi:trn></strong>&nbsp;${aimParisIndicatorReportForm.startYear}&nbsp;|
																<strong><digi:trn key="rep:pop:SelectedRangeEndYear">End Year:</digi:trn></strong>&nbsp;${aimParisIndicatorReportForm.closeYear}&nbsp;|				
																<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a'}">
																	<%String cal = "All";
																	Iterator iterCal = formPI.getCalendarColl().iterator();
																	while(iterCal.hasNext()){
																		AmpFiscalCalendar auxFC = (AmpFiscalCalendar)iterCal.next();
																		if(auxFC.getAmpFiscalCalId().toString().equals(formPI.getCalendar())){
																			cal = auxFC.getName();
																		}
																	}%>
																	<strong><digi:trn key="rep:pop:CalendarType">Calendar Type:</digi:trn></strong>&nbsp;<%=cal%>&nbsp;|
						
																	<%String curr = "All";
																	Iterator iterCurr = formPI.getCurrencyColl().iterator();
																	while(iterCurr.hasNext()){
																		AmpCurrency auxCurr = (AmpCurrency)iterCurr.next();
																		if(auxCurr.getCurrencyCode().toString().equals(formPI.getCurrency())){
																			curr = auxCurr.getCurrencyName();
																		}
																	}%>
																	<strong><digi:trn key="rep:pop:CurrencyType">Currency Type:</digi:trn></strong>&nbsp;<%=curr%>&nbsp;|
																</c:if>
																<%String donorAux = "All";
																Iterator iterDonor = formPI.getDonorColl().iterator();
																while(iterDonor.hasNext()){
																	AmpOrganisation auxAmpOrg = (AmpOrganisation) iterDonor.next();
																	if(auxAmpOrg.getAmpOrgId().toString().equals(formPI.getDonor())){
																		donorAux = auxAmpOrg.getAcronym();
																	}
																}%>
																<strong><digi:trn key="rep:pop:Donors">Donors:</digi:trn></strong>&nbsp;<%=donorAux%>&nbsp;|			
																<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a'}">
																	<%
																		String statusName = "All";
																																Collection ampCategoryValues = CategoryManagerUtil.loadAmpCategoryClassByKey(org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY).getPossibleValues();
																																if (ampCategoryValues != null && ampCategoryValues.size() > 0) {
																																	Iterator iterator = ampCategoryValues.iterator();
																																	while (iterator.hasNext()) {
																																		AmpCategoryValue ampCategoryValue = (AmpCategoryValue)iterator.next();
																																		if (ampCategoryValue!=null){
																																			String outputValue = CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request);
																																			if ( formPI.getStatus() != null && formPI.getStatus() == ampCategoryValue.getId().longValue() /*|| 
																																					( valueIdsColl != null && valueIdsColl.contains(ampCategoryValue.getId()) ) */) {
																																				statusName = outputValue;
																																			}
																																		}
																																	}
																																}
																	%>
																	<strong><digi:trn key="rep:pop:StatusType">Status Type:</digi:trn></strong>&nbsp;<%=statusName%>&nbsp;|
																</c:if>
																<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10a'}">
																	<%
																		String groupsAux = "All";
																																Iterator iterGroups = formPI.getOrgGroupColl().iterator();
																																while(iterGroups.hasNext()){
																																	AmpOrgGroup auxAmpOrg = (AmpOrgGroup) iterGroups.next();
																																	if(auxAmpOrg.getOrgGrpCode().equals(formPI.getOrgGroup())){
																																		groupsAux = auxAmpOrg.getOrgGrpName();
																																	}
																																}
																	%>
																	<strong><digi:trn key="rep:pop:Groups">Groups:</digi:trn></strong>&nbsp;<%=groupsAux%>&nbsp;|
																</c:if>
															</tr>
														</table>
													</td>
												</tr>
												<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a'}">
													<tr height=20>
														<td>
															<%
																String groupsAux = "All";
																												Iterator iterGroups = formPI.getOrgGroupColl().iterator();
																												while(iterGroups.hasNext()){
																													AmpOrgGroup auxAmpOrg = (AmpOrgGroup) iterGroups.next();
																													if(auxAmpOrg.getOrgGrpCode().equals(formPI.getOrgGroup())){
																														groupsAux = auxAmpOrg.getOrgGrpName();
																													}
																												}
															%>
															<strong><digi:trn key="rep:pop:Groups">Groups:</digi:trn></strong>&nbsp;<%=groupsAux%>&nbsp;|
															<%
																String indName = "All";
																												Collection ampCategoryValues = CategoryManagerUtil.loadAmpCategoryClassByKey(org.digijava.module.categorymanager.util.CategoryConstants.FINANCING_INSTRUMENT_KEY).getPossibleValues();
																												if (ampCategoryValues != null && ampCategoryValues.size() > 0) {
																													Iterator iteratorInd = ampCategoryValues.iterator();
																													while (iteratorInd.hasNext()) {
																														AmpCategoryValue ampCategoryValue = (AmpCategoryValue)iteratorInd.next();
																														if (ampCategoryValue!=null){
																															String outputValue = CategoryManagerUtil.translateAmpCategoryValue(ampCategoryValue, request);
																															if ( formPI.getFinancingInstrument() != null && formPI.getFinancingInstrument().equals(ampCategoryValue.getId()) /*|| 
																																	( valueIdsColl != null && valueIdsColl.contains(ampCategoryValue.getId()) ) */) {
																																indName = outputValue;
																															}
																														}
																													}
																												}
															%>
															<strong><digi:trn key="rep:filer:financingInstrument">Financing Instrument</digi:trn>:</strong>&nbsp;<%=indName%>&nbsp;|
															<%String sectorAux = "All";
															Iterator iterSector = formPI.getSectorColl().iterator();
															while(iterSector.hasNext()){
																AmpSector auxAmpSector = (AmpSector) iterSector.next();
																if(auxAmpSector.getAmpSectorId().toString().equals(formPI.getSector())){
																	sectorAux = auxAmpSector.getName();
																}
															}%>
															<strong><digi:trn key="rep:pop:sectors">Sectors:</digi:trn></strong>&nbsp;<%=sectorAux%>&nbsp;|
														</td>
													</tr>
												</c:if>
											</table>
							           	</td>
							       	</tr>
					           </table>
				           	</div>
				    	</div>
					</td>
				</tr>			
				<tr>
					<td>
						<div class="toolbar" align="center">
							<table border="0" align="center" bgcolor="#addadd" class="toolbartable">
								<tr>
									<td noWrap align=left valign="center">
										<a href="" onclick="javascript:popup_pdf('<bean:write name="aimParisIndicatorReportForm" property="indicatorCode" />'); return false;" paramName="indcId" paramId="indcId" target="_blank">
											<digi:img width="17" height="20" hspace="2" vspace="2"src="module/aim/images/pdf.gif" border="0" alt="Export to PDF" />
										</a>
									</td>
									<td noWrap align=left valign="center">
										<a href="" onclick="javascript:popup_xls('<bean:write name="aimParisIndicatorReportForm" property="indicatorCode" />'); return false;" paramName="indcId" paramId="indcId" target="_blank">
											<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/excel.gif" border="0" alt="Export to Excel" />
										</a>
									</td>				
									<td noWrap align=left valign="center">
										
										<a href="#" paramName="indcId" paramId="indcId" onclick="javascript:openPrinter(); return false;" paramName="indcId" paramId="indcId" target="_blank">
											<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/printer.gif" border="0" alt="Printer Friendly" />
										</a>
										
									</td>
								</tr>
							</table>
						</div>
					</td>
				</tr>
				</logic:equal>
				<tr>
			    	<td>
			        	<table align="left">
				        	<tr align="left">
				            	<td class="subtitle-blue-3" style="width:70%;text-align:left;">
				               		<digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn> <digi:trn key="aim:report">Report</digi:trn> ${aimParisIndicatorReportForm.indicatorCode}&nbsp;
				             	</td>
				             	<td class="subtitle-blue-3" style="width:250px;text-align:right;">
				               		<feature:display  name="Target Value" module="PI Reports">
				                 		<c:if test="${!empty aimParisIndicatorReportForm.targetValue && !empty aimParisIndicatorReportForm.calcResult}">${aimParisIndicatorReportForm.targetValue}&nbsp;
						                	<digi:trn key="aim:piTargerValue">Target</digi:trn>: ${aimParisIndicatorReportForm.calcResult}%
				                 		</c:if>
				               		</feature:display>&nbsp;
				            	</td>
				        	</tr>
			         	</table>
			       	</td>
				</tr>
				<tr>
					<td colspan=3 class=box-title align=center></td>
				</tr>
				<logic:equal name="aimParisIndicatorReportForm" property="print" value="false">
				<tr>
					<td>
						<logic:notEmpty name="aimParisIndicatorReportForm"  property="indicatorsColl">
							<div id="content"  class="yui-skin-sam" style="padding-left:10px;width:98%;width:1000px;"> 
<!--							<div style="padding-left:5px;width:1000px;">-->
								<div id="demo" class="yui-navset">
									<ul id="MyTabs" class="yui-nav"">
										<logic:iterate id="report" name="aimParisIndicatorReportForm"  property="indicatorsColl" type="org.digijava.module.aim.dbentity.AmpAhsurveyIndicator" indexId="tabIndex">
											<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
											<c:if test="${report.indicatorCode != '10b'}">
								            	<feature:display  name="PI report ${report.indicatorCode}" module="PI Reports">
													<c:set target="${urlParams}" property="indcId" value="${report.ampIndicatorId}" />
													<c:set target="${urlParams}" property="reset" value="true" />
													<c:set target="${urlParams}" property="print" value="false" />
													<c:set target="${urlParams}" property="print2" value="false" />
													<!--<LI>-->
													<!--<a name="node"><div>-->
													<!--<digi:trn key="aim:parisIndicator:${report.indicatorCode}"><c:out value="Paris Indicator ${report.indicatorCode}"/></digi:trn>-->
													<!--</div></a>-->
													<!--</LI>-->
													<%int newTabIndex = tabIndex.intValue() + 1;%>
													<LI class='<%=(request.getParameter("indcId").equals(new Integer(newTabIndex).toString()))?"selected":""%>'>
														<span>
															<digi:link href="/parisIndicatorReport.do" name="urlParams" title="${translation}" target="_self">
															<div style="max-width: 90px">
																<digi:trn key="aim:parisIndicator:${report.indicatorCode}"><c:out value="Paris Indicator ${report.indicatorCode}"/></digi:trn>
									                        </div>
															</digi:link>
								                    	</span>
													</LI>
								          		</feature:display>
											</c:if>
										</logic:iterate>
									</UL>
								</DIV>
							</div>
						</logic:notEmpty>
					</td>
				</tr>
				</logic:equal>
				<tr>
					<td width="90%" class="td_top1" style="padding-left:2px;" >
						<table border="0" cellpadding="0" cellspacing="0" width="100%" height="169">
							<tr>
								<td colspan="2" ></td>
							</tr>
							<tr>
								<td style="padding:5">
									<strong><p><img src="/TEMPLATE/ampTemplate/images/info.png" width="15" height="15">
									<c:if test="${aimParisIndicatorReportForm.indicatorName != null}">
									<digi:trn key="aim:parisIndicator:name"> ${aimParisIndicatorReportForm.indicatorName}</digi:trn>
									</c:if>
									</p></strong>
								</td>
							</tr>
							<tr align="top">
								<td>
									<!--for headers and data-->
									<table cellspacing="0" cellpadding="0" border="1" width="100%" 
										style="font-family: Arial, Helvetica, sans-serif; padding-right:5px; padding-left:5px; padding-top:5px;border-top-style:hidden;border-right-style:hidden;border-left-style:hidden;border-bottom-style:hidden">
										<tr align="center"  bgcolor="#CCCCFF">
											<td width="15%" height="33">
												<div align="center">
													<strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong>
												</div>
											</td>
											<td width="5%" height="33">
												<div align="center">
													<strong><digi:trn key="aim:disbursmentYear">Disbursement Year</digi:trn></strong>
												</div>
											</td>
											<!--Start of Indicator headers selection here-->
											<!-- Indicator 3 -->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '3'}">
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:aidFlowsGovernmentSectorReported">Aid flows to the government sector reported on the government's budget</digi:trn></strong>
													</div>
												</td>
												<td width="26%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:totalAidFlowsDisbursed">Total Aid flows disbursed to the government sector</digi:trn></strong>
													</div>
												</td>
											</c:if>
											<!--Indicator 4 -->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '4'}">
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:VolumeOfTehnicalCoOperation">Volume of technical co-operation for capacity development provided through co-ordinated programmes</digi:trn></strong>
													</div>
												</td>
												<td width="26%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:totalVolumeOfTehnicalCoOperation">Total volume of technical co-operation provided</digi:trn></strong>
													</div>
												</td>
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:noOfTC">% of TC for capacity development provided through coordinated programmes consistent with national development strategies</digi:trn></strong>
													</div>
												</td>
											</c:if>
											<!--Indicator 5a -->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5a'}">
												<td width="11%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:aidFlowsToGovernmentSectorBudget">Aid flows to the goverment sector that use national budget execution procedures</digi:trn></strong>
													</div>
												</td>
												<td width="11%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:aidFlowsToGovernmentSectorFinancialReporting">Aid flows to the goverment sector that use national financial reporting procedures</digi:trn></strong>
													</div>
												</td>
												<td width="11%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:aidFlowsToGovernmentSectorFinancialAuditing">Aid flows to the goverment sector that use national financial auditing procedures</digi:trn></strong>
													</div>
												</td>
												<td width="11%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:odaThatUses">ODA that uses all 3 national PFM</digi:trn></strong>
													</div>
												</td>
												<td width="11%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:totalAidFlowsDisbursed">Total aid flows disbursed to the government sector</digi:trn></strong>
													</div>
												</td>
<!--												<td width="0%" height="0" style="visibility: hidden">-->
<!--													<div align="center">-->
<!--														<strong><digi:trn key="aim:proportionAidFlowsUsingOne">Proportion aid flows to the government sector using one of the 3 country PFM systems</digi:trn></strong>-->
<!--													</div>-->
<!--												</td>-->
												<td width="11%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:proportionOfAidFlowsUsingAll">Proportion of aid flows to the government sector using all the 3 country PFM systems</digi:trn></strong>
													</div>
												</td>
											</c:if>
											<!--Indicator 5b -->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '5b'}">
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:aidFlowsToTheGovernmentSector">Aid flows to the government sector that use national procurement procedures</digi:trn></strong>												
													</div>
												</td>
												<td width="26%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:totalAidFlowsDisbured">Total aid flows disbursed to the government sector</digi:trn></strong>
													</div>
												</td>
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:proportionOfAidFlowsToTheGovernmentSector">Proportion of aid flows to the government sector using national procurement procedures</digi:trn></strong>
													</div>
												</td>
											</c:if>
											<!--Indicator 9 -->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '9'}">
												<td width="20%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:budgetSupportAidFlowsInTheContextOfProgammeBasedApproach">Budget support aid flows provided in the context of programme based approach</digi:trn></strong>
													</div>
												</td>
												<td width="20%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:otherAidFlowsProvidedInTheContextOfProgrammeBasedApproach">Other aid flows provided in the context of programme based approach</digi:trn></strong>
													</div>
												</td>
												<td width="20%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:totalAidFlowsProvided">Total aid flows provided</digi:trn></strong>
													</div>
												</td>
												<td width="20%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:proportionOfAidFlowsInTheContextOfProgrammeBasedApproach">Proportion of aid flows provided in the context of programme based approach</digi:trn></strong>
													</div>
												</td>
											</c:if>
											<!--Indicator 10a -->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10a'}">
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:numberOfMissions">Number of missions to the field that are joint</digi:trn></strong>
													</div>
												</td>
												<td width="26%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:totalNumberOfMissions">Total number of missions to the field</digi:trn></strong>
													</div>
												</td>
											</c:if>
											<!--Indicator 10b -->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode == '10b'}">
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:numberOfCountryAnalytic">Number of country analytic reports that are joint</digi:trn></strong>
													</div>
												</td>
												<td width="26%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:totalNumberOfCountryAnalytic">Total number of country analytic reports</digi:trn></strong>
													</div>
												</td>
											</c:if>
											<!-- end of c:if for all the Indicators-->
											<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '4'  &&
															aimParisIndicatorReportForm.indicatorCode != '5a' &&
															aimParisIndicatorReportForm.indicatorCode != '5b' &&
															aimParisIndicatorReportForm.indicatorCode != '9'}">
												<td width="27%" height="33">
													<div align="center">
														<strong><digi:trn key="aim:${aimParisIndicatorReportForm.indicatorNameTrn}"><c:out value="${aimParisIndicatorReportForm.indicatorName}"/></digi:trn></strong>
													</div>
											  	</td>
											</c:if>
										</tr>
								  		<c:set var="numCols" value="${aimParisIndicatorReportForm.numColsCalculated}" />
										<logic:empty name="aimParisIndicatorReportForm" property="donorsColl">
											<tr>
												<td width="100%" align="center" height="65" colspan='<c:out value="${numCols}" />' >
													<div align="center">
														<strong><font color="red"><digi:trn key="aim:noSurveyDataFound">No survey data found.</digi:trn></font></strong>
													</div>
												</td>
											</tr>
										</logic:empty>
								  		<logic:notEmpty name="aimParisIndicatorReportForm" property="donorsColl">
								  			<% boolean flag = false; %>
								  			<% int indexRow = 0; int indexRow2 = 0;%>
											<nested:iterate name="aimParisIndicatorReportForm" property="donorsColl">
												<% indexRow2++; %>
									    		<%if(indexRow2 % 2 == 0){%>
												<tr>
												<%}else{%>
												<tr bgcolor="#EBEBEB">
												<%}%>
													<c:set var="yearRange" value="${aimParisIndicatorReportForm.closeYear - aimParisIndicatorReportForm.startYear + 1}" />
													<td width="15%" align="center" height="65" rowspan='${yearRange}' >
														<div align="center">
															<c:set var="key"><nested:write property="donor"/></c:set>
				                                            <c:set var="key">${fn:replace(key, " ", "")}</c:set>
				                                            <c:set var="key">${fn:replace(key, "%", "")}</c:set>
		                                            		<c:set var="key">${fn:toLowerCase(key)}</c:set>
															<strong><digi:trn key="${key}"><nested:write property="donor" /></digi:trn></strong>
														</div>
													</td> 
													<nested:iterate property="answers"> 
														<%if (flag) {%>
															<%if(indexRow2 % 2 == 0){%>
															<tr>
															<%}else{%>
															<tr bgcolor="#EBEBEB">
															<%}%>
														<%}%>
															<c:set var="index" value="1" />

															<c:set var="firstVal" value="" />
															<c:set var="secondVal" value="" />
                                                            <c:set var="thirdVal" value="" />
															<nested:iterate id="rowVal" indexId="ansIndex">
																<c:if test="${ansIndex == 1}">
																	<c:set var="firstVal" value="${rowVal}" />
																</c:if>
																<c:if test="${ansIndex == 2}">
																	<c:set var="secondVal" value="${rowVal}" />
																</c:if>
                                                                <c:if test="${ansIndex == 3}">
                                                                    <c:set var="thirdVal" value="${rowVal}" />
                                                                </c:if>
                                                                <c:if test="${ansIndex == 4}">
                                                                    <c:set var="fourthVal" value="${rowVal}" />
                                                                </c:if>
                                                                <c:if test="${ansIndex == 5}">
                                                                    <c:set var="fifthVal" value="${rowVal}" />
                                                                </c:if>
																<% indexRow++; %>
                                                                <%String visible = ""; boolean show = true;%>
																<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a' && ansIndex == 7}">
                                                                    <%visible = "style='visibility: hidden'"; show = false;%>
                                                                </c:if>
                                                                <%if(show){ %>
                                                                <td>
																	<div align="center">
																		<c:if test="${index == 1}">
																			<fmt:formatNumber type="number" value="${rowVal}" pattern="####" maxFractionDigits="0" />
																		</c:if>
																		<c:if test="${index != 1}">
																			<c:choose>
																				<c:when test="${index < (numCols-1)}">
																					<fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />
																				</c:when>
																				<c:when test="${index == (numCols-1)}">
																					<c:choose>
																						<c:when test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">
<!--                                                                                            <c:if test="${rowVal <= -1}">-->
<!--																								<c:if test="${rowVal <= -1}">n.a.</c:if>-->
<!--																								<c:if test="${rowVal > -1}">-->
<!--	                                                                                                <c:if test="${firstVal+secondVal+thirdVal > 0}">-->
<!--																									    <fmt:formatNumber type="number" value="${(firstVal+secondVal+thirdVal)*100/3/fifthVal}" maxFractionDigits="0" />%-->
<!--	                                                                                                </c:if>-->
<!--	                                                                                                <c:if test="${firstVal+secondVal+thirdVal <= 0}">n.a.</c:if>-->
<!--																								</c:if>-->
<!--                                                                                            </c:if>-->
                                                                                            <c:if test="${firstVal+secondVal+thirdVal > 0}">
                                                                                                        <fmt:formatNumber type="number" value="${fourthVal*100/fifthVal}" maxFractionDigits="0" />%
                                                                                                    </c:if>
                                                                                                    <c:if test="${firstVal+secondVal+thirdVal <= 0}">n.a.</c:if>
																						</c:when>
																						<c:otherwise >
																							<fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />
																						</c:otherwise>
																					</c:choose>
																				</c:when>
																				<c:otherwise >
																					<c:if test="${rowVal <= -1}">n.a.</c:if>
																					<c:if test="${rowVal > -1}">
																						<c:if test="${ansIndex == 3}">
                                                                                            <c:if test="${secondVal != 0}">
                                                                                                <fmt:formatNumber type="number" value="${firstVal*100/secondVal}" maxFractionDigits="0"/>%
                                                                                            </c:if>
                                                                                            <c:if test="${secondVal == 0}">n.a.</c:if>
																						</c:if>
																						<c:if test="${ansIndex != 3}">
                                                                                            <c:if test="${aimParisIndicatorReportForm.indicatorCode == '9'}">
                                                                                                 <c:if test="${thirdVal != 0}">
																							         <fmt:formatNumber type="number" value="${(firstVal+secondVal)*100/thirdVal}" maxFractionDigits="0" />%
                                                                                                 </c:if>
                                                                                                 <c:if test="${thirdVal == 0}">n.a.</c:if>
                                                                                            </c:if>
                                                                                            <c:if test="${aimParisIndicatorReportForm.indicatorCode != '9'}">
                                                                                                <c:if test="${aimParisIndicatorReportForm.indicatorCode != '5a'}">
                                                                                                    <fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />%
                                                                                                </c:if>
                                                                                                <c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">
                                                                                                    <c:if test="${firstVal+secondVal+thirdVal > 0}">
                                                                                                        <fmt:formatNumber type="number" value="${fourthVal*100/fifthVal}" maxFractionDigits="0" />%
                                                                                                    </c:if>
                                                                                                    <c:if test="${firstVal+secondVal+thirdVal <= 0}">n.a.</c:if>
                                                                                                </c:if>
                                                                                            </c:if>
																						</c:if>
																					</c:if>
																				</c:otherwise>
																			</c:choose>
																		</c:if>
																		<c:set var="index" value="${index + 1}" />
																	</div>
																</td>
                                                                <%} %>
															</nested:iterate>
															<% flag = true; %>
														</tr>
													</nested:iterate>
													<% flag = false; %>
												</tr>
											</nested:iterate>
										</logic:notEmpty> 
									</table>
								</td>
							</tr>
							<c:if test = "${aimParisIndicatorReportForm.indicatorCode != '10a' && aimParisIndicatorReportForm.indicatorCode != '10b'}">
								<tr>
									<td>
										<font color="blue">* 
											<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
												<digi:trn key="aim:allTheAmounts">All the amounts are in thousands (000) </digi:trn>
											</gs:test>
		                                    <c:set var="selCurrency">${aimParisIndicatorReportForm.currency}</c:set>
											<c:set var="selCurrency">${fn:replace(selCurrency," ","")}</c:set>
		                                    <c:set var="selCurrency">${fn:replace(selCurrency,"%","")}</c:set>
		                                    <c:set var="selCurrency">${fn:toLowerCase(selCurrency)}</c:set>
		                                    <digi:trn key="aim:currency:${selCurrency}">${selCurrency}</digi:trn>
										</font>
									</td>
								</tr>
							</c:if>
						</table>
					</td>
				</tr> 
				<!-- Mini table -->
				<tr> 
					<td class="td_right_left1">
						<logic:notEmpty name="aimParisIndicatorReportForm" property="donorsColl">
							<table width="100%" cellspacing="0" cellpadding="0" border="0">
								<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a' || aimParisIndicatorReportForm.indicatorCode == '5b'}">
									<c:set var="range" value="${aimParisIndicatorReportForm.closeYear - aimParisIndicatorReportForm.startYear + 1}" />
									<c:set var="cntr" value="0" />
									<tr>
										<td align="center" colspan='<c:out value="${numCols + 1}" />' >
											<table border="1" width="50%" cellspacing="0" cellpadding="0">
												<c:set var="flag" value="true" />
												<nested:iterate name="aimParisIndicatorReportForm" property="donorsCollIndc5">
													<c:if test="${flag == true}">
														<tr bgcolor="#F4F4F2">
															<nested:iterate id="rowVal">
																<c:if test="${cntr == 0}">
																	<td align="center" rowspan="2">
																		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">
																			<strong><digi:trn key="aim:percentOfODAUsingAll">Percent of ODA using all three partner's PFM procedures</digi:trn></strong>
																		</c:if>
																		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5b'}">
																			<strong><digi:trn key="aim:percentOdODAUsingNational">Percent of ODA using national procurement systems</digi:trn></strong>
																		</c:if>
																	</td>
																	<td align="center" colspan='<c:out value="${range}" />' >	
																		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5a'}">
																			<strong><digi:trn key="aim:percentOfDonorsThatUseAllThree">Percent of donors that use all three partner's PFM procedures</digi:trn></strong>
																		</c:if>
																		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '5b'}">
																			<strong><digi:trn key="aim:percentOfDonorsThatUseNational">Percent of donors that use national procurement systems</digi:trn></strong>
																		</c:if>	
																	</td>
																</c:if>
																<c:if test="${cntr != 0}">
																	<c:if test="${cntr == 1}">
																		<tr>
																	</c:if>
																	<td align="center">
																		<strong><c:out value="${rowVal}"/></strong>
																	</td>
																	<c:if test="${cntr == range}">
																		</tr>
																	</c:if>
																</c:if>
																<c:set var="cntr" value="${cntr + 1}" />
															</nested:iterate>
														</tr>
													</c:if>
													<c:if test="${flag == false}">
														<tr>
															<nested:iterate id="rowVal">
																<c:if test="${flag == false}">
																	<td align="center">
				                                                      	<c:set var="rowValTrn">${fn:replace(rowVal, " ", "_")}</c:set>
				                                                      	<c:set var="rowValTrn">${fn:replace(rowValTrn, "%", "")}</c:set>
																		<strong><digi:trn key="aim:${rowValTrn}">${rowVal}</digi:trn></strong>
				                                                    </td>
																</c:if>
																<c:if test="${flag == true}">
																	<td align="center">
																		<c:out value="${rowVal}"/>
																		<%--<fmt:formatNumber type="number" value="${rowVal}" maxFractionDigits="0" />--%>
																	</td>
																</c:if>
																<c:set var="flag" value="true" />
															</nested:iterate>
														</tr>
													</c:if>
													<c:set var="flag" value="false" />
												</nested:iterate>
											</table>
										</td>
										<td align="right" width="14">&nbsp;</td>
									</tr>
								</c:if>
							</table>
						</logic:notEmpty>
					</td> 
				</tr>
				<tr>
					<td class="td_bottom1">&nbsp;</td>
				</tr>   
			</table> 
		</td>
	</tr>
</table>
</digi:form>