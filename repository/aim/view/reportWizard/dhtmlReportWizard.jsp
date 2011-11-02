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
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-min.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/yahoo-dom-event.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/panel/container-min.js'/>" >.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/element/element-beta.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/event-min.js'/>">.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/animation-min.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dom-min.js'/>">.</script>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/tab/tabview.js'/>" >.</script>
	<%-- <script type="text/javascript" src=".<digi:file src='module/aim/scripts/logger/logger-min.js'/>">.</script> --%>
	<script language="JavaScript" type="text/javascript" src="<digi:file src='module/aim/scripts/ajaxconnection/connection-min.js'/>" > .</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/panel/dragdrop.js'/>" >.</script>
	
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/myDragAndDropObjects.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/reportManager.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/fundingGroups.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/saving.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/reportWizard/prefilters.js'/>" >.</script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/filters.js'/>" ></script>
	<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>	
	<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" > .</script>
	
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/tab/assets/tabview.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/border_tabs.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/reportWizard/reportWizard.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters.css'/>">
	<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters/filters2.css'/>">
	<!--[if IE]>
		<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/css/filters/filters2_IE_.css'/>">
	<![endif]-->
	<br>
	<br>
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
		
		YAHOO.amp.reportwizard.tabLabels	= new Array("reportdetails_tab_label", "columns_tab_label", 
											"hierarchies_tab_label", "measures_tab_label");
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
			treeObj.showHideNode(false,'dhtmlgoodies_tree');
// 			if (YAHOO.widget.Logger) {
// 				var reader = new YAHOO.widget.LogReader( "logDiv", 
// 							{ newestOnTop: true, height: "400px" } );
// 			}
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


<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="85%">
	<tr>

		<td valign="bottom">
			&nbsp;&nbsp;&nbsp;
			<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
			</c:set>
			<digi:link href="/../aim/viewMyDesktop.do" styleClass="comment" title="${translation}" >
               	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
               </digi:link> &gt; ${pageTitle}
			<br />
			<br />
		</td>
	</tr>
	<tr>
		<td align="left" vAlign="top">
		<digi:form action="/reportWizard.do" method="post">
		<span id="formChild" style="display:none;">&nbsp;</span>
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
		<html:hidden name="aimReportWizardForm" property="originalTitle"/>
		<div style="color: red; text-align: center; visibility: hidden" id="savingReportDiv">
		</div>
		<br />
		<div id="wizard_container" class="yui-navset">
		<ul class="yui-nav">
			<li id="reportdetails_tab_label" class="selected"><a href="#type_step_div"><div>${detailsStepName}</div></a> </li>
			<li id="columns_tab_label" class="disabled"><a href="#columns_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:columns">2. Columns</digi:trn></div></a> </li>
			<li id="hierachies_tab_label" class="disabled"><a href="#hierarchies_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:hierarchies">3. Hierarchies</digi:trn></div></a> </li>
			<li id="measures_tab_label" class="disabled"><a href="#measures_step_div"><div><digi:trn key="rep:wizard:dhtmlTab:measures">4. Measures</digi:trn></div></a> </li>
		</ul>
			<div class="yui-content" style="background-color: #EEEEEE">
				<div id="type_step_div" class="yui-tab-content" style="padding: 0px 0px 1px 0px;" >
					<c:set var="stepNum" value="0" scope="request" />
					<jsp:include page="toolbar.jsp" />
					<div style="height: 370px;">
					<br />
					<table cellpadding="15px" width="100%" align="center" border="0" >
						<tr>
							<td width="46%" style="vertical-align: top;">

								<span class="list_header">
									<digi:trn key="rep:wizard:fundingGrouping"> Funding Grouping </digi:trn>
								</span>
								<div align="center" id="reportGroupDiv" style="border: 1px solid gray; background-color: white; height: 140px;
											position: relative;">
								<table style="top: 17%; left: 1%; position: absolute;">
								<c:set var="pledges_type_const"><%=ArConstants.PLEDGES_TYPE%></c:set>
								<c:choose>
									<c:when test="${param.type==null || param.type!=pledges_type_const}">
								
									<feature:display name="Donor Report" module="Report Types">
                                             <tr>
                                               <td>
                                                 <html:radio property="reportType" disabled="${disableFundingType}" value="donor"  onclick="repManager.checkSteps()">
                                                   ${donorFunding}
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                             <feature:display name="Regional Report" module="Report Types">										
                                             <tr>
                                               <td>
                                                 <html:radio property="reportType" disabled="${disableFundingType}" value="regional"  onclick="repManager.checkSteps()">
                                                 	${regionalFunding}
                                                 </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                             <feature:display name="Component Report" module="Report Types">
                                             <tr>
                                               <td>
                                 	            <html:radio property="reportType" disabled="${disableFundingType}" value="component"  onclick="repManager.checkSteps()">
	                                 	            ${componentFunding}
                                                </html:radio>
                                               </td>
                                             </tr>
                                             </feature:display>
                                               <feature:display module="Report Types" name="Contribution Report">
                                                 <tr>
                                                   <td>
                                                     <html:radio property="reportType" disabled="${disableFundingType}" value="contribution"  onclick="repManager.checkSteps()">
                                                     	${activityContributions}
                                                     </html:radio>
                                                   </td>
                                                 </tr>
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
                                     	</table>
									<br />
								</div>

								<c:if test="${!myForm.desktopTab}">
									<span class="list_header">
										<digi:trn key="aim:reportBuilder:TotalsGrouping">Totals Grouping</digi:trn>
									</span>
									<div align="center" id="totalsGroupingDiv" style="border: 1px solid gray; background-color: white; vertical-align: bottom; width: 100%">
										<table>
										<tr>
											<td colspan="4" align="center" style="margin-bottom: 2px;margin-top: 2px">
												<html:checkbox property="hideActivities" value="true">
													${summary}
												</html:checkbox>
											</td>
										</tr>
										<tr>
											<td>
												<html:radio property="reportPeriod" value="A">
													<digi:trn key="aim:AnnualReport">
														Annual Report
													</digi:trn>
												</html:radio>
											</td>
									<c:if test="${param.type==null || param.type!=pledges_type_const}">
											<td>								
												<html:radio property="reportPeriod" value="Q">
													<digi:trn key="aim:QuarterlyReport">
														Quarterly Report
													</digi:trn>
												</html:radio>
											</td>
											<td>
												<html:radio property="reportPeriod" value="M">
													<digi:trn key="aim:MonthlyReport">
														Monthly Report
													</digi:trn>
												</html:radio>
											</td>
									</c:if>
										</tr>
										<tr>
											<td colspan="4" style="margin-top: 5px;margin-bottom: 5px">
												<html:radio property="reportPeriod" value="N">
												<digi:trn>
													Totals Only
												</digi:trn>
											</html:radio>
											</td>
										</tr>
										</table>
									</div>
								</c:if>
								
								<bean:define id="member" name="currentMember" scope="session" />
                  				
                  				
							<c:if test="${ (!myForm.desktopTab) || (member.teamHead==true && member.teamAccessType=='Management')}">
								<span class="list_header">
									<digi:trn key="aim:reportBuilder:Options">Options</digi:trn>
								</span>
								<div align="center" id="optionsDiv" style="border: 1px solid gray; background-color: white; vertical-align: bottom; width: 100%">
									<table>
										<feature:display  name="Public View Checkbox" module="Report and Tab Options">
											<c:if test="${member.teamHead == true && member.teamAccessType == 'Management'}">
											<tr>
											<td>
			                                    <html:checkbox property="publicReport"/>
			                                    <digi:trn key="aim:makePublic">
			                                        Make public
			                                    </digi:trn>
	                                    	</td>
	                                    	</tr>
	                                    	</c:if>
                                    	</feature:display>
                                    	<c:if test="${!myForm.desktopTab}">
                                    	<tr>
                                    	<td>
		                                    <html:checkbox property="allowEmptyFundingColumns"/>
		                                    <digi:trn key="rep:wizard:allowEmptyFundingCols">
		                                        Allow empty funding columns for year, quarter and month 
		                                    </digi:trn>
                                    	</td>
                                    	</tr>
                                    	</c:if>
                                    </table>
                                </div>
                               </c:if>
							</td>
							<td width="47%">
								<span class="list_header">
									${descriptionName}
								</span>
								<br/>
								<html:hidden property="reportDescription" />
								<textarea name="reportDescriptionClone" class="inp-text" style="border: 1px solid gray;width: 100%; height: 140px;">
									&nbsp;
								</textarea>
								<span class="list_header">
									<digi:trn key="rep:wizard:subtitle:selectedFilters">Selected Filters</digi:trn>
								</span>
								<br />
								<div id="listFiltersDiv" style="border: 1px solid gray; background-color: white; vertical-align: bottom; height: 120px; overflow: auto">
									<c:choose>
										<c:when test="${aimReportWizardForm.useFilters!=null && aimReportWizardForm.useFilters}">
											<jsp:include page="showSelectedFilters.jsp" />
											<c:set var="spanUseFiltersVisibility">visibility: visible</c:set>					
										</c:when>
										<c:otherwise>
											<c:set var="spanUseFiltersVisibility">visibility: hidden</c:set>
										</c:otherwise>
									</c:choose>
								</div>
								<span style="${spanUseFiltersVisibility}" id="spanUseFilters">
									<html:checkbox property="useFilters" styleId="useFiltersCheckbox" onclick="repManager.decideStrikeFilters()" /> 
									<digi:trn key="rep:wizard:useAboveFilters">Use above filters</digi:trn>
								</span>
							</td>
						</tr>
					</table>
					</div>
				</div>
				<div id="columns_step_div"  class="yui-tab-content" align="center" style="padding: 0px 0px 1px 0px; display: none;">
					<c:set var="stepNum" value="1"  scope="request" />
					<jsp:include page="toolbar.jsp" />
					<div style="height: 370px;">
					<table cellpadding="5px" style="vertical-align: middle" width="100%">
						<tr>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:availableColumns">Available Columns</digi:trn>
							</span>
							<div id="source_col_div" class="draglist">
								<jsp:include page="setColumns.jsp" />
							</div>
						</td>
						<td valign="middle" align="center">
							<button class="buton arrow" type="button" onClick="ColumnsDragAndDropObject.selectObjs('source_col_div', 'dest_col_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton arrow" type="button" onClick="ColumnsDragAndDropObject.deselectObjs('dest_col_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:selectedColumns">Selected Columns</digi:trn>
							</span>
							<ul id="dest_col_ul" class="draglist">
							
							</ul>
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
				</div>
				<div id="hierarchies_step_div"  class="yui-tab-content"  style="padding: 0px 0px 1px 0px; display: none;">
					<c:set var="stepNum" value="2" scope="request" />
					<jsp:include page="toolbar.jsp" />
					<div style="height: 370px;">
					<table cellpadding="5px" style="vertical-align: middle" width="100%" >
						<tr>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:availableHierarchies">Available Hierarchies</digi:trn>
							</span>
							<ul id="source_hierarchies_ul" class="draglist">
							</ul>
						</td>
						<td valign="middle" align="center">
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.selectObjs('source_hierarchies_ul', 'dest_hierarchies_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_hierarchies_ul', 'source_hierarchies_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:selectedHierarchies">Selected Hierarchies</digi:trn>
							</span>
							<ol id="dest_hierarchies_ul" class="draglist">
							</ol>
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
								<br>
								<span id="incompatiblehierarchies" style="visibility: hidden;">
								<font color="red">* 
									<digi:trn key="rep:wizard:hint:notmorehierarchies">
										You cannot Select Primary and Secondary Sectors as hierarchies
									</digi:trn>
								</font>
								</span>
							</td>
						</tr>
					</table>
					</div>
				</div>
				<div id="measures_step_div" class="yui-tab-content" style="padding: 0px 0px 1px 0px; display: none;" >
					<c:set var="stepNum" value="3" scope="request" />
					<jsp:include page="toolbar.jsp" />
					<div style="height: 370px;">
					<table cellpadding="5px" style="vertical-align: middle" width="100%">
						<tr>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:availableMeasures">Available Measures</digi:trn>
							</span>
							<ul id="source_measures_ul" class="draglist">
								<jsp:include page="setMeasures.jsp" />
							</ul>
						</td>
						<td valign="middle"  align="center">
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.selectObjs('source_measures_ul', 'dest_measures_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_right.gif"/>
							</button>
							<br/> <br />
							<button class="buton arrow" type="button" onClick="MyDragAndDropObject.deselectObjs('dest_measures_ul', 'source_measures_ul')">
								<img src="/TEMPLATE/ampTemplate/images/arrow_left.gif"/>
							</button>
						</td>
						<td width="47%" align="center">
							<span class="list_header">
								<digi:trn key="rep:wizard:selectedMeasures">Selected Measures</digi:trn>
							</span>
							<ul id="dest_measures_ul" class="draglist">
							</ul>
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
			</div>
		</div>
		<%-- <div id="logDiv" style="border: medium solid red; width: 20%;">
		</div> --%>
			
			<div id="titlePanel" style="display: none">
				<div class="hd" style="font-size: 8pt">
					${plsEnterTitle}
				</div>
				<div class="bd" id="titlePanelBody">
				<html:text onkeyup="repManager.checkSteps()" onkeypress="return saveReportEngine.checkEnter(event);" property="reportTitle" styleClass="inp-text" 
						style="border: 1px solid gray; width: 100%; font-size: 8pt; font-weight: bolder;" />
				</div>
				<div class="ft" align="right">
					<button id="last_save_button" type="button" class="buton repbuton" 
						style="color: lightgray" onclick="saveReportEngine.saveReport();" disabled="disabled">
							${saveBtn}
					</button>
					&nbsp;&nbsp;&nbsp;
				</div>
			</div>
		</digi:form>
	</td>
	</tr>
</table>

<%@ include file="/repository/aim/view/scripts/newCalendar.jsp"  %>
	