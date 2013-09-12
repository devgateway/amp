<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<!-- Visualization's Stylesheet-->
<link rel="stylesheet" href="css_2/visualization.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="js_2/yui/tabview/assets/skins/sam/tabview.css">
<digi:ref href="css_2/visualization_yui_tabs.css" type="text/css" rel="stylesheet" />
<% int idx = 2; 
%>
<style>
	.flashcontent {
		border: solid 1px #000;
		margin:5px 0px 0px 0px;
		padding:0px 0px 0px 0px;
		vertical-align:top;
		width:634px;
		height:460px;
		clear: both;
	    background-color: #FFFFFF;
	}
	.side_opt_sel {
		background-color: rgb(191, 210, 223); 
	}

	.chart_header {
		font-size: 11px;
		padding: 5px 5px 10px 5px;
		margin: 0px 0px 10px 0px;
		font-weight: bold;
	 	border-color: #DADAD6 #C2C1BA #C2C1BA #DADAD6;
	    border-style: solid;
	    border-width: 1px 2px 2px 1px;
	    width:500px;
	    background-color: #FFFFFF;
    }
	.chart_header label {
		font-size: 11px;
    }
    .chartFieldset {
	    background-color: #F4F4F4;
    }	
</style>
<!-- Visualization's Scripts-->
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/selector/selector-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo/yahoo-min.js"></script> 
<script type="text/javascript">
//Global declaration
var trnPrimary = "";
var trnAll = "";
var trnExportOptions = "";
var trnAdvancedFilter = "";
var trnInstallFlash = "";
var trnCancel = "";
var trnShowSettings = "";
var trnHideSettings = "";
var trnLoading = "";
var trnShowTop = "";
var trnTotalDisbs = "";
var trnNumOfProjs = "";
var trnNumOfDons = "";
var trnNumOfSecs = "";
var trnNumOfRegs = "";
var trnAvgProjSize = "";
var trnTotalDisbsDescription = "";
var trnNumOfProjsDescription = "";
var trnNumOfDonsDescription = "";
var trnNumOfSecsDescription = "";
var trnNumOfRegsDescription = "";
var trnAvgProjSizeDescription = "";
var trnCommitments = "";
var trnDisbursements = "";
var trnExpenditures = "";
var trnMTEFProjections = "";
var trnPledges = "";
var trnAidPredictability = "";
var trnAidType = "";
var trnFinancingInstrument = "";
var trnOrganizationProfile = "";
var trnSectorProfile = "";
var trnSubSectorProfile = "";
var trnRegionProfile = "";
var trnSubRegionProfile = "";
var trnShowFullList = "";
var trnTopProjects = "";
var trnTopSectors = "";
var trnTopRegions = "";
var trnTopOrganizations = "";
var trnTopNPOs = "";
var trnTopPrograms = "";
var trnShowFilterSetttings = "";
var trnHideFilterSetttings = "";
var trnTotalCommitments = "";
var trnAllAmountsInMillions = "";
var trnAllAmountsInThousands = "";
var trnMillions = "";
var trnThousands = "";
var trnInMillions = "";
var trnInThousands = "";
var trnInBillions = "";
var trnTitle = "";
var trnName = "";
var trnEmails = "";
var trnPhones = "";
var trnFaxes = "";
var trnNoContactInfo = "";
var trnSave = "";
var trnDescription = "";
var trnBackgroundOrganization = "";
var trnNoAdditionalInfo = "";
var trnAllOrgGroups = "";
var trnMultipleOrgs = "";
var trnOrgInfo="";
var trnAllSectors = "";
var trnMultipleSubSector = "";
var trnAllSubSector = "";
var trnAllRegions = "";
var trnAllZones = "";
var trnMultipleZones = "";
var trnMultipleOrgGrp = ""; 
var trnMultipleRegion = ""; 
var trnMultipleSector = ""; 
var trnODAGrowth = "";
var trnSavingInformation = "";
var trnSavedInformation = "";
var trnFailedSave = "";
var trnNoDataToShow = "";
var alertBadDate="";
var loadingPanel;
var urlPdfExport = "";
var urlWordExport = "";
var urlExcelExport = "";
var urlSaveAdditional = "";
var urlShowList = "";
var trnKeyAreas = "";
var trnBackgroundOrganizationGroup = "";
var trnActual = "";
var trnPlanned = "";

//Section for all translation as global so included javascript can use them
function initializeTranslations(){
	trnPrimary = "<digi:trn jsFriendly='true'>Primary</digi:trn>";
	trnAll="<digi:trn jsFriendly='true'>All</digi:trn>";
	trnExportOptions = '\n<digi:trn jsFriendly="true">Export Options</digi:trn>' 
	trnAdvancedFilter = '\n<digi:trn jsFriendly="true">Advanced Filters</digi:trn>'; 
	trnInstallFlash = '\n<digi:trn jsFriendly="true">Install Flash Plugin</digi:trn>'; 
	trnCancel = '<digi:trn>Cancel</digi:trn>';
	trnShowSettings="<digi:trn jsFriendly='true'>Show settings</digi:trn>"; 
	trnHideSettings="<digi:trn jsFriendly='true'>Hide settings</digi:trn>"; 
	trnLoading = '<digi:trn>Loading, please wait...</digi:trn>';
	trnShowTop="<digi:trn jsFriendly='true'>View Top List</digi:trn>"; 
	trnTotalDisbs="<digi:trn jsFriendly='true'>Total Disbursements</digi:trn>: ";
	trnNumOfProjs="<digi:trn jsFriendly='true'>Total Number of Projects</digi:trn>: ";
	trnNumOfDons="<digi:trn jsFriendly='true'>Total Number of Organizations</digi:trn>: ";
	trnNumOfSecs="<digi:trn jsFriendly='true'>Total Number of Sectors</digi:trn>: ";
	trnNumOfRegs="<digi:trn jsFriendly='true'>Total Number of Regions</digi:trn>: ";
	trnAvgProjSize="<digi:trn jsFriendly='true'>Average Project Size</digi:trn>: ";
	trnTotalCommitsDescription="<digi:trn jsFriendly='true'>Sum of Commitments on projets filtered.</digi:trn>";
	trnTotalDisbsDescription="<digi:trn jsFriendly='true'>Sum of Disbursements on projets filtered.</digi:trn>";
	trnNumOfProjsDescription="<digi:trn jsFriendly='true'>Number of Projects filtered.</digi:trn>";
	trnNumOfDonsDescription="<digi:trn jsFriendly='true'>Number of Organizations on projects filtered</digi:trn>";
	trnNumOfSecsDescription="<digi:trn jsFriendly='true'>Number of Sectors on projects filtered</digi:trn>";
	trnNumOfRegsDescription="<digi:trn jsFriendly='true'>Number of Regions on projects filtered</digi:trn>";
	trnAvgProjSizeDescription="<digi:trn jsFriendly='true'>Total Disbursements divided Number of Projects</digi:trn>";
	trnCommitments="<digi:trn jsFriendly='true'>Commitments</digi:trn>";
	trnDisbursements="<digi:trn jsFriendly='true'>Disbursements</digi:trn>";
	trnExpenditures="<digi:trn jsFriendly='true'>Expenditures</digi:trn>";
	trnMTEFProjections="<digi:trn jsFriendly='true'>MTEF Projections</digi:trn>";
	trnPledges="<digi:trn jsFriendly='true'>Pledges</digi:trn>";
	trnAidPredictability="<digi:trn jsFriendly='true'>Aid Predictability</digi:trn>";
	trnAidType="<digi:trn jsFriendly='true'>Aid Type</digi:trn>";
	trnFinancingInstrument="<digi:trn jsFriendly='true'>Financing Instrument</digi:trn>";
	trnOrganizationProfile="<digi:trn jsFriendly='true'>Organization Profile</digi:trn>";
	trnSectorProfile="<digi:trn jsFriendly='true'>Sector Profile</digi:trn>";
	trnSubSectorProfile="<digi:trn jsFriendly='true'>Sub-sector breakdown</digi:trn>";
	trnRegionProfile="<digi:trn jsFriendly='true'>Region Profile</digi:trn>";
	trnSubRegionProfile="<digi:trn jsFriendly='true'>Zone breakdown</digi:trn>";
	trnShowFullList="<digi:trn jsFriendly='true'>Show Full List</digi:trn>"; 
	trnTopProjects="<digi:trn jsFriendly='true'>Top Projects</digi:trn>";
	trnTopSectors="<digi:trn jsFriendly='true'>Top Sectors</digi:trn>";
	trnTopRegions="<digi:trn jsFriendly='true'>Top Regions</digi:trn>"; 
	trnTopOrganizations="<digi:trn jsFriendly='true'>Top Organizations</digi:trn>"; 
	trnTopNPOs="<digi:trn jsFriendly='true'>Top NPO</digi:trn>"; 
	trnTopPrograms="<digi:trn jsFriendly='true'>Top Programs</digi:trn>"; 
	trnShowFilterSetttings="<digi:trn jsFriendly='true'>Show filter settings</digi:trn>"; 
	trnHideFilterSetttings="<digi:trn jsFriendly='true'>Hide filter settings</digi:trn>"; 
	trnTotalCommitments = "<digi:trn>Total Commitments</digi:trn>:";
	trnAllAmountsInMillions = "<digi:trn>All amounts in millions</digi:trn>";
	trnAllAmountsInThousands = "<digi:trn>All amounts in thousands</digi:trn>";
	trnMillions = "<digi:trn>Millions</digi:trn>";
	trnThousands = "<digi:trn>Thousands</digi:trn>";
	trnInMillions = "<digi:trn>In millions</digi:trn>";
	trnInThousands = "<digi:trn>In thousands</digi:trn>";
	trnInBillions = "<digi:trn>In billions</digi:trn>";
	trnTitle = "<digi:trn>Title</digi:trn>";
	trnName = "<digi:trn>Name</digi:trn>";
	trnEmails = "<digi:trn>Emails</digi:trn>";
	trnPhones = "<digi:trn>Phones</digi:trn>";
	trnFaxes = "<digi:trn>Faxes</digi:trn>";
	trnNoContactInfo ="<digi:trn>No Contact Information available for current filter</digi:trn>"; 
	trnSave = "<digi:trn>Save</digi:trn>";
	trnDescription = "<digi:trn>Description</digi:trn>";
	trnBackgroundOrganization = "<digi:trn>Background of organization</digi:trn>";
	trnBackgroundOrganizationGroup = "<digi:trn>Background of organization group</digi:trn>";
	trnNoAdditionalInfo = "<digi:trn>No Additional Information available for current filter</digi:trn>";
	trnAllOrgGroups = "<digi:trn jsFriendly='true'>ALL Organization Groups</digi:trn>";
	trnMultipleOrgs = "<digi:trn jsFriendly='true'>Multiple Organizations</digi:trn>"; 
	trnAllSectors = "<digi:trn jsFriendly='true'>ALL Sectors</digi:trn>"; 
	trnMultipleSubSector = "<digi:trn jsFriendly='true'>Multiple Sub Sectors</digi:trn>"; 
	trnAllSubSector = "<digi:trn jsFriendly='true'>ALL Sub Sectors</digi:trn>"; 
	trnAllRegions ="<digi:trn jsFriendly='true'>ALL Regions</digi:trn>"; 
	trnAllZones = "<digi:trn jsFriendly='true'>ALL Zones</digi:trn>"; 
	trnMultipleZones = "<digi:trn jsFriendly='true'>Multiple Zones</digi:trn>";
	trnMultipleOrgGrp = "<digi:trn jsFriendly='true'>Multiple Org. Groups</digi:trn>"; 
	trnMultipleRegion = "<digi:trn jsFriendly='true'>Multiple Regions</digi:trn>"; 
	trnMultipleSector = "<digi:trn jsFriendly='true'>Multiple Sectors</digi:trn>"; 
	trnODAGrowth = "<digi:trn jsFriendly='true'>ODA Growth Percentage</digi:trn>"; 
	trnSavingInformation = "<digi:trn>saving information, please wait</digi:trn>..."; 
	trnSavedInformation = "<digi:trn>Information was saved</digi:trn>"; 
	trnFailedSave = "<digi:trn>Failed to save information</digi:trn>";
	alertBadDate="<digi:trn>Start year can't be greater than end year</digi:trn>";
	trnNoDataToShow="<digi:trn>No data to show</digi:trn>";
	trnOrgInfo="<digi:trn>Organization Info</digi:trn>";
	trnOrgGrpInfo="<digi:trn>Organization Group Info</digi:trn>";
	trnKeyAreas="<digi:trn>Key Areas of focus</digi:trn>";
	trnActual="<digi:trn>Actual</digi:trn>";
	trnPlanned="<digi:trn>Planned</digi:trn>";
}
function initializeGlobalVariables(){
	//Other global variables
	<digi:context name="url1" property="/visualization/pdfExport.do"/>
	urlPdfExport = "${url1}";
	<digi:context name="url2" property="/visualization/wordExport.do"/>
	urlWordExport = "${url2}";
	<digi:context name="url3" property="/visualization/excelExport.do"/>
	urlExcelExport = "${url3}";
	<digi:context name="url" property="context/visualization/saveOrgInfo.do"/>
	urlSaveAdditional = "${url}";
	<digi:context name="showList" property="context/module/moduleinstance/showProjectsList.do?" />
	urlShowList = "${showList}";
}

</script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/visualization/visualization.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/flash/swfobject.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/amp/common.js"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-contains-ignorecase.js"/>"></script>

<digi:instance property="visualizationform"/>
<digi:form action="/filters.do">

<!-- POPUPS START -->
<script language="javascript">
<!--
-->
</script>
<div id="myOrgInfo" style="display: none;" >
	<div id="divOrgInfo" style="display: none;height: 372px;width: auto;">
	<div id="demo" class="yui-navset">
		<ul class="yui-nav">
		<c:if test="${visualizationform.filter.dashboardType eq '1' }">
		<c:if test="${!visualizationform.filter.fromPublicView}">
		<li><a href="#tab3"><div><digi:trn>Additional Notes</digi:trn></div></a></li>
		</c:if>
		<li id="contact_info_tab"><a href="#tab2"><div><digi:trn>Contact Information</digi:trn></div></a></li>
		</c:if>
		</ul>
		<div class="yui-content">
			<div id="tab3">
			</div>
			<div id="tab2">
				<digi:trn>No Contact Information available for current filter selection</digi:trn>
			</div>
		</div>
	</div>
	</div>
</div>

<table>
<tr>
<td>
<div id="dialog2" class="dialog" title="Advanced Filters">
					<div id="popinContent" class="content">
						<c:set var="selectorHeaderSize" scope="page" value="11" />
							<div class="yui-content" style="height: 92%; margin-top: 10px;">
								<div id="generalTab" style="height: 91%;">
										<div class="grouping_selector_wrapper"
											style="float: left; width: 40%; padding: 0px; height: 98%;">
											<div
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside">
													<b class="ins_header"><digi:trn>Grouping Selector</digi:trn>
													</b>
												</div>
											</div>
											<div
												style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;">
												<table style="width: 95%; margin-top: 15px;" align="center"
													class="inside">
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_0">
															<div class="selector_type_cont" onclick="changeTab(0)">
																<digi:trn>General</digi:trn>
															</div></td>

													</tr>
													<c:if test="${visualizationform.filter.dashboardType ne '4' }">
														<tr style="cursor: pointer;">
															<td class="side_opt_sel" id="general_selector_1" >
																<div class="selector_type_cont" onclick="changeTab(1)">
																	<digi:trn>Organization Groups With Organizations</digi:trn>
																</div></td>
	
														</tr>
													</c:if>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_2">
															<div class="selector_type_cont" onclick="changeTab(2)">
																<digi:trn>Regions With Zones</digi:trn>
															</div></td>

													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_3">
															<div class="selector_type_cont" onclick="changeTab(3)">
																<digi:trn>Sectors and Sub Sectors</digi:trn>
															</div></td>

													</tr>
													<c:if test="${visualizationform.filter.dashboardType eq '4' }">
														<tr style="cursor: pointer;">
															<td class="side_opt_sel" id="general_selector_1" >
																<div class="selector_type_cont" onclick="changeTab(1)">
																	<digi:trn>Donor Agencies</digi:trn>
																</div></td>
	
														</tr>
														<tr style="cursor: pointer;">
															<td class="side_opt_sel" id="general_selector_4" >
																<div class="selector_type_cont" onclick="changeTab(4)">
																	<digi:trn>Beneficiary Agencies</digi:trn>
																</div></td>
	
														</tr>
														<tr style="cursor: pointer;">
															<td class="side_opt_sel" id="general_selector_5" >
																<div class="selector_type_cont" onclick="changeTab(5)">
																	<digi:trn>Implementing Agencies</digi:trn>
																</div></td>
	
														</tr>
														<tr style="cursor: pointer;">
															<td class="side_opt_sel" id="general_selector_6" >
																<div class="selector_type_cont" onclick="changeTab(6)">
																	<digi:trn>Secondary Programs</digi:trn>
																</div></td>
	
														</tr>
													</c:if>
												</table>
											</div>
										</div>
										<div class="member_selector_wrapper"
											style="margin-left: 40%; padding: 0px; height: 98%;" id="generalInfoId">
											<div
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Options Selector</digi:trn>
													</b>
												</div>
											</div>
											<div
												style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="generalDivList">
												<c:if test="${!visualizationform.filter.fromPublicView}">
													<html:checkbox property="filter.workspaceOnly"
														styleId="workspace_only">
														<digi:trn>Show Only Data From This Workspace</digi:trn>
													</html:checkbox>
													<c:set var="translation">
														<digi:trn>Dashboards will show only data from activities of current workspace.</digi:trn>
													</c:set>
													<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/>
													<br />
												</c:if>
												<hr />
												<br />
												<digi:trn>For Time Series Comparison, what data do you want to show?</digi:trn>
												<c:set var="translation">
													<digi:trn>What data will show the ODA Historical Trend graph.</digi:trn>
												</c:set>
												<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/><br />
												<html:checkbox property="filter.commitmentsVisible"
													styleId="commitments_visible">
													<digi:trn>Commitments</digi:trn>&nbsp;&nbsp;</html:checkbox>
												<br />
												<html:checkbox property="filter.disbursementsVisible"
													styleId="disbursements_visible">
													<digi:trn>Disbursements</digi:trn>&nbsp;&nbsp;</html:checkbox>
												<br />
												<feature:display module="Funding" name="Expenditures">
													<html:checkbox property="filter.expendituresVisible"
														styleId="expenditures_visible">
														<digi:trn>Expenditures</digi:trn>&nbsp;&nbsp;</html:checkbox>
													<br />
												</feature:display>
												<module:display name="Pledges"
													parentModule="PROJECT MANAGEMENT">
													<html:checkbox property="filter.pledgeVisible"
														styleId="pledge_visible">
														<digi:trn>Pledges</digi:trn>&nbsp;&nbsp;</html:checkbox>
													<br />
												</module:display>
												<hr />
												<br />
												<digi:trn>What data should the dashboard show?</digi:trn>
												<c:set var="translation">
													<digi:trn>What type of funding the dashboard should use.</digi:trn>
												</c:set>
												<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/><br />
												<br />
												<table>
													<tr>
														<td><digi:trn>Funding Type</digi:trn>:</td>
														<td>
															<html:select property="filter.transactionTypeFilter" styleId="transaction_type" styleClass="dropdwn_sm" style="width:150px;">
																<html:option value="0"><digi:trn>Commitments</digi:trn></html:option>
																<html:option value="1"><digi:trn>Disbursements</digi:trn></html:option>
																<feature:display module="Funding" name="Expenditures">
																	<html:option value="2"><digi:trn>Expenditures</digi:trn></html:option>
																</feature:display>
																<html:option value="3"><digi:trn>MTEF Projections</digi:trn></html:option>
															</html:select>
														</td>
													</tr>
													<tr>
														<td><digi:trn>Adjustment Type</digi:trn>:</td>
														<td>
															<html:select property="filter.adjustmentTypeFilter"
																styleId="adjustment_type" styleClass="dropdwn_sm"
																style="width:150px;">
																<html:optionsCollection property="filter.adjustmentTypeList" value="valueKey" label="valueKey" />
															</html:select>
														</td>
													</tr>
												</table>
												<hr />
												<br />
												<digi:trn>Select activity Status</digi:trn>
												<c:set var="translation">
													<digi:trn>Select the activity status that you want to show on dashboard, if none is selected, then show all.</digi:trn>
												</c:set>
												<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/>
												<br />
												<br />
												<c:forEach items="${visualizationform.filter.statusList}" var="item">
												<input type="checkbox" id="status_check_${item.id}" name="status_check" title="<c:out value='${item.value}'/>" value="${item.id}" /> 
													<span><c:out value="${item.value}"/></span>
												<br />
												</c:forEach>
												<br />
											</div>
										</div>
										<c:if test="${visualizationform.filter.dashboardType ne '4' }">
											<div class="member_selector_wrapper" id="orgGrpContent"
												style="margin-left: 40%; padding: 0px; height: 98%; display:none">
												<div
													style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
													<div class="inside" style="float: left">
														&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
														</b>
													</div>
													<div style="float: right">
														<input onkeypress="clearSearch('orgGrpDivList')"
															id="orgGrpDivList_search" type="text" class="inputx" /> <input
															type="button" class="buttonx"
															onclick="findPrev('orgGrpDivList')" value='&lt;&lt;' /> <input
															type="button" onclick="findNext('orgGrpDivList')"
															class="buttonx" value="&gt;&gt;" />
													</div>
	
												</div>
												<div
													style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
													id="orgGrpDivList">
													<ul style="list-style-type: none;margin-left: 0px;">
														<li>
															<input type="checkbox" id="org_grp_check_all" value="-1"
																name="org_grp_check"
																onClick="allOptionChecked(this,'org_grp_check','organization_check')" />
															<span><digi:trn>All</digi:trn>
															</span>
														</li>
														<c:forEach
															items="${visualizationform.filter.orgGroupWithOrgsList}"
															var="item">
															<c:set var="orgGrp">
																<c:out value="${item.mainEntity.orgGrpName}"/>
															</c:set>
															<li>
																<input type="checkbox" name="org_grp_check"
																	id="org_grp_check_${item.mainEntity.ampOrgGrpId}"
																	title="${orgGrp}"
																	value="${item.mainEntity.ampOrgGrpId}"
																	onClick="uncheckAllOption('org_grp_check');checkRelatedEntities(this,'organization_check',${item.mainEntity.ampOrgGrpId})" />
																<span><c:out value="${orgGrp}"/>
																</span> <br />
																<ul style="list-style-type: none">
																	<c:forEach items="${item.subordinateEntityList}"
																		var="organization">
																		<li><input type="checkbox"
																			id="organization_check_${organization.ampOrgId}"
																			class="organization_check_${item.mainEntity.ampOrgGrpId}"
																			name="organization_check" title="<c:out value='${organization.name}'/>"
																			value="${organization.ampOrgId}"
																			onclick="uncheckAllOption('org_grp_check');checkParentOption('org_grp_check',${item.mainEntity.ampOrgGrpId})" /> <span><c:out value="${organization.name}"/></span>
																		</li>
																	</c:forEach>
																</ul></li>
														</c:forEach>
													</ul>
												</div>
											</div>
										</c:if>
										<div class="member_selector_wrapper" id="regionDivContent"
											style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
													</b>

												</div>
												<div class="inside" style="float: right">
													<input onkeypress="clearSearch('regionDivList')"
														id="regionDivList_search" type="text" class="inputx" /> <input
														type="button" class="buttonx"
														onclick="findPrev('regionDivList')" value='&lt;&lt;' /> <input
														type="button" onclick="findNext('regionDivList')"
														class="buttonx" value="&gt;&gt;" />
												</div>
											</div>
											<div
												style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
												id="regionDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li>
														<input type="checkbox" id="region_check_all"
															name="region_check" value="-1"
															onClick="allOptionChecked(this,'region_check','zone_check')" />
														<span><digi:trn>All</digi:trn>
													</span></li>
													<c:forEach
														items="${visualizationform.filter.regionWithZones}"
														var="item">
														<li>
															<input type="checkbox" name="region_check"
																id="region_check_${item.mainEntity.id}"
																title="${item.mainEntity.name}"
																value="${item.mainEntity.id}"
																onClick="uncheckAllOption('region_check');checkRelatedEntities(this,'zone_check',${item.mainEntity.id})">
															<span><c:out value="${item.mainEntity.name}"/>
														</span> <br />
															<ul style="list-style-type: none">
																<c:forEach items="${item.subordinateEntityList}"
																	var="zone">
																	<li><input type="checkbox"
																		id="zone_check_${zone.id}"
																		class="zone_check_${item.mainEntity.id}"
																		name="zone_check" title="${zone.name}"
																		value="${zone.id}"
																		onclick="uncheckAllOption('region_check');checkParentOption('region_check',${item.mainEntity.id})" /><span><c:out value="${zone.name}"/></span>
																	</li>
																</c:forEach>
															</ul></li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="sectorDivContent"
												style="margin-left: 40%; padding: 0px; height: 98%; display:none">
												<div
													style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
													<div class="inside" style="float: left">
														&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
														</b>
													</div>
													<div class="inside" style="float: right">
														<input onkeypress="clearSearch('sectorDivList')"
															id="sectorDivList_search" type="text" class="inputx" />
														<input type="button" class="buttonx"
															onclick="findPrev('sectorDivList')" value='&lt;&lt;' />
														<input type="button" onclick="findNext('sectorDivList')"
															class="buttonx" value="&gt;&gt;" />

													</div>

												</div>
												<div
													style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
													id="sectorDivList">
													<ul style="list-style-type: none;margin-left: 0px;">
														<c:forEach
															items="${visualizationform.filter.configWithSectorAndSubSectors}"
															var="item">
															<c:set var="item" scope="request" value="${item}" />
															<c:choose>
																<c:when test="${item.mainEntity.name=='Primary'}">
																	<field:display name="Primary Sector" feature="Sectors">
																		<jsp:include page="sectorPopinHelper.jsp" flush="true" />
																	</field:display>
																</c:when>
																<c:when test="${item.mainEntity.name=='Secondary'}">
																	<field:display name="Secondary Sector"
																		feature="Sectors">
																		<jsp:include page="sectorPopinHelper.jsp" />
																	</field:display>
																</c:when>
																<c:when test="${item.mainEntity.name=='Tertiary'}">
																	<field:display name="Tertiary Sector" feature="Sectors">
																		<jsp:include page="sectorPopinHelper.jsp" />
																	</field:display>
																</c:when>
															</c:choose>
														</c:forEach>
													</ul>
												</div>
											</div>
											<c:if test="${visualizationform.filter.dashboardType eq '4' }">
												<div class="member_selector_wrapper" id="orgGrpContent" style="margin-left: 40%; padding: 0px; height: 98%; display:none">
													<div style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
														<div class="inside" style="float: left">
															&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
															</b>
														</div>
														<div style="float: right">
															<input onkeypress="clearSearch('orgGrpDivList')"
																id="orgGrpDivList_search" type="text" class="inputx" /> <input
																type="button" class="buttonx"
																onclick="findPrev('orgGrpDivList')" value='&lt;&lt;' /> <input
																type="button" onclick="findNext('orgGrpDivList')"
																class="buttonx" value="&gt;&gt;" />
														</div>
		
													</div>
													<div style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;" id="orgGrpDivList">
														<ul style="list-style-type: none;margin-left: 0px;">
															<c:forEach items="${visualizationform.filter.organizations}" var="item">
																<li>
																	<input type="checkbox" name="organization_check"
																		id="organization_check_${item.ampOrgId}"
																		title="${item.name}"
																		value="${item.ampOrgId}" >
																	<span><c:out value="${item.name}"/>
																</span> <br />
															</c:forEach>
														</ul>
													</div>
												</div>
												<div class="member_selector_wrapper" id="beneficiaryAgencyDivContent"
													style="margin-left: 40%; padding: 0px; height: 98%; display:none">
													<div
														style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
														<div class="inside" style="float: left">
															&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
															</b>
														</div>
														<div class="inside" style="float: right">
															<input onkeypress="clearSearch('beneficiaryAgencyDivList')"
																id="beneficiaryAgencyDivList_search" type="text" class="inputx" />
															<input type="button" class="buttonx"
																onclick="findPrev('beneficiaryAgencyDivList')" value='&lt;&lt;' />
															<input type="button" onclick="findNext('beneficiaryAgencyDivList')"
																class="buttonx" value="&gt;&gt;" />
	
														</div>
	
													</div>
													<div
														style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
														id="beneficiaryAgencyDivList">
														<ul style="list-style-type: none;margin-left: 0px;">
															<c:forEach items="${visualizationform.filter.beneficiaryAgencyList}" var="item">
																<li>
																	<input type="checkbox" name="beneficiary_agency_check"
																		id="beneficiary_agency_check_${item.ampOrgId}"
																		title="${item.name}"
																		value="${item.ampOrgId}" >
																	<span><c:out value="${item.name}"/>
																</span> <br />
															</c:forEach>
														</ul>
													</div>
												</div>
												<div class="member_selector_wrapper" id="implementingAgencyDivContent"
													style="margin-left: 40%; padding: 0px; height: 98%; display:none">
													<div
														style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
														<div class="inside" style="float: left">
															&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
															</b>
														</div>
														<div class="inside" style="float: right">
															<input onkeypress="clearSearch('implementingAgencyDivList')"
																id="implementingAgencyDivList_search" type="text" class="inputx" />
															<input type="button" class="buttonx"
																onclick="findPrev('implementingAgencyDivList')" value='&lt;&lt;' />
															<input type="button" onclick="findNext('implementingAgencyDivList')"
																class="buttonx" value="&gt;&gt;" />
	
														</div>
	
													</div>
													<div
														style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
														id="implementingAgencyDivList">
														<ul style="list-style-type: none;margin-left: 0px;">
															<c:forEach items="${visualizationform.filter.implementingAgencyList}" var="item">
																<li>
																	<input type="checkbox" name="implementing_agency_check"
																		id="implementing_agency_check_${item.ampOrgId}"
																		title="${item.name}"
																		value="${item.ampOrgId}" >
																	<span><c:out value="${item.name}"/>
																</span> <br />
															</c:forEach>
														</ul>
													</div>
												</div>
												<div class="member_selector_wrapper" id="secondaryProgramDivContent"
													style="margin-left: 40%; padding: 0px; height: 98%; display:none">
													<div
														style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32px; border: 1px solid #CCCCCC; border-bottom: 0px;">
														<div class="inside" style="float: left">
															&nbsp; <b class="ins_header"> <digi:trn>Member Selector</digi:trn>
															</b>
														</div>
														<div class="inside" style="float: right">
															<input onkeypress="clearSearch('secondaryProgramDivList')"
																id="secondaryProgramDivList_search" type="text" class="inputx" />
															<input type="button" class="buttonx"
																onclick="findPrev('secondaryProgramDivList')" value='&lt;&lt;' />
															<input type="button" onclick="findNext('secondaryProgramDivList')"
																class="buttonx" value="&gt;&gt;" />
	
														</div>
	
													</div>
													<div
														style="height: 180px; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180px; padding: 20px;"
														id="secondaryProgramDivList">
														<ul style="list-style-type: none;margin-left: 0px;">
															<c:forEach items="${visualizationform.filter.secondaryProgramsList}" var="item">
																<li>
																	<input type="checkbox" name="secondary_program_check"
																		id="secondary_program_check_${item.ampThemeId}"
																		title="${item.name}"
																		value="${item.ampThemeId}" >
																	<span><c:out value="${item.name}"/>
																</span> <br />
															</c:forEach>
														</ul>
													</div>
												</div>
											</c:if>
									<div>
										<table border="0" cellspacing="3" cellpadding="3">
											<tr>
												<td><b><digi:trn>Currency Type</digi:trn>:</b>
												</td>
												<td><html:select property="filter.currencyId"
														styleId="currencies_dropdown_ids" styleClass="dropdwn_sm"
														style="width:150px;">
														<html:optionsCollection property="filter.currencies"
															value="ampCurrencyId" label="currencyName" />
													</html:select></td>
												<td><b><digi:trn>Start year</digi:trn>:</b>
												</td>
												<td><html:select property="filter.startYearFilter"
														styleId="startYear_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:optionsCollection property="filter.years"
															label="key" value="value" />
													</html:select></td>
												<td><b><digi:trn>Show in top ranks</digi:trn>:</b>
												</td>
												<td><html:select property="filter.topLists"
														styleId="topLists_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:option value="5">5</html:option>
														<html:option value="10">10</html:option>
														<html:option value="20">20</html:option>
														<html:option value="50">50</html:option>
													</html:select></td>
											</tr>
											<tr>
												<td><b><digi:trn>Fiscal Calendar</digi:trn>:</b>
												</td>
												<td><html:select property="filter.fiscalCalendarId"
														styleId="fiscalCalendar_dropdown_Id"
														styleClass="dropdwn_sm" style="width:150px;">
														<html:optionsCollection property="filter.fiscalCalendars"
															label="name" value="ampFiscalCalId" />
													</html:select></td>
												<td><b><digi:trn>End year</digi:trn>:</b>
												</td>
												<td><html:select property="filter.endYearFilter"
														styleId="endYear_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:optionsCollection property="filter.years"
															label="key" value="value" />
													</html:select></td>
												<td><b><digi:trn>Show amounts in</digi:trn>:</b>
												</td>
												<td>
 											<html:select property="filter.showAmountsInThousands" styleId="show_amounts_in_thousands" styleClass="dropdwn_sm" style="width:150px;">
 										<%--	<html:option value="0"><digi:trn>Units</digi:trn></html:option>  --%>
 												<html:option value="1"><digi:trn>Thousands</digi:trn></html:option>
 												<html:option value="2"><digi:trn>Millions</digi:trn></html:option>
 											</html:select>													</td>
											</tr>
											<tr>
												<c:if test="${visualizationform.filter.dashboardType==1}">
												<td><b><digi:trn>Type of Agency</digi:trn>:</b>
												 	</td>
													<td align="right">
														<html:select property="filter.agencyTypeFilter" styleId="agencyType_dropdown" styleClass="dropdwn_sm" style="width:145px;">
															<html:option value="0"><digi:trn>Donor</digi:trn></html:option>
															<html:option value="1"><digi:trn>Executing</digi:trn></html:option>
															<html:option value="2"><digi:trn>Beneficiary</digi:trn></html:option>
														</html:select>
												 	</td>
												</c:if>
												<td><b><digi:trn>Decimals to show</digi:trn>:</b>
												</td>
												<td><html:select property="filter.decimalsToShow"
														styleId="decimalsToShow_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:option value="0">0</html:option>
														<html:option value="1">1</html:option>
														<html:option value="2">2</html:option>
														<html:option value="3">3</html:option>
														<html:option value="4">4</html:option>
														<html:option value="5">5</html:option>
													</html:select></td>
												<td></td>
												<td></td>
											</tr>
										</table>
									</div>
								</div>



							</div>
			


 <input type="button" value="<digi:trn>Apply</digi:trn>" class="buttonx" style="margin-right:10px; margin-top:10px;" id="applyButtonPopin">
<input type="button" value="<digi:trn>Reset to defaults</digi:trn>" onclick="resetToDefaults()" class="buttonx" style="margin-right:10px; margin-top:10px;">
<input type="button" value="<digi:trn>Close</digi:trn>" class="buttonx" onclick="hidePopin()" style="margin-right:10px; margin-top:10px;">


</div>
</div>
</td>
</tr>
</table>
<!-- POPUPS END -->

<table>
<tr>
<td>
<div id="installFlashPopin" class="dialog" title="Export Options">
	<div id="popinContent3" class="content" align="center">
		<a href="http://www.adobe.com/go/getflashplayer" target="_blank" title="<digi:trn>Click here to get Adobe Flash player</digi:trn>">
			<digi:trn>To view the dashboards you'll need Flash Player plug in installed. Please click here to go to Adobe website to download and install. After the plug in is installed please restart the browser.</digi:trn>
		</a>
	</div>
</div>
<div id="exportPopin" class="dialog" title="Export Options">
<div id="popinContent2" class="content">
	<div id="exportDiv" class="yui-navset">
		<table width="100%" height=400 cellpadding="0" cellspacing="0">
			<tr>
				<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Export Type</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.exportTypeOption" styleId="export_type_0" value="0" ><digi:trn>PDF</digi:trn>  </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_pdf.gif"><br />
		            <html:radio property="exportData.exportTypeOption" styleId="export_type_1" value="1"><digi:trn>Word</digi:trn>   </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_word.gif"><br />
		        	<html:radio property="exportData.exportTypeOption" styleId="export_type_2" value="2"><digi:trn>Excel</digi:trn>   </html:radio><img src="/TEMPLATE/ampTemplate/img_2/ico_exc.gif"><br />
		        </div>
		        </td>
				<td class="inside" width="30%" >
				<div class="selector_type"><b><digi:trn>Summary</digi:trn></b></div>
				<div>
		            <html:radio property="exportData.exportSummaryOption" styleId="export_summary_0" value="0"><digi:trn>Exclude Summary</digi:trn></html:radio><br />
		            <html:radio property="exportData.exportSummaryOption" styleId="export_summary_1" value="1"><digi:trn>Include Summary</digi:trn></html:radio><br />
		        </div>
		        </td>
				<c:forEach items="${visualizationform.graphList}" var="graph">
					<%  
					idx++;
					%>
					<td class="inside" width="30%" >
						<div class="selector_type"><b><digi:trn>${graph.name}</digi:trn></b></div>
						<div>
				            <html:radio property="exportData.export${graph.containerId}Option" styleId="export_${graph.containerId}_0" value="0"><digi:trn>None</digi:trn></html:radio><br />
				            <html:radio property="exportData.export${graph.containerId}Option" styleId="export_${graph.containerId}_1" value="1"><digi:trn>Data Source Only</digi:trn></html:radio><br />
				            <html:radio property="exportData.export${graph.containerId}Option" styleId="export_${graph.containerId}_2" value="2"><digi:trn>Chart Only</digi:trn></html:radio><br />
				            <html:radio property="exportData.export${graph.containerId}Option" styleId="export_${graph.containerId}_3" value="3"><digi:trn>Data Source and Chart</digi:trn></html:radio><br />
				        </div>
				    </td>
			    <%  
				if (idx==3){
					idx=0;	
				%>
				</tr>
		    	<tr>
				<%
				}
				%>
				</c:forEach>
				
		    </tr>
		</table>
	</div>
	</div>

        <input type="button" value="<digi:trn>Export</digi:trn>" class="buttonx" onclick="doExport()" style="margin-right:10px; margin-top:10px;">
        <input type="button" value="<digi:trn>Close</digi:trn>" class="buttonx" onclick="hideExport()" style="margin-right:10px; margin-top:10px;">
		        
</div>
</td>
</tr>
</table>

<table>
<tr>
<td>
<div id="listPopin" class="dialog" title="List of Activities">
	<div id="popinContent3" class="dash_left"  style="max-height: 500px; overflow: auto;">
		
	</div>
</div>
</td>
</tr>
</table>


<!-- MAIN CONTENT PART START -->

<div class="dashboard_header">
<!--<div class="dashboard_total"><b class="dashboard_total_num">${visualizationform.summaryInformation.totalCommitments}</b><br /><digi:trn>Total Commitments</digi:trn> ( ${visualizationform.filter.currencyId} )</div>-->
<div class="dashboard_name" id="dashboard_name">
<span style="font-size:18px">${visualizationform.dashboardTitle}</span><br/><span style="font-size:13px">${visualizationform.dashboardSubTitle}</span>
</div>
<div>
 <table border="0" cellspacing="0" cellpadding="0">
  <tr>  
    <td>
    	<table>
    		<tr>
    			<td style="width:110px;"><div class="dash_ico"><img src="/TEMPLATE/ampTemplate/img_2/ico_export.gif" align=left style="margin-right:5px;"><div class="dash_ico_link"><a href="javascript:showExport()" class="l_sm"><digi:trn>Export Options</digi:trn></a></div></div></td>
    			<td><div id="info_link"><img src='/TEMPLATE/ampTemplate/img_2/ico_info.gif' onclick='showOrgInfo();' align=left style="margin-right:5px;margin-top:5px;"/><div class="dash_ico_link"><a href="javascript:showOrgInfo()" class="l_sm"><digi:trn>Additional Info</digi:trn></a></div></div></td>
    		</tr>
    	</table>
   	</td>
  </tr>
</table>
</div>
<br />
<div class="dashboard_total" id="divTotals"></div><div id="currencyInfo"></div>

<div class="dashboard_stat" id="divSummaryInfo" ></div>
<div class="dashboard_stat"><a onClick="toggleSettings();" id="displaySettingsButton"><digi:trn>Show filter settings</digi:trn></a></div>
<div class="dashboard_stat" style="display:none;" id="currentDisplaySettings" >
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr style="background-color:#FAFAFA;">
	<td style="font-size:11px;font-family:Arial,Helvetica,sans-serif; padding-top:5px; padding-bottom:5px; padding-left:5px;" valign="top">
	<strong>
	<digi:trn>Selected Filters</digi:trn>:</strong>
	   <i><digi:trn>Currency type</digi:trn>: </i><label id="filterCurrency"></label> |
	   <i><digi:trn>Start year</digi:trn>: </i><label id="filterStartYear">${visualizationform.filter.startYear}</label> | 
	   <i><digi:trn>End year</digi:trn>: </i><label id="filterEndYear">${visualizationform.filter.endYear}</label> | 
	   <i><digi:trn>Org. groups</digi:trn>: </i><label id="filterOrgGroups"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Donor Agencies</digi:trn>: </i><label id="filterOrganizations"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Implementing Agencies</digi:trn>: </i><label id="filterImpAgencies"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Beneficiary Agencies</digi:trn>: </i><label id="filterBenAgencies"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Secondary Programs</digi:trn>: </i><label id="filterSecPrograms"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Configuration</digi:trn>: </i><label id="filterSectorConfiguration"><digi:trn>Primary</digi:trn></label> | 
	   <i><digi:trn>Sectors</digi:trn>: </i><label id="filterSectors"><digi:trn>All</digi:trn></label> |
	   <i><digi:trn>SubSectors</digi:trn>: </i><label id="filterSubSectors"><digi:trn>All</digi:trn></label> |
	   <i><digi:trn>Regions</digi:trn>: </i><label id="filterRegions"><digi:trn>All</digi:trn></label> |
	   <i><digi:trn>Zones</digi:trn>: </i><label id="filterZones"><digi:trn>All</digi:trn></label> |
	   <i><digi:trn>Status</digi:trn>: </i><label id="filterStatus"><digi:trn>All</digi:trn></label> |
	</td>
	</tr>
	<tr>
	</tr>
	</table>
</div>
<!--<div class="dashboard_stat">Total Disbursements: <div id="divTotalDisbs"></div> <span class="breadcrump_sep">|</span>Total Number of Projects: <div id="divNumOfProjs"></div><span class="breadcrump_sep">|</span>Total Number of Sectors: <div id="divNumOfSecs"></div><span class="breadcrump_sep">|</span>Total Number of Regions: <div id="divNumOfRegs"></div><span class="breadcrump_sep">|</span>Average Project Size: <div id="divAvgProjSize"></div></div>-->

</div>


<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center" style="margin-top:15px;">
  <tr>
    <td width=296 bgcolor="#F4F4F4" valign="top">
	<div style="background-color:#FFFFFF; height:7px;">&nbsp;</div>
	<div class="dash_left">
	<fieldset>
	<legend><span class=legend_label><digi:trn>Quick Filter</digi:trn></span></legend>
<!--	<html:checkbox property="filter.workspaceOnly" styleId="workspace_only"><digi:trn>Show Only Data From This Workspace</digi:trn></html:checkbox>-->
	<hr />
	<c:set var="translation">
		<digi:trn>Show all charts in grayscale for print purposes</digi:trn>
	</c:set>
	<html:checkbox  property="filter.showMonochrome" styleId="show_monochrome" onclick="reloadGraphs();"><digi:trn>Show Monochrome</digi:trn></html:checkbox> <img title="${translation}" src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" /><br />
	<hr />
	<table cellspacing="0" cellpadding="0" width="100%">
		<c:if test="${!visualizationform.filter.fromPublicView}">
			<tr>
				<td colspan="2" style="padding-bottom: 2px;"><html:checkbox property="filter.workspaceOnlyQuickFilter"
						styleId="workspaceOnlyQuickFilter">
						<digi:trn>Show Only Data From This Workspace</digi:trn>
					</html:checkbox>
				</td>
			</tr>
		</c:if>
		<c:if test="${visualizationform.filter.dashboardType eq '4' }">
				<tr>
				  <td><digi:trn>Peacebuilding Marker</digi:trn>:</td>
				  	<td align=right>
				     <html:select property="filter.peacebuilderMarkerId" styleId="peacebuilding_marker_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
				         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
				          <c:forEach var="cat" items="${visualizationform.filter.peacebuilderMarkerList}">
				         	<html:option value="${cat.id}"><c:out value="${cat.value}"/></html:option>
				         </c:forEach>
				     </html:select>
					</td>
				</tr>
				<tr>
					<td><digi:trn>Donor Agency</digi:trn>:</td>
					<td align=right>
					   <html:select property="filter.orgId" styleId="org_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
					       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
					          <c:forEach var="org" items="${visualizationform.filter.organizations}">
					         	<html:option value="${org.ampOrgId}"><c:out value="${org.name}"/></html:option>
					         </c:forEach>
					   </html:select>
					   <div id="org_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
					</td>
				</tr>
				<tr>
				  <td><digi:trn>Implementing Agency</digi:trn>:</td>
				  	<td align=right>
				     <html:select property="filter.implementingAgencyId" styleId="implementing_agency_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
				         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
				          <c:forEach var="org" items="${visualizationform.filter.implementingAgencyList}">
				         	<html:option value="${org.ampOrgId}"><c:out value="${org.name}"/></html:option>
				         </c:forEach>
				     </html:select>
				     <div id="imp_ag_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
					</td>
				</tr>
				<tr>
				  <td><digi:trn>Beneficiary Agency</digi:trn>:</td>
				  	<td align=right>
				     <html:select property="filter.beneficiaryAgencyId" styleId="beneficiary_agency_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
				         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
				          <c:forEach var="org" items="${visualizationform.filter.beneficiaryAgencyList}">
				         	<html:option value="${org.ampOrgId}"><c:out value="${org.name}"/></html:option>
				         </c:forEach>
				     </html:select>
				     <div id="ben_ag_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
					</td>
				</tr>
				<tr>
				  <td><digi:trn>Secondary Program</digi:trn>:</td>
				  	<td align=right>
				     <html:select property="filter.secondaryProgramId" styleId="secondary_program_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
				         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
				          <c:forEach var="prog" items="${visualizationform.filter.secondaryProgramsList}">
				         	<html:option value="${prog.ampThemeId}"><c:out value="${prog.name}"/></html:option>
				         </c:forEach>
				     </html:select>
				     <div id="sec_prog_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
					</td>
				</tr>
		</c:if>
		<c:if test="${visualizationform.filter.dashboardType eq '1' }">
				<tr>
				  <td><b><digi:trn>Organization Group</digi:trn>:</b></td>
				  	<td align=right>
				     <html:select property="filter.orgGroupId" styleId="org_group_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
				         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
				         <c:forEach var="orgGrp" items="${visualizationform.filter.orgGroups}">
				         	<html:option value="${orgGrp.ampOrgGrpId}"><c:out value="${orgGrp.orgGrpName}"/></html:option>
				         </c:forEach>
				     </html:select>
				     <div id="org_group_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
					</td>
				</tr>
				<tr>
					<td><b><digi:trn>Organization</digi:trn>:</b></td>
					<td align=right>
					   <html:select property="filter.orgId" styleId="org_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
					       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
					   </html:select>
					   <div id="org_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
					</td>
				</tr>
			</c:if>
			<c:if test="${visualizationform.filter.dashboardType eq '2' }">
			  <tr>
				<td><b><digi:trn>Region</digi:trn>:</b></td>
				<td align=right>
				   <html:select property="filter.regionId" styleId="region_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
				       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
				       <html:optionsCollection property="filter.regions" value="id" label="name" />
				   </html:select>
				   <div id="region_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
				</td>
			 </tr> 
			 <tr>
			  	<td><b><digi:trn>Zone</digi:trn>:</b></td>
			   	<td align=right>
			      <html:select property="filter.zoneId" styleId="zone_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
			          <html:option value="-1"><digi:trn>All</digi:trn></html:option>
			      </html:select>
			      <div id="zone_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
				</td>
			</tr> 
		  </c:if>
		  <c:if test="${visualizationform.filter.dashboardType eq '3' }">
		  <tr>
			<td><b><digi:trn>Configurations</digi:trn>:</b></td>
			  <td align="right">
			  <html:select property="filter.selSectorConfigId" styleId="sector_config_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
			  <c:forEach var="config" items="${visualizationform.filter.sectorConfigs}">
				<c:choose>
								<c:when test="${config.name=='Primary'}">
									<field:display name="Primary Sector" feature="Sectors">
									         <html:option value="${config.id}">${config.classification.secSchemeName}</html:option>
			     					 </field:display>
								</c:when>
								<c:when test="${config.name=='Secondary'}">
									<field:display name="Secondary Sector" feature="Sectors">
									    <html:option value="${config.id}">${config.classification.secSchemeName}</html:option>
			     					 </field:display>
								</c:when>
								<c:when test="${config.name=='Tertiary'}">
									<field:display name="Tertiary Sector" feature="Sectors">
									    <html:option value="${config.id}">${config.classification.secSchemeName}</html:option>
			     					 </field:display>
								</c:when>
				</c:choose>
		
		        
			  </c:forEach>
			     </html:select>
			     <div id="sector_config_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
				</td>
			</tr>
			<tr>
			<td><b><digi:trn>Sector</digi:trn>:</b></td>
			  <td align="right">
			  <html:select property="filter.sectorId" styleId="sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
			         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
			         <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
			     </html:select>
			     <div id="sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
				</td>
			</tr>
			<tr>
			   <td><b><digi:trn>Sub-Sector</digi:trn>:</b></td>
			  <td align=right>
			     <html:select property="filter.subSectorId" styleId="sub_sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
			         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
			     </html:select>
			     <div id="sub_sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
				</td>
			</tr>
		  </c:if>
			<td><digi:trn>Currency Type</digi:trn>:
		 	</td>
			<td align="right">
				<html:select property="filter.currencyIdQuickFilter" styleId="currencyQuickFilter_dropdown" styleClass="dropdwn_sm" style="width:145px;">
					<html:optionsCollection property="filter.currencies" value="ampCurrencyId" label="currencyName" />
				</html:select>
		 	</td>
		</tr>
		<tr>
			<td><digi:trn>Start Year</digi:trn>:
		 	</td>
			<td align="right">
				<html:select property="filter.startYearQuickFilter" styleId="startYearQuickFilter_dropdown" styleClass="dropdwn_sm" style="width:145px;">
					<html:optionsCollection property="filter.years" label="key" value="value" />
				</html:select>
		 	</td>
		</tr>
		<tr>
			<td><digi:trn>End Year</digi:trn>:
		 	</td>
			<td align="right">
				<html:select property="filter.endYearQuickFilter" styleId="endYearQuickFilter_dropdown" styleClass="dropdwn_sm" style="width:145px;">
					<html:optionsCollection property="filter.years" label="key" value="value" />
				</html:select>
		 	</td>
		</tr>
	<c:if test="${visualizationform.filter.dashboardType ne '1' }">
		<c:if test="${visualizationform.filter.dashboardType ne '4' }">
		<tr>
		  <td><digi:trn>Organization Group</digi:trn>:</td>
		  	<td align=right>
		     <html:select property="filter.orgGroupId" styleId="org_group_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
		         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
		          <c:forEach var="orgGrp" items="${visualizationform.filter.orgGroups}">
		         	<html:option value="${orgGrp.ampOrgGrpId}"><c:out value="${orgGrp.orgGrpName}"/></html:option>
		         </c:forEach>
		     </html:select>
		     <div id="org_group_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
			</td>
		</tr>
		<tr>
			<td><digi:trn>Organization</digi:trn>:</td>
			<td align=right>
			   <html:select property="filter.orgId" styleId="org_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
			       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
			   </html:select>
			   <div id="org_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
			</td>
		</tr>
		</c:if>
	</c:if>
	<c:if test="${visualizationform.filter.dashboardType ne '2' }">
	  <tr>
		<td><digi:trn>Region</digi:trn>:</td>
		<td align=right>
		   <html:select property="filter.regionId" styleId="region_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
		       <html:option value="-1"><digi:trn>All</digi:trn></html:option>
		       <html:optionsCollection property="filter.regions" value="id" label="name" />
		   </html:select>
		   <div id="region_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	 </tr> 
	 <tr>
	  	<td><digi:trn>Zone</digi:trn>:</td>
	   	<td align=right>
	      <html:select property="filter.zoneId" styleId="zone_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	          <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	      </html:select>
	      <div id="zone_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr> 
  </c:if>
  <c:if test="${visualizationform.filter.dashboardType ne '3' }">
  <tr>
	<td><digi:trn>Configurations</digi:trn>:</td>
	  <td align="right">
	  <html:select property="filter.selSectorConfigId" styleId="sector_config_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <c:forEach var="config" items="${visualizationform.filter.sectorConfigs}">
		<c:choose>
						<c:when test="${config.name=='Primary'}">
							<field:display name="Primary Sector" feature="Sectors">
							         <html:option value="${config.id}">${config.classification.secSchemeName}</html:option>
	     					 </field:display>
						</c:when>
						<c:when test="${config.name=='Secondary'}">
							<field:display name="Secondary Sector" feature="Sectors">
							    <html:option value="${config.id}">${config.classification.secSchemeName}</html:option>
	     					 </field:display>
						</c:when>
						<c:when test="${config.name=='Tertiary'}">
							<field:display name="Tertiary Sector" feature="Sectors">
							    <html:option value="${config.id}">${config.classification.secSchemeName}</html:option>
	     					 </field:display>
						</c:when>
		</c:choose>
		</c:forEach>
	     </html:select>
	     <div id="sector_config_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
	<tr>
	<td><digi:trn>Sector</digi:trn>:</td>
	  <td align="right">
	  <html:select property="filter.sectorId" styleId="sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	         <html:optionsCollection property="filter.sectors" value="ampSectorId" label="name" />
	     </html:select>
	     <div id="sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
	<tr>
	   <td><digi:trn>Sub-Sector</digi:trn>:</td>
	  <td align=right>
	     <html:select property="filter.subSectorId" styleId="sub_sector_dropdown_id" styleClass="dropdwn_sm" style="width:145px;">
	         <html:option value="-1"><digi:trn>All</digi:trn></html:option>
	     </html:select>
	     <div id="sub_sector_list_id" align="left" style="display:none;max-width:145;width:145px;"></div>
		</td>
	</tr>
  </c:if>
  
 </table>

	<center>
	<input type="button" value="<digi:trn>Filter</digi:trn>" class="buttonx" style="margin-top:10px;" id="applyButton">
	<input type="button" value="<digi:trn>Reset</digi:trn>" onclick="resetToDefaults()" class="buttonx" style="margin-right:10px; margin-top:10px;">
	<hr />
		<div class="tab_opt"><div class="tab_opt_cont"><a href="javascript:showPopin()" class="l_sm"><digi:trn>Advanced Filters</digi:trn></a></div></div>
	</center>
</fieldset>

<fieldset>
	<legend><span class=legend_label><digi:trn>Data Source</digi:trn></span></legend>
	<table cellspacing="0" cellpadding="0" width="100%"> 
		<tr>
			<td><b><digi:trn>Type of Funding</digi:trn>:</b>
		 	</td>
			<td align="right">
				<html:select property="filter.transactionTypeQuickFilter" styleId="transactionType_dropdown" styleClass="dropdwn_sm" style="width:145px;" onchange="callbackApplyFilter()">
					<html:option value="0"><digi:trn>Commitments</digi:trn></html:option>
					<html:option value="1"><digi:trn>Disbursements</digi:trn></html:option>
					<feature:display module="Funding" name="Expenditures">
						<html:option value="2"><digi:trn>Expenditures</digi:trn></html:option>
					</feature:display>
					<html:option value="3"><digi:trn>MTEF Projections</digi:trn></html:option>
				</html:select>
		 	</td>
		</tr>
		<tr>
			<td><b><digi:trn>Adjustment Type</digi:trn>:</b>
		 	</td>
			<td align="right">
				<html:select property="filter.adjustmentTypeQuickFilter" styleId="adjustment_type_quick" styleClass="dropdwn_sm" style="width:145px;" onchange="callbackApplyFilter()">
					<html:optionsCollection property="filter.adjustmentTypeList" value="valueKey" label="valueKey" />
				</html:select>
			</td>
		</tr>	
		<c:if test="${visualizationform.filter.dashboardType==1}">
			<tr>
				<td><b><digi:trn>Type of Agency</digi:trn>:</b>
			 	</td>
				<td align="right">
					<html:select property="filter.agencyTypeQuickFilter" styleId="agencyTypeQuickFilter_dropdown" styleClass="dropdwn_sm" style="width:145px;" onchange="callbackApplyFilter()">
						<html:option value="0"><digi:trn>Donor</digi:trn></html:option>
						<html:option value="1"><digi:trn>Executing</digi:trn></html:option>
						<html:option value="2"><digi:trn>Beneficiary</digi:trn></html:option>
					</html:select>
			 	</td>
			</tr>
		</c:if>
		<tr>
	</table>
</fieldset>
<c:if test="${visualizationform.filter.dashboardType==1}">
<fieldset id="additional_info">
	<legend><span class=legend_label><digi:trn>Additional Info</digi:trn></span></legend>
	<table cellspacing="0" cellpadding="0" width="100%"> 
		<tr>
			<td id="additional_info_box">
			</td>
		</tr>
	</table>
</fieldset>
</c:if>
<fieldset>
	<legend><span class=legend_label><digi:trn>Quick Access</digi:trn></span></legend>
	<table cellspacing="0" cellpadding="0" width="100%"> 
		<tr>
			<td>
				<c:forEach items="${visualizationform.graphList}" var="graph">
					<a href="javascript:document.getElementById('${graph.containerId}TitleLegend').scrollIntoView(true);"><digi:trn>${graph.name}</digi:trn></a> -
				</c:forEach>
			</td>
		</tr>
	</table>
</fieldset>

<feature:display name="Ranking Categories" module="Dashboard Generator">
	<field:display name="Projects Ranking" feature="Ranking Categories">
		<c:if test="${visualizationform.filter.showProjectsRanking eq 'true'}">
			<fieldset>
				<legend><span id="topProjectsTitle" class=legend_label style="width:200px"></span></legend>
				<div id="divTopProjects" class="field_text">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.topProjects}" var="projectItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <a href="/aim/selectActivityTabs.do~ampActivityId=${projectItem.key.ampActivityId}">${projectItem.key}</a> <b>($<c:out value="${projectItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:showFullProjects()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
				</div>
				<div id="divFullProjects" class="field_text" style="display: none;">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.fullProjects}" var="projectItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <a href="/aim/selectActivityTabs.do~ampActivityId=${projectItem.key.ampActivityId}">${projectItem.key}</a>  <b>($<c:out value="${projectItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:hideFullProjects()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
				</div>
			</fieldset>
		</c:if>
	</field:display>
	<c:if test="${visualizationform.filter.dashboardType ne '4' }">
	<field:display name="Organizations Ranking" feature="Ranking Categories">	
		<c:if test="${visualizationform.filter.showOrganizationsRanking eq 'true'}">
			<fieldset>
				<legend><span id="topOrganizationsTitle" class=legend_label style="width:200px"></span></legend>
				<div id="divTopOrganizations" class="field_text">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.topOrganizations}" var="organizationItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${organizationItem.key}"/>  <b>($<c:out value="${organizationItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:showFullOrganizations()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
				</div>
				<div id="divFullOrganizations" class="field_text" style="display: none;">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.fullOrganizations}" var="organizationItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${organizationItem.key}"/>  <b>($<c:out value="${organizationItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:hideFullOrganizations()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
				</div>
			</fieldset>	
		</c:if>
	</field:display>
	</c:if>	
	<field:display name="Sectors Ranking" feature="Ranking Categories">	
		<c:if test="${visualizationform.filter.showSectorsRanking eq 'true'}">
			<fieldset>
				<legend><span id="topSectorsTitle" class=legend_label style="width:200px"></span></legend>
				<div id="divTopSectors" class="field_text">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.topSectors}" var="sectorItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:showFullSectors()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
				</div>
				<div id="divFullSectors" class="field_text" style="display: none;">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.fullSectors}" var="sectorItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${sectorItem.key}"/>  <b>($<c:out value="${sectorItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:hideFullSectors()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
				</div>
			</fieldset>	
		</c:if>
	</field:display>
	<field:display name="Regions Ranking" feature="Ranking Categories">	
		<c:if test="${visualizationform.filter.showRegionsRanking eq 'true'}">
			<fieldset>
				<legend><span id="topRegionsTitle" class=legend_label style="width:200px"></span></legend>
				<div id="divTopRegions" class="field_text">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.topRegions}" var="regionItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${regionItem.key}"/>  <b>($<c:out value="${regionItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:showFullRegions()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
				</div>
				<div id="divFullRegions" class="field_text" style="display: none;">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.fullRegions}" var="regionItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${regionItem.key}"/>  <b>($<c:out value="${regionItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:hideFullRegions()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
				</div>
			</fieldset>	
		</c:if>
	</field:display>
	<field:display name="NPO Ranking" feature="Ranking Categories">	
		<c:if test="${visualizationform.filter.showNPORanking eq 'true'}">
			<fieldset>
				<legend><span id="topNPOsTitle" class=legend_label style="width:200px"></span></legend>
				<div id="divTopNPOs" class="field_text">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.topNPOs}" var="NPOItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${NPOItem.key}"/>  <b>($<c:out value="${NPOItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:showFullNPOs()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
				</div>
				<div id="divFullNPOs" class="field_text" style="display: none;">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.fullNPOs}" var="NPOItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${NPOItem.key}"/>  <b>($<c:out value="${NPOItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:hideFullNPOs()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
				</div>
			</fieldset>	
		</c:if>
	</field:display>
	<field:display name="Programs Ranking" feature="Ranking Categories">	
		<c:if test="${visualizationform.filter.showProgramsRanking eq 'true'}">
			<fieldset>
				<legend><span id="topProgramsTitle" class=legend_label style="width:200px"></span></legend>
				<div id="divTopPrograms" class="field_text">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.topPrograms}" var="programItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${programItem.key}"/>  <b>($<c:out value="${programItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:showFullPrograms()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
				</div>
				<div id="divFullPrograms" class="field_text" style="display: none;">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.fullPrograms}" var="programItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${programItem.key}"/>  <b>($<c:out value="${programItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:hideFullPrograms()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
				</div>
			</fieldset>	
		</c:if>
	</field:display>
	<field:display name="Secondary Programs Ranking" feature="Ranking Categories">	
		<c:if test="${visualizationform.filter.showSecondaryProgramsRanking eq 'true'}">
			<fieldset>
				<legend><span id="topSecondaryProgramsTitle" class=legend_label style="width:200px"></span></legend>
				<div id="divTopSecondaryPrograms" class="field_text">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.topSecondaryPrograms}" var="programItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${programItem.key}"/>  <b>($<c:out value="${programItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:showFullSecondaryPrograms()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
				</div>
				<div id="divFullSecondaryPrograms" class="field_text" style="display: none;">
					<c:set var="index" value="0"/>
					<c:forEach items="${visualizationform.ranksInformation.fullSecondaryPrograms}" var="programItem">
					<c:set var="index" value="${index+1}"/>
					
					 <c:out value="${index}"/>. <c:out value="${programItem.key}"/>  <b>($<c:out value="${programItem.value}"/>)</b>
						<hr />
					</c:forEach>
				
					<a href="javascript:hideFullSecondaryPrograms()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
				</div>
			</fieldset>	
		</c:if>
	</field:display>
</feature:display>
	</div>

</td>
<td width=15>&nbsp;</td>
<td width=689 valign="top">
<table width="689" border="0" cellspacing="0" cellpadding="0" align="center">
<tr>
<td valign="top">

<div id="demo" class="yui-navset">
	<div class="yui-content">
	<div id="tab1">
			<c:forEach items="${visualizationform.graphList}" var="graph">
			
			<%@ include file="graph.jsp" %>
			
			</c:forEach>
		</div>
	</div>
</div>


</td>
</tr>
</table>
</td>
</tr>
</table>

<script language="Javascript">

/*
function initializeGlobalVariables(){
	//Other global variables
	loadingPanel;
	myTabs;
	<digi:context name="url1" property="/visualization/pdfExport.do"/>
	urlPdfExport = "${url1}";
	<digi:context name="url2" property="/visualization/wordExport.do"/>
	urlWordExport = "${url2}";
	<digi:context name="url3" property="/visualization/excelExport.do"/>
	urlExcelExport = "${url3}";
	<digi:context name="url" property="context/visualization/saveOrgInfo.do"/>
	urlSaveAdditional = "${url}";
	<digi:context name="showList" property="context/module/moduleinstance/showProjectsList.do?" />
	urlShowList = "${showList}";
}
*/
</script>

</digi:form>


