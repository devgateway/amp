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
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 

<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" ></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" ></script>
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
<c:if test="${myForm.desktopTab}">
	<c:set var="pageTitle">
		<digi:trn key="aim:reportwizard:tabgenerator">Tab Generator</digi:trn>
	</c:set>
	<c:set var="detailsStepName">
		<digi:trn key="rep:wizard:dhtmlTab:tabDetails">1. Tab Details</digi:trn>
	</c:set>
	<c:set var="descriptionName">
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
	<c:set var="donorFunding">
		<digi:trn key="aim:donorTab">Donor Tab (Donor Funding)</digi:trn>
	</c:set>
	<c:set var="regionalFunding">
		<digi:trn key="aim:regionalTab">Regional Tab (Regional Funding)</digi:trn>
	</c:set>
	<c:set var="componentFunding">
		<digi:trn key="aim:componentTab">Component Tab (Component Funding)</digi:trn>
	</c:set>
	<c:set var="activityContributions">
		<digi:trn key="aim:contributionTab">Contribution Tab (Activity Contributions)</digi:trn>
	</c:set>
	<c:set var="summary">
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
	<c:set var="descriptionName">
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
	<c:set var="donorFunding">
		<digi:trn key="aim:donorReport">Donor Report (Donor Funding)</digi:trn>
	</c:set>
	<c:set var="regionalFunding">
		<digi:trn key="aim:regionalReport">Regional Report (Regional Funding)</digi:trn>
	</c:set>
	<c:set var="componentFunding">
		<digi:trn key="aim:componentReport">Component Report (Component Funding)</digi:trn>
	</c:set>
	<c:set var="activityContributions">
		<digi:trn key="aim:contributionReport">Contribution Report (Activity Contributions)</digi:trn>
	</c:set>
	<c:set var="summary">
		<digi:trn key="aim:summaryReport">Summary Report</digi:trn>
	</c:set>
	<c:set var="PledgesFunding">
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
		
		YAHOO.amp.reportwizard.tabView 		= new YAHOO.widget.TabView('wizard_container');
		YAHOO.amp.reportwizard.tabView.addListener("contentReady", continueInitialization);
	}

	function continueInitialization(){
		aimReportWizardForm.reportDescriptionClone.value	= unescape(aimReportWizardForm.reportDescription.value);
		treeObj = new DHTMLSuite.JSDragDropTree();
		treeObj.setTreeId('dhtmlgoodies_tree');
		treeObj.init();
		//treeObj.minusImage = 'DHTMLSuite_plus.gif';
		treeObj.showHideNode(false,'dhtmlgoodies_tree');
// 		if (YAHOO.widget.Logger) {
// 			var reader = new YAHOO.widget.LogReader( "logDiv", 
// 						{ newestOnTop: true, height: "400px" } );
// 		}
		if ( ${myForm.desktopTab} )
			repManager		= new TabReportManager();
		else
			repManager		= new NormalReportManager();
		
		var saveBtns		= document.getElementsByName("save");	
		for (var i=0; i<saveBtns.length; i++  ) {
			repManager.addStyleToButton(saveBtns[i]);
		}
		for (var i=0; i<YAHOO.amp.reportwizard.numOfSteps; i++) {
			repManager.addStyleToButton("step"+ i +"_prev_button");
			repManager.addStyleToButton("step"+ i +"_next_button");
			repManager.addStyleToButton("step"+ i +"_add_filters_button");
			repManager.addStyleToButton("step"+ i +"_cancel");
		}
		
		columnsDragAndDropObject	= new ColumnsDragAndDropObject('source_col_div');
		columnsDragAndDropObject.createDragAndDropItems();
		new YAHOO.util.DDTarget('source_measures_ul');
		new YAHOO.util.DDTarget('dest_measures_ul');
		new YAHOO.util.DDTarget('source_hierarchies_ul');
		new YAHOO.util.DDTarget('dest_hierarchies_ul');
		measuresDragAndDropObject	= new MyDragAndDropObject('source_measures_ul');
		measuresDragAndDropObject.createDragAndDropItems();
		
		//createDragAndDropItems('source_ul');
		//createDragAndDropItems('dest_col_ul');
		//new YAHOO.util.DDTarget('dest_li_1');
		//new YAHOO.util.DD('logDiv');
		for (var i=1; i<YAHOO.amp.reportwizard.numOfSteps; i++) {
			tab		= YAHOO.amp.reportwizard.tabView.getTab(i);
			tab.set("disabled", true);
		}
		tab2	= YAHOO.amp.reportwizard.tabView.getTab(2);
		tab2.addListener("beforeActiveChange", generateHierarchies);
		
		ColumnsDragAndDropObject.selectObjsByDbId ("source_col_div", "dest_col_ul", selectedCols);
		generateHierarchies();
		MyDragAndDropObject.selectObjsByDbId ("source_hierarchies_ul", "dest_hierarchies_ul", selectedHiers);
		MyDragAndDropObject.selectObjsByDbId ("source_measures_ul", "dest_measures_ul", selectedMeas);

		repFilters					= new Filters("${filterPanelName}", "${failureMessage}", "${filterProblemsMessage}", 
											"${loadingDataMessage}", "${savingDataMessage}", "${cannotSaveFiltersMessage}");
		
		saveReportEngine			= new SaveReportEngine("${savingMessage}","${failureMessage}");
											
		var dg			= document.getElementById("DHTMLSuite_treeNode1");
		var cn			= dg.childNodes;
		
		for (var i=0; i<cn.length; i++) {
			if ( cn[i].nodeName.toLowerCase()=="input" || cn[i].nodeName.toLowerCase()=="img" ||
				cn[i].nodeName.toLowerCase()=="a" )
				cn[i].style.display		= "none";
		}
		repManager.checkSteps();
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
				<td width="768">
					<div class="step_head">
						<div id="rgTitle" class="step_head_cont">${pageTitle}</div>
					</div>
				</td>
				<td width="232">&nbsp;</td>
			</tr>
		  	<tr valign="top">
				<td class="main_side">
					<div class="yui-content">
						<div class="main_side_cont yui-tab-content" id="type_step_div">
							<c:set var="stepNum" value="0" scope="request" />
							<jsp:include page="toolbar.jsp" />
							<br />
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="365" valign="top">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn>Funding Grouping</digi:trn></span></legend>
											<div id="reportGroupDiv" class="inputx">
										<c:set var="pledges_type_const"><%=ArConstants.PLEDGES_TYPE%></c:set>
										<c:choose>
										<c:when test="${param.type==null || param.type!=pledges_type_const}">								
													<feature:display name="Donor Report" module="Report Types">                                      	
				                                    	<html:radio property="reportType" disabled="${disableFundingType}" value="donor"  onclick="repManager.checkSteps()">
				                                           	${donorFunding}
														</html:radio>
														<br />
													</feature:display>
				                                    <feature:display name="Regional Report" module="Report Types">										
				                                    	<html:radio property="reportType" disabled="${disableFundingType}" value="regional"  onclick="repManager.checkSteps()">
				                                        	${regionalFunding}
				                                      	</html:radio>
				                                        <br />
													</feature:display>
				                                    <feature:display name="Component Report" module="Report Types">
				                                       	<html:radio property="reportType" disabled="${disableFundingType}" value="component"  onclick="repManager.checkSteps()">
					                                		${componentFunding}
				                                        </html:radio>
				                                        <br />
				                                   	</feature:display>
				                                    <feature:display name="Contribution Report" module="Report Types">
														<html:radio property="reportType" disabled="${disableFundingType}" value="contribution"  onclick="repManager.checkSteps()">
				                                        	${activityContributions}
				                                      	</html:radio>
				                                        <br />   	
				                                   	</feature:display>
												</c:when>
                           						<c:otherwise>
                                              <c:set var="disablePledgeType">true</c:set>
                                              	<tr>
                                              		<td>
                                                		<html:radio property="reportType" disabled="${disablePledgeType}" value="pledge" onclick="repManager.checkSteps()">
                                                   			${PledgesFunding}
                                                     	</html:radio>
                                                   </td>
                                                </tr>
                                           </c:otherwise>
                                         </c:choose>
                                         </div>
										</fieldset>
									</td>
									<td width="16">&nbsp;</td>
									<td width="365" valign="top">
										<fieldset class="main_side_cont">
											<html:hidden property="reportDescription" />
											<legend><span class="legend_label">${descriptionName}</span></legend>
											<textarea name="reportDescriptionClone"  rows="5" class="inputx" style="width:340px; height:90px;"></textarea>
										</fieldset>
									</td>
								</tr>
								<tr>
									<c:if test="${!myForm.desktopTab}">
										<td valign="top">
									    	<fieldset class="main_side_cont">
												<legend><span class="legend_label"><digi:trn>Totals Grouping</digi:trn></span></legend>
												<div id="totalsGroupingDiv" class="inputx">
													<html:checkbox property="hideActivities" value="true">
														${summary}
													</html:checkbox>
													<br />
													<html:radio property="reportPeriod" value="A">
														<digi:trn key="aim:AnnualReport">Annual Report</digi:trn>
													</html:radio>
													<br />
													<c:if test="${param.type==null || param.type!=pledges_type_const}">
													<html:radio property="reportPeriod" value="Q">
														<digi:trn key="aim:QuarterlyReport">Quarterly Report</digi:trn>
													</html:radio>
													<br />
													<html:radio property="reportPeriod" value="M">
														<digi:trn key="aim:MonthlyReport">Monthly Report</digi:trn>
													</html:radio>
													</c:if>
													<br />
													<html:radio property="reportPeriod" value="N">
														<digi:trn>Totals Only</digi:trn>
													</html:radio>
												</div>											
											</fieldset>									
										</td>
										<td valign="top">&nbsp;</td>
									</c:if>
									<td valign="top">
										<c:set var="spanUseFiltersVisibility">visibility: hidden</c:set>
										<c:set var="spanUseFiltersHeight">height:90px</c:set>
										<c:set var="spanUseFiltersDisabled">disabled</c:set>
										<c:if test="${aimReportWizardForm.useFilters!=null && aimReportWizardForm.useFilters}">
											<c:set var="spanUseFiltersVisibility">visibility: visible</c:set>
											<c:set var="spanUseFiltersDisabled">enabled</c:set>
										</c:if>
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:subtitle:selectedFilters">Selected Filters</digi:trn></span></legend>
											<div id="listFiltersDiv" style="height:85px; overflow-y:auto; overflow-x:hidden; margin-bottom: 5px;" class="inputx">
												<c:choose>
													<c:when test="${aimReportWizardForm.useFilters!=null && aimReportWizardForm.useFilters}">
														<jsp:include page="showSelectedFilters.jsp" />				
													</c:when>
												</c:choose>
											</div>
											<span id="spanUseFilters">
												<html:checkbox property="useFilters" styleId="useFiltersCheckbox" onclick="repManager.decideStrikeFilters()" disabled="${spanUseFiltersDisabled}"/> 
												<digi:trn key="rep:wizard:useAboveFilters">Use above filters</digi:trn>
											</span>
										</fieldset>
									</td>
								</tr>
					  			<c:if test="${ (!myForm.desktopTab) || (member.teamHead==true && member.teamAccessType=='Management')}">
									<tr>
										<td valign="top" colspan="3">
									    	<fieldset class="main_side_cont">
												<legend><span class="legend_label"><digi:trn>Options</digi:trn></span></legend>
												<div id="optionsDiv" class="inputx">
													<feature:display  name="Public View Checkbox" module="Report and Tab Options">
														<c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
															<html:checkbox property="publicReport"/><digi:trn key="aim:makePublic">Make public</digi:trn>
															<br />
				                                    	</c:if>
			                                    	</feature:display>
			                                    	<c:if test="${!myForm.desktopTab}">
			                                    		<html:checkbox property="allowEmptyFundingColumns"/>
					                                    <digi:trn key="rep:wizard:allowEmptyFundingCols">Allow empty funding columns for year, quarter and month</digi:trn>
			                                    		<br />
			                                    	</c:if>
			                                    </div>
											</fieldset>
										</td>
				  					</tr>
								</c:if>
							</table>
							<br />
						</div>
						<div id="columns_step_div" class="main_side_cont yui-hidden">
							<c:set var="stepNum" value="1"  scope="request" />
							<jsp:include page="toolbar.jsp" />
							<br />
							<table style="width: 100%;">
								<tr>
									<td width="48%" align="center">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:availableColumns">Available Columns</digi:trn></span></legend>
											<div id="source_col_div" class="draglist">
												<jsp:include page="setColumns.jsp" />
											</div>
										</fieldset>
									</td>
									<td valign="middle" align="center">
										<button type="button" onClick="ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')" style="border: none;">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif"/>
										</button>
										<br/> <br />
										<button type="button" onClick="ColumnsDragAndDropObject.deselectObjs('dest_col_ul')" style="border: none;">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif"/>
										</button>
									</td>
									<td width="48%" align="center">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:selectedColumns">Selected Columns</digi:trn></span></legend>
											<ul id="dest_col_ul" class="draglist" style="line-height: 20px;">
												
											</ul>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td align="center" valign="top">
										<span id="columnsMust" style="visibility: hidden">
											<font color="red">* 
												<digi:trn key="rep:wizard:hint:mustselectcolumn">
													Must select at least one column
												</digi:trn>
											</font>
										</span>
									</td>
									<td>&nbsp;</td>
									<td align="center" valign="top">
										<span id="columnsLimit" style="visibility: hidden">
											<font color="red">* 
												<digi:trn key="rep:wizard:hint:limit3columns">
													You cannot select more than 3 columns in a desktop tab
												</digi:trn>
											</font>
										</span>
									</td>
								</tr>
							</table>							
						</div>
						<div id="hierarchies_step_div" class="main_side_cont yui-hidden">
							<c:set var="stepNum" value="2" scope="request" />
							<jsp:include page="toolbar.jsp" />
							<br />
							<table cellpadding="5px" style="vertical-align: middle" width="100%" >
								<tr>
								<td width="48%" align="center">
									<fieldset class="main_side_cont">
										<legend><span class="legend_label"><digi:trn key="rep:wizard:availableHierarchies">Available Hierarchies</digi:trn></span></legend>
										<ul id="source_hierarchies_ul" class="draglist">
										</ul>
									</fieldset>	
								</td>
								<td valign="middle" align="center">
									<button style="border: none;" type="button" onClick="MyDragAndDropObject.selectObjs('source_hierarchies_ul', 'dest_hierarchies_ul')">
										<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif"/>
									</button>
									<br/> <br />
									<button style="border: none;" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_hierarchies_ul', 'source_hierarchies_ul')">
										<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif"/>
									</button>
								</td>
								<td width="48%" align="center">
									<fieldset class="main_side_cont">
										<legend><span class="legend_label"><digi:trn key="rep:wizard:selectedHierarchies">Selected Hierarchies</digi:trn></span></legend>
										<ol id="dest_hierarchies_ul" class="draglist">
										</ol>
									</fieldset>
								</td>					
								</tr>
								<tr>
									<td colspan="3">
										<span id="hierarchiesMust" style="visibility: hidden;">
											<font color="red">* 
												<digi:trn key="rep:wizard:hint:notmorehierarchies">
													You cannot select more than 3 hierarchies
												</digi:trn>
											</font>
										</span>
									</td>
								</tr>
							</table>
						</div>
						<div id="measures_step_div" class="main_side_cont yui-hidden">
							<c:set var="stepNum" value="3" scope="request" />
							<jsp:include page="toolbar.jsp" />			
							<br />			
							<table cellpadding="5px" style="vertical-align: middle" width="100%">
								<tr>
									<td width="48%" align="center">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:availableMeasures">Available Measures</digi:trn></span></legend>
											<ul id="source_measures_ul" class="draglist">
												<jsp:include page="setMeasures.jsp" />
											</ul>
										</fieldset>
									</td>
									<td valign="middle"  align="center">
										<button style="border: none;" type="button" onClick="MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_right.gif"/>
										</button>
										<br/> <br />
										<button style="border: none;" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_measures_ul', 'source_measures_ul')">
											<img src="/TEMPLATE/ampTemplate/img_2/ico_arr_left.gif"/>
										</button>
									</td>
									<td width="48%" align="center">
										<fieldset class="main_side_cont">
											<legend><span class="legend_label"><digi:trn key="rep:wizard:selectedMeasures">Selected Measures</digi:trn></span></legend>
											<ul id="dest_measures_ul" class="draglist">
											</ul>
										</fieldset>
									</td>
								</tr>
								<tr>
									<td align="center" valign="top">
										<span id="measuresMust" style="visibility: visible">
											<font color="red">* 
												<digi:trn key="rep:wizard:hint:mustselectmeasure">
													Must select at least one measure
												</digi:trn>
											</font>
										</span>
									</td>
									<td>&nbsp;</td>
									<td align="center" valign="top">
										<span id="measuresLimit" style="visibility: hidden">
											<font color="red">* 
												<digi:trn key="rep:wizard:hint:limit2measures">
													You cannot select more than 2 measures in a desktop tab
												</digi:trn>
											</font>
										</span>
									</td>
								</tr>
							</table>
						</div>
					</div>
				</td>
				<td style="padding-top:35px;">
					<ul class="yui-nav" style="width: 70%; position: static;">
						<div class="tab selected" id="rtab">
							<div class="tab_cont">
								<span class="step_num">1</span>
								<a href="#type_step_div" class="l_big_b"><digi:trn>Report Details</digi:trn></a>
							</div>
						</div>
						<div class="tab" id="rtab">
							<div class="tab_cont">
								<span class="step_num">2</span>
								<a href="#columns_step_div" class="l_big_b"><digi:trn>Columns</digi:trn></a>
							</div>
						</div>
						<div class="tab" id="rtab">
							<div class="tab_cont">
								<span class="step_num">3</span>
								<a href="#hierarchies_step_div" class="l_big_b"><digi:trn>Hierarchies</digi:trn></a>
							</div>
						</div>
						<div class="tab" id="rtab">
							<div class="tab_cont">
								<span class="step_num">4</span>
								<a href="#measures_step_div" class="l_big_b"><digi:trn>Measures</digi:trn></a>
							</div>
						</div>
					</ul>
				</td>
			</tr>
			<tr>
				<div id="titlePanel" style="display: none">
					<div class="hd" style="font-size: 8pt">
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
</digi:form>
<!-- MAIN CONTENT PART END -->

<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>