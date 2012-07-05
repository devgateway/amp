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
var trnPledges = "";
var trnAidPredictability = "";
var trnAidType = "";
var trnFinancingInstrument = "";
var trnDonorProfile = "";
var trnSectorProfile = "";
var trnSubSectorProfile = "";
var trnRegionProfile = "";
var trnSubRegionProfile = "";
var trnShowFullList = "";
var trnTopProjects = "";
var trnTopSectors = "";
var trnTopRegions = "";
var trnTopDonors = "";
var trnShowFilterSetttings = "";
var trnHideFilterSetttings = "";
var trnTotalCommitments = "";
var trnAllAmountsInMillions = "";
var trnAllAmountsInThousands = "";
var trnTitle = "";
var trnName = "";
var trnEmails = "";
var trnPhones = "";
var trnFaxes = "";
var trnNoContactInfo = "";
var trnSave = "";
var trnDescription = "";
var trnBackgroundDonor = "";
var trnNoAdditionalInfo = "";
var trnAllOrgGroups = "";
var trnMultipleOrgs = "";
var trnAllSectors = "";
var trnMultipleSubSector = "";
var trnAllSubSector = "";
var trnAllRegions = "";
var trnAllZones = "";
var trnMultipleZones = "";
var trnODAGrowth = "";
var trnSavingInformation = "";
var trnSavedInformation = "";
var trnFailedSave = "";

var alertBadDate="";
var loadingPanel;
var urlPdfExport = "";
var urlWordExport = "";
var urlExcelExport = "";
var urlSaveAdditional = "";
var urlShowList = "";

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
	trnNumOfDons="<digi:trn jsFriendly='true'>Total Number of Donors</digi:trn>: ";
	trnNumOfSecs="<digi:trn jsFriendly='true'>Total Number of Sectors</digi:trn>: ";
	trnNumOfRegs="<digi:trn jsFriendly='true'>Total Number of Regions</digi:trn>: ";
	trnAvgProjSize="<digi:trn jsFriendly='true'>Average Project Size</digi:trn>: ";
	trnTotalDisbsDescription="<digi:trn jsFriendly='true'>Sum of Disbursements on projets filtered.</digi:trn>";
	trnNumOfProjsDescription="<digi:trn jsFriendly='true'>Number of Projects filtered.</digi:trn>";
	trnNumOfDonsDescription="<digi:trn jsFriendly='true'>Number of Donors on projects filtered</digi:trn>";
	trnNumOfSecsDescription="<digi:trn jsFriendly='true'>Number of Sectors on projects filtered</digi:trn>";
	trnNumOfRegsDescription="<digi:trn jsFriendly='true'>Number of Regions on projects filtered</digi:trn>";
	trnAvgProjSizeDescription="<digi:trn jsFriendly='true'>Total Disbursements divided Number of Projects</digi:trn>";
	trnCommitments="<digi:trn jsFriendly='true'>Commitments</digi:trn>";
	trnDisbursements="<digi:trn jsFriendly='true'>Disbursements</digi:trn>";
	trnExpenditures="<digi:trn jsFriendly='true'>Expenditures</digi:trn>";
	trnPledges="<digi:trn jsFriendly='true'>Pledges</digi:trn>";
	trnAidPredictability="<digi:trn jsFriendly='true'>Aid Predictability</digi:trn>";
	trnAidType="<digi:trn jsFriendly='true'>Aid Type</digi:trn>";
	trnFinancingInstrument="<digi:trn jsFriendly='true'>Financing Instrument</digi:trn>";
	trnDonorProfile="<digi:trn jsFriendly='true'>Donor Profile</digi:trn>";
	trnSectorProfile="<digi:trn jsFriendly='true'>Sector Profile</digi:trn>";
	trnSubSectorProfile="<digi:trn jsFriendly='true'>Sub-sector breakdown</digi:trn>";
	trnRegionProfile="<digi:trn jsFriendly='true'>Region Profile</digi:trn>";
	trnSubRegionProfile="<digi:trn jsFriendly='true'>Zone breakdown</digi:trn>";
	trnShowFullList="<digi:trn jsFriendly='true'>Show Full List</digi:trn>"; 
	trnTopProjects="<digi:trn jsFriendly='true'>Top Projects</digi:trn>";
	trnTopSectors="<digi:trn jsFriendly='true'>Top Sectors</digi:trn>";
	trnTopRegions="<digi:trn jsFriendly='true'>Top Regions</digi:trn>"; 
	trnTopDonors="<digi:trn jsFriendly='true'>Top Donors</digi:trn>"; 
	trnShowFilterSetttings="<digi:trn jsFriendly='true'>Show filter settings</digi:trn>"; 
	trnHideFilterSetttings="<digi:trn jsFriendly='true'>Hide filter settings</digi:trn>"; 
	trnTotalCommitments = "<digi:trn>Total Commitments</digi:trn>";
	trnAllAmountsInMillions = "<digi:trn>All amounts in millions</digi:trn>";
	trnAllAmountsInThousands = "<digi:trn>All amounts in thousands</digi:trn>";
	trnTitle = "<digi:trn>Title</digi:trn>";
	trnName = "<digi:trn>Name</digi:trn>";
	trnEmails = "<digi:trn>Emails</digi:trn>";
	trnPhones = "<digi:trn>Phones</digi:trn>";
	trnFaxes = "<digi:trn>Faxes</digi:trn>";
	trnNoContactInfo ="<digi:trn>No Contact Information available for current filter</digi:trn>"; 
	trnSave = "<digi:trn>Save</digi:trn>";
	trnDescription = "<digi:trn>Description</digi:trn>";
	trnBackgroundDonor = "<digi:trn>Background of donor</digi:trn>";
	trnNoAdditionalInfo = "<digi:trn>No Additional Information available for current filter</digi:trn>";
	trnAllOrgGroups = "<digi:trn jsFriendly='true'>ALL Organization Groups</digi:trn>";
	trnMultipleOrgs = "<digi:trn jsFriendly='true'>Multiple Organizations</digi:trn>"; 
	trnAllSectors = "<digi:trn jsFriendly='true'>ALL Sectors</digi:trn>"; 
	trnMultipleSubSector = "<digi:trn jsFriendly='true'>Multiple Sub Sectors</digi:trn>"; 
	trnAllSubSector = "<digi:trn jsFriendly='true'>ALL Sub Sectors</digi:trn>"; 
	trnAllRegions ="<digi:trn jsFriendly='true'>ALL Regions</digi:trn>"; 
	trnAllZones = "<digi:trn jsFriendly='true'>ALL Zones</digi:trn>"; 
	trnMultipleZones = "<digi:trn jsFriendly='true'>Multiple Zones</digi:trn>";
	trnODAGrowth = "<digi:trn jsFriendly='true'>ODA Growth Percentage</digi:trn>"; 
	trnSavingInformation = "<digi:trn>saving information, please wait</digi:trn>..."; 
	trnSavedInformation = "<digi:trn>Information was saved</digi:trn>"; 
	trnFailedSave = "<digi:trn>Failed to save information</digi:trn>";
	alertBadDate="<digi:trn>Start year can't be greater than end year</digi:trn>";
	
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
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-contains-ignorecase.js"/>"></script>

<digi:instance property="visualizationform"/>
<digi:form action="/filters.do">

<!-- POPUPS START -->
<script language="javascript">
<!--
-->
</script>
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
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside">
													<b class="ins_header"><digi:trn>Grouping Selector</digi:trn>
													</b>
												</div>
											</div>
											<div
												style="height: 180; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding: 20px;">
												<table style="width: 95%; margin-top: 15px;" align="center"
													class="inside">
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_0">
															<div class="selector_type_cont" onclick="changeTab(0)">
																<digi:trn>General</digi:trn>
															</div></td>

													</tr>
													<tr style="cursor: pointer;">
														<td class="side_opt_sel" id="general_selector_1" >
															<div class="selector_type_cont" onclick="changeTab(1)">
																<digi:trn>Organization Groups With Organizations</digi:trn>
															</div></td>

													</tr>
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
												</table>
											</div>
										</div>
										<div class="member_selector_wrapper"
											style="margin-left: 40%; padding: 0px; height: 98%;" id="generalInfoId">
											<div
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32; border: 1px solid #CCCCCC; border-bottom: 0px;">
												<div class="inside" style="float: left">
													&nbsp; <b class="ins_header"> <digi:trn>Options Selector</digi:trn>
													</b>
												</div>
											</div>
											<div
												style="height: 180; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding: 20px;"
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
												<html:radio property="filter.transactionTypeFilter"
													styleId="transaction_type_0" value="0">
													<digi:trn>Commitments</digi:trn>
												</html:radio>
												<br />
												<html:radio property="filter.transactionTypeFilter"
													styleId="transaction_type_1" value="1">
													<digi:trn>Disbursements</digi:trn>
												</html:radio>
												<br />
												<feature:display module="Funding" name="Expenditures">
													<html:radio property="filter.transactionTypeFilter"
														styleId="transaction_type_2" value="2">
														<digi:trn>Expenditures</digi:trn>
													</html:radio>
													<br />
												</feature:display>
												<hr />
											</div>
										</div>
										<div class="member_selector_wrapper" id="orgGrpContent"
											style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32; border: 1px solid #CCCCCC; border-bottom: 0px;">
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
												style="height: 180; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding: 20px;"
												id="orgGrpDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li><c:if
															test="${visualizationform.filter.dashboardType eq '1' }">
															<input type="radio" value="-1" id="org_grp_check_all"
																name="org_grp_check"
																onClick="uncheckAllRelatedEntities('organization_check')" />
														</c:if> <c:if
															test="${visualizationform.filter.dashboardType ne '1' }">
															<input type="checkbox" id="org_grp_check_all" value="-1"
																name="org_grp_check"
																onClick="allOptionChecked(this,'org_grp_check','organization_check')" />
														</c:if> <span><digi:trn>All</digi:trn>
													</span></li>
													<c:forEach
														items="${visualizationform.filter.orgGroupWithOrgsList}"
														var="item">
														<c:set var="orgGrp">
															<c:out value="${item.mainEntity.orgGrpName}"/>
														</c:set>
														<li><c:if
																test="${visualizationform.filter.dashboardType eq '1' }">
																<input type="radio" name="org_grp_check"
																	title="${orgGrp}"
																	value="${item.mainEntity.ampOrgGrpId}"
																	onClick="checkUncheckRelatedEntities(this,'organization_check',${item.mainEntity.ampOrgGrpId})" />
															</c:if> <c:if
																test="${visualizationform.filter.dashboardType ne '1' }">
																<input type="checkbox" name="org_grp_check"
																	title="${orgGrpe}"
																	value="${item.mainEntity.ampOrgGrpId}"
																	onClick="uncheckAllOption('org_grp_check');checkRelatedEntities(this,'organization_check',${item.mainEntity.ampOrgGrpId})" />
															</c:if> <span><c:out value="${orgGrp}"/>
														</span> <br />
															<ul style="list-style-type: none">
																<c:forEach items="${item.subordinateEntityList}"
																	var="organization">
																	<li><input type="checkbox"
																		class="organization_check_${item.mainEntity.ampOrgGrpId}"
																		name="organization_check" title="<c:out value='${organization.name}'/>"
																		value="${organization.ampOrgId}"
																		onclick="uncheckAllOption('org_grp_check');" /> <span><c:out value="${organization.name}"/></span>
																	</li>
																</c:forEach>
															</ul></li>
													</c:forEach>
												</ul>
											</div>
										</div>
										<div class="member_selector_wrapper" id="regionDivContent"
											style="margin-left: 40%; padding: 0px; height: 98%; display:none">
											<div
												style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32; border: 1px solid #CCCCCC; border-bottom: 0px;">
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
												style="height: 180; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding: 20px;"
												id="regionDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li><c:if
															test="${visualizationform.filter.dashboardType eq '2' }">
															<input type="radio" id="region_check_all"
																name="region_check" value="-1"
																onClick="uncheckAllRelatedEntities('zone_check')" />
														</c:if> <c:if
															test="${visualizationform.filter.dashboardType ne '2' }">
															<input type="checkbox" id="region_check_all"
																name="region_check" value="-1"
																onClick="allOptionChecked(this,'region_check','zone_check')" />
														</c:if> <span><digi:trn>All</digi:trn>
													</span></li>
													<c:forEach
														items="${visualizationform.filter.regionWithZones}"
														var="item">
														<li><c:if
																test="${visualizationform.filter.dashboardType eq '2' }">
																<input type="radio" name="region_check"
																	title="${item.mainEntity.name}"
																	value="${item.mainEntity.id}"
																	onClick="checkUncheckRelatedEntities(this,'zone_check',${item.mainEntity.id})" />
															</c:if> <c:if
																test="${visualizationform.filter.dashboardType ne '2' }">
																<input type="checkbox" name="region_check"
																	title="${item.mainEntity.name}"
																	value="${item.mainEntity.id}"
																	onClick="uncheckAllOption('region_check');checkRelatedEntities(this,'zone_check',${item.mainEntity.id})">
															</c:if> <span><c:out value="${item.mainEntity.name}"/>
														</span> <br />
															<ul style="list-style-type: none">
																<c:forEach items="${item.subordinateEntityList}"
																	var="zone">
																	<li><input type="checkbox"
																		class="zone_check_${item.mainEntity.id}"
																		name="zone_check" title="${zone.name}"
																		value="${zone.id}"
																		onclick="uncheckAllOption('region_check');" /><span><c:out value="${zone.name}"/></span>
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
													style="background-image: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif); margin: 0px; color: white; padding: 2px; height: 32; border: 1px solid #CCCCCC; border-bottom: 0px;">
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
													style="height: 180; border: 1px solid #CCCCCC; overflow: auto; background: white; maxHeight: 180; padding: 20px;"
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
															label="wrappedInstance" value="wrappedInstance" />
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
														<html:option value="-1">
															<digi:trn>None</digi:trn>
														</html:option>
														<html:optionsCollection property="filter.fiscalCalendars"
															label="name" value="ampFiscalCalId" />
													</html:select></td>
												<td><b><digi:trn>End year</digi:trn>:</b>
												</td>
												<td><html:select property="filter.endYearFilter"
														styleId="endYear_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:optionsCollection property="filter.years"
															label="wrappedInstance" value="wrappedInstance" />
													</html:select></td>
												<td><b><digi:trn>Show amounts in thousands</digi:trn>:</b>
												</td>
												<td>
														<html:checkbox property="filter.showAmountsInThousands"
																				styleId="show_amounts_in_thousands"/>
													</td>
											</tr>
											<tr>
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
												<!--<td><b><digi:trn>Year To Compare Growth</digi:trn>:</b>
												</td>
												<td><html:select property="filter.yearToCompare"
														styleId="yearToCompare_dropdown" styleClass="dropdwn_sm"
														style="width:70px;">
														<html:optionsCollection property="filter.years"
															label="wrappedInstance" value="wrappedInstance" />
													</html:select></td>
											--></tr>
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
		            <html:radio property="exportData.exportSummaryOption" styleId="export_summary_1" value="1"><digi:trn>Inculde Summary</digi:trn></html:radio><br />
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

<html:hidden property="filter.currencyCode" styleId="currencyCode" />
<html:hidden property="filter.topLists" styleId="topLists" />
<html:hidden property="filter.decimalsToShow" styleId="decimalsToShow" />
<html:hidden property="filter.startYear" styleId="startYear"/>
<html:hidden property="filter.endYear" styleId="endYear" />
<html:hidden property="filter.defaultStartYear" styleId="defaultStartYear"/>
<html:hidden property="filter.defaultEndYear" styleId="defaultEndYear" />
<html:hidden property="filter.yearToCompare" styleId="yearToCompare"/>
<html:hidden property="filter.dashboardType" styleId="dashboardType" />
<html:hidden property="filter.workspaceOnly" styleId="workspaceOnly"/>
<html:hidden property="filter.showAmountsInThousands" styleId="showAmountsInThousands"/>
<html:hidden property="filter.showMonochrome" styleId="showMonochrome"/>
<html:hidden property="filter.commitmentsVisible" styleId="commitmentsVisible"/>
<html:hidden property="filter.disbursementsVisible" styleId="disbursementsVisible" />
<html:hidden property="filter.expendituresVisible" styleId="expendituresVisible" />
<html:hidden property="filter.pledgeVisible" styleId="pledgeVisible"/>
<html:hidden property="filter.transactionType" styleId="transactionType" />
<html:hidden property="filter.currencyId" styleId="currencyId" />
<html:hidden property="filter.fiscalCalendarId" styleId="fiscalCalendarId" />
<html:hidden property="filter.groupSeparator" styleId="groupSeparator" />
<html:hidden property="filter.decimalSeparator" styleId="decimalSeparator" />
<html:hidden property="filter.fromPublicView" styleId="fromPublicView" />

<div class="dashboard_header">
<!--<div class="dashboard_total"><b class="dashboard_total_num">${visualizationform.summaryInformation.totalCommitments}</b><br /><digi:trn>Total Commitments</digi:trn> ( ${visualizationform.filter.currencyId} )</div>-->
<div class="dashboard_total" id="divTotalComms"></div>
<div class="dashboard_name" id="dashboard_name">${visualizationform.dashboard.name}</div>
 <table border="0" cellspacing="0" cellpadding="0">
  <tr>  
    <td>
    	<table>
    		<tr>
    			<td><div class="dash_ico"><img src="/TEMPLATE/ampTemplate/img_2/ico_export.gif" align=left style="margin-right:5px;"> <div class="dash_ico_link"><a href="javascript:showExport()" class="l_sm"><digi:trn>Export Options</digi:trn></a></div></div></td>
    		</tr>
    		<tr>
    			<td><div id="currencyInfo"></div></td>
    		</tr>
    	</table>
   	</td>
  </tr>
</table>
<div class="dashboard_stat" id="divSummaryInfo" ></div>
<div class="dashboard_stat" align="right" ><a onClick="toggleSettings();" id="displaySettingsButton"><digi:trn>Show filter settings</digi:trn></a></div>
<div class="dashboard_stat" style="display:none; padding:2px 2px 2px 2px;" id="currentDisplaySettings" >
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr style="background-color:white;" >
	<td style="font-size:11px;font-family:Arial,Helvetica,sans-serif" valign="top">
	<strong>
	<digi:trn>Selected Filters</digi:trn>:</strong>
	   <i><digi:trn>Currency type</digi:trn>: </i><label id="filterCurrency"></label> |
	   <i><digi:trn>Start year</digi:trn>: </i><label id="filterStartYear">${visualizationform.filter.startYear}</label> | 
	   <i><digi:trn>End year</digi:trn>: </i><label id="filterEndYear">${visualizationform.filter.endYear}</label> | 
	   <i><digi:trn>Org. groups</digi:trn>: </i><label id="filterOrgGroups"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Organizations</digi:trn>: </i><label id="filterOrganizations"><digi:trn>All</digi:trn></label> | 
	   <i><digi:trn>Configuration</digi:trn>: </i><label id="filterSectorConfiguration"><digi:trn>Primary</digi:trn></label> | 
	   <i><digi:trn>Sectors</digi:trn>: </i><label id="filterSectors"><digi:trn>All</digi:trn></label> |
	   <i><digi:trn>Regions</digi:trn>: </i><label id="filterRegions"><digi:trn>All</digi:trn></label>
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
		<tr>
			<td><digi:trn>Type of Funding</digi:trn>:
		 	</td>
			<td align="right">
				<html:select property="filter.transactionTypeQuickFilter" styleId="transactionType_dropdown" styleClass="dropdwn_sm" style="width:145px;">
					<html:option value="0"><digi:trn>Commitments</digi:trn></html:option>
					<html:option value="1"><digi:trn>Disbursements</digi:trn></html:option>
					<html:option value="2"><digi:trn>Expenditures</digi:trn></html:option>
				</html:select>
		 	</td>
		</tr>
		<tr>
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
					<html:optionsCollection property="filter.years" label="wrappedInstance" value="wrappedInstance" />
				</html:select>
		 	</td>
		</tr>
		<tr>
			<td><digi:trn>End Year</digi:trn>:
		 	</td>
			<td align="right">
				<html:select property="filter.endYearQuickFilter" styleId="endYearQuickFilter_dropdown" styleClass="dropdwn_sm" style="width:145px;">
					<html:optionsCollection property="filter.years" label="wrappedInstance" value="wrappedInstance" />
				</html:select>
		 	</td>
		</tr>
	<c:if test="${visualizationform.filter.dashboardType eq '1' }">
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
	<c:if test="${visualizationform.filter.dashboardType eq '2' }">
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
  <c:if test="${visualizationform.filter.dashboardType eq '3' }">
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
  
	<c:if test="${visualizationform.filter.dashboardType ne '1' }">
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
<c:if test="${visualizationform.filter.showDonorsRanking eq 'true'}">
	<fieldset>
		<legend><span id="topDonorsTitle" class=legend_label style="width:200px"></span></legend>
		<div id="divTopDonors" class="field_text">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.topDonors}" var="donorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:showFullDonors()" style="float:right;"><digi:trn>Show Full List</digi:trn></a>
		</div>
		<div id="divFullDonors" class="field_text" style="display: none;">
			<c:set var="index" value="0"/>
			<c:forEach items="${visualizationform.ranksInformation.fullDonors}" var="donorItem">
			<c:set var="index" value="${index+1}"/>
			
			 <c:out value="${index}"/>. <c:out value="${donorItem.key}"/>  <b>($<c:out value="${donorItem.value}"/>)</b>
				<hr />
			</c:forEach>
		
			<a href="javascript:hideFullDonors()" style="float:right;"><digi:trn>View Top List</digi:trn></a>
		</div>
	</fieldset>	
</c:if>
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
</div>
</td>
<td width=15>&nbsp;</td>
<td width=689 valign="top">
<table width="689" border="0" cellspacing="0" cellpadding="0" align="center">
<tr>
<td valign="top">

<!--  Start Global variables for all flash files -->
<input type="hidden" id="GlobalFontSize" value="11" />
<input type="hidden" id="GlobalFontFamily" value="Arial" />
<input type="hidden" id="GlobalFontWeight" value="bold" />
<input type="hidden" id="trnMessagePanel" value="<digi:trn jsFriendly='true'>Empty Dataset</digi:trn>" />
<input type="hidden" id="trnMessageEmpty" value="<digi:trn jsFriendly='true'>No data to show</digi:trn>" />
<input type="hidden" id="trnMessageLoadingPanel" value="<digi:trn jsFriendly='true'>Loading</digi:trn>" />
<input type="hidden" id="trnMessageLoading" value="<digi:trn jsFriendly='true'>Loading data...</digi:trn>" />
<!--  End Global variables for all flash files -->

<div id="demo" class="yui-navset">
	<ul class="yui-nav">
		<li><a href="#tab1"><div id="visualizationDiv"><digi:trn>Visualization</digi:trn></div></a></li>
		<c:if test="${visualizationform.filter.dashboardType eq '1' }">
		<li><a href="#tab2"><div><digi:trn>Contact Information</digi:trn></div></a></li>
		<c:if test="${!visualizationform.filter.fromPublicView}">
		<li><a href="#tab3"><div><digi:trn>Additional Notes</digi:trn></div></a></li>
		</c:if>
		</c:if>
	</ul>
	<div class="yui-content">
	<div id="tab1">
			<c:forEach items="${visualizationform.graphList}" var="graph">
			<fieldset class="chartFieldset">
				<legend><span id="${graph.containerId}TitleLegend" class=legend_label ></span></legend>
				<a onclick="toggleHeader(this, '${graph.containerId}Header')" style=""><img src="/TEMPLATE/ampTemplate/img_2/ico_perm_open.gif" vspace="5" align="absMiddle"/> <digi:trn>Show settings</digi:trn></a>
				<div id="${graph.containerId}Header" class="chart_header" style="display:none;">
				<digi:trn>Title</digi:trn> <input type="text" id="${graph.containerId}Title" value="" size="50">
				<input type="hidden" id="${graph.containerId}ShowFontFamily" value="Verdana"/>
				&nbsp;<digi:trn>Size</digi:trn>
				<select id="${graph.containerId}FontSize">
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
				</select>
				&nbsp;<input type="checkbox" id="${graph.containerId}Bold"><label for="${graph.containerId}Bold"><digi:trn>Bold</digi:trn></label><br/>
				<input type="checkbox" id="${graph.containerId}ShowLegend" checked="checked"><label id="${graph.containerId}ShowLegendLabel" for="${graph.containerId}ShowLegend"><digi:trn>Show legend</digi:trn></label>
				&nbsp;<input type="checkbox" id="${graph.containerId}Divide"><label id="${graph.containerId}DivideLabel" for="${graph.containerId}Divide"><digi:trn>Divide by thousands</digi:trn></label>
				&nbsp;<input type="checkbox" id="${graph.containerId}ShowDataLabel"><label id="${graph.containerId}ShowDataLabelLabel" for="${graph.containerId}ShowDataLabel"><digi:trn>Show data label</digi:trn></label>
				&nbsp;<input type="checkbox" id="${graph.containerId}RotateDataLabel"><label id="${graph.containerId}RotateDataLabelLabel" for="${graph.containerId}RotateDataLabel"><digi:trn>Rotate data label</digi:trn></label>
				&nbsp;<input type="checkbox" id="${graph.containerId}Ignore" style="display: none;" checked="checked"><label id="${graph.containerId}IgnoreLabel" style="display: none;" for="${graph.containerId}Ignore"><digi:trn>Ignore big values</digi:trn></label></br>
				<input type="hidden" id="${graph.containerId}DataAction" value="get${graph.containerId}GraphData" />
				<input type="hidden" id="${graph.containerId}DataField" value="dataField" />
				<input type="hidden" id="${graph.containerId}TitleLegendTrn" value="<digi:trn>${graph.name}</digi:trn>" />
				<input type="button" class="buttonx" value="<digi:trn>Update chart</digi:trn>" onclick="updateGraph(event, '${graph.containerId}')">
				</div>
				<div class="dash_graph_opt">
					<c:if test="${graph.barGraphEnabled==true}">
						<img style="padding-left: 5px" onclick="changeChart(event, 'bar', '${graph.containerId}', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/>
					</c:if>
					<c:if test="${graph.barProfileGraphEnabled==true}">
						<img style="padding-left: 5px" onclick="changeChart(event, 'bar_profile', '${graph.containerId}', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/>
					</c:if>
					<c:if test="${graph.barGrowthGraphEnabled==true}">
						<img style="padding-left: 5px" onclick="changeChart(event, 'bar_growth', '${graph.containerId}', true)" src="/TEMPLATE/ampTemplate/img_2/barchart.gif" title="<digi:trn>Bar Chart</digi:trn>"/>
					</c:if>
					<c:if test="${graph.pieGraphEnabled==true}">
						<img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/donutchart.png" onclick="changeChart(event, 'donut', '${graph.containerId}', true)" title="<digi:trn>Donut Chart</digi:trn>"/>
					</c:if>
					<c:if test="${graph.lineGraphEnabled==true}">
						<img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/linechart.gif" onclick="changeChart(event, 'line', '${graph.containerId}', true)" title="<digi:trn>Line Chart</digi:trn>"/>
					</c:if>
					<c:if test="${graph.dataListEnabled==true}">
						<img style="padding-left: 5px" src="/TEMPLATE/ampTemplate/img_2/datasheet.gif" onclick="changeChart(event, 'dataview', '${graph.containerId}', true)" title="<digi:trn>Data View</digi:trn>"/>
					</c:if>
				</div>
				<br />
				<br />
				<div class="flashcontent" name="flashContent">
					<div id="${graph.containerId}">
						<a href="http://www.adobe.com/go/getflashplayer" title="<digi:trn>Click here to get Adobe Flash player</digi:trn>">
							<img src="/TEMPLATE/ampTemplate/img_2/get_flash_player.gif" alt="Get Adobe Flash player" />
						</a>
					</div>
				</div>
				<div align="right">
					<br /><a href="javascript:document.getElementById('dashboard_name').scrollIntoView(true);"><digi:trn>Back to Top</digi:trn></a>
				</div> 
			</fieldset>
			
			</c:forEach>
		</div>
	<c:if test="${visualizationform.filter.dashboardType eq '1' }">
	<div id="tab2">
		<digi:trn>No Contact Information available for current filter selection</digi:trn>
	</div>
	<c:if test="${!visualizationform.filter.fromPublicView}">
	<div id="tab3">
	</div>
	</c:if>
	</c:if>
	</div>
</div>


</td>
</tr>
</table>
</td>
</tr>
</table>

<script language="Javascript">
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
	trnNumOfDons="<digi:trn jsFriendly='true'>Total Number of Donors</digi:trn>: ";
	trnNumOfSecs="<digi:trn jsFriendly='true'>Total Number of Sectors</digi:trn>: ";
	trnNumOfRegs="<digi:trn jsFriendly='true'>Total Number of Regions</digi:trn>: ";
	trnAvgProjSize="<digi:trn jsFriendly='true'>Average Project Size</digi:trn>: ";
	trnTotalDisbsDescription="<digi:trn jsFriendly='true'>Sum of Disbursements on projets filtered.</digi:trn>";
	trnNumOfProjsDescription="<digi:trn jsFriendly='true'>Number of Projects filtered.</digi:trn>";
	trnNumOfDonsDescription="<digi:trn jsFriendly='true'>Number of Donors on projects filtered</digi:trn>";
	trnNumOfSecsDescription="<digi:trn jsFriendly='true'>Number of Sectors on projects filtered</digi:trn>";
	trnNumOfRegsDescription="<digi:trn jsFriendly='true'>Number of Regions on projects filtered</digi:trn>";
	trnAvgProjSizeDescription="<digi:trn jsFriendly='true'>Total Disbursements divided Number of Projects</digi:trn>";
	trnCommitments="<digi:trn jsFriendly='true'>Commitments</digi:trn>";
	trnDisbursements="<digi:trn jsFriendly='true'>Disbursements</digi:trn>";
	trnExpenditures="<digi:trn jsFriendly='true'>Expenditures</digi:trn>";
	trnPledges="<digi:trn jsFriendly='true'>Pledges</digi:trn>";
	trnAidPredictability="<digi:trn jsFriendly='true'>Aid Predictability</digi:trn>";
	trnAidType="<digi:trn jsFriendly='true'>Aid Type</digi:trn>";
	trnFinancingInstrument="<digi:trn jsFriendly='true'>Financing Instrument</digi:trn>";
	trnDonorProfile="<digi:trn jsFriendly='true'>Donor Profile</digi:trn>";
	trnSectorProfile="<digi:trn jsFriendly='true'>Sector Profile</digi:trn>";
	trnSubSectorProfile="<digi:trn jsFriendly='true'>Sub-sector breakdown</digi:trn>";
	trnRegionProfile="<digi:trn jsFriendly='true'>Region Profile</digi:trn>";
	trnSubRegionProfile="<digi:trn jsFriendly='true'>Zone breakdown</digi:trn>";
	trnShowFullList="<digi:trn jsFriendly='true'>Show Full List</digi:trn>"; 
	trnTopProjects="<digi:trn jsFriendly='true'>Top Projects</digi:trn>";
	trnTopSectors="<digi:trn jsFriendly='true'>Top Sectors</digi:trn>";
	trnTopRegions="<digi:trn jsFriendly='true'>Top Regions</digi:trn>"; 
	trnTopDonors="<digi:trn jsFriendly='true'>Top Donors</digi:trn>"; 
	trnShowFilterSetttings="<digi:trn jsFriendly='true'>Show filter settings</digi:trn>"; 
	trnHideFilterSetttings="<digi:trn jsFriendly='true'>Hide filter settings</digi:trn>"; 
	trnTotalCommitments = "<digi:trn>Total Commitments</digi:trn>";
	trnAllAmountsInMillions = "<digi:trn>All amounts in millions</digi:trn>";
	trnTitle = "<digi:trn>Title</digi:trn>";
	trnName = "<digi:trn>Name</digi:trn>";
	trnEmails = "<digi:trn>Emails</digi:trn>";
	trnPhones = "<digi:trn>Phones</digi:trn>";
	trnFaxes = "<digi:trn>Faxes</digi:trn>";
	trnNoContactInfo ="<digi:trn>No Contact Information available for current filter</digi:trn>"; 
	trnSave = "<digi:trn>Save</digi:trn>";
	trnDescription = "<digi:trn>Description</digi:trn>";
	trnBackgroundDonor = "<digi:trn>Background of donor</digi:trn>";
	trnNoAdditionalInfo = "<digi:trn>No Additional Information available for current filter</digi:trn>";
	trnAllOrgGroups = "<digi:trn jsFriendly='true'>ALL Organization Groups</digi:trn>";
	trnMultipleOrgs = "<digi:trn jsFriendly='true'>Multiple Organizations</digi:trn>"; 
	trnAllSectors = "<digi:trn jsFriendly='true'>ALL Sectors</digi:trn>"; 
	trnMultipleSubSector = "<digi:trn jsFriendly='true'>Multiple Sub Sectors</digi:trn>"; 
	trnAllSubSector = "<digi:trn jsFriendly='true'>ALL Sub Sectors</digi:trn>"; 
	trnAllRegions ="<digi:trn jsFriendly='true'>ALL Regions</digi:trn>"; 
	trnAllZones = "<digi:trn jsFriendly='true'>ALL Zones</digi:trn>"; 
	trnMultipleZones = "<digi:trn jsFriendly='true'>Multiple Zones</digi:trn>";
	trnODAGrowth = "<digi:trn jsFriendly='true'>ODA Growth Percentage</digi:trn>"; 
	trnSavingInformation = "<digi:trn>saving information, please wait</digi:trn>..."; 
	trnSavedInformation = "<digi:trn>Information was saved</digi:trn>"; 
	trnFailedSave = "<digi:trn>Failed to save information</digi:trn>";
	alertBadDate="<digi:trn>Start year can't be greater than end year</digi:trn>";
}
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

</script>

</digi:form>


