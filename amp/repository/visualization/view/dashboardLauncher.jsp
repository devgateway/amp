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
var trnBackgroundOrganization = "";
var trnNoAdditionalInfo = "";
var trnAllOrgGroups = "";
var trnMultipleOrgs = "";
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

var alertBadDate="";
var selectOneGraph="";
var loadingPanel;
var urlPdfExport = "";
var urlWordExport = "";
var urlExcelExport = "";
var urlSaveAdditional = "";
var urlShowList = "";
var urlLaunchDashboard = "";

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
	trnTotalDisbsDescription="<digi:trn jsFriendly='true'>Sum of Disbursements on projets filtered.</digi:trn>";
	trnNumOfProjsDescription="<digi:trn jsFriendly='true'>Number of Projects filtered.</digi:trn>";
	trnNumOfDonsDescription="<digi:trn jsFriendly='true'>Number of Organizations on projects filtered</digi:trn>";
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
	trnBackgroundOrganization = "<digi:trn>Background of organization</digi:trn>";
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
	selectOneGraph="<digi:trn>At least one graph should be selected</digi:trn>";
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
	<digi:context name="launchDashboard" property="context/module/moduleinstance/launchDashboard.do?" />
	urlLaunchDashboard = "${launchDashboard}";
	
}

</script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/flash/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-contains-ignorecase.js"/>"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/visualization/visualization.js"></script> 

<digi:instance property="visualizationform"/>

<digi:form action="/launchDashboard.do">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title>Steps</title>

	<style>
    	*{margin:0px;padding:0px;}

		#stepscontainer{width:960px;margin:0 auto;padding:5px;background:#fff;font-family:arial;font-size:12px;}

		#stepscontainer h2{text-align:center;text-transform:uppercase;font-size:13px;font-weight:bold;margin:0px 0px 0px 0px; background-color: #4A687A; color:#FFFFFF; padding:5px;}

		.stepsbox{border:1px solid #d0d0d0;padding:10px;margin:0px 0px 20px 0px; background-color: #FBFBFB;}

		.leftbox{float:left;width:465px;border:1px solid #fff;}

		.rightbox{float:right;width:465px;border:1px solid #fff;}

		.boxes{background:url(/TEMPLATE/ampTemplate/img_2/split.gif) repeat-y center;border:1px solid #d0d0d0;background-color:#FFFFFF;}

		.stepsbox h3{background:url(/TEMPLATE/ampTemplate/img_2/ins_header.gif) repeat-x;height:40px;margin:0px;padding:5px 10px 0px 10px;color:#76797A;line-height:10px; font-size:12px;}

		.innerbox{height:250px;overflow:auto;}

		ul li{list-style:none;}

		.leftbox ul{margin:10px;border:1px solid #d0d0d0;border-bottom:none;}

		.leftbox ul li{border-bottom:1px solid #d0d0d0;}

		.leftbox ul li a{padding:10px;display:block;text-decoration:none;color:#000;}

		.leftbox ul li a:hover{background:#bfd2df;}

		.rightbox ul{margin:10px;}

		.rightbox ul li {padding:2px;}
		.rightbox ul li input {margin-right:5px;}

		.stepsbox ul li label{padding:0px 0px 0px 10px;}

		#step2 ul li {padding:5px; float:left;width:290px;font-weight:bold;}

		#step2 form {width:600px;text-align:center;margin:0 auto;}
	</style>

</head>

<body>

	<div id="stepscontainer">

    	<h2><digi:trn>Step 1: Select a Dashboard Type</digi:trn></h2>

    	<div id="step1" class="stepsbox">

			<div class="boxes">

        	<div class="leftbox">

            	<h3><digi:trn>Dashboards</digi:trn></h3>

				<div class="innerbox">

                	<ul>
						<c:forEach var="dashboard" items="${visualizationform.dashboardList}">
							<li>
								<a class="side_opt_sel" name="dsbd" id="${dashboard.id}" href="JavaScript:callbackGetGraphs(${dashboard.id},${dashboard.baseType});">
								<digi:trn><c:out value="${dashboard.name}"/></digi:trn>
								</a>
							</li>
						</c:forEach>
                	</ul>

				</div>

            </div>

            <div class="rightbox"><h3><digi:trn>Graphs in dashboards</digi:trn></h3>

				<div class="innerbox">

                <ul id="graphList">


                </ul>

				</div>

				</div>

            <div style="clear:both;"></div>

			</div>

		</div>

			<h2><digi:trn>Step 2: Select type of funding and ranking categories</digi:trn></h2>

    	<div id="step2" class="stepsbox" >
			<div class="boxes">
				<div style="float:left;width:450px;border:1px solid #fff;">
					<table>
					<tr><td>
					<ul>
						<li>
							<html:radio property="filter.transactionTypeFilter" styleId="transaction_type_0" value="0">
								<digi:trn>Commitments</digi:trn>
							</html:radio>
						</li>
						<li>
							<html:radio property="filter.transactionTypeFilter" styleId="transaction_type_1" value="1">
								<digi:trn>Disbursements</digi:trn>
							</html:radio>
						</li>
						<li>
							<feature:display module="Funding" name="Expenditures">
								<html:radio property="filter.transactionTypeFilter" styleId="transaction_type_2" value="2">
									<digi:trn>Expenditures</digi:trn>
								</html:radio>
							</feature:display>
						</li>
					</ul>
					</td></tr>
					<tr><td><hr/></td></tr>
					<tr><td>
					<ul>
						<c:forEach items="${visualizationform.filter.adjustmentTypeList}" var="item">
							<li>
								<html:radio property="filter.adjustmentType" styleId="adjustment_type" value="${item.valueKey}">
									<digi:trn><c:out value='${item.valueKey}'/></digi:trn>
								</html:radio>
							</li>
						</c:forEach>
					</ul>
					</td></tr>
					</table>
				</div>
				<div style="float:right; width:450px;border:1px solid #fff;">
				<feature:display name="Ranking Categories" module="Dashboard Generator">
					<ul>
						<field:display name="Projects Ranking" feature="Ranking Categories">
							<li>
								<html:checkbox property="filter.showProjectsRanking" styleId="show_projects_ranking">
									<digi:trn>Show Projects Ranking</digi:trn>
								</html:checkbox>
							</li>
						</field:display>
						<field:display name="Organizations Ranking" feature="Ranking Categories">	
							<li>
							<html:checkbox property="filter.showOrganizationsRanking" styleId="show_organizations_ranking">
								<digi:trn>Show Organizations Ranking</digi:trn>
							</html:checkbox>
							</li>
						</field:display>
						<field:display name="Sectors Ranking" feature="Ranking Categories">		
							<li>
							<html:checkbox property="filter.showSectorsRanking" styleId="show_sectors_ranking">
								<digi:trn>Show Sectors Ranking</digi:trn>
							</html:checkbox>
							</li>
						</field:display>	
						<field:display name="Regions Ranking" feature="Ranking Categories">
							<li>
								<html:checkbox property="filter.showRegionsRanking" styleId="show_regions_ranking">
									<digi:trn>Show Regions Ranking</digi:trn>
								</html:checkbox>
							</li>
						</field:display>
						<field:display name="NPO Ranking" feature="Ranking Categories">
							<li>
								<html:checkbox property="filter.showNPORanking" styleId="show_NPO_ranking">
									<digi:trn>Show NPO Ranking</digi:trn>
								</html:checkbox>
							</li>
						</field:display>
						<field:display name="Programs Ranking" feature="Ranking Categories">
							<li>
								<html:checkbox property="filter.showProgramsRanking" styleId="show_programs_ranking">
									<digi:trn>Show Programs Ranking</digi:trn>
								</html:checkbox>
							</li>
						</field:display>
						<field:display name="Secondary Programs Ranking" feature="Ranking Categories">
							<li>
								<html:checkbox property="filter.showSecondaryProgramsRanking" styleId="show_secondary_programs_ranking">
									<digi:trn>Show Secondary Programs Ranking</digi:trn>
								</html:checkbox>
							</li>
						</field:display>
					</ul>
				</feature:display>	
				</div>
				<div style="clear:both;"></div>
				
			</div>

		</div>

        <h2><digi:trn>Step 3: Select filters</digi:trn></h2>

    	<div id="step3" class="stepsbox">
			<c:set var="selectorHeaderSize" scope="page" value="11" />
								<div class="boxes" id="generalTab">
										<div class="leftbox">
											<h3><digi:trn>Grouping Selector</digi:trn></h3>
											<div class="innerbox">
											
												<ul>
								                	<li><a class="side_opt_sel" id="general_selector_0" href="JavaScript:changeTab(0);"><digi:trn>General</digi:trn></a></li>
								                    <li><a class="side_opt_sel" id="general_selector_1" href="JavaScript:changeTab(1);"><digi:trn>Organization Groups With Organizations</digi:trn></a></li>
								                    <li><a class="side_opt_sel" id="general_selector_2" href="JavaScript:changeTab(2);"><digi:trn>Regions With Zones</digi:trn></a></li>
								                    <li><a class="side_opt_sel" id="general_selector_3" href="JavaScript:changeTab(3);"><digi:trn>Sectors and Sub Sectors</digi:trn></a></li>
								                </ul>
								             </div>
										</div>
										<div  class="rightbox" id="generalInfoId" style="display: none;">
											<h3><digi:trn>Options Selector</digi:trn></h3>
												<div class="innerbox" id="generalDivList">
												<c:if test="${!visualizationform.filter.fromPublicView}">
													<ul>
														<li>
														<html:checkbox property="filter.workspaceOnly"
														styleId="workspace_only">
														<digi:trn>Show Only Data From This Workspace</digi:trn>
														</html:checkbox>
														<c:set var="translation">
															<digi:trn>Dashboards will show only data from activities of current workspace.</digi:trn>
														</c:set>
														<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/>
														</li>
													</ul>
													<br />
												</c:if>
												<hr />
												<br />
													<digi:trn>For Time Series Comparison, what data do you want to show?</digi:trn>
													<c:set var="translation">
													<digi:trn>What data will show the ODA Historical Trend graph.</digi:trn>
													</c:set>
													<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/><br />
													<ul>
														<li>
															<html:checkbox property="filter.commitmentsVisible" styleId="commitments_visible">
															<digi:trn>Commitments</digi:trn>&nbsp;&nbsp;</html:checkbox>
														</li>
														<li>
															<html:checkbox property="filter.disbursementsVisible" styleId="disbursements_visible">
															<digi:trn>Disbursements</digi:trn>&nbsp;&nbsp;</html:checkbox>
														</li>
														<li>
															<feature:display module="Funding" name="Expenditures">
																<html:checkbox property="filter.expendituresVisible" styleId="expenditures_visible">
																<digi:trn>Expenditures</digi:trn>&nbsp;&nbsp;</html:checkbox>
															</feature:display>
														</li>
														<li>
															<module:display name="Pledges" parentModule="PROJECT MANAGEMENT">
																<html:checkbox property="filter.pledgeVisible" styleId="pledge_visible">
																<digi:trn>Pledges</digi:trn>&nbsp;&nbsp;</html:checkbox>
															</module:display>
														</li>
													</ul>
													<br />
													<digi:trn>Select activity Status</digi:trn>
													<c:set var="translation">
														<digi:trn>Select the activity status that you want to show on dashboard, if none is selected, then show all.</digi:trn>
													</c:set>
													<img src="/TEMPLATE/ampTemplate/img_2/ico_quest.gif" title="${translation}"/>
													<br />
													<br />
													<ul>
													<c:forEach items="${visualizationform.filter.statusList}" var="item">
														<li>
														<input type="checkbox" id="status_check_${item.id}" name="status_check" title="<c:out value='${item.value}'/>" value="${item.id}" /> 
														<span><c:out value="${item.value}"/></span>
														</li>
													</c:forEach>
													</ul>
												<br />
											</div>
										</div>
										<div class="rightbox" id="orgGrpContent" style="display: none;">
											<h3><div style="float:left;"><digi:trn>Member Selector</digi:trn></div>
											<div style="float:right; padding:0;"><input onkeypress="clearSearch('orgGrpDivList')"
														id="orgGrpDivList_search" type="text" class="inputx" /> <input
														type="button" class="buttonx_sm"
														onclick="findPrev('orgGrpDivList')" value='&lt;&lt;' /> <input
														type="button" onclick="findNext('orgGrpDivList')"
														class="buttonx_sm" value="&gt;&gt;" /></div>
											</h3>			
											<div
												class="innerbox" id="orgGrpDivList">
												<ul style="list-style-type: none;margin-left: 0px;">
													<li><input type="checkbox" id="org_grp_check_all" value="-1"
																name="org_grp_check"
																onClick="allOptionChecked(this,'org_grp_check','organization_check')" />
													<span><digi:trn>All</digi:trn>
													</span></li>
													<c:forEach
														items="${visualizationform.filter.orgGroupWithOrgsList}"
														var="item">
														<c:set var="orgGrp">
															<c:out value="${item.mainEntity.orgGrpName}"/>
														</c:set>
														<li><input type="checkbox" name="org_grp_check"
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
										<div class="rightbox" id="regionDivContent" style="display: none;">
											<h3><div style="float:left;"><digi:trn>Member Selector</digi:trn></div>
											<div style="float:right; padding:0;"><input onkeypress="clearSearch('regionDivList')"
														id="regionDivList_search" type="text" class="inputx" /> <input
														type="button" class="buttonx_sm"
														onclick="findPrev('regionDivList')" value='&lt;&lt;' /> <input
														type="button" onclick="findNext('regionDivList')"
														class="buttonx_sm" value="&gt;&gt;" /></div>
											</h3>	
											
											<div class="innerbox" id="regionDivList">
												<ul>
													<li>
														<input type="checkbox" id="region_check_all"
																name="region_check" value="-1"
																onClick="allOptionChecked(this,'region_check','zone_check')" />
														<span><digi:trn>All</digi:trn>
													</span></li>
													<c:forEach
														items="${visualizationform.filter.regionWithZones}"
														var="item">
														<li><input type="checkbox" name="region_check"
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
										<div class="rightbox" id="sectorDivContent" style="display: none;">
												<h3><div style="float:left;"><digi:trn>Member Selector</digi:trn></div>
												<div style="float:right; padding:0;"><input onkeypress="clearSearch('sectorDivList')"
															id="sectorDivList_search" type="text" class="inputx" />
														<input type="button" class="buttonx_sm"
															onclick="findPrev('sectorDivList')" value='&lt;&lt;' />
														<input type="button" onclick="findNext('sectorDivList')"
															class="buttonx_sm" value="&gt;&gt;" /></div>
												</h3>	
												<div class="innerbox" id="sectorDivList">
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
											<div style="clear:both;">
					        		</div>
							</div>
							<div>

								<br />
									<div >
									<table border="0" cellspacing="3" cellpadding="3">
 									 <tr>
  										  <td class="dashboard_generator_opt"><b><digi:trn>Start year</digi:trn>:</b></td>
   										  <td class="dashboard_generator_opt"><html:select property="filter.startYearFilter" styleId="startYear_dropdown" styleClass="dropdwn_sm" style="width:70px;">
														<html:optionsCollection property="filter.years"
															label="key" value="value" />
													</html:select></td>
													<td rowspan=2 width=15>&nbsp;</td>
   										 <td class="dashboard_generator_opt"><b><digi:trn>Decimals to show</digi:trn>:</b></td>
    										 <td class="dashboard_generator_opt"><html:select property="filter.decimalsToShow" styleId="decimalsToShow_dropdown" styleClass="dropdwn_sm" style="width:70px;">
														<html:option value="0">0</html:option>
														<html:option value="1">1</html:option>
														<html:option value="2">2</html:option>
														<html:option value="3">3</html:option>
														<html:option value="4">4</html:option>
														<html:option value="5">5</html:option>
													</html:select>
											</td>
													<td rowspan=2 width=15>&nbsp;</td>
   										 <td class="dashboard_generator_opt"><b><digi:trn>Fiscal Calendar</digi:trn>:</b></td>
   										 <td class="dashboard_generator_opt"><html:select property="filter.fiscalCalendarId" styleId="fiscalCalendar_dropdown_Id" styleClass="dropdwn_sm" style="width:150px;">
											<html:optionsCollection property="filter.fiscalCalendars" label="name" value="ampFiscalCalId" />
										</html:select></td>
										<td rowspan=2 width=15>&nbsp;</td>
										<td class="dashboard_generator_opt"><b><digi:trn>Show amounts in</digi:trn>:</b></td>
 										<td class="dashboard_generator_opt">
 											<html:select property="filter.showAmountsInThousands" styleId="show_amounts_in_thousands" styleClass="dropdwn_sm" style="width:150px;">
 										<%--	<html:option value="0"><digi:trn>Units</digi:trn></html:option>  --%>
 												<html:option value="1"><digi:trn>Thousands</digi:trn></html:option>
 												<html:option value="2"><digi:trn>Millions</digi:trn></html:option>
 											</html:select>
 										</td>
										
 									 </tr>
									 <tr>
									 	<td class="dashboard_generator_opt"><b><digi:trn>End year</digi:trn>:</b></td>
  									     <td class="dashboard_generator_opt"><html:select property="filter.endYearFilter" styleId="endYear_dropdown" styleClass="dropdwn_sm" style="width:70px;">
														<html:optionsCollection property="filter.years" label="key" value="value" />
													</html:select></td>
   										
    										<td class="dashboard_generator_opt"><b><digi:trn>Show in top ranks</digi:trn>:</b></td>
    									    <td class="dashboard_generator_opt"><html:select property="filter.topLists" styleId="topLists_dropdown" styleClass="dropdwn_sm" style="width:70px;">
														<html:option value="5">5</html:option>
														<html:option value="10">10</html:option>
														<html:option value="20">20</html:option>
														<html:option value="50">50</html:option>
													</html:select></td>
											<td class="dashboard_generator_opt"><b><digi:trn>Currency Type</digi:trn>:</b></td>
  										    <td class="dashboard_generator_opt"><html:select property="filter.currencyId" styleId="currencies_dropdown_ids" styleClass="dropdwn_sm" style="width:150px;">
														<html:optionsCollection property="filter.currencies" value="ampCurrencyId" label="currencyName" />
													</html:select></td>
													
   											 
											<td class="dashboard_generator_opt" id="agencyTypeSelector1" style="display: none;"><b><digi:trn>Type of Agency</digi:trn>:</b></td>
											<td class="dashboard_generator_opt" id="agencyTypeSelector2" style="display: none;">
												<html:select property="filter.agencyTypeFilter" styleId="agencyType_dropdown" styleClass="dropdwn_sm" style="width:150px;">
													<html:option value="0"><digi:trn>Donor</digi:trn></html:option>
													<html:option value="1"><digi:trn>Executing</digi:trn></html:option>
													<html:option value="2"><digi:trn>Beneficiary</digi:trn></html:option>
												</html:select>
										 	</td>	
									  </tr>
								</table>
										
									</div>
								
					        </div>	
					        
					</div>
					<center><input type="button" value="<digi:trn>Generate Dashboard</digi:trn>" onclick="launchDashboard()" class="buttonx" style="margin-right:10px; margin-top:10px;"></center>
									
            </div>

</body>

</html>
<script type="text/javascript">
	changeTab(0);
	callbackGetGraphs(document.getElementsByName("dsbd")[0].click());
</script>

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
<html:hidden property="filter.agencyType" styleId="agencyType" />
<html:hidden property="filter.showProjectsRanking" styleId="showProjectsRanking" />
<html:hidden property="filter.showOrganizationsRanking" styleId="showOrganizationsRanking" />
<html:hidden property="filter.showSectorsRanking" styleId="showSectorsRanking" />
<html:hidden property="filter.showRegionsRanking" styleId="showRegionsRanking" />
<html:hidden property="filter.showNPORanking" styleId="showNPORanking" />
<html:hidden property="filter.showProgramsRanking" styleId="showProgramsRanking" />

</digi:form>


