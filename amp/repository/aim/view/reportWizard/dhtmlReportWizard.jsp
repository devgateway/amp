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
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
   
<%@page import="org.dgfoundation.amp.ar.ArConstants"%>

<!-- CSS -->
<%-- <link href='TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>
<link href='TEMPLATE/ampTemplate/css_2/tabs.css' rel='stylesheet' type='text/css'> --%>
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">

<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css"> 
<digi:ref href="css_2/report_html2_view.css" type="text/css" rel="stylesheet" /> 

<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 

<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/ScrollEvent.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportPreviewEngine.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>	
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/reportWizard/searchColumn.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" ></script>

<!-- leave this to make the trees work. -->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dragDropTree.js"/>"></script>

<!-- MORE CSS -->
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">

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
<c:set var="settingsPanelName">
	<digi:trn key="rep:filter:filters">Settings</digi:trn>
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
<c:set var="january"><digi:trn>January</digi:trn></c:set>
<c:set var="february"><digi:trn>February</digi:trn></c:set>
<c:set var="march"><digi:trn>March</digi:trn></c:set>
<c:set var="april"><digi:trn>April</digi:trn></c:set>
<c:set var="may"><digi:trn>May</digi:trn></c:set>
<c:set var="june"><digi:trn>June</digi:trn></c:set>
<c:set var="july"><digi:trn>July</digi:trn></c:set>
<c:set var="august"><digi:trn>August</digi:trn></c:set>
<c:set var="september"><digi:trn>September</digi:trn></c:set>
<c:set var="october"><digi:trn>October</digi:trn></c:set>
<c:set var="november"><digi:trn>November</digi:trn></c:set>
<c:set var="december"><digi:trn>December</digi:trn></c:set>
<c:set var="reportUsesFilters">false</c:set>
<c:if test="${myForm.useFilters!=null && myForm.useFilters}">
	<c:set var="reportUsesFilters">true</c:set>
</c:if> 

<c:if test="${myForm.desktopTab}">
	<c:set var="pageTitle">
		<digi:trn key="aim:reportwizard:tabgenerator">Tab Generator</digi:trn>
	</c:set>
	<c:set var="detailsStepName">
		<digi:trn key="rep:wizard:dhtmlTab:tabDetails">Tab Details</digi:trn>
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
		<digi:trn key="rep:wizard:dhtmlTab:reportDetails">Report Details</digi:trn>
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
		
		if ( "true" == "${myForm.budgetExporter}" )	
			NormalReportManager.prototype.maxHierarchies	= 5;
	function initializeDragAndDrop() {
		var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);
		//alert( YAHOO.util.Dom.getDocumentHeight() );
		//alert( document.body.clientHeight );
		var rd				= document.getElementsByName("reportDescription")[0];
		rd.style.height		= (rd.parentNode.offsetHeight - 40) + "px";
		//validation messages
	    var equalSymbolMsg = '<digi:trn key="rep:format:equalsSymbol">Decimal Symbol and group symbol must be diferents</digi:trn>';
		var badSymbolEmptyMsg ='<digi:trn key="rep:format:badSymbolEmpty">Symbols can not be a empty, you can use the space character</digi:trn>';
		var badSymbolNumberMsg = '<digi:trn key="rep:format:badSymbolNumber">Symbols can not be a number</digi:trn>';
		var badGorupSize = '<digi:trn key="rep:format:badGorupSize">The value should be greater than zero</digi:trn>';
		var badYearRange = '<digi:trn key="rep:format:badYearRange">Year Range To should be greater than From</digi:trn>';

		var filterPanelNameText = '<c:out value="${filterPanelName}" escapeXml="true"/>' + ': ' + '<c:out value="${aimReportWizardForm.reportTitle}" escapeXml="true"/>';
		
		repManagerParams	= {
									desktopTab: ${myForm.desktopTab},
									onePager: ${myForm.onePager},
									reportUsesFilters: ${reportUsesFilters},
									loadingDataMessage: "${loadingDataMessage}",
									filterProblemsMessage: "${filterProblemsMessage}",
									failureMessage: "${failureMessage}",
									savingDataMessage: "${savingDataMessage}",
									filterPanelName: filterPanelNameText, 
                                    settingsPanelName: "${settingsPanelName}",
									cannotSaveFiltersMessage: "${cannotSaveFiltersMessage}",
									savingRepTabMessage: "${savingMessage}",
									previewFundingTrn: "${previewFundingNameTrn}",
									previewTotalCostTrn: "${previewTotalCostTrn}",
									previewReportTotalsTrn: "${previewReportTotalsTrn}",
									previewUnselectedMeasureTrn: "${previewUnselectedMeasureTrn}",
									monthNames:["${january}","${february}","${march}","${april}","${may}","${june}","${july}","${august}","${september}","${october}","${november}","${december}"],
									validationMsgs:[equalSymbolMsg, badSymbolEmptyMsg, badSymbolNumberMsg, badGorupSize, badYearRange]
								};
		
		YAHOO.amp.reportwizard.tabView 		= new YAHOO.widget.TabView('wizard_container');
		YAHOO.amp.reportwizard.tabView.addListener("contentReady", continueInitialization, repManagerParams);
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
	<bean:define id="member" name="currentMember" scope="session" toScope="request" />
	
	<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv"></div>
	
	<div id="wizard_container" class="yui-navset-right" style="padding-right: 0em;" >
		<table width="1000" border="0" cellspacing="0" cellpadding="0" >
			<tr>
				<td width="768">
					<div class="step_head_lng">
						<div id="rgTitle" class="step_head_cont">${pageTitle}: ${aimReportWizardForm.reportTitle}</div>
					</div>
				</td>
				<td width="232">&nbsp;</td>
			</tr>
		  	<tr valign="top">
				<td class="main_side">
					<div class="yui-content">
						<jsp:include page="dhtmlReportWizard_step1.jsp"></jsp:include>
						<jsp:include page="dhtmlReportWizard_step2.jsp"></jsp:include>
						<jsp:include page="dhtmlReportWizard_step3.jsp"></jsp:include>
						<jsp:include page="dhtmlReportWizard_step4.jsp"></jsp:include>
					</div>
				</td>
				<td style="padding-top:35px;">
					<ul class="yui-nav" style="width: 70%; position: static;">
						<div class="tab selected" id="rtab">
							<div class="tab_cont">
								<span class="step_num">1</span>
								<a href="#type_step_div" ><c:out value="${detailsStepName}"/></a>
							</div>
						</div>
						<div class="tab" id="rtab">
							<div class="tab_cont">
								<span class="step_num">2</span>
								<a href="#columns_step_div" ><digi:trn>Columns</digi:trn></a>
							</div>
						</div>
						<div class="tab" id="rtab">
							<div class="tab_cont">
								<span class="step_num">3</span>
								<a href="#hierarchies_step_div" ><digi:trn>Hierarchies</digi:trn></a>
							</div>
						</div>
						<div class="tab" id="rtab">
							<div class="tab_cont">
								<span class="step_num">4</span>
								<a href="#measures_step_div" ><digi:trn>Measures</digi:trn></a>
							</div>
						</div>
					</ul>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="titlePanel" class="invisible-item-hidden">
						<div class="hd" style="font-size: 8pt">
							${plsEnterTitle}
						</div>
						<div class="bd" id="titlePanelBody">
							<html:text onkeyup="repManager.checkSteps();" onkeypress="return saveReportEngine.checkEnter(event);" property="reportTitle" styleClass="inp-text" 
								style="border: 1px solid gray; width: 100%; font-size: 8pt; font-weight: bolder;" />
							<feature:display name="Reports classification"  module="Report Generator">
                                <c:if test="${aimReportWizardForm.desktopTab ==false }">
                                    <br><br>
                                    <c:set var="translation">
                                        <digi:trn>Please select a category from below</digi:trn>
                                    </c:set>
                                    <category:showoptions  firstLine="${translation}" name="aimReportWizardForm" property="reportCategory"  keyName="<%= CategoryConstants.REPORT_CATEGORY_KEY %>" styleClass="dropdwn_sm" styleId="repCat"/>
                                </c:if>
                            </feature:display>
						</div>
						<div class="ft" align="right">
							<button id="last_save_button" type="button" class="buttonx_dis" onclick="saveReportEngine.saveReport();" disabled="disabled">
								${saveBtn}
							</button>
							&nbsp;&nbsp;&nbsp;
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div id="myHiddenDiv" style="display: none;"></div>
	<c:if test="${!myForm.desktopTab}">
		<jsp:include page="previewPanel.jsp" />
	</c:if>
</digi:form>
<!-- MAIN CONTENT PART END -->
<div id="customFormat" style="display:none; height: 372px;width: auto;">
	<jsp:include page="/repository/aim/view/ar/customFormatPicker.jsp" />
</div>

<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>
