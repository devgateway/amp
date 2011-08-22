<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
   
<%@page import="org.dgfoundation.amp.ar.ArConstants"%>

<!-- CSS -->
<link href='TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>
<link href='TEMPLATE/ampTemplate/css_2/tabs.css' rel='stylesheet' type='text/css'>

<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css"> 
<digi:ref href="css_2/report_html2_view.css" type="text/css" rel="stylesheet" /> 

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script>  

<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/ScrollEvent.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportPreviewEngine.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>		
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" ></script>

<!-- leave this to make the trees work. -->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dragDropTree.js"/>"></script>

<!-- MORE CSS -->
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">

<!-- DEFINITIONS AND VARIABLES -->
<digi:instance property="aimReportWizardForm" />
<bean:define name="aimReportWizardForm" id="myForm" type="org.digijava.module.aim.form.reportwizard.ReportWizardForm" toScope="request"/>
<c:set var="failureMessage">
	<digi:trn key="aim:reportwizard:connectionProblems">Apparently there are some connection problems. Please try again in a few moments.</digi:trn>
</c:set>
<c:set var="filterProblemsMessage">
	<digi:trn key="aim:reportwizard:filterProblems">Apparently there are some problems displaying filters pop-up. Please try again.</digi:trn>
</c:set>
<c:set var="loadingDataMessage">
	<digi:trn key="aim:reportwizard:loadingData">Loading data. Please wait.</digi:trn>
</c:set>
<c:set var="savingDataMessage">
	<digi:trn key="aim:reportwizard:savingData">Saving data. Please wait.</digi:trn>
</c:set>
<c:set var="filterPanelName">
	<digi:trn key="rep:filter:filters">Filters</digi:trn>
</c:set>
<c:set var="cannotSaveFiltersMessage">
	<digi:trn key="aim:reportwizard:cannotSaveFilters">There was a problem saving the filters. Please try again.</digi:trn>
</c:set>
<c:set var="previewFundingTrn">
	<digi:trn>Funding</digi:trn>
</c:set>
<c:set var="previewTotalCostTrn">
	<digi:trn>Total Cost</digi:trn>
</c:set>
<c:set var="previewReportTotalsTrn">
	<digi:trn>Report Totals</digi:trn>
</c:set>
<c:set var="previewUnselectedMeasureTrn">
	<digi:trn>UNSELECTED MEASURE</digi:trn>
</c:set>
<c:set var="january">January</c:set>
<c:set var="february">February</c:set>
<c:set var="march">March</c:set>
<c:set var="april">April</c:set>
<c:set var="may">May</c:set>
<c:set var="june">June</c:set>
<c:set var="july">July</c:set>
<c:set var="august">August</c:set>
<c:set var="september">September</c:set>
<c:set var="october">October</c:set>
<c:set var="november">November</c:set>
<c:set var="december">December</c:set>

<c:set var="reportUsesFilters">false</c:set>
<c:if test="${myForm.useFilters!=null && myForm.useFilters}">
	<c:set var="reportUsesFilters">true</c:set>
</c:if> 


<c:if test="${myForm.desktopTab}">
	<c:set var="pageTitle">
		<digi:trn key="aim:reportwizard:tabgenerator">Tab Generator</digi:trn>
	</c:set>
	<c:set var="detailsStepName">
		<digi:trn key="rep:wizard:dhtmlTab:tabDetails">1. Tab Details</digi:trn>
	</c:set>
	<c:set var="descriptionName" scope="request">
		<digi:trn key="aim:reportBuilder:TabDescription">Tab Description</digi:trn>
	</c:set>
	<c:set var="savingMessage">
		<digi:trn key="aim:reportwizard:savingTab">Saving tab</digi:trn>
	</c:set>
	<c:set var="saveBtn">
		<digi:trn key="btn:saveTab">Save Tab</digi:trn>
	</c:set>
	<c:set var="plsEnterTitle">
		<digi:trn key="rep:wizard:enterTitleForTab">Please enter a title for this tab: </digi:trn>
	</c:set>
	<c:set var="donorFunding" scope="request">
		<digi:trn key="aim:donorTab">Donor Tab (Donor Funding)</digi:trn>
	</c:set>
	<c:set var="regionalFunding" scope="request">
		<digi:trn key="aim:regionalTab">Regional Tab (Regional Funding)</digi:trn>
	</c:set>
	<c:set var="componentFunding" scope="request">
		<digi:trn key="aim:componentTab">Component Tab (Component Funding)</digi:trn>
	</c:set>
	<c:set var="activityContributions" scope="request">
		<digi:trn key="aim:contributionTab">Contribution Tab (Activity Contributions)</digi:trn>
	</c:set>
	<c:set var="summary" scope="request">
		<digi:trn key="aim:summaryTab">Summary Tab</digi:trn>
	</c:set>
</c:if>
<c:if test="${!myForm.desktopTab}">
	<c:set var="pageTitle">
		<digi:trn key="aim:reportwizard:reportgenerator">Report Generator</digi:trn>
	</c:set>
	<c:set var="detailsStepName">
		<digi:trn key="rep:wizard:dhtmlTab:reportDetails">1. Report Details</digi:trn>
	</c:set>
	<c:set var="descriptionName" scope="request">
		<digi:trn key="aim:reportBuilder:ReportDescription">Report Description</digi:trn>
	</c:set>
	<c:set var="savingMessage">
		<digi:trn key="aim:reportwizard:savingReport">Saving report</digi:trn>
	</c:set>
	<c:set var="saveBtn">
		<digi:trn key="btn:saveReport">Save Report</digi:trn>
	</c:set>
	<c:set var="plsEnterTitle">
		<digi:trn key="rep:wizard:enterTitleForReport">Please enter a title for this report: </digi:trn>
	</c:set>
	<c:set var="donorFunding" scope="request">
		<digi:trn key="aim:donorReport">Donor Report (Donor Funding)</digi:trn>
	</c:set>
	<c:set var="regionalFunding" scope="request">
		<digi:trn key="aim:regionalReport">Regional Report (Regional Funding)</digi:trn>
	</c:set>
	<c:set var="componentFunding" scope="request">
		<digi:trn key="aim:componentReport">Component Report (Component Funding)</digi:trn>
	</c:set>
	<c:set var="activityContributions" scope="request">
		<digi:trn key="aim:contributionReport">Contribution Report (Activity Contributions)</digi:trn>
	</c:set>
	<c:set var="summary" scope="request">
		<digi:trn key="aim:summaryReport">Summary Report</digi:trn>
	</c:set>
	<c:set var="PledgesFunding" scope="request">
		<digi:trn key="aim:pledgesfunding">Pledges Report</digi:trn>
	</c:set>
</c:if>
<c:set var="disableFundingType">false</c:set>
<c:if test="${!empty aimReportWizardForm.reportId}">
	<c:set var="disableFundingType">true</c:set>
</c:if>

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp.reportwizard");
	YAHOO.amp.reportwizard.numOfSteps	= 4;
		
	YAHOO.amp.reportwizard.tabLabels	= new Array("reportdetails_tab_label", "columns_tab_label","hierarchies_tab_label", "measures_tab_label");
	selectedCols						= new Array();
	selectedHiers						= new Array();
	selectedMeas						= new Array();
	
	function initializeDragAndDrop() {
		var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);
		//alert( YAHOO.util.Dom.getDocumentHeight() );
		//alert( document.body.clientHeight );
		var rd				= document.getElementsByName("reportDescription")[0];
		rd.style.height		= (rd.parentNode.offsetHeight - 40) + "px";
		
		repManagerParams	= { // Global scoped object
				desktopTab: ${myForm.desktopTab},
				onePager: ${myForm.onePager},
				reportUsesFilters: ${reportUsesFilters},
				loadingDataMessage: "${loadingDataMessage}",
				filterProblemsMessage: "${filterProblemsMessage}",
				failureMessage: "${failureMessage}",
				savingDataMessage: "${savingDataMessage}",
				filterPanelName: "${filterPanelName}",
				cannotSaveFiltersMessage: "${cannotSaveFiltersMessage}",
				savingRepTabMessage: "${savingMessage}",
				previewFundingTrn: "${previewFundingNameTrn}",
				previewTotalCostTrn: "${previewTotalCostTrn}",
				previewReportTotalsTrn: "${previewReportTotalsTrn}",
				previewUnselectedMeasureTrn: "${previewUnselectedMeasureTrn}",
				monthNames:["${january}","${february}","${march}","${april}","${may}","${june}","${july}","${august}","${september}","${october}","${november}","${december}"]
				
			};
		
		//YAHOO.amp.reportwizard.tabView 		= new YAHOO.widget.TabView('wizard_container');
		//YAHOO.amp.reportwizard.tabView.addListener("contentReady", continueInitialization);
		continueInitialization(null, repManagerParams );
	}

	
	YAHOO.util.Event.addListener(window, "load", initializeDragAndDrop) ;
</script>

<style type="text/css">
/*margin and padding on body element
  can introduce errors in determining
  element position and are not recommended;
  we turn them off as a foundation for YUI
  CSS treatments. */
body {
	margin:0;
	padding:0;
}
</style>

<!-- BREADCRUMP START -->

<div class="breadcrump">
	<div class="centering">
		<div class="breadcrump_cont" style="visibility: hidden">
			<span class="sec_name">${pageTitle}</span>
		</div>
	</div>
</div>
<!-- BREADCRUMP END --> 

<!-- BREADCRUMP START -->
<!--<div class="breadcrump">-->
<!--	<div class="centering">-->
<!--		<div class="breadcrump_cont">-->
<!--			<span class="sec_name">Report details</span>-->
<!--			<span class="breadcrump_sep">|</span><a href=# class="l_sm">Report Generator</a>-->
<!--			<span class="breadcrump_sep"><b>»</b></span><span class="bread_sel">Reprort details</span>-->
<!--		</div>-->
<!--	</div>-->
<!--</div>-->
<!-- BREADCRUMP END -->

<!-- MAIN CONTENT PART START -->
<digi:form action="/reportWizard.do" method="post">
	<script type="text/javascript">
		<c:forEach items="${aimReportWizardForm.selectedColumns}" var="dbId">
			selectedCols.push('${dbId}');
		</c:forEach>	
		<c:forEach items="${aimReportWizardForm.selectedHierarchies}" var="dbId">
			selectedHiers.push('${dbId}');
		</c:forEach>	
		<c:forEach items="${aimReportWizardForm.selectedMeasures}" var="dbId">
			selectedMeas.push('${dbId}');
		</c:forEach>
			
		//If ptoject title is enable in GS add it to the donor array
		var ptitle='${myForm.projecttitle}';
		YAHOO.amp.reportwizard.fundingGroups["donor"].push(ptitle);
	</script>
	
	<html:hidden name="aimReportWizardForm" property="projecttitle"/>
	<html:hidden name="aimReportWizardForm" property="desktopTab"/>
	<bean:define id="member" name="currentMember" scope="session" />
	
	<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv"></div>
	
	<div id="wizard_container" class="yui-navset-right" style="padding-right: 0em;" >
		<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td width="1000">
					<div class="step_head">
						<div id="rgTitle" class="step_head_cont">${pageTitle}</div>
					</div>
				</td>
			</tr>
		  	<tr valign="top">
				<td class="main_side">
					<div class="yui-content">
						<c:set var="topBottomPadding" scope="request">padding-bottom: 0px; padding-top:0px;</c:set>
						<jsp:include page="dhtmlReportWizard_step1.jsp"></jsp:include>
						<jsp:include page="dhtmlReportWizard_step2.jsp"></jsp:include>
						<c:set var="hierarchiesVisibility" scope="request">display: none;</c:set>
						<fieldset id="hierarchiesInfoFieldset" style="margin-left: 10px;margin-right: 10px;background-color: #F6F6F6;">
							<legend><digi:trn>Hiearchies</digi:trn></legend>
							<div>
								<digi:trn>Hierarchies will appear here when available</digi:trn>....
							</div>	
						</fieldset>
						<jsp:include page="dhtmlReportWizard_step3.jsp"></jsp:include>
						<jsp:include page="dhtmlReportWizard_step4.jsp"></jsp:include>
					</div>
				</td>
			</tr>
			<tr>
				<div id="titlePanel" style="display: none">
					<div class="hd" style="font-size: 8pt;">
						${plsEnterTitle}
					</div>
					<div class="bd" id="titlePanelBody">
						<html:text onkeyup="repManager.checkSteps()" onkeypress="return saveReportEngine.checkEnter(event);" property="reportTitle" styleClass="inp-text" 
							style="border: 1px solid gray; width: 100%; font-size: 8pt; font-weight: bolder;" />
					</div>
					<div class="ft" align="right">
						<button id="last_save_button" type="button" class="buton repbuton" style="color: lightgray" onclick="saveReportEngine.saveReport();" disabled="disabled">
							${saveBtn}
						</button>
						&nbsp;&nbsp;&nbsp;
					</div>
				</div>
			</tr>
		</table>
	</div>
	<c:if test="${!myForm.desktopTab}">
		<jsp:include page="previewPanel.jsp" />
	</c:if>
</digi:form>
<!-- MAIN CONTENT PART END -->
<div id="customFormat" style="display:none; height: 372px;width: auto;">
	<jsp:include page="/repository/aim/view/ar/customFormatPicker.jsp" />
</div>
<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>

