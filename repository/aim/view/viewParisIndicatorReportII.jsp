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
		<digi:context name="clearVal" property="context/module/moduleinstance/parisIndicatorReport.do" />
		var param = document.aimParisIndicatorReportForm.indicatorId.value;
		document.aimParisIndicatorReportForm.filterFlag.value = "true";
		document.aimParisIndicatorReportForm.action = "<%= clearVal %>?indcId=" + param;
		document.aimParisIndicatorReportForm.target = "_self";
		document.aimParisIndicatorReportForm.submit();
	}

	function openPrinter(){
		var val = document.getElementsByName('indicatorId')[0].value;
		window.open('/aim/parisIndicatorReport.do~indcId='+val+'~reset=true','mywindow','toolbar=no,location=no,directories=no,status=no,menubar=yes,scrollbars=yes,copyhistory=yes,resizable=yes');
	}
	
	function popup_pdf(val) {
		//openResisableWindow(800, 600);
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
		var stYr = document.aimParisIndicatorReportForm.startYear.value;
		var clYr = document.aimParisIndicatorReportForm.closeYear.value;
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

<digi:instance property="aimParisIndicatorReportForm" />

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
				<tr>
					<td>
						<div style="margin-left:5px;margin-right:5px;background-color:#ccdbff;padding:2px 2px 2px 2px;Font-size:8pt;font-family:Arial,Helvetica,sans-serif; ">
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
										<a href="" onclick="javascript:popup_pdf('<bean:write name="aimParisIndicatorReportForm" property="indicatorCode" />');return false;" paramName="indcId" paramId="indcId" target="_blank">
											<digi:img width="17" height="20" hspace="2" vspace="2"src="module/aim/images/pdf.gif" border="0" alt="Export to PDF" />
										</a>
									</td>
									<td noWrap align=left valign="center">
										<a href="" onclick="javascript:popup_xls('<bean:write name="aimParisIndicatorReportForm" property="indicatorCode" />');return false;" paramName="indcId" paramId="indcId" target="_blank">
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
				<tr>
					<td width="90%" class="td_top1" style="padding-left:2px;" >
						<table border="0" cellpadding="0" cellspacing="0" width="100%" height="169">
							<tr>
								<td colspan="2" ></td>
							</tr>
							<tr>
								<td style="padding:5">
									<strong><p><img src="/TEMPLATE/ampTemplate/images/info.png" width="15" height="15"><digi:trn key="aim:parisIndicator:name"> ${aimParisIndicatorReportForm.indicatorName}</digi:trn></p></strong>
									<c:if test="${aimParisIndicatorReportForm.indicatorCode == 6}">
										[<digi:trn key="aim:numParallelPIU">Number Of Parallel PIUs</digi:trn>]
									</c:if>
								</td>
							</tr>
							<tr align="top">
								<td>
									<!--for headers and data-->
									<table cellspacing="0" cellpadding="0" border="1" width="100%" 
										style="font-family: Arial, Helvetica, sans-serif; padding-right:5px; padding-left:5px; padding-top:5px;border-top-style:hidden;border-right-style:hidden;border-left-style:hidden;border-bottom-style:hidden">
										<tr align="center"  bgcolor="#F4F4F2">
											<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">
												<td width="15%" height="25">
													<div align="center"><strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong></div>
											    </td>
											</c:if>
											<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
												<td width="15%" height="25" rowspan="2">
													<div align="center"><strong><digi:trn key="aim:donors">Donor(s)</digi:trn></strong></div>
											  	</td>
											</c:if>
											<%-- Loop-1[Years] starts here --%>
											<c:set var="stYear" value="${aimParisIndicatorReportForm.startYear}" />
											<c:set var="clYear" value="${aimParisIndicatorReportForm.closeYear}" />
											<c:set var="yrRange" value="${clYear - stYear + 1}" />
											<logic:iterate id="year" name="aimParisIndicatorReportForm" property="yearColl">
											  	<c:if test="${year == stYear}">
											  		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">
											  			<td width="75" height="25">
											  		</c:if>
											  		<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
											  			<td width="15%" height="25" colspan="3">
											  		</c:if>
										  			<div align="center">
														<strong><c:out value="${stYear}" /></strong>
													</div>
											 		</td>
											 		<c:if test="${stYear != clYear}">
											 			<c:set var="stYear" value="${stYear + 1}" />
											 		</c:if>
											 	</c:if>
									 		</logic:iterate>
											<%-- Loop-1[Years] ends here --%>
										</tr>
										<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
											<tr align="center"  bgcolor="#F4F4F2">
												<%-- Loop-2 starts here --%>
												<c:forEach begin="1" end="${yrRange}" step="1">
													<td width="15%" height="25">
														<div align="center">
															<strong><digi:trn key="aim:aidFlowsToTheGovernmentSectorScheduled">Aid flows to the government sector scheduled for fiscal year</digi:trn></strong>
														</div>
													 </td>
													 <td width="15%" height="25" >
														<div align="center">
															<strong><digi:trn key="aim:totalAidFlowsDisbursedToTheGovernmentSector">Total Aid flows disbursed to the government sector</digi:trn></strong>
														</div>
													 </td>
													 <td width="15%" height="25" >
														<div align="center">
															<strong><digi:trn key="aim:proportionOfAidToTheGovernmentSectorDisbursed">Proportion of aid to the government sector disbursed within the fiscal year it was scheduled</digi:trn></strong>
														</div>
													 </td>
												</c:forEach>
											</tr>
										</c:if>
										<tr>
											<%-- Loop-3[ANSWERS] starts here --%>
											<td align="center">
												<%--// Answers here--%>
												<c:set var="numCols" value="${aimParisIndicatorReportForm.numColsCalculated}" />
										  		<logic:empty name="aimParisIndicatorReportForm" property="donorsColl">
													<tr>
														<td width="100%" align="center" height="65" colspan='<c:out value="${numCols}" />'>
															<div align="center">
																<strong><font color="red"><digi:trn key="aim:noSurveyDataFound">No survey data found.</digi:trn></font></strong>
															</div>
														</td>
													</tr>
										  		</logic:empty>
										  		<logic:notEmpty name="aimParisIndicatorReportForm" property="donorsColl">
											  		<c:set var="index1" value="${numCols - 1}" />
													<%int indexRow2 = 0;%>
											  		<nested:iterate name="aimParisIndicatorReportForm" property="donorsColl">
														<% indexRow2++; %>
											    		<%if(indexRow2 % 2 == 0){%>
														<tr>
														<%}else{%>
														<tr bgcolor="#EBEBEB">
														<%}%>
															<td width="15%" align="center" height="65">
																<div align="center">
																	<strong><digi:trn key="aim:parisIndicatorDonor"><nested:write property="donor" /></digi:trn></strong>
																</div>
															</td>
															<nested:iterate property="answers">
																<c:set var="index2" value="0" />
																<nested:iterate id="rowVal">
	                                                                <c:if test="${index2 == 1}">
	                                                                    <c:set var="firstVal" value="${rowVal}" />
	                                                                </c:if>
	                                                                <c:if test="${index2 == 2}">
	                                                                    <c:set var="secondVal" value="${rowVal}" />
	                                                                </c:if>
																	<c:if test="${aimParisIndicatorReportForm.indicatorCode == '6'}">
																		<td>
																			<div align="center">
	                				                                            <fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />
																			</div>
																		</td>
																	</c:if>
																	<c:if test="${aimParisIndicatorReportForm.indicatorCode == '7'}">
																		<c:if test="${index2 != 0}">
																			<td>
																				<div align="center">
																					<c:if test="${index1 == index2}">
																						<c:if test="${rowVal == -1}">n.a.</c:if>
					                                                                    <c:if test="${rowVal != -1}">
					                                                                       <c:if test="${firstVal != 0}">
					                                                                           <fmt:formatNumber type="number" value="${secondVal*100/firstVal}" pattern="###" maxFractionDigits="0" />%
                                                                                           </c:if>
                                                                                           <c:if test="${firstVal == 0}">n.a.</c:if>
					                                                                    </c:if>
																					</c:if>
																					<c:if test="${index1 != index2}">
				    	                                                                <fmt:formatNumber type="number" value="${rowVal}" pattern="###" maxFractionDigits="0" />
																					</c:if>
																				</div>
																			</td>
																		</c:if>
																		<c:set var="index2" value="${index2 + 1}" />
																	</c:if>
																</nested:iterate>
															</nested:iterate>
														</tr>
													</nested:iterate>
												</logic:notEmpty>
											</td>
											<%-- Loop-3[ANSWERS] ends here --%>
										</tr>
									</table>
								</td>
							</tr>
							<c:if test="${aimParisIndicatorReportForm.indicatorCode != '6'}">
								<tr>
									<td>
										<font color="blue">*
											<gs:test name="<%= org.digijava.module.aim.helper.GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS %>" compareWith="true" onTrueEvalBody="true">
												<digi:trn key="aim:allTheAmounts">All the amounts are in thousands (000)</digi:trn>
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
				<tr>
					<td class="td_bottom1">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>
</table>