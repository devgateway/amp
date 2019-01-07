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

<%-- This JSP is used for rendering the ReportWizard, both one-pager and 4-pager --%>

<!-- CSS -->
<%--<link href='TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>
<link href='TEMPLATE/ampTemplate/css_2/tabs.css' rel='stylesheet' type='text/css'> --%>
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">
<link href='/TEMPLATE/ampTemplate/nireports/nireports_view.css' rel='stylesheet' type='text/css'>
<link href='/TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>
<link href='/TEMPLATE/ampTemplate/css_2/saiku.table.preview.css' rel='stylesheet' type='text/css'>

<!-- Individual YUI CSS files --> 
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/skins/sam/tabview.css" />
<digi:ref href="css_2/report_html2_view.css" type="text/css" rel="stylesheet" /> 
<!-- Individual YUI JS files -->

<link rel="stylesheet" href="/TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter.css">
<link href='tabs/fonts/open-sans.css' rel='stylesheet' type='text/css'>

<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/yahoo-dom-event/yahoo-dom-event.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/button/button-min.js"></script>
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script>   
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/json/json-min.js"></script> 
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" ></script>
<script type="text/javascript" src="/repository/aim/view/scripts/reportWizard/reportManager.js?ver=20150208"></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>?ver=2" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/ScrollEvent.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportPreviewEngine.js'/>" ></script>
<script type="text/javascript" src="/repository/aim/view/multilingual/multilingual_scripts.js"></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/reportWizard/searchColumn.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" ></script>

<!-- leave this to make the trees work. -->
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script type="text/javascript" src="<digi:file src="module/aim/scripts/separateFiles/dhtmlSuite-dragDropTree.js"/>"></script>

<!-- MORE CSS -->
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
<link re="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/amp_extra_sam.css" />

<script type="text/javascript" src="/TEMPLATE/ampTemplate/saikuui_reports/js/backbone/underscore.js"></script>
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/backbone/backbone.js" type="text/javascript"></script>
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/jquery/jquery.min.js" type="text/javascript"></script>
<script src="/TEMPLATE/ampTemplate/saikuui_reports/js/jquery/jquery-ui.min.js" type="text/javascript"></script>
<script type="text/javascript"
		src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter.js"/>"></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/filterWidgetLoader.js'/>"></script>

<!-- DEFINITIONS AND VARIABLES -->
<digi:instance property="aimReportWizardForm" />
<bean:define name="aimReportWizardForm" id="myForm" type="org.digijava.module.aim.form.reportwizard.ReportWizardForm" toScope="request"/>

<%@ include file="reportWizardMessages.jspf"  %>

<c:set var="reportUsesFilters">false</c:set>
<c:if test="${myForm.useFilters!=null && myForm.useFilters}">
	<c:set var="reportUsesFilters">true</c:set>
</c:if> 


<c:set var="disableFundingType">false</c:set>
<c:if test="${!empty aimReportWizardForm.reportId}">
	<c:set var="disableFundingType">true</c:set>
</c:if>
<c:set var="pledges_type_const"><%=ArConstants.PLEDGES_TYPE%></c:set>
<c:set var="pledgedReport">false</c:set>
<c:if test="${param.type==null || param.type!=pledges_type_const}">
	<c:set var="pledgedReport">true</c:set>
</c:if>

<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp.reportwizard");
	YAHOO.amp.reportwizard.numOfSteps	= 4;
		
	YAHOO.amp.reportwizard.tabLabels	= new Array("reportdetails_tab_label", "columns_tab_label","hierarchies_tab_label", "measures_tab_label");
	selectedCols						= new Array();
	selectedHiers						= new Array();
	selectedMeas						= new Array();
	var defaultLanguage = '${aimReportWizardForm.defaultLanguage}';
	var emptyCurrentLanguage = '<digi:trn jsFriendly="true" key="rep:format:currentLanguageEmpty">Please enter a report name in current language</digi:trn>';

	if ( "true" == "${myForm.budgetExporter}" )	
		NormalReportManager.prototype.maxHierarchies = 5;
	
	function initializeDragAndDrop() {
		//var height			= Math.round(YAHOO.util.Dom.getDocumentHeight() / 2.3);
		//alert( YAHOO.util.Dom.getDocumentHeight() );
		//alert( document.body.clientHeight );
		var rd				= document.getElementsByName("reportDescription")[0];
		
		<c:if test="${not param.isOnePager}">
			rd.style.height		= (rd.parentNode.offsetHeight - 40) + "px";
		</c:if>
		
		//validation messages
	    var equalSymbolMsg = '<digi:trn jsFriendly="true" key="rep:format:equalsSymbol">Decimal Symbol and group symbol must be diferents</digi:trn>';
		var badSymbolEmptyMsg ='<digi:trn jsFriendly="true" key="rep:format:badSymbolEmpty">Symbols can not be a empty, you can use the space character</digi:trn>';
		var badSymbolNumberMsg = '<digi:trn jsFriendly="true" key="rep:format:badSymbolNumber">Symbols can not be a number</digi:trn>';
		var badGorupSize = '<digi:trn jsFriendly="true" key="rep:format:badGorupSize">The value should be greater than zero</digi:trn>';
		var badYearRange = '<digi:trn jsFriendly="true" key="rep:format:badYearRange">Year Range To should be greater than From</digi:trn>';

		var filterPanelNameText = '${filterPanelName}' + ': ' + '${fn:escapeXml(aimReportWizardForm.reportTitle)}';
		
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
									pledgedReport: "${pledgedReport}",
									monthNames:["${january}","${february}","${march}","${april}","${may}","${june}","${july}","${august}","${september}","${october}","${november}","${december}"],
									validationMsgs:[equalSymbolMsg, badSymbolEmptyMsg, badSymbolNumberMsg, badGorupSize, badYearRange]
								};

		<c:if test="${param.isOnePager}">
			continueInitialization(null, repManagerParams );
		</c:if>
		<c:if test="${not param.isOnePager}">		
			YAHOO.amp.reportwizard.tabView 		= new YAHOO.widget.TabView('wizard_container');
			YAHOO.amp.reportwizard.tabView.addListener("contentReady", continueInitialization, repManagerParams);
		</c:if>
	}

	
	YAHOO.util.Event.addListener(window, "load", initializeDragAndDrop);

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

<!-- BREADCRUMB START -->

<div class="breadcrump">
	<div class="centering">
		<div class="breadcrump_cont" style="visibility: hidden">
			<span class="sec_name">${pageTitle}</span>
		</div>
	</div>
</div>
<!-- BREADCRUMB END --> 

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
			
		//If project title is enable in GS add it to the donor array
		var ptitle='${myForm.projecttitle}';
		YAHOO.amp.reportwizard.fundingGroups["donor"].push(ptitle);
	</script>
	
	<html:hidden name="aimReportWizardForm" property="projecttitle"/>
	<html:hidden name="aimReportWizardForm" property="desktopTab"/>

	<logic:present name="currentMember" scope="session">
		<bean:define id="member" name="currentMember" scope="session" toScope="request" />
	</logic:present>
	<div id="savingReportDiv" style="color: red; text-align: center;<c:if test='${not param.isOnePager}'>visibility: hidden</c:if> ">
	</div>
	
	<div id="wizard_container" class="content-direction yui-navset-right" style="padding-right: 0em;" >
		<table width="1000" border="0" cellspacing="0" cellpadding="0" >
			<tr>
				<c:if test="${param.isOnePager}">
					<td width="1000">
						<div class="step_head_lng">
							<div id="rgTitle" class="step_head_cont">${pageTitle}</div>
						</div>
					</td>
				</c:if>
				<c:if test="${not param.isOnePager}">
					<td width="768">
						<div class="step_head_lng">
							<div id="rgTitle" class="step_head_cont">${pageTitle}:
								<div class="step_head_cont_name">${aimReportWizardForm.reportTitle}</div>
							</div>
						</div>
					</td>
					<td width="232">&nbsp;</td>
				</c:if>
			</tr>
		  	<tr valign="top">
				<td class="main_side">
					<div class="yui-content">
						<c:if test="${param.isOnePager}">
							<c:set var="topBottomPadding" scope="request">padding-bottom: 0px; padding-top:0px;</c:set>
							<c:set var="hierarchiesVisibility" scope="request">display: none;</c:set>
						</c:if>
						<jsp:include page="dhtmlReportWizard_step1.jsp"></jsp:include>
						<jsp:include page="dhtmlReportWizard_step2.jsp"></jsp:include>
						<c:if test="${param.isOnePager}">
							<fieldset id="hierarchiesInfoFieldset" style="margin-left: 10px;margin-right: 10px;background-color: #F6F6F6;">
								<legend><digi:trn>Hierarchies</digi:trn></legend>
								<div><digi:trn>Hierarchies will appear here when available</digi:trn>....</div>
							</fieldset>
						</c:if>
						<jsp:include page="dhtmlReportWizard_step3.jsp"></jsp:include>
						<jsp:include page="dhtmlReportWizard_step4.jsp"></jsp:include>
					</div>
				</td>
				<c:if test="${not param.isOnePager}">
					<td style="padding-top:35px;">
						<ul class="yui-nav" style="width: 70%; position: static;">
							<div class="tab selected" id="rtab">
								<div class="tab_cont">
									<span class="step_num">1</span>
									<a href="#type_step_div" ><c:out escapeXml="false" value="${detailsStepName}"/></a>
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
				</c:if>
			</tr>
			<tr><td>
				<div id="titlePanel" class="invisible-item-hidden yui-skin-sam">
					<div class="hd" style="font-size: 8pt">
						${plsEnterTitle}
					</div>
					<div class="bd content-direction" id="titlePanelBody">
						<jsp:include page="/repository/aim/view/multilingual/multilingualFieldEntry.jsp">
							<jsp:param name="attr_name" value="multilingual_report_title" />
						</jsp:include>
						<feature:display name="Reports classification"  module="Report Generator">
							<c:if test="${aimReportWizardForm.desktopTab ==false }">
								<br /><br />
								<c:set var="translation">
									<digi:trn>Please select a category from below</digi:trn>
								</c:set>
								<category:showoptions  firstLine="${translation}" name="aimReportWizardForm" property="reportCategory"  keyName="<%= CategoryConstants.REPORT_CATEGORY_KEY %>" styleClass="dropdwn_sm" styleId="repCat"/>
							</c:if>
						</feature:display>
					</div>
					<div class="ft wizard-panel-buttons">
						<button id="last_save_button" type="button" class="buttonx yui-button" onclick="saveReportEngine.saveReport();">
							${saveBtn}
						</button>
						<feature:display name="Save and Open Button"  module="Report Generator">
							&nbsp;&nbsp;&nbsp;
							<c:if test="${not empty saveAndOpenBtn}">
								<button id="last_save_and_open_button" type="button" class="buttonx" onclick="saveReportEngine.saveAndOpenReport();">
									${saveAndOpenBtn}
								</button>
							</c:if>
						</feature:display>
					</div>
				</div>
			</td></tr>
		</table>
	</div>
	<div id="myHiddenDiv" style="display: none;"></div>
	<c:if test="${!myForm.desktopTab}">
		<jsp:include page="previewPanel.jsp" />
	</c:if>
</digi:form>
<!-- MAIN CONTENT PART END -->
<div id="customFormat" class="content-direction" style="display:none; height: 372px;width: auto;">
	<jsp:include page="/repository/aim/view/ar/customFormatPicker.jsp" />
</div>

<div id="filter-popup" style="font-size: 14px !important;"></div>

<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>